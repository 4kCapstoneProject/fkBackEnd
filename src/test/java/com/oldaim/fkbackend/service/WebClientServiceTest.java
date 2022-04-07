package com.oldaim.fkbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.File;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class WebClientServiceTest {

    private static final int PORT = 9000;
    private static ClientAndServer mockServer;

    @BeforeEach
    void setUp(){
        mockServer = ClientAndServer.startClientAndServer("localhost",PORT);
        setMockServer();
        log.info("mock server start");
    }

    @AfterEach
    void tearDown(){

        mockServer.stop();
        log.info("mock server stop");
    }

    @Test
    public void WebClient_요청_test() throws Exception{

         String returnString = callWebClient();
        //given



      log.info(returnString);

        //then


    }
    private void setMockServer(){

        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/test")




                )

                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("hello world")
                );


    }

    private String callWebClient(){

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file",new FileSystemResource("/Users/kimdonggyun/Desktop/bootpicture/testImage.png"));
        builder.part("fileName","testImage.png");

        MultiValueMap<String, HttpEntity<?>> parts = builder.build();

        WebClient client = WebClient.create("http://127.0.0.1:5000");

        return client.post()
                .uri("/fileUpload")
                .bodyValue(parts)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}