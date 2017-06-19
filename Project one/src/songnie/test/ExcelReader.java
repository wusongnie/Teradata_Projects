package songnie.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * This is the class that handles the reading process of the excel. @author：
 * 
 * @version
 */
public class ExcelReader {
	private POIFSFileSystem fs;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private HSSFRow row;

	/**
	 * read the title of the rows from the excel
	 * 
	 * @param InputStream
	 * @return String the array that stores the titles
	 * 
	 */
	public String[] readExcelTitle(InputStream is, int sheetNum) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(sheetNum);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			title[i] = getTitleValue(row.getCell((short) i));
		}
		return title;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容，若为字符串的要加单引号
	 */
	public String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		if (cell == null || cell.toString().equals(""))
			return "null";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = "'" + cell.getStringCellValue() + "'";
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());

			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "''";
			break;
		default:
			strCell = "''";
			break;
		}
		if (strCell.equals("''") || strCell == null) {
			return "";
		}

		return strCell;
	}

	public String getTitleValue(HSSFCell cell) {
		String strCell = cell.getStringCellValue();
		return strCell;
	}

}