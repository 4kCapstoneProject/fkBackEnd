package com.oldaim.fkbackend.controller.dto;

import lombok.*;

@Getter
@Setter
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
