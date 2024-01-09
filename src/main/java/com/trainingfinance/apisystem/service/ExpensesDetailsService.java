package com.trainingfinance.apisystem.service;

import com.trainingfinance.apisystem.dto.ExpensesDetailsHP;
import com.trainingfinance.apisystem.dto.TeachingPeriodDetailsHP;
import com.trainingfinance.apisystem.entity.ExpensesDetailsEntity;
import com.trainingfinance.apisystem.entity.TargetsEntity;
import com.trainingfinance.apisystem.entity.TeachingDetailsEntity;
import com.trainingfinance.apisystem.exception.NotFoundException;
import com.trainingfinance.apisystem.repository.ExpensesDetailsRepository;
import com.trainingfinance.apisystem.utils.ExcelExpensesDetailsHelper;
import com.trainingfinance.apisystem.utils.ExcelTeachingDetailsHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpensesDetailsService {
    private final ExpensesDetailsRepository expensesDetailsRepository;
    private final ExpensesPriceService expensesPriceService;
    private final StudentExpensesService studentExpensesService;


    public ExpensesDetailsEntity getItem(Long id) {
        return expensesDetailsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public ExpensesDetailsEntity getItem2ID(Long student_expenses_id,Long expenses_price_id) {
        return expensesDetailsRepository.findBy2ID(student_expenses_id,expenses_price_id);
    }

    public ExpensesDetailsEntity addItem(ExpensesDetailsEntity item){
        return expensesDetailsRepository.save(item);
    }

    public ExpensesDetailsEntity deleteItem(Long id){
        ExpensesDetailsEntity item = getItem(id);
        expensesDetailsRepository.delete(item);
        return item;
    }

    public void saveListExpensesDetails(MultipartFile file, Map<Long,Long> data) {
        try {
            List<ExpensesDetailsHP> invoiceDetailsHPS = ExcelExpensesDetailsHelper.excelToCommunes(file.getInputStream());
            List<ExpensesDetailsEntity> invoiceEntities = new ArrayList<>();
            for (ExpensesDetailsHP invoiceDetailsHP : invoiceDetailsHPS) {
                ExpensesDetailsEntity invoiceDetails = new ExpensesDetailsEntity();
                invoiceDetails.setExpenses_price(expensesPriceService.getItem(invoiceDetailsHP.getExpenses_price_id()));
                invoiceDetails.setStudent_expenses(studentExpensesService.getItem(data.get(invoiceDetailsHP.getStudent_expenses_id())));
                invoiceEntities.add(invoiceDetails);
            }
            StopWatch watch = new StopWatch();
            watch.start();
            expensesDetailsRepository.saveAll(invoiceEntities);
            watch.stop();
            log.info("Save communes {} time Elapsed: {}", invoiceEntities.size(), watch.getTotalTimeSeconds());
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
