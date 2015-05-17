import org.jruby.RubyProcess;
import org.mortbay.util.ajax.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.jruby.Ruby;
import org.jruby.javasupport.Java;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.Block;
import org.jruby.runtime.GlobalVariable;
import org.jruby.runtime.builtin.IRubyObject;


import java.io.*;
import java.util.HashMap;

/**
 * Created by stepa on 12.01.15.
 */

@RestController
public class RequestController {

    @RequestMapping("/test")
    public TestClass  testHandler(@RequestParam("task") String task, @RequestParam("group") String group, @RequestParam("student") String student,@RequestParam("file")MultipartFile file) {
        String result;
        String teach_path = DEFAULT_TEACH_PATH + task + '/';
        String stud_path = DEFAULT_STUD_PATH + task + '/';
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File upload = new File("Uploaded");
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(upload));
                stream.write(bytes);
                stream.close();
                DSL testOne = new DSL(teach_path, stud_path);
                HashMap<String, String> testResult = (HashMap<String,String>)testOne.run_all();
                System.out.println(testResult);
                result = "You successfully uploaded!";
                System.out.println(new String(bytes));
            } catch (Exception e) {
                result = "You failed to upload => " + e.toString();
            }
        } else {
            result = "You failed to upload because the file was empty.";
        }
        System.out.println(result);
        return new TestClass(result);
    }

    String DEFAULT_TEACH_PATH = "/Users/stepa/IdeaProjects/CodeValPlatform/resources/tasks/";
    String DEFAULT_STUD_PATH = "/Users/stepa/IdeaProjects/CodeValPlatform/resources/tasks/";
}
