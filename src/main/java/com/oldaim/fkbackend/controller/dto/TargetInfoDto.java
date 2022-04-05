package com.oldaim.fkbackend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TargetInfoDto {

  private Long targetPk;

  private String userId;

  private String personName;

  private Long personAge;

  private String characteristic;

}
