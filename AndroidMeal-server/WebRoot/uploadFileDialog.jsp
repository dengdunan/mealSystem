<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="Script/jquery-1.4.4.js">
</script>

<script src="uploadify-v2.1.4/swfobject.js" type="text/javascript">
</script>
<script src="uploadify-v2.1.4/jquery.uploadify.v2.1.4.min.js"
	type="text/javascript">

</script>
<script type="text/javascript">
////输入一个name值，获得Url参数值
function getUrlParamValue(name) {
	//正则表达式(匹配字符串的开头或者结尾 + 名称  + [开头和结尾])
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	//搜索匹配正则表达式的值
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	} else {
		return null;
	}
}
//文件大小为0.
var fFileSize = 0;
//上传文件的设置属性参数
$(document).ready(
				function() {
					$("#uploadify")
							.uploadify(
									{
										'uploader' : 'uploadify-v2.1.4/uploadify.swf',
										'script' : 'servlet/UploadServlet',
										'cancelImg' : 'uploadify-v2.1.4/cancel.png',
										'buttonImg' : 'images/voice_btn_titlebar_nor.png',
										'method' : 'get',
										'width' : 49,
										'height' : 24,
										'folder' : 'UploadFile',
										'queueID' : 'fileQueue',
										'sizeLimit' : 1024 * 1024 * 100,
										'auto' : true,
										'multi' : false,
										"fileDesc" : "选择文件",
										'fileExt' : '*.png;*.jpg;*.gif;',
										'onSelect' : function(a, b, fileObj) { /*选择文件上传时可以禁用某些按钮*/
											fFileSize = fileObj.size;
										},
										'onComplete' : function(event, queueID,
												fileObj, reposnse, data) {
											$("#div_guid").html(reposnse);
											$("#div_fileName").html("已添加");				
										},
										'onAllComplete' : function(a, b) {
										},
										'onCancel' : function(a, b, c, d, e) {
										},
										'onError' : function(a, b, c, d, e) {
										}
									});
									
//点击上传按钮
$('#btn_upload').click(function() {
//标题的内容是否为空
if ($("#txttitle").val() == "") {
		alert("请输入标题！");
	return;
}
//判断图片是否为空
if($("#div_guid").html() ==""){
alert("请选择图片");
return;
}
//判断视频文件是否为空
if($("#div_gui1").html() ==""){
alert("请选择视频文件");
return;
}
//判断单价是否为空
if ($("#txtprice").val() == "") {
		alert("请输入单价！");
	return;
}
//判断数量是否为空
if ($("#txtamount").val() == "") {
		alert("请输入数量！");
	return;
}
//id参数的值
var id = getUrlParamValue("id");
	if (id == null)
		id = 0;		
//定义相关的参数								
var param = {
        ID:id,
		Action : "edit",
		title : encodeURI($("#txttitle").val()),
		intro : encodeURI($("#txtintro").val()),
		typeid : encodeURI($("#DropDownList1").val()),
		typename : encodeURI($("#DropDownList1").find("option:selected").text()),
		img_url:$("#div_guid").html(),
		price : encodeURI($("#txtprice").val()),
		amount : encodeURI($("#txtamount").val())
	};
	//向服务器请求地址数据
	$.get("servlet/ServletService?ran=" + Math.random(), param, function(data) {
		//如果数据的长度大于0，且数据为1则操作成功
		if (data.length > 0) {	
			if(data==1){
			alert("操作成功");
			location.href="/MealAppService/servlet/GridServlet?Action=getlist&currentpage=1";
			 }
		}
	});
})
});

//获取公用一行数据
$(function() {
	var param = {
		Action : "getOneRow",
		Table : "types"
	};

	$.get("<%=path%>/servlet/ServletService?ran=" + Math.random(), param, function(data) {
		//如果数据的长度大于0
		if (data.length > 0) {
			data = eval("(" + data + ")");
			if (data.length > 0) {
				var ops = "";
				for (i = 0; i < data.length; i++) { 
				 ops += "<option value=" + data[i].id + ">"+ data[i].typename + "</option>";
				}
				
				$("#DropDownList1").append(ops);
				}
				
			}
		
	});


});
//获取到表单中的内容
$(function() {
if(getUrlParamValue("id")!=null){
	var param = {
		Action : "getOneRow",
		Table:"dishes",
		ID:getUrlParamValue("id"),
	};
	$.get("servlet/ServletService?ran=" + Math.random(), param, function(data) {	
		if (data.length > 0) {
			data = eval("(" + data + ")");
			if (data.length > 0) {
				$("#txttitle").val(data[0].title) ;
				$("#txtintro").val(data[0].intro) ;
				$("#DropDownList1").val(data[0].typeid) ;
				$("#div_guid").html(data[0].img_url);	
				$("#txtprice").val(data[0].price) ;
			   $("#txtamount").val(data[0].amount) ;
				
			}
		}
	});
}
})
</script>
<style type="text/css">
.td-left {
	text-align: right;
}

.fileTable {
	margin: 0 auto;
	width: 100%;
}

.fileTable td {
	padding: 3px;
}
</style>
<link rel="stylesheet" href="style/PublicStyle.css" type="text/css"></link>
<script type="text/javascript" src="My97DatePicker/WdatePicker.js">
</script>
</head>
<body>
	<form id="formFileLoad">
		<div class="backbody" style="background: #FFF;">

			<jsp:include page="top.jsp"></jsp:include>

			<div style=" padding:10px;">

				<table border="0" class="fileTable">
					<tr>
						<td class="td-left">菜式标题：</td>
						<td class="td-right"><textarea id="txttitle" rows="2"
								cols="80"></textarea> <span class="red">*</span></td>
					</tr>

					<tr>
						<td class="td-left">菜式简介：</td>
						<td class="td-right"><textarea id="txtintro" rows="4"
								cols="80"></textarea></td>
					</tr>
					<tr>
						<td class="td-left">类型：</td>
						<td class="td-right"><select id="DropDownList1"></select></td>
					</tr>
					<tr>
						<td class="td-left">选择图片：</td>
						<td class="td-right">
							<div id="fileQueue" style="margin-bottom: 10px"></div> <input
							type="file" name="uploadify" id="uploadify" />
							<div id="div_guid" style="display: none;"></div>
							<div id="div_fileName"></div></td>
					</tr>
                    <tr>
						<td class="td-left">单价：</td>
						<td class="td-right"><input id="txtprice" class="textbox" type="text"></input>元<span style="color:red;">*</span></td>
					</tr>
                     <tr>
						<td class="td-left">数量：</td>
						<td class="td-right"><input id="txtamount" class="textbox" type="text"></input>
						  <span style="color:red;">*</span>
						</td>
					</tr>
					<tr>
						<td class="td-left">
						<td><input type="button" id="btn_upload"
							class="btnClass_79px_A" value="保存" /> <input type="button"
							class="btnClass_79px_A" value="返回"
							onclick="javascript:history.go(-1);" /></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</body>
</html>
