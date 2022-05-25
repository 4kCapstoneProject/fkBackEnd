package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.ModelProcessedDataDto;
import com.oldaim.fkbackend.security.jwt.JwtAuthenticProvider;
import com.oldaim.fkbackend.service.ReturnInfoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/return")
public class ReturnInfoController {

    private final ReturnInfoService returnInfoService;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticProvider jwtAuthenticationProvider;




    @GetMapping("/transmitToModel")
    public void transmitToModelAndSaveReturnInfo(@RequestParam("targetId") Long id,
                                                 HttpServletRequest request) throws IOException, ParseException {

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationProvider
                .getUserPk(jwtAuthenticationProvider.resolveToken(request)));

        returnInfoService.transmitToModelAndSaveInfo(userDetails.getUsername(),id);

    }

    @GetMapping("/modelInfoList")
    public ResponseEntity<List<ModelProcessedDataDto>> modelProcessedDataView(@RequestParam("targetId") Long id){

       return ResponseEntity.ok(returnInfoService.findAllModelTransmitDataByTargetId(id)) ;

    }









}
