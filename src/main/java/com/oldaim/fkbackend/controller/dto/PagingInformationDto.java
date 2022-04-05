package com.oldaim.fkbackend.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingInformationDto<T> {

    private List<T> dtoList;

    private List<ImagePathDto> imagePathDtoList;

    private int totalPage;

    private int totalElement;

    private boolean hasPreviousPage;

    private boolean hasNextPage;
}
