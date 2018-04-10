
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class setup {

	WiniumDriver driver;

	@BeforeClass
	public WiniumDriver setup() throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("C:\\WINDOWS\\system32\\notepad.exe");
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
	public void test() throws IOException {

		try {
			System.out.println("Start of demo");
			Thread.sleep(2000);
			driver.findElement(By.name("Maximize")).click();
			Thread.sleep(2000);
			driver.findElementByClassName("Edit").sendKeys("This is sample test");
			Thread.sleep(2000);
			driver.findElement(By.name("Help")).click();
			driver.findElement(By.name("About Notepad")).click();
			driver.findElement(By.name("OK")).click();
			
			driver.findElementByName("Close").click();
			driver.findElementByName("Don't Save").click();
			System.out.println("End of demo");

			driver.close();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}
}