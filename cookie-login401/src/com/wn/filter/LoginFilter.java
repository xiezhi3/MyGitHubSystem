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
	//定义集合保存请求放行白名单
	private static List<String> ps = new ArrayList<String>();
	private static List<String> ss = new ArrayList<String>();
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		//servletRequest和ServletResponse向下转型，
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		//获取真实的请求地址
		String uri = request.getRequestURI();
		//取出请求地址中的资源名称
		String resourceURI = uri.substring(uri.lastIndexOf("/")+1);
		//定义前缀变量和后缀变量用于存储本次请求的前后缀
		String prefix = null;
		String suffix = null;
		//通过.的位置，把上线的资名称分别存入字符串
		int index = resourceURI.lastIndexOf(".");
		if(index == -1){
			//未找到，请求地址无后缀
			prefix = resourceURI;
		}else{
			prefix = resourceURI.substring(0,index);
			suffix = resourceURI.substring(index+1);
		}
		//创建数据层对象
		UsersDao ud = new UsersDaoImpl();
		
		if(ps.contains(prefix) || ss.contains(suffix)){
			//如果查询出来的地址是在白名单里面，就直接放行
			chain.doFilter(request, response);
		}else{
			//验证session中的user以及cookie中的token来判断放行还是跳转登陆界面
			HttpSession session = request.getSession();
			Object object = session.getAttribute("users");
			UsersService us = new UsersServiceImpl();
			//判断object是否为空，作为先行判断
			if(object != null){
				//用户存在，直接判断对象token是不是数据库的token一致
				Users users = (Users)object;
				//通过用户的id查询数据库的token，来与对象的token作为对比
				if(users.getToken().equals(ud.selectToken(users.getId()))){
					//放行
					chain.doFilter(request, response);
				}else{
					session.setAttribute("message","此账户已在其他地方登陆，请重新登陆");
					//跳转到登陆界面，顺便把他的users干掉
					session.removeAttribute("users");
					//重定向到登陆界面
					response.sendRedirect("login.jsp");
				}
			}else{
				//用户不存在，就通过cookie值来判断用户是不是已经登陆过，还在有效期内
				//得到通过request对象得到cookie的数组
				Cookie[] cookies = request.getCookies();
				String token = null;
				//如果cookies数组不为空再进行下面步奏，不然有空指向异常风险
				if(cookies!=null){
					//遍历cookie数组，来查找是否有键是“token”的对象
					for(Cookie cookie : cookies){
						if(cookie.getName().equals("token")){
							//找到对象，保存并退出
							token = cookie.getValue();
							break;
						}
					}
				}
				if(token == null){
					//根本就没找到cookie值，直接重定向到登陆界面，让他先登陆
					request.setAttribute("message","请先登录");
					response.sendRedirect("login.jsp");
				}else{
					//用这个token和数据库的token作对比，看是不是当前可登陆用户
					//先用token去数据库查找对象是否存在
					Users user =  us.selectByToken(token);
					if(user == null){
						request.setAttribute("message","用户在其他地方登陆");
						//把cookie干掉
						response.sendRedirect("login.jsp");
					}else{
						//直接把user对象保存到session会话对象中，
						session.setAttribute("users",user);
						//放行
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
		//添加白名单信息
		ps.add("login");
		
		ss.add("jpg");
		ss.add("png");
		ss.add("gif");
		ss.add("css");
		ss.add("js");
	}
}
