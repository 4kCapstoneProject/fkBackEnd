package com.oldaim.fkbackend.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {

   private String userId;

   private String userPw;

   private String auth;

   private String email;

}
