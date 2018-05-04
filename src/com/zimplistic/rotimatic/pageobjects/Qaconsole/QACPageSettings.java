package com.zimplistic.rotimatic.pageobjects.Qaconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;

public class QACPageSettings {
	public static WebElement element;
	public boolean isSaveEEPROM = false;

	public WebElement selectSettings(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElement(By.name("Settings"));

		} catch (Exception e) {

			Assert.fail("Error in getting Settings tab");
		}
		return element;
	}

	public WebElement selectPower(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByName("POWER");

		} catch (Exception e) {

			Assert.fail("Error in getting Power field");
		}
		return element;
	}

	public WebElement selectSaveEEPROM(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='btn_SavEepToFile']");

		} catch (Exception e) {

			Assert.fail("Error in getting Save EEPROM to File button");
		}
		return element;
	}
	
	public boolean saveEEPROMAlertDisplayed(WiniumDriver driver) throws Exception {
		try {
			if (driver
					.findElementsByXPath(
							"//*[contains(@ControlType,'ControlType.Window') and contains(@Name,'modalForm')]")
					.isEmpty())
				isSaveEEPROM = true;

		} catch (Exception e) {

			Assert.fail("Error in getting Save EEPROM popup");
		}
		return isSaveEEPROM;
	}
	
	public WebElement selectValidateSettingsTab(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='btnSettingsValidate']");

		} catch (Exception e) {

			Assert.fail("Error in getting Validate Settings button");
		}
		return element;
	}
	
	// Select Recipe and Flour window   pageobjects
	
	public WebElement selectValidateSettingsWindow(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='btnValidateSettings']");

		} catch (Exception e) {

			Assert.fail("Error in getting Validate Settings button on window");
		}
		return element;
	}
	
	
	
	public WebElement selectRecipeField(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='cbo_RecipeSelection']");

		} catch (Exception e) {

			Assert.fail("Error in getting Recipe field on window");
		}
		return element;
	}
	
	
	
	public WebElement selectFlourField(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='cbo_FlourSelection']");

		} catch (Exception e) {

			Assert.fail("Error in getting Flour field on window");
		}
		return element;
	}
}
