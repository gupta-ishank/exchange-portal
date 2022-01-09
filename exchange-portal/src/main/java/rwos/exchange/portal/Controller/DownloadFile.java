package rwos.exchange.portal.Controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class DownloadFile {

    @GetMapping("/download")
    public void downloadFile(String path, HttpServletResponse res) {
        try {
            res.setHeader("Content-Disposition", "attachment; filename=sample.txt");
            res.getOutputStream().write(contentOf(path));
        } catch (Exception e) {
            System.out.println("downloadFile() | " + e.getMessage());
        }
    }

    @GetMapping("/fileContent")
    private byte[] contentOf(String path) {
        try {
            return Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(path).toURI()));
        } catch (Exception e) {
            System.out.println("contentOf() | " + e.getMessage());
        }
        return new byte[0];
    }
}
