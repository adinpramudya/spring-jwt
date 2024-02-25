package com.example.springsecurity.jwt.service;

import com.example.springsecurity.jwt.dao.ReturnMessage;
import com.example.springsecurity.jwt.dao.UserDao;
import com.example.springsecurity.jwt.entities.User;
import com.example.springsecurity.jwt.repository.UserAuthorityRepository;
import com.example.springsecurity.jwt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserAuthorityRepository userAuthorityRepository;

    public ReturnMessage registerUser(UserDao userDto) {

        // log info
        log.info("SERVICE REGISTER USER");

        ReturnMessage ret = new ReturnMessage();
        ret.setId(-1);

        User user = new User();
        // encode password
        String encodePass = passwordEncoder.encode(userDto.getPassword());

        // mapping user and authorities
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPasswordHash(encodePass);
        user.setCreatedDate(Instant.now());
        user.setCreatedBy(userDto.getEmail());
        user.setIsActive(true);
//        user.setUserAuthoryties(userDto.getUserAuthoryties());

        // save user
        user = userRepository.save(user);
        if (user.getId() != null) {
            User finalUser = user;
            userDto.getUserAuthoryties().forEach(userAuthority -> {
                userAuthority.setUser(finalUser);
                userAuthorityRepository.save(userAuthority);
            });
            ret.setId(1);
        }
        return ret;
    }

}
