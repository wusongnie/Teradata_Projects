import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class test {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/usermaster?characterEncoding=utf-8";
	public static Connection conn;
	public static String filePath = "/Users/wusongnie/Desktop/project2";
	public static String badFilePath = "/Users/wusongnie/Desktop/bad";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "root");
			String init1 = "create table if not exists psa1 (姓名 varchar(20) null,E_mail_Display_Name varchar(20) null,CN_Name varchar(20) null,QID varchar(20) null,Super__Pref__Nm varchar(20) null,Team varchar(20) null, Total varchar(20) null, "
					+ "Billable_project_name varchar(60) null,  Billable_hrs varchar(20) null,"
					+ "Presale_project_name varchar(100) null, Presale_hrs varchar(20) null, 挂帐 varchar(20) null,"
					+ "total_other_hrs  varchar(20) null, admin varchar(20) null, training varchar(20) null , holiday varchar(20) null , "
					+ "annual_leave varchar(20) null , Others varchar(20) null ,Demo varchar(20) null, presale_工作描述 varchar(200) null,tasks varchar(200) null,  status varchar(20) null);";
			String init2 = "alter table psa1 convert to character set utf8mb4 collate utf8mb4_bin;";
			String init3 = "create table if not exists psa2 (姓名 varchar(20) null,E_mail_Display_Name varchar(20) null,CN_Name varchar(20) null,QID varchar(20) null,Super__Pref__Nm varchar(20) null,Team varchar(20) null, Total varchar(20) null, "
					+ "Billable_project_name varchar(60) null,  Billable_hrs varchar(20) null,"
					+ "Presale_project_name varchar(100) null, Presale_hrs varchar(20) null, 挂帐 varchar(20) null,"
					+ "total_other_hrs  varchar(20) null, admin varchar(20) null, training varchar(20) null , holiday varchar(20) null , "
					+ "annual_leave varchar(20) null , Others varchar(20) null ,Demo varchar(20) null, presale_工作描述 varchar(200) null,tasks varchar(200) null,  status varchar(20) null);";
			String init4 = "alter table psa2 convert to character set utf8mb4 collate utf8mb4_bin;";
			// create the table
			PreparedStatement ps = conn.prepareStatement(init1.toString());
			ps.execute();
			ps.close();
			System.out.println(init1);
			// change the word to utf8
			PreparedStatement ps1 = conn.prepareStatement(init2.toString());
			ps1.execute();
			ps1.close();
			ExcelReader excelReader = new ExcelReader();
			FileInputStream is = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFRow row = wb.getSheetAt(0).getRow(0);
			String[] colname = new String[row.getLastCellNum()];
			
			for(int i = 0; i < row.getLastCellNum();i++){
				colname[i] = excelReader.getStringCellValue((row.getCell(i))).replaceAll("\\.", " ").replaceAll(" ", "_");
				colname[i] = colname[i].replaceAll("-", "_").replaceAll("'", " ");
				System.out.println(colname[i]);
			}
			String update = "";
			
			
			System.out.println(init2);
			System.out.println("psa database初始化完成！");
			
			
			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
