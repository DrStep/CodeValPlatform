require 'open3'
require 'java'

# Class that implements data generation for array-tasks
class ArrayGen
  attr_reader :length
  attr_reader :range

  def initialize(length, range)
    @range = range.to_i
    @length = length.to_i
  end

  def all_positive
    Array.new(@length) { rand(0..@range) }
  end

  def all_negative
    Array.new(@length) { rand(-@range..0) }
  end

  def all_zero
    Array.new(@length).fill(0)
  end

  def mixed
    Array.new(@length) { rand(-@range..@range) }
  end

  def all_the_same
    rand_numb = rand(-@range..@range)
    Array.new(@length).fill(rand_numb)
  end

  def all_odd
  	result_arr = Array.new(@length) { rand(-@range..@range) }
  	result_arr.map do |var|
  	  if var.even?
  	    var = var + 1
  	  end
  	  var
  	end
  end

  def not_divided_by_three
    result_arr = Array.new(@length) { rand(-@range..@range) }
  	result_arr.map do |var|
  	  if var%3 == 0
  	    var = var + 1
  	  end
  	  var
  	end
  end

  def not_divided_by_five
    result_arr = Array.new(@length) { rand(-@range..@range) }
  	result_arr.map do |var|
  	  if var%5 == 0
  	    var = var + 1
  	  end
  	  var
  	end
  end

  def gen_tests
  	result_map = {}
  	result_map["All values are positive"] = all_positive.unshift(@length.to_i)
    result_map["All values are negative"] = all_negative.unshift(@length.to_i)
    result_map["All values equal 0"] = all_zero.unshift(@length.to_i)
    result_map["Mixed values"] = mixed.unshift(@length.to_i)
    result_map["All the same"] = all_the_same.unshift(@length.to_i)
    result_map["All values are odd"] = all_odd.unshift(@length.to_i)
    result_map["All values don't divided by 3"] = not_divided_by_three.unshift(@length.to_i)
    result_map["All values don't divided by 5"] = not_divided_by_five.unshift(@length.to_i)
    result_map
  end
end