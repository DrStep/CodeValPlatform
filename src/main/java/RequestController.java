import org.mortbay.util.ajax.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by stepa on 12.01.15.
 */

@RestController
public class RequestController {

    @RequestMapping("/test")
    public TestClass  testHandler(@RequestParam("file")MultipartFile file) {
        String result;
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File upload = new File("Uploaded");
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(upload));
                stream.write(bytes);
                stream.close();
                result = "You successfully uploaded!";
                System.out.println(new String(bytes));
            } catch (Exception e) {
                result = "You failed to upload => " + e.toString();
            }
        } else {
            result = "You failed to upload because the file was empty.";
        }
        System.out.println(result);
        return new TestClass(result);
    }
}
