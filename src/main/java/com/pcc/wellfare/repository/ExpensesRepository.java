package com.pcc.wellfare.repository;

import com.pcc.wellfare.model.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {

    @Query(value = "SELECT SUM(opd) AS total_opd, SUM(ipd) AS total_ipd " +
            "FROM Expenses e " +
            "WHERE e.empId = CAST(?1 AS bigint) AND EXTRACT(YEAR FROM e.date_of_admission) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "GROUP BY e.empId", nativeQuery = true)
    List<Object[]> getUse(Long empId);

}
