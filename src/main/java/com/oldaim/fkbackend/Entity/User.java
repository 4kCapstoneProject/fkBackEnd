package com.oldaim.fkbackend.Entity;

import com.oldaim.fkbackend.Entity.EnumType.Auth;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public User(Long id, String userId, String userPassword, String userEmail, Auth auth) {
        this.id = id;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.auth = auth;
    }
}
