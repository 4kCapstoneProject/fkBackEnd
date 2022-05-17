package com.oldaim.fkbackend.controller.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TransmitModelDto {

    private Long targetPk;

    private Double onSimilarity;

    private Double offSimilarity;

}
