package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.InvoiceDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "expiration_date")
    private Date expiration_date;

    @Column(name = "date_of_payment")
    private Date date_of_payment;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "state", nullable = false)
    private Boolean state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceDetailsEntity> invoiceDetails;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "student_id",referencedColumnName = "student_id")
    private Student student;

    @ManyToOne()
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    @JsonIgnore
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
    private HistoryPaymentEntity historyPayment;

    public void addHistoryPayment(HistoryPaymentEntity historyPayment){
        this.historyPayment = historyPayment;
    }

    public static InvoiceEntity from(InvoiceDto invoiceDto){
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setName(invoiceDto.getName());
        invoiceEntity.setStatus(invoiceDto.getStatus());
        invoiceEntity.setDate_of_payment(invoiceDto.getDate_of_payment());
        invoiceEntity.setExpiration_date(invoiceDto.getExpiration_date());
        return invoiceEntity;
    }

}
