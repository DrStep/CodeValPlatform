

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.runtime.Helpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;

import org.jruby.Ruby;
import org.jruby.javasupport.Java;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.Block;
import org.jruby.runtime.GlobalVariable;
import org.jruby.runtime.builtin.IRubyObject;

public class DSL extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'open3'\n" +
            "require 'java'\n" +
            "#require \"#{Dir.pwd}/RubyTest.jar\"\n" +
            "#java_import Java::RubyTest\n" +
            "\n" +
            "# Class that implements dsl\n" +
            "class DSL\n" +
            "  attr_reader :lang\n" +
            "  attr_reader :teach_files\n" +
            "  attr_reader :stud_files\n" +
            "  attr_reader :test_arr\n" +
            "  attr_reader :time_lim\n" +
            "  attr_reader :memory_lim\n" +
            "\n" +
            "  attr_reader :teach_cmd\n" +
            "  attr_reader :stud_cmd\n" +
            "  attr_reader :context\n" +
            "  attr_reader :test\n" +
            "\n" +
            "  def initialize(path)\n" +
            "    @test_arr = []\n" +
            "    @teach_cmd = {}\n" +
            "    @stud_cmd = {}\n" +
            "    @test = {}\n" +
            "    read_config  path\n" +
            "  end\n" +
            "    \n" +
            "  def read_config(path)\n" +
            "    eval File.read(path)\n" +
            "  end\n" +
            "\n" +
            "  def language(lang)\n" +
            "    @lang = \".#{lang}\"\n" +
            "  end\n" +
            "\n" +
            "  def students_files(*files_arr)\n" +
            "    @stud_files = files_arr.map { |file| file + @lang }\n" +
            "    @stud_cmd = cmd_create @stud_files\n" +
            "  end\n" +
            "\n" +
            "  def teachers_files(*files_arr)\n" +
            "    @teach_files = files_arr.map { |file| file + @lang }\n" +
            "    @teach_cmd = cmd_create @teach_files\n" +
            "  end\n" +
            "\n" +
            "  def time_limit(time_lim)\n" +
            "    @time_lim = time_lim\n" +
            "  end\n" +
            "\n" +
            "  def memory_limit(memory_lim)\n" +
            "    @memory_lim = memory_lim\n" +
            "  end\n" +
            "\n" +
            "  def test(name, &block)\n" +
            "    @test[name] = block\n" +
            "  end\n" +
            "\n" +
            "  def in(*args)\n" +
            "    # @context : stdin, stdout, stderr, wait_thr\n" +
            "    @context[0].puts(args.join ' ') if args.any?\n" +
            "    @context[0].close\n" +
            "  end\n" +
            "\n" +
            "  def out(*args)\n" +
            "    # @context : stdin, stdout, stderr, wait_thr\n" +
            "    full_result = {}\n" +
            "    output = @context[1].gets.strip       # with trailing and leading whitespaces removed\n" +
            "    full_result[:result] = output\n" +
            "    if args.any?\n" +
            "      expected = args.join ' '\n" +
            "      if output.eql?(expected)\n" +
            "        full_result[:message] = 'Test passed!'\n" +
            "        full_result[:pass] = true\n" +
            "      else\n" +
            "        full_result[:message] = \"Wrong answer! Excpected #{expected}, when the result is #{output}\"\n" +
            "        full_result[:pass] = false\n" +
            "      end\n" +
            "    end\n" +
            "    return full_result\n" +
            "  end\n" +
            "\n" +
            "  def cmd_create(files_arr)\n" +
            "    cmd = {}\n" +
            "    case @lang\n" +
            "    when '.c'\n" +
            "      cmd[:compile] = \"gcc #{files_arr.join(' ')}\"\n" +
            "      cmd[:run] = './a.out'\n" +
            "    when '.p'\n" +
            "      cmd[:compile] = \"pc #{files_arr.join(' ')}\"\n" +
            "      cmd[:run] = './#{files_arr[0]}'\n" +
            "    when '.rb'\n" +
            "      cmd[:run] = \"ruby #{files_arr[0]}\"\n" +
            "    end\n" +
            "    return cmd\n" +
            "  end\n" +
            "\n" +
            "# function for tests runing\n" +
            "  def run_test(cmd)\n" +
            "    tests_result = {}\n" +
            "    if cmd[:compile]\n" +
            "      compile_result = compile_program cmd[:compile]\n" +
            "      if compile_result\n" +
            "        return compile_result\n" +
            "      end\n" +
            "    end\n" +
            "    @test.each do |name, block|\n" +
            "      @context = *Open3.popen3(\"#{cmd[:run]}\")\n" +
            "      result = block.call(self)\n" +
            "      errors = @context[2].gets\n" +
            "      tests_result[name] = errors ? errors : result\n" +
            "      @context[1..2].map(&:close)\n" +
            "    end\n" +
            "    return tests_result\n" +
            "  end\n" +
            "\n" +
            "  def compile_program(cmd)\n" +
            "    Open3.popen3(cmd) do |stdin, stdout, stderr, wait_thr|\n" +
            "      error = stderr.read\n" +
            "      unless error.empty?\n" +
            "        return error\n" +
            "      end\n" +
            "    end\n" +
            "  end\n" +
            "\n" +
            "  # function that is called from Java-part for all tests\n" +
            "  def run_all\n" +
            "    stud_results = run_test stud_cmd\n" +
            "    teach_results = run_test teach_cmd\n" +
            "    puts(\"Students one: #{stud_results}\")\n" +
            "    puts(\"Teachers one: #{teach_results}\")\n" +
            "    if stud_results[:result].eql?(teach_results[:result])\n" +
            "      puts('100% passed!')\n" +
            "    end\n" +
            "    return stud_results\n" +
            "  end\n" +
            "end\n" +
            "\n" +
            "\n" +
            "# script code\n" +
            "#test = DSL.new('/Users/stepa/IdeaProjects/Ccheck/config')\n" +
            "#test.run_all\n" +
            "").toString();
        __ruby__.executeScript(source, "DSL.rb");
        RubyClass metaclass = __ruby__.getClass("DSL");
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: DSL");
        metaclass.setRubyStaticAllocator(DSL.class);
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private DSL(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * A static method used by JRuby for allocating instances of this object
     * from Ruby. Generally not for user comsumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public static IRubyObject __allocate__(Ruby ruby, RubyClass metaClass) {
        return new DSL(ruby, metaClass);
    }


    
    public  DSL(Object path) {
        this(__ruby__, __metaclass__);
        IRubyObject ruby_arg_path = JavaUtil.convertJavaToRuby(__ruby__, path);
        Helpers.invoke(__ruby__.getCurrentContext(), this, "initialize", ruby_arg_path);

    }

    
    public Object read_config(Object path) {
        IRubyObject ruby_arg_path = JavaUtil.convertJavaToRuby(__ruby__, path);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "read_config", ruby_arg_path);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object language(Object lang) {
        IRubyObject ruby_arg_lang = JavaUtil.convertJavaToRuby(__ruby__, lang);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "language", ruby_arg_lang);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object students_files(Object files_arr) {
        IRubyObject ruby_arg_files_arr = JavaUtil.convertJavaToRuby(__ruby__, files_arr);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "students_files", ruby_arg_files_arr);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object teachers_files(Object files_arr) {
        IRubyObject ruby_arg_files_arr = JavaUtil.convertJavaToRuby(__ruby__, files_arr);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "teachers_files", ruby_arg_files_arr);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object time_limit(Object time_lim) {
        IRubyObject ruby_arg_time_lim = JavaUtil.convertJavaToRuby(__ruby__, time_lim);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "time_limit", ruby_arg_time_lim);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object memory_limit(Object memory_lim) {
        IRubyObject ruby_arg_memory_lim = JavaUtil.convertJavaToRuby(__ruby__, memory_lim);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "memory_limit", ruby_arg_memory_lim);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object test(Object name, Object block) {
        IRubyObject ruby_arg_name = JavaUtil.convertJavaToRuby(__ruby__, name);
        IRubyObject ruby_arg_block = JavaUtil.convertJavaToRuby(__ruby__, block);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "test", ruby_arg_name, ruby_arg_block);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object in(Object args) {
        IRubyObject ruby_arg_args = JavaUtil.convertJavaToRuby(__ruby__, args);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "in", ruby_arg_args);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object out(Object args) {
        IRubyObject ruby_arg_args = JavaUtil.convertJavaToRuby(__ruby__, args);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "out", ruby_arg_args);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object cmd_create(Object files_arr) {
        IRubyObject ruby_arg_files_arr = JavaUtil.convertJavaToRuby(__ruby__, files_arr);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "cmd_create", ruby_arg_files_arr);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object run_test(Object cmd) {
        IRubyObject ruby_arg_cmd = JavaUtil.convertJavaToRuby(__ruby__, cmd);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "run_test", ruby_arg_cmd);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object compile_program(Object cmd) {
        IRubyObject ruby_arg_cmd = JavaUtil.convertJavaToRuby(__ruby__, cmd);
        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "compile_program", ruby_arg_cmd);
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object run_all() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "run_all");
        return (Object)ruby_result.toJava(Object.class);

    }

}
