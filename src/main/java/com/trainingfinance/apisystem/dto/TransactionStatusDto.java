package com.trainingfinance.apisystem.dto;

import lombok.Data;

@Data
public class TransactionStatusDto {
    private String status;
    private String messages;
    private String bankCode;
    private String data;
}
