package com.example.demo.dto.team;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class TeamCreateDto {
    private String name;
    private String description;
    private Long countMembers;
    private String leaderId;
}
