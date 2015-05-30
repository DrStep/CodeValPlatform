package cvp;

/**
 * Created by stepa on 26.12.14.
 */

import org.apache.tomcat.jni.Proc;
import org.jruby.RubyProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Main {

    public static void main(String[] args) {

        //run spring
        SpringApplication.run(Main.class, args);
    }
}
