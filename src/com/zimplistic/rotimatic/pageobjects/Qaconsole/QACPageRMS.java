package com.zimplistic.rotimatic.pageobjects.Qaconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;

public class QACPageRMS {
	public static WebElement element;

	public WebElement selectRMS(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElement(By.name("RMS"));

		} catch (Exception e) {

			Assert.fail("Error in getting RMS tab");
		}
		return element;
	}

	public WebElement selectSerialNumber(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='textBoxRotiSerialNo']");

		} catch (Exception e) {

			Assert.fail("Error in getting Serial Number field");
		}
		return element;
	}
	
	public WebElement selectStatus(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='textBoxBrokerConnStatus']");

		} catch (Exception e) {

			Assert.fail("Error in getting Status field");
		}
		return element;
	}
	
	public WebElement selectDisconnectClient(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElement(By.name("Disconnect"));

		} catch (Exception e) {

			Assert.fail("Error in getting Disconnect Client button");
		}
		return element;
	}


}
