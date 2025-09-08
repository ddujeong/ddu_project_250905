package com.ddu.dduboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.question.QuestionRepository;

@SpringBootTest
public class Test01 {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Test
	@DisplayName("질문 (INSERT) save()")
	public void testJpa1() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다!");
		q1.setCreatedate(LocalDateTime.now());
		
		questionRepository.save(q1);
		// 엔티티만 인자값으로 받을 수 있음(DTO x)
		// INSERT INTO question VALUES (QUESTION_SEQ.nextval, 'sbb가 무엇인가요?','sbb에 대해서 알고 싶습니다!',sysdate)

		Question q2 =new Question();
		q2.setSubject("스프링 부트 모델 질문입니다.");
		q2.setContent("ID는 자동 생성 되나요??");
		q2.setCreatedate(LocalDateTime.now());
		
		questionRepository.save(q2);
	}
	@Test
	@DisplayName("모든 질문 조회 (SELECT) findAll()")
	public void testJpa2() {
		List<Question> allQuestions = questionRepository.findAll();
		// SELECT * FROM question
		
		assertEquals(2, allQuestions.size()); 
		// 예상 결과 비교하기 (리스트의 예상 사이즈 (2), 리스트의 실제 사이즈 (?)) => 틀리면 failures(org.opentest4j.AssertionFailedError: expected: <3> but was: <2>)
		
		Question q = allQuestions.get(0);
		// 첫번째 질문 entity 가져오기
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}
	@Test
	@DisplayName("질문글 번호로 조회 (SELECT) findById()")
	public void testJpa3() {
		Optional<Question> qoptional = questionRepository.findById(1);
		// SELECT * FROM question WHERE id=1
		
		if (qoptional.isPresent()) { // 참이면 돌아감(있으면 true) !
			Question q = qoptional.get(); // 참이면 글 꺼내기
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}
	@Test
	@DisplayName("질문글 제목으로 조회 (SELECT) findBySubject()")
	public void testJpa4() {
		// Entity 로 받기!!
		 //Question question1 = questionRepository.findBySubject1("스프링 부트 모델 질문입니다.");
		 //SELECT * FROM question WHERE subject='스프링 부트 모델 질문입니다.'
		
		//assertEquals(2,question1.getId() );
		
		// Optional 로 받기!!
		Optional<Question> qOptional = questionRepository.findBySubject("스프링 부트 모델 질문입니다.");
		// SELECT * FROM question WHERE subject='스프링 부트 모델 질문입니다.'
		if (qOptional.isPresent()) { 
		Question question2 = qOptional.get();
		
		assertEquals(2,  question2.getId());
		}
	}
	@Test
	@DisplayName("질문글 제목과 내용으로 조회(SELECT) findBySubjectAndContent()")
	public void testJpa5() {
	
		Optional<Question> qOptional = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다!");
		// SELECT * FROM question WHERE subject='sbb가 무엇인가요?' AND content= 'sbb에 대해서 알고 싶습니다!'
		
		if (qOptional.isPresent()) {
		Question question = qOptional.get();
		
		assertEquals(1, question.getId());
		}
	}
	@Test
	@DisplayName("질문글 제목을 특정 문자열로 조회(SELECT) finBySubjectLike()")
	public void testJpa6() {
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");
		// SELECT * FROM question WHERE subject LIKE 'sbb%'
		
		Question question =qList.get(0);
		// 리스트의 첫번째 레코드 가져오기 
		
		assertEquals("sbb가 무엇인가요?", question.getSubject());
	}
}
