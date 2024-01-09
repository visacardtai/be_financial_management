package com.trainingfinance.apisystem.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StudentExpensesHP {
    private Long id;
    private String name;
    private Date created_date;
    private Long student_id;
    private Long semester_id;
}
