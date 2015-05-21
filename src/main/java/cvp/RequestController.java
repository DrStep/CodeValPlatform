package cvp;

import cvp.DBService.LabsService;
import cvp.DBService.StudentService;
import cvp.DBService.tables.Labs;
import cvp.DBService.tables.Students;
import org.jruby.ir.operands.Hash;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import DSL.DSL;
import org.yecht.Data;

import javax.validation.constraints.Null;

/**
 * Created by stepa on 12.01.15.
 */

@RestController
public class RequestController {

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @RequestMapping("/testDB")
    public void dbHandler() {
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        LabsService dao = context.getBean(LabsService.class);
        Labs one = new Labs("Array", "Petrov", 10, "01:12:31", "passed");
        Labs two = new Labs("String", "Petrov", 7, "00:07:31", "failed");
        Labs three = new Labs("Array", "Ivanov", 11, "00:22:01", "passed");

        System.out.println("!!!!!");
        dao.save(one);
        dao.save(two);
        dao.save(three);

        List<Labs> result = dao.getAllForStudent("Petrov");

        for (Labs iter : result) {
            System.out.println(iter.getLabName());
        }
    }

    @RequestMapping(value = "/students/{student}", method = RequestMethod.GET)
    public LabsList getStudentResults(@PathVariable String student) {
        HashMap<String, ArrayList<HashMap>> resultHash = new HashMap<>();
        ArrayList<HashMap> allLabsArr = new ArrayList<>();
        LabsService labsServ = context.getBean(LabsService.class);
        List<Labs> labs = labsServ.getAllForStudent(student);
        if (labs.isEmpty()) {
            resultHash.put("Empty", new ArrayList<HashMap>());
            return new LabsList(resultHash);
        }

        for (Labs iter : labs) {
            HashMap<String, String> oneLabHash =new HashMap<>();
            oneLabHash.put("labName", iter.getLabName());
            oneLabHash.put("timeDoing", iter.getTime());
            oneLabHash.put("attempts", String.valueOf(iter.getAttempts()));
            oneLabHash.put("assessment", iter.getAssessment());
            allLabsArr.add(oneLabHash);
        }
        resultHash.put("labs", allLabsArr);
        return new LabsList(resultHash);
    }

    @RequestMapping("/code")
    public TestClass testHandler(@RequestParam("task") String task, @RequestParam("group") String group, @RequestParam("student") String student,@RequestParam("filename")String filename, @RequestParam("file")MultipartFile file) {
        String result;
        String teach_path = DEFAULT_TEACH_PATH + task + '/';
        String stud_path = DEFAULT_STUD_PATH + group + '/' + student + '/' + task + '/';
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File dir = new File(stud_path);
                if (!dir.exists())
                    dir.mkdirs();
                File upload = new File(dir, filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(upload));
                stream.write(bytes);
                stream.close();
                DSL testOne = new DSL(teach_path, stud_path);
                HashMap<String, HashMap> testResult = (HashMap<String,HashMap>)testOne.run_all();
                System.out.println(testResult);
                for (Map.Entry<String, HashMap> entry : testResult.entrySet()) {
                    String key = entry.getKey();
                    HashMap value = entry.getValue();
                    System.out.println("Key: " + key + " Values :" + value.toString());
                }

                result = "You successfully uploaded!";
            } catch (Exception e) {
                result = "You failed to upload => " + e.toString();
            }
        } else {
            result = "You failed to upload because the file was empty.";
        }
        System.out.println(result);
        return new TestClass(result);
    }

    String DEFAULT_TEACH_PATH = "resources/tasks/";
    String DEFAULT_STUD_PATH = "resources/students/";
}
