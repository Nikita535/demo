package com.example.demo.dto.user;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class UserPatchDto {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String mobilePhoneNumber;

    private String password;
}