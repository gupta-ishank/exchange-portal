package rwos.exchange.portal.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadFile {

    @GetMapping("/download")
    public void downloadFile(String path, HttpServletResponse res) {
        try {
            res.setHeader("Content-Disposition", "attachment; filename=sample.txt");
            res.getOutputStream().write(contentOf(path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private byte[] contentOf(String path) {
        try {
            return Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(path).toURI()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
