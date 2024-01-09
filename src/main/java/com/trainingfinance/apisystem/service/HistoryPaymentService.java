package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.HistoryPaymentEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.HistoryPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class HistoryPaymentService {
    @Autowired
    HistoryPaymentRepository historyPaymentRepository;

    public HistoryPaymentEntity getByIdInvoice(Long invoiceId){
        return historyPaymentRepository.findByIdInvoice(invoiceId);
    }

    public void addItem(HistoryPaymentEntity historyPayment){
        historyPaymentRepository.save(historyPayment);
    }

    public HistoryPaymentEntity getItem(Long id) {
        return historyPaymentRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    @Transactional
    public void editItem(Long id, HistoryPaymentEntity historyPayment){
        HistoryPaymentEntity itemToEdit = getItem(id);
        itemToEdit.setPrice(historyPayment.getPrice());
        itemToEdit.setStatus(historyPayment.getStatus());
        itemToEdit.setVnp_TxnRef(historyPayment.getVnp_TxnRef());
        historyPaymentRepository.save(itemToEdit);
    }
}
