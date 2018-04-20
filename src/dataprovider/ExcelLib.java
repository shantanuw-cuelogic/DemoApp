package dataprovider;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelLib {

	// Excel File which having all data
	String Excelfile = "src//utilities//TestScripts.xls";

	public String getXLcellValue(String sheetName, int rowNum, int cellNum) {
		try {
			FileInputStream fis = new FileInputStream(Excelfile);
			Workbook wb = WorkbookFactory.create(fis);

			return wb.getSheet(sheetName).getRow(rowNum).getCell(cellNum).getStringCellValue();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return "";
	}

	// get xl row count

	public int getXLRowCount(String sheetName) {
		try {
			FileInputStream fis = new FileInputStream(Excelfile);

			Workbook wb = WorkbookFactory.create(fis);

			return wb.getSheet(sheetName).getLastRowNum();
		} catch (Exception ex) {

		}
		return -1;
	}

	// set the value of the cell present in specific sheet

	public void setXLCellValue(String sheetName, int rowNum, int cellNum, String input) {
		try {
			FileInputStream fis = new FileInputStream(Excelfile);

			Workbook wb = WorkbookFactory.create(fis);

			wb.getSheet(sheetName).getRow(rowNum).createCell(cellNum).setCellValue(input);

			FileOutputStream fos = new FileOutputStream(Excelfile);

			wb.write(fos);

			fos.close();

		} catch (Exception ex) {

		}

	}

	public int getXLcellValueInt(String sheetName, int rowNum, int cellNum) {
		try {
			FileInputStream fis = new FileInputStream(Excelfile);
			Workbook wb = WorkbookFactory.create(fis);

			return (int) wb.getSheet(sheetName).getRow(rowNum).getCell(cellNum).getNumericCellValue();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return 0;
	}

}