package com.pcc.wellfare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dept {

    @Id
    private String deptid;
    private String code;

    private String company;
    private String edivision;
    private String tdivision;
    private String divionid;
    private String edept;
    private String tdept;
    private String deptcode;

    private String remark;
}
