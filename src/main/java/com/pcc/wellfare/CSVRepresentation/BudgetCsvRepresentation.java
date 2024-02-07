package com.pcc.wellfare.CSVRepresentation;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetCsvRepresentation {
	
	@CsvBindByName(column = "ID")
	private Long id;
	
	@CsvBindByName(column = "Level")
	private String level;
	
	@CsvBindByName(column = "OPD")
	private String opd;
	
	@CsvBindByName(column = "IPD")
	private String ipd;
	
	@CsvBindByName(column = "Room")
	private String room;
}
