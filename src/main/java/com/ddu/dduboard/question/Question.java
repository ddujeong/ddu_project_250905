package com.ddu.dduboard.question;

import java.time.LocalDateTime;
import java.util.List;

import com.ddu.dduboard.answer.Answer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "question")
@SequenceGenerator(
		name = "QUESTION_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "QUESTION_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1 , // 시퀀스 증가 값
		allocationSize = 1 // 시퀀스 증가치
		)
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="QUESTION_SEQ_GENERATOR" )  // 자동증가 (AI)
	private Integer id;
	
	@Column(length =200 ) // 질문게시판의 제목은 200자 까지 가능
	private String subject;
	
	@Column(length = 500 ) // 질문게시판의 내용은 글자 제한이 없고 문자열로 이루어짐 
	private String content;
	
	private LocalDateTime createdate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) // 1(질문) : N(대답) 관계 , 질문이 삭제 시 연관 된 답변도 삭제
	private List<Answer> answerList;
}
