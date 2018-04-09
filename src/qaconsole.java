import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class qaconsole {
	
	WiniumDriver driver;
	
	@BeforeClass
	public WiniumDriver setup() throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("D:\\qaconsole\\iTor11QAConsole.exe");
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



	@Test
	public void newTest() throws IOException {

		try {
			String status="";
			System.out.println("Start of demo");
			Thread.sleep(3000);
			
			driver.findElement(By.name("RMS+")).click();
			Thread.sleep(1000);
			
		/*	status = driver.findElementByXPath("//*[contains(@AutomationId:,'textBoxConnStatus') and contains(@Name,'Status: ')]").getText();
			
			System.out.println("Before connecting status is :-"+status);*/
			
			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Roti Serial: ')]").clear();
			driver.findElementByXPath("//*[contains(@ControlType,'ControlType.Edit') and contains(@Name,'Roti Serial: ')]").sendKeys("7500000F");
			
			// Check button is enabled or not
			driver.findElement(By.name("Connect")).click();  
			Thread.sleep(2000);

			System.out.println("After connecting status is :-"+status);
			/*status = driver.findElementByXPath("//*[contains(@AutomationId:,'textBoxConnStatus') and contains(@Name,'Status: ')]").getText();			
			System.out.println("Status is :- "+status);*/
			
			driver.findElement(By.name("Close")).click();
			
			System.out.println("End of demo");

			//driver.close();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}
}