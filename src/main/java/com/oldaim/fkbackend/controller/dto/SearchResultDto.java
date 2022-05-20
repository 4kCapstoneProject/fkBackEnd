package com.oldaim.fkbackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto<T> {

    List<T> dtoList;

    List<ImagePathDto> imagePathDtoList;

}
