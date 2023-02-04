package com.automation.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

public class GeneralHelper {
	public String getTheLatestExtentReportFile() throws Exception {
		File testOutputLocation = new File(System.getProperty("user.dir") + File.separator + "test-output");
		if (testOutputLocation.exists()) {
			Path dir = (Path) Paths.get(testOutputLocation.getAbsolutePath());
			Optional<Path> lastFilePath = Files.list(dir)
					.filter(f -> !Files.isDirectory(f) && f.getFileName().toString().startsWith("AutomationTestReport"))
					.max(Comparator.comparingLong(f -> f.toFile().lastModified()));
			if (lastFilePath.isPresent()) {
				return lastFilePath.toString().replace("[", "").replace("]", "").replace("Optional", "").trim();
			} else {
				throw new Exception("[ERROR] Error finding the Latest Report File in the TEST-OUTPUT");
			}
		} else {
			throw new Exception("TEST-OUTPUT folder not found in the workspace");
		}
	}

	public String convertExceptionToString(Exception e) {

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();

	}

	public void createFile(String contents, String fileExtension) {
		Writer writer = null;
		try {
			String fileName = "file_" + System.currentTimeMillis() + "." + fileExtension;
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
			writer.write(contents);
		} catch (IOException ex) {
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}
}
