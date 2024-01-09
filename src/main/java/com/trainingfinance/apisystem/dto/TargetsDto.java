package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.TargetsEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TargetsDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Double quantity;
    private String description;
    private Boolean status;

    public static TargetsDto from(TargetsEntity targets){
        TargetsDto targetsDto = new TargetsDto();
        targetsDto.setId(targets.getId());
        targetsDto.setName(targets.getName());
        targetsDto.setQuantity(targets.getQuantity());
        targetsDto.setDescription(targets.getDescription());
        targetsDto.setStatus(targets.getStatus());
        return targetsDto;
    }
}
