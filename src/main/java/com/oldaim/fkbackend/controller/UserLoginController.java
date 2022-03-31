package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.DTO.UserDTO;
import com.oldaim.fkbackend.Entity.User;
import com.oldaim.fkbackend.Security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/auth")
public class UserLoginController {

    private final UserService userService;

    private final JwtAuthenticProvider jwtAuthenticationProvider;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO){

        String userId = userService.registerUser(userDTO);

        return userId +" "+ "님이 회원가입 하셨습니다.";

    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO, HttpServletResponse response){

        User userEntity = userService.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(userDTO.getUserPw() , userEntity.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String token = jwtAuthenticationProvider.createToken(userEntity.getUsername(),userEntity.getAuth().toString());

        response.setHeader("X-AUTH-TOKEN", token);

        String loginId = userEntity.getUserId();

        return loginId + " " + "님이 로그인 하셨습니다.";

    }



}
