package com.zimplistic.rotimatic.pageobjects.Firmware;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;



public class FWUpdatePage{
	public static WebElement element;
	public boolean isPopup = true;
	public boolean isError = true;
	public boolean isContinue = true;

	public boolean popupDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver.findElementsByName("OK").isEmpty())
				isPopup = false;

		} catch (Exception e) {

			Assert.fail("Error in getting OK popup");
		}
		return isPopup;
	}

	public boolean errorDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver
					.findElementsByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Quit')]")
					.isEmpty())
				isError = false;

		} catch (Exception e) {

			Assert.fail("Error in getting Quit Alert screen");
		}
		return isError;
	}

	public boolean continueAlertDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver.findElementsByName("Continue").isEmpty())
				isContinue = false;

		} catch (Exception e) {

			Assert.fail("Error in getting Quit Alert screen");
		}
		return isContinue;
	}

	public WebElement getSportsMode(WiniumDriver driver) throws Exception {
		try {
			element = driver
					.findElementByXPath("//*[@AutomationId='checkBoxSpeed']");

		} catch (Exception e) {

			Assert.fail("Error in getting Sports Mode field");
		}
		return element;
	}
	
	public WebElement selectSerialNumber(WiniumDriver driver) throws Exception {
		try {
			element = driver
					.findElementByXPath("//*[@AutomationId='textBoxRotiSerial']");

		} catch (Exception e) {

			Assert.fail("Error in getting Serial Number field");
		}
		return element;
	}

	public WebElement getConnect(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='buttonConnect']");

		} catch (Exception e) {

			Assert.fail("Error in getting Connect button");
		}
		return element;
	}

	public WebElement selectStatus(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='textBoxMqttLog']");

		} catch (Exception e) {

			Assert.fail("Error in getting Status");
		}
		return element;
	}

	public WebElement selectFWVersion(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='buttonFwVersion']");
			
		} catch (Exception e) {

			Assert.fail("Error in getting FW version button");
		}
		return element;
	}

	public WebElement selectStartFWUpdate(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='buttonStartUpdate']");

		} catch (Exception e) {

			Assert.fail("Error in getting Start Update button");
		}
		return element;
	}

	public WebElement selectContinue(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByName("Continue");

		} catch (Exception e) {

			Assert.fail("Error in getting Continue button");
		}
		return element;
	}

	public WebElement getClearLogElement(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='textBoxMqttLog']");

		} catch (Exception e) {

			Assert.fail("Error in getting Clear Log field");
		}
		return element;
	}

	public WebElement selectOK(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElement(By.name("OK"));

		} catch (Exception e) {

			Assert.fail("Error in getting OK button");
		}
		return element;
	}

	public WebElement selectDisconnect(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByName("Disconnect");

		} catch (Exception e) {

			Assert.fail("Error in getting Disconnect button");
		}
		return element;
	}

	public WebElement selectQuit(WiniumDriver driver) throws Exception {
		try {
			element = driver
					.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Quit')]");

		} catch (Exception e) {

			Assert.fail("Error in getting Quit button");
		}
		return element;
	}

	public WebElement selectClose(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='Close']");

		} catch (Exception e) {

			Assert.fail("Error in getting Close icon");
		}
		return element;
	}

}
