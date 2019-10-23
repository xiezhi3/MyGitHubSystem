package com.wn.service;

import com.wn.entity.Users;

public interface UsersService {
	Users login(String username);
	void updateToken(int id,String token);
	String selectToken(int id);
	Users selectByToken(String token);
}
