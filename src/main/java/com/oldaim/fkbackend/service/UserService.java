package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ReissueDto;
import com.oldaim.fkbackend.controller.dto.TokenResponseDto;
import com.oldaim.fkbackend.controller.dto.UserDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.enumType.Auth;
import com.oldaim.fkbackend.repository.UserRepository;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    public String registerUser(UserDto userDTO)  {

        try {

            return userRepository.save(dtoToEntity(userDTO)).getUserId();

        }catch (DataIntegrityViolationException e){

            throw new DataIntegrityViolationException("아이디는 중복될수 없습니다.");
        }
    }

    public TokenResponseDto loginUser(UserDto userDTO){

        User userEntity = this.findByUserId(userDTO.getUserId())
                .orElseThrow(()->new IllegalArgumentException("유저가 존재하지 않습니다."));

        if (!passwordEncoder.matches(userDTO.getUserPw() , userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");
        }

        String accessToken = jwtAuthenticationProvider.createAccessToken(userEntity.getUsername(),userEntity.getAuth().toString());

        String refreshToken = jwtAuthenticationProvider.createRefreshToken(userEntity.getUsername(),userEntity.getAuth().toString());

        userEntity.updateRefreshToken(refreshToken);

        userRepository.saveAndFlush(userEntity);

        return new TokenResponseDto(userEntity.getUserId(),accessToken,refreshToken);
    }

    public String reIssueAccessToken(ReissueDto dto) throws RuntimeException {

        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(NullPointerException::new);

        if (user.getRefreshToken() == null || !jwtAuthenticationProvider.validateToken(dto.getRefreshToken())) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
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
