package com.pcc.wellfare.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pcc.wellfare.model.Employee;

@Repository
public class JdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertDataJDBC(Employee employee) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "INSERT INTO seeddb.nighttable (name, lastname, age, gender, birthday, created_Date, createdby, updated_Date, updatedby) ");
        sql.append("VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?) ");

    }
}
