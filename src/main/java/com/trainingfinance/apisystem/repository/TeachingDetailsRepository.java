package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.StudentExpensesEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingDetailsRepository extends JpaRepository<TeachingDetailsEntity,Long> {

    @Modifying
    @Transactional
    @Query(value ="DELETE FROM teaching_details WHERE teaching_details_id = :teaching_details_id", nativeQuery = true)
    Integer deleteByEID(@Param("teaching_details_id")long teaching_details_id);
}
