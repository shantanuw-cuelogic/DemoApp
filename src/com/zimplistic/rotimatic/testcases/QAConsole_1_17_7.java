package com.zimplistic.rotimatic.testcases;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
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

public class QAConsole_1_17_7 extends BaseSetup {
	WiniumDriver driver;
	ExcelLib xl = new ExcelLib();
	gmailLogin glogin = new gmailLogin();
	QACPageLogin qacLogin = new QACPageLogin();
	QACPageSettings qacSettings = new QACPageSettings();
	QAConsole qaconsole = new QAConsole();

	String path = xl.getXLcellValue("TestData", 5, 1);
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);

	public boolean ispowerOff;
	WebElement RMS, serialNoElement, settings, power, gSignIn, OK, disconnectClient, closeIcon;

	@Test(priority = 0)
	public boolean powerOFF() throws IOException {

		try {
			driver = setup(path);
			Thread.sleep(5000); // waiting for app to get in focus
			assertTrue(!qacLogin.loginDisplayed(driver), "QAConsole login failed, please try again");

			// Login to QAConsole
			qaconsole.qaConsoleLogin(driver);

			// Check whether qaconsole is opened successfully or not
			if (!qacLogin.homeDisplayed(driver)) {
				ispowerOff = false;
				getScreenshot(driver, FOLDER_QACONSOLE);
				Assert.fail(" QAConsole login failed, please try again");
			}
			// Connect to serial number
			qaconsole.connectClient(driver, serialNumber);

			settings = qacSettings.selectSettings(driver);
			settings.click();
			Thread.sleep(1000);
			getScreenshot(driver, FOLDER_QACONSOLE);

			// Check machine is power off / On Step 4
			power = qacSettings.selectPower(driver);
			power.click();

			qaconsole.disconnectClient(driver);
			ispowerOff = true;
			System.out.println("qaConsole login passed from poweroff");

		} catch (Exception e) {
		}

		System.out.println("After qa 1.17 " + ispowerOff);
		return ispowerOff;
	}

}