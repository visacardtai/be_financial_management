package com.trainingfinance.apisystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddInvoiceDetailsDto {
    @NotNull
    private Long credit_price_id;
    @NotNull
    private Long subject_id;
}
