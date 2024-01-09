package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ChartInvoiceDto {
    private int total_invoice;
    private Double retired;
    private Double total;

    public static ChartInvoiceDto from(List<InvoiceDto> invoiceEntities){
        ChartInvoiceDto chartInvoiceDto = new ChartInvoiceDto();
        chartInvoiceDto.setTotal((Double) total(invoiceEntities).get("sum"));
        chartInvoiceDto.setTotal_invoice((Integer) total(invoiceEntities).get("num"));
        chartInvoiceDto.setRetired((Double) total(invoiceEntities).get("retired"));
        return chartInvoiceDto;
    }

    public static Map<String, Object> total(List<InvoiceDto> invoiceEntities){
        double sum = 0.0;
        double retired = 0.0;
        double notRetired = 0.0;
        int num = invoiceEntities.size();
        Map<String,Object> data = new HashMap<>();
        for (InvoiceDto a:invoiceEntities) {
            if (a.getStatus() == true) {
                for (InvoiceDetailsDto b : a.getInvoiceDetails()) {
                    retired += b.getCreditPrice().getPrice() * b.getSubject().getCreditNum();
                }
            } else {
                for (InvoiceDetailsDto b : a.getInvoiceDetails()){
                    notRetired +=b.getCreditPrice().getPrice() * b.getSubject().getCreditNum();
                }
            }
        }
        sum = retired + notRetired;
        data.put("num",num);
        data.put("retired",retired);
        data.put("sum",sum);
        return data;
    }
}
