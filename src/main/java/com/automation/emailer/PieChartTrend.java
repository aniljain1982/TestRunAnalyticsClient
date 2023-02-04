package com.automation.emailer;

import static com.googlecode.charts4j.Color.BLACK;
import static com.googlecode.charts4j.Color.WHITE;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

public class PieChartTrend {
	public String create3DPieChart(GlobalPOJO globalPOJO) throws Exception {
		try {

			HashMap<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("Pass", globalPOJO.getTotalPassed());
			hashmap.put("Fail", globalPOJO.getTotalFailed());
			hashmap.put("Skip", globalPOJO.getTotalSkipped());
			ArrayList<Slice> slices = new ArrayList<Slice>();
			String title = globalPOJO.getProjectName().toUpperCase() + ":[";
			if (!hashmap.get("Pass").equals("0")) {
				slices.add(Slice.newSlice(Integer.parseInt(hashmap.get("Pass")),
						com.googlecode.charts4j.Color.newColor("2b7488"), "Pass", "Pass"));
				title = title + "Pass=" + hashmap.get("Pass");
			}
			if (!hashmap.get("Fail").equals("0")) {
				slices.add(Slice.newSlice(Integer.parseInt(hashmap.get("Fail")),
						com.googlecode.charts4j.Color.newColor("FF5233"), "Fail", "Fail"));
				title = title + "," + "Fail=" + hashmap.get("Fail");
			}
			if (!hashmap.get("Skip").equals("0")) {
				slices.add(Slice.newSlice(Integer.parseInt(hashmap.get("Skip")),
						com.googlecode.charts4j.Color.newColor("2b7488"), "Skip", "Skip"));
				title = title + "," + "Skip=" + hashmap.get("Skip");
			}
			title = title + "]";

			title = title.substring(title.length() - 1, title.length()).equals(",")
					? title.substring(0, title.length() - 2) + "]"
					: title;

			slices.removeAll(Collections.singleton(null));
			PieChart chart = GCharts.newPieChart(slices);
			chart.setSize(500, 200);
			chart.setAreaFill(Fills.newSolidFill(WHITE));
			chart.setBackgroundFill(Fills.newSolidFill(WHITE));
			chart.setTitle(title, BLACK, 12);
			chart.setThreeD(true);
			Response resultImageResponse = Jsoup.connect(chart.toURLString()).ignoreContentType(true).execute();

			if (resultImageResponse.statusCode() != 200) {
				resultImageResponse = Jsoup.connect(chart.toURLString()).ignoreContentType(true).execute();
			}

			String chartFileString = "piechart_" + System.currentTimeMillis() + ".jpeg";
			File file = new File(chartFileString);
			FileOutputStream out = (new FileOutputStream(file));
			out.write(resultImageResponse.bodyAsBytes());
			out.close();
			return chartFileString;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error Generating Pie Chart";
		}
	}
}
