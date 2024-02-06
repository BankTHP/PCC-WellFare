package com.pcc.wellfare.controllers;


import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.repository.ExpensesRepository;
import com.pcc.wellfare.response.ApiResponse;
import com.pcc.wellfare.response.ResponseData;
import com.pcc.wellfare.service.ExpensesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {


    private final ExpensesService expensesService;
    private final BudgetRepository budgetRepository;
    private final ExpensesRepository expensesRepository;

    public ExpensesController(ExpensesRepository expensesRepository, ExpensesService expensesService, BudgetRepository budgetRepository, ExpensesRepository expensesRepository1) {
        this.expensesService = expensesService;
        this.budgetRepository = budgetRepository;
        this.expensesRepository = expensesRepository1;
    }

    @PostMapping(value = "/create")
    public String test(){
        return "Hello world2";
    }



    @GetMapping(value = "/getTotal")
    public ResponseEntity<ApiResponse> getTotal(Long empId) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            Object budgets = expensesService.getTotal(empId);
            data.setResult(budgets);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping(value = "/getExpenses")
    public ResponseEntity<ApiResponse> getexpenses(Long empId) {
        ApiResponse response = new ApiResponse();
        ResponseData data = new ResponseData();
        try {
            List<Object[]> budgets = expensesRepository.getUse(empId);
            System.out.println(budgets);
            data.setResult(budgets);
            response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
            response.setResponseData(data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    private double parseDoubleWithComma(String value) {
        value = value.replaceAll(",", "");
        return Double.parseDouble(value);
    }

    @PutMapping(value = "/editExpenses")
    public String editExpenses() {
        return "Hello world2";
    }

    @DeleteMapping(value = "/deleteExpenses")
    public String deleteExpenses() {
        return "Hello world2";
    }

    @GetMapping(value = "/searchExpenses")
    public String searchExpenses(String test) {
        return "Hello world2";
    }
}
