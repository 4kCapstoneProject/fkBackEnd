package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.enumType.Auth;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.repository.UserRepository;
import com.oldaim.fkbackend.repository.informationRepository.TargetInfoRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
@Transactional
@WebAppConfiguration
public class TargetInfoServiceTest {

    @Autowired
    TargetInfoService targetInfoService;

    @Autowired
    TargetInfoRepository targetInfoRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 게시글_페이징처리_테스트() throws Exception{

        //given
        String[] method = {"personAge","personName"};
        int page = 1;
        User dummyUser = User.builder()
                .userId("oldaim")
                .userEmail("dgk0911@gmail.com")
                .userPassword("1111")
                .auth(Auth.ROLE_ADMIN)
                .build();

        dummyUser.updateRefreshToken("asdasdasdasdasddsadasdasdasdasdasdasdas");

        userRepository.save(dummyUser);

        IntStream.range(0, 20).mapToObj(i -> TargetInfo.builder()
                .user(dummyUser)
                .personAge((long) i)
                .personName("dummy" + i)
                .personCharacteristic("He is" + i + "people")
                .build()).forEach(targetInfo -> targetInfoRepository.save(targetInfo));



        //when
        PagingInformationDto<Object> dtoList = targetInfoService.findTargetInfoPagingViewWithImage(method[0],page);


        //then
        assertThat(dtoList.getTotalPage()).isEqualTo(4);


    }
}