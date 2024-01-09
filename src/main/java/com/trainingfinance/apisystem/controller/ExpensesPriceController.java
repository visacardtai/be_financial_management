package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.CreditPriceDto;
import com.trainingfinance.apisystem.dto.ExpensesPriceDto;
import com.trainingfinance.apisystem.entity.CreditPriceEntity;
import com.trainingfinance.apisystem.entity.ExpensesPriceEntity;
import com.trainingfinance.apisystem.service.ExpensesPriceService;
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
@RequestMapping("/api/v1/expert/expenses-price")
@Validated
public class ExpensesPriceController {
    @Autowired
    ExpensesPriceService expensesPriceService;

    @GetMapping
    public ResponseEntity<List<ExpensesPriceDto>> getExpensesPrice(@RequestParam("status") boolean status){
        List<ExpensesPriceEntity> expensesPriceEntities = expensesPriceService.getByStatus(status);
        List<ExpensesPriceDto> cartsDto = expensesPriceEntities.stream().map(ExpensesPriceDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(cartsDto, HttpStatus.OK);
    }

    @GetMapping("/news")
    public ResponseEntity<List<ExpensesPriceDto>> getExpensesPriceNew(){
        List<ExpensesPriceEntity> expensesPriceEntities = expensesPriceService.getByNew();
        List<ExpensesPriceDto> cartsDto = expensesPriceEntities.stream().map(ExpensesPriceDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(cartsDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ExpensesPriceDto expensesPriceDto){
        ExpensesPriceEntity expensesPrice = new ExpensesPriceEntity();
        expensesPrice.setName(expensesPriceDto.getName());
        expensesPrice.setPrice(expensesPriceDto.getPrice());
        expensesPrice.setDescription(expensesPriceDto.getDescription());
        expensesPrice.setStatus(false);
        expensesPriceService.add(expensesPrice);
        Map<String, Object> data = new HashMap<>();
        data.put("value", "successfully");
        data.put("status", 0);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (expensesPriceService.getItem((long) index).getStatus() != true) {
                    expensesPriceService.editItem((long) index);;
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
                if (expensesPriceService.getItem((long) index).getStatus() != true){
                    expensesPriceService.deleteItem((long) index);
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
