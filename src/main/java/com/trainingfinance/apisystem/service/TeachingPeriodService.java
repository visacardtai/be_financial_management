package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.dto.*;
import com.trainingfinance.apisystem.entity.*;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.TeachingDetailsRepository;
import com.trainingfinance.apisystem.repository.TeachingPeriodRepository;
import com.trainingfinance.apisystem.utils.ExcelInvoiceHelper;
import com.trainingfinance.apisystem.utils.ExcelTeachingPeriodHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Slf4j
@Service
public class TeachingPeriodService {

    private final TeachingPeriodRepository teachingPeriodRepository;
    private final TeachingDetailsRepository teachingDetailsRepository;
    private final LecturerService lecturerService;
    private final LecturePriceService lecturePriceService;
    private final TargetsService targetsService;
    private final SemesterService semesterService;
    private final ClassCoefficientService classCoefficientService;
    private final SubjectService subjectService;

    public List<TeachingPeriodEntity> getAllByIdLecture(Long lecturerId){
        return teachingPeriodRepository.findAllByIdLecture(lecturerId);
    }
    public TeachingPeriodEntity getNewByIdLecture(Long lecturerId){
        return teachingPeriodRepository.findNewByIdLecture(lecturerId);
    }

    public TeachingPeriodEntity getItem(Long id) {
        return teachingPeriodRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public List<TeachingPeriodEntity> getAllTeachingPeriod(boolean status, boolean is_used){
        return StreamSupport
                .stream(teachingPeriodRepository.findAllByStatus(status,is_used).spliterator(),false)
                .collect(Collectors.toList());
    }

    public List<TeachingPeriodEntity> getAllBySemesterId(Long semester_id) {
        return teachingPeriodRepository.findBySemesterId(semester_id);
    }

    @Transactional
    public void addItemToTeachingPeriod(TeachingPeriodEntity teachingPeriodEntity, List<TeachingDetailsEntity> teachingDetailsEntities){
        TeachingPeriodEntity entity = teachingPeriodRepository.save(teachingPeriodEntity);
        for (TeachingDetailsEntity item: teachingDetailsEntities) {
            item.setTeaching_period(entity);
            teachingDetailsRepository.save(item);
        }
    }

    @Transactional
    public void editTeachingPeriodAdmin(AddTeachingPeriod addTeachingPeriod){
        TeachingPeriodEntity cartToEdit = getItem(addTeachingPeriod.getId());
        Lecturer lecturer = lecturerService.getItem(addTeachingPeriod.getLecturer_id());
        cartToEdit.setSemester(semesterService.getItem(addTeachingPeriod.getSemester_id()));
        cartToEdit.setLecturer(lecturer);
        cartToEdit.setTargets(targetsService.getByNameNew(lecturer.getPosition().getName()));
        cartToEdit.setLecture_price(lecturePriceService.getByName(lecturer.getProfile().getQualification()));
        cartToEdit.setIsUsed(false);
        cartToEdit.setStatus(false);
        teachingPeriodRepository.save(cartToEdit);
    }
    @Transactional
    public boolean editTeachingDetails(AddTeachingPeriod addTeachingPeriod){
        try {
            editTeachingPeriodAdmin(addTeachingPeriod);
            TeachingPeriodEntity cartToEdit = getItem(addTeachingPeriod.getId());
            if(addTeachingPeriod.getListDelete().size() >0) {
                for (Long id: addTeachingPeriod.getListDelete()) {
                    teachingDetailsRepository.deleteById(id);
                }
            }
            if(addTeachingPeriod.getDetails().size()>0){
                for (AddTeachingDetails item : addTeachingPeriod.getDetails()) {
                    TeachingDetailsEntity entity1 = new TeachingDetailsEntity();
                    entity1.setClass_coefficient(classCoefficientService.getItem(item.getClass_coefficient_id()));
                    entity1.setSubject(subjectService.getItem(item.getSubject_id()));
                    entity1.setTeaching_period(cartToEdit);
                    teachingDetailsRepository.save(entity1);
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Transactional
    public void editItem(Long id){
        TeachingPeriodEntity itemToEdit = getItem(id);
        itemToEdit.setStatus(true);
        teachingPeriodRepository.save(itemToEdit);
    }

    @Transactional
    public void changeIsUsed(Long id){
        TeachingPeriodEntity itemToEdit = getItem(id);
        itemToEdit.setIsUsed(true);
        teachingPeriodRepository.save(itemToEdit);
    }

    public void removeTeachingPeriod(Long id){
        try {
            TeachingPeriodEntity teachingPeriod = getItem(id);
            List<TeachingDetailsEntity>  teachingDetailsEntities = teachingPeriod.getTeaching_details();
            for (TeachingDetailsEntity entity: teachingDetailsEntities) {
                System.out.println(entity.getId());
                teachingDetailsRepository.deleteByEID(entity.getId());
            }
            teachingPeriodRepository.deleteByEID(id);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public Map<Long, Long> saveLisTeachingPeriod(MultipartFile file) {
        try {
            List<TeachingPeriodHP> invoiceHPS = ExcelTeachingPeriodHelper.excelToCommunes(file.getInputStream());
            List<TeachingPeriodEntity> invoiceEntities = new ArrayList<>();
            for (TeachingPeriodHP invoiceHP : invoiceHPS) {
                TeachingPeriodEntity invoice = new TeachingPeriodEntity();
                invoice.setLecturer(lecturerService.getItem(invoiceHP.getLecturer_id()));
                invoice.setStatus(false);
                invoice.setIsUsed(false);
                invoice.setLecture_price(lecturePriceService.getItem(invoiceHP.getLecture_price_id()));
                invoice.setSemester(semesterService.getItem(invoiceHP.getSemester_id()));
                invoice.setTargets(targetsService.getItem(invoiceHP.getTargets_id()));
                invoiceEntities.add(invoice);
            }
            StopWatch watch = new StopWatch();
            watch.start();
            Map<Long, Long> data = new HashMap<>();
            List<TeachingPeriodEntity> invoiceEntities1 = teachingPeriodRepository.saveAll(invoiceEntities);
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
