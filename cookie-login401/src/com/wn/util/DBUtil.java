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
		// ��������
		try {
			Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			System.err.println("��������ʧ�ܣ�");
			e.printStackTrace();
		}
	}
	
	// ��ȡ����
	public static Connection getCon() {
		Connection con = null; 
		try {
			con = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			System.err.println("��ȡ����ʧ�ܣ�");
			e.printStackTrace();
		}
		return con;
	}
	
	// �ر�����
	public static void close(Connection con) {
		try {
			if(con!=null) con.close();
		} catch (SQLException e) {
			System.err.println("���ӹر�ʧ�ܣ�");
			e.printStackTrace();
		}
	}
}
