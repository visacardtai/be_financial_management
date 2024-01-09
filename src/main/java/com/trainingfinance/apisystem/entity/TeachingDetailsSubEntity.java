package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.TeachingDetailsSubDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name = "teaching_details_sub")
public class TeachingDetailsSubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teaching_details_sub_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "attendance")
    private Date attendance;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "practical")
    private Double practical;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "teaching_details_id",referencedColumnName = "teaching_details_id")
    private TeachingDetailsEntity teaching_details;

    public static TeachingDetailsSubEntity from(TeachingDetailsSubDto teachingDetailsSubDto){
        TeachingDetailsSubEntity teachingDetailsSubEntity = new TeachingDetailsSubEntity();
        teachingDetailsSubEntity.setName(teachingDetailsSubDto.getName());
        teachingDetailsSubEntity.setQuantity(teachingDetailsSubDto.getQuantity());
        teachingDetailsSubEntity.setAttendance(teachingDetailsSubDto.getAttendance());
        teachingDetailsSubEntity.setPractical(teachingDetailsSubDto.getPractical());
        return teachingDetailsSubEntity;
    }
}
