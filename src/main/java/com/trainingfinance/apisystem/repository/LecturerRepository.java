package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer,Long> {
    @Query(value ="SELECT * FROM lecturer WHERE account_id = :account_id", nativeQuery = true)
    Lecturer findByAccountId(@Param("account_id")long account_id);
}
