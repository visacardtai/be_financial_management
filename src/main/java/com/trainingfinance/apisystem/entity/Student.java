package com.trainingfinance.apisystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "academic_year")
    private String academicYear;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id")
    private Profile profile;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "account_id",referencedColumnName = "account_id")
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<BehaviorSheet> bSheets;

    @JsonIgnore
    @ManyToMany(mappedBy = "students")
    private List<ClassCredit> classCredits;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "class_id",referencedColumnName = "class_id")
    private Class aClass;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEntity> invoice;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentExpensesEntity> student_expenses;
}
