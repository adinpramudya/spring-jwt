package com.example.springsecurity.jwt.repository;

import com.example.springsecurity.jwt.entities.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
//    @Query("SELECT ua FROM UserAuthority ua WHERE ua.user.id = :userId")
//    Set<UserAuthority> findUserAuthoritiesByUserId(Long userId);

    List<UserAuthority> findUserAuthorytiesByUserId(Long userId);
}
