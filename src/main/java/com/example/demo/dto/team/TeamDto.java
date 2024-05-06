package com.example.demo.dto.team;

import com.example.demo.dto.user.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class TeamDto {
    private Long id;

    private String name;

    private String description;

    private UserDto leader;

    private Long countMembers;

    private List<UserDto> users;
}