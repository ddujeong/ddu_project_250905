package com.ddu.dduboard.question;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ddu.dduboard.answer.Answer;
import com.ddu.dduboard.answer.AnswerForm;
import com.ddu.dduboard.answer.AnswerService;
import com.ddu.dduboard.user.SiteUser;
import com.ddu.dduboard.user.UserService;

import jakarta.validation.Valid;

@RequestMapping(value = "/question") // prefix (접두사)
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;
	
	// @Autowired
	// private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AnswerService answerService;

    QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
	
	@GetMapping(value = "/list")
	// @ResponseBody // 리턴값의 문자열이 그대로 뷰에 찍힘
	public String list(Model model, @RequestParam(value ="page", defaultValue = "0")int page, @RequestParam(value = "kw" , defaultValue = "") String kw) {
		// List<Question> questionList = questionRepository.findAll();
		//  List<Question> questionList = questionService.getList();
		// SELECT * FROM question
		
		Page<Question> paging = questionService.getPageList(page,kw);
		// Page<Question> 객체: 요청한 페이지(page) 번호에 해당하는 10개 게시글을 담음
		
		//model.addAttribute("paging",paging);
		 model.addAttribute("paging",paging);
		 model.addAttribute("kw",kw);
		
		return "question_list";
	}
	@GetMapping(value = "/detail/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String detail(Model model ,@PathVariable("id") Integer id, AnswerForm answerForm, @RequestParam(value ="page", defaultValue = "0") int page ) {
		questionService.hit(id);
		Question question =questionService.getQuestion(id);
		//questionService.hit1(question);
		//Page<Answer> paging = answerService.getAnswerPageList(page, id);
		model.addAttribute("question", question);
		//model.addAttribute("paging",paging);
		
		return"question_detail";
	}
	@PreAuthorize("isAuthenticated()")
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
	
	@PreAuthorize("isAuthenticated()") // 로그인 한 유저만(인증받은 유저) 해당 메서드가 실행되게 하는 annotation
	@PostMapping(value = "/create") // 질문 내용을 DB에 저장하는 메서드(post)
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		SiteUser siteUser = userService.getUser(principal.getName());
		// 현재 로그인 한 유저의 userName 으로 SiteUser 반환받기
		if (bindingResult.hasErrors()) { // 참이면 유효성 체크에서 에러 발생!
			return "question_form"; // 에러 발생 시 질문 등록 폼으로 다시 이동
		}
		questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		
		return"redirect:/question/list"; // 질문 리스트로 이동 -> 반드시 redirect!
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@GetMapping(value = "/modify/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String modify(@PathVariable("id") Integer id, QuestionForm questionForm, Principal principal) {
		Question question =questionService.getQuestion(id);
		
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		
		// question_form 에 questionForm에 subject 와 content 를 value 로 출력하는 기능이 이미 구현되어 있으므로 
		// 해당 폼을 재사용하기 위해 questionForm에 question 의 필드값을 저장하여 전송
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		
		return "question_form";
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@PostMapping(value = "/modify/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String questionModify(@PathVariable("id") Integer id, @Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) { // 에러가 있으면 다시 수정폼으로 이동
			return"question_form";
		}
		Question question =questionService.getQuestion(id); // 질문글의 아이디로 질뮨글(원본)을 불러옴
		
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		
		questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s",id);
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@GetMapping(value = "/delete/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String questionDelete(@PathVariable("id") Integer id, Principal principal) {
		Question question =questionService.getQuestion(id); // 질문글의 아이디로 질뮨글(원본)을 불러옴
		
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		questionService.delete(question);
		return"redirect:/"; // 리스트로 이동
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@GetMapping(value = "/vote/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String questionVote(@PathVariable("id") Integer id, Principal principal) {
		Question question =questionService.getQuestion(id); // 질문글의 아이디로 질뮨글(원본)을 불러옴
		SiteUser siteUser =userService.getUser(principal.getName());

		questionService.vote(siteUser, question);
		return String.format("redirect:/question/detail/%s", id) ; // 리스트로 이동
	}
	@PreAuthorize("isAuthenticated()") // form -> action 으로 넘어오지 않으면 권한 인증이 안됨
	@GetMapping(value = "/disvote/{id}") // 파라미터 이름 없이 값만 넘어왔을때 처리
	public String questionDisVote(@PathVariable("id") Integer id, Principal principal) {
		Question question =questionService.getQuestion(id); // 질문글의 아이디로 질뮨글(원본)을 불러옴
		SiteUser siteUser =userService.getUser(principal.getName());

		questionService.disvote(siteUser, question);
		return String.format("redirect:/question/detail/%s", id) ; // 리스트로 이동
	}
}
