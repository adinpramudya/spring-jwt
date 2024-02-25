package com.example.springsecurity.jwt.entities;

import com.example.springsecurity.jwt.entities.enumaration.UserAuthorityType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "user_authorities")
public class UserAuthority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private UserAuthorityType name;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private User user;














}
