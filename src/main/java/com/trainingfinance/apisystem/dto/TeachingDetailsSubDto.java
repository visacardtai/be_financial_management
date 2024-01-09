package com.trainingfinance.apisystem.dto;
import com.trainingfinance.apisystem.entity.TeachingDetailsSubEntity;
import lombok.Data;

import java.util.Date;

@Data
public class TeachingDetailsSubDto {
    private Long id;
    private String name;
    private Date attendance;
    private Double quantity;
    private Double practical;
    private Long details_id;

    public static TeachingDetailsSubDto from(TeachingDetailsSubEntity teachingDetailsSubEntity){
        TeachingDetailsSubDto teachingDetailsSubDto = new TeachingDetailsSubDto();
        teachingDetailsSubDto.setId(teachingDetailsSubEntity.getId());
        teachingDetailsSubDto.setName(teachingDetailsSubEntity.getName());
        teachingDetailsSubDto.setQuantity(teachingDetailsSubEntity.getQuantity());
        teachingDetailsSubDto.setAttendance(teachingDetailsSubEntity.getAttendance());
        teachingDetailsSubDto.setPractical(teachingDetailsSubEntity.getPractical());
        return teachingDetailsSubDto;
    }
}
