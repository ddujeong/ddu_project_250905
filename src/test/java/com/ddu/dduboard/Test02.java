package com.ddu.dduboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ddu.dduboard.answer.Answer;
import com.ddu.dduboard.answer.AnswerRepository;
import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.question.QuestionRepository;

@SpringBootTest
public class Test02 {
	
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	AnswerRepository answerRepository;
	
	@Test
	@DisplayName("질문글 제목 수정(UPDATE) finById -> save() ")
	@Transactional
	public void testJpa1() {
		Optional<Question> qOptional = questionRepository.findById(1);
		// SELECT * FROM WHERE id=1
		
		assertTrue(qOptional.isPresent()); // 있으면 (true) Runs 로 테스트 지속 / 없으면 (false) Failures 로 테스트 종료
		
		Question question = qOptional.get();
		
		question.setSubject("수정된 제목");
		questionRepository.save(question);
		// UPDATE question SET subject="수정된 제목" WHERE id=1
	}
	@Test
	@DisplayName("질문글 번호로 삭제 (DELETE) delte(Entity) ")
	public void testJpa2() {
		assertEquals(2,questionRepository.count());
		// questionRepository.count() -> 모든 행(레코드)의 갯수 반환
		// SELECT COUNT(*) FROM question
		
		Optional<Question> qOptional = questionRepository.findById(1);
		// SELECT * FROM question WHERE id=1
		assertTrue(qOptional.isPresent()); // 1번 글이 있으면 다음 문장 실행
		
		Question question = qOptional.get(); // 1번글에 해당하는 엔티티 가져오기
		
		questionRepository.delete(question); // 해당 엔티티 삭제
		// DELETE FROM question WHERE id=1
		
		assertEquals(1, questionRepository.count());
		// SELECT COUNT(*) FROM question
		
	}
	@Test
	@DisplayName("답변 게시판 글 생성하기(INSERT) findById -> save()")
	public void testJpa3() {
		// 답변 -> 해당 답변이 달릴 질문글의 ID 를 준비
		Optional<Question> qOptional = questionRepository.findById(2);
		// SELECT * FROM question WHERE id=2
		assertTrue(qOptional.isPresent()); // 2번 질문글이 있으면 실행
		Question question = qOptional.get(); // 아이디가 2번인 글을 가져옴
		
		Answer answer = new Answer(); 
		
		answer.setContent("네 자동으로 생성됩니다.");
		answer.setQuestion(question); // 답변글이 달릴 질문글을(2번 질문글) 지정해줌
		answer.setCreatedate(LocalDateTime.now());
		
		answerRepository.save(answer);
		// INSERT INTO answer(id, content, question_id, createdate ) VALUES(ANSWER_SEQ.nextval ,"네 자동으로 생성됩니다.",2, sysdate) 
	}
	@Test
	@DisplayName("답변 댓글 조회하기 (SELECT) findById -> getQuestion().getId()")
	public void testJpa4() {
		Optional<Answer> aOptional = answerRepository.findById(1);
		// SELECT * FROM answer WHERE id=1
		
		assertTrue(aOptional.isPresent()); // ID가 1번인 답변이 존재하면 실행
		Answer answer = aOptional.get(); // 1번 답변글을 가져옴
		
		assertEquals(2, answer.getQuestion().getId()); // 해당 답변(답변글의 ID가 1번)이 달린 게시글의 번호가 2번이 맞는지 확인
	}
	@Test
	@DisplayName("질문글을 통해 답변 리스트 가져오기 (SELECT) findById -> getAnswerList() ")
	@Transactional
	public void testJpa5() {
		Optional<Question> qOptional = questionRepository.findById(2);
		// SELECT * FROM question WHERE id=2
		assertTrue(qOptional.isPresent()); // 2번 질문글이 있으면 실행
		Question question = qOptional.get(); // 아이디가 2번인 글을 가져옴
		
		List<Answer> aList = question.getAnswerList(); 
		// SELECT * FROM answer WHERE question_id=2(1 : N 관계 조회)=> 해당 질문글에 달린 답변들의 리스트
		// @Transactional 이 없으면 영속성 컨텍스트가 닫혀 LazyInitializationException 발생 가능
		
		assertEquals(1, aList.size()); // 답변리스트의 사이즈가 1개인지 확인
		assertEquals("네 자동으로 생성됩니다.", aList.get(0).getContent()); // 답변리스트의 첫번째 답변의 글내용을 가져옴
	}

}
