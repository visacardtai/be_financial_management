package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    @Query(value ="SELECT * FROM invoice WHERE student_id = :student_id and status = :status and state = true", nativeQuery = true)
    List<InvoiceEntity> findByIdStudent(@Param("student_id")Long student_id,@Param("status")boolean status);

    @Query(value ="SELECT * FROM invoice WHERE student_id = :student_id and state = true", nativeQuery = true)
    List<InvoiceEntity> findByIdStudentAll(@Param("student_id")Long student_id);

    @Query(value ="SELECT * FROM invoice WHERE state = :state and status = :status", nativeQuery = true)
    List<InvoiceEntity> findByStateAndStatus(@Param("status")boolean status,@Param("state")boolean state);

    @Query(value ="SELECT * FROM invoice WHERE semester_id = :semester_id and state = true", nativeQuery = true)
    List<InvoiceEntity> findBySemesterId(@Param("semester_id")long semester_id);
}
