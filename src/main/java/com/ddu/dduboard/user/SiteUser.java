package com.ddu.dduboard.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "siteuser")
@SequenceGenerator(sequenceName = "USER_SEQ", 
					name = "USER_SEQ_GENERATOR", 
					initialValue = 1, 
					allocationSize = 1
					)
public class SiteUser {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "USER_SEQ_GENERATOR")
	private Long id; // 유저 번호 (PK)
	
	@Column(unique = true)
	private String username; // 유저 이름 (유니크값 -> 중복 XX)
	
	private String password;
	
	@Column(unique = true)
	private String email; // 유저 이메일 (유니크값 -> 중복 XX)
}
