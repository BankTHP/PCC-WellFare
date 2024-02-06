package com.pcc.wellfare.controllers;
import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Employee;
import com.pcc.wellfare.requests.CreateBudgetRequest;
import com.pcc.wellfare.requests.CreateEmployeeRequest;
import com.pcc.wellfare.response.ApiResponse;
import com.pcc.wellfare.response.ResponseData;
import com.pcc.wellfare.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeContoller {

    private final EmployeeService employeeService;

    public EmployeeContoller(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> createEmployee(
            @RequestBody CreateEmployeeRequest createEmployeeRequest
            ) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        System.out.println(createEmployeeRequest);
        try {
            Employee employee = employeeService.createEmployee(createEmployeeRequest);
            data.setResult(employee);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


}
