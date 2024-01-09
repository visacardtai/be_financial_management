package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.ChartTeachingDto;
import com.trainingfinance.apisystem.dto.TeachingDetailsDto;
import com.trainingfinance.apisystem.dto.TeachingPeriodDto;
import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import com.trainingfinance.apisystem.entity.TeachingPeriodEntity;
import com.trainingfinance.apisystem.service.TeachingDetailsService;
import com.trainingfinance.apisystem.service.TeachingPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/lecturer/chart-lecture")
public class CharLecturerController {

    @Autowired
    TeachingPeriodService teachingPeriodService;
    @GetMapping("/subject/")
    public ResponseEntity<List<ChartTeachingDto>> getChartSubjectLecture(@RequestParam("lecturerId") Long lecturerId) {
        TeachingPeriodEntity teachingPeriodEntities = teachingPeriodService.getNewByIdLecture(lecturerId);
        List<TeachingDetailsEntity> teachingDetailsEntities = teachingPeriodEntities.getTeaching_details();
        List<TeachingDetailsDto> teachingDetailsDtoList = teachingDetailsEntities.stream().map(TeachingDetailsDto::from).toList();
        List<ChartTeachingDto> chartTeachingDtoList = teachingDetailsDtoList.stream().map(ChartTeachingDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(chartTeachingDtoList, HttpStatus.OK);
    }
}
