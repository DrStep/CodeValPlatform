package cvp.ResponseJSON;

import org.yecht.Data;

import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class StudentResults {

    private HashMap<String, String> studentRes;

    public StudentResults(HashMap<String, String> studentRes) {
        this.studentRes = studentRes;
    }

    public HashMap<String, String> getStudentRes() {
        return studentRes;
    }
}
