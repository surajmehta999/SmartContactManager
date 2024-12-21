package com.scm.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelGeneratorService {

    public byte[] generateExcelForSingleContact(Contact contact) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Contact");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Email", "Phone"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            // Add contact details
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(contact.getId().toString());
            dataRow.createCell(1).setCellValue(contact.getName());
            dataRow.createCell(2).setCellValue(contact.getEmail());
            dataRow.createCell(3).setCellValue(contact.getPhoneNumber());

            // Adjust column width
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}

