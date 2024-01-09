package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import com.trainingfinance.apisystem.repository.InvoiceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InvoiceDetailsController {
    @Autowired
    InvoiceDetailsRepository repo;

    @GetMapping("/invoice-details")
    List<InvoiceDetailsEntity> getInvoiceDetails() {
        return repo.findAll();
    }
}
