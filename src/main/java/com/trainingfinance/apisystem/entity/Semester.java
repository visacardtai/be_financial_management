package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.CreditPriceDto;
import com.trainingfinance.apisystem.dto.SemesterDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "semester")
@Data
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

//    @JsonFormat(pattern = "dd/MM/yyyy'-'HH:mm", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "year")
    private Integer year;

    @Column(name = "name")
    private String name;

    @Column(name = "num")
    private Integer num;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "semester")
    private List<BehaviorSheet> bSheets;

    @JsonIgnore
    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEntity> invoiceEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentExpensesEntity> studentExpensesEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingPeriodEntity> teachingPeriodEntities;

    public static Semester from(SemesterDto semesterDto){
        Semester semester = new Semester();
        semester.setName(semesterDto.getName());
        semester.setNum(semesterDto.getNum());
        semester.setYear(semesterDto.getYear());
        return semester;
    }
}
