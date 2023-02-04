package com.automation.emailer;

import com.automation.helper.GeneralHelper;

public class EmailHelper {
	public void prepareEmailData(GlobalPOJO globalPOJO) throws Exception{
		System.out.println();
		System.out.println("Gathering data for email");
		try {
			HTMLInteractions htmlInterations = new HTMLInteractions(globalPOJO);

			String greeting = htmlInterations.getGreetings();

			// Pie Chart
			String pieChart = new PieChartTrend().create3DPieChart(globalPOJO);

			String errorGreeting = "";
			String flag = "";

			if (pieChart.equalsIgnoreCase("Error Generating Pie Chart")) {
				pieChart = "";
				if (flag.isEmpty()) {
					errorGreeting += "Pie Chart";
				} else if (flag.equalsIgnoreCase("trend")) {
					errorGreeting += " ,Pie Chart";
				}
				flag = "pie";
			}

			/**********************************
			 * Start of Email content
			 **********************************/
			String emailHtmlBody = "";

			if (flag.isEmpty()) {
				emailHtmlBody += greeting;
			} else {
				emailHtmlBody += errorGreeting;
				emailHtmlBody += htmlInterations.getLineBreaker() + htmlInterations.getLineBreaker();
			}

			emailHtmlBody += htmlInterations.getLineBreaker();
			
			emailHtmlBody+=htmlInterations.getSummary();

			emailHtmlBody += htmlInterations.getLineBreaker();
			
			emailHtmlBody += htmlInterations.getExecutionBreakUpString();

			if (!pieChart.isEmpty()) {
				emailHtmlBody += htmlInterations.getPieChartString();
				emailHtmlBody += htmlInterations.getLineBreaker();
			}

			emailHtmlBody += htmlInterations.getLineBreaker();
			
			//stats
			StatTables statTables=new StatTables(globalPOJO);
			
			emailHtmlBody +=statTables.generateFailedTestsTable();
			emailHtmlBody += htmlInterations.getLineBreaker();
			
			emailHtmlBody += htmlInterations.getEndLabel();

			/**********************************
			 * End of Email content
			 **********************************/
			globalPOJO.setEmailBody(emailHtmlBody);
			globalPOJO.setPieChart(pieChart);
			System.out.println("Successfully gather email data");
			System.out.println();
			new Emailer().sendEmail(globalPOJO);
			System.out.println();
			System.out.println("Email sent Successfully");
		} catch (Exception e) {
			throw new Exception("Issue in gathering email data --> " + new GeneralHelper().convertExceptionToString(e));
		}

	}
}
