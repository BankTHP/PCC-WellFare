package com.pcc.wellfare.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Dept;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@Builder
public class EditEmployeeRequest {

  String code;
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
