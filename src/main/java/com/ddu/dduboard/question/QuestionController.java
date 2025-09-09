package com.ddu.dduboard.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ddu.dduboard.answer.AnswerForm;

import jakarta.validation.Valid;

@RequestMapping(value = "/question") // prefix (접두사)
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;
	
	// @Autowired
	// private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;

    QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
	
	@GetMapping(value = "/")
	public String root() {
		// url 기본 루트 url 설정 (-> 서버 start 시 리스트로 이동)
		return "redirect:/question/list";
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
	public String detail(Model model ,@PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question =questionService.getQuestion(id);
		model.addAttribute("question", question);
		
		return"question_detail";
	}
	@GetMapping(value = "/create") // 질문등록 폼만 매핑해주는 메서드(get)
	public String questionCreate(QuestionForm questionForm) {
		return"question_form";
	}
	/* @PostMapping(value = "/create") // 질문 내용을 DB에 저장하는 메서드(post)
	public String questionCreate(@RequestParam(value = "subject") String subject, @RequestParam(value = "content") String content) {
		// @RequestParam(value = "subject") String subject == String subject = request.getParameter("subject")
		// @RequestParam(value = "content") String content == String content = request.getParameter("content")
		
		questionService.create(subject, content);
		
		return"redirect:/question/list";
	} validation 하기 전! */ 
	@PostMapping(value = "/create") // 질문 내용을 DB에 저장하는 메서드(post)
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) { // 참이면 유효성 체크에서 에러 발생!
			return "question_form"; // 에러 발생 시 질문 등록 폼으로 다시 이동
		}
		questionService.create(questionForm.getSubject(), questionForm.getContent());
		
		return"redirect:/question/list"; // 질문 리스트로 이동 -> 반드시 redirect!
	}
}
