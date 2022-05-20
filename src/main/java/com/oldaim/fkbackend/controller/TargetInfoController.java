package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.SearchResultDto;
import com.oldaim.fkbackend.controller.dto.TargetInfoDto;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.service.ImageService;
import com.oldaim.fkbackend.service.TargetInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                              @RequestParam Long targetId, @RequestParam int isUploadFile) throws IOException {



        String msg = imageService.imageListFileUpload(imageFileList,targetId,isUploadFile) +
                " " + "에 업로드 이미지 파일이 저장 되었습니다.";

       return ResponseEntity.ok(msg);
    }

    @PostMapping(value = "/uploadCaptureImage")
    public ResponseEntity<String> uploadCaptureImage(@RequestParam List<MultipartFile> captureImage,
                                                     @RequestParam Long targetId, @RequestParam int thumbNum) throws IOException {

        String msg= imageService.imageListFileUpload(captureImage,targetId,thumbNum) +
                " "+ "에 캡쳐 이미지 파일이 저장 되었습니다.";

        return ResponseEntity.ok(msg);
    }

    @GetMapping(value = "/existData")
    public ResponseEntity<Boolean> existTarget(HttpServletRequest request){

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));


       Boolean existData = targetInfoService.targetInfoDataExist(userDetails.getUsername());

       return ResponseEntity.ok(existData);
    }

    @GetMapping(value = "/deleteTarget")
    public ResponseEntity<Long> deleteTarget(@RequestParam(value = "targetId") Long targetId){

       Long deleteEntityId = targetInfoService.targetDelete(targetId);

       return ResponseEntity.ok(deleteEntityId);

    }

    @GetMapping(value = "/searchName")
    public ResponseEntity<SearchResultDto<Object>> searchTarget(@RequestParam(value = "searchName") String searchString){

        return ResponseEntity.ok(targetInfoService.searchTargetInfo(searchString));

    }

    @GetMapping(value = "/viewTarget")
    public ResponseEntity<PagingInformationDto<Object>> viewTarget(@RequestParam(value = "method")String method,
                                                           @RequestParam(value = "page")int pageNumber,
                                                           HttpServletRequest request){

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));



        return ResponseEntity.ok(targetInfoService.findTargetInfoPagingViewWithImage
                (method, pageNumber,userDetails.getUsername()));
    }


}
