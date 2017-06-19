package org.krylov.lib;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: krylov
 * Date: 08.07.13
 * Time: 9:47
 * To change this template use File | Settings | File Templates.
 */
  class InsertToDB {

    private static String DB_DRIVER; //= "oracle.jdbc.driver.OracleDriver";
    private static String DB_CONNECTION; //= "jdbc:oracle:thin:@localhost:1521:MKYONG";
    private static String DB_USER; //= "user";
    private static String DB_PASSWORD;// = "password";
    private static String DB_TABLE_NAME;
    private final List<LinkedHashMap> xlsData;
    private final Map<Integer,Integer> columnLength;

    public InsertToDB(Properties dbconf, List<LinkedHashMap> xlsData, Map<Integer,Integer> columnLength){

          DB_DRIVER=dbconf.getProperty("Driver");
          DB_CONNECTION=dbconf.getProperty("Connection");
          DB_USER=dbconf.getProperty("User");
          DB_PASSWORD=dbconf.getProperty("Password");
          DB_TABLE_NAME=dbconf.getProperty("TableName");
          this.xlsData=xlsData;
          this.columnLength=columnLength;

    }
    public void start(Map<Integer, String> column_names){
        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("JDBC Driver Registered!");
        Connection connection;

        try {
            connection = DriverManager
                    .getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            try {
                CreateTableHeader createTableHeader = new CreateTableHeader(xlsData, columnLength);
                String query = createTableHeader.getHeaderString(column_names);
                String db_colums_names="";
                Statement statement = connection.createStatement();
                statement.addBatch("DROP TABLE IF EXISTS  "+DB_TABLE_NAME);
                String final_query = "CREATE TABLE "+ DB_TABLE_NAME +" ("+ query +")";
                System.out.println(final_query);
                statement.addBatch(final_query);
                for (int i=0; i <xlsData.size();i++){
                    String db_questions="";
                    LinkedHashMap iterMap=xlsData.get(i);
                    if (i==0){
                        for (int x=1; x <=iterMap.size();x++){
                            db_colums_names=db_colums_names+createTableHeader.getHeaderNames().get(x-1)+", ";
                        }
                        db_colums_names=db_colums_names.substring(0,db_colums_names.length()-2);


                    } else{
                        for (int x=0; x <iterMap.size();x++){
                            if (iterMap.get(x)==null){
                                db_questions=db_questions+"NULL"+", ";
                            }
                            else if((iterMap.get(x).getClass().getCanonicalName()=="java.lang.String") || (iterMap.get(x).getClass().getCanonicalName()=="java.sql.Date")) {
                                db_questions=db_questions+"'"+iterMap.get(x)+"', ";
                            }
                            else{
                                db_questions=db_questions+iterMap.get(x)+", ";
                            }
                        }
                        db_questions=db_questions.substring(0,db_questions.length()-2);
                        String insertTableSQL = "INSERT INTO "+DB_TABLE_NAME
                                + "("+db_colums_names+") VALUES"
                                + "("+db_questions+")";
                        //System.out.println(insertTableSQL);
                        statement.addBatch(insertTableSQL);

                    }

                    if(i % 1000 == 0) {
                        try{
                            statement.executeBatch();
                            System.out.println("Batch");
                        } catch (java.sql.BatchUpdateException ex){
                            System.out.println(ex);
                        }
                    }

                }
                // statement.executeBatch();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            System.out.println("Failed to make connection!");
        }
    }
    public void start(){
        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("JDBC Driver Registered!");
        Connection connection;

        try {
            connection = DriverManager
                    .getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            try {
                CreateTableHeader createTableHeader = new CreateTableHeader(xlsData, columnLength);
                String query = createTableHeader.getHeaderString();
                String db_colums_names="";
                Statement statement = connection.createStatement();
                statement.addBatch("DROP TABLE IF EXISTS  "+DB_TABLE_NAME);
                String final_query = "CREATE TABLE "+ DB_TABLE_NAME +" ("+ query +")";
                System.out.println(final_query);
                statement.addBatch(final_query);
                for (int i=0; i <xlsData.size();i++){
                    String db_questions="";
                    LinkedHashMap iterMap=xlsData.get(i);
                    if (i==0){
                        for (int x=1; x <=iterMap.size();x++){
                                db_colums_names=db_colums_names+createTableHeader.getHeaderNames().get(x-1)+", ";
                        }
                        db_colums_names=db_colums_names.substring(0,db_colums_names.length()-2);


                    } else{
                        for (int x=0; x <iterMap.size();x++){
                            if (iterMap.get(x)==null){
                                db_questions=db_questions+"NULL"+", ";
                            }
                            else if((iterMap.get(x).getClass().getCanonicalName()=="java.lang.String") || (iterMap.get(x).getClass().getCanonicalName()=="java.sql.Date")) {
                                db_questions=db_questions+"'"+iterMap.get(x)+"', ";
                            }
                            else{
                                db_questions=db_questions+iterMap.get(x)+", ";
                            }
                        }
                        db_questions=db_questions.substring(0,db_questions.length()-2);
                        String insertTableSQL = "INSERT INTO "+DB_TABLE_NAME
                                + "("+db_colums_names+") VALUES"
                                + "("+db_questions+")";
                        //System.out.println(insertTableSQL);
                        statement.addBatch(insertTableSQL);

                    }

                    if(i % 1000 == 0) {
                        try{
                        statement.executeBatch();
                        System.out.println("Batch");
                        } catch (java.sql.BatchUpdateException ex){
                            System.out.println(ex);
                        }
                    }

                }
               // statement.executeBatch();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            System.out.println("Failed to make connection!");
        }
    }

}
