package com.pcc.wellfare.controllers;



import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Expenses;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.repository.ExpensesRepository;
import com.pcc.wellfare.response.ApiResponse;
import com.pcc.wellfare.response.ResponseData;
import com.pcc.wellfare.service.ExpensesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {


    private final ExpensesService expensesService;
    private final BudgetRepository budgetRepository;
    private final ExpensesRepository expensesRepository;

    public ExpensesController(ExpensesRepository expensesRepository, ExpensesService expensesService, BudgetRepository budgetRepository) {
        this.expensesService = expensesService;
        this.budgetRepository = budgetRepository;
        this.expensesRepository = expensesRepository;
    }

    // @PostMapping(value = "/create")
    // public ResponseEntity<ApiResponse> createExpenses(
    //         @RequestBody TestCreateExpensesRequest createExpensesRequest
    //         ) {
    //     ApiResponse response = new ApiResponse();
    //     ResponseData data = new ResponseData();
    //     try {
    //         Expenses expenses = expensesService.create(createExpensesRequest);
    //         data.setResult(expenses);
    //         response.setResponseMessage("กรอกข้อมูลเรียบร้อย");
    //         response.setResponseData(data);
    //         return ResponseEntity.ok().body(response);
    //     } catch (Exception e) {
    //         response.setResponseMessage(e.getMessage());
    //         return ResponseEntity.internalServerError().body(response);
    //     }
    // }

    @GetMapping(value = "/getAllExpenseInUsed")
    public ResponseEntity<List<Expenses>> getAllExpenseInUsed() {
    	List<Expenses> allExpense = expensesService.findAllExpensesWithDateOfAdmissionNotNull();
    	return new ResponseEntity<>(allExpense, HttpStatus.OK);
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


    @GetMapping("/searchExpenses/{empId}")
public ResponseEntity<List<Expenses>> findByEmpid(@PathVariable Long empId) {
    List<Expenses> expensesList = expensesService.getExpensesById(empId);
    if (expensesList.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
        return new ResponseEntity<>(expensesList, HttpStatus.OK);
    }
}
}
