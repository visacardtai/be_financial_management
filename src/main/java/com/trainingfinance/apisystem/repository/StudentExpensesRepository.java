package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.StudentExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExpensesRepository extends JpaRepository<StudentExpensesEntity, Long> {

    @Query(value ="SELECT * FROM student_expenses WHERE student_id = :student_id and status = true and state = true", nativeQuery = true)
    List<StudentExpensesEntity> findByIdStudent(@Param("student_id")Long student_id);

    @Query(value ="SELECT * FROM student_expenses WHERE state = :state and status = :status", nativeQuery = true)
    List<StudentExpensesEntity> findByStateAndStatus(@Param("status")boolean status, @Param("state")boolean state);

}
