package com.wn.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wn.dao.UsersDao;
import com.wn.entity.Users;
import com.wn.util.DBUtil;

public class UsersDaoImpl implements UsersDao {
	@Override
	public Users selectByUsername(String username) {
		Connection conn=DBUtil.getCon();
		PreparedStatement ps=null;
		ResultSet rs=null;
		Users users=null;
		try {
			 ps= conn.prepareStatement("select * from users1 where username=?");
			 ps.setString(1, username);
			 rs=ps.executeQuery();
			 if(rs.next()) {
				 users=new Users();
				 users.setId(rs.getInt("id"));
				 users.setUsername(rs.getString("username"));
				 users.setPassword(rs.getString("password"));
				 users.setToken(rs.getString("token"));
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users;
	}

	
	@Override
	public void updateToken(int id, String token) {
		Connection con = DBUtil.getCon();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update users1 set token = ? where id = ?");
			ps.setString(1, token);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				DBUtil.close(con);
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String selectToken(int id) {
		Connection con = DBUtil.getCon();
		PreparedStatement ps = null;
		String token = null;
		try {
			ps = con.prepareStatement("select token from users1 where id = ?");
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				token = rs.getString("token");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(con);
		}
		return token;
	}


	@Override
	public Users selectByToken(String token) {
		Connection con = DBUtil.getCon();
		Users users = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from users1 where token=?");
			ps.setString(1,token);
			rs = ps.executeQuery();
			if(rs.next()){
				users = new Users();
				users.setId(rs.getInt("id"));
				users.setUsername(rs.getString("username"));
				users.setPassword(rs.getString("password"));
				users.setToken(rs.getString("token"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users;
	}
	
	
}
