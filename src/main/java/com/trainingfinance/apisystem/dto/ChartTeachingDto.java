package com.trainingfinance.apisystem.dto;
import lombok.Data;

@Data
public class ChartTeachingDto {
    private String name;
    private Double max_total;
    private Double total;

    public static ChartTeachingDto from(TeachingDetailsDto teachingDetailsEntity){
        ChartTeachingDto chartTeachingDto = new ChartTeachingDto();
        chartTeachingDto.setName(teachingDetailsEntity.getSubject().getName());
        chartTeachingDto.setMax_total((double) (teachingDetailsEntity.getSubject().getTheoryNum() + teachingDetailsEntity.getSubject().getPracticalNum()));
        chartTeachingDto.setTotal(teachingDetailsEntity.getTotal());
        return chartTeachingDto;
    }
}
