package com.automation.emailer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ErrorClassificationPojo {
	String projectName;
	String suiteType;
	String dateOfExecution;
	Long timeStamp;
	String issueDescription;
	ArrayList<String> testName;
	String issueType;
	String browserName;
	String browserVersion;
	boolean classified;
	Integer sizeOfTest;
	String sanitizeException;
	int totalAssertionError;
	int totalRetryFailures;
	String jiraTickets;
	String suggestiveJiraTickets;
	List<String> testWithNobug;
	int failuresWithTickets;
	int failuresWithoutTickets;
	String jiraTicketsWoFormat;
	String suggestiveTicketsWoFormat;
	String exceptionAt;
	List<String> qTestLinks;
	Map<String, String> packageQtestMap;
	String createDefectLink;
	String classifiedStatus;
	Boolean isFailure;
}
