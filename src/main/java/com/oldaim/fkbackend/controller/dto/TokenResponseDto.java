package com.oldaim.fkbackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class TokenResponseDto {

    private String userId;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}
