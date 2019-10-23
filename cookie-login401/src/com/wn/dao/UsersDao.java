package com.wn.dao;

import com.wn.entity.Users;

public interface UsersDao {
	Users selectByUsername(String username);
	void updateToken(int id,String token);
	String selectToken(int id);
	//ͨ��token����usees����
	Users selectByToken(String token);
}
