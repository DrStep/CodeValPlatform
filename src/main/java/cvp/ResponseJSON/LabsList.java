package cvp.ResponseJSON;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class LabsList {

    private HashMap<String, ArrayList<HashMap>> resultHash;

    public LabsList(HashMap<String, ArrayList<HashMap>> resultHash) {
        this.resultHash = resultHash;
    }

    public HashMap<String, ArrayList<HashMap>> getResultHash() {
        return resultHash;
    }
}
