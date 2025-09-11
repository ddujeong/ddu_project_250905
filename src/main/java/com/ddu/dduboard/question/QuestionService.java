package com.ddu.dduboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.ddu.dduboard.DataNotFoundException;
import com.ddu.dduboard.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	// @Autowired
	private final QuestionRepository questionRepository;
	// @RequiredArgsConstructor 에 의해 생성자 방식으로 주입된 questionRepository 

	// 페이징 X 일반 보드리스트 불러오기
	 public List<Question> getList(){
		return questionRepository.findAll();
	 } 
	 
	public Question getQuestion(Integer id){
		Optional<Question> qOptional =questionRepository.findById(id);
		
		if (qOptional.isPresent()) {
			return qOptional.get();
		} else {
			throw new DataNotFoundException("question not found");
		}
	}
	public void create(String subject, String content, SiteUser user) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreatedate(LocalDateTime.now());
		question.setAuthor(user);
		
		questionRepository.save(question);
		
	}
	// 페이지 리스트
	public Page<Question> getPageList(int page){
		int size = 10; // 1페이지당 글 10개씩 출력
		int startRow = page *10; // * 페이징 의 첫번째 글 넘버 * 첫 페이지 page=0 -> 0*10 = 0 / 두번째 페이지 page =1 -> 1*10 = 10
		int endRow = (startRow) + size; // * 페이징의 마지막 글 넘버 * 첫번째 페이지  0 +10 = 10 / 두번째 페이지 10 + 10 = 20
		List<Question> pageQuestionList = questionRepository.findQuestionsWithPaging(startRow, endRow);
		
		Long totalQuestion = questionRepository.count(); // DB 의 모든 글 갯수 가져오기
		
		// (10개씩 뽑아온 리스트 ,(실제유저가 선택한 페이지 , 페이지의 사이즈),모든 글의 갯수)
		Page<Question> pagingList = new PageImpl<>(pageQuestionList, PageRequest.of(page, size), totalQuestion);
		
		return pagingList;
	}
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifydate(LocalDateTime.now());
		questionRepository.save(question);
	}
	public void delete(Question question) {
		questionRepository.delete(question);
	}
	public void vote(SiteUser siteUser, Question question) {
		question.getVoter().add(siteUser);
		questionRepository.save(question);
	}
	public void disvote(SiteUser siteUser, Question question) {
		question.getDisvoter().add(siteUser);
		questionRepository.save(question);
	}
	
	public void hit(Integer id) {
		questionRepository.updateHit(id);
	}
	public void hit1(Question question) {
		question.setHit(question.getHit()+1);
		questionRepository.save(question);
	}
}
