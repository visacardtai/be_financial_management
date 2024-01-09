package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.dto.InvoiceDetailsHP;
import com.trainingfinance.apisystem.dto.InvoiceHP;
import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.InvoiceDetailsRepository;
import com.trainingfinance.apisystem.utils.ExcelInvoiceDetailsHelper;
import com.trainingfinance.apisystem.utils.ExcelInvoiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InvoiceDetailsService {
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;
    @Autowired
    CreditPriceService creditPriceService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    InvoiceService invoiceService;

    public InvoiceDetailsEntity getItem(Long id) {
        return invoiceDetailsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void saveListInvoiceDetails(MultipartFile file, Map<Long,Long> data) {
        try {
            List<InvoiceDetailsHP> invoiceDetailsHPS = ExcelInvoiceDetailsHelper.excelToCommunes(file.getInputStream());
            List<InvoiceDetailsEntity> invoiceEntities = new ArrayList<>();
            for (InvoiceDetailsHP invoiceDetailsHP : invoiceDetailsHPS) {
                InvoiceDetailsEntity invoiceDetails = new InvoiceDetailsEntity();
                invoiceDetails.setSubject(subjectService.getItem(invoiceDetailsHP.getSubject_id()));
                invoiceDetails.setCreditPrice(creditPriceService.getItem(invoiceDetailsHP.getCredit_price_id()));
                invoiceDetails.setInvoice(invoiceService.getInvoice(data.get(invoiceDetailsHP.getInvoice_id())));
                invoiceEntities.add(invoiceDetails);
            }
            StopWatch watch = new StopWatch();
            watch.start();
            invoiceDetailsRepository.saveAll(invoiceEntities);
            watch.stop();
            log.info("Save communes {} time Elapsed: {}", invoiceEntities.size(), watch.getTotalTimeSeconds());
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
