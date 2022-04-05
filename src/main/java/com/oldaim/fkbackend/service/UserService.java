package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ReissueDto;
import com.oldaim.fkbackend.controller.dto.UserDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.enumType.Auth;
import com.oldaim.fkbackend.repository.UserRepository;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtAuthenticProvider jwtAuthenticationProvider;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUserId(String userId) {

       return userRepository.findByUserId(userId);

    }

    public String registerUser(UserDto userDTO) {

        return userRepository.save(dtoToEntity(userDTO)).getUserId();

    }

    public String loginUser(UserDto userDTO){

        User userEntity = this.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(userDTO.getUserPw() , userEntity.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtAuthenticationProvider.createAccessToken(userEntity.getUsername(),userEntity.getAuth().toString());

        String refreshToken = jwtAuthenticationProvider.createRefreshToken(userEntity.getUsername(),userEntity.getAuth().toString());

        userEntity.updateRefreshToken(refreshToken);

        userRepository.saveAndFlush(userEntity);

        return accessToken;
    }

    public String reIssueAccessToken(ReissueDto dto) throws RuntimeException {

        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(() -> new NullPointerException("존재하지 않는 계정입니다."));

        if (user.getRefreshToken() == null || !jwtAuthenticationProvider.validateToken(dto.getRefreshToken())) {
            throw new RuntimeException("만료 되었습니다 ");
        }

        return jwtAuthenticationProvider.createAccessToken(dto.getUserId(), user.getAuth().toString());
    }

    private User dtoToEntity(UserDto userDTO){
        return User.builder()
                .userId(userDTO.getUserId())
                .userPassword(passwordEncoder.encode(userDTO.getUserPw()))
                .userEmail(userDTO.getEmail())
                .auth(Auth.valueOf(userDTO.getAuth()))
                .build();
    }
}
