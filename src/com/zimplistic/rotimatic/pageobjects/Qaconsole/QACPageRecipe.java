package com.zimplistic.rotimatic.pageobjects.Qaconsole;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;

public class QACPageRecipe {
	public static WebElement element;
	
	public WebElement selectRecipeTab(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByName("Recipe");

		} catch (Exception e) {

			Assert.fail("Error in getting Recipe tab");
		}
		return element;
	}
	
	public WebElement selectGetButton(WiniumDriver driver) throws Exception {
		try {
			element = driver.findElementByXPath("//*[@AutomationId='btnRemoteRecipesGet']");

		} catch (Exception e) {

			Assert.fail("Error in finding Get button");
		}
		return element;
	}
}
