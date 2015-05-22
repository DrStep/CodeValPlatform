package cvp;

import cvp.DBService.LabsService;
import cvp.DBService.StudentService;
import cvp.DBService.tables.Labs;
import cvp.DBService.tables.Students;
import cvp.ResponseJSON.CodeRunResults;
import cvp.ResponseJSON.GroupStudents;
import cvp.ResponseJSON.LabsList;
import cvp.ResponseJSON.StudentResults;
import org.jruby.ir.operands.Hash;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cvp.DSL;

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

        /*StudentService dao = context.getBean(StudentService.class);
        Students one = new Students("Petrov", "IU6-81",10, "passed");
        Students two = new Students("Alexeev", "IU6-81", 7, "failed");
        Students three = new Students("Ivanov", "IU6-83", 8, "passed");*/


        System.out.println("!!!!!");
        dao.save(one);
        dao.save(two);
        dao.save(three);

    }

    @RequestMapping(value = "/labs/{student}", method = RequestMethod.GET)
    public LabsList getLabsResults(@PathVariable String student) {
        HashMap<String, ArrayList<HashMap>> resultHash = new HashMap<>();
        ArrayList<HashMap> allLabsArr = new ArrayList<>();
        LabsService labsServ = context.getBean(LabsService.class);
        List<Labs> labs = labsServ.getAllForStudent(student);
        if (labs.isEmpty()) {
            resultHash.put("empty", new ArrayList<HashMap>());
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

    @RequestMapping(value = "/students/{studName}", method = RequestMethod.GET)
    public StudentResults getStudentResults(@PathVariable String studName) {
        HashMap<String, String> resultHash = new HashMap<>();
        StudentService studServ = context.getBean(StudentService.class);
        List<Students> studentList = studServ.getResultForStudent(studName);
        if (studentList.isEmpty()) {
            resultHash.put("empty", "");
            return new StudentResults(resultHash);
        }

        Students student = studentList.get(0);
        resultHash.put("studName", student.getStudName());
        resultHash.put("group", student.getGroup());
        resultHash.put("labsCompleted", String.valueOf(student.getLabsCompleted()));
        resultHash.put("finalAssessment", student.getFinalAssessment());
        return new StudentResults(resultHash);
    }

    @RequestMapping(value = "/groups/{group}", method = RequestMethod.GET)
    public GroupStudents getGroupResults(@PathVariable String group) {
        ArrayList<HashMap<String, Object>> resultArr = new ArrayList<>();
        ArrayList<HashMap<String, String>> labsArr = new ArrayList<>();
        StudentService studServ = context.getBean(StudentService.class);
        LabsService labsServ = context.getBean(LabsService.class);

        List<Students> allStudents = studServ.getStudentsFromGroup(group);
        for (Students stud : allStudents) {
            HashMap<String, Object> studentResults = new HashMap<>();
            String studentName = stud.getStudName();
            List<Labs> labs = labsServ.getAllForStudent(studentName);

            studentResults.put("studName", studentName);
            studentResults.put("finalAssessment", stud.getFinalAssessment());
            for (Labs oneLab : labs) {
                HashMap<String, String> labHash = new HashMap<>();
                labHash.put("labName", oneLab.getLabName());
                labHash.put("assessment", oneLab.getAssessment());
                labHash.put("attempts", String.valueOf(oneLab.getAttempts()));
                labHash.put("time", oneLab.getTime());
                labsArr.add(labHash);
            }
            studentResults.put("labs", labsArr);
            resultArr.add(studentResults);
        }

        return new GroupStudents(resultArr);
    }

    @RequestMapping("/code")
    public CodeRunResults testHandler(@RequestParam("task") String task, @RequestParam("group") String group, @RequestParam("student") String student,@RequestParam("filename")String filename, @RequestParam("file")MultipartFile file) {
        HashMap<String, Object> result = new HashMap<>();
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
                HashMap<String, Object> testResult = (HashMap<String,Object>)testOne.run_all();

                //Server logs
                System.out.println(testResult);
                for (Map.Entry<String, Object> entry : testResult.entrySet()) {
                    String key = entry.getKey();
                    HashMap value = (HashMap)entry.getValue();
                    System.out.println("Key: " + key + " Values :" + value.toString());
                }

                result = testResult;
                result.put("error", false);
            } catch (Exception e) {
                result.put("error", true);
                result.put("message", "You failed to upload!");
            }
        } else {
            result.put("error", true);
            result.put("message", "You failed to upload because the file was empty.");
        }
        System.out.println(result);
        return new CodeRunResults(result);
    }

    String DEFAULT_TEACH_PATH = "resources/tasks/";
    String DEFAULT_STUD_PATH = "resources/students/";
}
