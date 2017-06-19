package songnie.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DataInsert {

	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/usermaster?characterEncoding=utf-8";
	public static Connection conn;
	public static String uploadPath = "/Users/wusongnie/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/TomcatTest//upload/";

	public static void main(String[] args) throws Exception {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "root");
			FileInputStream fileInput = new FileInputStream(uploadPath + "book1.xls");
			POIFSFileSystem fs = new POIFSFileSystem(fileInput);
			HSSFWorkbook workbook = new HSSFWorkbook(fs);

			// load the sheets
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				insertData(workbook.getSheetName(i), i);
				System.out.println("表" + workbook.getSheetName(i) + "已进入数据库！");
			}
			System.out.println("成功！所有表均已进入数据库！");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "resource", "deprecation" })
	public static void insertData(String tbName, int sheetNum) {
		try {

			// read data form the excel
			InputStream is = new FileInputStream(uploadPath + "book1.xls");
			ExcelReader excelReader = new ExcelReader();
			String[] colName = excelReader.readExcelTitle(is, sheetNum);
			String createTable = "CREATE TABLE IF NOT EXISTS " + tbName + "(";
			for (int i = 0; i < colName.length; i++) {
				createTable = createTable + colName[i] + " VARCHAR(20) null";
				if (i != colName.length - 1) {
					createTable = createTable + ", ";
				} else
					createTable = createTable + ");";
			}

			PreparedStatement ss = conn.prepareStatement(createTable.toString());
			ss.execute();
			// form the SQL to make the insertion
			StringBuffer sqlBegin = new StringBuffer("insert into " + tbName + "(");
			// get the title of the rows to make up the SQL sentence
			for (int i = 0; i < colName.length; i++) {
				sqlBegin.append(colName[i]);
				if (i != colName.length - 1) {
					sqlBegin.append(",");
				}
			}
			sqlBegin.append(") values(");
			is.close();

			// lines after this read the data form excel
			POIFSFileSystem fs;
			HSSFWorkbook wb;
			HSSFSheet sheet;
			HSSFRow row;

			is = new FileInputStream(uploadPath + "book1.xls");
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(sheetNum);

			// get the total number of rows in the current sheet
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// because the first line is always the title, we then read from the
			// second line
			String sql = new String(sqlBegin);
			String temp;
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				int j = 0;
				while (j < colNum) {
					temp = excelReader.getStringCellValue(row.getCell((short) j)).trim();

					// handle the date
					if (colName[j].indexOf("date") != -1) {
						temp = temp.substring(0, temp.length() - 2);
						// do some math to handle it
						Date d = new Date((Long.valueOf(temp) - 25569) * 24 * 3600 * 1000);
						DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						temp = "'" + formater.format(d) + "'";
					}

					sql = sql + temp;
					if (j != colNum - 1) {
						sql = sql + ",";
					}
					j++;
				}
				sql = sql + ")";
				System.out.println(sql.toString());
				PreparedStatement ps = conn.prepareStatement(sql.toString());
				ps.execute();
				ps.close();
				sql = "";
				sql = sqlBegin.toString();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
