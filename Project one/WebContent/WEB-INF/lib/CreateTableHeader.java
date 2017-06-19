package org.krylov.lib;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: krylov
 * Date: 08.07.13
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
class  CreateTableHeader {
    private Map<Integer,Integer> columnLength = new HashMap<Integer, Integer>();
    private final List<LinkedHashMap> xlsData;

    public List<String> getHeaderNames() {
        return headerNames;
    }

    private final List<String> headerNames = new ArrayList<String>();


    public CreateTableHeader(List<LinkedHashMap> xlsData, Map<Integer,Integer> columnLength){
        this.columnLength=columnLength;
        this.xlsData=xlsData;

    }

    public Integer getColumsCount(){

        return headerNames.size();
    }

    public String getHeaderString(Map<Integer, String> column_names){
        String result;
        List<String> dataTypes = new ArrayList<String>();
        boolean datatypes_found=false;
        for (int i=0; i <xlsData.size();i++){
            if (datatypes_found){
                break;
            }
            LinkedHashMap iterMap=xlsData.get(i);
            //getting header names that were given by user or read first row for names
            if (i==0){
                for (int x=0; x <iterMap.size();x++){
                    if (column_names.containsKey(x)){
                        headerNames.add(x,column_names.get(x));
                    } else {
                    headerNames.add(x,iterMap.get(x).toString());
                    }
                }
            }
            //getting datatypes for first database row
            else {
                for (int y=0; y <iterMap.size();y++){
                    if (!iterMap.containsValue(null)){
                        for (int x=0; x <iterMap.size();x++){
                            if (iterMap.get(x).getClass().getCanonicalName()=="java.lang.String"){
                                dataTypes.add(x, "VARCHAR");
                            } else if (iterMap.get(x).getClass().getCanonicalName()=="java.lang.Double"){
                                dataTypes.add(x,"DOUBLE");
                            } else if (iterMap.get(x).getClass().getCanonicalName()=="java.lang.Boolean"){
                                dataTypes.add(x,"BIT");
                            }else if (iterMap.get(x).getClass().getCanonicalName()=="java.sql.Date"){
                                dataTypes.add(x,"DATE");
                            }else{
                                System.out.println("UNKNOWN_TYPE");
                                dataTypes.add(x,"UNKNOWN_TYPE");
                            }
                        }
                        datatypes_found=true;
                    }
                }
            }

        }
        result=createQuery(headerNames, dataTypes);


        return result;
    }


    public String getHeaderString(){
        String result;
        List<String> dataTypes = new ArrayList<String>();
        boolean datatypes_found=false;
        for (int i=0; i <xlsData.size();i++){
            if (datatypes_found){
                break;
            }
            LinkedHashMap iterMap=xlsData.get(i);
            //getting header names
            if (i==0){
                for (int x=0; x <iterMap.size();x++){
                    headerNames.add(x,iterMap.get(x).toString());
                }
            }
            //getting datatypes for first database row
            else {
                for (int y=0; y <iterMap.size();y++){
                    if (!iterMap.containsValue(null)){
                        for (int x=0; x <iterMap.size();x++){
                            if (iterMap.get(x).getClass().getCanonicalName()=="java.lang.String"){
                                dataTypes.add(x, "VARCHAR");
                            } else if (iterMap.get(x).getClass().getCanonicalName()=="java.lang.Double"){
                                dataTypes.add(x,"DOUBLE");
                            } else if (iterMap.get(x).getClass().getCanonicalName()=="java.lang.Boolean"){
                                dataTypes.add(x,"BIT");
                            }else if (iterMap.get(x).getClass().getCanonicalName()=="java.sql.Date"){
                                dataTypes.add(x,"DATE");
                            }else{
                                System.out.println("UNKNOWN_TYPE");
                                dataTypes.add(x,"UNKNOWN_TYPE");
                            }
                        }
                        datatypes_found=true;
                    }
                }
            }

        }
        result=createQuery(headerNames, dataTypes);


        return result;
    }
    //creating query
    private String createQuery(List<String> headerNames, List<String> dataTypes){
        String result="";
        System.out.println("Rows "+headerNames.size());
        for (int i=0; i<headerNames.size(); i++){
              if (dataTypes.get(i)=="VARCHAR"){
                  result=result+headerNames.get(i)+" "+dataTypes.get(i)+"("+columnLength.get(i)+"), ";
              }else if (i==headerNames.size()-1){
                  result=result+headerNames.get(i)+" "+dataTypes.get(i);
              }else{
                  result=result+headerNames.get(i)+" "+dataTypes.get(i)+", ";
              }


        }

        return result.substring(0, result.length());

    }
}
