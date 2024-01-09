package com.trainingfinance.apisystem.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AddStudentExpensesDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Long student_id;
    @NotNull
    private Long semester_id;
    private Long[] details;
}
