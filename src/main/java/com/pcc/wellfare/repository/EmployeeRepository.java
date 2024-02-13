package com.pcc.wellfare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pcc.wellfare.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmpid(Long empid);

    Optional<Employee> findById(Long userId);
    
    List<Employee> findByTnameStartingWith(String letter);
    
    Optional<Employee> findByTsurnameStartingWith(String letter);
    
    List<Employee> findByTnameEqualsAndTsurnameStartingWith(String name, String surnameStart);
    
    List<Employee> findByTnameContainingOrTsurnameContaining(String name, String surname);
}
