package com.automation.emailer;

import java.util.List;
import java.util.Map;

import com.automation.helper.GeneralHelper;

public class StatTables {
	private GlobalPOJO globalPOJO;
	static String htmlStyle = "<style type=\"text/css\"> \r\n" + "th>td \r\n" + "{ \r\n" + "white-space:wrap;\r\n"
			+ "text-align: center; \r\n" + "}\r\n" + "table \r\n" + "{\r\n" + "white-space:nowrap;\r\n"
			+ "color: black;\r\n" + "border:1px black solid;" + "margin:0px auto;" + "} \r\n" + "th \r\n" + "{  \r\n"
			+ "white-space:nowrap;\r\n" + "border:1px black solid;" + "background-color: #2b7488;\r\n"
			+ "color: #000000;" + "tr:nth-child(even) {\r\n" + "background-color: #000000;" + "}\r\n"
			+ "tr:nth-child(odd) {\r\n" + "background-color: #E3F0F4;" + "}\r\n"

			+ "</style>";

	public StatTables(GlobalPOJO globalPOJO) {
		this.globalPOJO = globalPOJO;
	}

	public String generateTimeTakenTable() throws Exception {

		try {
			String htmlCode = htmlStyle + "<div><b><center>    " + "<u>" + "Top 5 Longest Run Tests Today" + "</u>"
					+ "</b>" + "</center><br></div>";

			htmlCode = htmlCode + "<table align=\"center\"> "
					+ "<tr style=\"background-color:#2b7488;border:1px solid black;\">"
					+ "<th><center><Font color=\"white\">Test Name</Font></center></th>"
					+ "<th><center><Font color=\"white\">Time Taken</Font></center></th>";
			int counters = 1;
			Map<String, String> data = globalPOJO.getLongestRunningTests();

			if (data == null || data.size() == 0) {

				String color = (counters % 2 == 0) ? "<tr style=\"background-color:#E3F0F4\">" : "<tr color>";
				htmlCode = htmlCode.trim() + color
						+ "<td><center>Data in this table is not applicable for this run</center></td>"
						+ "<td style=\"border-left:1px solid black;\"><center>Data in this table is not applicable for this run</center></td></tr>";
				counters++;

			} else {
				for (Map.Entry<String, String> entry : data.entrySet()) {
					String color = (counters % 2 == 0) ? "<tr style=\"background-color:#E3F0F4\">" : "<tr color>";
					htmlCode = htmlCode.trim() + color + "<td><center>" + entry.getKey() + "</center></td>"
							+ "<td style=\"border-left:1px solid black;\"><center>" + entry.getValue()
							+ "</center></td></tr>";
					counters++;

				}
			}

			htmlCode = htmlCode + "</table>";

			return htmlCode;
		} catch (Exception e) {
			throw new Exception("Exception occured while invoking generateTimeTakenTable :" + e);
		}
	}
	
	public String generateFailedTestsTable() throws Exception {

		try {
			String htmlCode = htmlStyle + "<div><b><center>    " + "<u>" + "Failed Tests Details" + "</u>"
					+ "</b>" + "</center><br></div>";

			htmlCode = htmlCode + "<table align=\"center\"> "
					+ "<tr style=\"background-color:#2b7488;border:1px solid black;\">"
					+ "<th><center><Font color=\"white\">Test Name</Font></center></th>"
					+ "<th><center><Font color=\"white\">Exception</Font></center></th>";
			int counters = 1;
			
			Map<String, String> data=globalPOJO.getFailedTests();
			if (data == null || data.size() == 0) {

				String color = (counters % 2 == 0) ? "<tr style=\"background-color:#E3F0F4\">" : "<tr color>";
				htmlCode = htmlCode.trim() + color
						+ "<td><center>Data in this table is not applicable for this run</center></td>"
						+ "<td style=\"border-left:1px solid black;\"><center>Data in this table is not applicable for this run</center></td></tr>";
				counters++;

			} else {
				for (Map.Entry<String, String> entry : data.entrySet()) {
					String color = (counters % 2 == 0) ? "<tr style=\"background-color:#E3F0F4\">" : "<tr color>";
					htmlCode = htmlCode.trim() + color + "<td><center>" + entry.getKey() + "</center></td>"
							+ "<td style=\"border-left:1px solid black;\"><center>" + entry.getValue()
							+ "</center></td></tr>";
					counters++;

				}
			}

			htmlCode = htmlCode + "</table>";

			return htmlCode;
		} catch (Exception e) {
			throw new Exception("Exception occured while invoking generateTimeTakenTable :" + e);
		}
	}
	
	
	public String errorClassificationTable(List<ErrorClassificationPojo> listErrorClassificationPojo) throws Exception {

		try {
			String htmlCode = htmlStyle + "<div><b><center>    " + "<u>" + "Test Failure Classification" + "</u>"
					+ "</b>" + "</center><br></div>";

			htmlCode = htmlCode + "<table align=\"center\"> "
					+ "<tr style=\"background-color:#2b7488;border:1px solid black;\">"
					+ "<th><Font color=\"white\">Exception</Font></th>"
					+ "<th><center><Font color=\"white\">Test count</Font></center></th>"
					+ "<th><center><Font color=\"white\">Test Names</Font></center></th>";
			int counters = 1;
			
			if(listErrorClassificationPojo==null || listErrorClassificationPojo.size()==0) {
				String color = (counters % 2 == 0) ? "<tr style=\"background-color:#E3F0F4\">" : "<tr color>";
				htmlCode = htmlCode.trim() + color
						+ "<td><center>Data in this table is not applicable for this run</center></td>"
						+ "<td style=\"border-left:1px solid black;\"><center>Data in this table is not applicable for this run</center></td>"
						+ "<td style=\"border-left:1px solid black;\"><center>Data in this table is not applicable for this run</center></td>"
						+ "</tr>";
				counters++;
				
			}else {
				
				for (ErrorClassificationPojo errorClassificationPojo : listErrorClassificationPojo) {
					String result = "<br>";
					for(String test:errorClassificationPojo.getTestName()) {
						result+=test + "<br><br>";
					}
					String color = (counters % 2 == 0) ? "<tr style=\"background-color:#E3F0F4\">" : "<tr color>";
					htmlCode = htmlCode.trim() + color + "<td width=\"100\">" + errorClassificationPojo.getIssueDescription() + "</td>"
							+ "<td width=\"100\"><center>" + errorClassificationPojo.getTestName().size()
							+ "</center></td>"
							+ "<td style=\"border-left:1px solid black;\"><center>" + result
							+ "</center></td>"
							+ "</tr>";
					counters++;
				}
			}

			htmlCode = htmlCode + "</table>";
			
			new GeneralHelper().createFile(htmlCode, "html");
			return htmlCode;
		} catch (Exception e) {
			throw new Exception("Exception occured while invoking generateTimeTakenTable :" + e);
		}
	}

}
