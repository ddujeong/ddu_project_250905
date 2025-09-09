package com.ddu.dduboard.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/signup") // 회원가입 폼 매핑
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	@PostMapping(value = "/signup") // 회원 가입 정보를 DB에 입력
	public String signup(@Valid UserCreateForm userCreateForm , BindingResult bindingResult) {
		if (bindingResult.hasErrors()) { // 참이면 유저가 입력한 회원정보 중에 에러 발생
			return"signup_form";
		}
		// 비밀번호 확인 실패 에러(UserCreateForm에 없는 에러) -> bindingResult에 추가
		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호 확인이 일치하지 않습니다.");
			
			return "signup_form";
		}
		try {
			userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail());
		} catch (DataIntegrityViolationException e) { // 중복된 데이테에 대한 예외처라
			e.printStackTrace();
			// 이미 등록된 사용자 아이디의 경우 발샹하는 에러 추가
			bindingResult.reject("signupFailed","이미 등록 된 사용자입니다.");
			
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			// 기타 나머지 예외 처리
			bindingResult.reject("signupFailed",e.getMessage());
			return"signup_form";
		}
		return "redirect:/question/list"; // 첫화면으로 이동
	}
	@GetMapping(value = "/login")
	public String login() {
		return "login_form";
	}
}
