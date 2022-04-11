package com.oldaim.fkbackend.service;

import com.oldaim.fkbackend.controller.dto.ImagePathDto;
import com.oldaim.fkbackend.controller.dto.PagingInformationDto;
import com.oldaim.fkbackend.controller.dto.ReturnInfoDto;
import com.oldaim.fkbackend.entity.User;
import com.oldaim.fkbackend.entity.information.ReturnInfo;
import com.oldaim.fkbackend.repository.informationRepository.ReturnInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReturnInfoService {

    private final UserService userService;

    private final ReturnInfoRepository returnInfoRepository;

    private final ImageService imageService;

    public ReturnInfo ReturnInfoSave(ReturnInfoDto infoDto, String userId){

        User infoOwner = userService.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        ReturnInfo returnInfo = dtoToEntity(infoDto,infoOwner);

        return returnInfoRepository.save(returnInfo);

    }

    public void returnInfoSaveWithImage(ReturnInfoDto returnInfoDto, UserDetails userDetails, MultipartFile imageFile)
            throws IOException {

       ReturnInfo returnInfo = this.ReturnInfoSave( returnInfoDto,userDetails.getUsername());

       imageService.imageFileUpload(imageFile,returnInfo,"Able");

    }

    public PagingInformationDto<Object> findReturnInfoPagingViewWithImage(String sortMethod, int pageNumber){


        Sort sort = Sort.by(sortMethod).ascending();

        Pageable pageable = PageRequest.of(pageNumber - 1,5,sort);

        Page<ReturnInfo> boardList = returnInfoRepository.findAll(pageable);

        List<Object> returnDtoList = new ArrayList<>();

        List<ImagePathDto> imageDtoList = new ArrayList<>();

        int bound = boardList.getContent().size();

        for (int i = 0; i < bound; i++) {

            ReturnInfoDto returnInfoDto = entityToDto(boardList.getContent().get(i));

            ImagePathDto dto= imageService.ImageFindByTargetId(returnInfoDto.getReturnPk());

            returnDtoList.add(returnInfoDto);

            imageDtoList.add(dto);

        }

        return PagingInformationDto.builder()
                .imagePathDtoList(imageDtoList)
                .dtoList(returnDtoList)
                .hasNextPage(boardList.hasNext())
                .hasPreviousPage(boardList.hasPrevious())
                .totalElement((int) boardList.getTotalElements())
                .totalPage(boardList.getTotalPages())
                .build();
    }

    private ReturnInfo dtoToEntity(ReturnInfoDto dto,User user){

        return ReturnInfo.builder()
                .user(user)
                .personAge(dto.getPersonAge())
                .personName(dto.getPersonName())
                .maskOffSimilarity(dto.getOffSimilarity())
                .maskOnSimilarity(dto.getOnSimilarity())
                .build();

    }

    private ReturnInfoDto entityToDto(ReturnInfo returnInfo){

        return ReturnInfoDto.builder()
                .returnPk(returnInfo.getId())
                .personAge(returnInfo.getPersonAge())
                .personName(returnInfo.getPersonName())
                .onSimilarity(returnInfo.getMaskOnSimilarity())
                .offSimilarity(returnInfo.getMaskOffSimilarity())
                .build();

    }

}
