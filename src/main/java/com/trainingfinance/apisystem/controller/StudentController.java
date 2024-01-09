package com.trainingfinance.apisystem.controller;

import com.trainingfinance.apisystem.dto.InvoiceDto;
import com.trainingfinance.apisystem.dto.StudentDto;
import com.trainingfinance.apisystem.entity.InvoiceEntity;
import com.trainingfinance.apisystem.entity.Semester;
import com.trainingfinance.apisystem.entity.Student;
import com.trainingfinance.apisystem.repository.StudentRepository;
import com.trainingfinance.apisystem.service.SemesterService;
import com.trainingfinance.apisystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/expert/student")
public class StudentController {
    @Autowired
    StudentService studentService;
    @Autowired
    SemesterService semesterService;

    @GetMapping("/semester")
    public Student getSStudent(){
        return studentService.getItem(1L);
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllInvoiceByStateAndStatus() {
        List<Student> student = studentService.getAllStudent();
        List<StudentDto> studentDtoList = student.stream().map(StudentDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(studentDtoList, HttpStatus.OK);
    }
}
