package com.zimplistic.rotimatic.testcases;

import org.testng.annotations.Test;

import com.zimplistic.rotimatic.dataprovider.ExcelLib;

public class QAConsoleSettings {
	ExcelLib xl = new ExcelLib();
	String testID, recipe, flour;

	@Test
	public void getSettingsData() {

		// Iterate first row data
		int rowNum = 1;
		int cellNum = 1;

		for (int i = rowNum; i <= xl.getXLRowCount("Settings"); i++) {

			for (int j = cellNum; j <= 3; j++) {
				
				testID = xl.getXLcellValue("Settings", i, j);
				
				recipe = xl.getXLcellValue("Settings", i, ++j);
				flour = xl.getXLcellValue("Settings", i, ++j);

				// Call function here
				checkQAConsoleSettings(testID, recipe, flour);
			}
		}

	}

	private void checkQAConsoleSettings(String testID2, String recipe2, String flour2) {
		// TODO Auto-generated method stub
		System.out.println("\n Values are :- " + testID2 + ", " + recipe2 + ", " + flour2);
	}
}
