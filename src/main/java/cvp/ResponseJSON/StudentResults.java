package cvp.ResponseJSON;

import org.yecht.Data;

import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class StudentResults {

    private HashMap<String, String> resultHash;

    public StudentResults(HashMap<String, String> resultHash) {
        this.resultHash = resultHash;
    }

    public HashMap<String, String> getResultHash() {
        return resultHash;
    }
}
