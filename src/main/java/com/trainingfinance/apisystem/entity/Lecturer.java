package com.trainingfinance.apisystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lecturer")
@Data
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecturer_id")
    private Long lecturerId;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "faculty_id")
    private Faculty faculty;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id")
    private Profile profile;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id",referencedColumnName = "account_id")
    private Account account;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    private PositionEntity position;

//    @OneToMany(mappedBy = "lecturer")
//    private List<java.lang.Class> classes;

    @ToString.Exclude()
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "lecturers")
    private List<Exam> exams;

    @OneToMany(mappedBy = "lecturer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingPeriodEntity> teaching_period;

}
