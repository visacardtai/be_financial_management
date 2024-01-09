package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.StudentDto;
import com.trainingfinance.apisystem.dto.SubjectDto;
import com.trainingfinance.apisystem.entity.Student;
import com.trainingfinance.apisystem.entity.Subject;
import com.trainingfinance.apisystem.service.SubjectService;
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
@RequestMapping("/api/v1/expert/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;
    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAllSub() {
        List<Subject> subjects = subjectService.getAllSubject();
        List<SubjectDto> subjectDtoList = subjects.stream().map(SubjectDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(subjectDtoList, HttpStatus.OK);
    }
}
