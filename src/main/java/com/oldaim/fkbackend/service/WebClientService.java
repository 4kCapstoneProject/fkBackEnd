package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.controller.dto.TransmitModelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientService {

    private final URI uri = URI.create("http://127.0.0.1:5000");
    private final WebClient client = WebClient.create(String.valueOf(uri));
    private final ImageService imageService;

    public String transmitCaptureImageToModel(Long targetId){

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ImagePathDto imagePathDto = imageService.captureImageFindByTargetId(targetId);

        builder.part("file[]", new FileSystemResource(imagePathDto.getFilePath()));

        return client
                .post()
                .uri("/captureImage")
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public byte[] transmitUploadImageToModel(Long targetId) throws IOException {
        // 모델에 이미지 전송하기 위해 이미지 불러오기


        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ImagePathDto imagePathDto = imageService.uploadImageFindByTargetId(targetId);

        builder.part("file[]", new FileSystemResource(imagePathDto.getFilePath()));

        return client
                .post()
                .uri("/uploadImage")
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    public TransmitModelDto transmitInformationToModel(){


        return client
                 .post()
                 .uri("/dataUpload")
                 .retrieve()
                 .bodyToMono(TransmitModelDto.class)
                 .block();
    }

}
