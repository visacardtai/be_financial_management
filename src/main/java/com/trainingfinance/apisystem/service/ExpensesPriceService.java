package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.CreditPriceEntity;
import com.trainingfinance.apisystem.entity.ExpensesPriceEntity;
import com.trainingfinance.apisystem.exception.CreditPriceNotFoundException;
import com.trainingfinance.apisystem.repository.ExpensesPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExpensesPriceService {
    @Autowired
    ExpensesPriceRepository expensesPriceRepository;

    public ExpensesPriceEntity add(ExpensesPriceEntity expensesPrice) {
        return  expensesPriceRepository.save(expensesPrice);
    }

    public List<ExpensesPriceEntity> getAllExpensesPrice() {
        return StreamSupport
                .stream(expensesPriceRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public List<ExpensesPriceEntity> getByStatus(boolean status) {
        return StreamSupport
                .stream(expensesPriceRepository.findByStatus(status).spliterator(),false)
                .collect(Collectors.toList());
    }

    public List<ExpensesPriceEntity> getByNew() {
        return expensesPriceRepository.findByNew();

    }

    public ExpensesPriceEntity getItem(Long id) {
        return expensesPriceRepository.findById(id).orElseThrow(() -> new CreditPriceNotFoundException(id));
    }

    public ExpensesPriceEntity deleteItem(Long id){
        ExpensesPriceEntity item = getItem(id);
        expensesPriceRepository.delete(item);
        return item;
    }

    @Transactional
    public void editItem(Long id){
        ExpensesPriceEntity itemToEdit = getItem(id);
        itemToEdit.setStatus(true);
        expensesPriceRepository.save(itemToEdit);
    }
}
