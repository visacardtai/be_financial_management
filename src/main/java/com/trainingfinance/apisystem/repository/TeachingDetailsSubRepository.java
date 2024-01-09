package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.TeachingDetailsSubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingDetailsSubRepository extends JpaRepository<TeachingDetailsSubEntity,Long> {
}
