package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.DTO.TargetInfoDTO;
import com.oldaim.fkbackend.Entity.Information.TargetInfo;
import com.oldaim.fkbackend.Security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.Service.ImageService;
import com.oldaim.fkbackend.Service.TargetInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/target")
public class TargetInfoController {

    private final TargetInfoService targetInfoService;
    private final ImageService imageService;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticProvider jwtAuthenticationProvider;

    @PostMapping(value = "/upload",consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadTarget(@RequestPart TargetInfoDTO targetInfoDTO,
                             @RequestPart List<MultipartFile> imageFileList,
                             // @RequestHeader(value = "X-AUTH-TOKEN") String token
                             HttpServletRequest request) throws IOException {

        String token = jwtAuthenticationProvider.resolveToken(request);

        log.info(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider.getUserPk(token));

        TargetInfo targetInfo = targetInfoService.targetInfoSave(targetInfoDTO,userDetails.getUsername()); // 유저 로그인 도입하면 수정해야됨

        for (MultipartFile file : imageFileList) {
            imageService.imageFileUpload(file, targetInfo);
        }
    }
}
