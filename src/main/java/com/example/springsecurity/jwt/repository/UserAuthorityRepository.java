package com.example.springsecurity.jwt.repository;

import com.example.springsecurity.jwt.entities.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
