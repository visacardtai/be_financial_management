package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.ExpensesPriceEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExpensesPriceDto {
    private Long id;
    @NotEmpty
    private String name;
    private String description;
    @NotNull
    private Double price;
    private Boolean status;

    public static ExpensesPriceDto from(ExpensesPriceEntity expensesPriceEntity){
        ExpensesPriceDto expensesPriceDto = new ExpensesPriceDto();
        expensesPriceDto.setId(expensesPriceEntity.getId());
        expensesPriceDto.setName(expensesPriceEntity.getName());
        expensesPriceDto.setDescription(expensesPriceEntity.getDescription());
        expensesPriceDto.setPrice(expensesPriceEntity.getPrice());
        expensesPriceDto.setStatus(expensesPriceEntity.getStatus());
        return expensesPriceDto;
    }
}
