package com.ddu.dduboard.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	public Optional<SiteUser> findByUsername(String username); // 유저 아이디로 조회
}
