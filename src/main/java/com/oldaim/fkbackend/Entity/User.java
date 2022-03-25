package com.oldaim.fkbackend.Entity;

import com.oldaim.fkbackend.Entity.EnumType.Auth;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User extends BaseEntity {

    @Id
    @Column(name = "user_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userId;

    @Column
    private String userPassword;

    @Column
    private String userEmail;

    @Column
    @Enumerated
    private Auth auth;
}
