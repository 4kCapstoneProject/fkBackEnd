package com.oldaim.fkbackend.controller.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReissueDto {

    private String userId;

    private String refreshToken;

}
