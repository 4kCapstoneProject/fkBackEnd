package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.TokenResponseDto;
import com.oldaim.fkbackend.controller.dto.UserDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.enumType.Auth;
import com.oldaim.fkbackend.repository.UserRepository;
import com.oldaim.fkbackend.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.oldaim.fkbackend.utils.ApiDocumentUtils.getDocumentRequest;
import static com.oldaim.fkbackend.utils.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
  서블릿 컨테이너의 Mocking => 웹 환경에서 컨트롤러를 테스트 하기 위해서는 서블릿 컨테이너가 구동되고
  DispatcherServlet 객체가 메모리에 올라가야하는데 이때 서블릿 컨테이너를 모킹하면 실제 서블릿 컨테이너가 아닌
  테스트 모형 컨테이너를 사용해서 간단하게 테스트 가능
*/


@SpringBootTest(properties = "spring.config.location="+"classpath:/application-test.yml")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class UserLoginControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;






    @Test
    @DisplayName("클라이언트로 회원가입 정보를 받아 저장하는 기능")
    public void 회원가입_테스트() throws Exception{

        //given
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("userId","oldaim");
        jsonObject.put("userPw","11111");
        jsonObject.put("auth","ROLE_ADMIN");
        jsonObject.put("email","dgk0911@gmail.com");

        String bodyContent = jsonObject.toString();



        //then
       ResultActions resultActions = mockMvc.perform(
                       RestDocumentationRequestBuilders.post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyContent))
                .andExpect(status().isOk())
                .andExpect(content().string("oldaim"+ " "+"님이 회원가입 하셨습니다."));

        resultActions.andDo(document("Register User",
                        getDocumentRequest(),
                        getDocumentResponse(),
                requestFields(
                        fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
                        fieldWithPath("userPw").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("auth").type(JsonFieldType.STRING).description("권한"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                )

        ));

    }

    @Test
    @DisplayName("저장된 회원 정보로 로그인 하는 테스트")
    public void 로그인_테스트() throws Exception{

        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId","oldaims");
        jsonObject.put("userPw","11111");
        jsonObject.put("auth","ROLE_ADMIN");
        jsonObject.put("email","dgk0911@gmail.com");
        String requestBodyContent = jsonObject.toString();

        //when
        User mockUser = User.builder()
                .userId("oldaims")
                .userPassword(passwordEncoder.encode("11111"))
                .userEmail("dgk0911@gmail.com")
                .auth(Auth.ROLE_ADMIN)
                .build();

        userRepository.save(mockUser);



        //then
        ResultActions resultActions =mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyContent))
                .andExpect(status().isOk());


        resultActions.andDo(document("login user"
                ,getDocumentRequest()
                ,getDocumentResponse()
                ,requestFields(
                        fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
                        fieldWithPath("userPw").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("auth").type(JsonFieldType.STRING).description("권한"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                )
                ,responseFields(
                        fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access 토큰"),
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh 토큰")
                )

        ));


    }

    @Test
    @DisplayName("만료된 Access 토큰을 refresh 토큰으로 재발급하는 테스트")
    public void 토큰_재발급_테스트() throws Exception{

        //given
        User mockUser = User.builder()
                .userId("oldaim")
                .userPassword(passwordEncoder.encode("11111"))
                .userEmail("dgk0911@gmail.com")
                .auth(Auth.ROLE_ADMIN)
                .build();

        userRepository.save(mockUser);

        UserDto userDto = UserDto.builder()
                .userId("oldaim")
                .userPw("11111")
                .auth("ROLE_ADMIN")
                .email("dgk0911@gmai.com")
                .build();

        TokenResponseDto tokenResponseDto = userService.loginUser(userDto);

        //when
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId","oldaim");
        jsonObject.put("refreshToken",tokenResponseDto.getRefreshToken());
        String requestBodyContent = jsonObject.toString();

        //then
        ResultActions resultActions =mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/reissueToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyContent))
                .andExpect(status().isOk());

        resultActions.andDo(document("reissue AccessToken",
                getDocumentRequest(),
                getDocumentResponse()
                ,requestFields(
                        fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refresh 토큰")
                )
                ,responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("access 토큰")
                )

        ));

    }
}