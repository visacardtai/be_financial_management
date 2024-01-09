package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.trainingfinance.apisystem.dto.ExpensesPriceDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "expenses_price")
public class ExpensesPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenses_price_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status;

    @JsonBackReference
    @OneToMany(mappedBy = "expenses_price", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpensesDetailsEntity> expenses_details;

    public static ExpensesPriceEntity from(ExpensesPriceDto expensesPriceDto){
        ExpensesPriceEntity expensesPrice = new ExpensesPriceEntity();
        expensesPrice.setName(expensesPriceDto.getName());
        expensesPrice.setPrice(expensesPriceDto.getPrice());
        expensesPrice.setDescription(expensesPriceDto.getDescription());
        expensesPrice.setStatus(expensesPriceDto.getStatus());
        return expensesPrice;
    }
}
