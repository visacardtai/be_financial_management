package com.trainingfinance.apisystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTeachingDetails {
    @NotNull
    private Long class_coefficient_id;
    @NotNull
    private Long subject_id;
}
