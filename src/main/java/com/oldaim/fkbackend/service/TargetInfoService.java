package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.TargetInfoDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.information.TargetInfo;
import com.oldaim.fkbackend.repository.informationRepository.TargetInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TargetInfoService {

    private final UserService userService;
    private final TargetInfoRepository targetInfoRepository;
    private final ImageService imageService;

    public Long targetInfoSave(TargetInfoDto infoDTO, String userId){

        User infoOwner = userService.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("인증되지 않은 유저의 접근입니다."));

        TargetInfo targetInfo = dtoToEntity(infoDTO,infoOwner);

        return  targetInfoRepository.save(targetInfo).getId();

    }


    public PagingInformationDto<Object> findTargetInfoPagingViewWithImage(String sortMethod, int pageNumber){


        Sort sort = Sort.by(sortMethod).ascending();

        Pageable pageable = PageRequest.of(pageNumber - 1,5,sort);

        Page<TargetInfo> boardList = targetInfoRepository.findAll(pageable);

        List<Object> targetDtoList = new ArrayList<>();

        List<ImagePathDto> imageDtoList = new ArrayList<>();

        log.info(boardList.getTotalElements());

        int bound = boardList.getContent().size();

        for (int i = 0; i < bound; i++) {

            log.info(boardList.getContent().get(i));

            TargetInfoDto targetInfoDto = entityToDto(boardList.getContent().get(i));

            ImagePathDto dto= imageService.ImageFindByTargetId(targetInfoDto.getTargetPk());

            targetDtoList.add(targetInfoDto);

            imageDtoList.add(dto);

        }

        return PagingInformationDto.builder()
                .imagePathDtoList(imageDtoList)
                .dtoList(targetDtoList)
                .hasNextPage(boardList.hasNext())
                .hasPreviousPage(boardList.hasPrevious())
                .totalElement((int) boardList.getTotalElements())
                .totalPage(boardList.getTotalPages())
                .build();
    }

    private TargetInfo dtoToEntity(TargetInfoDto infoDTO, User user){
        return TargetInfo.builder()
                .user(user)
                .personAge(infoDTO.getPersonAge())
                .personName(infoDTO.getPersonName())
                .personCharacteristic(infoDTO.getCharacteristic())
                .build();
    }

    private TargetInfoDto entityToDto(TargetInfo targetInfo){
        return TargetInfoDto.builder()
                .targetPk(targetInfo.getId())
                .userId(targetInfo.getUser().getUserId())
                .personName(targetInfo.getPersonName())
                .personAge(targetInfo.getPersonAge())
                .characteristic(targetInfo.getPersonCharacteristic())
                .build();
    }


}
