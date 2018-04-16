
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
	
	public boolean webDriverSetup() throws Exception
	{
		try
		{
			System.out.println("1");
			System.setProperty("webdriver.chrome.driver", "src//dependencies//chromedriver.exe");
			driver = new ChromeDriver();
			Dimension n = new Dimension(360,592); 
			 driver.manage().window().setSize(n);
			System.out.println("2");
			isWebDriverSetupDone = true;
		}
		catch(Exception e)
		{
			System.out.println("webdriver setup failed");
			isWebDriverSetupDone = false;
			throw e;
			
		}
		return isWebDriverSetupDone;
	}
	
	public void isGmailLoggedIn() throws Exception
	{
		try
		{
			driver.manage().window().maximize();
			String windowID = driver.getWindowHandle();	
			System.out.println("3");
			Set<String> windowIDs = driver.getWindowHandles();
			String[] arrayWindowIds = windowIDs.toArray(new String[0]);
			System.out.println(Arrays.toString(arrayWindowIds));
			if(!(windowID == null) || !(windowID == ""))
			{
				System.out.println("4");
				System.out.println("it came here");
				Thread.sleep(10000);
				driver.findElement(By.xpath("//*[contains(@role,'presentation')")).click();
				Thread.sleep(7000);
				System.out.println("clicked on zimplistic's login");
				if(driver.getPageSource().contains("Please return to the app."))
				{
					System.out.println("gmail login was successful");
//					isGmailLogInSuccessful = true;
				}
				else
				{
					System.out.println("gmail login was not successful");
//					isGmailLogInSuccessful = false;
				}
			}
		
		}
		catch(Exception e)
		{
			System.out.println("gmail auth failed");
			throw e;
		}
		
	}

}
