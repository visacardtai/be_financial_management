package com.trainingfinance.apisystem.dto;


import com.trainingfinance.apisystem.entity.LecturePriceEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LecturePriceDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Double basic_price;
    @NotNull
    private Double coefficient;
    private String description;
    private Boolean status;

    public static LecturePriceDto from(LecturePriceEntity lecturePrice){
        LecturePriceDto lecturePriceDto = new LecturePriceDto();
        lecturePriceDto.setId(lecturePrice.getId());
        lecturePriceDto.setName(lecturePrice.getName());
        lecturePriceDto.setCoefficient(lecturePrice.getCoefficient());
        lecturePriceDto.setBasic_price(lecturePrice.getBasic_price());
        lecturePriceDto.setDescription(lecturePrice.getDescription());
        lecturePriceDto.setStatus(lecturePrice.getStatus());
        return lecturePriceDto;
    }
}
