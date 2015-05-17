require 'open3'
require 'java'
require "#{Dir.pwd}/lib/ArrayGen.jar"
import Java::ArrayGen

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

  def initialize(teachers_path, students_path)
    @test_arr = []
    @teach_cmd = {}
    @stud_cmd = {}
    @test = {}
    @teach_path = teachers_path
    @stud_path = students_path
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
    else
      files_arr.map! { |file_name| @stud_path + file_name }
      path = @stud_path
    end
    case @lang
    when '.c'
      cmd[:compile] = "gcc #{files_arr.join(' ')}"
      cmd[:run] = "./a.out"
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
    if args.any?
      expected = args.join ' '
      if output.eql?(expected)
        full_result[:message] = 'Test passed!'
        full_result[:pass] = true
      else
        full_result[:message] = "Wrong answer! Excpected #{expected}, when the result is #{output}"
        full_result[:pass] = false
      end
    end
    return full_result
  end

# function for tests runing
  def run_test(cmd)
    tests_result = {}
    if cmd[:compile]
      compile_result = compile_program cmd[:compile]
      puts compile_result
      if compile_result
        return compile_result
      end
    end
    @test.each do |name, block|
      @context = Open3.popen3("#{cmd[:run]}")
      puts name
      result = block.call(self)
      errors = @context[2].gets
      tests_result[name] = errors ? errors : result
      @context[1..2].map(&:close)
      exit_status = @context[3].value
      sleep 0.5      
    end
    return tests_result
  end

  def compile_program(cmd)
    puts cmd
    Open3.popen3(cmd) do |stdin, stdout, stderr, wait_thr|
      error = stderr.read
      unless error.empty?
        return error
      end
    end
  end

  # function that is called from Java-part for all tests
  def run_all
    stud_results = run_test stud_cmd
    teach_results = run_test teach_cmd
    puts("Students one: #{stud_results}")
    puts("Teachers one: #{teach_results}")
    if stud_results[:result].eql?(teach_results[:result])
      puts('100% passed!')
    end
    return stud_results
  end
end

# script code
test = DSL.new('/Users/stepa/IdeaProjects/CodeValPlatform/resources/tasks/arr/', '/Users/stepa/IdeaProjects/CodeValPlatform/ruby_code/DSL/')
test.run_all