package com.ddu.dduboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.dduboard.entity.Question;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	public Question findBySubject(String subject);

}
