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
    private Long empid;


    private String tprefix;
    private String tname;
    private String tsurname;
    private String tposition;

    @OneToOne
    @JoinColumn(name = "level")
    private Budget budget;

    @OneToOne
    @JoinColumn(name = "deptid")
    private Dept dept;

    private String remark;
    private String status;

    @Column(name = "e-mail")
    private String email;
}
// @Column(name = "e-mail")
// private String email;
