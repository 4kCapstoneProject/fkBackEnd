package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.TransmitModelDto;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.service.ImageService;
import com.oldaim.fkbackend.service.ReturnInfoService;
import com.oldaim.fkbackend.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/return")
public class ReturnInfoController {

    private final ReturnInfoService returnInfoService;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticProvider jwtAuthenticationProvider;




    @GetMapping("/transmitToModel")
    public void transmitToModelAndSaveReturnInfo(@RequestParam("targetId") Long id,
                                                 HttpServletRequest request)throws IOException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));

        returnInfoService.transmitToModelAndSaveInfo(userDetails.getUsername(),id);

    }



    @GetMapping(value = "/pagingView")
    public ResponseEntity<PagingInformationDto<Object>> pagingViewReturnInfo(@RequestParam(value = "method")String method,
                                                                     @RequestParam(value = "page")int pageNumber){

        return ResponseEntity.ok(returnInfoService.findReturnInfoPagingViewWithImage(method, pageNumber));
    }






}
