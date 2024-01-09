package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.entity.id.AccountRoleEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name",unique = true)
    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private Set<Account> accounts;
//    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private Set<Account> accounts;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountRoleEntity> accountRoleEntities;

}
