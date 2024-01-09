package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.HistoryPaymentDto;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "history_payment")
public class HistoryPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_payment_id")
    private Long id;

    @Column(name = "vnp_TxnRef")
    private String vnp_TxnRef;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    public static HistoryPaymentEntity from(HistoryPaymentDto historyPaymentDto){
        HistoryPaymentEntity historyPayment = new HistoryPaymentEntity();
        historyPayment.setPrice(historyPaymentDto.getPrice());
        historyPayment.setStatus(historyPaymentDto.getStatus());
        historyPayment.setVnp_TxnRef(historyPaymentDto.getVnp_TxnRef());
        return historyPayment;
    }

}
