package cvp.DBService;

import cvp.DBService.tables.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by stepa on 21.05.15.
 */

@Component("studentService")
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    public Long save(Students student) {
        Long id;
        id = studentDAO.save(student);
        return id;
    }

    public List<Students> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public List<Students> getStudentsFromGroup(String group) {
        return studentDAO.getStudentsFromGroup(group);
    }

    public List<Students> getResultForStudent(String student) {
        System.out.println("In StudentDAOImpl getAllFromGroup");
        return studentDAO.getResultForStudent(student);
    }
}
