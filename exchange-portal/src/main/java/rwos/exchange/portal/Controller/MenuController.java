package rwos.exchange.portal.Controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.FileContent;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Service.MenuService;

@RestController
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Value("${app.base.path}")
    private String myPath;
    
    @RequestMapping(method = RequestMethod.GET, path = "")
    public List<Menu> getAllMenu(){
        final File folder = new File(myPath);
        return menuService.getAllMenu(folder);
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public FileContent getFileContent(@RequestBody Menu data){
        return new FileContent(menuService.getFileContent(data.getPath()));
    }
}
