package com.example.demo.repository;


import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,String>, JpaSpecificationExecutor<User>,EntityRepository<User,String>{
    Optional<User> findByUsername(String userName);
    @Query("SELECT u.authorities FROM User u WHERE u.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") String userId);

    Set<User> findAllByIdIn(Set<Long> userId);

    Set<User> findAllByTeamId(String id);

    @Query("SELECT u.email FROM User u WHERE :role MEMBER OF u.authorities")
    List<String> findAllByAuthorities(@Param("role") Role role);
}
