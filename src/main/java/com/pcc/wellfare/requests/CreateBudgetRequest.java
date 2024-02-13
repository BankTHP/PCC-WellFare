package com.pcc.wellfare.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateBudgetRequest {

  String level;
  String opd;
  String ipd;
  String room;
}
