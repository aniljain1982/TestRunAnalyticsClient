package com.automation.report;

import lombok.Data;

@Data
public class FailDerivation {

	private String description;
	private String status;
	private String timetaken;
	private String exception;
	private String name;
	private String automationContent;
	private String customerGroups;

	public FailDerivation(String description, String status, String timetaken, String exception, String name,
			String automationContent, String customerGroups) {
		this.description = description;
		this.status = status;
		this.timetaken = timetaken;
		this.exception = exception;
		this.name = name;
		this.automationContent = automationContent;
		this.customerGroups = customerGroups;
	}

}
