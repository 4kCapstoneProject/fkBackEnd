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
    @DisplayName("타겟과 이미지를 업로드 해서 저장하는 테스트")
    public void 타겟_이미지_저장테스트() throws Exception{

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
                                headerWithName("AUTHORIZATION").description("Access token 전달")
                        ),
                        requestParts(
                                partWithName("imageFileList").description("업로드 될 파일 (여러장 가능)"),
                                partWithName("imageFileList").description("업로드 될 파일 (여러장 가눙)"),
                                partWithName("targetInfoDto").description("타겟 신상정보")
                        ),
                        requestPartFields("targetInfoDto",
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("유저 ID"),
                                fieldWithPath("personName").type(JsonFieldType.STRING).description("타겟 이름"),
                                fieldWithPath("personAge").type(JsonFieldType.NUMBER).description("타겟 나이"),
                                fieldWithPath("characteristic").type(JsonFieldType.STRING).description("타겟 특징")
                        ),
                        requestParameters(parameterWithName("imageThumbNum").description("이미지 썸네일 여부"))

                ));

    }

    @Test
    public void 타겟_정보_조회_테스트() throws Exception{

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
                               headerWithName("AUTHORIZATION").description("Access Token 전달")
                        ),
                        requestParameters(
                                parameterWithName("method").description(" 정렬방법 ( personName : 이름순  , personAge : 나이순) "),
                                parameterWithName("page").description("페이지 번호 (5개씩 정렬)")

                        ),
                        responseFields(
                                fieldWithPath("dtoList.[].targetPk").type(JsonFieldType.NUMBER).description("타겟 정보의 PK"),
                                fieldWithPath("dtoList.[].userId").type(JsonFieldType.STRING).description("유저 ID"),
                                fieldWithPath("dtoList.[].personName").type(JsonFieldType.STRING).description("타겟 이름"),
                                fieldWithPath("dtoList.[].personAge").type(JsonFieldType.NUMBER).description("타겟 나이"),
                                fieldWithPath("dtoList.[].characteristic").type(JsonFieldType.STRING).description("타겟 특징"),
                                fieldWithPath("imagePathDtoList.[].filePath").type(JsonFieldType.STRING).description("이미지 경로"),
                                fieldWithPath("imagePathDtoList.[].fileName").type(JsonFieldType.STRING).description("이미지 파일 이름"),
                                fieldWithPath("imagePathDtoList.[].targetPk").type(JsonFieldType.NUMBER).description("이미지가 게시된 타겟 정보 PK"),
                                fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("totalElement").type(JsonFieldType.NUMBER).description("총 타겟 정보 갯수"),
                                fieldWithPath("hasPreviousPage").type(JsonFieldType.BOOLEAN).description("이전 페이지 유무"),
                                fieldWithPath("hasNextPage").type(JsonFieldType.BOOLEAN).description("다음 페이지 유무")

                        )

                ));


    }

   */

}