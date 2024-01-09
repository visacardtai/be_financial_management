package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.Semester;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SemesterDto {
    private Long id;
    private String name;
    private Integer num;
    private Integer year;

    public static SemesterDto from(Semester semester){
        SemesterDto semesterDto = new SemesterDto();
        semesterDto.setId(semester.getSemesterId());
        semesterDto.setName(semester.getName());
        semesterDto.setNum(semester.getNum());
        semesterDto.setYear(semester.getYear());
        return semesterDto;
    }
}
