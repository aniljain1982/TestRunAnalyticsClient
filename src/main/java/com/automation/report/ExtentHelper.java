package com.automation.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class ExtentHelper {

	public Map<String, List<Object>> fetchTheJsonFromExtentReport(String filePath) {
		Map<String, List<Object>> derivatives = new HashMap<String, List<Object>>();
		List<Object> listPassDerivatives = new ArrayList<Object>();
		List<Object> listFailDerivatives = new ArrayList<Object>();
		List<Object> listSkipDerivatives = new ArrayList<Object>();
		List<Object> listErrorDerivatives = new ArrayList<Object>();
		List<Object> listTotalTime = new ArrayList<Object>();
		List<Object> listPassed = new ArrayList<Object>();
		List<Object> listFailed = new ArrayList<Object>();
		List<Object> listRetried = new ArrayList<Object>();
		List<Object> listSkipped = new ArrayList<Object>();
		List<Object> listTotalTests = new ArrayList<Object>();
		List<Object> listOfLongestRunTests = new ArrayList<Object>();
		List<Object> listOfPassPercentage = new ArrayList<Object>();
		List<Object> listOfRunDate = new ArrayList<Object>();
		List<Object> extentTimeStamp = new ArrayList<Object>();

		Map<String, Long> testsDuration = new HashMap<String, Long>();

		try {
			Document doc = Jsoup.parse(new File(filePath), "UTF-8");
			Elements testName = doc.select("span.test-name");
			Elements testDescription = doc.select("div.test-desc");
			Elements testStatus = doc.select("div>span.test-status");
			Elements testTimeTaken = doc.select("div>span.label.time-taken");
			Elements totalTimeTaken = doc.select(".col.s2");
			Elements exceptions = doc.select("td.step-details");
			Elements automationContent = doc.select("span.category.label:nth-child(1)");
			Elements allTestGroups = doc.select(".category-list");
			Elements dateOfExecution = doc.select("span.label.suite-start-time");
			listOfRunDate.add(convertDateToRightFormat(dateOfExecution.get(0).text()));
			extentTimeStamp.add(convertSuiteStartTimeToEpoch(dateOfExecution.get(0).text().trim()));
			for (int i = 0; i < testDescription.size(); i++) {
				Elements elements = allTestGroups.get(i).getElementsByClass("category label white-text");
				String groupNames = new String();
				for(int j=1;j<elements.size();j++) {
					String group = elements.get(j).text().trim();
					if(groupNames.isEmpty()) {
						groupNames = group;
					}else {
						groupNames = groupNames + "," + group;
					}					
				}
				int brandIndex = 0;
				if( automationContent.get(i).text().contains("com.apttus")) {
					brandIndex = automationContent.get(i).text().indexOf(":com.apttus");
				}else {
					brandIndex = automationContent.get(i).text().indexOf(":com.conga");
				}
				if (testStatus.get(i).text().trim().equals("pass")) {
					PassDerivation passDerivation = new PassDerivation(testDescription.get(i).text().trim(),
							testStatus.get(i).text().trim(), testTimeTaken.get(i).text().split("\\+")[0].split("h")[1],
							testName.get(i).text().trim(),
							automationContent.get(i).text().trim().substring(brandIndex + 1,
									automationContent.get(i).text().length()),groupNames);
					listPassDerivatives.add(passDerivation);
					testsDuration.put(testName.get(i).text().trim(),
							convertTimeToMilli(convertExtentTime(testTimeTaken.get(i).text().trim())));
				} else if (testStatus.get(i).text().trim().equals("fail")) {
					FailDerivation failDerivation = new FailDerivation(testDescription.get(i).text().trim(),
							testStatus.get(i).text().trim(), testTimeTaken.get(i).text().split("\\+")[0].split("h")[1],
							exceptions.get(i).text().trim(), testName.get(i).text().trim(),
							automationContent.get(i).text().trim().substring(brandIndex + 1,
									automationContent.get(i).text().length()),groupNames);
					listFailDerivatives.add(failDerivation);
					testsDuration.put(testName.get(i).text().trim(),
							convertTimeToMilli(convertExtentTime(testTimeTaken.get(i).text().trim())));
				} else if (testStatus.get(i).text().trim().equals("skip")) {
					SkipDerivation skipDerivation = new SkipDerivation(testDescription.get(i).text().trim(),
							testStatus.get(i).text().trim(), testTimeTaken.get(i).text().split("\\+")[0].split("h")[1],
							testName.get(i).text().trim(),
							automationContent.get(i).text().trim().substring(brandIndex + 1,
									automationContent.get(i).text().length()),groupNames);
					listSkipDerivatives.add(skipDerivation);
					testsDuration.put(testName.get(i).text().trim(),
							convertTimeToMilli(convertExtentTime(testTimeTaken.get(i).text().trim())));
				} else if (testStatus.get(i).text().trim().equals("error")) {
					ErrorDerivation errorDerivation = new ErrorDerivation(testDescription.get(i).text().trim(),
							testStatus.get(i).text().trim(), testTimeTaken.get(i).text().split("\\+")[0].split("h")[1],
							testName.get(i).text().trim(),
							automationContent.get(i).text().trim().substring(brandIndex + 1,
									automationContent.get(i).text().length()),groupNames);
					listErrorDerivatives.add(errorDerivation);
				}
			}

			LinkedHashMap<String, String> longestRunMap = sortValues(testsDuration);

			derivatives.put("passedDerivatives", listPassDerivatives);
			derivatives.put("failedDerivatives", listFailDerivatives);
			derivatives.put("skippedDerivatives", listSkipDerivatives);
			derivatives.put("errorDerivatives", listErrorDerivatives);
			String timeTaken= totalTimeTaken.get(4).text();
			String totalTime;
			if(timeTaken.contains("+")) {
				totalTime = timeTaken.split("Taken ")[1];
			}else {
				totalTime = convertToTime(timeTaken.split("ms")[0].split(" ")[timeTaken.split("ms")[0]
						.split(" ").length - 1]).replace(":", " ");
			}

			listTotalTime.add(convertExtentTime((totalTime)));
			listPassed.add(listPassDerivatives.size());
			listFailed.add(listFailDerivatives.size());
			listRetried.add(listErrorDerivatives.size());
			listSkipped.add(listSkipDerivatives.size());
			listTotalTests.add(listPassDerivatives.size() + listFailDerivatives.size() + listSkipDerivatives.size());
			listOfLongestRunTests.add(longestRunMap);

			double passPercentage = Double.valueOf(listPassed.get(0).toString()) / Double.valueOf(listTotalTests.get(0).toString());

			listOfPassPercentage.add(passPercentage*100);

			derivatives.put("totalTime", listTotalTime);
			derivatives.put("totalPassed", listPassed);
			derivatives.put("totalFailed", listFailed);
			derivatives.put("totalRetried", listRetried);
			derivatives.put("totalSkipped", listSkipped);
			derivatives.put("totalTestsRun", listTotalTests);
			derivatives.put("longestRunTests", listOfLongestRunTests);
			derivatives.put("passPercentage", listOfPassPercentage);
			derivatives.put("dateOfExecution", listOfRunDate);
			derivatives.put("executionTimeStamp", extentTimeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return derivatives;

	}
	
	public long convertSuiteStartTimeToEpoch(String suiteStartTime) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss aa");
		Date date = df.parse(suiteStartTime);
		long epoch = date.getTime();
		return epoch;
	}
	
	public String convertDateToRightFormat(String date) {
		String pattern = "dd-MM-YYYY";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		return simpleDateFormat.format(new Date(date));

	}

	public static String convertToTime(String millis) {
		String times = String.format("%02d h:%02d m:%02d s",
				TimeUnit.MILLISECONDS.toHours(Long.parseLong(millis.replace(",", ""))),
				TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(millis.replace(",", ""))) - TimeUnit.HOURS
						.toMinutes(TimeUnit.MILLISECONDS.toHours(Long.parseLong(millis.replace(",", "")))),
				TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(millis.replace(",", ""))) - TimeUnit.MINUTES
						.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(millis.replace(",", "")))));
		String newtime = (times.contains("00 h:")) ? times.split("00 h:")[1] : times;
		return newtime;
	}

	public long convertTimeToMilli(String strTime) {
		long milliSeconds = 0;
		try {
			long hours = Long.parseLong(strTime.split("h")[0]) * 60 * 60 * 1000;
			long minutes = Long.parseLong(strTime.split("m")[0].split(" ")[1]) * 60 * 1000;
			long seconds = Long.parseLong(strTime.split("s")[0].split("m ")[1]) * 1000;
			long millis = Long.parseLong(strTime.split("ms")[0].split("\\+")[1]);
			milliSeconds = hours + minutes + seconds + millis;
		}catch(Exception e) {
			return milliSeconds;
		}
		return milliSeconds;
	}

	public String convertExtentTime(String valueToConvert) {
		if (valueToConvert.contains("h")) {
			valueToConvert = valueToConvert.replace(" h", "h");
		}

		if (valueToConvert.contains("m")) {
			valueToConvert = valueToConvert.replace(" m", "m");
		}

		if (valueToConvert.contains("s")) {
			valueToConvert = valueToConvert.replace(" s", "s");
		}
		return valueToConvert;
	}
	
	public LinkedHashMap<String, String> sortValues(Map<String, Long> data) {
		List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(data.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
		int size = list.size();
		if(size > 5)
			{
				for (int i = size - 1; i > size - 6; i--) {
					Map.Entry<String, Long> aa = list.get(i);
					sortedMap.put(aa.getKey(), convertToTime(String.valueOf(aa.getValue())));
				}
			}
		else {
			while(size-1 >= 0) {
				size --;
				Map.Entry<String, Long> aa = list.get(size);
				sortedMap.put(aa.getKey(), convertToTime(String.valueOf(aa.getValue())));
				
			}
		}
		return sortedMap;

	}
	
	public static void main(String[] args) {
		new ExtentHelper().fetchTheJsonFromExtentReport("C:\\Conga-workspace\\TA-Offline\\frameworkservices\\offlineClient\\test-output\\AutomationTestReport1643969882582.html");
	}
}
