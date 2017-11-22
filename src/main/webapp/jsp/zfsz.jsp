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
		<title>支付设置首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="js/cwDic.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
      function reZfFlash(){
         //reload:重新执行url，condition是url中的参数  
            $("#searchZfFm").form("clear"); 
            //$("#zfDg").datagrid("reload"); 
         $("#zfDg").datagrid({
	           url:"getZfsz.action"  	     
	         });    
        
        }
      function doZfSearch(){  	 
	      $("#zfDg").datagrid({
	           url:"getZfszBy.action",      
	           queryParams: {  
	              type1: $("#type1").combobox("getValue"), 	
	              //$("#type1").combobox('getText');//这种是获取text文本
	          }  
	      });  
	   } 
    function addZfWin(){
         setClear();
         $("#zfDg").datagrid("uncheckAll");         
         $( "#addZf" ).window("open").window("setTitle", "新增");
        // $( "#addZf" ).window("open"); 
      }
    function closeZfWin(){
         $( "#addZf" ).window("close");
         //$("#zfFm").form("clear"); 
         setClear();
      }

    function saveZf(){
	     var grid = $("#zfDg"); 
	     var fm = $("#zfFm");	   
	     var addWin = $( "#addZf" );
	     commonSaveOperate(fm,"${pageContext.request.contextPath}/addZfsz.action",addWin,grid);  

     }
     
    
    function deleteZf() {
        var grid = $("#zfDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delZfsz.action","确认删除所选数据吗？");
     }
    
    function editZfWin(){
       var grid = $("#zfDg"); 
	   var fm = $("#zfFm");
	   var addWin = $( "#addZf" );  
       openEditWin(grid,addWin,fm);
     }
     
     function setClear(){
        $("#id").val("0");
     	$("#modelTime").val("");
	    $("#modelMoney").val("");
     }
 
    </script>  
	<body class="easyui-layout">
		<table id="zfDg" title="支付设置列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="getZfsz.action"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<th field="id" width="50">id</th>
					<th field="type" width="100" formatter="formatCwzfType">设置类型</th>
					<th field="modelTime" width="200">相应时长</th>
					<th field="modelMoney" width="200">相应金额</th>
					<th field="cjsj" width="150">创建时间</th>
					<th field="xgsj" width="150">修改时间</th>

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<div style="padding:3px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addZfWin()">新增设置</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit"  onclick="editZfWin()">修改设置</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteZf()">删除设置</a>
		    </div>
		    <div style="padding:3px">
		        <form id="searchZfFm">  
					<span>类型:</span>&nbsp;&nbsp;
					 <input id="type1" name="type1" style="line-height:18px;border:1px solid #95b9e7" class="easyui-combobox" data-options="    
					            valueField: 'bz_id',    
						        textField: 'bz_value',    
						        url: 'getClsCo.action?bz_type='+'zfsz_type',
						        method:'get',  
                                panelHeight:'auto'
                              "/>&nbsp;&nbsp;&nbsp;&nbsp;					
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doZfSearch()">搜索</a>&nbsp;&nbsp;				
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reZfFlash()">刷新</a>
				 </form>
			</div>
		</div>
       <div id="addZf" class="easyui-window" title="新增设置"  closed = "true" style="width:500px;height:300px;">
			 <form method="post" id="zfFm"  enctype="multipart/form-data" text-align:left>
                <table cellspacing="8px;"> 
                   <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr> 
                    <tr> 
                        <td>支付类型：</td>
                        <td> 
                        <input id="type" name="type" class="easyui-combobox" data-options="    
					            valueField: 'bz_id',    
						        textField: 'bz_value',    
						        url: 'getClsCo.action?bz_type='+'zfsz_type',
						        method:'get',
						        required:true,    
                                panelHeight:'auto'
                              "/> &nbsp;<span style="color: red">*</span>
                        </td>
                    </tr>               
                    <tr>
                        <td>相应时长：</td>
                        <td><input type="text" id="modelTime" name="modelTime"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>     
                    <tr>
                        <td>相应金额：</td>
                        <td><input type="text" id="modelMoney" name="modelMoney"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>                  
                                  
                        
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveZf()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closeZfWin()">取消</a>
				</div>
			</form>
	  </div>	  
	
	  <style>
	  	#zfFm{
	  		margin:0 auto !important;
	  		width: 300px
	  	}
	  	
	  	 .datagrid-btable tr{height: 32px;}
	  	   	
	  </style>
	  
  </body>

	
</html>
