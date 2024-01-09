package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.HistoryPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryPaymentRepository extends JpaRepository<HistoryPaymentEntity,Long> {
    @Query(value ="SELECT * FROM history_payment WHERE invoice_id = :invoiceId", nativeQuery = true)
    HistoryPaymentEntity findByIdInvoice(@Param("invoiceId")Long invoiceId);
}
