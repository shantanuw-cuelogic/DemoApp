package com.zimplistic.rotimatic.setup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

import com.zimplistic.rotimatic.configuration.ConfigFile;


public class BaseSetup implements ConfigFile {
	WiniumDriver driver;

	public WiniumDriver setup(String path) throws Exception {
		try {
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath(path);

			String WiniumDriverPath = "src//dependencies//Winium.Desktop.Driver.exe";
			File drivePath = new File(WiniumDriverPath);
			WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(drivePath)
					.usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			driver = new WiniumDriver(service, options);
		} catch (Exception e) {
			System.err.println("\n Driver setup failed");
		}
		return driver;

	}

	// function to capture screenshots
	public void getScreenshot(WiniumDriver driver, String outputlocation) throws IOException {

		String baseFolderName = TEST_FOLDER_NAME;
		File baseFolder = new File(ROOT_DIR, baseFolderName);
		if (!baseFolder.exists() && baseFolder.isDirectory())
			baseFolder.mkdir();

		String filename = new SimpleDateFormat("d_MMM_yyyy_HH_mm_ss'.png'").format(new Date());

		File folderName = new File(baseFolder, outputlocation);
		if (!folderName.exists() && folderName.isDirectory())
			folderName.mkdir();

		// System.out.println("Capturing screenshot");
		File srcFiler = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFiler, new File(folderName, filename));
		// System.out.println("Screenshot captured and saved");

	}
}
