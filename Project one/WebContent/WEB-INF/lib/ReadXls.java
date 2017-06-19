package org.krylov.lib;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: krylov
 * Date: 05.07.13
 * Time: 9:41
 * To change this template use File | Settings | File Templates.
 */
class ReadXls {
    public List<LinkedHashMap> getResult() {
        return result;
    }


    public Map<Integer, Integer> getColumnLength() {
        return columnLength;
    }

    private final Map<Integer,Integer> columnLength = new HashMap<Integer, Integer>();
    private List<LinkedHashMap> result;

    Map<Integer, Integer>getAnnotationsId(Map<Integer, String> annotations) {
        Map<Integer,Integer> annotId = new HashMap<Integer,Integer>();
        for (Map.Entry<Integer, String> entry : annotations.entrySet()) {
            if (entry.getValue()=="text"){
                annotId.put(entry.getKey(),1);
            } else if (entry.getValue()=="numeric"){
                annotId.put(entry.getKey(),0);
            } else if (entry.getValue()=="date"){
                annotId.put(entry.getKey(),0);
            }
        }

        return annotId;
    }

    public ReadXls(FileInputStream file, Map<Integer, String> annotations){
        try {

            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            int rowStart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();
            List<LinkedHashMap> xlsData= new ArrayList<LinkedHashMap>();
            Map<Integer,Integer> annotId=getAnnotationsId(annotations);
            int iter = 0;
            for (int rowNumber=rowStart; rowNumber<=rowEnd; rowNumber++){
                LinkedHashMap lHashMap = new LinkedHashMap();
                Row currentRow = sheet.getRow(rowNumber);
                for (int i=currentRow.getFirstCellNum(); i<currentRow.getLastCellNum(); i++){
                    Cell currentCell = currentRow.getCell(i);
                    if (annotId.containsKey(i)){
                        currentCell.setCellType(annotId.get(i));
                    }
                    switch (currentCell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            lHashMap.put(i, currentCell.getRichStringCellValue().getString());
                            if (columnLength.get(currentCell.getColumnIndex())==null){
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            } else if (currentCell.getRichStringCellValue().getString().length()>columnLength.get(currentCell.getColumnIndex())) {
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    String date = dateFormat.format(currentCell.getDateCellValue());
                                    java.sql.Date sql_date=new java.sql.Date(dateFormat.parse(date).getTime());
                                    lHashMap.put(i, sql_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                                System.out.println(lHashMap.get(i).getClass().getCanonicalName());

                            } else {
                                lHashMap.put(i, currentCell.getNumericCellValue());
                                //   System.out.println(lHashMap.get(i));

                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            lHashMap.put(i, currentCell.getBooleanCellValue());
                            //System.out.println(lHashMap.get(i));

                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            // System.out.println(currentCell.getCellFormula());

                            break;
                        case Cell.CELL_TYPE_BLANK:
                            //System.out.println("Blank");
                            lHashMap.put(i, null);
                            break;

                        default:
                            System.out.println("fuck");


                    }

                }

                xlsData.add(iter, lHashMap);
                iter++;

            }
            System.out.println(columnLength);

            this.result=xlsData;


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ReadXls(String file, Map<Integer, String> annotations){
        try {

            Workbook wb = WorkbookFactory.create(new File(file));
            Sheet sheet = wb.getSheetAt(0);
            int rowStart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();
            List<LinkedHashMap> xlsData= new ArrayList<LinkedHashMap>();
            Map<Integer,Integer> annotId=getAnnotationsId(annotations);
            int iter = 0;
            for (int rowNumber=rowStart; rowNumber<=rowEnd; rowNumber++){
                LinkedHashMap lHashMap = new LinkedHashMap();
                Row currentRow = sheet.getRow(rowNumber);
                for (int i=currentRow.getFirstCellNum(); i<currentRow.getLastCellNum(); i++){
                    Cell currentCell = currentRow.getCell(i);
                    if (annotId.containsKey(i)){
                        currentCell.setCellType(annotId.get(i));
                    }
                    switch (currentCell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            lHashMap.put(i, currentCell.getRichStringCellValue().getString());
                            if (columnLength.get(currentCell.getColumnIndex())==null){
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            } else if (currentCell.getRichStringCellValue().getString().length()>columnLength.get(currentCell.getColumnIndex())) {
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    String date = dateFormat.format(currentCell.getDateCellValue());
                                    java.sql.Date sql_date=new java.sql.Date(dateFormat.parse(date).getTime());
                                    lHashMap.put(i, sql_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                                System.out.println(lHashMap.get(i).getClass().getCanonicalName());

                            } else {
                                lHashMap.put(i, currentCell.getNumericCellValue());
                                //   System.out.println(lHashMap.get(i));

                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            lHashMap.put(i, currentCell.getBooleanCellValue());
                            //System.out.println(lHashMap.get(i));

                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            // System.out.println(currentCell.getCellFormula());

                            break;
                        case Cell.CELL_TYPE_BLANK:
                            //System.out.println("Blank");
                            lHashMap.put(i, null);
                            break;

                        default:
                            System.out.println("fuck");


                    }

                }

                xlsData.add(iter, lHashMap);
                iter++;

            }
            System.out.println(columnLength);

            this.result=xlsData;


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ReadXls(String file){
        try {

            Workbook wb = WorkbookFactory.create(new File(file));
            Sheet sheet = wb.getSheetAt(0);
            int rowStart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();
            List<LinkedHashMap> xlsData= new ArrayList<LinkedHashMap>();

            int iter = 0;
            for (int rowNumber=rowStart; rowNumber<=rowEnd; rowNumber++){
                LinkedHashMap lHashMap = new LinkedHashMap();
                Row currentRow = sheet.getRow(rowNumber);
                for (int i=currentRow.getFirstCellNum(); i<currentRow.getLastCellNum(); i++){
                    Cell currentCell = currentRow.getCell(i);
                    switch (currentCell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            lHashMap.put(i, currentCell.getRichStringCellValue().getString());
                            if (columnLength.get(currentCell.getColumnIndex())==null){
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            } else if (currentCell.getRichStringCellValue().getString().length()>columnLength.get(currentCell.getColumnIndex())) {
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    String date = dateFormat.format(currentCell.getDateCellValue());
                                    java.sql.Date sql_date=new java.sql.Date(dateFormat.parse(date).getTime());
                                    lHashMap.put(i, sql_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                                System.out.println(lHashMap.get(i).getClass().getCanonicalName());

                            } else {
                                lHashMap.put(i, currentCell.getNumericCellValue());
                                //   System.out.println(lHashMap.get(i));

                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            lHashMap.put(i, currentCell.getBooleanCellValue());
                            //System.out.println(lHashMap.get(i));

                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            // System.out.println(currentCell.getCellFormula());

                            break;
                        case Cell.CELL_TYPE_BLANK:
                            //System.out.println("Blank");
                            lHashMap.put(i, null);
                            break;

                        default:
                            System.out.println("fuck");


                    }
                }

                xlsData.add(iter, lHashMap);
                iter++;

            }
            System.out.println(columnLength);

            this.result=xlsData;


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public ReadXls(FileInputStream file){
        try {

            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            int rowStart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();
            List<LinkedHashMap> xlsData= new ArrayList<LinkedHashMap>();

            int iter = 0;
            for (int rowNumber=rowStart; rowNumber<=rowEnd; rowNumber++){
                LinkedHashMap lHashMap = new LinkedHashMap();
                Row currentRow = sheet.getRow(rowNumber);
                for (int i=currentRow.getFirstCellNum(); i<currentRow.getLastCellNum(); i++){
                    Cell currentCell = currentRow.getCell(i);
                    switch (currentCell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            lHashMap.put(i, currentCell.getRichStringCellValue().getString());
                            if (columnLength.get(currentCell.getColumnIndex())==null){
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            } else if (currentCell.getRichStringCellValue().getString().length()>columnLength.get(currentCell.getColumnIndex())) {
                                columnLength.put(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString().length());
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    String date = dateFormat.format(currentCell.getDateCellValue());
                                    java.sql.Date sql_date=new java.sql.Date(dateFormat.parse(date).getTime());
                                    lHashMap.put(i, sql_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                                System.out.println(lHashMap.get(i).getClass().getCanonicalName());

                            } else {
                                lHashMap.put(i, currentCell.getNumericCellValue());
                                //   System.out.println(lHashMap.get(i));

                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            lHashMap.put(i, currentCell.getBooleanCellValue());
                            //System.out.println(lHashMap.get(i));

                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            // System.out.println(currentCell.getCellFormula());

                            break;
                        case Cell.CELL_TYPE_BLANK:
                            //System.out.println("Blank");
                            lHashMap.put(i, null);
                            break;

                        default:
                            System.out.println("fuck");


                    }
                }

                xlsData.add(iter, lHashMap);
                iter++;

            }
            System.out.println(columnLength);

            this.result=xlsData;


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
