package com.zimplistic.rotimatic.testcases;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.annotations.Test;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;
import com.zimplistic.rotimatic.setup.BaseSetup;

public class CopyFWImage extends BaseSetup {

	WiniumDriver driver;
	ExcelLib xl = new ExcelLib();
	String source =  xl.getXLcellValue("TestData", 8, 1); 
	String dest =  xl.getXLcellValue("TestData", 9, 1); 

	@Test
	public void copyFW() throws Exception {

		File s = new File(source);
		File d = new File(dest);

		try {
			FileUtils.copyFileToDirectory(s, d);
			System.out.println("File copied");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
