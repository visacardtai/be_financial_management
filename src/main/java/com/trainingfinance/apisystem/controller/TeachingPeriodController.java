package com.trainingfinance.apisystem.controller;
import com.trainingfinance.apisystem.dto.*;
import com.trainingfinance.apisystem.entity.*;
import com.trainingfinance.apisystem.service.*;
import com.trainingfinance.apisystem.utils.ExcelInvoiceHelper;
import com.trainingfinance.apisystem.utils.ExcelTeachingPeriodHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
@Validated
public class TeachingPeriodController {

    @Autowired
    TeachingPeriodService teachingPeriodService;
    @Autowired
    TeachingDetailsService teachingDetailsService;
    @Autowired
    TargetsService targetsService;
    @Autowired
    LecturerService lecturerService;
    @Autowired
    LecturePriceService lecturePriceService;
    @Autowired
    SemesterService semesterService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    ClassCoefficientService classCoefficientService;

    @GetMapping("/lecturer/teaching-period/all")
    public ResponseEntity<List<TeachingPeriodDto>> getAllByIdLecture(@RequestParam("lecturerId") Long lecturerId) {
        List<TeachingPeriodEntity> teachingPeriodEntities = teachingPeriodService.getAllByIdLecture(lecturerId);
        List<TeachingPeriodDto> teachingPeriodDtoList = teachingPeriodEntities.stream().map(TeachingPeriodDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(teachingPeriodDtoList, HttpStatus.OK);
    }

    @GetMapping("/expert/teaching-period/alls")
    public ResponseEntity<List<TeachingPeriodDto>> getAllBy(@RequestParam("status") boolean status,
                                                            @RequestParam("is_used") boolean is_used) {
        List<TeachingPeriodEntity> teachingPeriodEntities = teachingPeriodService.getAllTeachingPeriod(status,is_used);
        List<TeachingPeriodDto> teachingPeriodDtoList = teachingPeriodEntities.stream().map(TeachingPeriodDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(teachingPeriodDtoList, HttpStatus.OK);
    }
    @GetMapping("/lecturer/teaching-period/")
    public ResponseEntity<TeachingPeriodDto> getNewByIdLecture(@RequestParam("lecturerId") Long lecturerId) {
        TeachingPeriodEntity teachingPeriodEntities = teachingPeriodService.getNewByIdLecture(lecturerId);
        TeachingPeriodDto teachingPeriodDtoList = TeachingPeriodDto.from(teachingPeriodEntities);
        return new ResponseEntity<>(teachingPeriodDtoList, HttpStatus.OK);
    }

    @PostMapping("/expert/teaching-period")
    public ResponseEntity<?> addTeachingPeriod(@Valid @RequestBody AddTeachingPeriod addTeachingPeriod) {
        var message = "";
        try {
            // create teaching period
            TeachingPeriodEntity teachingPeriod = new TeachingPeriodEntity();
            Lecturer lecturer = lecturerService.getItem(addTeachingPeriod.getLecturer_id());
            teachingPeriod.setIsUsed(false);
            teachingPeriod.setStatus(false);
            teachingPeriod.setTargets(targetsService.getByNameNew(lecturer.getPosition().getName()));
            teachingPeriod.setLecturer(lecturer);
            teachingPeriod.setSemester(semesterService.getItem(addTeachingPeriod.getSemester_id()));
            System.out.println(lecturer.getProfile().getQualification());
            teachingPeriod.setLecture_price(lecturePriceService.getByName(lecturer.getProfile().getQualification()));

            // create teaching details
            List<TeachingDetailsEntity> entityList = new ArrayList<>();
            for (AddTeachingDetails item : addTeachingPeriod.getDetails()) {
                TeachingDetailsEntity entity = new TeachingDetailsEntity();
                entity.setSubject(subjectService.getItem(item.getSubject_id()));
                entity.setClass_coefficient(classCoefficientService.getItem(item.getClass_coefficient_id()));
                entityList.add(entity);
            }
            teachingPeriodService.addItemToTeachingPeriod(teachingPeriod,entityList);
            message = "Add Invoice successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not add Invoice";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PutMapping("/expert/teaching-period")
    public ResponseEntity<?> editTeachingPeriodByAdmin(@Valid @RequestBody AddTeachingPeriod addTeachingPeriod) {
        var message = "";
        try {
            boolean check = teachingPeriodService.editTeachingDetails(addTeachingPeriod);
            System.out.println(check);
            if (check) {
                message = "Edit Teaching Period successfully";
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } else {
                message = "Could not edit Teaching Period";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        } catch (Exception e) {
            message = "Could not Edit Teaching Period";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

//    @DeleteMapping
//    public ResponseEntity<?> delete(@RequestBody Integer[] response){
//        if (Arrays.stream(response).count() != 0) {
//            for (int index:response) {
//                if (teachingPeriodService.getItem((long) index).getStatus() != true &&
//                        teachingPeriodService.getItem((long) index).getIsUsed() != true){
//                    teachingPeriodService.deleteItem((long) index);
//                }
//            }
//        } else {
//            Map<String, Object> data = new HashMap<>();
//            data.put("value", "Data null");
//            data.put("status", -1);
//            return new ResponseEntity<>(data,HttpStatus.OK);
//        }
//        Map<String, Object> data = new HashMap<>();
//        data.put("value", "successfully");
//        data.put("status", 0);
//        return new ResponseEntity<>(data,HttpStatus.OK);
//    }

    @PutMapping("/expert/teaching-period/censor")
    public ResponseEntity<?> censor(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (teachingPeriodService.getItem((long) index).getStatus() != true) {
                    teachingPeriodService.editItem((long) index);;
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
    @DeleteMapping("/expert/teaching-period")
    public ResponseEntity<?> deleteTeachingPeriod(@RequestBody Long[] response) {
        var message = "";
        try {
            if (Arrays.stream(response).count() != 0) {
                for (long index:response) {
                    if (teachingPeriodService.getItem(index).getStatus() != true &&
                            teachingPeriodService.getItem( index).getIsUsed() != true){
                        teachingPeriodService.removeTeachingPeriod(index);
                    }
                }
            } else {
                message = "Could not Delete Teaching Period";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
            message = "Delete Teaching Period successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not Delete Teaching Period";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/expert/teaching-period/upload")
    public ResponseEntity<Object> uploadTeachingPeriodFile(@RequestParam("file") MultipartFile files) {
        var message = "";
        if (ExcelTeachingPeriodHelper.hasExcelFormat(files)) {
            try {
                Map<Long,Long> data = teachingPeriodService.saveLisTeachingPeriod(files);
                teachingDetailsService.saveListTeachingDetails(files,data);
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

    @GetMapping("/expert/teaching-period/chart")
    public ResponseEntity<?> getAllBySemester(@RequestParam("semester_id") long semester_id) {
        List<TeachingPeriodEntity> teachingPeriodEntities = teachingPeriodService.getAllBySemesterId(semester_id);
        List<TeachingPeriodDto> teachingPeriodDtoList = teachingPeriodEntities.stream().map(TeachingPeriodDto::from).collect(Collectors.toList());
        double debt=0, paid=0;
        List<Object> list_debt = new ArrayList<>();
        List<Object> list_paid = new ArrayList<>();
        for (TeachingPeriodDto item:teachingPeriodDtoList) {
            if(item.getTargets().getQuantity() <= item.getCalculate()){
                Map<String, Object> data1= new HashMap<>();
                data1.put("lecturer_id",item.getLecturer_id());
                data1.put("lecturer_name",item.getName());
                data1.put("qualification",item.getQualification());
                data1.put("position",item.getPosition());
                data1.put("price",item.getLecturePrice().getBasic_price());
                data1.put("coefficient",item.getLecturePrice().getCoefficient());
                data1.put("targets",item.getTargets().getQuantity());
                data1.put("period",item.getCalculate());
                data1.put("total",item.getTien_troi());
                list_paid.add(data1);
            } else {
                Map<String, Object> data2= new HashMap<>();
                data2.put("lecturer_id",item.getLecturer_id());
                data2.put("lecturer_name",item.getName());
                data2.put("qualification",item.getQualification());
                data2.put("position",item.getPosition());
                data2.put("price",item.getLecturePrice().getBasic_price());
                data2.put("coefficient",item.getLecturePrice().getCoefficient());
                data2.put("targets",item.getTargets().getQuantity());
                data2.put("period",item.getCalculate());
//                double a = (item.getTargets().getQuantity() - item.getCalculate())*item.getLecturePrice().getBasic_price()*item.getLecturePrice().getCoefficient();
//                BigDecimal bigDecimal = new BigDecimal(a);
//                BigDecimal roundedValue = bigDecimal.setScale(2, RoundingMode.HALF_UP);
//                data2.put("debt",roundedValue);
                data2.put("total",item.getTien_troi());
                list_debt.add(data2);
                debt += item.getTien_troi();
            }
            paid += item.getTien_troi();
        }
        Map<String, Object> data= new HashMap<>();
        BigDecimal bigDecimal2 = new BigDecimal(debt);
        BigDecimal bigDecimal3 = new BigDecimal(paid);
        BigDecimal roundedValue2 = bigDecimal2.setScale(2, RoundingMode.HALF_UP);
        BigDecimal roundedValue3 = bigDecimal3.setScale(2, RoundingMode.HALF_UP);
        data.put("debt",roundedValue2);
        data.put("paid",roundedValue3);
        data.put("list_debt", list_debt);
        data.put("list_paid", list_paid);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
}
