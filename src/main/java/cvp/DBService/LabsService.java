package cvp.DBService;

import cvp.DBService.tables.Labs;
import cvp.DBService.tables.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by stepa on 21.05.15.
 */

@Component("labsService")
public class LabsService {

    @Autowired
    LabsDAO labsDAO;

    public Long save(Labs lab) {
        Long id;
        id = labsDAO.save(lab);
        return id;
    }

    public List<Labs> getAllForStudent(String studName) {
        System.out.println("In StudentDAOImpl getAllFromGroup");
        return labsDAO.getAllForStudent(studName);
    }
}
