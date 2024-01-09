package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.CreditPriceDto;
import com.trainingfinance.apisystem.entity.BranchEntity;
import com.trainingfinance.apisystem.entity.CreditPriceEntity;

import com.trainingfinance.apisystem.service.BranchService;
import com.trainingfinance.apisystem.service.CreditPriceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/expert/credit-price")
@Validated
public class CreditPriceController {
    @Autowired
    CreditPriceService creditPriceService;
    @Autowired
    BranchService branchService;

    @GetMapping
    public ResponseEntity<List<CreditPriceDto>> getCreditPrice(@RequestParam("status") boolean status){
        List<CreditPriceEntity> carts = creditPriceService.getByStatus(status);
        List<CreditPriceDto> cartsDto = carts.stream().map(CreditPriceDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(cartsDto, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreditPriceDto creditPriceDto){
        BranchEntity branch = new BranchEntity();
        branch = branchService.getItem(creditPriceDto.getBranch_id());
        CreditPriceEntity creditPrice = new CreditPriceEntity();
        creditPrice.setName(creditPriceDto.getName());
        creditPrice.setPrice(creditPriceDto.getPrice());
        creditPrice.setStatus(false);
        creditPrice.setType(creditPriceDto.getType());
        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        creditPrice.setDate_create(date);
        creditPrice.setBranch(branch);
        creditPriceService.add(creditPrice);
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (creditPriceService.getItem((long) index).getStatus() != true) {
                    creditPriceService.editItem((long) index);
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
                if (creditPriceService.getItem((long) index).getStatus() != true) {
                    creditPriceService.deleteItem((long) index);
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
