package com.example.demo.service.impl;

import com.example.demo.config.jwt.JwtUtil;
import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.controller.AuthController;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.user.*;
import com.example.demo.exception.UserException.UserAlreadyExistException;
import com.example.demo.exception.UserException.UserNotFoundException;
import com.example.demo.exception.UserException.UserServiceMessages;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends CrudServiceImpl<User, UserCreateDto, UserPatchDto, UserDto, String, UserRepository> {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailService customUserDetailService;


    public UserService(UserRepository repository, UserMapper mapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        super(repository, mapper);
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.customUserDetailService = customUserDetailService;
    }

    public void save(User user) {
        repository.save(user);
    }


    public ResponseEntity<AuthController.JwtResponse> loginUser(UserLoginDto loginDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        user.setEmail(loginDto.getEmail());
        String jwt = jwtUtil.generateToken(user.getUsername());

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(
                new AuthController.JwtResponse(
                        jwt, user.getId(),
                        user.getEmail(),
                        user.getUsername(),
                        authorities)
        );
    }

    @Override
    public UserDto create(UserCreateDto userCreateDto) {
        if (repository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException(UserServiceMessages.USER_ALREADY_EXISTS, userCreateDto.getUsername());
        }
        User user = entityMapper.toEntityFromCreateDto(userCreateDto);
        user.setAuthorities(Set.of(Role.ROLE_USER));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        buildFullName(user);
        save(user);
        return mapper.domainToDto(user);
    }


    public ResponseEntity<AuthController.JwtResponse> update(String jsonUser, String id) throws IOException {
        checkUser(id);
        UserPatchDto userPatchDto = new ObjectMapper().readValue(jsonUser, UserPatchDto.class);

        userPatchDto.setPassword(passwordEncoder.encode(userPatchDto.getPassword()));
        User user = repository.findById(id).get();
        user = entityMapper.toEntityFromCreateDtoPatch(user, userPatchDto);
        buildFullName(user);

        repository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(user.getUsername());

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new AuthController.JwtResponse(jwt, user.getId(), user.getEmail(), user.getUsername(), authorities));
    }

    @Override
    public void delete(String id) {
        checkUser(id);
        User user = repository.findById(id).get();
        user.setActive(false);
        repository.save(user);
    }

    @Override
    public Page<UserDto> get(QuerySearchParams params) {
        Page<User> page = repository.findAll(RSQLJPASupport.rsql(params.getQuery()), params.getPageRequest());
        return new PageImpl<>(
                mapper.domainsToDtos(page.getContent()),
                params.getPageRequest(),
                page.getTotalElements()
        );
    }

    @Override
    public UserDto getById(String aString) {
        return super.getById(aString);
    }

    public void registerUser(RegisterRequestDto registerRequestDTO) {
        if (repository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistException(UserServiceMessages.USER_ALREADY_EXISTS, registerRequestDTO.getUsername());
        }
        save(User.builder()
                .email(registerRequestDTO.getEmail())
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .middleName(registerRequestDTO.getMiddleName())
                .fullName(registerRequestDTO.getLastName() + " " + registerRequestDTO.getFirstName() + " " + registerRequestDTO.getMiddleName())
                .mobilePhoneNumber(registerRequestDTO.getMobilePhoneNumber())
                .active(true)
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .authorities(Set.of(Role.ROLE_USER))
                .build()
        );
    }

    public List<UserDto> domainsToDtos(Set<User> users) {
        return mapper.domainsToDtos(new ArrayList<>(users));
    }

    public void buildFullName(User user) {
        StringBuilder fullName = new StringBuilder();
        if (StringUtils.hasText(user.getFirstName())) fullName.append(user.getFirstName());
        if (StringUtils.hasText(user.getLastName())) fullName.append(" ").append(user.getLastName());
        if (StringUtils.hasText(user.getMiddleName())) fullName.append(" ").append(user.getMiddleName());

        user.setFullName(fullName.toString());
    }

    public List<Role> getRoles(String id) {
        checkUser(id);
        return repository.findRolesByUserId(id);
    }

    public Set<Role> addRole(String id, Role role) {
        checkUser(id);
        User user = repository.findById(id).get();
        user.getAuthorities().add(role);
        repository.save(user);
        return user.getAuthorities();
    }

    public void checkUser(String id) {
        if (repository.findById(id).isEmpty()) {
            throw new UserNotFoundException(UserServiceMessages.USER_NOT_FOUND, id);
        }
    }

    public Set<Role> removeRole(String id, Role role) {
        checkUser(id);
        User user = repository.findById(id).get();
        user.getAuthorities().remove(role);
        save(user);
        return user.getAuthorities();
    }

    public ResponseEntity<AuthController.JwtResponse> changePassword(UserChangePasswordDto passDto, String id) {
        checkUser(id);
        User user = repository.findById(id).get();
        user.setPassword(passwordEncoder.encode(passDto.getPassword()));
        save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(user.getUsername());

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new AuthController.JwtResponse(jwt, user.getId(), user.getEmail(), user.getUsername(), authorities));
    }

}
