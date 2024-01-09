package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.LecturerDto;
import com.trainingfinance.apisystem.entity.Lecturer;
import com.trainingfinance.apisystem.service.LecturerService;
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
@RequestMapping("/api/v1/expert/lecturer")
public class LecturerController {
    @Autowired
    LecturerService lecturerService;
    @GetMapping
    public ResponseEntity<List<LecturerDto>> getCarts(){
        List<Lecturer> lecture = lecturerService.getAllLecture();
        List<LecturerDto> lecturerDtoList = lecture.stream().map(LecturerDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(lecturerDtoList, HttpStatus.OK);
    }
}
