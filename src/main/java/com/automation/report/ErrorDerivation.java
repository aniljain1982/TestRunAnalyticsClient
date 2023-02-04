package com.automation.report;
import lombok.Data;

@Data
public class ErrorDerivation {

	private String description;
	private String status;
	private String timetaken;
	private String name;
	private String automationContent;
	private String customerGroups;
	
	public ErrorDerivation(String description, String status, String timetaken, String name, String automationContent,String customerGroups) {
		this.description = description;
		this.status = status;
		this.timetaken = timetaken;
		this.name = name;
		this.automationContent = automationContent;
		this.customerGroups = customerGroups;
	}

}