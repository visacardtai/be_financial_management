package com.trainingfinance.apisystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trainingfinance.apisystem.dto.CreditPriceDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "credit_price")
public class CreditPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_price_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "date_create")
    private Date date_create;

    @Column(name = "type")
    private Boolean type;

    @Column(name = "status")
    private Boolean status;

    @JsonBackReference
    @OneToMany(mappedBy = "creditPrice")
    private List<InvoiceDetailsEntity> invoiceDetails;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "branch_id",referencedColumnName = "branch_id")
    private BranchEntity branch;

    public static CreditPriceEntity from(CreditPriceDto creditPriceDto){
        CreditPriceEntity creditPrice = new CreditPriceEntity();
        creditPrice.setName(creditPriceDto.getName());
        creditPrice.setPrice(creditPriceDto.getPrice());
        creditPrice.setDate_create(creditPriceDto.getDate_create());
        creditPrice.setType(creditPriceDto.getType());
        creditPrice.setStatus(creditPriceDto.getStatus());
        return creditPrice;
    }
}
