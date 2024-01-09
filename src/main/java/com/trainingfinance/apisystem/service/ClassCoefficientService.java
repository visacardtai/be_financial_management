package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.ClassCoefficientEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.ClassCoefficientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ClassCoefficientService {
    @Autowired
    ClassCoefficientRepository classCoefficientRepository;

    public List<ClassCoefficientEntity> getAllClassCoefficient() {
        return classCoefficientRepository.findAll();
    }

    public ClassCoefficientEntity getItem(Long id) {
        return classCoefficientRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
