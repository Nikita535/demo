package com.example.demo.controller.api;

import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.controller.AuthController;
import com.example.demo.domain.Role;
import com.example.demo.dto.Result;
import com.example.demo.dto.user.UserChangePasswordDto;
import com.example.demo.dto.user.UserCreateDto;
import com.example.demo.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RequestMapping(ApiPath.USER_API)
@CrossOrigin(origins = "http://localhost:3000")
public interface UserApi {

    @GetMapping
    @Operation(description = "Получение списка пользователей с фильтрацией")
    Result<List<UserDto>> getUser(
            @Parameter(description = "query - содержит условия для фильтрации")
            QuerySearchParams params
            );

    @GetMapping("/{id}")
    @Operation(description = "Получение пользователя по идентификатору")
    ResponseEntity<UserDto> getUserById(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long id
    );

    @PostMapping
    @Operation(description = "Создание пользователя")
    UserDto createUser(
            @Parameter(description = "Данные пользователя")
            @RequestBody UserCreateDto userDto
    );

    @DeleteMapping({"{id}"})
    @Operation(description = "Удаление пользователя")
    ResponseEntity<?> deleteUser(
            @Parameter(description = "Данные пользователя")
            @PathVariable Long id
    );

    @PostMapping({"{id}"})
    @Operation(description = "Восстановление пользователя")
    ResponseEntity<?> activateUser(
            @Parameter(description = "Данные пользователя")
            @PathVariable Long id
    );

    @PatchMapping({"{id}"})
    @Operation(description = "Обновление пользователя")
    ResponseEntity<?> updateUser(
            @Parameter(description = "Данные пользователя")
            @RequestParam(value = "user", required = false) String jsonUser,
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile multipartFile
    ) throws IOException;

    @GetMapping("/{id}/roles")
    @Operation(description = "Получение ролей пользователя")
    ResponseEntity<List<Role>> getRoles(
            @Parameter(description = "Id пользователя")
            @PathVariable Long id
    );

    @PostMapping("/role/{id}")
    @Operation(description = "Выдать роль пользователю")
    Set<Role> setRole(
            @Parameter(description = "идентификатор пользователя") @PathVariable Long id,
            @Parameter(description = "Роль") @RequestParam Role role
    );

    @PostMapping("/role/{id}/remove")
    @Operation(description = "Снять роль с пользователя")
    Set<Role> removeRole(
            @Parameter(description = "идентификатор пользователя") @PathVariable Long id,
            @Parameter(description = "Роль") @RequestParam Role role
    );

    @PostMapping("/password/{id}")
    @Operation(description = "Смена пароля пользователя")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<AuthController.JwtResponse> changePassword(
            @Parameter(description = "Пароль пользователя")
            @RequestBody UserChangePasswordDto passDto,
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long id
    );

}
