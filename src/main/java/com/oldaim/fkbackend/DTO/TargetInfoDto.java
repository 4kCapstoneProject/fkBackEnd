package com.oldaim.fkbackend.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TargetInfoDto {

    Long userId;

    String personName;

    Long personAge;

    String characteristic;

}
