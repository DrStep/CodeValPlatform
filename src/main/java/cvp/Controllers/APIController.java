package cvp.Controllers;

import cvp.DBService.LabsService;
import cvp.DBService.StudentService;
import cvp.DBService.tables.Labs;
import cvp.DBService.tables.Students;
import cvp.ResponseJSON.GroupStudents;
import cvp.ResponseJSON.LabsList;
import cvp.ResponseJSON.StudentResults;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stepa on 31.05.15.
 */
@RestController
public class APIController {
    
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
            oneLabHash.put("time", iter.getTime());
            oneLabHash.put("attempts", String.valueOf(iter.getAttempts()));
            oneLabHash.put("test", iter.getTest());
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
                labHash.put("test", oneLab.getTest());
                labHash.put("attempts", String.valueOf(oneLab.getAttempts()));
                labHash.put("time", oneLab.getTime());
                labsArr.add(labHash);
            }
            studentResults.put("labs", labsArr);
            resultArr.add(studentResults);
        }

        return new GroupStudents(resultArr);
    }

}
