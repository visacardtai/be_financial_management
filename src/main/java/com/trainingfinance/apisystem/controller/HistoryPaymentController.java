package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.HistoryPaymentDto;
import com.trainingfinance.apisystem.dto.TeachingPeriodDto;
import com.trainingfinance.apisystem.entity.HistoryPaymentEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.TeachingPeriodEntity;
import com.trainingfinance.apisystem.service.HistoryPaymentService;
import com.trainingfinance.apisystem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/public/history-payment")
public class HistoryPaymentController {
    @Autowired
    HistoryPaymentService historyPaymentService;
    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<HistoryPaymentDto> getByIdInvoice(@RequestParam("invoiceId") Long invoiceId) {
        HistoryPaymentEntity hisHistoryPayment = historyPaymentService.getByIdInvoice(invoiceId);
        HistoryPaymentDto historyPaymentDto = HistoryPaymentDto.from(hisHistoryPayment);
        return new ResponseEntity<>(historyPaymentDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HistoryPaymentDto> addItem(@RequestParam("invoiceId") Long invoiceId,@RequestBody final HistoryPaymentDto historyPaymentDto){
        InvoiceEntity invoice = invoiceService.getInvoice(invoiceId);
        if (invoice == null) {
            // Xử lý khi không tìm thấy Invoice với invoiceId tương ứng
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HistoryPaymentEntity historyPayment = HistoryPaymentEntity.from(historyPaymentDto);
        historyPayment.setInvoice(invoice);
        historyPaymentService.addItem(historyPayment);
        return new ResponseEntity<>(HistoryPaymentDto.from(historyPayment), HttpStatus.OK);
    }
}
