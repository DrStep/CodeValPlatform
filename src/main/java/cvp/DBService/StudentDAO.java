package cvp.DBService;

import cvp.DBService.tables.Students;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by stepa on 20.05.15.
 */

@Repository("studentDAO")
@Transactional
public class StudentDAO {

    @PersistenceContext
    private EntityManager em;

    public StudentDAO() {

    }

    public Long save(Students student) {
        System.out.println("In StudentDAOImpl save");
        em.persist(student);
        return student.getId();
    }

    public List<Students> getAllFromGroup(String group) {
        System.out.println("select all from STUDENTS where GROUP_NUMB=" + group);
        return em.createQuery("select s from Students s where s.group=\'" + group + "\'", Students.class).getResultList();
    }
}
