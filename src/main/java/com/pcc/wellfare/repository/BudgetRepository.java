package com.pcc.wellfare.repository;

import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByLevel(String level);

}
