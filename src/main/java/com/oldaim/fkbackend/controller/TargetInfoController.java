package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.TargetInfoDto;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
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

    @PostMapping(value = "/upload",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadTarget(@RequestPart TargetInfoDto targetInfoDto, @RequestPart List<MultipartFile> imageFileList,
                                               @RequestParam(value = "imageThumbNum")int thumbnailNumber,
                                               HttpServletRequest request) throws IOException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));

       String personName = targetInfoService.targetInfoSaveWithImage(targetInfoDto,userDetails,imageFileList,thumbnailNumber);

       String msg = personName + " " + "의 정보를 저장했습니다.";

       return ResponseEntity.ok(msg);
    }

    @GetMapping(value = "/view")
    public ResponseEntity<PagingInformationDto> viewTarget(@RequestParam(value = "method")String method,
                                                           @RequestParam(value = "page")int pageNumber){

        return ResponseEntity.ok(targetInfoService.findTargetInfoPagingViewWithImage(method, pageNumber));
    }

}
