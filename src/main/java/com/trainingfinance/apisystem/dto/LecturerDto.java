package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.Lecturer;
import lombok.Data;

@Data
public class LecturerDto {
    private Long lecturerId;
    private String name;

    public static LecturerDto from(Lecturer lecturer){
        LecturerDto lecturerDto = new LecturerDto();
        lecturerDto.setLecturerId(lecturer.getLecturerId());
        lecturerDto.setName(lecturer.getProfile().getFullname());
        return lecturerDto;
    }
}
