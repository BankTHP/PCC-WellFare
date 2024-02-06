package com.pcc.wellfare.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateEmployeeRequest {

    Long empid;
    String code;
    String deptid;
    String tprefix;
    String tname;
    String tsurname;
    String tposition;
    String level;
    String remark;
    String status;
}
