package com.zimplistic.rotimatic.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;


public class QAConsolePage{

	public static WebElement element;


	public WebElement selectRMS(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElement(By.name("RMS"));
			
		} catch (Exception e) {
		
			Assert.fail("Error in getting Settings tab");
		}
		return element;
	}
	
}
