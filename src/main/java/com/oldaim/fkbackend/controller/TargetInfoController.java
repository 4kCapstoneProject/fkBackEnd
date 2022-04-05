package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.TargetInfoDto;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.service.ImageService;
import com.oldaim.fkbackend.service.TargetInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/target")
public class TargetInfoController {

    private final TargetInfoService targetInfoService;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticProvider jwtAuthenticationProvider;

    @PostMapping(value = "/upload",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadTarget(@RequestPart TargetInfoDto targetInfoDto, @RequestPart List<MultipartFile> imageFileList,
                             @RequestParam(value = "imageThumbNum")int thumbnailNumber,
                             HttpServletRequest request) throws IOException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));

        targetInfoService.targetInfoSaveWithImage(targetInfoDto,userDetails,imageFileList,thumbnailNumber);


    }

    @GetMapping(value = "/view")
    public ResponseEntity viewTarget(@RequestParam(value = "method")String method,
                                           @RequestParam(value = "page")int pageNumber){

        return new ResponseEntity<>(targetInfoService.findTargetInfoPagingViewWithImage(method,pageNumber), HttpStatus.OK);
    }

}
