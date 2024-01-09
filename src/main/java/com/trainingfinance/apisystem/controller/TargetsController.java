package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.ExpensesPriceDto;
import com.trainingfinance.apisystem.dto.TargetsDto;
import com.trainingfinance.apisystem.entity.ExpensesPriceEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
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
@RequestMapping("/api/v1/expert/targets")
@Validated
public class TargetsController {

    @Autowired
    TargetsService targetsService;

    @GetMapping
    public ResponseEntity<List<TargetsDto>> getAllTargetsByStatus(@RequestParam("status") boolean status){
        List<TargetsEntity> targetsEntities = targetsService.getByStatus(status);
        List<TargetsDto> targetsDtoList = targetsEntities
                .stream()
                .map(TargetsDto::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(targetsDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TargetsDto targetsDto){
        TargetsEntity targets = new TargetsEntity();
        targets.setName(targetsDto.getName());
        targets.setQuantity(targetsDto.getQuantity());
        targets.setDescription(targetsDto.getDescription());
        targets.setStatus(false);
        targetsService.add(targets);
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (targetsService.getItem((long) index).getStatus() != true) {
                    targetsService.editItem((long) index);;
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

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (targetsService.getItem((long) index).getStatus() != true){
                    targetsService.deleteItem((long) index);
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
