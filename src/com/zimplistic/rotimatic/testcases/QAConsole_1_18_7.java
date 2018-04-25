package com.zimplistic.rotimatic.testcases;

import static org.testng.Assert.assertTrue;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageLogin;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageRMS;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageSettings;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class QAConsole_1_18_7 extends BaseSetup {
	ExcelLib xl = new ExcelLib();
	gmailLogin glogin = new gmailLogin();
	FWUpdate fw = new FWUpdate();
	QACPageLogin qacLogin = new QACPageLogin();
	QACPageRMS qacRMS = new QACPageRMS();
	QACPageSettings qacSettings = new QACPageSettings();
	QAConsole qaconsole = new QAConsole();

	WiniumDriver driver;
	String path = xl.getXLcellValue("TestData", 6, 1);
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);


	WebElement settings, power, gSignIn, OK, disconnectClient, saveEEPROM;

	@Test(priority = 2)
	public void saveRotiFile() throws Exception {

		driver = setup(path);

		Thread.sleep(5000);
		System.out.println("inside QAConsole 1.18.7, value = " + fw.isFWUpdate);
		assertTrue(fw.isFWUpdate, "FW Update failed before login to QAConsole1.18.7");
		assertTrue(!qacLogin.loginDisplayed(driver), "QAConsole login failed, please try again");

		// Login to QAConsole
		qaconsole.qaConsoleLogin(driver);

		// Check whether qaconsole is opened successfully or not
		if (!qacLogin.homeDisplayed(driver)) {
			getScreenshot(driver, FOLDER_QACONSOLE);
			Assert.fail(" QAConsole login failed, please try again");
		}

		qaconsole.connectClient(driver, serialNumber);

		settings = qacSettings.selectSettings(driver);
		settings.click();
		Thread.sleep(1000);
		getScreenshot(driver, FOLDER_QACONSOLE);

		// Power On machine / On Step 12
		power = qacSettings.selectPower(driver);
		power.click();

		// Save rotifile Step 13
		saveEEPROM = qacSettings.selectSaveEEPROM(driver);
		saveEEPROM.click();

		// Checking wait dialog
		if (!qacSettings.saveEEPROMAlertDisplayed(driver)) {
			System.out.println("\n Saving EEPROM file");
			getScreenshot(driver, FOLDER_QACONSOLE);
			Thread.sleep(20000);
		} else
			System.out.println("\n Saving EEPROm file process not started");

		// Checking success/error dialog
		if (qacLogin.popupDisplayed(driver)) {
			System.err.println("\n EEPROM transaction fail! (Timeout after 30s)");
			getScreenshot(driver, FOLDER_QACONSOLE);
			qacLogin.selectOK(driver).click();
			try {
				qaconsole.disconnectClient(driver);
				Assert.fail(" EEPROM transaction fail! (Timeout after 30s)");
			} catch (Exception e) {
			}
		}

		// need to check EEPROM success condition

		// Check rotifile saved location here

		qaconsole.disconnectClient(driver);

		driver.close();

	}

}