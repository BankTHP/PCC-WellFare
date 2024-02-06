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
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;

    //เชื่อมกับ employee
    String level;

    String opd;
    String ipd;

    @Column(name = "ค่าห้อง_ค่าอาหาร")
    String room;
}
