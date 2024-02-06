package com.pcc.wellfare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dept {

    @Id
    private String code;

    // @OneToOne
    // @JoinColumn(name = "employee")
    private String deptid;

    private String company;
    private String edivision;
    private String tdivision;
    private String divionid;
    private String edept;
    private String tdept;
    private String deptcode;

    private String remark;
}
