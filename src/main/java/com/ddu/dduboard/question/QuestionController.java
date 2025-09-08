package com.ddu.dduboard.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/question") // prefix (접두사)
@Controller
public class QuestionController {
	
	// @Autowired
	// private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;
	
	@GetMapping(value = "/")
	public String root() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "redirect:/list";
	}
	@GetMapping(value = "/list")
	// @ResponseBody // 리턴값의 문자열이 그대로 뷰에 찍힘
	public String list(Model model) {
		// List<Question> questionList = questionRepository.findAll();
		// SELECT * FROM question
		List<Question> questionList = questionService.getList();
		
		model.addAttribute("questionList",questionList);
		
		return "question_list";
	}
	@GetMapping(value = "/detail/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String detail(Model model ,@PathVariable("id") Integer id) {
		Question question =questionService.getQuestion(id);
		model.addAttribute("question", question);
		
		return"question_detail";
	}
}
