package com.wn.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static String clsName = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/users?characterEncoding=utf8";
	private static String user = "root";
	private static String pwd = "123456";
	
	static {
		// 加载驱动
		try {
			Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			System.err.println("加载驱动失败！");
			e.printStackTrace();
		}
	}
	
	// 获取连接
	public static Connection getCon() {
		Connection con = null; 
		try {
			con = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			System.err.println("获取连接失败！");
			e.printStackTrace();
		}
		return con;
	}
	
	// 关闭连接
	public static void close(Connection con) {
		try {
			if(con!=null) con.close();
		} catch (SQLException e) {
			System.err.println("连接关闭失败！");
			e.printStackTrace();
		}
	}
}
