require 'open3'
require 'java'
require "#{Dir.pwd}/lib/ArrayGen.jar"
import Java::ArrayGen
#java_package 'cvp'
java_package 'DSLLib'
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
  
  attr_reader :generated_tests
  attr_reader :generated_tests_type
  attr_reader :number_of_tests
  attr_reader :names_of_tests_data
  attr_reader :pass_expected
  attr_reader :pass_teachers

  attr_writer :timeout_for_inf

  def initialize(teachers_path, students_path)
    @timeout_for_inf = 10                 #10 seconds limit, more - infinite loop
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
      path = ""
      output_name = "stud_out"
      dockerIntr = "docker exec -i test timeout #{@timeout_for_inf}"     # timeout for cycle
      dockerNonIntr = "docker exec test"
    end
    path_to_exec_file = "#{path}#{output_name}"
    case @lang
    when '.c'
      if teachers
        cmd[:compile] = "gcc #{files_arr.join(' ')} -o #{path_to_exec_file}"
        cmd[:run] = "./#{path_to_exec_file}"
      else
        cmd[:compile] = "#{dockerNonIntr} gcc #{files_arr.join(' ')} -o #{path_to_exec_file}"
        cmd[:run] = "#{dockerIntr} ./#{path_to_exec_file}"
      end
    when '.pas'
      if teachers
        cmd[:compile] = "fpc #{files_arr.join(' ')}"
        cmd[:run] = "./#{files_arr[0]}"
      else
        cmd[:compile] = "#{dockerNonIntr} pc #{files_arr.join(' ')}"
        cmd[:run] = "#{dockerIntr} ./#{files_arr[0]}"
      end
    when '.rb'
      if teachers
        cmd[:run] = "#{dockerIntr} ruby #{files_arr[0]}"
      else 
        cmd[:run] = "#{dockerIntr} ruby #{files_arr[0]}"
      end  
    end
    return cmd
  end

  def generate_tests(type, length = 20, range = 100)
  	@generated_tests_type = "#{type}" 
  	case @generated_tests_type
  	when 'arr'
  	  generation_obj = ArrayGen.new length,range
  	  @generated_tests = generation_obj.gen_tests
  	  @generated_tests.each do |test_name, array|
        test test_name do
         	given array
         	expected
        end
  	  end
  	end
  end

  def test(name, &block)
    @test[name] = block
  end

  def given(*args)
    # @context : stdin, stdout, stderr, wait_thr
    @context[0].puts(args.join ' ') if args.any?
    @context[0].close
  end

  def expected(*args)
    # @context : stdin, stdout, stderr, wait_thr
    result = {}
    begin
      output = @context[1].gets.strip     # with trailing and leading whitespaces removed
    rescue Exception => e
      result[:result] = "-"
      result[:error] = true
      result[:message] = "Your console print is empty. \n"
      return result
    end

    result[:result] = output
    result[:error] = false
    if args.any?
      expected = args.join ' '
      if !output.eql?(expected)
        result[:message] = "Wrong answer! Excpected #{expected}, when the result is #{output} \n"
        result[:error] = true
        @pass_expected = false
      end
    end
    return result
  end

# function for tests runing
  def run_test(cmd)
    tests_result = {}
    if cmd[:compile]
      compile_result = compile_program cmd[:compile]
      if compile_result
        return { "compile_error" => compile_result, :error => true }
      end
    end
    @test.each do |name, block|
      start_time = (Time.now.to_f * 1000.0).to_i
      @context = Open3.popen3("#{cmd[:run]}")
      result = block.call(self)
      result[:taken_time] = (Time.now.to_f * 1000.0).to_i - start_time 
      if (result[:taken_time] >= @timeout_for_inf*1000)         
        result[:message] = "Overall timeout. Maybe infinite loop."
      elsif (result[:taken_time] > @time_lim)
        time_msg = "Your programm is running too long. #{@time_lim} ms expected and yours - #{result[:taken_time]} \n"
        if (result[:message])
          result[:message] += time_msg
        else
          result[:message] = time_msg
        end
        result[:error] = true
      end
      result[:tests_data] = @generated_tests[name]
      errors = @context[2].gets
      tests_result[name] = errors ? java.util.HashMap.new({ :result => errors, :error => true, :message => 'Error or warning!\n' }) : result
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

  def remove_container
    res = `docker stop test`
    `docker rm test`
  end

  # function that is called from Java-part for all tests
  def run_all

    teach_results = run_test teach_cmd
    puts("Teachers one: #{teach_results}")

    # run docker new container "test"
    `docker run --name test -v #{Dir.pwd}/#{@stud_path}:/home/test -w /home/test -d -t codeval`

    stud_results = run_test stud_cmd
    puts("Students one: #{stud_results}")

    if stud_results["compile_error"]
      remove_container
      return java.util.HashMap.new(stud_results)
    end

    stud_results.each do |test_name, test|
      if !(test[:error])
        if !test[:result].eql?(teach_results[test_name][:result]) 
          test[:error] = true
          test[:message] = "Aoutomatic generated test failed. Excpected #{teach_results[test_name][:result]}, when result is #{test[:result]}"
          @pass_teachers = false
        end
      else
        @pass_teachers = false
      end
      stud_results[test_name] = java.util.HashMap.new(test)
    end
    
    if @pass_teachers && @pass_expected
      stud_results["overall_result"] =  "passed"   
    else 
      stud_results["overall_result"] =  "failed"   
    end
    remove_container
    return java.util.HashMap.new(stud_results)
  end
end

#script code
#test = DSL.new('resources/tasks/arr/', '/Users/stepa/IdeaProjects/CodeValPlatform/resources/students/iu6-81/Alexeev/arr/')
#for i in 1..20
# res = test.run_all
#end