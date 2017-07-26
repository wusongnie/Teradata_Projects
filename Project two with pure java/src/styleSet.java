import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class styleSet {
	public void set(String source,String sample) throws Exception {
		FileInputStream is = new FileInputStream(sample);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFSheet target = wb.getSheetAt(0);
		is.close();
		FileInputStream is2 = new FileInputStream(source);
		XSSFWorkbook wb2 = new XSSFWorkbook(is2);
		XSSFSheet result = wb2.getSheetAt(0);
		
		for (int i = 0; i < target.getRow(1).getLastCellNum(); i++) {
			result.getRow(1).getCell(i).setCellStyle(target.getRow(1).getCell(6).getCellStyle());
		}
		FileOutputStream outFS = new FileOutputStream("/Users/wusongnie/Desktop/临时/222.xlsx");
		wb2.write(outFS);
		outFS.close();
		System.out.println("成功！表的格式已修改完成！");
	}
}
