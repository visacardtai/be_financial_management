package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.LecturePriceDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "lecture_price")
public class LecturePriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_price_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "basic_price")
    private Double basic_price;

    @Column(name = "coefficient")
    private Double coefficient;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "lecture_price", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingPeriodEntity> teaching_period;

    public static LecturePriceEntity from(LecturePriceDto lecturePriceDto){
        LecturePriceEntity lecturePrice = new LecturePriceEntity();
        lecturePrice.setName(lecturePriceDto.getName());
        lecturePrice.setBasic_price(lecturePriceDto.getBasic_price());
        lecturePrice.setCoefficient(lecturePriceDto.getCoefficient());
        lecturePrice.setDescription(lecturePriceDto.getDescription());
        lecturePrice.setStatus(lecturePriceDto.getStatus());
        return lecturePrice;
    }
}
