package com.ddu.dduboard.answer;

import java.time.LocalDateTime;

import com.ddu.dduboard.question.Question;
import com.ddu.dduboard.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "answer")
@SequenceGenerator(
		name = "ANSWER_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "ANSWER_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1 , // 시퀀스 증가 값
		allocationSize = 1 // 시퀀스 증가치
		)
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "ANSWER_SEQ_GENERATOR")
	private Integer id;

	@Column(length = 500)
	private String content;
	
	private LocalDateTime createdate;
	
	@ManyToOne // N(답변) : 1(질문) 관계 
	private Question question;
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifydate;
}
