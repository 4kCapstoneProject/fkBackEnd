package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.controller.dto.ModelProcessedDataDto;
import com.oldaim.fkbackend.controller.dto.ReturnInfoDto;
import com.oldaim.fkbackend.controller.dto.TransmitModelDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.information.ReturnInfo;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.repository.informationRepository.ReturnInfoRepository;
import com.oldaim.fkbackend.repository.informationRepository.TargetInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnInfoService {

    private final UserService userService;
    private final ReturnInfoRepository returnInfoRepository;
    private final ImageService imageService;
    private final TargetInfoRepository targetInfoRepository;
    private final WebClientService webClientService;



    public void transmitToModelAndSaveInfo(String userName ,Long targetId) throws IOException, ParseException {

        String captureMsg = webClientService.transmitCaptureImageToModel(targetId);

        log.info(captureMsg);

        TransmitModelDto transmitModelDto = webClientService.transmitUploadImageToModel(targetId);

        imageService.captureImageDeleteByTargetIdAndFileType(targetId);

        log.info("Capture Image is deleted!");

        Long returnId = this.ReturnInfoSave(transmitModelDto,userName,targetId);

        imageService.imageByteFileUpload(Base64.decodeBase64(transmitModelDto.getImg()), this.findReturnInfo(returnId),"UPLOAD_FILE");

    }

    public List<ModelProcessedDataDto> findAllModelTransmitDataByTargetId(Long id) {

        List<ReturnInfo> returnInfoList = returnInfoRepository.findAllReturnInfoByTargetId(id);

        List<ModelProcessedDataDto> dtoList = new ArrayList<>();

        for (ReturnInfo returnInfo : returnInfoList) {

            List<ImagePathDto> imagePathDtoList = imageService.imageFindAllByInformationId(returnInfo.getId());

            dtoList.add( ModelProcessedDataDto.builder()
                            .imagePathDto(imagePathDtoList)
                            .scoreList(returnInfo.getScore())
                            .build());

        }

        Collections.reverse(dtoList);

        return dtoList;

    }



    public Long ReturnInfoSave(TransmitModelDto transmitModelDto, String userId, Long targetPk){

        User infoOwner = userService.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("인증되지 않은 유저의 접근입니다."));

        TargetInfo targetInfo = targetInfoRepository.findById(targetPk)
                .orElseThrow(()->new IllegalArgumentException("타겟 정보가 유효하지 않습니다."));

        ReturnInfoDto infoDto = ReturnInfoDto.builder()
                .personAge(targetInfo.getPersonAge())
                .personName(targetInfo.getPersonName())
                .score(Float.parseFloat(transmitModelDto.getScore()))
                .targetId(targetPk)
                .build();

        ReturnInfo returnInfo = dtoToEntity(infoDto,infoOwner);

        return returnInfoRepository.save(returnInfo).getId();

    }

    public ReturnInfo findReturnInfo(Long returnInfoId) {

        return  returnInfoRepository.findById(returnInfoId)
                .orElseThrow(()->new IllegalArgumentException("정보가 유효하지 않습니다."));

    }





    private ReturnInfo dtoToEntity(ReturnInfoDto dto,User user){

        return ReturnInfo.builder()
                .user(user)
                .personAge(dto.getPersonAge())
                .personName(dto.getPersonName())
                .score(dto.getScore())
                .targetId(dto.getTargetId())
                .build();

    }

    private ReturnInfoDto entityToDto(ReturnInfo returnInfo){

        return ReturnInfoDto.builder()
                .returnId(returnInfo.getId())
                .personAge(returnInfo.getPersonAge())
                .personName(returnInfo.getPersonName())
                .score(returnInfo.getScore())
                .returnId(returnInfo.getTargetId())
                .build();

    }


}
