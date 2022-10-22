package com.example.PayMe.controller;

import com.example.PayMe.Exception.RestApiErrorHandler;
import com.example.PayMe.Exception.UserAlreadyExistException;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.repository.UserRepository;
import com.example.PayMe.security.JWT.JwtTokenUtils;
import com.example.PayMe.security.JWT.JwtUserDetailsService;
import com.example.PayMe.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImp userServiceImp;
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @GetMapping("/find/{email}")
    public ResponseEntity<UserEntity> findUser(@PathVariable(value = "email") String email){
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return  ResponseEntity.ok(user.get());

    }


    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> listUser(){
        return ResponseEntity.ok(userServiceImp.listUser());
    }

}
