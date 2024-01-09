package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import com.trainingfinance.apisystem.entity.Student;
import com.trainingfinance.apisystem.entity.Subject;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public Subject getItem(Long id) {
        return subjectRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<Subject> getAllSubject(){
        return StreamSupport
                .stream(subjectRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
}
