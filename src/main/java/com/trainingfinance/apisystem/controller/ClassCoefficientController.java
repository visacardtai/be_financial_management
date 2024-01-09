package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.ClassCoefficientDto;
import com.trainingfinance.apisystem.entity.ClassCoefficientEntity;
import com.trainingfinance.apisystem.service.ClassCoefficientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/expert/class-coefficient")
public class ClassCoefficientController {
    @Autowired
    ClassCoefficientService classCoefficientService;

    @GetMapping
    public ResponseEntity<List<ClassCoefficientDto>> getAllClassCoefficient(){
        List<ClassCoefficientEntity> classCoefficientEntities = classCoefficientService.getAllClassCoefficient();
        List<ClassCoefficientDto> coefficientDtoList = classCoefficientEntities.stream().map(ClassCoefficientDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(coefficientDtoList, HttpStatus.OK);
    }
}
