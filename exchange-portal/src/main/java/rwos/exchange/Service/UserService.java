package rwos.exchange.portal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.User;
import rwos.exchange.portal.Repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public User loginUser(User user) {
		return userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
	}

	public User signupUser(User user) {
		return userRepository.save(user);
	}


}
