package com.zimplistic.rotimatic.testcases;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.winium.WiniumDriver;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;
import com.zimplistic.rotimatic.pageobjects.Firmware.FWUpdatePage;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class FWUpdate extends BaseSetup {
	ExcelLib xl = new com.zimplistic.rotimatic.dataprovider.ExcelLib();
	QAConsole_1_17_7 qa1_17 = new QAConsole_1_17_7();
	FWUpdatePage fwUpdate = new FWUpdatePage();


	WiniumDriver driver;
	String path = xl.getXLcellValue("TestData", 4, 1);
	String successFWUpdate = xl.getXLcellValue("TestData", 2, 1);
	String currentFWVersion = xl.getXLcellValue("TestData", 3, 1);
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);
	String status = "";
	String connectedSuccess = "Connected";
	String notConnected = "Rotimatic machine is not connected.";
	boolean isFWUpdate;

	WebElement sportsMode, serialNoElement, connect, OK, close, FWVersionElement, quit, log, startFWUpdate,
			continueElement, disconnect;

	@Test(priority = 1)
	public boolean fwUpdateTest() throws IOException {
		try {
			driver = setup(path);
			//System.out.println("inside fw update, value = " +qa1_17.powerOFF());

			assertTrue(qa1_17.powerOFF(), "QAConsole 1.17.7 failed, can not start with FWUpdate test");

			Thread.sleep(3000);
			// Check for app focus

			// Sports mode // Step 7
			sportsMode = fwUpdate.getSportsMode(driver);
			sportsMode.click();

			serialNoElement = fwUpdate.selectSerialNumber(driver);
			serialNoElement.clear();
			serialNoElement.sendKeys(serialNumber); // Step 6

			// Need to Check button is enabled or not
			connect = fwUpdate.getConnect(driver);
			connect.click(); // Step 8
			Thread.sleep(3000);
			getScreenshot(driver, FOLDER_FWUPDATETOOL);

			// Check for Error - update.img file not exist
			if (fwUpdate.popupDisplayed(driver)) {

				getScreenshot(driver, FOLDER_FWUPDATETOOL);

				OK = fwUpdate.selectOK(driver);
				OK.click();

				System.err.println("\n Error: update.img file not exist");

				close = fwUpdate.selectClose(driver);
				close.click();
				try {
					Assert.fail("\n Error :- update.img file not exist");
				} catch (Exception e) {
				}
			}

			status = getStatus();
			System.out.println("\n Current status is => \n" + status);

			if (status.contains(connectedSuccess))
				System.out.println("\n Client is connected"); // Step 8
			else {
				System.err.println("\n Error occured:- " + status);
				try {
					getScreenshot(driver, FOLDER_FWUPDATETOOL);
					Assert.fail("\n Error occured:- Client not connected");

				} catch (Exception e) {
				}
			}
			// Check current FW version of machine
			status = checkCurrentFWversion();

			if (status.contains(successFWUpdate)) {

				System.err.println("\n Machine is already updated to latest FW version 1.18.7");
				disconnectClient();
				try {
					getScreenshot(driver, FOLDER_FWUPDATETOOL);
					Assert.fail("\n Machine is already updated to latest FW version 1.18.7");

				} catch (Exception e) {
				}
			}

			clearLogs();

			// Start FW update Step 9
			fwUpdate();
			System.out.println("\n After FW upgrade");
			// Check FW version after update Step 11
			status = checkCurrentFWversion();

			// Check for semantic fw version > 1.17.7
			if (!status.contains(currentFWVersion)) {
				isFWUpdate = true;
				System.out.println("\n FW is upgraded successfully");
				getScreenshot(driver, FOLDER_FWUPDATETOOL);
			} else {
				try {
					getScreenshot(driver, FOLDER_FWUPDATETOOL);
					System.err.println("\n Error :- FW is not upgraded successfully");
					isFWUpdate = false;
					disconnectClient();
					Assert.fail("\n Error :- FW is not upgraded !!");
				} catch (Exception e) {
				}
			}
			disconnectClient();

		} catch (Exception e) {
		}
		return isFWUpdate;
	}

	private void checkErrorDialog() throws Exception {
		// Check error state
		if (fwUpdate.errorDisplayed(driver)) {
			getScreenshot(driver, FOLDER_FWUPDATETOOL);
			System.err.println("Error info :- sys state is not for firmware update");

			quit = fwUpdate.selectQuit(driver);
			quit.click(); // 1st popup
			quit = fwUpdate.selectQuit(driver);
			quit.click(); // 2nd popup
			try {
				Assert.fail("Error info :- sys state is not for firmware update");
			} catch (Exception e) {
			}
		}
	}

	private void fwUpdate() throws Exception {
		startFWUpdate = fwUpdate.selectStartFWUpdate(driver);
		startFWUpdate.click();
		checkErrorDialog();
		checkFWUpdateProgress(); // Step 10

	}

	// Wait till 2 hours and retry continuously on error
	private void checkFWUpdateProgress() {

		try {
			(new WebDriverWait(driver, 7200)).until(new ExpectedCondition<Boolean>() {
				Boolean flag = false;

				public Boolean apply(WebDriver d) {
					try {

						String expected = "firmware write done";

						String actual = getStatus();
						clearLogs();
						System.out.println("\n Current FW update status is => \n" + actual);

						if (!driver.findElementsByName("Continue").isEmpty()) {

							System.err.println("\n info : data pull max tries reached , trying again");

							fwUpdateRetry();
						}

						if (actual.contains(expected)) {
							System.out.println("\n FW update process completed ..");
							flag = true;

						}

					} catch (Exception e) {

					}

					return flag;

				}

			});
		} catch (Exception e) {
			System.err.println("\n  FW update is not completed in two hours");
			Assert.fail("\n FW update is not completed in two hours");
		}

	}

	private void fwUpdateRetry() throws Exception {
		// click on Continue button
		getScreenshot(driver, FOLDER_FWUPDATETOOL);
		continueElement = fwUpdate.selectContinue(driver);
		continueElement.click();
		clearLogs();
		fwUpdate(); // Calling FW Update again

	}

	private String checkCurrentFWversion() throws Exception {

		// Need to Check button is enabled or not
		FWVersionElement = fwUpdate.selectFWVersion(driver);
		FWVersionElement.click();
		Thread.sleep(4000);
		checkErrorDialog();

		// Get releaseVersion: 1.18.7 and parse it to check current FW
		status = getStatus();

		// Check machine error
		if (status.contains(notConnected)) {
			getScreenshot(driver, FOLDER_FWUPDATETOOL);
			System.err.println("\n Error :- Rotimatic machine is not connected");
			disconnectClient();
			try {
				Assert.fail("\n Error :- Rotimatic machine is not connected");
			} catch (Exception e) {
			}
		}

		int index = status.lastIndexOf("releaseVersion:");

		String fwVersion = status.substring(index + 15);

		System.out.println("\n Current FW version = " + fwVersion);

		return fwVersion;

	}

	private void disconnectClient() throws Exception {

		disconnect = fwUpdate.selectDisconnect(driver);
		disconnect.click();
		System.out.println("\n Client disconnected");

		close = fwUpdate.selectClose(driver);
		close.click();

		driver.close();
	}

	private String getStatus() throws Exception {
		status = fwUpdate.selectStatus(driver).getText();
		return status;

	}

	private void clearLogs() throws Exception {

		log = fwUpdate.getClearLogElement(driver);
		try {
			Actions action = new Actions(driver).doubleClick(log);
			action.build().perform();
		} catch (Exception e) {

		}

	}
}
