package com.ddu.dduboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
//	// 페이지 리스트
//	public Page<Question> getList(int page){
//		Pageable pageable = PageRequest.of(page, 10);
//		// page는 조회할 번호 10은 한페이지에 보여줄 게시물의 갯수
//		return questionRepository.findAll(pageable);
//	}
}
