package cvp.DBService;

import cvp.DBService.tables.Students;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yecht.Data;

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
        em.persist(student);
        return student.getId();
    }

    public void updateLabsCompleted(String studName) {
        em.createQuery("update Students s set s.labsCompleted = s.labsCompleted+1  "
                + "where s.studName=:studName")
                .setParameter("studName", studName)
                .executeUpdate();
    }

    public void updateFinalAssessment(String studName) {
        em.createQuery("update Students s set s.finalAssessment=\'passed\'  "
                + "where s.studName=:studName")
                .setParameter("studName", studName)
                .executeUpdate();
    }

    public List<Students> getAllStudents() {
        return em.createQuery("from Students", Students.class).getResultList();
    }

    public List<Students> getStudentsFromGroup(String group) {
        return em.createQuery("select s from Students s where s.group=:group", Students.class)
                .setParameter("group", group).getResultList();
    }

    public List<Students> getResultForStudent(String student) {
        return em.createQuery("select s from Students s where s.studName=:studName", Students.class)
                .setParameter("studName", student).getResultList();
    }
}
