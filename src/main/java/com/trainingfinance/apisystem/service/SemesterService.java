package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.Semester;
import com.trainingfinance.apisystem.entity.Student;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SemesterService {
    @Autowired
    SemesterRepository semesterRepository;

    public Semester getItem(Long id) {
        return semesterRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<Semester> getAllSemester(){
        return StreamSupport
                .stream(semesterRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
}
