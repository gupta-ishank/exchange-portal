package rwos.exchange.portal.Controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.Menu;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class DownloadFile {

    @GetMapping("/download")
    public String downloadFile(String path, HttpServletResponse res) {
        try {
            res.setHeader("Content-Disposition", "attachment; filename=sample.txt");
            System.out.println(path.toString());
            res.getOutputStream().write(contentOf(path));
            return "Successfully Downloaded";
        } catch (Exception e) {
            System.out.println("downloadFile() | " + e.getMessage());
        }
        return "Failed!";
    }

    private byte[] contentOf(String path) {
        try {
            return Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(path).toURI()));
        } catch (Exception e) {
            System.out.println("contentOf() | " + e.getMessage());
        }
        return new byte[0];
    }
}