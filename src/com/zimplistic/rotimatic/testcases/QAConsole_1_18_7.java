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
import com.zimplistic.rotimatic.pageobjects.QAConsolePage;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class QAConsole_1_18_7 extends BaseSetup {
	ExcelLib xl = new ExcelLib();
	gmailLogin glogin = new gmailLogin();
	FWUpdate fw = new FWUpdate();
	QAConsolePage qaconsole = new QAConsolePage();

	WiniumDriver driver;
	String path = xl.getXLcellValue("TestData", 6, 1);
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);
	String status = "";
	String connectedStatus = "Rotimatic connected";
	String machineStatus = "";
	String notConnectedStatus = "Server connected";

	WebElement RMS, serialNoElement, settings, power, gSignIn, OK, disconnectClient, closeIcon, saveEEPROM;

	@Test(priority = 2)
	public void saveRotiFile() throws Exception {

		driver = setup(path);

		Thread.sleep(5000);
		System.out.println("inside QAConsole 1.18.7, value = " + fw.isFWUpdate);
		assertTrue(fw.isFWUpdate, "FW Update failed before login to QAConsole1.18.7");
		assertTrue(!qaconsole.loginDisplayed(driver), "QAConsole login failed, please try again");

		// Login to QAConsole
		qaConsoleLogin();

		// Check whether qaconsole is opened successfully or not
		if (qaconsole.homeDisplayed(driver)) {
			getScreenshot(driver, FOLDER_QACONSOLE);
			Assert.fail(" QAConsole login failed, please try again");
		}

		connectClient();

		settings = qaconsole.selectSettings(driver);
		settings.click();
		Thread.sleep(1000);
		getScreenshot(driver, FOLDER_QACONSOLE);

		// Power On machine / On Step 12
		power = qaconsole.selectPower(driver);
		power.click();
		
		// Save rotifile Step 13
		saveEEPROM = qaconsole.selectSaveEEPROM(driver);
		saveEEPROM.click();
		
		// Checking wait dialog
		if (!qaconsole.saveEEPROMAlertDisplayed(driver)) {
			System.out.println("\n Saving EEPROM file");
			getScreenshot(driver, FOLDER_QACONSOLE);
		}else
			System.out.println("\n Saving EEPROm file process not started");

		// Checking success/error dialog
		if (!qaconsole.popupDisplayed(driver)) {
			System.err.println("\n EEPROM transaction fail! (Timeout after 30s)");
			getScreenshot(driver, FOLDER_QACONSOLE);
			driver.findElementByName("OK").click();
			try {
				disconnectClient();
				Assert.fail(" EEPROM transaction fail! (Timeout after 30s)");
			} catch (Exception e) {
			}
		}

		// need to check EEPROM success condition

		// Check rotifile saved location here

		disconnectClient();

		driver.close();

	}

	private void connectClient() throws Exception {
		// Clicking on RMS tab and connecting to machine
		RMS = qaconsole.selectRMS(driver);
		RMS.click();

		serialNoElement = qaconsole.selectSerialNumber(driver);
		serialNoElement.clear();
		serialNoElement.sendKeys(serialNumber);

		driver.findElement(By.name("Connect")).click();
		Thread.sleep(3000);

		// Check status
		status = qaconsole.selectStatus(driver).getText();
		getScreenshot(driver, FOLDER_QACONSOLE);

		// Check machine is connected to Internet or not
		if (status.contains(notConnectedStatus)) {
			getScreenshot(driver, FOLDER_QACONSOLE);
			System.err.println("\n Machine is not connected to internet");
			disconnectClient();
			try {
				Assert.fail("\n Machine is not connected to internet");

			} catch (Exception e) {
			}
		}

		if (status.contains(connectedStatus))
			System.out.println("Status is :- " + status);
	}

	private void qaConsoleLogin() throws Exception {
		String windowsHandle = driver.getWindowHandle();
		glogin.webDriverSetup();

		gSignIn = qaconsole.selectGoogleSignIn(driver);
		gSignIn.click();
		Thread.sleep(5000);

		System.out.println(glogin.wb.setUpTrue);
		if (glogin.wb.setUpTrue) {
			System.out.println("Navigated to the QAConsole1.17.7");
			Thread.sleep(5000);
			System.out.println("it came here");
			System.out.println(glogin.wb.windowsId);
			glogin.isGmailLoggedIn(glogin.wb.windowsId);
			System.out.println("gmail log in setup done");
		} else {
			System.out.println("gmail log in setup failed"); // It can be case where user is already logged in to gmail.
		}

		if (!qaconsole.popupDisplayed(driver)) {
			System.out.println("\n User is already logged in");

			OK = qaconsole.selectOK(driver);
			OK.click();

			System.out.println("OK button to continue login clicked on");
		} else {
			System.out.println("popup to confirm login with OK button did not show");
		}
		Thread.sleep(2000);
	}

	private void disconnectClient() throws Exception {

		RMS.click();
		Thread.sleep(1000);

		disconnectClient = qaconsole.selectDisconnectClient(driver);
		disconnectClient.click();

		closeIcon = qaconsole.selectClose(driver);
		closeIcon.click();
	}
}