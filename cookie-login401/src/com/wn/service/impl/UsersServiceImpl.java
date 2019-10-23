package com.wn.service.impl;

import com.wn.dao.UsersDao;
import com.wn.dao.impl.UsersDaoImpl;
import com.wn.entity.Users;
import com.wn.service.UsersService;

public class UsersServiceImpl implements UsersService{
	private UsersDao dao=new UsersDaoImpl();
	@Override
	public Users login(String username) {
		return dao.selectByUsername(username);
	}
	@Override
	public void updateToken(int id, String token) {
		dao.updateToken(id, token);
	}
	@Override
	public String selectToken(int id) {
		return dao.selectToken(id);
	}
	@Override
	public Users selectByToken(String token) {
		return dao.selectByToken(token);
	}
}
