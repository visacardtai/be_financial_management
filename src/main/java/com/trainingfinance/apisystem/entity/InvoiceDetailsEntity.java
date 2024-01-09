package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trainingfinance.apisystem.dto.InvoiceDetailsDto;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "invoice_details")
public class InvoiceDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_details_id")
    private Long id;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumn(name = "credit_price_id", referencedColumnName = "credit_price_id")
    private CreditPriceEntity creditPrice;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
    private InvoiceEntity invoice;

    @ManyToOne()
    @JoinColumn(name = "subject_id",referencedColumnName = "subject_id")
    private Subject subject;

    public static InvoiceDetailsEntity from(InvoiceDetailsDto invoiceDetailsDto){
        InvoiceDetailsEntity invoiceDetails = new InvoiceDetailsEntity();
        return invoiceDetails;
    }
}
