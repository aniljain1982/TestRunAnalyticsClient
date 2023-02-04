package com.automation.helper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyHelper {
	private Properties properties;

	public PropertyHelper(String fileName) throws Exception {
		String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + fileName;
		File file = new File(path);
		FileInputStream fileInput = new FileInputStream(file);
		this.properties = new Properties();
		properties.load(fileInput);
	}

	public PropertyHelper(String fileName, String folderName) throws Exception {
		String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + folderName + File.separator + fileName;
		File file = new File(path);
		FileInputStream fileInput = new FileInputStream(file);
		this.properties = new Properties();
		properties.load(fileInput);
	}

	public String getPropertyValue(String propertyName) {
		return (properties.getProperty(propertyName) == null || properties.getProperty(propertyName).isEmpty())
				? "[ERROR] " + propertyName + " property not found"
				: properties.getProperty(propertyName);
	}

}
