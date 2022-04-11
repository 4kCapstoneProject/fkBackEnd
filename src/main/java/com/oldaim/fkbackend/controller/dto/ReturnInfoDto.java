package com.oldaim.fkbackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnInfoDto {

    private Long returnPk;

    private Long personAge;

    private String personName;

    private Double onSimilarity;

    private Double offSimilarity;

}
