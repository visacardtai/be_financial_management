package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.ExpensesPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesPriceRepository extends JpaRepository<ExpensesPriceEntity,Long> {
    @Query(value ="SELECT * FROM expenses_price WHERE status = :status", nativeQuery = true)
    List<ExpensesPriceEntity> findByStatus(@Param("status")boolean status);

    @Query(value ="SELECT t.expenses_price_id, e.description, e.name, t.price, e.status\n" +
            "FROM expenses_price AS e\n" +
            "INNER JOIN (\n" +
            "   SELECT MAX(expenses_price_id) AS expenses_price_id, name, MAX(price) AS price\n" +
            "   FROM expenses_price\n" +
            "   GROUP BY name\n" +
            ") AS t ON e.expenses_price_id = t.expenses_price_id AND e.name = t.name AND e.price = t.price;", nativeQuery = true)
    List<ExpensesPriceEntity> findByNew();
}
