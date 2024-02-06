package com.pcc.wellfare.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EditEmployeeRequest {

  String empCode;
  String title;
  String firstname;
  String lastname;
  String positionName;
  String email;

  Long dept_actual;
  Long sector_actual;
  private List<Long> deptID;
  private List<Long> companyID;
  private List<Long> sectorID;
  private List<String> roles;
}
