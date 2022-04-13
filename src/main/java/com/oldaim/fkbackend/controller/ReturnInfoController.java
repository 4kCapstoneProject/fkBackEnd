package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.ReturnInfoDto;
import com.oldaim.fkbackend.controller.dto.ReturnWithImageDto;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.service.GmailTransmitService;
import com.oldaim.fkbackend.service.ReturnInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/return")
public class ReturnInfoController {

    private final ReturnInfoService returnInfoService;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticProvider jwtAuthenticationProvider;
    private final GmailTransmitService gmailTransmitService;

    @PostMapping("/download")
    public ResponseEntity<String> returnInfoFromModel(@RequestPart MultipartFile imageFile,
                                    @RequestPart ReturnInfoDto returnInfoDto,
                                    HttpServletRequest request) throws IOException, MessagingException, GeneralSecurityException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));

        Long returnId = returnInfoService.returnInfoSaveWithImage(returnInfoDto,userDetails,imageFile);

        returnInfoDto.setReturnPk(returnId);

        String msg = gmailTransmitService.sendEmailForUser(returnInfoDto,userDetails);

        return ResponseEntity.ok(msg);
    }

    @GetMapping(value = "/pagingView")
    public ResponseEntity<PagingInformationDto> pagingViewReturnInfo(@RequestParam(value = "method")String method,
                                                                     @RequestParam(value = "page")int pageNumber){

        return ResponseEntity.ok(returnInfoService.findReturnInfoPagingViewWithImage(method, pageNumber));
    }

    @GetMapping(value = "view")
    public ResponseEntity<ReturnWithImageDto> viewReturnInfo(@RequestParam(value = "id")Long id){


        return ResponseEntity.ok(returnInfoService.findReturnInfoWithImage(id));
    }




}
