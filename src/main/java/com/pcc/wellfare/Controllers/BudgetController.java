package com.pcc.wellfare.controllers;


import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Employee;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.requests.CreateBudgetRequest;
import com.pcc.wellfare.requests.CreateEmployeeRequest;
import com.pcc.wellfare.response.ApiResponse;
import com.pcc.wellfare.response.ResponseData;
import com.pcc.wellfare.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/budget")
public class BudgetController {

    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;

    public BudgetController(BudgetRepository budgetRepository, BudgetService budgetService) {
        this.budgetRepository = budgetRepository;
        this.budgetService = budgetService;
    }


    @PostMapping(value = "/create")
    public ResponseEntity<ApiResponse> createBudget(
            @RequestBody CreateBudgetRequest createBudgetRequest
            ) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            Budget budget = budgetService.create(createBudgetRequest);
            data.setResult(budget);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping(value = "/getBudget")
    public ResponseEntity<ApiResponse> getBudget() {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            List<Budget> budgets = budgetRepository.findAll();
            data.setResult(budgets);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping(value = "/editBudget")
    public ResponseEntity<ApiResponse> editBudget(Long budgetId,CreateBudgetRequest createBudgetRequest) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            Budget budgets = budgetService.editBudget(budgetId,createBudgetRequest);
            data.setResult(budgets);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping(value = "/deleteBudget")
    public ResponseEntity<ApiResponse> deleteBudget(Long budgetId) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            budgetRepository.deleteById(budgetId);
            response.setResponseMessage("ลบข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping(value = "/searchBudget")
    public ResponseEntity<ApiResponse> searchBudget(String level) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            List<Budget> budgets = budgetRepository.findByLevel(level);
            data.setResult(budgets);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
