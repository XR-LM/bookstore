<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- saved from url=(0060)http://hovertree.com/login.php?gotopage=index.php -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<link type="text/css" rel="stylesheet"
	href="/bookstore/CSS/login.css"/>
<script type="text/javascript"
	src="http://hovertree.com/ziyuan/jquery/jquery-1.11.3.min.js"></script>
<script type="text/javascript"
	src="http://hovertree.com/texiao/jquery/13/login.js"></script>
<script type="text/javascript">
function _change(){
	var image=document.getElementById("imgHoverTreeCode");
	image.src="<c:url value='/VerifyCodeServlet'/>?a="+new Date().getTime();
}
</script>
</head>
<body style="overflow:hidden">
	<div class="pagewrap">
		<div class="main">
			<div class="header">
				<div style="width:768px;margin:0px auto;">
				</div>
			</div>
			<div class="content">
				<div class="con_left"></div>
				<div class="con_right">
					<div class="con_r_top">
						<a href="javascript:;" class="right"
							style="color: rgb(51, 51, 51); border-bottom-width: 2px; border-bottom-style: solid; border-bottom-color: rgb(46, 85, 142);">登录管理</a>
					</div>
					<ul>
						<li class="con_r_right" style="display: block;">
							<form name="form1" action="<c:url value='/UserServlet'/>" method="post" target="_top">
								<input type="hidden" name="method" value="login"></input>
								<div class="user">
									<div>
										<span class="user-icon"></span> <input type="text" id="userid"
											name="username" placeholder="　输入账号" value="${user.username }">
									</div>

									<div>
										<span class="mima-icon"></span> <input type="password"
											id="pwd" name="password" placeholder="　输入密码" value="${user.password }">
									</div>

									<div>
										<span class="yzmz-icon"></span> <input id="vdcode" type="text"
											name="validate" placeholder="　验证码" value=""
											style=" width:95px;">
										<!-- 这里是验证码的相关路径，各位站长自行更换 -->

										<img style="cursor: pointer; margin-top:-6px;"
											id="imgHoverTreeCode"
											src="<c:url value='/VerifyCodeServlet' />">
										<a href="javascript:_change()">换一张</a>
									</div>
									<div style="height:20px">	<p style="color: red; font-weight: 900">${msg }</p></div>
								</div>
								<br>
								<button id="btn_Login" type="submit">登 录</button>
							</form></li>
					</ul>

				</div>

			</div>
		</div>
	</div>

</body>
</html>
