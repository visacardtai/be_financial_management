package com.trainingfinance.apisystem.utils;

import com.trainingfinance.apisystem.dto.StudentExpensesHP;
import com.trainingfinance.apisystem.dto.TeachingPeriodHP;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelStudentExpensesHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "commune";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<StudentExpensesHP> excelToCommunes(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
//            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<StudentExpensesHP> communes = new ArrayList<StudentExpensesHP>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                StudentExpensesHP invoice = new StudentExpensesHP();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            invoice.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            invoice.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            invoice.setCreated_date(currentCell.getDateCellValue());
                            break;
                        case 3:
                            invoice.setStudent_id((long) currentCell.getNumericCellValue());
                            break;
                        case 4:
                            invoice.setSemester_id((long) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                communes.add(invoice);
            }
            workbook.close();
            return communes;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
