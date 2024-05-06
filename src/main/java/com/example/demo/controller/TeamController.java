package com.example.demo.controller;

import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.controller.api.TeamApi;
import com.example.demo.dto.PageableResult;
import com.example.demo.dto.Result;
import com.example.demo.dto.team.TeamCreateDto;
import com.example.demo.dto.team.TeamDto;
import com.example.demo.dto.team.TeamPatchDto;
import com.example.demo.service.impl.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;

    @Override
    public TeamDto createTeam(TeamCreateDto teamCreateDto){
        return teamService.create(teamCreateDto);
    }

    @Override
    public TeamDto updateTeam(TeamPatchDto teamPatchDto, String id){
        return teamService.update(teamPatchDto,id);
    }

    @Override
    public void deleteTeam(String id){
        teamService.delete(id);
    }
    @Override
    public Result<List<TeamDto>> get(QuerySearchParams params){
        var page = teamService.get(params);
        return PageableResult.success(page.getContent(),params.getOffset(),params.getLimit(),page.getTotalElements());
    }

    @Override
    public ResponseEntity<TeamDto> getTeamById(String id) {
        return ResponseEntity.ok(teamService.getById(id));
    }

    @Override
    public ResponseEntity<TeamDto> joinTeam(String userId, String id) {
        return ResponseEntity.ok(teamService.joinTeam(userId, id));
    }

    @Override
    public ResponseEntity<TeamDto> leaveTeam(String userId, String id) {
        return ResponseEntity.ok(teamService.leaveTeam(userId, id));
    }
}
