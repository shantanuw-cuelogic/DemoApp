package configuration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.winium.WiniumDriver;

public interface ConfigFile {

	String ROOT_DIR = "src//screenshots";
	String TEST_FOLDER_NAME = new SimpleDateFormat("d_MMM_yyyy_HH_mm").format(new Date());

	String FOLDER_FWUPDATETOOL = "FWUpdateTool";
	String FOLDER_QACONSOLE = "QAConsole";

	// Excel File which having all data
	String Excelfile = "src//utilities//TestScripts.xls";
}
