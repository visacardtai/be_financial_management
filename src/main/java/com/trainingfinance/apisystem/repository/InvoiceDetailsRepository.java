package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.CreditPriceEntity;
import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetailsEntity,Long> {
}
