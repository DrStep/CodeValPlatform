package cvp.ResponseJSON;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class LabsList {

    private HashMap<String, ArrayList<HashMap>> labsList;

    public LabsList(HashMap<String, ArrayList<HashMap>> labsList) {
        this.labsList = labsList;
    }

    public HashMap<String, ArrayList<HashMap>> getLabsList() {
        return labsList;
    }
}
