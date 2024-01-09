package com.trainingfinance.apisystem.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceHP {
    private Long id;
    private String name;
    private Date expiration_date;
    private Long student_id;
    private Long semester_id;
}
