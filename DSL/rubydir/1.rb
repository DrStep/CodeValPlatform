class Numeric
    def ghz
        self*1000
    end

    def gb
        self*1024
    end

    def mhz
        self
    end

    def mb
        self
    end
end

class Computer
    def initialize &block
        instance_eval &block
    end

    def method_missing name, *args, &block
        instance_variable_set("@#{name}".to_sym, args[0])
        self.class.send(:define_method, name, proc { instance_variable_get("@#{name}")}) 
    end
end

comp = Computer.new do
    cpu 2.2.ghz
    ram 2.gb
    disk 1.gb
    bios 0.5.mb
    bus 100
end

puts comp.cpu
puts comp.ram
puts comp.disk
puts comp.bios
puts comp.bus

prog_language "cpp"
dir "iu6-31/Petrov"
files ["main.cpp", "func.cpp"]
test1/2/3/.../oo  [[1,5,3], [10,34,22]]
time_limit 14.2
memory_limit 200