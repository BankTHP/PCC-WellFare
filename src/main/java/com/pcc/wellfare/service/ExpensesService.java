package com.pcc.wellfare.service;

import com.pcc.wellfare.model.Expenses;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.repository.ExpensesRepository;
import com.pcc.wellfare.requests.TestCreateExpensesRequest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final BudgetRepository budgetRepository;


    //   public Expenses create(TestCreateExpensesRequest testcreateExpensesRequest) {

    //     Expenses expenses = Expenses
    //             .builder()  
    //             .opd(testcreateExpensesRequest.getOpd())
    //             .ipd(testcreateExpensesRequest.getIpd())
    //             .empid(testcreateExpensesRequest.getEmpid())
    //             .remark(testcreateExpensesRequest.getRemark())
    //             .description(testcreateExpensesRequest.getDescription())
    //             .build();

    //     return expensesRepository.save(expenses);
    // }


    public List<Expenses> getExpensesById(Long empId) {         
        return expensesRepository.findByEmpid(empId);     
    }      
    
    public ExpensesService(ExpensesRepository expensesRepository, BudgetRepository budgetRepository) {
        this.expensesRepository = expensesRepository;
        this.budgetRepository = budgetRepository;
    }
    
    public List<Expenses> findAllExpensesWithDateOfAdmissionNotNull() {
        return expensesRepository.findByDateOfAdmissionIsNotNull();
    }

    private double parseDoubleWithComma(String value) {
        value = value.replaceAll(",", "");
        return Double.parseDouble(value);
    }

    public Object getTotal(Long empId) {

        List<Object[]> useBudget = expensesRepository.getUse(empId);
        List<Object[]> canUseBudget = budgetRepository.getCanUse(empId);

        if (canUseBudget.isEmpty()) {
            return "Can Use Budget is empty";
        } else {
            if (useBudget.isEmpty()) {
                return canUseBudget;
            } else {
                Object[] use = useBudget.get(0);
                Object[] can = canUseBudget.get(0);
                System.out.println(can);

                double useOpd = parseDoubleWithComma(use[0].toString());
                double useIpd = parseDoubleWithComma(use[1].toString());
                double canOpd = parseDoubleWithComma(can[0].toString());
                double canIpd = parseDoubleWithComma(can[1].toString());

                double totalOpd = canOpd - useOpd;
                double totalIpd = canIpd - useIpd;
                List<Double> result = new ArrayList<>();
                result.add(totalOpd);
                result.add(totalIpd);
                return result;
            }
        }
    }
}


