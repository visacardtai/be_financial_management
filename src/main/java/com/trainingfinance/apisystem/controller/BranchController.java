package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.BranchDto;
import com.trainingfinance.apisystem.entity.BranchEntity;
import com.trainingfinance.apisystem.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/expert/branch")
public class BranchController {

    @Autowired
    BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchDto>> getBranchs() {
        List<BranchEntity> branchEntityList = branchService.getItems();
        List<BranchDto> branchDtoList = branchEntityList.stream().map(BranchDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(branchDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<BranchDto> getBranch(@PathVariable final Long id) {
        BranchEntity branchEntityList = branchService.getItem(id);
        BranchDto branchDtoList = BranchDto.from(branchEntityList);
        return new ResponseEntity<>(branchDtoList, HttpStatus.OK);
    }

}
