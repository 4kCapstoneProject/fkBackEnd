package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web")
public class WebClientController {

    private final WebClientService webClientService;

    @GetMapping("/transmit")
    public String transmitToModel(@RequestParam(value = "targetId")Long targetId) throws IOException {

        return webClientService.transmitImageToModel(targetId);

    }
}
