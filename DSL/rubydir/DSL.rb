require 'open3'
require 'java'
require "#{Dir.pwd}/RubyTest.jar"
java_import Java::RubyTest

# Class that implements dsl
class DSL
  attr_reader :lang
  attr_reader :teach_files
  attr_reader :stud_files
  attr_reader :test_arr
  attr_reader :time_lim
  attr_reader :memory_lim

  attr_reader :teach_cmd
  attr_reader :stud_cmd
  attr_reader :context
  attr_reader :test

  def initialize
    @test_arr = []
    @teach_cmd = {}
    @stud_cmd = {}
    @test = {}
    read_config
  end
    
  def read_config
    eval File.read('config')
  end

  def language(lang)
    @lang = ".#{lang}"
  end

  def students_files(*files_arr)
    @stud_files = files_arr.map { |file| file + @lang }
    @stud_cmd = cmd_create @stud_files
    puts @stud_cmd
  end

  def teachers_files(*files_arr)
    @teach_files = files_arr.map { |file| file + @lang }
    @teach_cmd = cmd_create @teach_files
  end

  def time_limit(time_lim)
    @time_lim = time_lim
  end

  def memory_limit(memory_lim)
    @memory_lim = memory_lim
  end

  def test(name, &block)
    @test[name] = block
  end

  def in(*args)
    # @context : stdin, stdout, stderr, wait_thr
    @context[0].puts(args.join ' ') if args.any?
  end

  def out(*args)
    # @context : stdin, stdout, stderr, wait_thr
    if args.any?
      result = @context[1].gets
      expected = args.join ' '

      p result, expected
    end
  end

  def cmd_create(files_arr)
    cmd = {}
    case @lang
    when '.c'
      cmd[:compile] = "gcc #{files_arr.join(' ')}"
      cmd[:run] = './a.out'
    when '.p'
      cmd[:compile] = "pc #{files_arr.join(' ')}"
      cmd[:run] = './#{files_arr[0]}'
    when '.rb'
      cmd[:run] = "ruby #{files_arr[0]}"
    end
    return cmd
  end

  def method_missing(name, *test_data)
    if name.to_s =~ /^test(.+)$/
      @test_arr[test_arr.size] = test_data
    else
      puts "Method #{name} doesn't exists"
    end
  end

# functions for runing tests
  def run_test(cmd, tests)
    tests_result = []
    if cmd[:compile]
      compile_result = compile_program cmd[:compile]
      if compile_result
        return compile_result
      end
    end
    @test.each do |name, block|
      puts 'test: ' + name
      @context = *Open3.popen3("#{cmd[:run]} #{tests.join(' ')}")
      block.call(self)
      @context[0..2].map(&:close)
    end
    return tests_result
  end
end

def compile_program(cmd)
  Open3.popen3(cmd) do |stdin, stdout, stderr, wait_thr|
    error = stderr.read
    if !error.empty?
      return error
    end
  end
end

def run_program(cmd, *test)
  Open3.popen3("#{cmd} #{test.join(' ')}") do |stdin, stdout, stderr, wait_thr|
    error = stderr.read
    output = stdout.read
    if !error.empty?
      return error
    else 
      return output
    end
  end
end

# script code
test_code = DSL.new

result_stud = test_code.run_test test_code.stud_cmd, test_code.test_arr
puts "#{result_stud}"
