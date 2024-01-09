package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.Subject;
import lombok.Data;

@Data
public class SubjectDto {
    private Long id;
    private String name;
    private Integer creditNum;
    private Integer theoryNum;
    private Integer practicalNum;
    private String academicYear;

    public static SubjectDto from(Subject subject){
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subject.getSubjectId());
        subjectDto.setName(subject.getName());
        subjectDto.setAcademicYear(subject.getAcademicYear());
        subjectDto.setCreditNum(subject.getCreditNum());
        subjectDto.setPracticalNum(subject.getPracticalNum());
        subjectDto.setTheoryNum(subject.getTheoryNum());
        return subjectDto;
    }
}
