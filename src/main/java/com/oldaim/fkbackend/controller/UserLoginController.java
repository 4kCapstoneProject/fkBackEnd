package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.ReissueDto;
import com.oldaim.fkbackend.controller.dto.UserDto;
import com.oldaim.fkbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Log4j2
@RequestMapping("/api/auth")
public class UserLoginController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDTO){

        String userId = userService.registerUser(userDTO);

        return userId +" "+ "님이 회원가입 하셨습니다.";

    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDTO, HttpServletResponse response){


        String accessToken = userService.loginUser(userDTO);

        response.setHeader("ACCESS-TOKEN", accessToken);

        return "로그인이 성공하였습니다.";

    }

    @PostMapping("/reissueactoken")
    public String reIssueAccessToken(@RequestBody ReissueDto dto) throws RuntimeException {

        log.info("ReIssueDto email="+dto.getUserId()+" refreshToken=" +dto.getRefreshToken());

        return userService.reIssueAccessToken(dto);
    }



}
