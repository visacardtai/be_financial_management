package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class InvoiceDetailsDto {
    private Long id;
    private CreditPriceDto creditPrice;
    private SubjectDto subject;

    public static InvoiceDetailsDto from(InvoiceDetailsEntity invoiceDetailsEntity){
        InvoiceDetailsDto invoiceDetailsDto = new InvoiceDetailsDto();
        invoiceDetailsDto.setId(invoiceDetailsEntity.getId());
        if(Objects.nonNull(invoiceDetailsEntity.getCreditPrice())){
            invoiceDetailsDto.setCreditPrice(CreditPriceDto.from(invoiceDetailsEntity.getCreditPrice()));
        }
        invoiceDetailsDto.setSubject(SubjectDto.from(invoiceDetailsEntity.getSubject()));
        return invoiceDetailsDto;
    }
}
