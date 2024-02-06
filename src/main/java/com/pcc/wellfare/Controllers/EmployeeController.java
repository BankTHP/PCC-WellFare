package com.pcc.wellfare.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcc.wellfare.model.Employee;
// import com.pcc.wellfare.repository.EmployeeRepository;
import com.pcc.wellfare.requests.CreateEmployeeRequest;
import com.pcc.wellfare.service.EmployeeService;
import com.pcc.wellfare.response.ApiResponse;
import com.pcc.wellfare.response.ResponseData;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;

@RestController

@BasePathAwareController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // private final EmployeeRepository employeeRepository;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long empId) {
        return employeeService.getEmployeeById(empId)
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // @PostMapping
    // public ResponseEntity<Employee> createEmployee(@RequestBody Employee
    // employee) {
    // Employee createdEmployee = employeeService.createEmployee(employee);
    // return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    // }

    @DeleteMapping("/{empId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long empId) {
        employeeService.deleteEmployee(empId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest createEmployeeRequest) {
        System.out.println("Received createEmployeeRequest: " + createEmployeeRequest);
        Employee createdEmployee = employeeService.createEmployee(createEmployeeRequest);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{empid}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long empid,
            @RequestBody CreateEmployeeRequest createEmployeeRequest) {
        Employee updatedEmployee = employeeService.updateEmployee(empid, createEmployeeRequest);
        return ResponseEntity.ok(updatedEmployee);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // @PostMapping("/createUser")
    // public ResponseEntity<ApiResponse> create(@RequestBody CreateEmployeeRequest
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// createEmployeeRequest)
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// {
    // ApiResponse response = new ApiResponse();
    // ResponseData data = new ResponseData();

    // try {
    // if (employeeService.isUserNull(createEmployeeRequest)) {
    // response.setStatusCode(HttpStatus.BAD_REQUEST.value());
    // response.setResponseMessage("Invalid input data. Please check your
    // request.");
    // return ResponseEntity.badRequest().body(response);
    // }

    // Employee employee = employeeService.create(createEmployeeRequest);

    // data.setResult(employee);
    // response.setStatusCode(HttpStatus.OK.value());
    // response.setResponseMessage("Employee data saved successfully.");
    // response.setResponseData(data);

    // return ResponseEntity.ok().body(response);
    // } catch (Exception e) {
    // response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    // response.setResponseMessage("An unexpected error occurred while processing
    // your request.");
    // return ResponseEntity.internalServerError().body(response);
    // }
    // }
}
