package com.ddu.dduboard.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddu.dduboard.DataNotFoundException;

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
	// 유저 아이디로 엔티티 가져오기
	public SiteUser getUser(String membername){
		Optional<SiteUser> _siteUser = userRepository.findByUsername(membername);
		
		if (_siteUser.isPresent()) {
			SiteUser siteUser = _siteUser.get();
			return siteUser;
		} else {
			throw new DataNotFoundException("해당 유저는 존재하지 않는 유저입니다.");
		}
	}
}
