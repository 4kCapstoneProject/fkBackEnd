package com.oldaim.fkbackend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

   @NotBlank
   private String userId;
   @NotBlank
   private String userPw;

   private String auth;
   @Email
   private String email;

}
