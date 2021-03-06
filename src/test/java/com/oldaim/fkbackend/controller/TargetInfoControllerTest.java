package com.oldaim.fkbackend.controller;

import com.oldaim.fkbackend.controller.dto.TokenResponseDto;
import com.oldaim.fkbackend.controller.dto.UserDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.enumType.Auth;
import com.oldaim.fkbackend.repository.UserRepository;
import com.oldaim.fkbackend.service.TargetInfoService;
import com.oldaim.fkbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(properties = "spring.config.location="+"classpath:/application-test.yml")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Slf4j
class TargetInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String accessToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TargetInfoService targetInfoService;

    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    private void setUserLogin(){

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

        accessToken = tokenResponseDto.getAccessToken();

    }
   /*
    @Test
    @DisplayName("????????? ???????????? ????????? ?????? ???????????? ?????????")
    public void ??????_?????????_???????????????() throws Exception{

        //given
        List<MockMultipartFile> imageList = new ArrayList<>();

        imageList.add(new MockMultipartFile("imageFileList","testImage1.png",
                "image/png",new FileInputStream("src/test/resources/testImageUpload/testImage1.png")));

        imageList.add(new MockMultipartFile("imageFileList","testImage2.png",
                "image/png",new FileInputStream("src/test/resources/testImageUpload/testImage2.png")));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId","oldaim");
        jsonObject.put("personName","kim");
        jsonObject.put("personAge",11);
        jsonObject.put("characteristic","hello");
        String requestBodyContent = jsonObject.toString();
        //when

        MockMultipartFile jsonFile = new MockMultipartFile("targetInfoDto","",
                "application/json", requestBodyContent.getBytes());

        log.info(jsonFile.getName());


        //then
        mockMvc.perform(
                multipart("/api/target/upload")
                        .file(imageList.get(0))
                        .file(imageList.get(1))
                        .file(jsonFile)
                        .param("imageThumbNum","1")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+ this.accessToken)

        )
                .andExpect(status().isOk())
                .andDo(document("Upload TargetInfo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("AUTHORIZATION").description("Access token ??????")
                        ),
                        requestParts(
                                partWithName("imageFileList").description("????????? ??? ?????? (????????? ??????)"),
                                partWithName("imageFileList").description("????????? ??? ?????? (????????? ??????)"),
                                partWithName("targetInfoDto").description("?????? ????????????")
                        ),
                        requestPartFields("targetInfoDto",
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("?????? ID"),
                                fieldWithPath("personName").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("personAge").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("characteristic").type(JsonFieldType.STRING).description("?????? ??????")
                        ),
                        requestParameters(parameterWithName("imageThumbNum").description("????????? ????????? ??????"))

                ));

    }

    @Test
    public void ??????_??????_??????_?????????() throws Exception{

        //given
        String sortMethod ="personAge";

        int pageNumber = 1;

        List<MultipartFile> imageList = new ArrayList<>();

        imageList.add(new MockMultipartFile("imageFileList","testImage1.png",
                "image/png",new FileInputStream("src/test/resources/testImageUpload/testImage1.png")));

        imageList.add(new MockMultipartFile("imageFileList","testImage2.png",
                "image/png",new FileInputStream("src/test/resources/testImageUpload/testImage2.png")));

        //when
        for (int i = 0; i < 20; i++) {

            TargetInfoDto targetInfoDto = TargetInfoDto.builder()
                    .userId("oldaim")
                    .personName("kim"+" "+ i)
                    .personAge(11L+ i)
                    .characteristic("cute")
                    .build();

            UserDetails userDetails = userDetailsService.loadUserByUsername("oldaim");

          //  targetInfoService.targetInfoSaveWithImage(targetInfoDto,userDetails,imageList,1);

        }

        //then
        mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/api/target/view")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+ this.accessToken)
                        .param("method",sortMethod)
                        .param("page", String.valueOf(pageNumber))

                )
                .andExpect(status().isOk())
                .andDo(document("view targetInfo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                               headerWithName("AUTHORIZATION").description("Access Token ??????")
                        ),
                        requestParameters(
                                parameterWithName("method").description(" ???????????? ( personName : ?????????  , personAge : ?????????) "),
                                parameterWithName("page").description("????????? ?????? (5?????? ??????)")

                        ),
                        responseFields(
                                fieldWithPath("dtoList.[].targetPk").type(JsonFieldType.NUMBER).description("?????? ????????? PK"),
                                fieldWithPath("dtoList.[].userId").type(JsonFieldType.STRING).description("?????? ID"),
                                fieldWithPath("dtoList.[].personName").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("dtoList.[].personAge").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("dtoList.[].characteristic").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("imagePathDtoList.[].filePath").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("imagePathDtoList.[].fileName").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                                fieldWithPath("imagePathDtoList.[].targetPk").type(JsonFieldType.NUMBER).description("???????????? ????????? ?????? ?????? PK"),
                                fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                fieldWithPath("totalElement").type(JsonFieldType.NUMBER).description("??? ?????? ?????? ??????"),
                                fieldWithPath("hasPreviousPage").type(JsonFieldType.BOOLEAN).description("?????? ????????? ??????"),
                                fieldWithPath("hasNextPage").type(JsonFieldType.BOOLEAN).description("?????? ????????? ??????")

                        )

                ));


    }

   */

}