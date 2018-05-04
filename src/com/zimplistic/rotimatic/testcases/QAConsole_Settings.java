package com.zimplistic.rotimatic.testcases;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageLogin;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageRecipe;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageSettings;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class QAConsole_Settings extends BaseSetup {
	WiniumDriver driver;
	ExcelLib xl = new ExcelLib();
	String testID, recipe, flour;
	String path = xl.getXLcellValue("TestData", 6, 1); // 1.18.7
	String sheetName = "Recipe_Flour_Settings";
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);
	QACPageLogin qacLogin = new QACPageLogin();
	QACPageSettings qacSettings = new QACPageSettings();
	QACPageRecipe qacRecipe = new QACPageRecipe();
	QAConsole qaconsole = new QAConsole();

	WebElement recipeTab, getButton, validateSettingsTab, validateSettingsWindow;

	@Test
	public void QAConsoleSettings() throws Exception {

		driver = setup(path);
		Thread.sleep(5000); // waiting for app to get in focus

		assertTrue(!qacLogin.loginDisplayed(driver), "QAConsole login failed, please try again");

		// Login to QAConsole
		qaconsole.qaConsoleLogin(driver);

		// Check whether qaconsole is opened successfully or not
		if (!qacLogin.homeDisplayed(driver)) {
			getScreenshot(driver, FOLDER_QACONSOLE);
			Assert.fail(" QAConsole login failed, please try again");
		}
		/*
		 * // Connect to serial number qaconsole.qaConsoleConnectClient(driver,
		 * serialNumber);
		 * 
		 * // Recipe tab recipeTab = qacRecipe.selectRecipeTab(driver);
		 * recipeTab.click();
		 * 
		 * // Get button getButton = qacRecipe.selectGetButton(driver);
		 * getButton.click(); Thread.sleep(10000);
		 * 
		 * getScreenshot(driver, FOLDER_QACONSOLE);
		 */
		qacSettings.selectSettings(driver).click();

		validateSettingsTab = qacSettings.selectValidateSettingsTab(driver);
		validateSettingsTab.click();

		checkQAConsoleSettings("JIRA-1", "RECIPE_ROTI", "FLOUR_AASHIRVAAD");

		// getSettingsData();
		// checkQAConsoleSettings("JIRA-1", "RECIPE_ROTI", "FLOUR_AASHIRVAAD");

		System.out.println("Inside main qaconsole");
		qacLogin.selectClose(driver);

		qaconsole.qaConsoleDisconnectClient(driver);

		driver.close();

	}

	public void getSettingsData() throws Exception {
		int rowNum = 1;
		int cellNum = 1;

		for (int i = rowNum; i <= xl.getXLRowCount(sheetName); i++) {

			for (int j = cellNum; j <= 3; j++) {

				testID = xl.getXLcellValue(sheetName, i, j);
				recipe = xl.getXLcellValue(sheetName, i, ++j);
				flour = xl.getXLcellValue(sheetName, i, ++j);

				// Call function here
				checkQAConsoleSettings(testID, recipe, flour);
			}
		}

	}

	private void checkQAConsoleSettings(String testID2, String recipe2, String flour2) throws Exception {

		System.out.println("\n Values are :- " + testID2 + ", " + recipe2 + ", " + flour2);

		//qacSettings.selectRecipeField(driver).sendKeys(Keys.END);
		//qacSettings.selectRecipeField(driver).click();
		//qacSettings.selectRecipeField(driver).sendKeys(Keys.ARROW_DOWN);

		// qacSettings.selectFlourField(driver).sendKeys(Keys.ARROW_DOWN);

		// driver.findElementByXPath("//*[@AutomationID='ListBox']").findElement(By.name(recipe2)).click();

		// driver.findElementByXPath("//*[contains(@ControlType,'ControlType.ListItem') and contains(@Name,"+recipe2+")]").click();
		 
		// driver.findElementByXPath("//*[contains(@ControlType,'ControlType.ListItem') and contains(@Name,"+flour2+")]").click();

		//driver.findElementByXPath("//*[@AutomationId='DropDown']").findElement(By.name(recipe2)).click();
		
		//driver.findElementByXPath("//*[@AutomationId='cbo_FlourSelection']")
		//.findElement(By.xpath("//*[@AutomationId='DropDown']")).findElement(By.name(flour2)).click();
		
		/*driver.findElementByXPath("//*[@AutomationId='cbo_RecipeSelection']")
				.findElement(By.xpath("//*[@AutomationId='DropDown']")).sendKeys(recipe2);

		driver.findElementByXPath("//*[@AutomationId='cbo_FlourSelection']")
				.findElement(By.xpath("//*[@AutomationId='DropDown']")).sendKeys(flour2);
		*/
		 

		validateSettingsWindow = qacSettings.selectValidateSettingsWindow(driver);
		validateSettingsWindow.click();

		System.out.println("Syncing now ..");

		Thread.sleep(40000);
		getScreenshot(driver, testID2 + "_" + FOLDER_QACONSOLE);

		// Check here for matched popup
		if (qacLogin.popupDisplayed(driver)) {
			System.out.println("popup displayed for test case " + testID2);
			qacLogin.selectOK(driver).click();
		}

	}
}
