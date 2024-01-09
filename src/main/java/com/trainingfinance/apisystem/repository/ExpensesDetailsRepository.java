package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.ExpensesDetailsEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesDetailsRepository extends JpaRepository<ExpensesDetailsEntity,Long> {
    @Query(value ="SELECT * FROM expenses_details WHERE student_expenses_id = :student_expenses_id and expenses_price_id = :expenses_price_id", nativeQuery = true)
    ExpensesDetailsEntity findBy2ID(@Param("student_expenses_id")long student_expenses_id, @Param("expenses_price_id")long expenses_price_id);
}
