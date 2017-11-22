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
		<title>积分首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
	     
	  } );
 
    function addjfszWin(){
    	$("#jfszDg").datagrid("uncheckAll");         
        $( "#addjfsz" ).window("open").window("setTitle", "修改");
        // $( "#addCl" ).window("open"); 
      }
    function closejfszWin(){
         $( "#addjfsz" ).window("close");
         $("#jfszFm").form("clear"); 
      }

    function savejfsz(){
	     var grid = $("#jfszDg"); 
	     var fm = $("#jfszFm");	   
	     var addWin = $( "#addjfsz" );  
      	 commonSaveOperate(fm,"${pageContext.request.contextPath}/addCl.action",addWin,grid);     	     
     }
    
    function deletejfsz() {
        var grid = $("#jfszDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delCl.action","确认删除所选数据吗？");
     }
    
    function editFzWin(){
       var grid = $("#jfszDg"); 
	   var fm = $("#jfszFm");
	   var addWin = $( "#addjfsz" );  
       openEditWin(grid,addWin,fm);
     }
     

    
    </script>  
	<body class="easyui-layout">
		<table id="jfszDg" title="分积分设置列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="getClsBy.action?bz_type=jf_type"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<!-- <th field="id" width="50">id</th> -->
					<!-- <th field="bz_type" width="100">标志类型</th>
					<th field="bz_id" width="80">标志id</th> -->				
					<th field="bz_info" width="200">相应比例</th>
					<th field="bz_value" width="150">对应比例值</th>
					<th field="hrefStr" width="150">操作</th>					
					<!-- <th field="pid" width="80">父级</th>
					<th field="status" width="80">禁用状态</th> -->

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<div style="padding:3px">
			<!-- 	<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addClWin()">新增常量</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit"  onclick="editblWin()">修改封顶值</a>
				 <a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteCl()">删除常量</a> -->
		    </div>
		 <!--    <div style="padding:3px">
		        <form id="searchClFm">  
					<span>标志类型:</span>
					<input id="bz_type1" name="bz_type1" style="line-height:18px;border:1px solid #95b9e7">&nbsp;&nbsp;
					<span>对应值：</span>
					<input id="bz_value1" name="bz_value1" style="line-height:18px;border:1px solid #95b9e7">
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doClSearch()">搜索</a>&nbsp;&nbsp;
					 <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="reFlash()">reFlash</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reClFlash()">刷新</a>
				 </form>
			</div> -->
		</div>
       <div id="addjfsz" class="easyui-window" title="修改数值"  closed = "true" style="width:450px;height:250px;">
			 <form method="post" id="jfszFm" text-align:center>
                <table cellspacing="8px;">   
                    <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr>                          
                    <tr>
                        <td>对应标识：</td>
                        <td><input type="text" id="bz_info" name="bz_info"
                            class="easyui-validatebox" required="true" readonly="readonly"/>&nbsp;<span
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
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="savejfsz()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closejfszWin()">取消</a>
				</div>
			</form>
	  </div>	  
	<style>
	  	#jfszFm{
	  		margin:0 auto !important;
	  		width: 300px
	  	}
	  	  
	   .datagrid-btable tr{height: 32px;}	
	  </style>
  </body>

	
</html>
