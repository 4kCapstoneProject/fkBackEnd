package com.oldaim.fkbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.controller.dto.TransmitModelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    private final String MODEL_SERVER_URL = "http://2acb-34-147-59-106.ngrok.io";
    private final URI uri = URI.create(MODEL_SERVER_URL);
    private final WebClient client = WebClient.create(String.valueOf(uri));
    private final ImageService imageService;

    public String transmitCaptureImageToModel(Long targetId){

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ImagePathDto imagePathDto = imageService.uploadImageFindByTargetId(targetId);

        builder.part("file", new FileSystemResource(imagePathDto.getFilePath()));

        log.info("upload file");

        return client
                .post()
                .uri("/set_target")
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public TransmitModelDto transmitUploadImageToModel(Long targetId) throws IOException, ParseException {
        // 모델에 이미지 전송하기 위해 이미지 불러오기


        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        ImagePathDto imagePathDto = imageService.captureImageFindByTargetId(targetId);

        builder.part("file", new FileSystemResource(imagePathDto.getFilePath()));

        log.info("capture file");

        String informationFromModel = client
                                        .post()
                                        .uri("/predict")
                                        .bodyValue(builder.build())
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .block();

        log.info("transmit well capture");

        return convertJsonToDto(informationFromModel);

    }

    private TransmitModelDto convertJsonToDto(String jsonString) throws ParseException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        JSONParser parser = new JSONParser();

        JsonNode jsonNode = mapper.readTree((String) parser.parse(jsonString));

        String img = jsonNode.get("img").toString();

        String score = jsonNode.get("score").toString();



       return TransmitModelDto.builder()
               .img(img.substring(1,img.length() - 1))
               .score(score.substring(1,score.length()-1))
               .build();

    }



}
