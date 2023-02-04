package com.automation.client;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import com.automation.emailer.GlobalPOJO;
import com.automation.emailer.OfflineEmail;
import com.automation.helper.GeneralHelper;
import com.automation.helper.PropertyHelper;
import com.automation.report.ExtentHelper;
import com.google.gson.Gson;

public class AnalyticsClient {
	public static void main(String[] args) {
		System.out.println("Analytics Client v1.0.0");

		String projectName = "";
		String suiteType = "";

		try {
			if (args.length == 2) {
				projectName = args[0];
				suiteType = args[1];
			} else {
				throw new Exception("ProjectName and SuiteType are mandotory");
			}

			if (projectName == null || projectName.isEmpty()) {
				throw new Exception("projectName can not be null or empty");
			}

			if (suiteType == null || suiteType.isEmpty()) {
				throw new Exception("projectName can not be null or empty");
			}

			PropertyHelper propertyHelper = new PropertyHelper("test.properties");

			List<Object> listProjectName = new ArrayList<Object>();
			listProjectName.add(projectName);

			List<Object> listsuiteType = new ArrayList<Object>();
			listsuiteType.add(suiteType);

			List<Object> listRecipients = new ArrayList<Object>();
			String recipients = propertyHelper.getPropertyValue("recipients");
			listRecipients.add(recipients);

			List<Object> listPlatform = new ArrayList<Object>();
			listPlatform.add("Client");

			GlobalPOJO globalPOJO = new GlobalPOJO();
			globalPOJO.setProjectName(projectName);
			globalPOJO.setRecipients(recipients);
			
			System.out.println("Project Name: " + projectName);
			System.out.println("Suite Name: " + suiteType);
			System.out.println("Recipients: " + recipients);

			GeneralHelper generalHelper = new GeneralHelper();
			globalPOJO.setFileAttachment(generalHelper.getTheLatestExtentReportFile());
			Map<String, List<Object>> derivatives = new ExtentHelper()
					.fetchTheJsonFromExtentReport(generalHelper.getTheLatestExtentReportFile());

			int pass = derivatives.get("passedDerivatives").size();
			int fail = derivatives.get("failedDerivatives").size();
			int skip = derivatives.get("skippedDerivatives").size();
			int total = pass + fail + skip;

			String percentage = new Formatter().format("%.2f", ((double) pass / total) * 100).toString();
			percentage = percentage + "% Passed: ";

			String subject = percentage + propertyHelper.getPropertyValue("subject");
			List<Object> listSubject = new ArrayList<Object>();
			listSubject.add(subject);

			List<Object> listJenkinsUrl = new ArrayList<Object>();
			listJenkinsUrl.add(propertyHelper.getPropertyValue("jenkinsURL"));

			derivatives.put("projectName", listProjectName);
			derivatives.put("suiteType", listsuiteType);
			derivatives.put("recipients", listRecipients);
			derivatives.put("subject", listSubject);
			derivatives.put("jenkinsUrl", listJenkinsUrl);
			derivatives.put("platform", listPlatform);

			new OfflineEmail().invokeOfflineEmail(new Gson().toJson(derivatives), globalPOJO);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
