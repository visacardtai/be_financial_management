package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.ClassCoefficientEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import lombok.Data;

@Data
public class ClassCoefficientDto {
    private Long id;
    private String name;
    private Long quantity;
    private Double coefficient;

    public static ClassCoefficientDto from(ClassCoefficientEntity classCoefficientEntity){
        ClassCoefficientDto classCoefficientDto = new ClassCoefficientDto();
        classCoefficientDto.setId(classCoefficientEntity.getId());
        classCoefficientDto.setName(classCoefficientEntity.getName());
        classCoefficientDto.setQuantity(classCoefficientEntity.getQuantity());
        classCoefficientDto.setCoefficient(classCoefficientEntity.getCoefficient());
        return classCoefficientDto;
    }
}
