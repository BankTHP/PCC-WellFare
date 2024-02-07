package com.pcc.wellfare.model;

import java.util.Set;

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
    private Long id;

    private String level;
    private String opd;
    private String ipd;

    @Column(name = "ค่าห้อง_ค่าอาหาร")
    private String room;
    
    @OneToMany(mappedBy = "budget")
    private Set<Employee> employees;

}
