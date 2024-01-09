package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.dto.InvoiceDetailsHP;
import com.trainingfinance.apisystem.dto.TeachingPeriodDetailsHP;
import com.trainingfinance.apisystem.entity.InvoiceDetailsEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import com.trainingfinance.apisystem.entity.TeachingPeriodEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.TeachingDetailsRepository;
import com.trainingfinance.apisystem.utils.ExcelInvoiceDetailsHelper;
import com.trainingfinance.apisystem.utils.ExcelTeachingDetailsHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class TeachingDetailsService {

    private final TeachingDetailsRepository teachingDetailsRepository;
    private final TeachingPeriodService teachingPeriodService;
    private final ClassCoefficientService classCoefficientService;
    private final SubjectService subjectService;

    public List<TeachingDetailsEntity> getAllTeachingDetails(){
        return teachingDetailsRepository.findAll();
    }

    public TeachingDetailsEntity getItem(Long id) {
        return teachingDetailsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void saveListTeachingDetails(MultipartFile file, Map<Long,Long> data) {
        try {
            List<TeachingPeriodDetailsHP> invoiceDetailsHPS = ExcelTeachingDetailsHelper.excelToCommunes(file.getInputStream());
            List<TeachingDetailsEntity> invoiceEntities = new ArrayList<>();
            for (TeachingPeriodDetailsHP invoiceDetailsHP : invoiceDetailsHPS) {
                TeachingDetailsEntity invoiceDetails = new TeachingDetailsEntity();
                invoiceDetails.setTeaching_period(teachingPeriodService.getItem(data.get(invoiceDetailsHP.getTeaching_period_id())));
                invoiceDetails.setClass_coefficient(classCoefficientService.getItem(invoiceDetailsHP.getClass_coefficient_id()));
                invoiceDetails.setSubject(subjectService.getItem(invoiceDetailsHP.getSubject_id()));
                invoiceEntities.add(invoiceDetails);
            }
            StopWatch watch = new StopWatch();
            watch.start();
            teachingDetailsRepository.saveAll(invoiceEntities);
            watch.stop();
            log.info("Save communes {} time Elapsed: {}", invoiceEntities.size(), watch.getTotalTimeSeconds());
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
