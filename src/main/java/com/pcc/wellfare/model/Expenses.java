package com.pcc.wellfare.model;
import jakarta.persistence.*;
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

    // @Column(name = "empid") 
    // private Long empid;

  @Column(name = "empid", insertable = false, updatable = false)
  private Long empid;

    //เชื่อมกับ employee
    @ManyToOne
    @JoinColumn(name = "empid")
    private Employee employee;

    @Column(name = "date_of_admission")
    private Date dateOfAdmission;


    private String description;
    private double opd;
    private double ipd;
    private String remark;
}