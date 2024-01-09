package com.trainingfinance.apisystem.dto;

import com.trainingfinance.apisystem.entity.StudentExpensesEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class StudentExpensesDto {
    private Long id;
    private String name;
    private String student_name;
    private Long student_id;
    private Date created_date;
    private Date completion_date;
    private Boolean status;
    private List<ExpensesDetailsDto> expensesDetails = new ArrayList<>();
    private SemesterDto semester;
    private Double total;

    public static StudentExpensesDto from(StudentExpensesEntity studentExpensesEntity){
        StudentExpensesDto studentExpensesDto = new StudentExpensesDto();
        studentExpensesDto.setId(studentExpensesEntity.getId());
        studentExpensesDto.setName(studentExpensesEntity.getName());
        studentExpensesDto.setCreated_date(studentExpensesEntity.getCreated_date());
        studentExpensesDto.setCompletion_date(studentExpensesEntity.getCompletion_date());
        studentExpensesDto.setStatus(studentExpensesEntity.getStatus());
        studentExpensesDto.setStudent_id(studentExpensesEntity.getStudent().getStudentId());
        studentExpensesDto.setStudent_name(studentExpensesEntity.getStudent().getProfile().getFullname());
        if(Objects.nonNull(studentExpensesEntity.getSemester())){
            studentExpensesDto.setSemester(SemesterDto.from(studentExpensesEntity.getSemester()));
        }
        studentExpensesDto.setExpensesDetails(studentExpensesEntity.getExpenses_details()
                .stream()
                .map(ExpensesDetailsDto::from)
                .collect(Collectors.toList()));
        studentExpensesDto.setTotal(total(studentExpensesDto.getExpensesDetails()));
        return studentExpensesDto;
    }
    public static Double total(List<ExpensesDetailsDto> expensesDetails){
        double sum = 0.0;
        for (ExpensesDetailsDto a:expensesDetails) {
            sum +=a.getExpensesPrice().getPrice();
        }
        return sum;
    }
}
