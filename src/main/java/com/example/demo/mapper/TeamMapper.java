package com.example.demo.mapper;

import com.example.demo.domain.Team;
import com.example.demo.dto.team.TeamCreateDto;
import com.example.demo.dto.team.TeamDto;
import com.example.demo.dto.team.TeamPatchDto;
import org.mapstruct.Mapper;

@Mapper(config = EntityMapperConfig.class, uses = {UserMapper.class})
public interface TeamMapper extends EntityMapper<Team, TeamDto, TeamCreateDto, TeamPatchDto> {
}