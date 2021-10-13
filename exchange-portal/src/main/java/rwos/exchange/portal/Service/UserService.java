package rwos.exchange.portal.Service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.User;
import rwos.exchange.portal.Repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public List<User> loginUser(User user) {
		return userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
	}

	public User signupUser(User user) {
		if(Objects.isNull(userRepository.findByEmail(user.getEmail())))
			return userRepository.save(user);
		else 
			return null;
	}


}
