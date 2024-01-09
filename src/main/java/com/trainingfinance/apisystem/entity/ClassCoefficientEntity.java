package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.ClassCoefficientDto;
import com.trainingfinance.apisystem.dto.CreditPriceDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "class_coefficient")
public class ClassCoefficientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_coefficient_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "coefficient")
    private Double coefficient;

    @JsonIgnore
    @OneToMany(mappedBy = "class_coefficient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingDetailsEntity> teaching_details;

    public static ClassCoefficientEntity from(ClassCoefficientDto classCoefficientDto){
        ClassCoefficientEntity classCoefficientEntity = new ClassCoefficientEntity();
        classCoefficientEntity.setName(classCoefficientDto.getName());
        classCoefficientEntity.setCoefficient(classCoefficientDto.getCoefficient());
        classCoefficientEntity.setQuantity(classCoefficientDto.getQuantity());
        return classCoefficientEntity;
    }
}
