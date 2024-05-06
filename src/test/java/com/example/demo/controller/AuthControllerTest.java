package com.example.demo.controller;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.domain.User;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.user.UserLoginDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void successRegistration() throws Exception {
        RegisterRequestDto registerRequestDto = Instancio.create(RegisterRequestDto.class);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", equalTo("Пользователь зарегистрирован!"))
                );
    }

    @Test
    void validationRegisterError() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "a",
                "b",
                "c",
                "test",
                "test",
                "test",
                "test",
                "test"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", equalTo("Field 'username': Имя пользователя должно содержать от 4 до 10 символов")))
                .andExpect(jsonPath("$.errors[1]", equalTo("Field 'password': Пароль должен содержать 4 и более символов")));
    }

    @Test
    void userAlreadyExistRegisterError() throws Exception {
        RegisterRequestDto registerRequestDto = Instancio.create(RegisterRequestDto.class).setUsername("LOLIPOPS");

        User userToSave = new User()
                .setUsername(registerRequestDto.getUsername())
                .setPassword(registerRequestDto.getPassword())
                .setEmail(registerRequestDto.getEmail());
        userRepository.save(userToSave);

        assertEquals(1, userRepository.count());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code", equalTo(-10)))
                .andExpect(jsonPath("$.error.message", equalTo("Внимание! пользователь с именем пользователя LOLIPOPS уже существует")));

    }

    @Test
    void successLogin() throws Exception {
        UserLoginDto userLoginDto = Instancio.create(UserLoginDto.class)
                .setEmail("test@mail.ru")
                .setUsername("Testovich")
                .setPassword("12345");

        User userToSave = new User()
                .setUsername(userLoginDto.getUsername())
                .setPassword(passwordEncoder.encode(userLoginDto.getPassword()))
                .setEmail(userLoginDto.getEmail());
        userRepository.save(userToSave);

        assertEquals(1, userRepository.count());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo(userLoginDto.getEmail())))
                .andExpect(jsonPath("$.username", equalTo(userLoginDto.getUsername())));

    }

    @Test
    void loginWithBadCredentialsError() throws Exception {
        UserLoginDto userLoginDto = Instancio.create(UserLoginDto.class)
                .setEmail("test@mail.ru")
                .setUsername("Testovich")
                .setPassword("12345");

        User userToSave = new User()
                .setUsername(userLoginDto.getUsername())
                .setPassword(passwordEncoder.encode("12"))
                .setEmail(userLoginDto.getEmail());
        userRepository.save(userToSave);

        assertEquals(1, userRepository.count());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isUnauthorized());
    }


}
