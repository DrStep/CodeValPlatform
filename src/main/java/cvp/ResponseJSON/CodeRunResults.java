package cvp.ResponseJSON;

import java.util.HashMap;

/**
 * Created by stepa on 22.05.15.
 */
public class CodeRunResults {

    private HashMap<String, Object> codeRunRes;
    private Boolean error;
    private String message;

    public CodeRunResults(HashMap<String, Object> codeRunRes, Boolean error, String message) {
        this.codeRunRes = codeRunRes;
        this.error = error;
        this.message = message;
    }

    public HashMap<String, Object> getCodeRunRes() {
        return this.codeRunRes;
    }

    public Boolean getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }
}
