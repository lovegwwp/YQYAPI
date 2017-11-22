<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
		<title>未授权页面</title>
		<link rel="icon" href="img/mst.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="img/mst.ico" type="image/x-icon" />
 
<script type="text/javascript">
  
 
setTimeout("javascript:location.href='http://localhost:8080/SSM/login.jsp'", 3000);
</script>
 
</head>
<body>
<div align="center">
    <br/>
    <br/>
    <br/>
    <h2>对不起，您尚未授权！</h2>
    <h4><a href="http://localhost:8080/SSM/login.jsp" >立即跳转</a></h4>
</div>
  
</body>
</html>
