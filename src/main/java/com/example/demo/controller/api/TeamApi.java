package com.example.demo.controller.api;

import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.dto.Result;
import com.example.demo.dto.team.TeamCreateDto;
import com.example.demo.dto.team.TeamDto;
import com.example.demo.dto.team.TeamPatchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ApiPath.TEAM_API)
@CrossOrigin(origins = "http://localhost:3000")
public interface TeamApi {

    @PostMapping
    @Operation(description = "Создать команду")
    TeamDto createTeam(@RequestBody TeamCreateDto teamCreateDto);

    @PatchMapping("{id}")
    @Operation(description = "Изменить команду или ее состав")
    TeamDto updateTeam(
            @RequestBody TeamPatchDto teamPatchDto,
            @PathVariable String id
    );

    @PostMapping("/join/{id}")
    @Operation(description = "Вступить в команду")
    ResponseEntity<TeamDto> joinTeam(
            @RequestParam String userId,
            @PathVariable String id
    );

    @PostMapping("/leave/{id}")
    @Operation(description = "Вступить в команду")
    ResponseEntity<TeamDto> leaveTeam(
            @RequestParam String userId,
            @PathVariable String id
    );

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(description = "Удалить команду")
    void deleteTeam(
            @PathVariable String id
    );

    @GetMapping
    @Operation(description = "Получение списка команд с фильтрацией")
    Result<List<TeamDto>> get(QuerySearchParams params);

    @GetMapping("{id}")
    @Operation(description = "Получение списка команд с фильтрацией")
    ResponseEntity<TeamDto> getTeamById(
            @Parameter(description = "Идентификатор команды")
            @PathVariable String id
    );
}
