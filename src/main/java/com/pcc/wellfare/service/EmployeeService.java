package com.pcc.wellfare.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pcc.wellfare.CSVRepresentation.DeptCsvRepresentation;
import com.pcc.wellfare.CSVRepresentation.EmployeeCsvRepresentation;
import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Dept;
import com.pcc.wellfare.repository.BudgetRepository;
//import com.pcc.wellfare.repository.DeptRepository;
import com.pcc.wellfare.repository.DeptRepository;
import com.pcc.wellfare.requests.CreateEmployeeRequest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pcc.wellfare.repository.EmployeeRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.pcc.wellfare.model.Employee;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BudgetRepository budgetRepository;
    private final DeptRepository deptRepository;
    private EntityManager entityManager;

    @Transactional
    public void saveEmployee(Employee employee) {
        // ตรวจสอบว่าไม่ให้ Hibernate กำหนดค่า empid ให้
        if (employee.getEmpid() == null || employee.getEmpid() == 0) {
            throw new IllegalArgumentException("empid must be assigned before saving.");
        }

        // ทำการบันทึก Entity
        entityManager.persist(employee);
    }

    public EmployeeService(EmployeeRepository employeeRepository, BudgetRepository budgetRepository, DeptRepository deptRepository) {
        this.employeeRepository = employeeRepository;
        this.budgetRepository = budgetRepository;
        this.deptRepository = deptRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long empId) {
        return employeeRepository.findById(empId);
    }


    public Employee createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        System.out.println(createEmployeeRequest.getLevel());
        Optional<Budget> budgetOptional = budgetRepository.findByLevel(createEmployeeRequest.getLevel());
        Budget budget = budgetOptional.orElseThrow(() -> new RuntimeException("Budget not found"));
        Optional<Dept> deptOptional = deptRepository.findByCode(createEmployeeRequest.getCode());
        Dept dept = deptOptional.orElseThrow(() -> new RuntimeException("Dept not found"));
        Employee employee = Employee
                .builder()
                .empid(createEmployeeRequest.getEmpId())
                .dept(dept)
                .tprefix(createEmployeeRequest.getTPrefix())
                .email(createEmployeeRequest.getEmail())
                .tname(createEmployeeRequest.getTName())
                .tsurname(createEmployeeRequest.getTSurname())
                .tposition(createEmployeeRequest.getTPosition())
                .budget(budget)
                .remark(createEmployeeRequest.getRemark())
                .status(createEmployeeRequest.getStatus())
                .build();

        return employeeRepository.save(employee);
    }

//    @Transactional
//    public Employee updateEmployee(Long empid, CreateEmployeeRequest createEmployeeRequest) {
//        Employee employee = employeeRepository.findById(empid)
//                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + empid));
//
//        employee.setCode(createEmployeeRequest.getCode());
//        employee.setDeptid(createEmployeeRequest.getDeptid());
//        employee.setTprefix(createEmployeeRequest.getTprefix());
//        employee.setTname(createEmployeeRequest.getTname());
//        employee.setTsurname(createEmployeeRequest.getTsurname());
//        employee.setTposition(createEmployeeRequest.getTposition());
//        employee.setLevel(createEmployeeRequest.getLevel());
//        employee.setRemark(createEmployeeRequest.getRemark());
//        employee.setStatus(createEmployeeRequest.getStatus());
//
//        return employeeRepository.save(employee);
//    }
    ///////////////////////////////////////////////////////////////////////////////////
    // public Employee create(CreateEmployeeRequest createEmployeeRequest) {
    // if (createEmployeeRequest == null) {
    // throw new IllegalArgumentException("createEmployeeRequest cannot be null");
    // }
    // if (createEmployeeRequest.getTname() == null ||
    /////////////////////////////////////////////////////////////////////////////////// createEmployeeRequest.getTname().isEmpty())
    /////////////////////////////////////////////////////////////////////////////////// {
    // throw new IllegalArgumentException("Tname must not be null or empty");
    // }
    // Employee employee = new Employee(
    // createEmployeeRequest.getEmpid(),
    // createEmployeeRequest.getCode(),
    // createEmployeeRequest.getDeptid(),
    // createEmployeeRequest.getTprefix(),
    // createEmployeeRequest.getTname(),
    // createEmployeeRequest.getTsurname(),
    // createEmployeeRequest.getTposition(),
    // createEmployeeRequest.getLevel(),
    // createEmployeeRequest.getStartdate());
    // try {
    // return employeeRepository.save(employee);
    // } catch (DataAccessException e) {
    // throw new RuntimeException("Error saving employee data", e);
    // }
    // }

//    public boolean isUserNull(CreateEmployeeRequest request) {
//        return (request == null ||
//                request.getTname() == null ||
//                request.getTname().isEmpty() ||
//                request.getTsurname() == null ||
//                request.getTsurname().isEmpty());
//    }

    public void deleteEmployee(Long empId) {
        employeeRepository.deleteById(empId);
    }
    
    public Integer uploadEmps(MultipartFile file) throws IOException {
        Set<Employee> Emps = parseCsv(file);
        employeeRepository.saveAll(Emps);
        return Emps.size();
    }

    private Set<Employee> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<EmployeeCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EmployeeCsvRepresentation.class);
            CsvToBean<EmployeeCsvRepresentation> csvToBean =
                    new CsvToBeanBuilder<EmployeeCsvRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            return csvToBean.parse().stream()
                    .map(csvLine -> {
                        Budget budget = budgetRepository.findByLevel(csvLine.getLevel()).orElse(null);
                        Dept dept = deptRepository.findById(csvLine.getCode()).orElse(null);

                        return Employee.builder()
                                .empid(csvLine.getEmpid())
                                .tprefix(csvLine.getTprefix())
                                .tname(csvLine.getTname())
                                .tsurname(csvLine.getTsurname())
                                .tposition(csvLine.getTposition())
                                .budget(budget)
                                .dept(dept)
                                .status(csvLine.getStatus())
                                .remark(csvLine.getRemark())
                                .email(csvLine.getEmail())
                                .build();
                    })
                    .collect(Collectors.toSet());
        }
    }



}
