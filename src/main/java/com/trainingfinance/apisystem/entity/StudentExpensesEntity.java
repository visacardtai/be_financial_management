package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.StudentExpensesDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "student_expenses")
public class StudentExpensesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_expenses_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date")
    private Date created_date;

    @Column(name = "completion_date")
    private Date completion_date;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "state", nullable = false)
    private Boolean state;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "student_id",referencedColumnName = "student_id")
    private Student student;

    @OneToMany(mappedBy = "student_expenses", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpensesDetailsEntity> expenses_details;

    @ManyToOne()
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    public static StudentExpensesEntity from(StudentExpensesDto studentExpensesDto){
        StudentExpensesEntity studentExpensesEntity = new StudentExpensesEntity();
        studentExpensesEntity.setName(studentExpensesDto.getName());
        studentExpensesEntity.setCreated_date(studentExpensesDto.getCreated_date());
        studentExpensesEntity.setCompletion_date(studentExpensesDto.getCompletion_date());
        studentExpensesEntity.setStatus(studentExpensesDto.getStatus());
        return studentExpensesEntity;
    }
}
