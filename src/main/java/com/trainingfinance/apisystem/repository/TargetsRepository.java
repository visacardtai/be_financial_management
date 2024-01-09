package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.TargetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetsRepository extends JpaRepository<TargetsEntity,Long> {
    @Query(value ="SELECT * FROM targets WHERE status = :status", nativeQuery = true)
    List<TargetsEntity> findByStatus(@Param("status")boolean status);

    @Query(value ="SELECT * FROM targets WHERE name = :name ORDER BY targets_id DESC LIMIT 1;", nativeQuery = true)
    TargetsEntity findByName(@Param("name")String name);
}
