package com.example.viruscan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.viruscan.dto.LoginRequest;
import com.example.viruscan.dto.ResponseMessage;
import com.example.viruscan.dto.SignupRequest;
import com.example.viruscan.entity.User;
import com.example.viruscan.service.UserStorageService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserStorageService userStorageService;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000/api/users/signup")
    public ResponseEntity<ResponseMessage> signUp(@RequestBody SignupRequest signupRequest) {

        User registeredUser = userStorageService.registerUser(signupRequest.getUserName(), signupRequest.getUserSurname(), signupRequest.getMailAddress(), signupRequest.getPassword());

        if (registeredUser != null) {
            String message = "User registered successfully.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } else {
            String message = "User registration failed.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000/api/users/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequest loginRequest) {

        User user = userStorageService.loginUser(loginRequest.getMailAddress(), loginRequest.getPassword());

        if (user != null) {
            // Kullanıcı girişi başarılı ise oturum açılır.
            String message = "OK";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } else {
            String message = "Giriş başarısız. Geçersiz mail veya şifre.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(message));
        }
    }

}
