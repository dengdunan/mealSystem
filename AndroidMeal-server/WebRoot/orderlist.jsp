<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	//获得请求的路径
	String path = request.getContextPath();
	//获得基本路径(即服务器名，端口和路径等)
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">

<title></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="style/PublicStyle.css" type="text/css"></link>
<script type="text/javascript" src="Script/jquery-1.4.4.js">
</script>
<script type="text/javascript">
//删除点餐订单的函数
function fndelete(id) {
	//是否确认删除当前记录
	if (confirm("确定删除当前记录吗？")) {
		//请求服务器servlet/ServletService?Ran=地址
		$.get("servlet/ServletService?Ran=" + Math.random(), {
			Action : "Del",
			ID : id,
			Table : "orders"
		}, function(data) {
			//如果data为1，则删除成功，否则弹出对不起,删除失败,请稍后再试的对话框
			if (data == 1) {
				alert("恭喜您,删除成功！");
				location.href = location.href;
			} else {
				alert("对不起,删除失败,请稍后再试！");
			}

		});
	}
}
//改变订单状态值
function changestatus(id,status) {
		//请求服务器servlet/ServletService?Ran=地址
		$.get("servlet/ServletService?Ran=" + Math.random(), {
			Action : "ChangeStatus",
			ID : id,
			status : status
		}, function(data) {
			//如果data为1，则操作成功，否则弹出对不起,操作失败,请稍后再试的对话框(即改变状态值成功)
			if (data == 1) {
				alert("恭喜您,操作成功！");
				location.href = location.href;
			} else {
				alert("对不起,操作失败,请稍后再试！");
			}

		});
	
}

//执行搜索订单操作
function search100() {
	//如果当前关键值为空
	if ($("#txtkeyword").val() == "") {
		location.href = "/MealAppService/servlet/GridServlet?Action=getuserlist&currentpage=0";
	} else {
         location.href = "/MealAppService/servlet/GridServlet?Action=getuserlist&currentpage=0"+ "&msg="
				+ encodeURI($("#txtkeyword").val());	
	}
}
</script>

</head>
<body>
	<div class="backbody" style="background: #FFF;">

		<jsp:include page="top.jsp"></jsp:include>

		<div style=" padding:10px;">

			<div style="padding: 15px 5px 10px 0;">
<table class="SearchTable" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td>
								姓名：
							</td>
							<td>
								<input type="text" class="textbox" id="txtkeyword" />
							</td>
							<td>
								<input type="button" class="btnClass_79px_A" value="搜索"
									onclick="javascript:search100();" />
							</td>
							<td>
								<input type="button" class="btnClass_79px_A" value="添加" style="display: none;"
									onclick="javascript:location.href='/MealAppService/userinfo.jsp'" />

							</td>
						</tr>
					</table>
				<table width="940px;" class="GridTable" border="0" cellspacing="0"
					cellpadding="0" id="GridTableID">

					<tr>
						<th>用户</th>
						<th>所点菜式</th>
						<th>数量</th>
						<th>单价</th>
						<th>总额</th>
						<th>座位</th>
						<th>订单状态</th>
						<th>设为已完成</th>
						<th>设为已取消</th>
					
						
						<th class="right">删除</th>
					</tr>
					<c:forEach var="data" items="${datalist}">
						<tr>


							<td>${data.username}</td>
							<td>${data.title}</td>
							<td>${data.amount}</td>
							<td>${data.price}</td>
							<td>${data.total}</td>
							<td>${data.seat}</td>
							<td>${data.status1}</td>
							<td><a href='javascript:void(0);' style="text-decoration: underline;" onclick='javascript:changestatus(${data.id},1);'>设为已完成</a></td>
							<td><a href='javascript:void(0);' style="text-decoration: underline;" onclick='javascript:changestatus(${data.id},-1);'>设为已取消</a></td>
							
							<td class="right"><input id='${data.id}'
								onclick='javascript:fndelete(${data.id})' title="删除"
								type="button" value="" class="btnDel  btnGrid"
								style="background: url(images/delete.gif)" /></td>
						</tr>
					</c:forEach>
				</table>
				<div style="margin-top: 5px;">
					<a href="servlet/GridServlet?Action=getuserlist&currentpage=${1}">首页</a>
					<a
						href="servlet/GridServlet?Action=getuserlist&currentpage=${currentpage-1}">上一页</a>
					<a
						href="servlet/GridServlet?Action=getuserlist&currentpage=${currentpage+1}">下一页</a>
					<a
						href="servlet/GridServlet?Action=getuserlist&currentpage=${pagecount}">尾页</a>
					当前页是第${currentpage}/${pagecount}页,共有${total}条记录
				</div>
			</div>
		</div>
	</div>
</body>
</html>
