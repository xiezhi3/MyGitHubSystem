package com.wn.dao;

import com.wn.entity.Users;

public interface UsersDao {
	Users selectByUsername(String username);
	void updateToken(int id,String token);
	String selectToken(int id);
	//通过token查找usees对象
	Users selectByToken(String token);
}
