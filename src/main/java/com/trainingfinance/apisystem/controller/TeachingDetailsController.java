package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.TargetsDto;
import com.trainingfinance.apisystem.dto.TeachingDetailsDto;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import com.trainingfinance.apisystem.service.TargetsService;
import com.trainingfinance.apisystem.service.TeachingDetailsService;
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
@RequestMapping("/api/v1/teaching-details")
public class TeachingDetailsController {

    @Autowired
    TeachingDetailsService teachingDetailsService;

    @GetMapping
    public ResponseEntity<List<TeachingDetailsDto>> getAllTeachingDetails() {
        List<TeachingDetailsEntity> teachingDetailsEntities = teachingDetailsService.getAllTeachingDetails();
        List<TeachingDetailsDto> targetsDtoList = teachingDetailsEntities
                .stream()
                .map(TeachingDetailsDto::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(targetsDtoList, HttpStatus.OK);
    }
}
