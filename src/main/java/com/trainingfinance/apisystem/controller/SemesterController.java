package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.SemesterDto;
import com.trainingfinance.apisystem.dto.StudentDto;
import com.trainingfinance.apisystem.entity.Semester;
import com.trainingfinance.apisystem.entity.Student;
import com.trainingfinance.apisystem.service.SemesterService;
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
@RequestMapping("/api/v1/expert/semester")
public class SemesterController {
    @Autowired
    SemesterService semesterService;

    @GetMapping
    public ResponseEntity<List<SemesterDto>> getAllSemester() {
        List<Semester> semester = semesterService.getAllSemester();
        List<SemesterDto> semesterDtoList = semester.stream().map(SemesterDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(semesterDtoList, HttpStatus.OK);
    }
}
