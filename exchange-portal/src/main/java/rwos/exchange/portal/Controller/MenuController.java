package rwos.exchange.portal.Controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.Response;
import rwos.exchange.portal.Service.MenuService;


@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;
    
    @Value("${app.base.path}")
    private String myPath;
    
    @GetMapping("")
    public List<Menu> getAllMenu(){
        final File folder = new File(myPath);
        return menuService.getAllMenu(folder);
    }

    @PostMapping("")
    public Response getFileContent(@RequestBody Menu data){
        return new Response(menuService.getFileContent(data.getPath()));
    }
    
}
