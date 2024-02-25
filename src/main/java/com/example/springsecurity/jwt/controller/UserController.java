package com.example.springsecurity.jwt.controller;

import com.example.springsecurity.jwt.dao.AuthenticationReq;
import com.example.springsecurity.jwt.dao.AuthenticationRes;
import com.example.springsecurity.jwt.dao.ReturnMessage;
import com.example.springsecurity.jwt.dao.UserDao;
import com.example.springsecurity.jwt.entities.SecurityUser;
import com.example.springsecurity.jwt.entities.User;
import com.example.springsecurity.jwt.entities.UserAuthority;
import com.example.springsecurity.jwt.entities.enumaration.UserAuthorityType;
import com.example.springsecurity.jwt.repository.UserAuthorityRepository;
import com.example.springsecurity.jwt.repository.UserRepository;
import com.example.springsecurity.jwt.service.JwtService;
import com.example.springsecurity.jwt.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/api/v1/auth")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, UserService userServic, JwtService jwtService, AuthenticationManager authenticationManager, UserAuthorityRepository userAuthorityRepository) {
        this.userRepository = userRepository;
        this.userAuthorityRepository =userAuthorityRepository;
        this.userService = userServic;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/user/{login}")
    public ResponseEntity<User> findUserByLogin(@PathVariable String login) {
        Optional<User> user = userRepository.findUserByLogin(login);
        return ResponseEntity.ok().body(user.get());

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

        UserAuthority userAuthoryty = new UserAuthority();
        userAuthoryty.setName(UserAuthorityType.USER);
        Set<UserAuthority>  userAuthorytySet  = Set.of(userAuthoryty);
        userDao.setUserAuthoryties(userAuthorytySet);

        ret = this.userService.registerUser(userDao);

        return ResponseEntity.created(new URI("/api/v1/register")).body(ret);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationRes> authenticate(
            @Valid @RequestBody AuthenticationReq request
    ) {
        log.debug("LOGIN REQUSET, {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findUserByLoginIgnoreCase(request.getEmail()).orElseThrow();
        if(user.getUserAuthorities().isEmpty() || user.getUserAuthorities() == null){
            List<UserAuthority> userAuthoritiesList = userAuthorityRepository.findUserAuthorytiesByUserId(user.getId());
            Set<UserAuthority> userAuthoritiesSet = new HashSet<>();
            if (userAuthoritiesList != null) {
                userAuthoritiesSet.addAll(userAuthoritiesList);
            }
            user.setUserAuthorities(userAuthoritiesSet);


        }
        UserDetails userDetails = new SecurityUser(user);
        String jwtToken =  jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        AuthenticationRes res = new AuthenticationRes();
        res.setAccessToken(jwtToken);
        res.setRefreshToken(refreshToken);
        return ResponseEntity.ok().body(res);
    }
}

