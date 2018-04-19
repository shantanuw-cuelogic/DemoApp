import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
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
	dataprovider.ExcelLib xl = new dataprovider.ExcelLib();
	
	WiniumDriver driver;
	String successFWUpdate = xl.getXLcellValue("TestData", 2, 1);
	String currentFWVersion = xl.getXLcellValue("TestData", 3, 1);
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);
	String status = "";
	String connectedSuccess = "Connected";
	String notConnected = "Rotimatic machine is not connected.";
	boolean isFWUpdate;
	QAConsole_1_17_7 qa1_17 = new QAConsole_1_17_7();

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
			System.err.println("\n Driver setup failed");

			setup();
		}
		return driver;

	}

	@Test(priority = 1)
	public boolean fwUpdateTest() throws IOException {
		try {
			System.out.println("inside fw update, value = " + qa1_17.powerOFF());

			assertTrue(qa1_17.powerOFF(), "QAConsole 1.17.7 failed, can not start with FWUpdate");
			
			Thread.sleep(5000); // Wait till machine power off
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
			System.out.println("\n After FW upgrade");
			// Check FW version after update Step 11
			status = checkCurrentFWversion();

			// Check for semantic fw version > 1.17.7
			if (!status.contains(currentFWVersion)) {
				isFWUpdate = true;
				System.out.println("\n FW is upgraded successfully");
			} else {
				try {
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
		}
	}

	private void fwUpdate() throws Exception {

		driver.findElementByName("Start Update").click();
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
						// clearLogs();
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

		driver.findElementByName("Continue").click();
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

		driver.close();
	}

	private String getStatus() {
		status = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status:')]")
				.getText();
		return status;

	}

	private void clearLogs() throws Exception {

		WebElement element = driver.findElementByXPath("//*[contains(@AutomationId,'textBoxMqttLog')]");
		try {
			Actions action = new Actions(driver).doubleClick(element);
			action.build().perform();
		} catch (Exception e) {

		}

	}
}