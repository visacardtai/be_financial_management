package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.ExpensesDetailsEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class ExpensesDetailsDto {
    private Long id;
    private ExpensesPriceDto expensesPrice;

    public static ExpensesDetailsDto from(ExpensesDetailsEntity expensesDetailsEntity){
        ExpensesDetailsDto expensesDetailsDto = new ExpensesDetailsDto();
        expensesDetailsDto.setId(expensesDetailsEntity.getId());
        if(Objects.nonNull(expensesDetailsEntity.getExpenses_price())){
            expensesDetailsDto.setExpensesPrice(ExpensesPriceDto.from(expensesDetailsEntity.getExpenses_price()));
        }
        return expensesDetailsDto;
    }
}
