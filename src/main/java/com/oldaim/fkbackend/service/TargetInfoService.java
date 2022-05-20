package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.SearchResultDto;
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
import org.springframework.stereotype.Service;

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

        User user = userService.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("인증되지 않은 유저의 접근입니다."));

        TargetInfo targetInfo = dtoToEntity(infoDTO,user);

        return  targetInfoRepository.save(targetInfo).getId();

    }

    public boolean targetInfoDataExist(String userId){

       List<TargetInfo> existList= targetInfoRepository.findAllToExist(userId);

        return !existList.isEmpty();

    }

    public Long targetDelete(Long targetId){

        imageService.ImageDeleteByTargetId(targetId);

        targetInfoRepository.deleteById(targetId);

        return targetId;
    }

    public SearchResultDto<Object> searchTargetInfo (String searchString){


       List<Object> targetDtoList = new ArrayList<>();

       List<ImagePathDto> imageDtoList = new ArrayList<>();

       List<TargetInfo> boardList = targetInfoRepository.findAllByPersonName(searchString);

        for (TargetInfo targetInfo : boardList) {

            TargetInfoDto targetInfoDto = entityToDto(targetInfo);

            List<ImagePathDto> dtoList = imageService.imageFindAllByTargetId(targetInfoDto.getTargetPk());

            targetDtoList.add(targetInfoDto);

            imageDtoList.addAll(dtoList);

        }

        return SearchResultDto.builder()
                .dtoList(targetDtoList)
                .imagePathDtoList(imageDtoList)
                .build();
    }

    public PagingInformationDto<Object> findTargetInfoPagingViewWithImage(String sortMethod, int pageNumber, String userId){


        Sort sort = Sort.by(sortMethod).ascending();

        Pageable pageable = PageRequest.of(pageNumber - 1,3,sort);

        Page<TargetInfo> boardList = targetInfoRepository.findAllByUserIdPageable(userId,pageable);

        List<Object> targetDtoList = new ArrayList<>();

        List<ImagePathDto> imageDtoList = new ArrayList<>();

        log.info(boardList.getTotalElements());

        int bound = boardList.getContent().size();

        for (int i = 0; i < bound; i++) {

            log.info(boardList.getContent().get(i));

            TargetInfoDto targetInfoDto = entityToDto(boardList.getContent().get(i));

            List<ImagePathDto> dtoList = imageService.imageFindAllByTargetId(targetInfoDto.getTargetPk());

            targetDtoList.add(targetInfoDto);

            imageDtoList.addAll(dtoList);

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
