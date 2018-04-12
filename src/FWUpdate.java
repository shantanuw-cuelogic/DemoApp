import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FWUpdate {
	WiniumDriver driver;
	String successFWUpdate = "1.18.7";
	String serialNumber = "7500000F"; // Use config file
	String status = "";
	String connectedSuccess = "Connected";
	String notConnected = "Rotimatic machine is not connected.";

	@BeforeClass
	public WiniumDriver setup() throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("src//dependencies//1.18.7.4753_fwUpdateTool//ZimpleFirmwareUpdate.exe"); // Step
																													// 5
			String WiniumDriverPath = "src//dependencies//Winium.Desktop.Driver.exe";
			File drivePath = new File(WiniumDriverPath);
			WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(drivePath)
					.usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			driver = new WiniumDriver(service, options);
		} catch (Exception e) {
			System.out.println("\n Driver setup failed");
		}
		return driver;

	}

	@Test(priority = 1)
	public void fwUpdateTest() throws IOException {

		try {

			Thread.sleep(3000);

			// Sports mode
			driver.findElement(By.name("Sports Mode")).click(); // Step 7

			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Broker:')]")
					.clear();
			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Broker:')]")
					.sendKeys(serialNumber); // Step 6

			// Need to Check button is enabled or not
			driver.findElement(By.name("Connect")).click(); // Step 8
			Thread.sleep(3000);

			// Check for Error - update.img file not exist
			if (!driver.findElementsByName("OK").isEmpty()) {
				driver.findElementByName("OK").click();
				System.err.println("\n Error: update.img file not exist");

				driver.findElementByXPath(
						"//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Close')]").click();
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
					Assert.fail("\n Machine is already updated to latest FW version 1.18.7");

				} catch (Exception e) {
				}
			}

			clearLogs();

			// Start FW update Step 9
			fwUpdate();

			// Check FW version after update Step 11
			status = checkCurrentFWversion();

			// Check for semantic fw version > 1.17.7
			// Set flag to decide next test case execution

			if (status.equalsIgnoreCase(successFWUpdate)) {
				System.out.println("\n FW is upgraded successfully");
			} else {
				try {
					System.err.println("\\n Error :- FW is not upgraded successgully");
					disconnectClient();
					Assert.fail("\n Error :- FW is not upgraded successgully");
				} catch (Exception e) {
				}
			}
			disconnectClient();

			// driver.close();
		} catch (Exception e) {
		}
	}

	private void checkErrorDialog() {
		// Check error state
		if (!driver.findElementsByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Quit')]")
				.isEmpty()) {

			System.err.println("Error info :- sys state is not for firmware update");
			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Quit')]")
					.click();
			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Quit')]")
					.click();
			try {
				Assert.fail("Error info :- sys state is not for firmware update");
			} catch (Exception e) {
			}
		} else
			System.out.println("No error in getting current FW version");

	}

	private void fwUpdate() throws Exception {

		driver.findElementByName("Start Update").click();
		checkErrorDialog();

		Thread.sleep(10000);
		status = getStatus();
		System.out.println("\n Current status is => \n" + status);

		checkFWUpdateProgress(); // Step 10

		System.out.println("\n FW update process completed ..");

	}

	// Wait till 1 hour and retry on error
	private void checkFWUpdateProgress() {

		try {
			(new WebDriverWait(driver, 3600)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					try {

						String expected = "firmware write done";

						String actual = getStatus();

						//System.out.println("\n Current FW update status is => \n" + actual);

						clearLogs();
						
						if (!driver.findElementsByXPath(
								"//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Continue')]")
								.isEmpty()) {

							System.err.println("\n info : data pull max tries reached");

							fwUpdateRetry();
						}

						if (actual.contains(expected))
							return true;

						clearLogs();

					} catch (Exception e) {

					}
					return false;
				}

			});
		} catch (Exception e) {
			System.err.println("\n  FW update is not completed in one hour");
			Assert.fail("\n FW update is not completed in one hour");
		}

	}

	private void fwUpdateRetry() throws Exception {
		// click on Continue button
		driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Continue')]")
				.click();
		clearLogs();
		fwUpdate(); // Calling FW Update again

	}

	private String checkCurrentFWversion() throws Exception {

		// Need to Check button is enabled or not
		driver.findElementByName("FW version").click();
		Thread.sleep(4000);
		checkErrorDialog();

		// Get releaseVersion: 1.18.7 and parse it to check current FW

		status = getStatus();
		System.out.println("\n Current status is => \n" + status);

		// Check machine error
		if (status.contains(notConnected)) {
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

	private void disconnectClient() {
		driver.findElementByName("Disconnect").click();
		System.out.println("\n Client disconnected");

		driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Close')]")
				.click();
	}

	private String getStatus() {
		status = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status:')]")
				.getText();
		return status;

	}

	private void clearLogs() {
		WebElement element = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status:')]");
		try {
			Actions action = new Actions(driver).doubleClick(element);
			action.build().perform();
		} catch (Exception e) {
		}

	}
}