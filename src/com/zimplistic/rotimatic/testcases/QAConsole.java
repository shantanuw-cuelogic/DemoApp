package com.zimplistic.rotimatic.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageLogin;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageRMS;
import com.zimplistic.rotimatic.pageobjects.Qaconsole.QACPageSettings;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class QAConsole extends BaseSetup {

	ExcelLib xl = new ExcelLib();
	gmailLogin glogin = new gmailLogin();
	QACPageLogin qacLogin = new QACPageLogin();
	QACPageRMS qacRMS = new QACPageRMS();
	QACPageSettings qacSettings = new QACPageSettings();

	String status = "";
	String connectedStatus = "Rotimatic connected";
	String machineStatus = "";
	String notConnectedStatus = "Server connected";

	WebElement RMS, serialNoElement, gSignIn, OK, disconnectClient, closeIcon;

	void qaConsoleLogin(WiniumDriver driver) throws Exception {
		String windowsHandle = driver.getWindowHandle();
		glogin.webDriverSetup();
		gSignIn = qacLogin.selectGoogleSignIn(driver);
		gSignIn.click();
		Thread.sleep(5000);

		if (qacLogin.popupDisplayed(driver)) {
			System.out.println("\n User is already logged in");
			OK = qacLogin.selectOK(driver);
			OK.click();
			System.out.println("OK button to continue login clicked on");
		} else if (qacLogin.homeDisplayed(driver)) {
			System.out.println("User is logged in to the QA Console login system");
		} else {
			System.out.println("popup to confirm login with OK button did not show");
			System.out.println(glogin.wb.setUpTrue);

			if (glogin.wb.setUpTrue) {
				System.out.println("Navigated to the QAConsole");
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

	void connectClient(WiniumDriver driver, String serialNumber) throws Exception {

		// Clicking on RMS tab and connecting to machine
		RMS = qacRMS.selectRMS(driver);
		RMS.click();

		serialNoElement = qacRMS.selectSerialNumber(driver);
		serialNoElement.clear();
		serialNoElement.sendKeys(serialNumber);

		driver.findElement(By.name("Connect")).click();
		Thread.sleep(3000);

		// Check status
		status = qacRMS.selectStatus(driver).getText();
		getScreenshot(driver, FOLDER_QACONSOLE);

		// Check machine is connected to Internet or not
		if (status.contains(notConnectedStatus)) {
			getScreenshot(driver, FOLDER_QACONSOLE);
			System.err.println("\n Machine is not connected to internet");
			disconnectClient(driver);
			try {
				Assert.fail("\n Machine is not connected to internet");

			} catch (Exception e) {
			}
		}

		if (status.contains(connectedStatus))
			System.out.println("Status is :- " + status);

	}

	public void disconnectClient(WiniumDriver driver) throws Exception {

		// Go to RMS and click on disconnect
		RMS.click();
		Thread.sleep(1000);

		disconnectClient = qacRMS.selectDisconnectClient(driver);
		disconnectClient.click();

		closeIcon = qacLogin.selectClose(driver);
		closeIcon.click();

	}

}
