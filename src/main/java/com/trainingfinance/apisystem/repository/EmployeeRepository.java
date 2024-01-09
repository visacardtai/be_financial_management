package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.Employee;
import com.trainingfinance.apisystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    @Query(value ="SELECT * FROM employee WHERE account_id = :account_id", nativeQuery = true)
    Employee findByAccountId(@Param("account_id")long account_id);
}
