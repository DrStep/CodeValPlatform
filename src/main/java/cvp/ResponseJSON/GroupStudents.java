package cvp.ResponseJSON;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class GroupStudents {

    private ArrayList<HashMap<String, Object>> studentsGroup;

    public  GroupStudents(ArrayList<HashMap<String, Object>> studentsGroup) {
        this.studentsGroup = studentsGroup;
    }

    public ArrayList<HashMap<String, Object>> getStudentsGroup() {
        return this.studentsGroup;
    }
}
