package com.trainingfinance.apisystem.dto;
import com.trainingfinance.apisystem.entity.Student;
import lombok.Data;

@Data
public class StudentDto {
    private Long studentId;
    private String academicYear;
    private String name;

    public static StudentDto from(Student student){
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(student.getStudentId());
        studentDto.setAcademicYear(student.getAcademicYear());
        studentDto.setName(student.getProfile().getFullname());
        return studentDto;
    }
}
