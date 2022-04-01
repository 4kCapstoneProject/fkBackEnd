package com.oldaim.fkbackend.DTO;

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
