package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.TargetInfoDto;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.service.ImageService;
import com.oldaim.fkbackend.service.TargetInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticProvider jwtAuthenticationProvider;

    private final ImageService imageService;


    @PostMapping(value = "/uploadTargetInfo")
    public ResponseEntity<Long> uploadTarget(@RequestBody TargetInfoDto targetInfoDto, HttpServletRequest request){

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));

        Long targetId = targetInfoService.targetInfoSave(targetInfoDto,userDetails.getUsername());

        return ResponseEntity.ok(targetId);
    }

    @PostMapping(value = "/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam List<MultipartFile> imageFileList,
                                              @RequestParam Long targetId, @RequestParam int thumbNum) throws IOException {

        imageService.imageListFileUpload(imageFileList,targetId,thumbNum);

        String msg = "메세지를 잘 전달 하였습니다.";

       return ResponseEntity.ok(msg);
       
    }

    @GetMapping(value = "/view")
    public ResponseEntity<PagingInformationDto> viewTarget(@RequestParam(value = "method")String method,
                                                           @RequestParam(value = "page")int pageNumber){

        return ResponseEntity.ok(targetInfoService.findTargetInfoPagingViewWithImage(method, pageNumber));
    }

    @PostMapping(value = "/upload")
    public void testMethod(@RequestPart List<MultipartFile> imageFileList , @RequestPart TargetInfoDto targetInfoDto){

        log.info(imageFileList.get(0).getOriginalFilename());

        log.info(targetInfoDto.getPersonName());

    }

}
