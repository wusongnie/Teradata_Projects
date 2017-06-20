<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.jfree.chart.ChartFactory"%>
<%@ page import="org.jfree.chart.JFreeChart"%>
<%@ page import="org.jfree.chart.plot.PlotOrientation"%>
<%@ page import="org.jfree.chart.servlet.ServletUtilities"%>
<%@ page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@ page import="songnie.test.* "%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%
	
	CreateChart ct=new CreateChart();
	String [][]chartArray = ct.main(null);
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
		for(int j=0;j<chartArray[0].length;j++)
		dataset.addValue(Double.parseDouble(chartArray[1][j]), "Guangzhou", chartArray[0][j]);
	
	
	JFreeChart chart = ChartFactory.createBarChart3D("前十名省利润表", "省名", "利润",
			dataset, PlotOrientation.VERTICAL, false, false, false);
	String filename = ServletUtilities.saveChartAsPNG(chart, 1000, 600, null, session);
	String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;
	//out.println(filename);
	//out.println("------------------");
	//out.println(graphURL);
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
	<img src="<%=graphURL%>" width=1000 height= 600 border=0
		usemap="#<%=filename%>">
		
</body>
</html>