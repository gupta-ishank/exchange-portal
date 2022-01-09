package rwos.exchange.portal.Controller;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Service.MenuService;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Value("${app.base.path}")
    private String myPath;

    @GetMapping("/menu")
    public List<Menu> getAllMenu() {

        File pwd;
        String projectDir;
        try {
            pwd = new File(getClass().getResource("/").toURI());
            projectDir = pwd.getParentFile().getParentFile().getParentFile().getParent();
            projectDir = projectDir + myPath;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        final File folder = new File(projectDir);
        return menuService.getAllMenu(folder);
    }
}
