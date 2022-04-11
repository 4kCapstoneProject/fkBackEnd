package com.oldaim.fkbackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnWithImageDto {

    private ImagePathDto imagePathDto;

    private ReturnInfoDto returnInfoDto;

}
