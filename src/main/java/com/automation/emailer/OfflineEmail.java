package com.automation.emailer;

public class OfflineEmail {
	public void invokeOfflineEmail(String data, GlobalPOJO globalPOJO) throws Exception{
		new DataCollection().setRequiredData(data, globalPOJO);
	}
}
