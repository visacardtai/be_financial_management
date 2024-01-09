package com.trainingfinance.apisystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.TeachingPeriodDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "teaching_period")
public class TeachingPeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teaching_period_id")
    private Long id;

    @Column(name="status", nullable = false)
    private Boolean status;

    @Column(name="isUsed", nullable = false)
    private Boolean isUsed;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "lecturer_id",referencedColumnName = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne()
    @JoinColumn(name = "targets_id",referencedColumnName = "targets_id")
    private TargetsEntity targets;

    @ManyToOne()
    @JoinColumn(name = "lecture_price_id",referencedColumnName = "lecture_price_id")
    private LecturePriceEntity lecture_price;

    @OneToMany(mappedBy = "teaching_period", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingDetailsEntity> teaching_details;

    @ManyToOne()
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    public static TeachingPeriodEntity from(TeachingPeriodDto teachingPeriodDto){
        TeachingPeriodEntity teachingPeriodEntity = new TeachingPeriodEntity();
        return teachingPeriodEntity;
    }
}
