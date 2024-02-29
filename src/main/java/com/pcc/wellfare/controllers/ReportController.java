package com.pcc.wellfare.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcc.wellfare.service.JasperService;

import lombok.AllArgsConstructor;
import java.net.URLEncoder;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class ReportController {

	private final JasperService jasperService;
	
	@GetMapping("/expenseHistoryReport")
    public ResponseEntity<byte[]> printExpenseHistoryReport(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @RequestParam String type) {
        try {
            byte[] report = jasperService.printExpenseHistoryReport(month, year, type);
            Integer buddhistYear = year + 543;
            String filename = URLEncoder.encode("รายงานการเบิกค่ารักษาพยาบาลประจำเดือน" + jasperService.getThaiMonth(month) + buddhistYear + ".pdf", "UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", filename);
            headers.setContentLength(report.length);
            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
