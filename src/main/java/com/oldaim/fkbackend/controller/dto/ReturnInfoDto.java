package com.oldaim.fkbackend.controller.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnInfoDto {

    private Long returnId;

    private Long personAge;

    private String personName;

    private Float score;

    private Long targetId;

}
