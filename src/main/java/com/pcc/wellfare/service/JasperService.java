package com.pcc.wellfare.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.pcc.wellfare.model.Expenses;
import com.pcc.wellfare.repository.ExpensesRepository;
import com.pcc.wellfare.requests.ExpenseHistoryRequest;

import lombok.AllArgsConstructor;
import lombok.var;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@AllArgsConstructor
public class JasperService {

	@Autowired
	private final ExpensesRepository expensesRepository;
	
	@Autowired
	private ResourceLoader resourceLoader;

	public byte[] printExpenseHistoryReport(Integer month, Integer year, String type) throws JRException, IOException {
		Resource resource = resourceLoader.getResource("classpath:report/ExpenseHistory.jrxml");
	    InputStream ExpenseHistoryReportSteam = resource.getInputStream();
		
		List<Expenses> expenseList = new ArrayList<>();
		String ThaiMonth = getThaiMonth(month);
		String BuddhistYear = String.valueOf(year + 543);
		Map<String, Object> params = new HashMap<String, Object>();
		String typeName = (type.equals("ipd")) ? "ผู้ป่วยใน" : "ผู้ป่วยนอก";
		params.put("expenseType", typeName);
		params.put("expenseMonth", ThaiMonth);
		params.put("expenseYear", BuddhistYear);

		if (type.equals("ipd")) {
			expenseList = expensesRepository.getIpdExpenseByMonthAndYear(month, year);
		} else {
			expenseList = expensesRepository.getOpdExpenseByMonthAndYear(month, year);
		}

		List<ExpenseHistoryRequest> ExpenseHistoryList = new ArrayList<>();
		for (Expenses expense : expenseList) {
			ExpenseHistoryRequest ExpenseHistoryModel = ExpenseHistoryRequest
					.builder()
					.dateCount(expense.getDays())
					.dateOfAdmidtion(BuddhistDateInThaiFomat(expense.getDateOfAdmission()))
					.description(expense.getDescription())
					.endDate(BuddhistDateInThaiFomat(expense.getEndDate()))
					.firstname(expense.getEmployee().getTname())
					.remark(expense.getRemark())
					.startDate(BuddhistDateInThaiFomat(expense.getStartDate()))
					.surname(expense.getEmployee().getTsurname())
					.withdraw((double) expense.getCanWithdraw())
					.prefix(expense.getEmployee().getTprefix())
					.build();
			ExpenseHistoryList.add(ExpenseHistoryModel);
		}
		
		JasperReport jasperReport = JasperCompileManager.compileReport(ExpenseHistoryReportSteam);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ExpenseHistoryList);
		
		params.put("ExpenseHistoryDataSet", dataSource);
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
	    JasperExportManager.exportReportToPdfFile(jasperPrint, "Report.pdf");
	    File filePdf = new File("Report.pdf");
	    return FileUtils.readFileToByteArray(filePdf);
	}

	private String BuddhistDateInThaiFomat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = sdf.format(date);

		// เพิ่มค่า 543 ในปีก่อนที่จะแสดงผลลัพธ์
		String[] parts = formattedDate.split("/");
		int day = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[2]) + 543;

		return String.format("%02d/%02d/%02d", day, month, year);
	}

	public String getThaiMonth(int month) {
		switch (month) {
		case 1:
			return "มกราคม";
		case 2:
			return "กุมภาพันธ์";
		case 3:
			return "มีนาคม";
		case 4:
			return "เมษายน";
		case 5:
			return "พฤษภาคม";
		case 6:
			return "มิถุนายน";
		case 7:
			return "กรกฎาคม";
		case 8:
			return "สิงหาคม";
		case 9:
			return "กันยายน";
		case 10:
			return "ตุลาคม";
		case 11:
			return "พฤศจิกายน";
		case 12:
			return "ธันวาคม";
		default:
			return "";
		}
	}
}
