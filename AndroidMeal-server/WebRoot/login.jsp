<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	//获得请求的路径
	String path = request.getContextPath();
	//获得基本路径(即服务器名，端口和路径等)
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//获得会话内容
	HttpSession session11 = request.getSession(true);
	//会话中移除登录iD的属性
	session11.removeAttribute("LoginID");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>登录</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">


<link rel="stylesheet" href="style/Style_Login.css" type="text/css"></link>
<link rel="stylesheet" href="style/PublicStyle.css" type="text/css"></link>
<script type="text/javascript" src="Script/jquery-1.4.4.js"></script>
<Script type="text/javascript">
	$(function() {
		//点击登录按钮
		$("#button1")
				.click(
						function() {
							//如果登录名为空，则弹出对话框
							if ($("#txtLoginID").val() == "") {
								alert("请输入登录名");
								return;
							}
							//如果密码为空，则弹出对话框
							if ($("#txtPassWord").val() == "") {
								alert("请输入密码");
								return;
							}

							//实例化登录名和密码参数
							var param = {
								Action : "adminlogin",
								loginid : $("#txtLoginID").val(),
								passwords : $("#txtPassWord").val()
								
							};
							//像服务器请求参数
							$.get(
											"servlet/ServletService?ran="
													+ Math.random(),
											param,
											function(data) {
												//如果data为1，则跳转页面，否则弹出登录失败对话框
												if (data == 1) {
													location.href = "/MealAppService/servlet/GridServlet?Action=getlist&currentpage=0";
												} else {
													alert("登录失败");
												}
											});
						});
	})
</Script>
</head>

<body
	>
	<div style="background: url(images/b_login_bg.jpg) center; background-repeat: no-repeat; width: 1000px; height: 600px; margin: 0 auto; position: absolute; left:expression((document.body.clientWidth-this.offsetWidth)/2); top:expression((document.body.clientHeight-this.offsetHeight)/2)">
		<table border="0" cellspacing="10"
			style= "position: relative; z-index:1; left:600; top:200 ">
			<tr>
				<td>登录名：</td>
				<td class="td_right"><input id="txtLoginID" type="text"
					class="txt" value=""></td>
			</tr>
			<tr>
				<td>密 码：</td>
				<td class="td_right"><input id="txtPassWord" type="password"
					class="txt" value=""></td>
			</tr>
			
			<tr>
				<td></td>
				<td class="td_right" valign="top"><input id="button1"
					type="button" class="btnClass_79px_A" value="登录"></td>
			</tr>
		</table>
	</div>
</body>
</html>
