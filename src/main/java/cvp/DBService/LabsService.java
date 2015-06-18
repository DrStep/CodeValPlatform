package cvp.DBService;

import cvp.DBService.tables.Labs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by stepa on 21.05.15.
 */

@Component("labsService")
public class LabsService {

    @Autowired
    private LabsDAO labsDAO;

    public Long save(Labs lab) {
        Long id;
        id = labsDAO.save(lab);
        return id;
    }

    public void updateLabs(String studName, String labName, String testRes) {
        System.out.println("updateLabs");
        labsDAO.updateLabAttempts(studName, labName);
        if (testRes.equals("passed"))
            labsDAO.updateLabTest(studName, labName);
    }

    public List<Labs> getLabForStudent(String studName, String labName) {
        return labsDAO.getLabForStudent(studName, labName);
    }

    public List<Labs> getAllForStudent(String studName) {
        System.out.println("In LabsService getAllFromGroup");
        return labsDAO.getAllForStudent(studName);
    }
}
