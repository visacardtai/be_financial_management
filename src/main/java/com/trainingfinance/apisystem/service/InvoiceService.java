package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.dto.AddInvoiceDetailsDto;
import com.trainingfinance.apisystem.dto.AddInvoiceDto;
import com.trainingfinance.apisystem.dto.AddStudentExpensesDto;
import com.trainingfinance.apisystem.dto.InvoiceHP;
import com.trainingfinance.apisystem.entity.*;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.InvoiceDetailsRepository;
import com.trainingfinance.apisystem.repository.InvoiceRepository;
import com.trainingfinance.apisystem.utils.ExcelInvoiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;
    @Autowired
    SemesterService semesterService;
    @Autowired
    StudentService studentService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    CreditPriceService creditPriceService;
    @Autowired
    HistoryPaymentService hisPaymentService;

    public InvoiceEntity getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(()->new NotFoundException(invoiceId));
    }
    public List<InvoiceEntity> getInvoiceByIdStudent(Long studentId, boolean status) {
        return invoiceRepository.findByIdStudent(studentId, status);
    }

    public List<InvoiceEntity> getInvoiceByStateAndStatus(boolean status, boolean state) {
        return invoiceRepository.findByStateAndStatus(status,state);
    }

    public List<InvoiceEntity> getAllInvoiceByIdStudent(Long studentId) {
        return invoiceRepository.findByIdStudentAll(studentId);
    }

    public List<InvoiceEntity> getAllInvoiceBySemesterId(Long semester_id) {
        return invoiceRepository.findBySemesterId(semester_id);
    }

    public void addInvoice(InvoiceEntity invoice){
        invoiceRepository.save(invoice);
    }

    @Transactional
    public void addItemToInvoice(InvoiceEntity invoice, List<InvoiceDetailsEntity> invoiceDetailsEntities){
        InvoiceEntity invoice1 = invoiceRepository.save(invoice);
        for (InvoiceDetailsEntity item: invoiceDetailsEntities) {
            item.setInvoice(invoice1);
            invoiceDetailsRepository.save(item);
        }
    }

    @Transactional
    public void editInvoice(Long id, InvoiceEntity invoice){
        InvoiceEntity itemToEdit = getInvoice(id);
        itemToEdit.setName(invoice.getName());
        itemToEdit.setStatus(invoice.getStatus());
        itemToEdit.setExpiration_date(invoice.getExpiration_date());
        itemToEdit.setDate_of_payment(invoice.getDate_of_payment());
        invoiceRepository.save(itemToEdit);
    }

    @Transactional
    public void editInvoiceAdmin(AddInvoiceDto addInvoiceDto){
        InvoiceEntity cartToEdit = getInvoice(addInvoiceDto.getId());
        cartToEdit.setName(addInvoiceDto.getName());
        cartToEdit.setSemester(semesterService.getItem(addInvoiceDto.getSemester_id()));
        cartToEdit.setStudent(studentService.getItem(addInvoiceDto.getStudent_id()));
        cartToEdit.setState(false);
        cartToEdit.setStatus(false);
        cartToEdit.setExpiration_date(addInvoiceDto.getExpiration_date());
        invoiceRepository.save(cartToEdit);
    }
    @Transactional
    public boolean editInvoiceDetails(AddInvoiceDto addInvoiceDto){
        try {
            editInvoiceAdmin(addInvoiceDto);
            InvoiceEntity cartToEdit = getInvoice(addInvoiceDto.getId());
            if(addInvoiceDto.getListDelete().size() >0) {
                for (Long id: addInvoiceDto.getListDelete()) {
                    invoiceDetailsRepository.deleteById(id);
                }
            }
            List<InvoiceEntity> invoiceEntities = invoiceRepository.findByIdStudent(addInvoiceDto.getStudent_id(), true);
            Map<Long, Long> data = new HashMap<>();
            for (AddInvoiceDetailsDto item : addInvoiceDto.getDetails()) {
                data.put(item.getSubject_id(), 0L);
            }
            for (InvoiceEntity a : invoiceEntities) {
                List<InvoiceDetailsEntity> invoiceDetails = a.getInvoiceDetails();
                for (InvoiceDetailsEntity b : invoiceDetails) {
                    for (AddInvoiceDetailsDto item : addInvoiceDto.getDetails()) {
                        if (Objects.equals(item.getSubject_id(), b.getSubject().getSubjectId())) {
                            Long value = data.get(item.getSubject_id());
                            if (value != null) {
                                data.put(item.getSubject_id(), ++value);
                            }
                        }
                    }
                }
            }
            for (Map.Entry<Long, Long> entry : data.entrySet()) {
                InvoiceDetailsEntity entity1 = new InvoiceDetailsEntity();
                entity1.setSubject(subjectService.getItem(entry.getKey()));
                entity1.setInvoice(cartToEdit);
                if (entry.getValue() > 0){
                    entity1.setCreditPrice(creditPriceService.getByName("Kỹ Thuật 2"));
                } else {
                    entity1.setCreditPrice(creditPriceService.getByName("Kỹ Thuật"));
                }
                invoiceDetailsRepository.save(entity1);
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Transactional
    public void editItem(Long id){
        InvoiceEntity itemToEdit = getInvoice(id);
        itemToEdit.setState(true);
        invoiceRepository.save(itemToEdit);
    }

    @Transactional
    public void removeInvoice(Long id){
        invoiceRepository.deleteById(id);
    }

    public Map<Long, Long> saveListInvoice(MultipartFile file) {
        try {
            List<InvoiceHP> invoiceHPS = ExcelInvoiceHelper.excelToCommunes(file.getInputStream());
            List<InvoiceEntity> invoiceEntities = new ArrayList<>();
            for (InvoiceHP invoiceHP : invoiceHPS) {
                InvoiceEntity invoice = new InvoiceEntity();
                invoice.setName(invoiceHP.getName());
                invoice.setStatus(false);
                invoice.setState(false);
                invoice.setExpiration_date(invoiceHP.getExpiration_date());
                invoice.setStudent(studentService.getItem(invoiceHP.getStudent_id()));
                invoice.setSemester(semesterService.getItem(invoiceHP.getSemester_id()));
                invoiceEntities.add(invoice);
            }
            StopWatch watch = new StopWatch();
            watch.start();
            Map<Long, Long> data = new HashMap<>();
            List<InvoiceEntity> invoiceEntities1 = invoiceRepository.saveAll(invoiceEntities);
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
