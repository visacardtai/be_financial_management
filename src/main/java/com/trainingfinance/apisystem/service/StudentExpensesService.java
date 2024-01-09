package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.dto.AddStudentExpensesDto;
import com.trainingfinance.apisystem.dto.StudentExpensesHP;
import com.trainingfinance.apisystem.dto.TeachingPeriodHP;
import com.trainingfinance.apisystem.entity.*;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.ExpensesDetailsRepository;
import com.trainingfinance.apisystem.repository.StudentExpensesRepository;
import com.trainingfinance.apisystem.utils.ExcelStudentExpensesHelper;
import com.trainingfinance.apisystem.utils.ExcelTeachingPeriodHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class StudentExpensesService {

    @Autowired
    StudentExpensesRepository studentExpensesRepository;
    @Autowired
    ExpensesDetailsRepository expensesDetailsRepository;
    @Autowired
    SemesterService semesterService;
    @Autowired
    StudentService studentService;
    @Autowired
    ExpensesPriceService expensesPriceService;

    public List<StudentExpensesEntity> getExpensesByIdStudent(Long studentId) {
        return studentExpensesRepository.findByIdStudent(studentId);
    }

    public StudentExpensesEntity getItem(Long id) {
        return studentExpensesRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<StudentExpensesEntity> getStudentExpByStateAndStatus(boolean status, boolean state) {
        return studentExpensesRepository.findByStateAndStatus(status,state);
    }

    @Transactional
    public void addItemToStudentExp(StudentExpensesEntity studentExpensesEntity, List<ExpensesDetailsEntity> expensesDetailsEntities){
        StudentExpensesEntity studentExpenses = studentExpensesRepository.save(studentExpensesEntity);
        for (ExpensesDetailsEntity item: expensesDetailsEntities) {
            item.setStudent_expenses(studentExpenses);
            expensesDetailsRepository.save(item);
        }
    }
    @Transactional
    public void removeStudentExpenses(Long id){
        studentExpensesRepository.deleteById(id);
    }

    @Transactional
    public void editSExp(AddStudentExpensesDto addStudentExpensesDto){
        StudentExpensesEntity cartToEdit = getItem(addStudentExpensesDto.getId());
        cartToEdit.setName(addStudentExpensesDto.getName());
        cartToEdit.setSemester(semesterService.getItem(addStudentExpensesDto.getSemester_id()));
        cartToEdit.setStudent(studentService.getItem(addStudentExpensesDto.getStudent_id()));
        cartToEdit.setState(false);
        studentExpensesRepository.save(cartToEdit);
    }
    @Transactional
    public boolean editStudentExpenses(AddStudentExpensesDto addStudentExpensesDto){
        try {
            editSExp(addStudentExpensesDto);
            StudentExpensesEntity cartToEdit = getItem(addStudentExpensesDto.getId());
            if(addStudentExpensesDto.getDetails().length>0){
                for (Long item : addStudentExpensesDto.getDetails()) {
                    ExpensesDetailsEntity entity = expensesDetailsRepository.findBy2ID(cartToEdit.getId(),item);
                    if(entity != null){
                        expensesDetailsRepository.deleteById(entity.getId());
                    } else {
                        ExpensesDetailsEntity entity1 = new ExpensesDetailsEntity();
                        entity1.setExpenses_price(expensesPriceService.getItem(item));
                        entity1.setStudent_expenses(cartToEdit);
                        expensesDetailsRepository.save(entity1);
                    }
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Transactional
    public void editItem(Long id){
        StudentExpensesEntity itemToEdit = getItem(id);
        itemToEdit.setState(true);
        studentExpensesRepository.save(itemToEdit);
    }

    @Transactional
    public void payCensor(Long id){
        StudentExpensesEntity itemToEdit = getItem(id);
        itemToEdit.setStatus(true);
        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        itemToEdit.setCompletion_date(date);
        studentExpensesRepository.save(itemToEdit);
    }

    public Map<Long, Long> saveListStudentExpenses(MultipartFile file) {
        try {
            List<StudentExpensesHP> invoiceHPS = ExcelStudentExpensesHelper.excelToCommunes(file.getInputStream());
            List<StudentExpensesEntity> invoiceEntities = new ArrayList<>();
            for (StudentExpensesHP invoiceHP : invoiceHPS) {
                StudentExpensesEntity invoice = new StudentExpensesEntity();
                invoice.setName(invoiceHP.getName());
                invoice.setStatus(false);
                invoice.setState(false);
                invoice.setCreated_date(invoiceHP.getCreated_date());
                invoice.setSemester(semesterService.getItem(invoiceHP.getSemester_id()));
                invoice.setStudent(studentService.getItem(invoiceHP.getStudent_id()));
                invoiceEntities.add(invoice);
            }
            StopWatch watch = new StopWatch();
            watch.start();
            Map<Long, Long> data = new HashMap<>();
            List<StudentExpensesEntity> invoiceEntities1 = studentExpensesRepository.saveAll(invoiceEntities);
            for (int i =0 ; i<invoiceEntities1.size();i++) {
                data.put(invoiceHPS.get(i).getId(),invoiceEntities1.get(i).getId());
            }
            watch.stop();
            log.info("Save communes {} time Elapsed: {}", invoiceEntities.size(), watch.getTotalTimeSeconds());
            return data;
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
