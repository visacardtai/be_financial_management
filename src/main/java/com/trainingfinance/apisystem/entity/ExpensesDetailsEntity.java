package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "expenses_details")
public class ExpensesDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenses_details_id")
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "student_expenses_id",referencedColumnName = "student_expenses_id")
    private StudentExpensesEntity student_expenses;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenses_price_id",referencedColumnName = "expenses_price_id")
    private ExpensesPriceEntity expenses_price;
}
