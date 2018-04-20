package com.zimplistic.rotimatic.testcases;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class QAConsole_1_17_7 extends BaseSetup {

	ExcelLib xl = new com.zimplistic.rotimatic.dataprovider.ExcelLib();
	gmailLogin glogin = new gmailLogin();

	WiniumDriver driver;
	String path = xl.getXLcellValue("TestData", 5, 1);
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);
	String status = "";
	String connectedStatus = "Rotimatic connected";
	String machineStatus = "";
	String notConnectedStatus = "Server connected";
	public boolean ispowerOff;

	@Test(priority = 0)

	public boolean powerOFF() throws IOException {

		try {
			driver = setup(path);
			Thread.sleep(3000);
			assertTrue(!driver.findElementsByName("Log in").isEmpty(), "QAConsole login failed, please try again");
			// Login to QAConsole
			qaConsoleLogin();

			// Check whether qaconsole is opened successfully or not
			if (driver.findElementsByName("Manual").isEmpty()) {
				ispowerOff = false;
				getScreenshot(driver, FOLDER_QACONSOLE);
				Assert.fail(" QAConsole login failed, please try again");
			} else {
				// Connect to serial number
				connectClient();
				driver.findElement(By.name("Settings")).click();
				Thread.sleep(1000);
				getScreenshot(driver, FOLDER_QACONSOLE);
				// Check machine is power off / On Step 4
				driver.findElementByName("POWER").click();
				disconnectClient();
				ispowerOff = true;
				System.out.println("qaConsole login passed from poweroff");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("After qa 1.17 " + ispowerOff);
		return ispowerOff;
	}

	private void connectClient() throws Exception {
		// Clicking on RMS tab and connecting to mahcine
		driver.findElement(By.name("RMS")).click();

		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]").clear();
		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]")
				.sendKeys(serialNumber);

		driver.findElement(By.name("Connect")).click();
		Thread.sleep(3000);

		// Check status
		status = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status: ')]")
				.getText();

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
		driver.findElementByXPath("//*[contains(@AutomationId,'pictureBoxGSignIn')]").click();
		Thread.sleep(5000);

		// driver.switchTo().window(windowsHandle);

		if (!driver.findElementsByName("OK").isEmpty()) {
			System.out.println("\n User is already logged in");
			driver.findElementByName("OK").click();
			System.out.println("OK button to continue login clicked on");
		} else if (!driver.findElementsByName("Manual").isEmpty()) {
			System.out.println("User is logged in to the QA Console login system");
		} else {
			System.out.println("popup to confirm login with OK button did not show");

			System.out.println(glogin.wb.setUpTrue);
			if (glogin.wb.setUpTrue) {
				System.out.println("Navigated to the QAConsole1.17.7");
				Thread.sleep(5000);
				System.out.println("it came here");
				System.out.println(glogin.wb.windowsId);
				glogin.isGmailLoggedIn(glogin.wb.windowsId);
				System.out.println("gmail log in setup done");
			} else {
				System.out.println("gmail log in setup failed"); // It can be case where user is already logged in to
																	// gmail.
			}
		}
		Thread.sleep(2000);
	}

	private void disconnectClient() throws Exception {

		// Go to RMS and click on disconnect
		driver.findElement(By.name("RMS")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("Disconnect")).click();

		driver.findElement(By.name("Close")).click();

	}
}