package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.entity.Lecturer;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LecturerService {
    @Autowired
    LecturerRepository lecturerRepository;

    public Lecturer getItem(Long id) {
        return lecturerRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public List<Lecturer> getAllLecture(){
        return StreamSupport
                .stream(lecturerRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
}
