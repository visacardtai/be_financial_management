package com.trainingfinance.apisystem.entity.id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.entity.Account;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_role_id")
    private Long account_role_id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @ManyToOne()
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;
}
