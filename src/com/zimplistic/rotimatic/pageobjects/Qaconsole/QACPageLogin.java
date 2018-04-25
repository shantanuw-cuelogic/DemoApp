package com.zimplistic.rotimatic.pageobjects.Qaconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;

public class QACPageLogin {

	public static WebElement element;
	public boolean isLogin = false;
	public boolean isManual = false;
	public boolean isPopup = false;
	

	public boolean loginDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver.findElementsByName("Log in").isEmpty())
				isLogin = true;

		} catch (Exception e) {

			Assert.fail("Error in getting login screen");
		}
		return isLogin;
	}

	public WebElement selectGoogleSignIn(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[contains(@AutomationId,'pictureBoxGSignIn')]");

		} catch (Exception e) {

			Assert.fail("Error in getting Google Sign In field");
		}
		return element;
	}

	public boolean popupDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver.findElementsByName("OK").isEmpty())
				isPopup = true;

		} catch (Exception e) {

			Assert.fail("Error in getting popup screen");
		}
		return isPopup;
	}

	public WebElement selectOK(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByName("OK");

		} catch (Exception e) {

			Assert.fail("Error in getting OK button");
		}
		return element;
	}

	public boolean homeDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver.findElementsByName("Manual").isEmpty())
				isManual = true;

		} catch (Exception e) {

			Assert.fail("Error in getting Manual tab");
		}
		return isManual;
	}

	
	public WebElement selectClose(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElement(By.name("Close"));

		} catch (Exception e) {

			Assert.fail("Error in getting Close icon");
		}
		return element;
	}

}
