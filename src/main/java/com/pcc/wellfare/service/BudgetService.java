package com.pcc.wellfare.service;


import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.requests.CreateBudgetRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BudgetService {

    private final EntityManager entityManager;
    private final BudgetRepository budgetRepository;

    public Budget create(CreateBudgetRequest createBudgetRequest) {

        Budget budget = Budget
                .builder()
                .opd(createBudgetRequest.getOpd())
                .ipd(createBudgetRequest.getIpd())
                .level(createBudgetRequest.getLevel())
                .room(createBudgetRequest.getRoomFood())
                .build();

        return budgetRepository.save(budget);
    }

    public Budget editBudget(Long budgetId,CreateBudgetRequest createBudgetRequest) {

        Budget budget = budgetRepository
                .findById(budgetId)
                .orElseThrow(() -> new RuntimeException("budgetId not found: "));

        budget.setIpd(createBudgetRequest.getIpd());
        budget.setOpd(createBudgetRequest.getOpd());
        budget.setLevel(createBudgetRequest.getLevel());
        budget.setRoom(createBudgetRequest.getRoomFood());

        return budgetRepository.save(budget);
    }

}

