package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.TargetsDto;
import com.trainingfinance.apisystem.dto.TeachingDetailsSubDto;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsSubEntity;
import com.trainingfinance.apisystem.service.TargetsService;
import com.trainingfinance.apisystem.service.TeachingDetailsService;
import com.trainingfinance.apisystem.service.TeachingDetailsSubService;
import com.trainingfinance.apisystem.service.TeachingPeriodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/teaching-details-sub")
public class TeachingDetailsSubController {
    @Autowired
    TeachingDetailsSubService teachingDetailsSubService;
    @Autowired
    TeachingDetailsService teachingDetailsService;
    @Autowired
    TeachingPeriodService teachingPeriodService;

    @GetMapping
    public ResponseEntity<List<TeachingDetailsSubDto>> getAllTeachingDetailsSub() {
        List<TeachingDetailsSubEntity> teachingDetailsSubEntities = teachingDetailsSubService.getAllTeachingDetailsSub();
        List<TeachingDetailsSubDto> teachingDetailsSubDtoList = teachingDetailsSubEntities
                .stream()
                .map(TeachingDetailsSubDto::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(teachingDetailsSubDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TeachingDetailsSubDto teachingDetailsSubDto){
        TeachingDetailsSubEntity teachingDetails = new TeachingDetailsSubEntity();
        teachingDetails.setName("Điểm danh");
        teachingDetails.setQuantity(teachingDetailsSubDto.getQuantity());
        teachingDetails.setPractical(teachingDetailsSubDto.getPractical());
        teachingDetails.setAttendance(teachingDetailsSubDto.getAttendance());
        TeachingDetailsEntity teachingDetailsEntity = teachingDetailsService.getItem(teachingDetailsSubDto.getDetails_id());
        teachingDetails.setTeaching_details(teachingDetailsEntity);
        teachingDetailsSubService.addSub(teachingDetails);
        teachingPeriodService.changeIsUsed(teachingDetailsEntity.getTeaching_period().getId());
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
}
