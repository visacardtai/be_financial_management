package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.CreditPriceEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.Date;

@Data
public class CreditPriceDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Double price;
    private Date date_create;
    @NotNull
    private Boolean type;
    private Boolean status;
    @NotNull
    private Long branch_id;
    private String branch_name;
//    private List<InvoiceDetailsDto> invoiceDetailsDtoList = new ArrayList<>();

    public static CreditPriceDto from(CreditPriceEntity creditPrice){
        CreditPriceDto creditPriceDto = new CreditPriceDto();
        creditPriceDto.setId(creditPrice.getId());
        creditPriceDto.setName(creditPrice.getName());
        creditPriceDto.setPrice(creditPrice.getPrice());
        creditPriceDto.setDate_create(creditPrice.getDate_create());
        creditPriceDto.setType(creditPrice.getType());
        creditPriceDto.setStatus(creditPrice.getStatus());
        creditPriceDto.setBranch_name(creditPrice.getBranch().getName());
//        creditPriceDto.setInvoiceDetailsDtoList(creditPrice.getInvoiceDetails()
//                .stream()
//                .map(InvoiceDetailsDto::from)
//                .collect(Collectors.toList()));
        return creditPriceDto;
    }
}
