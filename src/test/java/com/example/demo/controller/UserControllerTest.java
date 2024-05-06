package com.example.demo.controller;

import com.example.demo.AbstractIntegrationTest;
import com.example.demo.domain.User;
import com.example.demo.dto.user.UserCreateDto;
import com.example.demo.dto.user.UserPatchDto;
import com.example.demo.mapper.UserMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void successCreateUser() throws Exception {
        UserCreateDto userCreateDto = Instancio.create(UserCreateDto.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo(userCreateDto.getUsername())))
                .andExpect(jsonPath("$.email", equalTo(userCreateDto.getEmail())))
                .andExpect(jsonPath("$.registrationDate",notNullValue()));
    }

    @Test
    void tryCreateUserAlreadyExistError() throws Exception {
        UserCreateDto userCreateDto = Instancio.create(UserCreateDto.class).setUsername("LOL");

        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto));

        assertEquals(1, userRepository.count());


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message", equalTo("Внимание! пользователь с именем пользователя LOL уже существует")));
    }

    @Test
    void successDeleteUser() throws Exception {
        UserCreateDto userCreateDto = Instancio.create(UserCreateDto.class);

        User savedUser = userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto));

        assertEquals(1, userRepository.count());

        mockMvc.perform(delete("/users/{%s}",savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(1, userRepository.count());

    }

    @Test
    void tryDeleteNonExistingUser() throws Exception {
        UserCreateDto userCreateDto = Instancio.create(UserCreateDto.class);

        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto));

        assertEquals(1, userRepository.count());

        mockMvc.perform(delete("/users/{%s}",10000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertEquals(1, userRepository.count());
    }

    @Test
    void getUserByFilterByUsername() throws Exception {
        UserCreateDto userCreateDto1 = Instancio.create(UserCreateDto.class).setUsername("qwe");
        UserCreateDto userCreateDto2 = Instancio.create(UserCreateDto.class).setUsername("zxc");

        User user1 = userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto1));
        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto2));

        assertEquals(2, userRepository.count());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query","username==qwe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.data[0].username", equalTo(user1.getUsername())))
                .andExpect(jsonPath("$.data[0].email", equalTo(user1.getEmail())))
                .andExpect(jsonPath("$.data[0].firstName", equalTo(user1.getFirstName())))
                .andExpect(jsonPath("$.data[0].lastName", equalTo(user1.getLastName())))
                .andExpect(jsonPath("$.data[0].middleName", equalTo(user1.getMiddleName())))
                .andExpect(jsonPath("$.data[0].fullName", equalTo(user1.getFullName())))
                .andExpect(jsonPath("$.data[0].registrationDate", notNullValue()))
                .andExpect(jsonPath("$.data[0].updatedDate", notNullValue()))
                .andExpect(jsonPath("$.data[0].mobilePhoneNumber", equalTo(user1.getMobilePhoneNumber())));
    }

    @Test
    void getUserByFilterByUsernameAndEmail() throws Exception {
        UserCreateDto userCreateDto1 = Instancio.create(UserCreateDto.class)
                .setUsername("qwe")
                .setEmail("abc@mail.ru");

        UserCreateDto userCreateDto2 = Instancio.create(UserCreateDto.class)
                .setUsername("zxc")
                .setEmail("abc@mail.ru");

        User user1 = userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto1));
        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto2));

        assertEquals(2, userRepository.count());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query","username==qwe and email==abc@mail.ru"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.data[0].username", equalTo(user1.getUsername())))
                .andExpect(jsonPath("$.data[0].email", equalTo(user1.getEmail())))
                .andExpect(jsonPath("$.data[0].firstName", equalTo(user1.getFirstName())))
                .andExpect(jsonPath("$.data[0].lastName", equalTo(user1.getLastName())))
                .andExpect(jsonPath("$.data[0].middleName", equalTo(user1.getMiddleName())))
                .andExpect(jsonPath("$.data[0].fullName", equalTo(user1.getFullName())))
                .andExpect(jsonPath("$.data[0].registrationDate", notNullValue()))
                .andExpect(jsonPath("$.data[0].updatedDate", notNullValue()))
                .andExpect(jsonPath("$.data[0].mobilePhoneNumber", equalTo(user1.getMobilePhoneNumber())));
    }

    @Test
    void noOneUserFoundByFilter() throws Exception {
        UserCreateDto userCreateDto1 = Instancio.create(UserCreateDto.class)
                .setUsername("qwe")
                .setEmail("abc@mail.ru");

        UserCreateDto userCreateDto2 = Instancio.create(UserCreateDto.class)
                .setUsername("zxc")
                .setEmail("abc@mail.ru");

        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto1));
        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto2));

        assertEquals(2, userRepository.count());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query","username==qwer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(0)));
    }

    @Test
    void tryToUpdateNonExistingUser() throws Exception {
        UserCreateDto userCreateDto = Instancio.create(UserCreateDto.class)
                .setUsername("qwe")
                .setEmail("abc@mail.ru");

        userRepository.save(userMapper.toEntityFromCreateDto(userCreateDto));

        UserPatchDto userPatchDto = Instancio.create(UserPatchDto.class);

        mockMvc.perform(patch("/users/{%s}",9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code",equalTo(-11)))
                .andExpect(jsonPath("$.error.message",equalTo("Warning! User with id \"9999\" not found!")));

    }
}
