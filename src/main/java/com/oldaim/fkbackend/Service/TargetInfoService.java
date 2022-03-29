package com.oldaim.fkbackend.Service;

import com.oldaim.fkbackend.DTO.TargetInfoDTO;
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


    public TargetInfo dtoToEntity(TargetInfoDTO infoDTO, User user){
        return TargetInfo.builder()
                .user(user)
                .personAge(infoDTO.getPersonAge())
                .personName(infoDTO.getPersonName())
                .personCharacteristic(infoDTO.getCharacteristic())
                .build();
    }

    public TargetInfo targetInfoSave(TargetInfoDTO infoDTO,Long id){

        User infoOwner = userService.userFind(id);

        TargetInfo targetInfo = dtoToEntity(infoDTO,infoOwner);

        return  targetInfoRepository.save(targetInfo);

    }


}
