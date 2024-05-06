package com.example.demo.controller.api;


import com.example.demo.controller.AuthController;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.RegisterResponseDto;
import com.example.demo.dto.user.UserLoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(ApiPath.AUTH_API)
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public interface AuthApi {

    @PostMapping("/login")
    ResponseEntity<AuthController.JwtResponse> login(
            @RequestBody UserLoginDto loginDto
    );

    @PostMapping("/register")
    ResponseEntity<RegisterResponseDto> registerUser(
            @RequestBody @Valid RegisterRequestDto registerRequestDTO
    );

}
