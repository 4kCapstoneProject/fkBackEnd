package com.oldaim.fkbackend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

   private String userId;

   private String userPw;

   private String auth;

   private String email;

}
