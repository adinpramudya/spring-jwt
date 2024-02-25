package com.example.springsecurity.jwt.controller;

import com.example.springsecurity.jwt.dao.AuthenticationReq;
import com.example.springsecurity.jwt.dao.AuthenticationRes;
import com.example.springsecurity.jwt.dao.ReturnMessage;
import com.example.springsecurity.jwt.dao.UserDao;
import com.example.springsecurity.jwt.entities.SecurityUser;
import com.example.springsecurity.jwt.entities.User;
import com.example.springsecurity.jwt.entities.UserAuthority;
import com.example.springsecurity.jwt.entities.enumaration.UserAuthorityType;
import com.example.springsecurity.jwt.repository.UserRepository;
import com.example.springsecurity.jwt.service.JwtService;
import com.example.springsecurity.jwt.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/api/v1/auth")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @GetMapping("/user/{login}")
    public ResponseEntity<User> findUserByLogin(@PathVariable String login) {
        log.debug("LOGIN USER: {}", login);
        Optional<User> user = userRepository.findUserByLogin(login);
        return ResponseEntity.ok().body(user.get());

    }
    @GetMapping("/user")
    public ResponseEntity<String> findUser() {
        log.debug("LOGIN USER: {}", "Masuk");
        return ResponseEntity.ok().body("Masuk");

    }

    @PostMapping("/register")
    public ResponseEntity<ReturnMessage> registerUser(@RequestBody UserDao userDao) throws URISyntaxException {
        log.debug("REGISTER USER: {}", userDao.getEmail());
        ReturnMessage ret = new ReturnMessage();
        ret.setId(-1);

        // validate login unique
        if(!userRepository.findUserByLogin(userDao.getEmail()).isEmpty()) {
            ret.setMessage("Email "+ userDao.getEmail() + " is Exist");
            return  ResponseEntity.badRequest().body(ret);
        }

        // validate if get user auth admin

        if(userDao.getUserAuthoryties() != null ) {
            ret.setMessage("Authority must not set at register");
            return  ResponseEntity.badRequest().body(ret);
        }

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setName(UserAuthorityType.USER);
        Set<UserAuthority> userAuthorytySet  = Set.of(userAuthority);
        userDao.setUserAuthoryties(userAuthorytySet);

        ret = this.userService.registerUser(userDao);

        return ResponseEntity.created(new URI("/api/v1/register")).body(ret);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationRes> authenticate(
            @Valid @RequestBody AuthenticationReq request
    ) {
        log.debug("LOGIN REQUSET, {}", request.getEmail());
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findUserByLoginIgnoreCase(request.getEmail()).orElseThrow();
        log.debug("USER REQUSET, {}", user);

        UserDetails userDetails = new SecurityUser(user);
        String jwtToken =  jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        AuthenticationRes res = new AuthenticationRes();
        res.setAccessToken(jwtToken);
        res.setRefreshToken(refreshToken);
        return ResponseEntity.ok().body(res);
    }

}

