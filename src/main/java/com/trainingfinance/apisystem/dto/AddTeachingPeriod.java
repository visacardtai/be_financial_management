package com.trainingfinance.apisystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class AddTeachingPeriod {
    private Long id;
//    @NotNull
    private Long targets_id;
    @NotNull
    private Long lecturer_id;
//    @NotNull
    private Long lecture_price_id;
    @NotNull
    private Long semester_id;
    private List<AddTeachingDetails> details;
    private List<Long> listDelete;
}
