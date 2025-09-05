package com.ddu.dduboard.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동증가 (AI)
	private Integer id;
	
	@Column(length =200 ) // 질문게시판의 제목은 200자 까지 가능
	private String subject;
	
	@Column(columnDefinition ="TEXT" ) // 질문게시판의 내용은 글자 제한이 없고 문자열로 이루어짐 
	private String content;
	
	@CreationTimestamp
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) // 1(질문) : N(대답) 관계 , 질문이 삭제 시 연관 된 답변도 삭제
	private List<Answer> answerList;
}
