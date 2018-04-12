import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class QAConsole_1_17_7 {

	WiniumDriver driver;
	String serialNumber = "7500000F";
	String status = "";
	String connectedStatus = "Server connected";
	String machineStatus = "";
	String notConnectedStatus = "Not Connected";

	@BeforeClass
	public WiniumDriver setup() throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("src//dependencies//1.17.7.4723_qaConsole//iTor11QAConsole.exe");
			String WiniumDriverPath = "src//dependencies//Winium.Desktop.Driver.exe";
			File drivePath = new File(WiniumDriverPath);
			WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(drivePath)
					.usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			driver = new WiniumDriver(service, options);
		} catch (Exception e) {
			System.out.println("Driver setup failed");
			// throw e;
		}
		return driver;
	}

	@Test(priority = 0)
	public void powerOFF() throws IOException {

		try {
			Thread.sleep(3000);
			// Login to QAConsole
			qaConsoleLogin();

			// Connect to serial number
			connectClient();

			driver.findElement(By.name("Settings")).click();
			Thread.sleep(1000);

			// Check machine is power off / On Step 4
			//driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'POWER')]").click();
			
			driver.findElementByName("POWER").click();
			
			disconnectClient();

		} catch (Exception e) {

		}
	}

	private void connectClient() throws Exception {
		// Clicking on RMS tab and connecting to mahcine
	//	driver.findElementByXPath("//*[contains(@ControlType,'ControlType.TabItem') and contains(@Name,'RMS')]").click();
		driver.findElement(By.name("RMS")).click();

		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]").clear();
		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]")
				.sendKeys(serialNumber);

		//driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Connect')]").click();
		driver.findElement(By.name("Connect")).click();
		Thread.sleep(3000);

		// Check status
		status = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status: ')]")
				.getText();

		if (status.equalsIgnoreCase(connectedStatus))
			System.out.println("Status is :- " + status);

	}

	private void qaConsoleLogin() throws Exception {

		driver.findElementByXPath("//*[contains(@AutomationId,'pictureBoxGSignIn')]").click();
		Thread.sleep(5000);

		// Selenium code to login google account

		// Assumption is user is already logged in to google account with full access

		// Return to qaconsole app

		// Alert dialog
		if (!driver.findElementsByName("OK").isEmpty()) {
			System.out.println("\n User is already logged in");
			driver.findElementByName("OK").click();
		}
		Thread.sleep(2000);

	}

	private void disconnectClient() throws Exception {

		// Go to RMS and click on disconnect

		driver.findElement(By.name("RMS")).click();
		Thread.sleep(1000);

		//driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Disconnect')]").click();
		driver.findElement(By.name("Disconnect")).click();
		
		driver.findElement(By.name("Close")).click();

	}
}