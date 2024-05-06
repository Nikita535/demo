package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "match_team")
public class Team implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 128)
    private String description;

    @Column(name = "count_members")
    private Long countMembers;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private User leader;

    @CreatedBy
    private String createdBy;

    @Column(length = 64)
    @LastModifiedBy
    private String updatedBy;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    @OneToMany(mappedBy = "team")
    private List<User> users;
}