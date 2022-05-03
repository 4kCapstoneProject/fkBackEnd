package com.oldaim.fkbackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TargetInfoDto {

  private Long targetPk;

  private String userId;

  private String personName;

  private Long personAge;

  private String characteristic;

}
