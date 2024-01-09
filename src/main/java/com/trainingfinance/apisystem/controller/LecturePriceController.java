package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.LecturePriceDto;
import com.trainingfinance.apisystem.dto.TargetsDto;
import com.trainingfinance.apisystem.entity.LecturePriceEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.service.LecturePriceService;
import com.trainingfinance.apisystem.service.TargetsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
@Validated
public class LecturePriceController {
    @Autowired
    LecturePriceService lecturePriceService;

    @GetMapping("/lecturer/lecture-price")
    public ResponseEntity<List<LecturePriceDto>> getAllLecturePriceByStatus(@RequestParam("status") boolean status){
        List<LecturePriceEntity> lecturePriceEntities = lecturePriceService.getByStatus(status);
        List<LecturePriceDto> lecturePriceDtoList = lecturePriceEntities
                .stream()
                .map(LecturePriceDto::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(lecturePriceDtoList, HttpStatus.OK);
    }

    @PostMapping("/expert/lecture-price")
    public ResponseEntity<?> create(@RequestBody @Valid LecturePriceDto lecturePriceDto){
        LecturePriceEntity lecturePrice = new LecturePriceEntity();
        lecturePrice.setName(lecturePriceDto.getName());
        lecturePrice.setBasic_price(lecturePriceDto.getBasic_price());
        lecturePrice.setDescription(lecturePriceDto.getDescription());
        lecturePrice.setCoefficient(lecturePriceDto.getCoefficient());
        lecturePrice.setStatus(false);
        lecturePriceService.add(lecturePrice);
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PutMapping("/expert/lecture-price")
    public ResponseEntity<?> update(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (lecturePriceService.getItem((long) index).getStatus() != true) {
                    lecturePriceService.editItem((long) index);;
                }
            }
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("value", "Data null");
            data.put("status", -1);
            return new ResponseEntity<>(data,HttpStatus.OK);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @DeleteMapping("/expert/lecture-price")
    public ResponseEntity<?> deleted(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (lecturePriceService.getItem((long) index).getStatus() != true){
                    lecturePriceService.deleteItem((long) index);
                }
            }
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("value", "Data null");
            data.put("status", -1);
            return new ResponseEntity<>(data,HttpStatus.OK);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
}
