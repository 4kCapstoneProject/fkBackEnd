package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final URI uri = URI.create("http://127.0.0.1:5000");
    private final WebClient client = WebClient.create(String.valueOf(uri));
    private final ImageService imageService;

    public String transmitImageToModel(Long targetId) throws IOException {
        // 모델에 이미지 전송하기 위해 이미지 불러오기
        List<ImagePathDto> dtoList = imageService.ImageFindAllByTargetId(targetId);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        for (int i = 0; i < dtoList.size(); i++) {

            ImagePathDto imagePathDto = dtoList.get(i);

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

            builder.part("file[]", new FileSystemResource(imagePathDto.getFilePath()));

        }

        return client
                .post()
                .uri("/fileUpload")
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
