

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * This is the class that handles the reading process of the excel. @author：
 * 
 * @version
 */
public class ExcelReader {
	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	private XSSFRow row;

	/**
	 * read the title of the rows from the excel
	 * 
	 * @param InputStream
	 * @return String the array that stores the titles
	 * 
	 */
	public String[] readExcelTitle(String filePath) {
		try {
			FileInputStream is = new FileInputStream(filePath);
			wb = new XSSFWorkbook(is);	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		}
		System.out.print(wb);
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// number of cells
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			title[i] = getTitleValue(row.getCell((short) i)).replace(" ","_").replace(".", "_");
		}
		return title;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param xssfCell
	 *            Excel单元格
	 * @return String 单元格数据内容，若为字符串的要加单引号
	 */
	public String getStringCellValue(XSSFCell xssfCell) {
		String strCell = "";
		if (xssfCell == null || xssfCell.toString().equals(""))
			return "' '";
		switch (xssfCell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = "'" + xssfCell.getStringCellValue().replaceAll("\n|\r", " ") + "'";
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(xssfCell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(xssfCell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "' '";
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			FormulaEvaluator evaluator = xssfCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateFormulaCell(xssfCell);
			CellValue cellValue = evaluator.evaluate(xssfCell);
			switch (cellValue.getCellType()) {				//判断公式类型
		    case XSSFCell.CELL_TYPE_BOOLEAN:
		    	strCell  = String.valueOf(cellValue.getBooleanValue());
		        break;
		    case XSSFCell.CELL_TYPE_STRING:
		    	strCell = "'" + xssfCell.getStringCellValue().replaceAll("\n|\r", " ") + "'";
		        break;
		    case XSSFCell.CELL_TYPE_BLANK:
		    	strCell = "''";
		        break;
		    case XSSFCell.CELL_TYPE_NUMERIC:
				strCell = String.valueOf(xssfCell.getNumericCellValue());
				break;
		    case XSSFCell.CELL_TYPE_ERROR:
				strCell = "' '";
				break;
			default:
				strCell = "' '";
				break;
		}
			
			break;
		default:
			strCell = "' '";
			break;
		}
		return strCell;
	}

	public String getTitleValue(XSSFCell xssfCell) {
		String strCell = xssfCell.getStringCellValue();
		return strCell;
	}
}