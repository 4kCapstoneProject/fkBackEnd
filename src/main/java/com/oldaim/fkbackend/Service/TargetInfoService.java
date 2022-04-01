package com.oldaim.fkbackend.Service;

import com.oldaim.fkbackend.DTO.TargetInfoDto;
import com.oldaim.fkbackend.Entity.Information.TargetInfo;
import com.oldaim.fkbackend.Entity.User;
import com.oldaim.fkbackend.Repository.InformationRepository.TargetInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TargetInfoService {

    private final UserService userService;
    private final TargetInfoRepository targetInfoRepository;


    public TargetInfo dtoToEntity(TargetInfoDto infoDTO, User user){
        return TargetInfo.builder()
                .user(user)
                .personAge(infoDTO.getPersonAge())
                .personName(infoDTO.getPersonName())
                .personCharacteristic(infoDTO.getCharacteristic())
                .build();
    }

    public TargetInfo targetInfoSave(TargetInfoDto infoDTO, String userId){

        User infoOwner = userService.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        TargetInfo targetInfo = dtoToEntity(infoDTO,infoOwner);

        return  targetInfoRepository.save(targetInfo);

    }


}
