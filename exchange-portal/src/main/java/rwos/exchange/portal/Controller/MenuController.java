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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.FileContent;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.Response;
import rwos.exchange.portal.Entity.User;
import rwos.exchange.portal.Service.MenuService;
import rwos.exchange.portal.Service.UserRepository;


@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${app.base.path}")
    private String myPath;
    
    @GetMapping("")
    public List<Menu> getAllMenu(){
        final File folder = new File(myPath);
        return menuService.getAllMenu(folder);
    }

    @PostMapping("")
    public FileContent getFileContent(@RequestBody Menu data){
        return new FileContent(menuService.getFileContent(data.getPath()));
    }
    
//    @PostMapping("/register")
//    public String processRegister(User user) {
//        
//    	User newUser = new User();
//    	newUser.setEmail(user.getEmail());
//    	newUser.se
//         
//        return "register_success";
//    }
//    
    @PostMapping("/login")
	public Response checkLoginDetails(@RequestBody User userCredentials) {
		User loginDataFromDb = userRepository.getData(userCredentials.getUserName());
		if(loginDataFromDb == null) return new Response("Incorrect credentials");
		boolean flag = true;
		if(!loginDataFromDb.getUserName().equals(userCredentials.getUserName()))
			flag = false;
		if(!loginDataFromDb.getPassword().equals(userCredentials.getPassword())){
			flag = false;
		}
		String str="";
		if(!flag) {
			str = "Incorrect credentials";
		}else {
			str = "correct credentials";
		}
		return new Response(str);
	}
//	
	@PostMapping("/signup")
	public Response AddUser(@RequestBody User userData) {
		User userDataFromDb = userRepository.isUserPresent(userData.getEmail());
		if(userDataFromDb != null) return new Response("User already exists");
		
		userRepository.save(userData);
		String status = "Registered";
		return new Response(status);
	}
    
    
    
}
