import static org.testng.Assert.assertTrue;

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

public class QAConsole_1_18_7 {
	dataprovider.ExcelLib xl = new dataprovider.ExcelLib();
	gmailLogin glogin = new gmailLogin();

	WiniumDriver driver;
	String serialNumber = xl.getXLcellValue("TestData", 1, 1);
	String status = "";
	String connectedStatus = "Rotimatic connected";
	String machineStatus = "";
	String notConnectedStatus = "Server connected";
	FWUpdate fw = new FWUpdate();

	@BeforeClass
	public WiniumDriver setup() throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("src//dependencies//1.18.7.4753_qaConsole//iTor11QAConsole.exe");
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

	@Test(priority = 2)
	public void saveRotiFile() throws Exception {

		Thread.sleep(5000);
		System.out.println("inside QAConsole 1.18.7, value = " + fw.isFWUpdate);
		 assertTrue(fw.isFWUpdate, "FW Update failed before login to QAConsole1.18.7");
		assertTrue(!driver.findElementsByName("Log in").isEmpty(), "QAConsole login failed, please try again");
		// Login to QAConsole
		qaConsoleLogin();

		// Check whether qaconsole is opened successfully or not
		assertTrue(!driver.findElementsByName("Manual").isEmpty(), "QAConsole login failed, please try again");

		connectClient();

		driver.findElement(By.name("Settings")).click();
		Thread.sleep(1000);

		// Power On machine / On Step 12

		driver.findElementByName("POWER").click();

		// Save rotifile Step 13

		driver.findElementByName("Save EEPROM to File").click();
		// Checking wait dialog
		if (!driver
				.findElementsByXPath("//*[contains(@ControlType,'ControlType.Window') and contains(@Name,'modalForm')]")
				.isEmpty()) {
			System.out.println("\n Saving EEPROM file");

		}

		// Checking success/error dialog

		if (!driver.findElementsByName("OK").isEmpty()) {
			System.err.println("\n EEPROM transaction fail! (Timeout after 30s)");
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
		// Clicking on RMS tab and connecting to mahcine
		driver.findElementByXPath("//*[contains(@ControlType,'ControlType.TabItem') and contains(@Name,'RMS')]")
				.click();

		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]").clear();
		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]")
				.sendKeys(serialNumber);

		driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button') and contains(@Name,'Connect')]")
				.click();
		Thread.sleep(4000);

		// Check status
		status = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status: ')]")
				.getText();


		// Check machine is connected to Internet or not

		if (status.contains(notConnectedStatus)) {
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

		// driver.switchTo().window(windowsHandle);

		if (!driver.findElementsByName("OK").isEmpty()) {
			System.out.println("\n User is already logged in");
			driver.findElementByName("OK").click();
			System.out.println("OK button to continue login clicked on");
		} else {
			System.out.println("popup to confirm login with OK button did not show");
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