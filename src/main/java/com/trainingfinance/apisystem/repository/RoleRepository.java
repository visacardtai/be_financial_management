package com.trainingfinance.apisystem.repository;

import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(value ="SELECT * FROM role where role_id =(SELECT role_id FROM accounts_roles where account_id = :account_id)", nativeQuery = true)
    List<Role> findByAccountID(@Param("account_id") Long account_id);

}
