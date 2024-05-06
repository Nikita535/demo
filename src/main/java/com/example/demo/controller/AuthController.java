package com.example.demo.controller;

import com.example.demo.controller.api.AuthApi;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.RegisterResponseDto;
import com.example.demo.dto.user.UserLoginDto;
import com.example.demo.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final UserService userService;

    @Override
    public ResponseEntity<JwtResponse> login(UserLoginDto loginDto) {
        return userService.loginUser(loginDto);
    }

    @Override
    public ResponseEntity<RegisterResponseDto> registerUser(RegisterRequestDto registerRequestDTO) {
        userService.registerUser(registerRequestDTO);
        return ResponseEntity.ok(new RegisterResponseDto("Пользователь зарегистрирован!"));
    }

    public record JwtResponse(String jwt, String id, String email, String username, List<String> authorities) {
    }

}
