package cvp.ResponseJSON;

import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class CodeRunResults {

    private HashMap<String, Object> codeRunRes;

    public CodeRunResults(HashMap<String, Object> codeRunRes) {
        this.codeRunRes = codeRunRes;
    }

    public HashMap<String, Object> getCodeRunRes() {
        return this.codeRunRes;
    }
}
