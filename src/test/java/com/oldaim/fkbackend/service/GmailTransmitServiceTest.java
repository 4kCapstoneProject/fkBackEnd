package com.oldaim.fkbackend.service;

import com.google.api.services.gmail.Gmail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class GmailTransmitServiceTest {

    @Autowired
    GmailTransmitService gmailTransmitService;

    @Test
    public void Email_전송_테스트() throws Exception{

        //given
        Gmail service= gmailTransmitService.getService();

        //when
        log.info(String.valueOf(service.users().getProfile("dgk0911@gmail.com")));

        //then


    }

}