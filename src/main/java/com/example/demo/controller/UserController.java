package com.example.demo.controller;

import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.controller.api.UserApi;
import com.example.demo.domain.Role;
import com.example.demo.dto.PageableResult;
import com.example.demo.dto.Result;
import com.example.demo.dto.user.UserChangePasswordDto;
import com.example.demo.dto.user.UserCreateDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public Result<List<UserDto>> getUser(QuerySearchParams params){
        var page = userService.get(params);
        return PageableResult.success(page.getContent(),params.getOffset(),params.getLimit(),page.getTotalElements());
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    public UserDto createUser(UserCreateDto userDto){
        return userService.create(userDto);
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Override
    public ResponseEntity<?> updateUser(String jsonUser, Long id, MultipartFile multipartFile) throws IOException {
        userService.update(jsonUser,id,multipartFile);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Role>> getRoles(Long id){
        return new ResponseEntity<>(userService.getRoles(id),HttpStatus.OK);
    }

    @Override
    public Set<Role> setRole(Long id, Role role)
    {
        return userService.addRole(id,role);
    }

    @Override
    public Set<Role> removeRole(@PathVariable Long id, Role role)
    {
        return userService.removeRole(id,role);
    }

    @Override
    public ResponseEntity<AuthController.JwtResponse> changePassword(UserChangePasswordDto passDto, Long id){
        return userService.changePassword(passDto,id);
    }

}
