package com.ddu.dduboard.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
	// 페이징 관련 메서드들 
		@Query( value = "SELECT * FROM ( " +
		                 " SELECT a.*, ROWNUM rnum FROM ( " +
		                 "   SELECT * FROM answer ORDER BY createdate DESC " +
		                 " ) a WHERE ROWNUM <= :endRow " +
		                 ") WHERE rnum > :startRow", nativeQuery = true)
		List<Answer> findAnswersWithPaging(@Param("startRow") int startRow,
		                                           @Param("endRow") int endRow);

}
