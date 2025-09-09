package com.ddu.dduboard.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public SiteUser create(String username, String password, String email) {
		SiteUser user =new SiteUser();
		user.setUsername(username);
		
		// 비밀번호 암호화 한 후 암호문을 DB에 저장
		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); -> 빈 객체 생성으로 AutoWired 하여 사용!
		user.setPassword(passwordEncoder.encode(password)); 
		
		user.setEmail(email);
		
		userRepository.save(user);
		return user;
	}
}
