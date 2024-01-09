package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsSubEntity;
import com.trainingfinance.apisystem.repository.TeachingDetailsSubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachingDetailsSubService {
    @Autowired
    TeachingDetailsSubRepository teachingDetailsSubRepository;

    public List<TeachingDetailsSubEntity> getAllTeachingDetailsSub(){
        return teachingDetailsSubRepository.findAll();
    }

    public void addSub(TeachingDetailsSubEntity teachingDetailsSub){
        teachingDetailsSubRepository.save(teachingDetailsSub);
    }
}
