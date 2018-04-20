package com.zimplistic.rotimatic.testcases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import com.thoughtworks.selenium.Wait;

public class gmailLogin {

	WebDriver driver;
	boolean isWebDriverSetupDone;
	String windowID;
	webDriverResponse wb = new webDriverResponse();

	public webDriverResponse webDriverSetup() throws Exception {
		try {
			System.out.println("1");
			// System.setProperty("webdriver.chrome.driver",
			// "src//dependencies//chromedriver.exe");
			// driver = new ChromeDriver();
			System.setProperty("webdriver.edge.driver", "src//dependencies//MicrosoftWebDriver.exe");
			driver = new EdgeDriver();
			Dimension n = new Dimension(360, 592);
			driver.manage().window().setSize(n);
			System.out.println("2");
			wb.setUpTrue = true;
			wb.windowsId = driver.getWindowHandle();
			System.out.println(wb.windowsId);

		} catch (Exception e) {
			System.out.println("webdriver setup failed");
			isWebDriverSetupDone = false;
			throw e;

		}
		return wb;
	}

	public void isGmailLoggedIn(String id) throws Exception {
		try {
			// driver.manage().window().maximize();
			id = wb.windowsId;
			driver.switchTo().window(id);
			System.out.println("3");
			/*
			 * if(!(wb1.windowsId == null) || !(wb1.windowsId == "")) {
			 */
			System.out.println("4");
			// driver.switchTo().window(wb1.windowsId);
			System.out.println("it came here");
			Thread.sleep(10000);
			ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs2.get(1));

			driver.findElement(By.xpath("//*[@id=\"view_container\"]/form/div[2]/div/div/div/ul/li[2]/div/div[2]/p[1]"))
					.click();
			Thread.sleep(10000);
			driver.findElement(By.id("identifierId")).sendKeys("zimplistic@cuelogic.com");
			driver.findElement(By.id("identifierNext")).click();
			Thread.sleep(5000);
			driver.findElement(By.name("password")).sendKeys("Cuelogic@123");
			driver.findElement(By.id("passwordNext")).click();
			Thread.sleep(7000);
			System.out.println("clicked on zimplistic's login");
			if (driver.getPageSource().contains("Please return to the app.")) {
				System.out.println("gmail login was successful");
				driver.quit();
				// isGmailLogInSuccessful = true;
			} else {
				System.out.println("gmail login was not successful");
				// isGmailLogInSuccessful = false;
			}
			// }

		} catch (Exception e) {
			System.out.println("gmail auth failed");
			throw e;
		}

	}

}
