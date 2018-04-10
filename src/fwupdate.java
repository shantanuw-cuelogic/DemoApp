import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class fwupdate {
	WiniumDriver driver;

	@BeforeClass
	public WiniumDriver setup() throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("src//dependencies//fwUpdateTool//ZimpleFirmwareUpdate.exe"); // Use config file
			String WiniumDriverPath = "src//dependencies//Winium.Desktop.Driver.exe";
			File drivePath = new File(WiniumDriverPath);
			WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(drivePath)
					.usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			driver = new WiniumDriver(service, options);
		} catch (Exception e) {
			System.out.println("Driver setup failed");
		}
		return driver;
	}

	@Test
	public void newTest() throws IOException {

		try {
			String serialNumber = "7500000F"; // Use config file
			String status = "";
			String successFWUpdate = "1.18.7";
			String connectedSuccess = "Connected";
			String notConnected = "Rotimatic machine is not connected.";

			System.out.println("Start of test");
			Thread.sleep(3000);

			// Sports mode **Need to replace it
			driver.findElement(By.name("Enable  Fast")).click();

			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Broker:')]")
					.clear();
			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Broker:')]")
					.sendKeys(serialNumber);

			// Need to Check button is enabled or not
			driver.findElement(By.name("Connect")).click();
			Thread.sleep(3000);
			status = getStatus();
			System.out.println("Status is :-" + status);

			if (status.contains(connectedSuccess))
				System.out.println("Client is connected");
			else
				System.err.println("\n Error occured:- " + status);

			// Need to Check button is enabled or not
			driver.findElementByName("FW version").click();
			Thread.sleep(3000);

			checkErrorDialog();

			// Check current FW version of machine
			status = checkCurrentFWversion();

			if (status.contains(successFWUpdate)) {

				System.err.println("Machine is already updated to latest FW version 1.18.7");
				disconnectClient();
				try {
					Assert.fail("Machine is already updated to latest FW version 1.18.7");

				} catch (Exception e) {
				}
			}

			// Check machine error
			if (status.contains(notConnected)) {
				System.err.println("Error :- Rotimatic machine is not connected");
				disconnectClient();
				try {
					Assert.fail("Error :- Rotimatic machine is not connected");
				} catch (Exception e) {
				}
			}

			clearLogs();

			// Start FW update
			fwUpdate();

			disconnectClient();

			System.out.println("End of test");

			driver.close();
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
		// Need to Check button is enabled or not
		driver.findElementByName("FW version").click();
		Thread.sleep(3000);

		checkCurrentFWversion();
		driver.findElementByName("Start Update").click();
		checkErrorDialog();

	}

	private String checkCurrentFWversion() {
		// Get releaseVersion: 1.18.7 and parse it to check current FW

		String status = getStatus();
		System.out.println("Original status = " + status);
		
		int index = status.lastIndexOf("releaseVersion:");

		String fwVersion = status.substring(index+15);
		
		System.out.println("Current FW version = " + fwVersion);
		
		return fwVersion;
		
	}

	private void disconnectClient() {
		driver.findElementByName("Disconnect").click();
		System.out.println("Client disconnected");

		driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Close')]")
				.click();
	}

	private String getStatus() {
		String status = driver
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

			// System.out.println("Double clicked the element");
		} catch (Exception e) {
		}

	}
}