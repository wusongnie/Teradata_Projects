package org.krylov.lib;

import org.krylov.lib.InsertToDB;
import org.krylov.lib.ReadXls;

import java.io.FileInputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: krylov
 * Date: 29.05.13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class ExcelToDB {
    private List<LinkedHashMap> xlsData;
    private Map<Integer,Integer> columnLength;

    Map<Integer, String> getColumn_names() {
        return column_names;
    }
    /**
     * <p>Set names for the table columns</p>
     *
     *                     For example
     *
     * <p>ExcelToDB excelToDB = new ExcelToDB(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD, DB_TABLE_NAME); <br>
     * Map<Integer, String> column_names = new HashMap<Integer, String>(); <br>
     * column_names.put(1, "testname1");<br>
     * excelToDB.setColumn_names(column_names);<br>
     *
     * will set first column of database table to "testname1" <br>
     * </p>
     *
     */
    public void setColumn_names(Map<Integer, String> column_names) {
        this.column_names = column_names;
    }

    private Map<Integer,String> column_names;


    private final Properties dbconf = new Properties();

    /**
     * <p>Set values that are necessary for db connection</p>
     *
     * @param db_driver Driver for the database (for example "com.mysql.jdbc.Driver")
     * @param db_conn The connection to the database (for example "jdbc:mysql://localhost:3306/exltodb")
     * @param db_user   The username for the database
     * @param db_password   The password to the database
     * @param tableName The name for a table that should be created.
     *
     */
    public ExcelToDB(String db_driver, String db_conn, String db_user, String db_password, String tableName){
        setDriver(db_driver);
        setConnection(db_conn);
        setUser(db_user);
        setDBPassword(db_password);
        setTableName(tableName);
    }

    // Db driver
     void setDriver(String myDriver){
          dbconf.put("Driver", myDriver);
     }
    // Db connection
    void setConnection(String myConnection){
        dbconf.put("Connection", myConnection);
    }
    //db user
    void setUser(String myUser){
        dbconf.put("User", myUser);
    }
    //db password
    void setDBPassword(String myPassword){
        dbconf.put("Password", myPassword);
    }
    //Excel file to parce
    public void setXLSFile (String xlsFile){
        dbconf.put("File", xlsFile);
    }
    //set table name to insert data to
    void setTableName(String tableName){
        dbconf.put("TableName", tableName);
    }

    /**
     * <p>Start to parse the file</p>
     *
     * @param file Full path to the file for parsing
     *
     *
     */
    public void start(String file){
        ReadXls readXls = new ReadXls(file);
        xlsData=readXls.getResult();
        columnLength=readXls.getColumnLength();
        InsertToDB insertToDB = new InsertToDB(dbconf, xlsData, columnLength);
        if (getColumn_names()==null){
            insertToDB.start();
        }
        else {
            insertToDB.start(getColumn_names());
        }
    }
    /**
     * <p>Start to parse the file from FileInputStream. InputStream requires more memory as it has to buffer the whole file</p>
     *
     * @param file Full path to the file for parsing
     *
     *
     */
    public void start(FileInputStream file){
        ReadXls readXls = new ReadXls(file);
        xlsData=readXls.getResult();
        columnLength=readXls.getColumnLength();
        InsertToDB insertToDB = new InsertToDB(dbconf, xlsData, columnLength);
        if (getColumn_names()==null){
            insertToDB.start();
        }
        else {
            insertToDB.start(getColumn_names());
        }
    }
    /**
     * <p>Start to parse the file from FileInputStream. InputStream requires more memory as it has to buffer the whole file</p>
     *
     * @param file Full path to the file for parsing
     * @param annotations Map that is used for setting type of parsed Excel column.
     *                    For example
     *                    <p>Map<Integer, String> annotation = new HashMap<Integer, String>(); <br>
     * annotation.put(6, "text");<br>
     * annotation.put(7, "numeric");<br>
     * annotation.put(2, "date");</p>
     *                    will set sixth column as String, seventh - as Double and second to Date
     *
     *
     */
    public void start(FileInputStream file, Map<Integer, String>annotations){
        ReadXls readXls = new ReadXls(file, annotations);
        xlsData=readXls.getResult();
        columnLength=readXls.getColumnLength();
        InsertToDB insertToDB = new InsertToDB(dbconf, xlsData, columnLength);
        if (getColumn_names()==null){
            insertToDB.start();
        }
        else {
            insertToDB.start(getColumn_names());
        }
    }
    /**
     * <p>Start to parse the file</p>
     *
     * @param file Full path to the file for parsing
     * @param annotations Map that is used for setting type of parsed Excel column.
     *                    For example
     *                    <p>Map<Integer, String> annotation = new HashMap<Integer, String>(); <br>
     * annotation.put(6, "text");<br>
     * annotation.put(7, "numeric");<br>
     * annotation.put(2, "date");</p>
     *                    will set sixth column as String, seventh - as Double and second to Date
     *
     *
     */
    public void start(String file, Map<Integer, String>annotations){
        ReadXls readXls = new ReadXls(file, annotations);
        xlsData=readXls.getResult();
        columnLength=readXls.getColumnLength();
        InsertToDB insertToDB = new InsertToDB(dbconf, xlsData, columnLength);
        if (getColumn_names()==null){
            insertToDB.start();
        }
        else {
            insertToDB.start(getColumn_names());
        }
    }
}
