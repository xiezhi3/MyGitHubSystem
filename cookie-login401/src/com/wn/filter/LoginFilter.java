package com.wn.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wn.dao.UsersDao;
import com.wn.dao.impl.UsersDaoImpl;
import com.wn.entity.Users;
import com.wn.service.UsersService;
import com.wn.service.impl.UsersServiceImpl;

@WebFilter("*")
public class LoginFilter implements Filter{
	//���弯�ϱ���������а�����
	private static List<String> ps = new ArrayList<String>();
	private static List<String> ss = new ArrayList<String>();
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		//servletRequest��ServletResponse����ת�ͣ�
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		//��ȡ��ʵ�������ַ
		String uri = request.getRequestURI();
		//ȡ�������ַ�е���Դ����
		String resourceURI = uri.substring(uri.lastIndexOf("/")+1);
		//����ǰ׺�����ͺ�׺�������ڴ洢���������ǰ��׺
		String prefix = null;
		String suffix = null;
		//ͨ��.��λ�ã������ߵ������Ʒֱ�����ַ���
		int index = resourceURI.lastIndexOf(".");
		if(index == -1){
			//δ�ҵ��������ַ�޺�׺
			prefix = resourceURI;
		}else{
			prefix = resourceURI.substring(0,index);
			suffix = resourceURI.substring(index+1);
		}
		//�������ݲ����
		UsersDao ud = new UsersDaoImpl();
		
		if(ps.contains(prefix) || ss.contains(suffix)){
			//�����ѯ�����ĵ�ַ���ڰ��������棬��ֱ�ӷ���
			chain.doFilter(request, response);
		}else{
			//��֤session�е�user�Լ�cookie�е�token���жϷ��л�����ת��½����
			HttpSession session = request.getSession();
			Object object = session.getAttribute("users");
			UsersService us = new UsersServiceImpl();
			//�ж�object�Ƿ�Ϊ�գ���Ϊ�����ж�
			if(object != null){
				//�û����ڣ�ֱ���ж϶���token�ǲ������ݿ��tokenһ��
				Users users = (Users)object;
				//ͨ���û���id��ѯ���ݿ��token����������token��Ϊ�Ա�
				if(users.getToken().equals(ud.selectToken(users.getId()))){
					//����
					chain.doFilter(request, response);
				}else{
					session.setAttribute("message","���˻����������ط���½�������µ�½");
					//��ת����½���棬˳�������users�ɵ�
					session.removeAttribute("users");
					//�ض��򵽵�½����
					response.sendRedirect("login.jsp");
				}
			}else{
				//�û������ڣ���ͨ��cookieֵ���ж��û��ǲ����Ѿ���½����������Ч����
				//�õ�ͨ��request����õ�cookie������
				Cookie[] cookies = request.getCookies();
				String token = null;
				//���cookies���鲻Ϊ���ٽ������沽�࣬��Ȼ�п�ָ���쳣����
				if(cookies!=null){
					//����cookie���飬�������Ƿ��м��ǡ�token���Ķ���
					for(Cookie cookie : cookies){
						if(cookie.getName().equals("token")){
							//�ҵ����󣬱��沢�˳�
							token = cookie.getValue();
							break;
						}
					}
				}
				if(token == null){
					//������û�ҵ�cookieֵ��ֱ���ض��򵽵�½���棬�����ȵ�½
					request.setAttribute("message","���ȵ�¼");
					response.sendRedirect("login.jsp");
				}else{
					//�����token�����ݿ��token���Աȣ����ǲ��ǵ�ǰ�ɵ�½�û�
					//����tokenȥ���ݿ���Ҷ����Ƿ����
					Users user =  us.selectByToken(token);
					if(user == null){
						request.setAttribute("message","�û��������ط���½");
						//��cookie�ɵ�
						response.sendRedirect("login.jsp");
					}else{
						//ֱ�Ӱ�user���󱣴浽session�Ự�����У�
						session.setAttribute("users",user);
						//����
						chain.doFilter(request,response);
					}
				}
			}
		}
		
	}

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//��Ӱ�������Ϣ
		ps.add("login");
		
		ss.add("jpg");
		ss.add("png");
		ss.add("gif");
		ss.add("css");
		ss.add("js");
	}
}
