package com.trainingfinance.apisystem.controller;


import com.trainingfinance.apisystem.dto.*;
import com.trainingfinance.apisystem.entity.*;

import com.trainingfinance.apisystem.repository.InvoiceRepository;
import com.trainingfinance.apisystem.service.*;
import com.trainingfinance.apisystem.utils.ExcelInvoiceDetailsHelper;
import com.trainingfinance.apisystem.utils.ExcelInvoiceHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@RestController
@RequestMapping("/api/v1")
@Validated
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    InvoiceDetailsService invoiceDetailsService;
    @Autowired
    StudentService studentService;
    @Autowired
    SemesterService semesterService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    CreditPriceService creditPriceService;
    @GetMapping("/public/invoice/")
    public ResponseEntity<List<InvoiceDto>> getAllInvoiceByStudentId(@RequestParam("studentId") Long studentId,
                                                                     @RequestParam("status") boolean status) {
        List<InvoiceEntity> invoiceEntities = invoiceService.getInvoiceByIdStudent(studentId, status);
        List<InvoiceDto> invoiceDtoList = invoiceEntities.stream().map(InvoiceDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(invoiceDtoList,HttpStatus.OK);
    }

    @GetMapping("/student/invoice/chart")
    public ResponseEntity<?> getChartByStudentId(@RequestParam("studentId") Long studentId) {
        List<InvoiceEntity> invoiceEntities = invoiceService.getAllInvoiceByIdStudent(studentId);
        double sum = 0.0;
        double retired = 0.0;
        double notRetired = 0.0;
        int num_unpaid = 0;
        int num = invoiceEntities.size();
        Map<String,Object> data = new HashMap<>();
        for (InvoiceEntity a:invoiceEntities) {
            if (a.getStatus() == true) {
                for (InvoiceDetailsEntity b : a.getInvoiceDetails()) {
                    retired += b.getCreditPrice().getPrice() * b.getSubject().getCreditNum();
                }
            } else {
                for (InvoiceDetailsEntity b : a.getInvoiceDetails()){
                    notRetired +=b.getCreditPrice().getPrice() * b.getSubject().getCreditNum();

                }
                num_unpaid++;
            }
        }
        sum = retired + notRetired;
        data.put("num",num);
        data.put("retired",retired);
        data.put("sum",sum);
        data.put("num_unpaid",num_unpaid);
        data.put("not_retired",notRetired);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @GetMapping("/expert/invoice/alls")
    public ResponseEntity<List<InvoiceDto>> getAllInvoiceByStateAndStatus(@RequestParam("status") boolean status,
                                                                     @RequestParam("state") boolean state) {
        List<InvoiceEntity> invoiceEntities = invoiceService.getInvoiceByStateAndStatus(status,state);
        List<InvoiceDto> invoiceDtoList = invoiceEntities.stream().map(InvoiceDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(invoiceDtoList,HttpStatus.OK);
    }
    @PostMapping("/expert/invoice")
    public ResponseEntity<?> addInvoice(@Valid @RequestBody AddInvoiceDto addInvoiceDto) {
        var message = "";
        try {
            // create invoice
            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setName(addInvoiceDto.getName());
            invoice.setState(false);
            invoice.setStatus(false);
            Student student = studentService.getItem(addInvoiceDto.getStudent_id());
            invoice.setExpiration_date(addInvoiceDto.getExpiration_date());
            invoice.setStudent(student);
            invoice.setSemester(semesterService.getItem(addInvoiceDto.getSemester_id()));

            // create invoice details
            List<InvoiceDetailsEntity> entityList = new ArrayList<>();
            List<InvoiceEntity> invoiceEntities = invoiceService.getInvoiceByIdStudent(addInvoiceDto.getStudent_id(), true);
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
                InvoiceDetailsEntity entity = new InvoiceDetailsEntity();
                entity.setSubject(subjectService.getItem(entry.getKey()));
                if (entry.getValue() > 0){
                    entity.setCreditPrice(creditPriceService.getByName("Kỹ Thuật 2"));
                } else {
                    entity.setCreditPrice(creditPriceService.getByName("Kỹ Thuật"));
                }
                entityList.add(entity);
            }
            invoiceService.addItemToInvoice(invoice, entityList);
            message = "Add Invoice successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
            }
            catch(Exception e){
                message = "Could not add Invoice";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }

    @PutMapping("/expert/invoice")
    public ResponseEntity<?> editInvoiceByAdmin(@Valid @RequestBody AddInvoiceDto AddInvoiceDto) {
        var message = "";
        try {
            boolean check = invoiceService.editInvoiceDetails(AddInvoiceDto);
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

    @PutMapping("/expert/invoice/censor")
    public ResponseEntity<?> update(@RequestBody Integer[] response){
        if (Arrays.stream(response).count() != 0) {
            for (int index:response) {
                if (invoiceService.getInvoice((long) index).getState() != true) {
                    invoiceService.editItem((long) index);;
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


    @DeleteMapping("/expert/invoice")
    public ResponseEntity<?> deleteInvoice(@RequestBody Long[] response) {
        var message = "";
        try {
            if (Arrays.stream(response).count() != 0) {
                for (long index:response) {
                    if (invoiceService.getInvoice(index).getStatus() != true &&
                            invoiceService.getInvoice(index).getState() != true){
                        invoiceService.removeInvoice(index);
                    }
                }
            } else {
                message = "Could not Delete Invoice";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
            message = "Delete Invoice successfully";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not Delete Invoice";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/expert/invoice/upload")
    public ResponseEntity<Object> uploadInvoiceFile(@RequestParam("file") MultipartFile files) {
        var message = "";
        if (ExcelInvoiceHelper.hasExcelFormat(files)) {
            try {
                Map<Long,Long> data = invoiceService.saveListInvoice(files);
                invoiceDetailsService.saveListInvoiceDetails(files,data);
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

    @GetMapping("/expert/invoice/chart")
    public ResponseEntity<?> getAllInvoiceBySemester(@RequestParam("semester_id") long semester_id) {
        List<InvoiceEntity> invoiceEntities = invoiceService.getAllInvoiceBySemesterId(semester_id);
        List<InvoiceDto> invoiceDtoList = invoiceEntities.stream().map(InvoiceDto::from).collect(Collectors.toList());
        double total=0, debt=0, paid=0;
        List<Object> list_debt = new ArrayList<>();
        List<Object> list_paid = new ArrayList<>();
        for (InvoiceDto item:invoiceDtoList) {
            if(item.getStatus()){
                Map<String, Object> data1= new HashMap<>();
                data1.put("invoice_id",item.getId());
                data1.put("invoice_name",item.getName());
                data1.put("student_id",item.getStudent_id());
                data1.put("student_name",item.getStudent_name());
                data1.put("expiration",item.getExpiration_date());
                data1.put("payment",item.getDate_of_payment());
                data1.put("total",item.getTotal());
                paid += item.getTotal();
                list_paid.add(data1);
            } else {
                Map<String, Object> data2= new HashMap<>();
                data2.put("invoice_id",item.getId());
                data2.put("invoice_name",item.getName());
                data2.put("student_name",item.getStudent_name());
                data2.put("student_id",item.getStudent_id());
                data2.put("expiration",item.getExpiration_date());
                data2.put("total",item.getTotal());
                list_debt.add(data2);
                debt += item.getTotal();
            }
        }
        total = debt + paid;
        Map<String, Object> data= new HashMap<>();
        BigDecimal bigDecimal1 = new BigDecimal(total);
        BigDecimal bigDecimal2 = new BigDecimal(debt);
        BigDecimal bigDecimal3 = new BigDecimal(paid);
        data.put("total", bigDecimal1);
        data.put("debt",bigDecimal2);
        data.put("paid",bigDecimal3);
        data.put("list_debt", list_debt);
        data.put("list_paid", list_paid);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

}
