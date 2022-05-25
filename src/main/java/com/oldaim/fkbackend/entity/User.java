package com.oldaim.fkbackend.entity;

import com.oldaim.fkbackend.entity.enumType.Auth;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ServiceUser")
public class User implements UserDetails{

    @Id
    @Column(name = "user_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    @Column
    private String userPassword;

    @Column(unique = true)
    private String userEmail;

    @Column
    @Enumerated
    private Auth auth;

    private String refreshToken;

    @Builder
    public User(Long id, String userId, String userPassword, String userEmail, Auth auth) {
        this.id = id;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.auth = auth;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.stream(Auth.values())
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
