package com.example.demo.dto.user;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class UserDto {

    private Long id;
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String fullName;

    private LocalDateTime registrationDate;

    private LocalDateTime updatedDate;

    private String mobilePhoneNumber;
    private boolean active;

    private String teamId;
}
