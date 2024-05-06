package com.example.demo.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Accessors(chain = true)
public class UserLoginDto {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @Length(min = 4,max = 10,message = "Имя пользователя должно содержать от 4 до 10 символов")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Неверный формат почты")
    private String email;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 4, message = "Пароль должен содержать 4 и более символов")
    private String password;
}
