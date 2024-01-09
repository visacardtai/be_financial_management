package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.CreditPriceDto;
import com.trainingfinance.apisystem.dto.SubjectDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subject")
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "name")
    private String name;

    @Column(name = "credit_num")
    private Integer creditNum;

    @Column(name = "theory_num")
    private Integer theoryNum;

    @Column(name = "practical_num")
    private Integer practicalNum;

    @Column(name = "academic_year")
    private String academicYear;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "prerequisite_id")
    private Subject prerequisite;

    @JsonIgnore
    @ManyToMany(mappedBy = "subjects")
    private List<Curriculum> curriculums;

    @JsonIgnore
   @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "components_subjects",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "component_id")
    )
    private List<ComponentPoint> componentPoints;
//
//    @OneToMany(fetch=FetchType.LAZY,mappedBy = "subject",cascade=CascadeType.ALL)
//    private List<ComponentSubject> componentSubjects;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceDetailsEntity> invoiceDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingDetailsEntity> teachingDetailsEntities;

    public static Subject from(SubjectDto subjectDto){
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        subject.setTheoryNum(subjectDto.getTheoryNum());
        subject.setPracticalNum(subjectDto.getPracticalNum());
        subject.setCreditNum(subjectDto.getCreditNum());
        subject.setAcademicYear(subjectDto.getAcademicYear());
        return subject;
    }
}
