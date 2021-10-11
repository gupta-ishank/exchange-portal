package rwos.exchange.portal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Service.FilePathService;
import rwos.exchange.portal.Service.RoleService;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class MappingController {
    @Autowired
    private RoleService roleService;

    
    @Autowired
    private FilePathService filePathService;
    
    @GetMapping("/{userId}/{role}")
    public void mapUserAndRole(@PathVariable Long userId, @PathVariable String role){
        roleService.addRole(role);
    }


    @GetMapping("/{role}/{filePath}")
    public void mapRoleAndFile(@PathVariable String filePath, @PathVariable String role){
        filePathService.addFilePath(filePath);
    }
    


}
