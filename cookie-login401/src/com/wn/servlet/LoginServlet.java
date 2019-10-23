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
		//������������
		req.setCharacterEncoding("utf-8");
		//����û���½������û���������
		String username = req.getParameter("username");
		String pwd = req.getParameter("pwd");
		HttpSession session = req.getSession();
		Object object = session.getAttribute("users");
		//�ж�session�������Ƿ���users����
		if(object != null){
			//����ж���ֱ����ת����ҳ����,��������ȥ�ж�
			res.sendRedirect("home.jsp");
		}else{
			//���usersΪ�գ��϶���û�е�½��ֱ���ж��û��������룬������ ��cookie�����ڵ�½ҳ������
			UsersDao ud = new UsersDaoImpl();
			//���û�������users����
			Users users = ud.selectByUsername(username);
			//ͨ���ж�users�Ƿ�������ж��˻����Ƿ���ȷ
			if(users == null){
				req.setAttribute("message", "���û���������");
				//ת������½����
				req.getRequestDispatcher("login.jsp").forward(req, res);
			}else{
				//�ж������Ƿ���ȷ
				if(users.getPassword().equals(pwd)){
					//�˻��������붼��ȷ���ı��������ݿ��tokenֵ,��users���󱣴浽session����,��token��ӵ��������cookie����
					//����һ���������ΪΨһ��tokenֵ
					String token = UUID.randomUUID().toString();
					//��tokenֵ��ӵ�users�����ݿ�����
					ud.updateToken(users.getId(), token);
					users.setToken(token);
					//��users������ӵ�session��������ȥ
					session.setAttribute("users",users);
					//��tokenֵ���浽cookie����ȥ
					Cookie cookie = new Cookie("token",token);
					//����cookie����Ч����
					cookie.setMaxAge(7*24*60*60);
					//��Ӧ�������
					res.addCookie(cookie);
					//�ض���home.jspҳ��
					res.sendRedirect("home.jsp");
				}else{
					//�������ת������½����
					req.setAttribute("message","�������");
					req.getRequestDispatcher("login.jsp").forward(req, res);
				}
			}
		}
	}
}
