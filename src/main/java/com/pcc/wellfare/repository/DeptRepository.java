package com.pcc.wellfare.repository;

import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Dept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeptRepository extends JpaRepository<Dept, String> {

    Optional<Dept> findByCode(String deptcode);

}
