<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.jfree.chart.ChartFactory" %>
<%@ page import="org.jfree.chart.JFreeChart" %>
<%@ page import="org.jfree.chart.plot.PlotOrientation" %>
<%@ page import="org.jfree.chart.servlet.ServletUtilities" %>
<%@ page import="org.jfree.data.category.DefaultCategoryDataset" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(610, "Guangzhou", "pork");
    dataset.addValue(220, "Guangzhou", "beaf");
    dataset.addValue(530, "Guangzhou", "chicken");
    dataset.addValue(340, "Guangzhou", "fish");
    JFreeChart chart = ChartFactory.createBarChart3D("Meat sales statistics figure", "meat",
            "Sales Volume", dataset, PlotOrientation.VERTICAL, false, false,
            false);
    String filename = ServletUtilities.saveChartAsPNG(chart, 500, 300,
            null, session);
    String graphURL = request.getContextPath()
            + "/DisplayChart?filename=" + filename;
    out.println(filename);
    out.println("------------------");
    out.println(graphURL);    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
    <img src="<%=graphURL%>" width=500 height=300 border=0
    usemap="#<%= filename %>">
  </body>
</html>