package com.pcc.wellfare.service;

import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Employee;
import com.pcc.wellfare.model.Expenses;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.repository.EmployeeRepository;
import com.pcc.wellfare.repository.ExpensesRepository;

import com.pcc.wellfare.requests.ExpensesRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final BudgetRepository budgetRepository;

    private final EmployeeRepository employeeRepository;


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


    
    public ExpensesService(ExpensesRepository expensesRepository, BudgetRepository budgetRepository, EmployeeRepository employeeRepository) {
        this.expensesRepository = expensesRepository;
        this.budgetRepository = budgetRepository;
        this.employeeRepository = employeeRepository;
    }
    
    public List<Expenses> findAllExpensesWithDateOfAdmissionNotNull() {
        return expensesRepository.findByDateOfAdmissionIsNotNull();
    }

    private double parseDoubleWithComma(String value) {
        value = value.replaceAll(",", "");
        return Double.parseDouble(value);
    }

    public Map<String, Double> getTotal(Long empId) {
        List<Object[]> useBudget = expensesRepository.getUse(empId);
        List<Object[]> canUseBudget = budgetRepository.getCanUse(empId);

        if (canUseBudget.isEmpty()) {
            throw new RuntimeException("Can Use Budget is empty");
        } else {
            if (useBudget.isEmpty()) {
                Map<String, Double> responseData = new HashMap<>();
                Object[] can = canUseBudget.get(0);
                double canOpd = parseDoubleWithComma(can[0].toString());
                double canIpd = parseDoubleWithComma(can[1].toString());
                responseData.put("opd", canOpd);
                responseData.put("ipd", canIpd);
                return responseData;
            } else {
                Object[] use = useBudget.get(0);
                Object[] can = canUseBudget.get(0);

                double useOpd = parseDoubleWithComma(use[0].toString());
                double useIpd = parseDoubleWithComma(use[1].toString());
                double canOpd = parseDoubleWithComma(can[0].toString());
                double canIpd = parseDoubleWithComma(can[1].toString());

                double totalOpd = canOpd - useOpd;
                double totalIpd = canIpd - useIpd;

                Map<String, Double> responseData = new HashMap<>();
                responseData.put("opd", totalOpd);
                responseData.put("ipd", totalIpd);
                return responseData;
            }
        }
    }



    public Object getExpenses(Long empId) {
        List<Object[]> useBudget = expensesRepository.getUse(empId);

        if (useBudget.isEmpty()) {
            return "ยังไม่มีการเบิกในระบบ";
        } else {
            Object[] use = useBudget.get(0);

            double useOpd = parseDoubleWithComma(use[0].toString());
            double useIpd = parseDoubleWithComma(use[1].toString());


            Map<String, Double> result = new HashMap<>();
            result.put("opd", useOpd);
            result.put("ipd", useIpd);
            return result;
        }
    }


    public Object create(ExpensesRequest expensesRequest, Long empId) {
        float perDay = budgetRepository.getPerDay(empId);
        float opdExpenses = (expensesRepository.getUseOpd(empId) != null) ? expensesRepository.getUseOpd(empId) : 0.0f;
        float ipdExpenses = (expensesRepository.getUseIpd(empId) != null) ? expensesRepository.getUseIpd(empId) : 0.0f;

        float totalOpd = budgetRepository.getOpd(empId) - opdExpenses;
        float totalIpd = budgetRepository.getIpd(empId) - ipdExpenses;

        float withdrawOpd = expensesRequest.getOpd();
        float withdrawIpd = expensesRequest.getIpd();
        int days = expensesRequest.getDays();
        float roomService = expensesRequest.getRoomService();
        String types = expensesRequest.getTypes();
        float calPerDay = roomService / days;
        float deduct = perDay * days;
        float canWithdraw = 0;


        if (types.equals("opd")) {
            if (totalOpd == 0) {
                return "เบิกได้ 0 บาท";
            } else if (totalOpd >= withdrawOpd) {
                totalOpd = totalOpd - withdrawOpd;
                canWithdraw = withdrawOpd;
                if (totalOpd == 0) {
                    return canWithdraw;
                } else {
                    if (calPerDay > perDay) {
                        if (deduct > totalOpd) {
                            canWithdraw = withdrawOpd+totalOpd;
                            totalOpd = 0;
                        } else {
                            totalOpd = totalOpd - deduct;
                            canWithdraw = withdrawOpd+deduct;
                        }
                    } else {
                        if (totalOpd - roomService < 0) {
                            canWithdraw =  canWithdraw + totalOpd;
                            totalOpd = 0;
                        } else {
                            totalOpd = totalOpd - roomService;
                            canWithdraw = withdrawOpd + roomService;
                        }
                    }
                }
            } else {
                canWithdraw = totalOpd;
                totalOpd = 0;
            }
        } else if (types.equals("ipd")) {
            if (totalIpd == 0) {
                return "เบิกได้ 0 บาท";
            } else if (totalIpd >= withdrawIpd) {
                totalIpd = totalIpd - withdrawIpd;
                canWithdraw = withdrawIpd;
                if (totalIpd == 0) {
                    return canWithdraw;
                } else {
                    if (calPerDay > perDay) {
                        if (deduct > totalIpd) {
                            canWithdraw = withdrawIpd+totalIpd;
                            totalIpd = 0;
                        } else {
                            totalIpd = totalIpd - deduct;
                            canWithdraw = withdrawIpd+deduct;
                        }
                    } else {
                        if (totalIpd - roomService < 0) {
                            canWithdraw =  canWithdraw + totalIpd;
                            totalIpd = 0;
                        } else {
                            totalIpd = totalIpd - roomService;
                            canWithdraw = withdrawIpd + roomService;
                        }
                    }
                }
            } else {
                System.out.println("test");
                canWithdraw = totalIpd;
                totalIpd = 0;
            }

        } else {
            System.out.println("Type ผิดเว้ย");
        }
        Optional<Employee> employeeOptional = employeeRepository.findByEmpid(empId);
        Employee employee = employeeOptional.orElseThrow(() -> new RuntimeException("Employee not found"));
        Expenses expenses = Expenses
                .builder()
                .employee(employee)
                .ipd(withdrawIpd)
                .opd(withdrawOpd)
                .roomService(roomService)
                .canWithdraw(canWithdraw)
                .days(days)
                .startDate(expensesRequest.getStartDate())
                .endDate(expensesRequest.getEndDate())
                .dateOfAdmission(expensesRequest.getAdMission())
                .description(expensesRequest.getDescription())
                .remark(expensesRequest.getRemark())
                .build();

        return expensesRepository.save(expenses);
    }

}


