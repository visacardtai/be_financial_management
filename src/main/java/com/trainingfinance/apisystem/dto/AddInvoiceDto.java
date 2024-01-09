package com.trainingfinance.apisystem.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AddInvoiceDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Date expiration_date;
    @NotNull
    private Long student_id;
    @NotNull
    private Long semester_id;
    private List<AddInvoiceDetailsDto> details;
    private List<Long> listDelete;
}
