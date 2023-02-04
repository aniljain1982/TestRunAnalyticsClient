package com.automation.report;

import lombok.Data;

@Data
public class PassDerivation {

	private String description;
	private String status;
	private String timetaken;
	private String name;
	private String automationContent;
	private String customerGroups;

	public PassDerivation(String description, String status, String timetaken, String name, String automationContent,
			String customerGroups) {
		this.description = description;
		this.status = status;
		this.timetaken = timetaken;
		this.name = name;
		this.automationContent = automationContent;
		this.customerGroups = customerGroups;
	}

}
