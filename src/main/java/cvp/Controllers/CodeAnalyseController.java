package cvp.Controllers;

import cvp.DBService.LabsService;
import cvp.DBService.StudentService;
import cvp.DBService.tables.Labs;
import cvp.DBService.tables.Students;
import cvp.DSL;
import cvp.ResponseJSON.CodeRunResults;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stepa on 31.05.15.
 */
@RestController
public class CodeAnalyseController {

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @RequestMapping("/code")
    public CodeRunResults codeHandler(@RequestParam("task") String task, @RequestParam("group") String group,
                                      @RequestParam("student") String studName,@RequestParam("filename")String filename,
                                      @RequestParam("labsCount") int labsCount,@RequestParam("file")MultipartFile file) {
        HashMap<String, Object> result = new HashMap<>();
        String teach_path = DEFAULT_TEACH_PATH + task + '/';
        String stud_path = DEFAULT_STUD_PATH + group + '/' + studName + '/' + task + '/';

        //save file
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

                // running DSL for results
                DSL testOne = new DSL(teach_path, stud_path);
                HashMap<String, Object> testResult = (HashMap<String,Object>)testOne.run_all();

                LabsService labsServ = context.getBean(LabsService.class);
                StudentService studServ = context.getBean(StudentService.class);

                if (testResult.get("compile_error") != null) {
                    labsServ.updateLabs(studName, task, "failed");
                    result.put("error", true);
                    result.put("message", testResult.get("compile_error"));
                    return new CodeRunResults(result);
                }

                HashMap<String, Object> overallHash = (HashMap<String, Object>)testResult.get("overall_result");
                String labResult = overallHash.get("test").toString();

                //save to DataBase lab results, create student if don't exists in STUDENTS
                Students student;
                List<Students> studentRes = studServ.getResultForStudent(studName);
                if (studentRes.isEmpty()) {
                    student = new Students(studName, group, 0, "failed");
                    studServ.save(student);
                } else {
                    student = studentRes.get(0);
                }

                List<Labs> labToUpdate = labsServ.getLabForStudent(studName, task);
                if (labToUpdate.isEmpty()) {
                    Labs labNew = new Labs(task, studName, 1, String.valueOf(0), labResult);
                    labsServ.save(labNew);
                    if (labResult.equals("passed")) {
                        studServ.updateStudent(studName, student.getLabsCompleted(), labsCount);
                    }
                } else {
                    Labs labUpd = labToUpdate.get(0);
                    if (!labUpd.getTest().equals("passed"))
                        labsServ.updateLabs(studName, task, labResult);
                }

                result = testResult;
                result.put("error", false);
            } catch (Exception e) {
                System.out.println("Error: " + e.toString());
                result.put("error", true);
                result.put("message", "You failed to upload!");
            }
        } else {
            result.put("error", true);
            result.put("message", "You failed to upload, because uploaded file was empty.");
        }
        return new CodeRunResults(result);
    }

    String DEFAULT_TEACH_PATH = "resources/tasks/";
    String DEFAULT_STUD_PATH = "resources/students/";
}
