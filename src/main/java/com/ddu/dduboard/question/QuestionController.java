package com.ddu.dduboard.question;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	
	@GetMapping(value = "/list")
	// @ResponseBody // 리턴값의 문자열이 그대로 뷰에 찍힘
	public String list(Model model, @RequestParam(value ="page", defaultValue = "0")int page) {
		// List<Question> questionList = questionRepository.findAll();
		 List<Question> questionList = questionService.getList();
		// SELECT * FROM question
		
		//Page<Question> paging = questionService.getList(page);
		// 게시글 10개씩 자른 리스트 -> 페이지당 10개 -> 2페이지에 해당하는 글 10개
		
		//model.addAttribute("paging",paging);
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
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		
		if (bindingResult.hasErrors()) { // 참이면 유효성 체크에서 에러 발생!
			return "question_form"; // 에러 발생 시 질문 등록 폼으로 다시 이동
		}
		questionService.create(questionForm.getSubject(), questionForm.getContent());
		
		return"redirect:/question/list"; // 질문 리스트로 이동 -> 반드시 redirect!
	}
}
