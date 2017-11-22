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
		<title>常量管理首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
	     
	  } );
    
      function reClFlash(){
         //reload:重新执行url，condition是url中的参数  
            $("#searchClFm").form("clear");  
           // $("#clDg").datagrid("reload");  
             $("#clDg").datagrid({
	           url:"getCls.action"      	     
	         });         
        }
      function doClSearch(){
	      $("#clDg").datagrid({
	           url:"getClsBy.action",      
	           queryParams: {  
	              bz_type: $("#bz_type1").val(),  
	              bz_value: $("#bz_value1").val()      
	          }  
	      });  
	   } 
    function addClWin(){
    	$("#clDg").datagrid("uncheckAll");         
        $( "#addCl" ).window("open").window("setTitle", "新增");
        // $( "#addCl" ).window("open"); 
      }
    function closeClWin(){
         $( "#addCl" ).window("close");
         $("#clFm").form("clear"); 
      }

    function saveCl(){
	     var grid = $("#clDg"); 
	     var fm = $("#clFm");	   
	     var addWin = $( "#addCl" );  
      	 commonSaveOperate(fm,"${pageContext.request.contextPath}/addCl.action",addWin,grid); 
      	 //setClear();     	     
     }
    
    function deleteCl() {
        var grid = $("#clDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delCl.action","确认删除所选数据吗？");
     }
    
    function editClWin(){
       var grid = $("#clDg"); 
	   var fm = $("#clFm");
	   var addWin = $( "#addCl" );  
       openEditWin(grid,addWin,fm);
     }
     
     function setClear(){
     	$("#bz_type").val("");
	    $("#bz_id").val("");
	    $("#bz_value").val("");
	    $("#pid").val("");
     }
    
    </script>  
	<body class="easyui-layout">
		<table id="clDg" title="常量列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="getCls.action"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<!-- <th field="id" width="50">id</th> -->
					<th field="bz_type" width="100">标志类型</th>
					<th field="bz_id" width="80">标志id</th>
					<th field="bz_value" width="150">对应值</th>
					<th field="pid" width="80">父级</th>
					<th field="status" width="80">禁用状态</th>

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<div style="padding:3px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addClWin()">新增常量</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit"  onclick="editClWin()">修改常量</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteCl()">删除常量</a>
		    </div>
		    <div style="padding:3px">
		        <form id="searchClFm">  
					<span>标志类型:</span>
					<input id="bz_type1" name="bz_type1" style="line-height:18px;border:1px solid #95b9e7">&nbsp;&nbsp;
					<span>对应值：</span>
					<input id="bz_value1" name="bz_value1" style="line-height:18px;border:1px solid #95b9e7">
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doClSearch()">搜索</a>&nbsp;&nbsp;
					<!--  <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="reFlash()">reFlash</a>-->
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reClFlash()">刷新</a>
				 </form>
			</div>
		</div>
       <div id="addCl" class="easyui-window" title="新增常量"  closed = "true" style="width:600px;height:400px;">
			 <form method="post" id="clFm" text-align:center>
                <table cellspacing="8px;"> 
                   <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr>              
                    <tr>
                        <td>标志类型：</td>
                        <td><input type="text" id="bz_type" name="bz_type"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>
                     <tr>
                        <td>标志ID：</td>
                        <td><input type="text" id="bz_id" name="bz_id"
 							class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>                   
                    <tr>
                        <td>对应值：</td>
                        <td><input type="text" id="bz_value" name="bz_value"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>  
                    <tr>
                        <td>父级（城市必选）：</td>
                        <td><input type="text" id="pid" name="pid"
                            class="easyui-validatebox" required="false" />&nbsp;
                        </td>
                    </tr>  
                     <tr>
                        <td>是否禁用：</td>
                         <td style="text-align:left">
				            <span class="radioSpan">
				                <input type="radio" name="status" value="1" checked >否</input>&nbsp;&nbsp;&nbsp;&nbsp;
				                <input type="radio" name="status" value="-1">是</input>
				            </span>
				        </td>
                    </tr>                                
                        
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveCl()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closeClWin()">取消</a>
				</div>
			</form>
	  </div>	  
	<style>
	  	#clFm{
	  		margin:0 auto !important;
	  		width: 400px
	  	}
	  	  
	   .datagrid-btable tr{height: 32px;}	
	  </style>
  </body>

	
</html>
