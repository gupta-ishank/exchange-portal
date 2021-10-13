package rwos.exchange.portal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.User;
import rwos.exchange.portal.Service.UserService;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public List<User> loginUser(@RequestBody User user) {
		return userService.loginUser(user);
	}
	
	@PostMapping("/signup")
	public User signupUser(@RequestBody User user) {
		return userService.signupUser(user);
	}
}
