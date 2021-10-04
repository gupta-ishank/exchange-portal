package rwos.exchange.portal.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import rwos.exchange.portal.Entity.User;


public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testLoginUser() {
        User user = new User();
        user.setUserName("Joynath");
        user.setPassword("password");
        try {
            System.out.println(userService.loginUser(user));
        } catch (Exception e) {
           System.err.println("User Not found");
        }
        
    }

    

    @Test
    void testSignupUser() {
        User user = new User();
        user.setUserName("Joynath");
        user.setPassword("password");
        user.setEmail("email@gmail.com");
        System.out.println(userService.signupUser(user));
    }
}
