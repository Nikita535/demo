package com.example.demo.dto.team;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class TeamPatchDto {
    private String name;

    private String description;

    private Set<Long> usersId;
}
