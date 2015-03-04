require 'open3'
require 'java'
require "#{Dir.pwd}/RubyTest.jar"
java_import Java::RubyTest

#Class that implements dsl
class DSL
	attr :lang
    attr :teach_files
	attr :stud_files
    attr :test_arr
    attr :time_lim
    attr :memory_lim

	attr :teach_cmd
	attr :stud_cmd

    def initialize
        @test_arr = []
        @teach_cmd = Hash.new
        @stud_cmd = {}
        @test = {}
        readConfig
    end
    
    def readConfig
      eval File.read('config')
    end

    def language lang
    	@lang = ".#{lang}"
    end

    def students_files *files_arr
    	@stud_files = files_arr.map { |file| file + @lang }
        @stud_cmd = cmd_create @stud_files
    end

    def teachers_files *files_arr
    	@teach_files = files_arr.map { |file| file + @lang }
        @teach_cmd = cmd_create @teach_files
    end

    def time_limit time_lim
    	@time_lim = time_lim
    end

    def memory_limit memory_lim
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

    def cmd_create files_arr
        cmd = Hash.new
        case @lang
            when ".c"
                cmd[:compile] = "gcc #{files_arr.join(" ")}"
                cmd[:run] = "./a.out"
            when ".p"
                cmd[:compile] = "pc #{files_arr.join(" ")}"
                cmd[:run] = "./#{files_arr[0]}"
            when ".rb"
                cmd[:run] = "ruby #{files_arr[0]}"
            end
        return cmd
    end

    def method_missing name, *test_data, &block
    	if name.to_s =~/^test(.+)$/
            @test_arr[test_arr.size] = test_data
    	else 
            puts "Method #{name} doesn't exists"
    	end
    end



#functions for runing tests
def run_test cmd, tests
    tests_result = []
    i = 0
    if (cmd[:compile])
        compile_result = compile_program cmd[:compile]
        if (compile_result)
           return compile_result 
        end
    end
#    tests.each do |test|
#        tests_result[i] = run_program cmd[:run], test
#        i += 1
#    end
  @test.each do |name, block|
    puts 'test: ' + name
    @context = *Open3.popen3("#{cmd[:run]} #{tests.join(' ')}")
    block.call(self)
    @context[0..2].map(&:close)
  end
    return tests_result
end
end

def compile_program cmd
    Open3.popen3(cmd) do |stdin, stdout, stderr, wait_thr|
        error = stderr.read
        if (!error.empty?)
            return error
        end
    end
end

def run_program cmd, *test
    Open3.popen3("#{cmd} #{test.join(" ")}") do |stdin, stdout, stderr, wait_thr|
        error = stderr.read
        output = stdout.read
        if (!error.empty?)
            return error
        else 
            return output
        end
    end
end

#script code
testCode = DSL.new

#result_teach = run_test testCode.teach_cmd, testCode.test_arr
result_stud = testCode.run_test testCode.stud_cmd, testCode.test_arr
puts "#{result_stud}"

