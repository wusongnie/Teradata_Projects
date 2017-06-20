package songnie.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CreateChart {

	static String selectsql = null;
	static ResultSet retsult = null;

	public static final String url = "jdbc:mysql://localhost:3306/usermaster?characterEncoding=utf-8";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "root";

	public static Connection conn = null;
	public static PreparedStatement pst = null;

	public static String[][] main(String[] args) {
		int paraCount = 2; // 读取参数数量
		selectsql = "select u.家庭住址,sum((s.售价-p.金额)*s.数量)as profit  from user u left join sale s on u.编号=s.用户 "
				+ "left join product p on p.编号=s.商品 where s.销售日期>=41315 group by u.家庭住址 having "
				+ "u.家庭住址 is not null order by profit desc limit 0,10;";// SQL语句

		try {
			Class.forName(name);// 指定连接类型
			conn = DriverManager.getConnection(url, user, password);// 获取连接
			pst = conn.prepareStatement(selectsql);// 准备执行语句
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[][] paras = new String[paraCount][10];
		int j=0;
		try {
			retsult = pst.executeQuery();// 执行语句，得到结果集
			
			while (retsult.next()) {
				
				for (int i = 0; i < paraCount; i++) {
					paras[i][j] = retsult.getString(i+1);
				}
				//System.out.println(Arrays.toString(paras));
				j++;
			} // 显示数据
			retsult.close();
			conn.close();// 关闭连接
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(int i=0;i<paras.length;i++)
			for(int k =0;k<paras[0].length;k++)
				System.out.println(paras[i][k]);
		
		return paras;
	}

}