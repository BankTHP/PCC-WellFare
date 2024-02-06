package com.pcc.wellfare.repository;

import com.pcc.wellfare.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByLevel(String level);

    @Query(value = "select opd,ipd from budget b where \"level\" = (select \"level\"  " +
            "from employee e where e.empId = CAST(?1 AS bigint))", nativeQuery = true)
    List<Object[]> getCanUse(Long empId);



    @Query(value = "select \"ค่าห้อง_ค่าอาหาร\" from budget b where \"level\" = (select \"level\"  " +
            "from employee e where e.empId = CAST(?1 AS bigint))", nativeQuery = true)
    List<Object[]> getCanUseDay(Long empId);

}
