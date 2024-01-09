package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.LecturePriceEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturePriceRepository extends JpaRepository<LecturePriceEntity,Long> {
    @Query(value ="SELECT * FROM lecture_price WHERE status = :status", nativeQuery = true)
    List<LecturePriceEntity> findByStatus(@Param("status")boolean status);

    @Query(value ="SELECT * FROM lecture_price WHERE name = :name ORDER BY lecture_price_id DESC LIMIT 1;", nativeQuery = true)
    LecturePriceEntity findByName(@Param("name")String name);
}
