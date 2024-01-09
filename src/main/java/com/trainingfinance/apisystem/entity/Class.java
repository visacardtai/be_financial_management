package com.trainingfinance.apisystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "class")
@Data
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecturer_id",referencedColumnName = "lecturer_id")
    private Lecturer lecturer;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "aClass")
    private List<Student> students;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id",referencedColumnName = "faculty_id")
    private Faculty faculty;

}
