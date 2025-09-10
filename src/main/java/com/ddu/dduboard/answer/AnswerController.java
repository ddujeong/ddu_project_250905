package com.ddu.dduboard.answer;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.question.QuestionForm;
import com.ddu.dduboard.question.QuestionService;
import com.ddu.dduboard.user.SiteUser;
import com.ddu.dduboard.user.UserService;

import jakarta.validation.Valid;

@RequestMapping(value = "/answer")
@Controller
public class AnswerController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id,@Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		Question question = questionService.getQuestion(id);
		
		// 로그인 한 유저의 아이디 얻기
		SiteUser siteUser = userService.getUser(principal.getName());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		answerService.create(question, answerForm.getContent(), siteUser);
		
		return String.format("redirect:/question/detail/%s", id);
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@GetMapping(value = "/modify/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String modify(@PathVariable("id") Integer id, AnswerForm answerForm, Principal principal) {
		Answer answer =answerService.getAnswer(id);
		
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		
		return "answer_form";
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@PostMapping(value = "/modify/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String answerModify(@PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) { // 에러가 있으면 다시 수정폼으로 이동
			return"question_detail";
		}
		Answer answer =answerService.getAnswer(id);
		
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s",answer.getQuestion().getId());
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@GetMapping(value = "/delete/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
		Answer answer =answerService.getAnswer(id);
		
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId()) ; // 리스트로 이동
	}
}

