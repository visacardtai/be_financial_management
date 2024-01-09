package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.ClassCoefficientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassCoefficientRepository extends JpaRepository<ClassCoefficientEntity,Long> {
}
