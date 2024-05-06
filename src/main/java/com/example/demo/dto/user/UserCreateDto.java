package com.example.demo.dto.user;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class UserCreateDto {
    private String username;
    private String password;
    private String email;
}
