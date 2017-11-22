<%@ page session="false" language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>登录首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	</head>
	<body>

	   <div id="loginWin" title="请先登录" class="easyui-window" data-options="modal:true,closed:true,iconCls:'icon-login',closable:false,minimizable:false" style="width:400px;padding:20px 70px 20px 70px;">
         <form id="logFm" action="${pageContext.request.contextPath}/shiro-getLogin.action" method="post">
	        <div style="margin-bottom:10px">
	             <span>登录账号：<input class="easyui-textbox" id="logName" name="logName" style="width:68%;height:20px;padding:2px" data-options="prompt:'登录账号',iconCls:'icon-users2',iconWidth:28 "></span>
	        </div>
	        <div style="margin-bottom:20px">
	             <span>登录密码：</span><input class="easyui-textbox" id="logPass" name="logPass" type="password" style="width:68%;height:20px;padding:2px" data-options="prompt:'登录密码',iconCls:'icon-pass',iconWidth:28 ">
	        </div>
	        <div style="margin-bottom:20px">
	              <span>验证码 ：</span><input class="easyui-textbox" type="text" id="logYzm" name="logYzm" style="width:30%;height:20px;padding:2px" data-options="prompt:'验证码'"> 
	            <a href="javascript:;" class="showcode" onclick="changImg()">
	            	<img id ="servletImg" src="${pageContext.request.contextPath}/code/shiro-getCode.action" style=" margin:0 0 0 3px ; vertical-align:middle; height:26px;width:100px;" >
	            </a>
	        </div>   
	        <div>
	            <a href="javascript:;" onclick="dologin()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="padding:5px 0px;width:100%;">
	                <span style="font-size:14px;">登录</span>
	            </a>
	        </div>
	     </form>   
       </div>
	</body>
	<script type="text/javascript">

		$(function(){
			  $( "#loginWin" ).window("open");  
			
		})
		
		 function changImg(){
		   /* var img = document.getElementById("servletImg");
		   var d = new Date();
		   var time = d.getTime();//如果没有这个
		   //下面这一句不会起作用，因为浏览器的缓存技术，图片并不会刷新
		   //img.src="/myHelloWeb/servlet/ImageServlet";
		   img.src="/codeImg?"+time;
		   //?号后面的东西是通过get方式传递的 */
		   $("#servletImg").attr("src", "${pageContext.request.contextPath}/code/shiro-getCode.action?r=" + Math.random());
  		}
		
		function checkLogValue(){
			var name = $.trim($("#logName").val()); 
			var pass = $.trim($("#logPass").val()); 
			var yzm = $.trim($("#logYzm").val());
			if(name.replace(/(^\s*)|(\s*$)/g, "").length ==0){
				$.messager.alert("系统提示", "登录名不能为空！");
				return false;
			}
			if(pass.replace(/(^\s*)|(\s*$)/g, "").length ==0){
				$.messager.alert("系统提示", "密码不能为空！");
				return false;
			}
			if(yzm.replace(/(^\s*)|(\s*$)/g, "").length ==0){
				$.messager.alert("系统提示", "验证码不能为空！");
				return false;
			}
			return true;
		}
		
		function dologin(){
			if(checkLogValue()){ 
				$("#logFm").submit();	       
			}
		}
		
 
	</script>
	
</html>
