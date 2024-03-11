package com.pcc.wellfare.requests;

import com.pcc.wellfare.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String email;
  private String password;
  private String firstname;
  private String lastname;
  private Role role;
}
