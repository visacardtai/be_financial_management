package com.trainingfinance.apisystem.entity;

import com.trainingfinance.apisystem.dto.TeachingDetailsDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@Entity
@Table(name = "teaching_details")
public class TeachingDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teaching_details_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "teaching_period_id",referencedColumnName = "teaching_period_id")
    private TeachingPeriodEntity teaching_period;

    @OneToMany(mappedBy = "teaching_details", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingDetailsSubEntity> teaching_details_sub;

    @ManyToOne()
    @JoinColumn(name = "class_coefficient_id",referencedColumnName = "class_coefficient_id")
    private ClassCoefficientEntity class_coefficient;

    @ManyToOne()
    @JoinColumn(name = "subject_id",referencedColumnName = "subject_id")
    private Subject subject;

    public static TeachingDetailsEntity from(TeachingDetailsDto teachingDetailsDto){
        TeachingDetailsEntity teachingDetailsEntity = new TeachingDetailsEntity();
        return teachingDetailsEntity;
    }
}
