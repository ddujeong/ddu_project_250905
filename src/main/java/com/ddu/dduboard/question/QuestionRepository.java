package com.ddu.dduboard.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	//ublic Question findBySubject(String subject);
	
	public Optional<Question> findBySubject(String subject);
	
	public Optional<Question> findBySubjectAndContent (String subject, String content);
	
	public List<Question> findBySubjectLike(String keyword);
	
}
