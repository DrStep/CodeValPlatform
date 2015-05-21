require 'open3'
require 'java'
require "#{Dir.pwd}/lib/ArrayGen.jar"
import Java::ArrayGen
java_package 'cvp'
#require "ruby_code/TestGenerator/ArrayGen"

# Class that implements dsl
class DSL
  attr_reader :lang
  attr_reader :teach_files
  attr_reader :stud_files
  attr_reader :stud_path
  attr_reader :teach_path
  attr_reader :test_arr
  attr_reader :time_lim
  attr_reader :memory_lim

  attr_reader :teach_cmd
  attr_reader :stud_cmd
  attr_reader :context
  attr_reader :test
  
  attr_reader :generated_tests_type
  attr_reader :number_of_tests
  attr_reader :names_of_tests_data
  attr_reader :pass_expected
  attr_reader :pass_teachers

  def initialize(teachers_path, students_path)
    @test_arr = []
    @teach_cmd = {}
    @stud_cmd = {}
    @test = {}
    @teach_path = teachers_path
    @stud_path = students_path
    @pass_expected = true
    @pass_teachers = true
    read_config
  end
    
  def read_config
    eval File.read(@teach_path + "config")
  end

  def language(lang)
    @lang = ".#{lang}"
  end

  def students_files(*files_arr)
    @stud_files = files_arr.map { |file| file + @lang }
    @stud_cmd = cmd_create @stud_files, false
  end

  def teachers_files(*files_arr)
    @teach_files = files_arr.map { |file| file + @lang }
    @teach_cmd = cmd_create @teach_files, true
  end

  def time_limit(time_lim)
    @time_lim = time_lim
  end

  def memory_limit(memory_lim)
    @memory_lim = memory_lim
  end

  def cmd_create(files_arr, teachers)
    cmd = {}
    if teachers
      files_arr.map! { |file_name| @teach_path + file_name }
      path = @teach_path
      output_name = "teach_out"
    else
      files_arr.map! { |file_name| @stud_path + file_name }
      path = @stud_path
      output_name = "stud_out"
    end
    path_to_exec_file = "#{path}#{output_name}"
    case @lang
    when '.c'
      cmd[:compile] = "gcc #{files_arr.join(' ')} -o #{path_to_exec_file}"
      cmd[:run] = "./#{path_to_exec_file}"
    when '.p'
      cmd[:compile] = "pc #{files_arr.join(' ')}"
      cmd[:run] = "./#{files_arr[0]}"
    when '.rb'
      cmd[:run] = "ruby #{files_arr[0]}"
    end
    return cmd
  end

  def generate_tests(type, length = 20, range = 100)
  	@generated_tests_type = "#{type}" 
  	case @generated_tests_type
  	when 'arr'
  	  generation_obj = ArrayGen.new length,range
  	  result_map = generation_obj.gen_tests
  	  result_map.each do |test_name, array|
        test test_name do |t|
         	t.in array
         	t.out
        end
  	  end
  	end
  end

  def test(name, &block)
    @test[name] = block
  end

  def in(*args)
    # @context : stdin, stdout, stderr, wait_thr
    @context[0].puts(args.join ' ') if args.any?
    @context[0].close
  end

  def out(*args)
    # @context : stdin, stdout, stderr, wait_thr
    full_result = {}
    output = @context[1].gets.strip       # with trailing and leading whitespaces removed
    full_result[:result] = output
    full_result[:pass_expected] = true
    full_result[:error] = false
    if args.any?
      expected = args.join ' '
      if output.eql?(expected)
        full_result[:message] = "Test passed! \n"
      else
        full_result[:message] = "Wrong answer! Excpected #{expected}, when the result is #{output} \n"
        full_result[:pass_expected] = false
        @pass_expected = false
      end
    end
    return full_result
  end

# function for tests runing
  def run_test(cmd)
    tests_result = {}
    if cmd[:compile]
      compile_result = compile_program cmd[:compile]
      if compile_result
        return { :result => compile_result, :compile_error => true }
      end
    end
    @test.each do |name, block|
      puts name
      start_time = (Time.now.to_f * 1000.0).to_i
      @context = Open3.popen3("#{cmd[:run]}")
      result = block.call(self)
      result[:taken_time] = (Time.now.to_f * 1000.0).to_i - start_time
      if (result[:taken_time] > @time_limit)
        result[:message] += "Your programm is running too long. #{@time_limit} ms expected and yours - #{result[:taken_time]}"

      errors = @context[2].gets
      tests_result[name] = errors ? java.util.HashMap.new({ :result => errors, :error => true, :message => 'Error or warning!' }) : result
      @context[1..2].map(&:close)
      exit_status = @context[3].value
    end
    return tests_result
  end

  def compile_program(cmd)
    Open3.popen3(cmd) do |stdin, stdout, stderr, wait_thr|
      error = stderr.read
      unless error.empty?
        return error
      end
    end
  end

  # function that is called from Java-part for all tests
  def run_all
    full_res = {}
    stud_results = run_test stud_cmd
    teach_results = run_test teach_cmd
    puts("Teachers one: #{teach_results}")

    if stud_results[:compile_error]
      return java.util.HashMap.new({ "compile" => java.util.HashMap.new(stud_results) })
    end

    stud_results.each do |test_name, test|
      if !(test[:error])
        if test[:result].eql?(teach_results[test_name][:result]) 
          test[:pass_teachers] = true
        else 
          test[:pass_teachers] = false
          @pass_teachers = false
        end
      else
        @pass_teachers = false
      end
      stud_results[test_name] = java.util.HashMap.new(test)
    end
    
    if @pass_teachers && @pass_expected
      stud_results["full_res"] =  java.util.HashMap.new({ :passed => true })   
    else 
      stud_results["full_res"] =  java.util.HashMap.new({ :passed => false })    
    end
    puts("Students one: #{stud_results}")
    return java.util.HashMap.new(stud_results)
  end
end

# script code
#test = DSL.new('resources/tasks/arr/', 'ruby_code/DSL/')
#res = test.run_all