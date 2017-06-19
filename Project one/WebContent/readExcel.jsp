<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.*,java.io.*"%>
<%@ page
	import="org.apache.poi.poifs.filesystem.*,org.apache.poi.hssf.usermodel.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Read in the file</title>
</head>
<body>
	<center>
		<h2>the excel uploaded</h2>
		<table border="1" width="100%">
			<%
			String uploadPath="/Users/wusongnie/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/TomcatTest//upload/";	
			FileInputStream fileInput = new FileInputStream(uploadPath + "book1.xls");
				POIFSFileSystem fs = new POIFSFileSystem(fileInput);
				HSSFWorkbook workbook = new HSSFWorkbook(fs);

				// load the sheets
				for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
					HSSFSheet sheet = workbook.getSheetAt(numSheet);

					// declare a row
					HSSFRow row = null;
					// declare a cell
					HSSFCell cell = null;
					short i = 0;
					short y = 0;
					// read in all the information
					for (i = 0; i <= sheet.getLastRowNum(); i++) {
						out.println("<tr>");
						row = sheet.getRow(i);
						for (y = 0; y < row.getLastCellNum(); y++) {
							cell = row.getCell(y);
							out.println("<td>");
							// decide the type of the cell
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_NUMERIC:
								out.println(cell.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_STRING:
								out.println(cell.getStringCellValue());
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								out.println(cell.getNumericCellValue());
								break;
							default:
								out.println("不明格式");
								break;
							}
						}
						out.println("</td>");
					}
					out.println("</tr>");
				}
			%>
		</table>
	</center>
</body>
</html>