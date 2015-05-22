package cvp.DBService;

import cvp.DBService.tables.Labs;
import cvp.DBService.tables.Students;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by stepa on 21.05.15.
 */

@Repository("labsDAO")
@Transactional
public class LabsDAO {

    @PersistenceContext
    private EntityManager em;

    public LabsDAO() {

    }

    public Long save(Labs lab) {
        System.out.println("In LabsDAO save");
        em.persist(lab);
        return lab.getId();
    }

    public void updateLabAttempts(String studName, String labName) {
        em.createQuery("update Labs l set l.attempts=l.attemprs+1 "
                + "where l.labName=\'" + labName + " and l.studName=\'" + studName + "\'", Labs.class).executeUpdate();
    }

    public void updateLabTest(String studName, String labName) {
        em.createQuery("update Labs l set l.test=\'passed\' "
                + "where l.labName=\'" + labName + " and l.studName=\'" + studName + "\'", Labs.class).executeUpdate();
    }

    public List<Labs> getLabForStudent(String studName, String labName) {
        return em.createQuery("select s from Labs s where s.studName=\'" + studName + "\' AND s.labName=\'" + labName + "\'", Labs.class).getResultList();
    }

    public List<Labs> getAllForStudent(String studName) {
        System.out.println("select all from Labs where studNmae=" + studName);
        return em.createQuery("select s from Labs s where s.studName=\'" + studName + "\'", Labs.class).getResultList();
    }
}
