package com.ddu.dduboard.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	//ublic Question findBySubject(String subject);
	
	public Optional<Question> findBySubject(String subject);
	
	public Optional<Question> findBySubjectAndContent (String subject, String content);
	
	public List<Question> findBySubjectLike(String keyword);
	
	@Modifying
	@Transactional
	@Query( value = "UPDATE question SET hit= hit+1 WHERE id= :id", nativeQuery = true)
	public int updateHit(@Param("id") Integer id);
	
	// 페이징 관련 메서드들 
	// public Page<Question> findAll(Pageable pageable);
}
