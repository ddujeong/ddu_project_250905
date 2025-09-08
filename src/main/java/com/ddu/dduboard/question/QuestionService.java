package com.ddu.dduboard.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
	
	@Autowired
	private QuestionRepository questionRepository;

}
