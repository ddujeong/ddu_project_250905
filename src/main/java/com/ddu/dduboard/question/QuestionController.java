package com.ddu.dduboard.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;

	@GetMapping(value = "/")
	public String root() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "redirect:/question/list";
	}
	
	@GetMapping(value = "/question/list")
	// @ResponseBody // 리턴값의 문자열이 그대로 뷰에 찍힘
	public String list(Model model) {
		List<Question> questionList = questionRepository.findAll();
		// SELECT * FROM question
		model.addAttribute("questionList",questionList);
		
		return "question_list";
	}
}
