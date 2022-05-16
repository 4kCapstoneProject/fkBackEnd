package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.TargetInfoDto;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web")
public class WebClientController {

    private final WebClientService webClientService;

    @GetMapping("/transmitImage")
    public ResponseEntity<String> transmitImageToModel(@RequestParam(value = "targetId")Long targetId) throws IOException {

        String msg = webClientService.transmitImageToModel(targetId);

        return ResponseEntity.ok(msg);
    }

    @PostMapping("/transmitInformation")
    public ResponseEntity<String> transmitInformationToModel(@RequestBody TargetInfoDto targetInfoDto){

        String msg = webClientService.transmitInformationToModel(targetInfoDto);

        return ResponseEntity.ok(msg);

    }
}
