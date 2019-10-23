<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h5>${message}</h5>
	<form action="login" method="post">
		<input placeholder="请输入用户名" name="username">
		<input type="password" placeholder="请输入密码" name="pwd">
		<input type="submit" value="登陆">
	</form>
</body>
</html>