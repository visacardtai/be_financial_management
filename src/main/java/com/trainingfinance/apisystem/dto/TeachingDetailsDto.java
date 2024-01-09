package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeachingDetailsDto {
    private Long id;
    private List<TeachingDetailsSubDto> detailsSub;
    private ClassCoefficientDto classCoefficient;
    private SubjectDto subject;
    private Double total;
    private Double practical_total;
    private Double theory_total;
    private Double calculate_total;

    public static TeachingDetailsDto from(TeachingDetailsEntity teachingDetailsEntity){
        TeachingDetailsDto teachingDetailsDto = new TeachingDetailsDto();
        teachingDetailsDto.setId(teachingDetailsEntity.getId());
        teachingDetailsDto.setSubject(SubjectDto.from(teachingDetailsEntity.getSubject()));
        teachingDetailsDto.setClassCoefficient(ClassCoefficientDto.from(teachingDetailsEntity.getClass_coefficient()));
        teachingDetailsDto.setDetailsSub(teachingDetailsEntity.getTeaching_details_sub()
                .stream()
                .map(TeachingDetailsSubDto::from)
                .collect(Collectors.toList()));
        teachingDetailsDto.setTotal(sum(teachingDetailsDto.getDetailsSub()));
        teachingDetailsDto.setPractical_total(sum_prac(teachingDetailsDto.getDetailsSub()));
        teachingDetailsDto.setTheory_total(sum_theory(teachingDetailsDto.getDetailsSub()));
        teachingDetailsDto.setCalculate_total(sTotal(teachingDetailsDto.getDetailsSub()));
        return teachingDetailsDto;
    }
    public static Double sum(List<TeachingDetailsSubDto> teachingDetailsSubDtoList){
        double s = 0;
        for (TeachingDetailsSubDto teachingDetailsSubDto: teachingDetailsSubDtoList) {
            s += teachingDetailsSubDto.getQuantity() + teachingDetailsSubDto.getPractical();
        }
        return s;
    }

    public static Double sum_prac(List<TeachingDetailsSubDto> teachingDetailsSubDtoList){
        double s = 0;
        for (TeachingDetailsSubDto teachingDetailsSubDto: teachingDetailsSubDtoList) {
            s += teachingDetailsSubDto.getPractical();
        }
        return s;
    }

    public static Double sum_theory(List<TeachingDetailsSubDto> teachingDetailsSubDtoList){
        double s = 0;
        for (TeachingDetailsSubDto teachingDetailsSubDto: teachingDetailsSubDtoList) {
            s += teachingDetailsSubDto.getQuantity();
        }
        return s;
    }
    public static Double sTotal(List<TeachingDetailsSubDto> teachingDetailsSubDtoList){
        double s = 0;
        for (TeachingDetailsSubDto teachingDetailsSubDto: teachingDetailsSubDtoList) {
            s += teachingDetailsSubDto.getQuantity() + teachingDetailsSubDto.getPractical()*0.5;
        }
        return s;
    }
}
