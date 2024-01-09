package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.BranchEntity;
import com.trainingfinance.apisystem.entity.HistoryPaymentEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BranchService {
    @Autowired
    BranchRepository branchRepository;


    public BranchEntity getItem(Long branch_id){
        return branchRepository.findById(branch_id).orElseThrow(()->new NotFoundException(branch_id));
    }

    public List<BranchEntity> getItems() {
        return StreamSupport.stream(branchRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }
}
