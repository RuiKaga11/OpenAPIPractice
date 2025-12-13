package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//サービス層③

@Service
public class UserService {
//	RepsitoryをDIする
	private final UserRepository userRepository;
	
//	推奨されるDIの方法
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	
	public List<User> findAllUsers(){
		return userRepository.findAll();
	}
	
	public User saveUser(User user) {
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Emailアドレスが既に登録済み");
		}
		return userRepository.save(user);
	}
	
	@Transactional
	public User updateUser(long id, User user) {
		
		User exitingUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id:" + id));
		exitingUser.setUsername(user.getUsername());
		exitingUser.setEmail(user.getEmail());
		
		return userRepository.save(exitingUser);
	}

	public void deleteUser(long id) {
		if(!userRepository.existsById(id)) {
			throw new RuntimeException("User not found with id:" + id);
		}
		
		userRepository.deleteById(id);
	}
	
}
