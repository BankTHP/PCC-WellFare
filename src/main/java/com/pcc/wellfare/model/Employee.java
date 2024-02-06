package com.pcc.wellfare.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empid;
    private String code;
    private String deptid;
    private String tprefix;
    private String tname;
    private String tsurname;
    private String tposition;
    private String level;
    private String remark;
    private String status;

    @Column(name = "e-mail")
    private String email;
}
// @Column(name = "e-mail")
// private String email;
