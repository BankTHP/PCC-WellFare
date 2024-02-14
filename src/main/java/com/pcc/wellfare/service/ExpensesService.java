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
    
    public Optional<Expenses> findbyid(Long id) {
    	return expensesRepository.findById(id);
    }

    private double parseDoubleWithComma(String value) {
        value = value.replaceAll(",", "");
        return Double.parseDouble(value);
    }

    public Map<String, Double> getTotal(Long userId) {
        List<Object[]> useBudget = expensesRepository.getUse(userId);
        List<Object[]> canUseBudget = budgetRepository.getCanUse(userId);
//        const RoomService = emp.file
        if (canUseBudget.isEmpty()) {
            throw new RuntimeException("Can Use Budget is empty");
        } else {
            if (useBudget.isEmpty()) {
                Map<String, Double> responseData = new HashMap<>();
                Object[] can = canUseBudget.get(0);
                double canOpd = parseDoubleWithComma(can[0].toString());
                double canIpd = parseDoubleWithComma(can[1].toString());
                double roomService = budgetRepository.getPerDay(userId);
                responseData.put("opd", canOpd);
                responseData.put("ipd", canIpd);
                responseData.put("room", roomService);
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
                double roomService = budgetRepository.getPerDay(userId);

                Map<String, Double> responseData = new HashMap<>();
                responseData.put("opd", totalOpd);
                responseData.put("ipd", totalIpd);
                responseData.put("room", roomService);

                return responseData;
            }
        }
    }
    
    public Map<String, Double> getTotalExpense(Long uid) {
    	//get MAXLimit of OPD and IPD
        float ipdMaxLimit = budgetRepository.getIPDlimit(uid);
        float opdMaxLimit = budgetRepository.getOPDlimit(uid);
        //useed OPD and IPD
        float usedOpd = (expensesRepository.getUseOpd(uid) != null) ? expensesRepository.getUseOpd(uid) : 0.0f;
        float usedIpd = (expensesRepository.getUseIpd(uid) != null) ? expensesRepository.getUseIpd(uid) : 0.0f;
        //remain
        float opdRemain = opdMaxLimit - usedOpd;
        float ipdRemain = ipdMaxLimit - usedIpd;
        float roomService = budgetRepository.getPerDay(uid);
        Map<String, Double> responseData = new HashMap<>();
        responseData.put("opd", (double) opdRemain);
        responseData.put("ipd", (double) ipdRemain);
        responseData.put("room", (double) roomService);

        return responseData;
    }



    public Object getExpenses(Long userId) {
        List<Object[]> useBudget = expensesRepository.getUse(userId);

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
    
    public Object withDraw(ExpensesRequest expensesRequest, Long userId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(userId);
        Employee employee = employeeOptional.orElseThrow(() -> new RuntimeException("Employee not found"));
        String type1 = expensesRequest.getTypes();
        float perDay = budgetRepository.getPerDay(userId);
        float roomServiceLimit = perDay * expensesRequest.getDays();
        float roomServiceRequest = expensesRequest.getRoomService();
        //roomService can withdraw
        float roomServiceCanUse = (roomServiceRequest >= roomServiceLimit) ? roomServiceLimit : roomServiceRequest;
        //get MAXLimit of OPD and IPD
        float ipdMaxLimit = budgetRepository.getIPDlimit(userId);
        float opdMaxLimit = budgetRepository.getOPDlimit(userId);
        //get Used OPD and IPD
        float usedOpd = (expensesRepository.getUseOpd(userId) != null) ? expensesRepository.getUseOpd(userId) : 0.0f;
        float usedIpd = (expensesRepository.getUseIpd(userId) != null) ? expensesRepository.getUseIpd(userId) : 0.0f;
        //calculate for defined Limit can Withdraw expense
        float ipdLimit = ipdMaxLimit - usedIpd;
        float opdLimit = opdMaxLimit - usedOpd;
        //health cost request select by type
        float healthCostRequest = (type1.equals("ipd")) ? expensesRequest.getIpd() : expensesRequest.getOpd();
        //get health cost limit by type
        float healthCostLimit = (type1.equals("ipd")) ? ipdLimit : opdLimit;
        //Sum all cost request
        float healthCost = healthCostRequest + roomServiceCanUse;
        float canWithdraw = 0.0f;
        if(healthCostLimit >= healthCost) {
        	canWithdraw = healthCost;
        }else {
        	canWithdraw = healthCostLimit;
        }
        
        Expenses expense = Expenses.builder()
        		.employee(employee)
                .ipd(expensesRequest.getIpd())
                .opd(expensesRequest.getOpd())
                .roomService(expensesRequest.getRoomService())
                .canWithdraw(canWithdraw)
                .days(expensesRequest.getDays())
                .startDate(expensesRequest.getStartDate())
                .endDate(expensesRequest.getEndDate())
                .dateOfAdmission(expensesRequest.getAdMission())
                .description(expensesRequest.getDescription())
                .remark(expensesRequest.getRemark())
        		.build();
        return expensesRepository.save(expense);
    }


    public Object create(ExpensesRequest expensesRequest, Long userId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(userId);
        Employee employee = employeeOptional.orElseThrow(() -> new RuntimeException("Employee not found"));

        float perDay = budgetRepository.getPerDay(userId);
        float opdExpenses = (expensesRepository.getUseOpd(userId) != null) ? expensesRepository.getUseOpd(userId) : 0.0f;
        float ipdExpenses = (expensesRepository.getUseIpd(userId) != null) ? expensesRepository.getUseIpd(userId) : 0.0f;

        float totalOpd = budgetRepository.getOpd(userId) - opdExpenses;
        float totalIpd = budgetRepository.getIpd(userId) - ipdExpenses;

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
                canWithdraw = totalIpd;
            }

        } else {
            System.out.println("Type ผิดเว้ย");
        }
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


