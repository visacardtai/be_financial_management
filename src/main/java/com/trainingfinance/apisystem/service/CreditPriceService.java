package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.BranchEntity;
import com.trainingfinance.apisystem.entity.CreditPriceEntity;
import com.trainingfinance.apisystem.exception.CreditPriceNotFoundException;
import com.trainingfinance.apisystem.repository.CreditPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CreditPriceService {

    @Autowired
    CreditPriceRepository creditPriceRepository;

    public CreditPriceEntity add(CreditPriceEntity creditPrice) {
        return  creditPriceRepository.save(creditPrice);
    }

    public List<CreditPriceEntity> getAllCreditPrice() {
        return StreamSupport
                .stream(creditPriceRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public List<CreditPriceEntity> getByStatus(boolean status) {
        return StreamSupport
                .stream(creditPriceRepository.findByStatus(status).spliterator(),false)
                .collect(Collectors.toList());
    }

    public CreditPriceEntity getItem(Long id) {
        return creditPriceRepository.findById(id).orElseThrow(() -> new CreditPriceNotFoundException(id));
    }

    public CreditPriceEntity getByName(String name) {
        return creditPriceRepository.findByName(name);
    }

    public CreditPriceEntity deleteItem(Long id){
        CreditPriceEntity item = getItem(id);
        creditPriceRepository.delete(item);
        return item;
    }

    @Transactional
    public void editItem(Long id){
        CreditPriceEntity itemToEdit = getItem(id);
        itemToEdit.setStatus(true);
        creditPriceRepository.save(itemToEdit);
    }
}
