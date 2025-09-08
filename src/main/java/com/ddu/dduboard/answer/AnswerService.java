package com.ddu.dduboard.answer;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddu.dduboard.question.Question;


@RequestMapping(value = "/answer")
@Service
public class AnswerService {

	@Autowired
	private AnswerRepository answerRepository;
	
	public void create(Question question, String content){
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreatedate(LocalDateTime.now());
		answer.setQuestion(question);
		
		answerRepository.save(answer);
	}
	
}