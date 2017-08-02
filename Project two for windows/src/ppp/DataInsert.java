package ppp;

/////////////////////////////////////////////////////////////////////////////
//Title         	   PSA
//文件            DataInsert.java, ExcelReader.java
//
//Author Songnie Wu
////////////////////////////80 columns wide //////////////////////////////////
/**
* This program reads the appropriate excel file and convert them to the
* assembiled file needed by the PSA. 
* Exception handling: Will put the excels that contains bad inputs to another directory.
*
* 
* Bugs: N/A
*
* @author Songnie WU
*/

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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataInsert {

	public static List<HashMap<String, String>> table = new ArrayList<HashMap<String, String>>();

	public static String filePath = readProperties()[0];
	public static String badFilePath = readProperties()[1];
	public static String outPath = readProperties()[2];
	public static String[] readProperties() {
		String[] paths = new String[3];
		Properties prop = new Properties();
		try {
			prop.load(DataInsert.class.getResourceAsStream("filePath.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paths[0]=prop.getProperty("Inpath");
		paths[1]=prop.getProperty("Badpath");
		paths[2]=prop.getProperty("Outpath");
		return paths;
		
	}
	/**
	 * Main: The brain of this whole f***ing program.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			List<File> files = new ArrayList<File>();
			List<File> badFiles = new ArrayList<File>();
			listFile(filePath, files);// traverse the directory recursively, put
										// the files into the list
			// the heads are the head of the target table
			String[] heads = { "E_mail_Display_Name", "CN_Name", "QID", "Super__Pref__Nm", "Team", "Total",
					"Billable_project_name", "Billable_hrs", "Presale_project_name", "Presale_hrs", "total_other_hrs",
					"admin", "training", "holiday", "annual_leave", "Others", "presale_工作描述", "status" };

			boolean badInput = false; // badInput indicates if the file contains
										// bad input
			String updater = null; // updater stores the file that will update
									// some particular inputs
			for (File xlsx : files) {
				if (xlsx.getName().substring(0, 1).equals(".") || xlsx.getName().substring(0, 1).equals("~")
						|| xlsx.getName().indexOf("Billable Hours") != -1) {
					continue;// skip the files starting with "Billable hours"
				}
				if (xlsx.getName().substring(0, 7).equals("profile")) {
					updater = xlsx.getAbsolutePath(); // "profile" is an
														// updater, skip it and
														// handle it in the
														// future
					continue;
				}

				String xlsxFile = xlsx.getPath();

				System.out.println("Handling..." + xlsxFile);

				badInput = insertData(xlsxFile);

				System.out.println(xlsx.getName() + "Finished!");
				if (badInput) {
					copyFile(xlsxFile, badFilePath + "/" + xlsx.getName());
					badFiles.add(xlsx);// put the bad input files into the list
					System.out.println(xlsx.getName() + " bad file recorded");
				}
			}
			System.out.println("Successfully put all the files into new file");// remind the user
			System.out.println("Please check the files below:");
			for (File bad : badFiles) {
				System.out.println(bad.getPath());
				// print out all the files with a bad input
			}
			updateTable(updater); // use the profile to update the table
			XSSFWorkbook outputWb = new XSSFWorkbook();
			XSSFSheet sheet = outputWb.createSheet("psa");
			int i = 1;
			int j = 0;
			XSSFRow Row1 = sheet.createRow(0);
			// following code generates the column heads
			for (String key : heads) {
				XSSFCell cell = Row1.createCell(j);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(key);
				j++;
			}
			j = 0;
			List<HashMap<String, String>> bads = new ArrayList<HashMap<String, String>>();
			// following code traverse the whole hashMap and creates an excel
			// file out of the hashMap
			for (HashMap<String, String> row : table) {
				if (row.get("status").equals("异常")) {
					bads.add(row);
					continue;
				}
				XSSFRow tempRow = sheet.createRow(i);
				for (String key : heads) {
					XSSFCell cell = tempRow.createCell(j);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);// set the cell
																// type to
																// String
					if (row.get(key) != null) {
						String k = row.get(key).replaceAll("'", ""); // cleans
																		// the
																		// "'"
						cell.setCellValue(k);
					} else {
						cell.setCellValue(row.get(key));
					}

					j++;
				}
				j = 0;
				++i;
			}
			// lines below put the bad lines to the end of the excel
			for (HashMap<String, String> row : bads) {
				XSSFRow tempRow = sheet.createRow(i);
				for (String key : heads) {
					XSSFCell cell = tempRow.createCell(j);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);// set the cell
																// type to
																// String
					if (row.get(key) != null) {
						String k = row.get(key).replaceAll("'", ""); // cleans
																		// the
																		// "'"
						cell.setCellValue(k);
					} else {
						cell.setCellValue(row.get(key));
					}

					j++;
				}
				j = 0;
				++i;
			}
			// create the file in the targeted folder

			FileOutputStream fos = new FileOutputStream(outPath);
			outputWb.write(fos);
			fos.flush();
			System.out.println("存盘完成！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * The function that handles the data insertion.
	 * 
	 * @param filePath
	 * @return
	 */
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
			HashMap<String, String> map = null;
			for (int i = 0; i < 5; i++) {
				row = wb.getSheetAt(0).getRow(2 + i);
				if (excelReader.getStringCellValue(row.getCell(31)).equals(" ")
						|| excelReader.getStringCellValue(row.getCell(31)).equals("0.0"))
					break;

				String[] datas = new String[14];
				for (int j = 0; j < 14; j++) {
					datas[j] = excelReader.getStringCellValue(row.getCell(j + 29));
				}

				dataList.add(datas);

			}

			// form the SQL to make the insertion

			String[] colNames = { "姓名", "QID", "Total", "Billable_project_name", "tasks", "Billable_hrs",
					"Presale_project_name", "Presale_hrs", "total_other_hrs", "admin", "training", "holiday",
					"annual_leave", "Others", "status" };
			for (String[] array : dataList) {
				map = new HashMap<String, String>();
				for (int k = 0; k < array.length; k++) {
					if (array[k].equals("0.0") || array[k].equals("' '")) {
						array[k] = " ";
					}
				}
				for (int k = 0; k < array.length; k++) {
					map.put(colNames[k], array[k]);
				}
				if (badInput) {
					map.put("status", "异常");
				} else {
					map.put("status", "正常");
				}

				map = updatePresale(filePath, map);
				for (Entry<String, String> entry : map.entrySet()) {
					System.out.println("正在记录......" + entry.getKey() + " 值：" + entry.getValue());
				}
				table.add(map);
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

	private static void updateTable(String filePath) throws IOException, SQLException {
		List<HashMap<String, String>> tablek = new ArrayList<HashMap<String, String>>();
		ExcelReader excelReader = new ExcelReader();
		FileInputStream is = new FileInputStream(filePath);

		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFRow row = wb.getSheetAt(0).getRow(0);
		String[] colname = new String[row.getLastCellNum()];

		HashMap<String, String> temp = null;
		HashMap<String, String> perm = new HashMap<String, String>();

		for (int i = 0; i < row.getLastCellNum(); i++) {
			colname[i] = excelReader.getStringCellValue((row.getCell(i))).replaceAll("\\.", " ").replaceAll(" ", "_");
			colname[i] = colname[i].replaceAll("-", "_").replaceAll("'", " ");
		}
		for (int i = 1; i < wb.getSheetAt(0).getLastRowNum(); i++) {

			row = wb.getSheetAt(0).getRow(i);
			if (excelReader.getStringCellValue((row.getCell(0))).equals("' '")) {
				break;
			}

			for (int k = 0; k < table.size(); k++) {
				temp = table.get(k);
				if (temp.get("QID").equalsIgnoreCase(excelReader.getStringCellValue((row.getCell(2))))) {
					for (int j = 0; j < row.getLastCellNum(); j++) {
						if (!temp.containsKey(colname[j])) {
							temp.put(colname[j].trim(), excelReader.getStringCellValue((row.getCell(j))));
						}
					}
				}

			}

		}
	}

	private static HashMap<String, String> updatePresale(String filePath, HashMap<String, String> map)
			throws IOException, SQLException {
		System.out.println("开始更新presale信息");
		ExcelReader excelReader = new ExcelReader();
		FileInputStream is = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(is);

		for (int i = 0; i < 5; i++) {

			XSSFRow row = wb.getSheetAt(0).getRow(i + 6);
			if (excelReader.getStringCellValue((row.getCell(2))).equals("' '")) {
				break;
			}

			if (map.get("Presale_project_name").equalsIgnoreCase(excelReader.getStringCellValue((row.getCell(0))))) {
				map.put("presale_工作描述", excelReader.getStringCellValue((row.getCell(2))));
			}
		}
		System.out.println("更新presale信息完成！");
		return map;

	}

	private static void listFile(String directoryName, List<File> fileNames) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				if (!fileNames.contains(file)) {
					fileNames.add(file);
				}
				System.out.println(file);
			} else if (file.isDirectory()) {
				listFile(file.getAbsolutePath(), fileNames);
			}
		}

	}

	public static void copyFile(String oldPath, String newPath) {
		try {
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

}
