package com.oldaim.fkbackend.controller.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ModelProcessedDataDto {

    private Float lpipsList;

    private List<ImagePathDto> imagePathDto;

}
