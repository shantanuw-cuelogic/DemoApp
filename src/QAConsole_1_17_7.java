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
	String serialNumber = "75000000";
	String status = "";
	String connectedStatus = "Server connected";
	String machineStatus = "";
	String notConnectedStatus = "Not Connected";
	boolean ispowerOff;
	gmailLogin glogin = new gmailLogin();

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
			setup();
		}
		return driver;
	}

	@Test(priority = 0)
	public boolean powerOFF() throws IOException {

		try {
				Thread.sleep(3000);
	
				if (driver.findElementsByName("Log in").isEmpty()) {
					Assert.fail(" QAConsole login failed, please try again");
				}
				// Login to QAConsole
				qaConsoleLogin();
	
				// Check whether qaconsole is opened successfully or not
				if (driver.findElementsByName("Manual").isEmpty()) {
					// Add termination of suit logic here
					ispowerOff = false;
					Assert.fail(" QAConsole login failed, please try again");
				}
				else
				{
					// Connect to serial number
					connectClient();
					driver.findElement(By.name("Settings")).click();
					Thread.sleep(1000);
					// Check machine is power off / On Step 4
					// driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button')
					// and contains(@Name,'POWER')]").click();
					driver.findElementByName("POWER").click();
					disconnectClient();
					ispowerOff = true;
					System.out.println("qaConsole login passed from poweroff");
				}

		} catch (Exception e) {

		}
		System.out.println(ispowerOff);
		return ispowerOff;
	}
	
	


	private void connectClient() throws Exception {
		// Clicking on RMS tab and connecting to mahcine
		// driver.findElementByXPath("//*[contains(@ControlType,'ControlType.TabItem')
		// and contains(@Name,'RMS')]").click();
		driver.findElement(By.name("RMS")).click();

		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]").clear();
		driver.findElementByXPath(
				"//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Rotimatic Serial: ')]")
				.sendKeys(serialNumber);

		// driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button')
		// and contains(@Name,'Connect')]").click();
		driver.findElement(By.name("Connect")).click();
		Thread.sleep(3000);

		// Check status
		status = driver
				.findElementByXPath("//*[contains(@ControlType,'ControlType.Document') and contains(@Name,'Status: ')]")
				.getText();

		if (status.equalsIgnoreCase(connectedStatus))
			System.out.println("Status is :- " + status);

	}

	private void qaConsoleLogin() throws Exception 
	{
		String windowsHandle = driver.getWindowHandle();
		glogin.webDriverSetup();
		driver.findElementByXPath("//*[contains(@AutomationId,'pictureBoxGSignIn')]").click();
		Thread.sleep(5000);
		// Selenium code to login google account
		// Assumption is user is already logged in to google account with full access
		// Return to qaconsole app
		// Alert dialog
		System.out.println(glogin.isWebDriverSetupDone);
		if(glogin.isWebDriverSetupDone)
		{
			System.out.println("Navigated to the QAConsole1.17.7");
			driver.switchTo().window(windowsHandle);
			driver.findElementByXPath("//*[contains(@AutomationId,'pictureBoxGSignIn')]").click();
			glogin.isGmailLoggedIn();
			System.out.println("gmail log in setup done");
		}
		else
		{
			System.out.println("gmail log in setup failed");
		}

		if (!driver.findElementsByName("OK").isEmpty() ) 
		{
			System.out.println("\n User is already logged in");
			driver.findElementByName("OK").click();
			System.out.println("OK button to continue login clicked on");
		}
		else 
		{
			System.out.println("popup to confirm login with OK button did not show");
		}
		Thread.sleep(2000);
	}

	private void disconnectClient() throws Exception {

		// Go to RMS and click on disconnect

		driver.findElement(By.name("RMS")).click();
		Thread.sleep(1000);

		// driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Button')
		// and contains(@Name,'Disconnect')]").click();
		driver.findElement(By.name("Disconnect")).click();

		driver.findElement(By.name("Close")).click();

	}
}