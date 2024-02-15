package com.pcc.wellfare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pcc.wellfare.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmpid(String empid);

    Optional<Employee> findById(Long userId);
    
    List<Employee> findByTnameStartingWith(String letter);
    
    Optional<Employee> findByTsurnameStartingWith(String letter);
    
    List<Employee> findByTnameEqualsAndTsurnameStartingWith(String name, String surnameStart);
    
    List<Employee> findByTnameContainingOrTsurnameContaining(String name, String surname);
   
    @Query(value = "SELECT e.user_id FROM Employee e WHERE e.tname = ?1 AND e.tsurname = ?2", nativeQuery = true)
    Optional<Long> findUserIdByTnameAndTsurname(String tname, String tsurname);
    
    @Query(value = "SELECT e.user_id FROM Employee e WHERE e.empid = ?1", nativeQuery = true)
    Optional<Long> findUserIdByEmpid(String empid);
}
