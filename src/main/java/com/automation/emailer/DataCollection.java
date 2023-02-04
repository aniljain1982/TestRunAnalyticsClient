package com.automation.emailer;

import java.util.HashMap;
import java.util.Map;

import com.automation.helper.GeneralHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DataCollection {
	public void setRequiredData(String executionResults, GlobalPOJO globalPOJO) throws Exception{
		try {
			JsonObject object = new Gson().fromJson(executionResults, JsonObject.class);

			globalPOJO.setExecutionResults(object);
			globalPOJO.setProjectName(object.get("projectName").getAsJsonArray().get(0).toString().replace("\"", ""));
			globalPOJO.setSuiteName(object.get("suiteType").getAsJsonArray().get(0).toString().replace("\"", ""));
			globalPOJO.setRecipients(object.get("recipients").getAsJsonArray().get(0).toString().replace("\"", ""));
			globalPOJO.setSubject(object.get("subject").getAsJsonArray().get(0).toString().replace("\"", ""));
			globalPOJO.setJenkinsUrl(object.get("jenkinsUrl").getAsJsonArray().get(0).toString().replace("\"", ""));
			
			globalPOJO.setTotalPassed(object.get("totalPassed").getAsJsonArray().get(0).toString());
			globalPOJO.setTotalFailed(object.get("totalFailed").getAsJsonArray().get(0).toString());
			globalPOJO.setTotalSkipped(object.get("totalSkipped").getAsJsonArray().get(0).toString());

			globalPOJO.setCcRecipients("aniljain1982@gmail.com");

			// stats
			globalPOJO.setLongestRunningTests(getTopLongestRunningTests(globalPOJO));
			globalPOJO.setFailedTests(getFailedTests(globalPOJO));

			new EmailHelper().prepareEmailData(globalPOJO);
		} catch (Exception e) {
			throw new Exception("Issue in data collection" + new GeneralHelper().convertExceptionToString(e)); 
		}
		
	}

	public Map<String, String> getTopLongestRunningTests(GlobalPOJO globalPOJO) {
		Map<String, String> testDetails = new HashMap();
		try {

			JsonElement element = globalPOJO.getExecutionResults().get("longestRunTests").getAsJsonArray().get(0);
			testDetails = new Gson().fromJson(element, Map.class);
			if (testDetails.isEmpty()) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testDetails;
	}

	public Map<String, String> getFailedTests(GlobalPOJO globalPOJO) {
		Map<String, String> testDetails = new HashMap();
		try {

			JsonArray jsonArray = globalPOJO.getExecutionResults().get("failedDerivatives").getAsJsonArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				testDetails.put(jsonArray.get(i).getAsJsonObject().get("name").toString().replace("\"", ""),
						jsonArray.get(i).getAsJsonObject().get("exception").toString().replace("\"", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return testDetails;
	}
}
