package com.ddu.dduboard.answer;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddu.dduboard.DataNotFoundException;
import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.question.QuestionRepository;
import com.ddu.dduboard.user.SiteUser;


@RequestMapping(value = "/answer")
@Service
public class AnswerService {

    private final QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

    AnswerService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
	
	public Answer create(Question question, String content, SiteUser author){
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreatedate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		
		answerRepository.save(answer);
		return answer; //DB에 등록한 답변을 바로 반환
	}
	public Answer getAnswer(Integer id) {
		Optional<Answer> _answer = answerRepository.findById(id);
		if (_answer.isPresent()) {
			return _answer.get();
		} else {
			throw new DataNotFoundException("해당 답변이 존재하지 않습니다");
		}
	}
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifydate(LocalDateTime.now());
		
		answerRepository.save(answer);
	}
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		answerRepository.save(answer);
	}
	public void disvote(Answer answer, SiteUser siteUser) {
		answer.getDisvoter().add(siteUser);
		answerRepository.save(answer);
	}
	public Page<Answer> getAnswerPageList(int page, int id){
		
		int size = 10; // 1페이지당 글 10개씩 출력
		int startRow = page *10; // * 페이징 의 첫번째 글 넘버 * 첫 페이지 page=0 -> 0*10 = 0 / 두번째 페이지 page =1 -> 1*10 = 10
		int endRow = (startRow) + size; // * 페이징의 마지막 글 넘버 * 첫번째 페이지  0 +10 = 10 / 두번째 페이지 10 + 10 = 20
		
		List<Answer> pageAnswerList = answerRepository.findAnswersWithPaging(startRow, endRow);
		Long totalAnswer = answerRepository.count();
		
		Page<Answer> pagingList = new PageImpl<>(pageAnswerList, PageRequest.of(page, size), totalAnswer);
		
		return pagingList;
		
		
		
	}
	
}