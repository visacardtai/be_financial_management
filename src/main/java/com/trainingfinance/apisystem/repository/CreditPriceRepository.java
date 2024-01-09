package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.CreditPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditPriceRepository extends JpaRepository<CreditPriceEntity,Long> {
    @Query(value ="SELECT * FROM credit_price WHERE status = :status", nativeQuery = true)
    List<CreditPriceEntity> findByStatus(@Param("status")boolean status);

    @Query(value ="SELECT * FROM credit_price WHERE name = :name ORDER BY credit_price_id DESC LIMIT 1", nativeQuery = true)
    CreditPriceEntity findByName(@Param("name")String name);
}
