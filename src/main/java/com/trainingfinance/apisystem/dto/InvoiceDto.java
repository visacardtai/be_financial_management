package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.CreditPriceEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InvoiceDto {

    private Long id;
    private String name;
    private String student_name;
    private Long student_id;
    private Date expiration_date;
    private Date date_of_payment;
    private Boolean status;
    private List<InvoiceDetailsDto> invoiceDetails = new ArrayList<>();
    private SemesterDto semester = new SemesterDto();
    private Double total;

    public static InvoiceDto from(InvoiceEntity invoiceEntity){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoiceEntity.getId());
        invoiceDto.setName(invoiceEntity.getName());
        invoiceDto.setExpiration_date(invoiceEntity.getExpiration_date());
        invoiceDto.setDate_of_payment(invoiceEntity.getDate_of_payment());
        invoiceDto.setStatus(invoiceEntity.getStatus());
        invoiceDto.setStudent_name(invoiceEntity.getStudent().getProfile().getFullname());
        invoiceDto.setStudent_id(invoiceEntity.getStudent().getStudentId());
        invoiceDto.setInvoiceDetails(invoiceEntity.getInvoiceDetails()
                .stream()
                .map(InvoiceDetailsDto::from)
                .collect(Collectors.toList()));
        invoiceDto.setSemester(SemesterDto.from(invoiceEntity.getSemester()));
        invoiceDto.setTotal(total(invoiceDto.getInvoiceDetails()));
        return invoiceDto;
    }
    public static Double total(List<InvoiceDetailsDto> invoiceDetails){
        double sum = 0.0;
        for (InvoiceDetailsDto a:invoiceDetails) {
            sum +=a.getCreditPrice().getPrice() * a.getSubject().getCreditNum();
        }
        return sum;
    }
}
