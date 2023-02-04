package com.automation.emailer;

import java.io.Serializable;
import java.util.Map;

import com.google.gson.JsonObject;

import lombok.Data;

@Data
public class GlobalPOJO implements Serializable {
	public JsonObject executionResults;
	
	//Test
	String testId;
	String testName;
	
	// workspace details
	String suiteName;
	String projectName;
	String recipients;
	String subject;
	String jenkinsUrl;
	String ccRecipients;
	String browserName;
	String environment;

	// Email preparation
	String emailBody;
	String pieChart;
	String fileAttachment;
	
	// Latest test results
	String totalPassed;
	String totalFailed;
	String totalSkipped;

	// Stats
	Map<String, String> longestRunningTests;
	Map<String, String> failureHistory;
	Map<String, String> retryHistory;
	Map<String, String> failedTests;

}
