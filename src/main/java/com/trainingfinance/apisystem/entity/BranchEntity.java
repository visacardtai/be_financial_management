package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.BranchDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Entity
@Table(name = "branch")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;


    @JsonIgnore
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditPriceEntity> creditPriceEntities;

    public static BranchEntity from(BranchDto branchDto){
        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setName(branchDto.getName());
        return new BranchEntity();
    }
}
