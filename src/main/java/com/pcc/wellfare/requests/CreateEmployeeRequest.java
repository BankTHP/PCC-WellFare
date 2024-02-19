package com.pcc.wellfare.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateEmployeeRequest {

	String empId;
    String deptCode;
    String deptId;
    String tPrefix;
    String tName;
    String tSurname;
    String tPosition;
    String level;
    String remark;
    String status;
    String email;
}
