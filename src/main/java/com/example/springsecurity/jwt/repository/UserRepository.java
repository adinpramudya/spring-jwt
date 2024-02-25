package com.example.springsecurity.jwt.repository;

import com.example.springsecurity.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByLoginIgnoreCase(String login);











}
