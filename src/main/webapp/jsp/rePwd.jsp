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
		<title>重新修改密码</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
		  pwdWin();
	  } );


    function pwdWin(){
         $( "#upPwd" ).window("open"); 
      }
    
    function closepwdWin(){
        $( "#upPwd" ).window("close");       
        $("#pwdFm").form("clear"); 
     }

    function checkPwd(){
       var p1 =  $("#password").val(); 
       var p2 =  $("#pwd2").val(); 
       if(p1!=p2){
       //alert(p1+"-----"+p2);
       	 $.messager.alert("系统提示", "两次密码不一致！");
       	 return false;
       }else{
         return true;
       }
     }
    
    
    function savePwd(){
    	if(checkPwd()){
    	var fm = $("#pwdFm");
    	var data = fm.serialize();
    	var url = "${pageContext.request.contextPath}/upHtPwd.action";
        if(fm.form("validate")){ 
              $.ajax({  
                      type: "post" ,  
                      url: url ,   
                      cache:false ,  
                      data:data ,  
                      dataType:"json" , 
                      /* contentType:"application/json;charset=utf-8",  */
                      success:function(result){  
                    	  $("#pwdFm").form("clear"); 
                          //3 提示信息
                          $.messager.alert(result.status, result.message);                        
                      } ,  
                      error:function(result){                             
                          $.messager.alert(result.status, result.message);   
                      }  
                  });  
              } else {  
                  $.messager.alert("系统提示", "数据验证不通过，请注意！");
              } 
    	}
    }
     

    
    </script>  
	<body class="easyui-layout">
  
	  <div id="upPwd" class="easyui-window" title="密码重置"  closed = "true" style="width:400px;height:300px;">
			 <form method="post" id="pwdFm" text-align:center>
                <table cellspacing="8px;">               
                    <tr>                       
                        <td>新密码：</td>
                        <td><input type="password" id="password" name="password"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>
                    <tr>                       
                        <td>密码确认：</td>
                        <td><input type="password" id="pwd2" name="pwd2"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>
                        
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="savePwd()">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closepwdWin()">取消</a>
				</div>
			</form>
	  </div>
	
	<style>
	  	#fm{
	  		margin:0 auto !important;
	  		width: 400px
	  	}
	  	#pwdFm{
	  		margin:0 auto !important;
	  		width: 300px
	  	}
	  
	  	
	  </style>
  </body>

	
</html>
