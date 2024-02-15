package com.pcc.wellfare.repository;

import com.pcc.wellfare.model.Expenses;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {

    @Query(value = "SELECT SUM(e.can_withdraw) AS total_opd, SUM(ipd) AS total_ipd " +
            "FROM Expenses e " +
            "WHERE e.user_id = CAST(?1 AS bigint) AND EXTRACT(YEAR FROM e.date_of_admission) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "GROUP BY e.user_id", nativeQuery = true)
    List<Object[]> getUse(Long empId);


    @Query(value = "SELECT SUM(e.can_withdraw) AS total_opd " +
            "FROM Expenses e " +
            "WHERE e.user_id = CAST(?1 AS bigint) AND EXTRACT(YEAR FROM e.date_of_admission) = EXTRACT(YEAR FROM CURRENT_DATE) and opd != 0" , nativeQuery = true)
    Float getUseOpd(Long userId);

    @Query(value = "SELECT SUM(e.can_withdraw) AS total_ipd " +
            "FROM Expenses e " +
            "WHERE e.user_id = CAST(?1 AS bigint) AND EXTRACT(YEAR FROM e.date_of_admission) = EXTRACT(YEAR FROM CURRENT_DATE) and ipd != 0" , nativeQuery = true)
    Float getUseIpd(Long userId);


//    List<Expenses> findByEmpId(Long empid);
    
    List<Expenses> findByDateOfAdmissionIsNotNull();

    List<Expenses> findByEmployeeUserId(Long userId);
    

    
}
