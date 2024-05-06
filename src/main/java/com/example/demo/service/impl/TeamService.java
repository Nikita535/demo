package com.example.demo.service.impl;

import com.example.demo.config.param.QuerySearchParams;
import com.example.demo.domain.Team;
import com.example.demo.domain.User;
import com.example.demo.dto.team.TeamCreateDto;
import com.example.demo.dto.team.TeamDto;
import com.example.demo.dto.team.TeamPatchDto;
import com.example.demo.exception.TeamException.TeamNotFoundException;
import com.example.demo.exception.TeamException.TeamServiceMessages;
import com.example.demo.mapper.TeamMapper;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class TeamService extends CrudServiceImpl<Team, TeamCreateDto, TeamPatchDto, TeamDto, String, TeamRepository> {

    private UserService userService;
    private UserRepository userRepository;

    public TeamService(TeamRepository repository, TeamMapper mapper, UserRepository userRepository, UserService userService) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public TeamDto create(TeamCreateDto teamCreateDto) {
        Team team = entityMapper.toEntityFromCreateDto(teamCreateDto);
        User user = userRepository.findById(teamCreateDto.getLeaderId()).get();
        team.setLeader(user);
        userRepository.save(user.setTeam(team));
        repository.save(team);
        return mapper.domainToDto(team);
    }
    


    @Override
    public void delete(String id) {
        Team teamToDelete = repository.findById(id).orElseThrow(() -> new TeamNotFoundException(TeamServiceMessages.TEAM_NOT_FOUND, id));
        Set<User> users = userRepository.findAllByTeamId(id);

        for (User user : users) {
            user.setTeam(null);
        }
        userRepository.saveAll(users);
        repository.delete(teamToDelete);
    }

    @Override
    public Page<TeamDto> get(QuerySearchParams params) {
        Page<Team> page = repository.findAll(RSQLJPASupport.rsql(params.getQuery()), params.getPageRequest());
        return new PageImpl<>(mapper.domainsToDtos(page.getContent()), params.getPageRequest(), page.getTotalElements());
    }

    @Override
    public TeamDto getById(String aString) {
        return super.getById(aString);
    }

    @Transactional
    public TeamDto joinTeam(String userId, String id) {
        Team latestTeam = userRepository.findById(userId).get().getTeam();
        if (!isNull(latestTeam)) {
            latestTeam.setCountMembers(latestTeam.getCountMembers() - 1);
            repository.save(latestTeam);
        }

        Team newestTeam = repository.findById(id).get();
        newestTeam.setCountMembers(newestTeam.getCountMembers()+1);
        repository.save(newestTeam);

        User user = userRepository.findById(userId).get().setTeam(newestTeam);
        userRepository.save(user);
        return mapper.domainToDto(newestTeam);
    }

    @Transactional
    public TeamDto leaveTeam(String userId, String id) {
        User user = userRepository.findById(userId).get();
        Team team = repository.findById(id).get();
        team.setCountMembers(team.getCountMembers()-1);
        user.setTeam(null);
        repository.save(team);
        userRepository.save(user);
        return mapper.domainToDto(team);
    }
}
