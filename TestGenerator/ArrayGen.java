

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.runtime.Helpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;


public class ArrayGen extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'open3'\n" +
            "require 'java'\n" +
            "\n" +
            "# Class that implements data generation for array-tasks\n" +
            "class ArrayGen\n" +
            "  attr_reader :length\n" +
            "  attr_reader :range\n" +
            "\n" +
            "  def initialize(length = 20, range = 100)\n" +
            "    @range = range.to_i\n" +
            "    @length = length.to_i\n" +
            "  end\n" +
            "\n" +
            "  def all_positive\n" +
            "    Array.new(@length) { rand(0..@range) }\n" +
            "  end\n" +
            "\n" +
            "  def all_negative\n" +
            "    Array.new(@length) { rand(-@range..0) }\n" +
            "  end\n" +
            "\n" +
            "  def all_zero\n" +
            "    Array.new(@length).fill(0)\n" +
            "  end\n" +
            "\n" +
            "  def mixed\n" +
            "    Array.new(@length) { rand(-@range..@range) }\n" +
            "  end\n" +
            "\n" +
            "  def all_the_same\n" +
            "    rand_numb = rand(-@range..@range)\n" +
            "    Array.new(@length).fill(rand_numb)\n" +
            "  end\n" +
            "\n" +
            "  def all_odd\n" +
            "  	result_arr = Array.new(@length) { rand(-@range..@range) }\n" +
            "  	result_arr.map do |var|\n" +
            "  	  if var.even?\n" +
            "  	    var = var + 1\n" +
            "  	  end\n" +
            "  	  var\n" +
            "  	end\n" +
            "  end\n" +
            "\n" +
            "  def not_divided_by_three\n" +
            "    result_arr = Array.new(@length) { rand(-@range..@range) }\n" +
            "  	result_arr.map do |var|\n" +
            "  	  if var%3 == 0\n" +
            "  	    var = var + 1\n" +
            "  	  end\n" +
            "  	  var\n" +
            "  	end\n" +
            "  end\n" +
            "\n" +
            "  def not_divided_by_five\n" +
            "    result_arr = Array.new(@length) { rand(-@range..@range) }\n" +
            "  	result_arr.map do |var|\n" +
            "  	  if var%5 == 0\n" +
            "  	    var = var + 1\n" +
            "  	  end\n" +
            "  	  var\n" +
            "  	end\n" +
            "  end\n" +
            "\n" +
            "  def gen_tests\n" +
            "  	result_map = {}\n" +
            "  	result_map[\"All values are positive\"] = all_positive.unshift(@length.to_i)\n" +
            "    result_map[\"All values are negative\"] = all_negative.unshift(@length.to_i)\n" +
            "    result_map[\"All values equal 0\"] = all_zero.unshift(@length.to_i)\n" +
            "    result_map[\"Mixed values\"] = mixed.unshift(@length.to_i)\n" +
            "    result_map[\"All the same\"] = all_the_same.unshift(@length.to_i)\n" +
            "    result_map[\"All values are odd\"] = all_odd.unshift(@length.to_i)\n" +
            "    result_map[\"All values don't divided by 3\"] = not_divided_by_three.unshift(@length.to_i)\n" +
            "    result_map[\"All values don't divided by 5\"] = not_divided_by_five.unshift(@length.to_i)\n" +
            "    result_map\n" +
            "  end\n" +
            "end").toString();
        __ruby__.executeScript(source, "ArrayGen.rb");
        RubyClass metaclass = __ruby__.getClass("ArrayGen");
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: ArrayGen");
        metaclass.setRubyStaticAllocator(ArrayGen.class);
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private ArrayGen(Ruby ruby, RubyClass metaclass) {
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
        return new ArrayGen(ruby, metaClass);
    }


    
    public  ArrayGen(Object length, Object range) {
        this(__ruby__, __metaclass__);
        IRubyObject ruby_arg_length = JavaUtil.convertJavaToRuby(__ruby__, length);
        IRubyObject ruby_arg_range = JavaUtil.convertJavaToRuby(__ruby__, range);
        Helpers.invoke(__ruby__.getCurrentContext(), this, "initialize", ruby_arg_length, ruby_arg_range);

    }

    
    public Object all_positive() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "all_positive");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object all_negative() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "all_negative");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object all_zero() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "all_zero");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object mixed() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "mixed");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object all_the_same() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "all_the_same");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object all_odd() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "all_odd");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object not_divided_by_three() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "not_divided_by_three");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object not_divided_by_five() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "not_divided_by_five");
        return (Object)ruby_result.toJava(Object.class);

    }

    
    public Object gen_tests() {

        IRubyObject ruby_result = Helpers.invoke(__ruby__.getCurrentContext(), this, "gen_tests");
        return (Object)ruby_result.toJava(Object.class);

    }

}
