package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.ExpensesPriceEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.CreditPriceNotFoundException;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.TargetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TargetsService {
    @Autowired
    TargetsRepository targetsRepository;

    public List<TargetsEntity> getAllTargets(){
        return StreamSupport
                .stream(targetsRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    public TargetsEntity add(TargetsEntity targets) {
        return  targetsRepository.save(targets);
    }

    public List<TargetsEntity> getByStatus(boolean status) {
        return StreamSupport
                .stream(targetsRepository.findByStatus(status).spliterator(),false)
                .collect(Collectors.toList());
    }

    public TargetsEntity getByNameNew(String name) {
        return targetsRepository.findByName(name);
    }

    public TargetsEntity getItem(Long id) {
        return targetsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public TargetsEntity deleteItem(Long id){
        TargetsEntity item = getItem(id);
        targetsRepository.delete(item);
        return item;
    }

    @Transactional
    public void editItem(Long id){
        TargetsEntity itemToEdit = getItem(id);
        itemToEdit.setStatus(true);
        targetsRepository.save(itemToEdit);
    }
}
