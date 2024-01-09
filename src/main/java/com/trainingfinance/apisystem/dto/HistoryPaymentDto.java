package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.HistoryPaymentEntity;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class HistoryPaymentDto {
    private Long id;
    private String vnp_TxnRef;
    private Double price;
    private Boolean status;

    public static HistoryPaymentDto from(HistoryPaymentEntity historyPayment){
        HistoryPaymentDto historyPaymentDto = new HistoryPaymentDto();
        historyPaymentDto.setId(historyPayment.getId());
        historyPaymentDto.setPrice(historyPayment.getPrice());
        historyPaymentDto.setStatus(historyPayment.getStatus());
        historyPaymentDto.setVnp_TxnRef(historyPayment.getVnp_TxnRef());
        return historyPaymentDto;
    }
}


