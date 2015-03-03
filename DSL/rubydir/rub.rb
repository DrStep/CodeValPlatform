require 'open3'
require 'java'
require "/Users/stepa/IdeaProjects/RubyTest/rubydir/RubyTest.jar"
java_import Java::RubyTest

class DSL
	attr :lang
	attr :files_arr
	attr :time_lim
	attr :memory_lim
	attr :compile_cmd
	attr :run_cmd
	attr :need_compile

    def initialize
    	@need_compile = true
        readConfig
    end
    
    def readConfig
		File.readlines('config' ).each do |line|
  			eval(line)
		end
	end

    def language lang
    	@lang = ".#{lang}"
    end

    def files *files_arr
    	@files_arr = files_arr.map { |file| file + @lang }

    	case @lang
    	when ".c"
    		@compile_cmd = "gcc #{@files_arr.join(" ")}"
    		@run_cmd = "./a.out"
    	when ".p"
    		@compile_cmd = "pc #{@files_arr.join(" ")}"
    		@run_cmd = "./#{files_arr[0]}"
    	when ".rb"
 			@run_cmd = "ruby #{@files_arr[0]}"
 			@need_compile = false
 		end
    end

    def teach_files *files_arr
    	file
    end

    def time_limit time_lim
    	@time_lim = time_lim
    end

    def memory_limit memory_lim
    	@memory_lim = memory_lim
    end

    def method_missing name, *args, &block
    	if name.to_s =~/^test(.+)$/
    		res = run_test name,*args,&block
    	else if name.to_s =~/^files_(.+)$/
    		file *args
    	end
        
    end

    def run_test method, *args, &block

    	if (@need_compile)

    		compile_result = compile_program @compile_cmd
    		if (compile_result)
    			return compile_result 
    		end
    	end
    	run_program @run_cmd
    end

    def compile_program cmd
    	Open3.popen3(cmd) do |stdin, stdout, stderr, wait_thr|
			error = stderr.read
			if (!error.empty?)
				return error
			end
		end
    end

    def run_program cmd
    	Open3.popen3(@run_cmd) do |stdin, stdout, stderr, wait_thr|
			error = stderr.read
			puts error
		end
    end

end

rb1 = DSL.new


#IO.popen('gcc main.c', 'r') do |pipe|
#	build_res = pipe.read
#end

#if (build_res == '')
#	IO.popen('./a.out', 'r') do |result|
#		puts result.read
#	end
#else 
#	puts 'Builds error'
#end

