package com.example.demo.repository;


import com.example.demo.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,String>, JpaSpecificationExecutor<Team>,EntityRepository<Team,String>{
    Team findTeamByName(String name);
}
