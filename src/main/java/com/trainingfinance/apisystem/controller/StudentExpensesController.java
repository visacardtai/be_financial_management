package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.AddStudentExpensesDto;
import com.trainingfinance.apisystem.dto.InvoiceDto;
import com.trainingfinance.apisystem.dto.StudentExpensesDto;
import com.trainingfinance.apisystem.dto.TeachingPeriodDto;
import com.trainingfinance.apisystem.entity.ExpensesDetailsEntity;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.StudentExpensesEntity;
import com.trainingfinance.apisystem.entity.TeachingPeriodEntity;
import com.trainingfinance.apisystem.service.*;
import com.trainingfinance.apisystem.utils.ExcelStudentExpensesHelper;
import com.trainingfinance.apisystem.utils.ExcelTeachingPeriodHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000" ,allowCredentials = "true")
@RestController
@RequestMapping("/api/v1")
@Validated
public class StudentExpensesController {
    @Autowired
    StudentExpensesService studentExpensesService;
    @Autowired
    ExpensesDetailsService expensesDetailsService;
    @Autowired
    StudentService studentService;
    @Autowired
    SemesterService semesterService;
    @Autowired
    ExpensesPriceService expensesPriceService;

    @GetMapping("/student/student-expenses/")
    public ResponseEntity<List<StudentExpensesDto>> getAllExpensesByStudentId(@RequestParam("studentId") Long studentId) {
        List<StudentExpensesEntity> studentExpensesEntities = studentExpensesService.getExpensesByIdStudent(studentId);
        List<StudentExpensesDto> invoiceDtoList = studentExpensesEntities
                .stream()
                .map(StudentExpensesDto::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(invoiceDtoList, HttpStatus.OK);
    }

    @GetMapping("/expert/student-expenses/alls")
    public ResponseEntity<List<StudentExpensesDto>> getAllByStateAndStatus(@RequestParam("status") boolean status,
                                                            @RequestParam("state") boolean state) {
        List<StudentExpensesEntity> teachingPeriodEntities = studentExpensesService.getStudentExpByStateAndStatus(status,state);
        List<StudentExpensesDto> teachingPeriodDtoList = teachingPeriodEntities.stream().map(StudentExpensesDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(teachingPeriodDtoList, HttpStatus.OK);
    }
    @PostMapping("/expert/student-expenses")
    public ResponseEntity<?> addStudentExpenses(@Valid @RequestBody AddStudentExpensesDto addStudentExpensesDto) {
        var message = "";
        try {
            // create Student expenses
            StudentExpensesEntity studentExpensesEntity = new StudentExpensesEntity();
            studentExpensesEntity.setName(addStudentExpensesDto.getName());
            studentExpensesEntity.setState(false);
            studentExpensesEntity.setStatus(false);
            Date currentDate = new Date();
            studentExpensesEntity.setCreated_date(currentDate);
            studentExpensesEntity.setStudent(studentService.getItem(addStudentExpensesDto.getStudent_id()));
            studentExpensesEntity.setSemester(semesterService.getItem(addStudentExpensesDto.getSemester_id()));

            // create expenses details list
            List<ExpensesDetailsEntity> entityList = new ArrayList<>();
            for (Long item : addStudentExpensesDto.getDetails()) {
                ExpensesDetailsEntity entity = new ExpensesDetailsEntity();
                entity.setExpenses_price(expensesPriceService.getItem(item));
                entityList.add(entity);
            }
            studentExpensesService.addItemToStudentExp(studentExpensesEntity,entityList);
            message = "Add Student-Expenses successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not add Student-Expenses";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @DeleteMapping("/expert/student-expenses")
    public ResponseEntity<?> deleteStudentExpenses(@RequestBody Long[] response) {
        var message = "";
        try {
            if (Arrays.stream(response).count() != 0) {
                for (long index:response) {
                    if (studentExpensesService.getItem(index).getStatus() != true &&
                            studentExpensesService.getItem(index).getState() != true){
                        studentExpensesService.removeStudentExpenses(index);
                    }
                }
            } else {
                message = "Could not Delete Student Expenses";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
            message = "Delete Student Expenses successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not Delete Student Expenses";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/expert/student-expenses/pay/censor")
    public ResponseEntity<?> payCensor(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (studentExpensesService.getItem((long) index).getState() == true && studentExpensesService.getItem((long) index).getStatus() != true) {
                    studentExpensesService.payCensor((long) index);;
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

    @PutMapping("/expert/student-expenses/censor")
    public ResponseEntity<?> censor(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (studentExpensesService.getItem((long) index).getStatus() != true) {
                    studentExpensesService.editItem((long) index);;
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
    @PutMapping("/expert/student-expenses")
    public ResponseEntity<?> editStudentExp(@Valid @RequestBody AddStudentExpensesDto addStudentExpensesDto) {
        var message = "";
        try {
            boolean check = studentExpensesService.editStudentExpenses(addStudentExpensesDto);
            System.out.println(check);
            if (check) {
                message = "Add Student-Expenses successfully";
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } else {
                message = "Could not add Student-Expenses";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        } catch (Exception e) {
            message = "Could not add Student-Expenses";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/expert/student-expenses/upload")
    public ResponseEntity<Object> uploadStudentExpensesFile(@RequestParam("file") MultipartFile files) {
        var message = "";
        if (ExcelStudentExpensesHelper.hasExcelFormat(files)) {
            try {
                Map<Long,Long> data = studentExpensesService.saveListStudentExpenses(files);
                expensesDetailsService.saveListExpensesDetails(files,data);
                message = "Uploaded the file commune successfully: " + files.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception e) {
                message = "Could not upload the commune file: " + files.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
