package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.Lecturer;
import com.trainingfinance.apisystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    @Query(value ="SELECT * FROM student WHERE account_id = :account_id", nativeQuery = true)
    Student findByAccountId(@Param("account_id")long account_id);
}
