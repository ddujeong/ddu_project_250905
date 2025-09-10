package com.ddu.dduboard.answer;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddu.dduboard.DataNotFoundException;
import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.user.SiteUser;


@RequestMapping(value = "/answer")
@Service
public class AnswerService {

	@Autowired
	private AnswerRepository answerRepository;
	
	public void create(Question question, String content, SiteUser author){
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreatedate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		
		answerRepository.save(answer);
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
	
}