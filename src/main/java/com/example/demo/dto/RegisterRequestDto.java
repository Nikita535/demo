package com.example.demo.dto;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterRequestDto {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @Length(min = 4,max = 10,message = "Имя пользователя должно содержать от 4 до 10 символов")
    private String username;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Length(min = 4, message = "Пароль должен содержать 4 и более символов")
    private String password;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Неверный формат почты")
    @NotEmpty(message = "Почта не может быть пустой")
    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String fullName;

    private String mobilePhoneNumber;
}