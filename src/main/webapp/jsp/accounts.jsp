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
		<title>账户管理首页</title>		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
	     
	  } );
    
      function reAccFlash(){
         //reload:重新执行url，condition是url中的参数  
            $("#searchAccFm").form("clear");  
            $("#AccDg").datagrid({
 	           url:"accountsSx.action"      	     
 	         }); 
            
        }
      function doAccSearch(){
	      $("#AccDg").datagrid({
	           url:"accountsCx.action",      
	           queryParams: {  
	        	   username: $("#username1").val(),       
	          }  
	      });  
	   } 
    function addAccWin(){
    	  $("#AccDg").datagrid("uncheckAll"); 
    	  $("#id").val(0);
    	  $("#username").val("");
         $( "#addAcc" ).window("open").window("setTitle", "新增"); 
      }
    function closeAccWin(){
         $( "#addAcc" ).window("close"); 
      }

    function saveAcc(){
	     var grid = $("#AccDg"); 
	     var fm = $("#AccFm");	   
	     var addWin = $( "#addAcc" ); 	    
      	 commonSaveOperate(fm,"${pageContext.request.contextPath}/addAccount.action",addWin,grid);  
      	 
     }
    
    function deleteAcc() {
        var grid = $("#AccDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delAccount.action","确认删除所选数据吗？");
     }
    
    function editAccWin(){
	     var selectedRows = $("#AccDg").datagrid("getSelections");
	     if (selectedRows.length != 1) {
	         $.messager.alert("系统提示", "请选择一条要编辑的数据！");
	         return;
	     }
	     var row = selectedRows[0];
	     var stype = row.status;
	     if(stype =='0'){
	     	 $.messager.alert("系统提示", "用户已禁用，不能修改！");
	         return;
	     }
	     $( "#addAcc" ).window("open").window("setTitle", "编辑用户信息");
	     $("#AccFm").form("load", row);
     }
    
    
    function gzAcc() {
   	    var grid = $("#AccDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/upAccountZt.action?status=0","确认更改所选数据状态吗？");
   }
    
    function hfAcc() {
   	    var grid = $("#AccDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/upAccountZt.action?status=1","确认更改所选数据状态吗？");
   }
     
    
    </script>  
	<body class="easyui-layout">
		<table id="AccDg" title="用户列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="accountsSx.action"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<!-- <th field="id" width="50">id</th> -->
					<th field=roleSign width="100">用户权限</th>
					<th field="username" width="200">用户名</th>
					<th field="cjsj" width="150">创建时间</th>
					<th field="status" width="100"  formatter="formatJy">是否禁用</th>

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<div style="padding:3px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addAccWin()">新增用户</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit"  onclick="editAccWin()">修改用户</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteAcc()">删除用户</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="gzAcc()">用户禁用</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="hfAcc()">用户恢复</a>
		    </div>
		    <div style="padding:3px">
		        <form id="searchAccFm">  
					<span>用户名:</span>
					<input id="username1" name="username1" style="line-height:18px;border:1px solid #95b9e7">&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doAccSearch()">搜索</a>&nbsp;&nbsp;				
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reAccFlash()">刷新</a>
				 </form>
			</div>
		</div>
       <div id="addAcc" class="easyui-window" title="用户新增"  closed = "true" style="width:400px;height:200px;">
			 <form method="post" id="AccFm" text-align:center>
                <table cellspacing="8px;"> 
                   <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr>   
                    <tr> 
                        <td>身份：</td>
                        <td> 
                        <input id="roleId" name="roleId" required="true" class="easyui-combobox" data-options="    
					            valueField: 'id',    
						        textField: 'roleSign',    
						        url: 'roleList.action',
						        method:'get', 
                                panelHeight:'150',
                                onSelect: function(rec){    
						             
						        }  "/> &nbsp;<span style="color: red">*</span>
                        </td>
                    </tr>               
                    <tr>
                        <td>用户名：</td>
                        <td><input type="text" id="username" name="username"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>                           
                        
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveAcc()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closeAccWin()">取消</a>
				</div>
			</form>
	  </div>	  
	 <style>
	  	#AccFm{
	  		margin:0 auto !important;
	  		width: 300px
	  	}
	  	
	  	 .datagrid-btable tr{height: 32px;}
	  	  	
	  </style>
  </body>

	
</html>
