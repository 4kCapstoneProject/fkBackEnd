package com.oldaim.fkbackend.controller.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ImagePathDto {

    private String filePath;

    private String fileName;

    private Long targetPk;

}
