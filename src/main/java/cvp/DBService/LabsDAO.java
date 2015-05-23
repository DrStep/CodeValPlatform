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
        em.createQuery("update Labs l set l.attempts=l.attempts+1 "
                + "where l.labName=:labName and l.studName=:studName")
                .setParameter("labName", labName)
                .setParameter("studName", studName)
                .executeUpdate();
    }

    public void updateLabTest(String studName, String labName) {
        em.createQuery("update Labs l set l.test=\'passed\' "
                + "where l.labName=:labName and l.studName=:studName")
                .setParameter("labName", labName)
                .setParameter("studName", studName)
                .executeUpdate();
    }

    public List<Labs> getLabForStudent(String studName, String labName) {
        return em.createQuery("select l from Labs l "+
                "where l.labName=:labName and l.studName=:studName", Labs.class)
                .setParameter("labName", labName)
                .setParameter("studName", studName).getResultList();
    }

    public List<Labs> getAllForStudent(String studName) {
        System.out.println("select all from Labs where studNmae=" + studName);
        return em.createQuery("select l from Labs l " +
                "where l.studName=:studName", Labs.class)
                .setParameter("studName", studName).getResultList();
    }
}
