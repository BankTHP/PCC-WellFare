package com.pcc.wellfare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private Long empid; // ไม่มี @GeneratedValue
    private String code;
    private String deptid;
    private String tprefix;
    private String tname;
    private String tsurname;
    private String tposition;
    private String level;
    private String remark;
    private String status;
}
// @Column(name = "e-mail")
// private String email;
