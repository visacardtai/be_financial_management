package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.BranchEntity;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BranchDto {
    private Long id;
    private String name;

    public static BranchDto from(BranchEntity branchEntity){
        BranchDto branchDto = new BranchDto();
        branchDto.setId(branchEntity.getId());
        branchDto.setName(branchEntity.getName());
        return branchDto;
    }
}
