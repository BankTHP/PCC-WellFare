package com.pcc.wellfare.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pcc.wellfare.CSVRepresentation.DeptCsvRepresentation;
import com.pcc.wellfare.model.Dept;
import com.pcc.wellfare.repository.DeptRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptService {
	
	 private final DeptRepository deptrepository;
	 
	 public Integer uploadDepts(MultipartFile file) throws IOException {
	        Set<Dept> depts = parseCsv(file);
	        deptrepository.saveAll(depts);
	        return depts.size();
	    }

	    private Set<Dept> parseCsv(MultipartFile file) throws IOException {
	        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
	            HeaderColumnNameMappingStrategy<DeptCsvRepresentation> strategy =
	                    new HeaderColumnNameMappingStrategy<>();
	            strategy.setType(DeptCsvRepresentation.class);
	            CsvToBean<DeptCsvRepresentation> csvToBean =
	                    new CsvToBeanBuilder<DeptCsvRepresentation>(reader)
	                            .withMappingStrategy(strategy)
	                            .withIgnoreEmptyLine(true)
	                            .withIgnoreLeadingWhiteSpace(true)
	                            .build();
	            return csvToBean.parse()
	                    .stream()
	                    .map(csvLine -> Dept.builder()
	                            .divisionid(csvLine.getDivisionid())
	                            .remark(csvLine.getRemark())
	                            .edivision(csvLine.getEdivision())
	                            .tdivision(csvLine.getTdivision())
	                            .edept(csvLine.getEdept())
	                            .tdept(csvLine.getTdept())
	                            .company(csvLine.getCompany())
	                            .deptcode(csvLine.getDeptcode())
	                            .deptid(csvLine.getDeptid())
	                            .code(csvLine.getCode())
	                            .build()
	                    )
	                    .collect(Collectors.toSet());
	        }
	    }
}
