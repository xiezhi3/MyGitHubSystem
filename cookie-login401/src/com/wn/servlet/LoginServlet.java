package com.wn.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wn.dao.UsersDao;
import com.wn.dao.impl.UsersDaoImpl;
import com.wn.entity.Users;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//处理中文乱码
		req.setCharacterEncoding("utf-8");
		//获得用户登陆传入的用户名和密码
		String username = req.getParameter("username");
		String pwd = req.getParameter("pwd");
		HttpSession session = req.getSession();
		Object object = session.getAttribute("users");
		//判断session对象中是否有users对象
		if(object != null){
			//如果有对象，直接跳转到主页界面,过滤器再去判断
			res.sendRedirect("home.jsp");
		}else{
			//如果users为空，肯定是没有登陆，直接判断用户名和密码，不考虑 有cookie但是在登陆页面的情况
			UsersDao ud = new UsersDaoImpl();
			//用用户名创建users对象
			Users users = ud.selectByUsername(username);
			//通过判断users是否存在先判断账户名是否正确
			if(users == null){
				req.setAttribute("message", "该用户名不存在");
				//转发到登陆界面
				req.getRequestDispatcher("login.jsp").forward(req, res);
			}else{
				//判断密码是否正确
				if(users.getPassword().equals(pwd)){
					//账户名和密码都正确，改变对象和数据库的token值,把users对象保存到session里面,把token添加到浏览器的cookie里面
					//创建一个随机数作为唯一的token值
					String token = UUID.randomUUID().toString();
					//将token值添加到users和数据库里面
					ud.updateToken(users.getId(), token);
					users.setToken(token);
					//将users对象添加到session对象里面去
					session.setAttribute("users",users);
					//把token值保存到cookie里面去
					Cookie cookie = new Cookie("token",token);
					//设置cookie的有效期限
					cookie.setMaxAge(7*24*60*60);
					//响应到浏览器
					res.addCookie(cookie);
					//重定向到home.jsp页面
					res.sendRedirect("home.jsp");
				}else{
					//密码错误，转发至登陆界面
					req.setAttribute("message","密码错误");
					req.getRequestDispatcher("login.jsp").forward(req, res);
				}
			}
		}
	}
}
