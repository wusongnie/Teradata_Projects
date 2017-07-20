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

public class DataInsert {

	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/usermaster?characterEncoding=utf-8";
	public static Connection conn;
	public static String filePath = "/Users/wusongnie/Desktop/project2";
	public static String badFilePath = "/Users/wusongnie/Desktop/bad";

	public static void main(String[] args) throws Exception {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "root");
			List<File> files = new ArrayList<File>();
			List<File> badFiles = new ArrayList<File>();
			listFile(filePath, files);// put the files to the list
			String init1 = "create table if not exists psa (姓名 varchar(20) null , QID varchar(20) null,Total varchar(20) null, tasks varchar(200) null, "
					+ "Billable_project_name varchar(60) null,  Billable_hrs varchar(20) null,"
					+ "Presale_project_name varchar(100) null, Presale_hrs varchar(20) null, "
					+ "total_other_hrs  varchar(20) null, admin varchar(20) null, training varchar(20) null , holiday varchar(20) null , "
					+ "annual_leave varchar(20) null , Others varchar(20) null , status varchar(20) null);";
			String init2 = "alter table psa convert to character set utf8mb4 collate utf8mb4_bin;";

			// create the table
			PreparedStatement ps = conn.prepareStatement(init1.toString());
			ps.execute();
			ps.close();
			System.out.println(init1);
			// change the word to utf8
			PreparedStatement ps1 = conn.prepareStatement(init2.toString());
			ps1.execute();
			ps1.close();
			System.out.println(init2);
			System.out.println("psa database初始化完成！");
			boolean badInput = false;
			for (File xlsx : files) {
				if (xlsx.getName().substring(0, 1).equals(".") || xlsx.getName().substring(0, 1).equals("~")
						|| xlsx.getName().indexOf("Billable Hours") != -1)
					continue;
				String xlsxFile = xlsx.getPath();
				System.out.println("正在录入..." + xlsxFile);
				badInput = insertData(xlsxFile);
				System.out.println(xlsx.getName() + "已进入数据库！");
				if (badInput) {
					copyFile(xlsxFile, badFilePath + "/" + xlsx.getName());
					badFiles.add(xlsx);
					System.out.println(xlsx.getName() + "已记录不良数据！");
				}
			}
			System.out.println("成功！所有表均已进入数据库！");
			System.out.println("所有表均已进入数据库,但请检查以下不良数据：");
			for (File bad : badFiles) {
				System.out.println(bad.getPath());
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void listFile(String directoryName, List<File> fileNames) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				fileNames.add(file);
				System.out.println(file);
			} else if (file.isDirectory()) {
				listFile(file.getAbsolutePath(), fileNames);
			}
		}

	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // if the file is existing
				FileInputStream inStream = new FileInputStream(oldPath); // read
																			// in
																			// the
																			// original
																			// file
				FileOutputStream outStream = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					outStream.write(buffer, 0, byteread);
				}
				inStream.close();
				outStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	@SuppressWarnings({ "resource" })
	public static boolean insertData(String filePath) {
		boolean badInput = false;
		try {
			List<String[]> dataList = new ArrayList<String[]>();
			// read data form the excel
			ExcelReader excelReader = new ExcelReader();
			FileInputStream is = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFRow row = wb.getSheetAt(0).getRow(2);
			badInput = testInput(wb);

			for (int i = 0; i < 5; i++) {
				row = wb.getSheetAt(0).getRow(2 + i);
				if (excelReader.getStringCellValue(row.getCell(31)).equals("' '")
						|| excelReader.getStringCellValue(row.getCell(31)).equals("0.0"))
					break;

				String[] datas = new String[14];
				for (int j = 0; j < 14; j++) {
					datas[j] = excelReader.getStringCellValue(row.getCell(j + 29));
				}

				dataList.add(datas);

			}

			// form the SQL to make the insertion
			StringBuffer sqlBegin = new StringBuffer("insert into psa(姓名,QID,Total,"
					+ "Billable_project_name,tasks,Billable_hrs," + "Presale_project_name,Presale_hrs,"
					+ "total_other_hrs,admin,training,holiday," + "annual_leave,Others, status) values(");

			for (String[] array : dataList) {
				String sql = new String(sqlBegin);
				for (int k = 0; k < array.length; k++) {
					if (k != array.length - 1) {
						sql = sql + array[k] + ",";
					} else {
						sql = sql + array[k];
					}
				}
				if(badInput){
					sql = sql + ",'异常'";
				}else{
					sql = sql + ",'正常'";
				}
				sql = sql + ")";
				System.out.println(sql.toString());
				PreparedStatement ps = conn.prepareStatement(sql.toString());
				ps.execute();
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
		return badInput;
	}

	private static boolean testInput(XSSFWorkbook wb) {
		ExcelReader excelReader = new ExcelReader();
		XSSFRow row = wb.getSheetAt(0).getRow(2);
		if (excelReader.getStringCellValue(row.getCell(29)).equalsIgnoreCase("'XXX'")
				|| excelReader.getStringCellValue(row.getCell(29)).equalsIgnoreCase("' '")
				|| excelReader.getStringCellValue(row.getCell(30)).equals("0.0")
				|| excelReader.getStringCellValue(row.getCell(30)).equals("' '")
				|| excelReader.getStringCellValue(row.getCell(30)).equalsIgnoreCase("'XXX'")) {
			return true;
		}
		return false;
	}

}