
package com.pcc.wellfare.model;

import java.util.Set;

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

    
    private String deptid;
    // @OneToOne
    // @JoinColumn(name = "employee")
    private String company;
    private String edivision;
    private String tdivision;
    private String divisionid;
    private String edept;
    private String tdept;
    private String deptcode;

    private String remark;
}
