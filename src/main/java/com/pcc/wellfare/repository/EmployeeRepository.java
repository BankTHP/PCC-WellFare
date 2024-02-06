package com.pcc.wellfare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pcc.wellfare.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
