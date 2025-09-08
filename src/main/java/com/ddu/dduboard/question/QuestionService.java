package com.ddu.dduboard.question;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ddu.dduboard.DataNotFoundException;

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
}
