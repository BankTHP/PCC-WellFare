package com.pcc.wellfare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // ระบุชื่อคอลัมน์เพื่อให้ตรงกับฐานข้อมู]
    private Long id;

    //เชื่อมกับ employee
    @Column(name = "empid") // ระบุชื่อคอลัมน์เพื่อให้ตรงกับฐานข้อมูล
    private Long empid;

    private Date dateOfAdmission;
    private String description;
    private double opd;
    private double ipd;
    private String remark;
}
