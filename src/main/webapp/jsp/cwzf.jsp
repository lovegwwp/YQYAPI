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
		<title>财务统计首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="js/cwDic.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
	     
	  } );
	  
	  $.fn.datebox.defaults.formatter = function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+'-'+m+'-'+d;
		}


    
      function reCwFlash(){
         //reload:重新执行url，condition是url中的参数  
            $("#searchCwFm").form("clear");  
           // $("#cwDg").datagrid("reload");  
            $("#cwDg").datagrid({
 	           url:"getCw.action"      	     
 	         }); 
        
        }
      function doCwSearch(){
	      $("#cwDg").datagrid({
	           url:"getCwBy.action",      
	           queryParams: {  
	              account: $("#account").val(), 
	              czType: $("#czType1").combobox("getValue"), 	
	              kssj: $("#kssj").datebox("getValue"), 
	              jssj: $("#jssj").datebox("getValue"),
	          }  
	      });  
	   } 
    function addCwWin(){
    	  $("#cwDg").datagrid("uncheckAll");         
          $( "#addCw" ).window("open").window("setTitle", "新增");
        // $( "#addCw" ).window("open"); 
      }
    function closeCwWin(){
         $( "#addCw" ).window("close");
         $("#cwFm").form("clear"); 
      }

    function saveCw(){
	     var grid = $("#cwDg"); 
	     var fm = $("#cwFm");	   
	     var addWin = $( "#addCw" );	    
	     commonSaveOperate(fm,"${pageContext.request.contextPath}/addCw.action",addWin,grid);  
      	     
     }
     
    
    function deleteCw() {
        var grid = $("#cwDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delCw.action","确认删除所选数据吗？");
     }
    


 
    </script>  
	<body class="easyui-layout">
		<table id="cwDg" title="财务统计列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="getCw.action"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<!-- <th field="id" width="50">id</th> -->
					<th field="macOrderId" width="250">充值订单号</th>
					<th field="account" width="200">充值账户</th>
					<th field="zhType" width="80" formatter="formatZhType">充值账户类型</th>
					<th field="czTime" width="100">充值时长</th>
					<th field="czMoney" width="100">充值金额</th>
					<th field="czType" width="100" formatter="formatCwzfType" >充值类型</th>
					<th field="zfType" width="80" formatter="formatZfType">支付类型</th>
					<th field="zfAccount" width="200">支付账号</th>
					<th field="zfUname" width="200">支付昵称</th>
					<th field="status" width="100" formatter="formatCzzt">支付状态</th>
				<!-- 	<th field="createdAt" width="200">支付时间</th> -->
					<th field="cjsj" width="150">创建时间</th>
					<!-- <th field="xgsj" width="150">修改时间</th> -->

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<!-- <div style="padding:3px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addCwWin()">新增记录</a>		
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteCw()">删除记录</a>
		    </div> -->
		    <div style="padding:3px">
		        <form id="searchCwFm">  
					<span>充值账号:</span>
				    <input id="account" name="account" style="line-height:18px;border:1px solid #95b9e7 ">&nbsp;&nbsp;
					<!-- <input  id="kssj"  type= "text" class= "easyui-datebox" required ="required"> </input>	 -->
					<span>开始时间:</span>
					<input  id="kssj"  type= "text" class= "easyui-datebox" > </input>&nbsp;&nbsp;   
					<span>结束时间:</span>
					<input  id="jssj"  type= "text" class= "easyui-datebox" > </input>&nbsp;&nbsp; 
					<span>充值类型:</span>&nbsp;&nbsp;
					<input id="czType1" name="czType1" style="line-height:18px;border:1px solid #95b9e7" class="easyui-combobox" data-options="    
					            valueField: 'bz_id',    
						        textField: 'bz_value',    
						        url: 'getClsCo.action?bz_type='+'zfsz_type',
						        method:'get',  
                                panelHeight:'auto'
                              "/>&nbsp;&nbsp;&nbsp;&nbsp;  			
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doCwSearch()">搜索</a>&nbsp;&nbsp;				
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reCwFlash()">刷新</a>
				 </form>
			</div>
		</div>
       <div id="addCw" class="easyui-window" title="新增财务记录"  closed = "true" style="width:500px;height:400px;">
			 <form method="post" id="cwFm"  enctype="multipart/form-data" text-align:left>
                <table cellspacing="8px;"> 
                   <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr> 
                    <tr> 
                        <td>训练类型：</td>
                        <td> 
                        <input id="status" name="status" class="easyui-combobox" data-options="    
					            valueField: 'bz_id',    
						        textField: 'bz_value',    
						        url: 'getClsCo.action?bz_type='+'xlxm_type',
						        method:'get',
						        required:true,    
                                panelHeight:'auto'
                              "/> &nbsp;<span style="color: red">*</span>
                        </td>
                    </tr>               
                                
                        
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveCw()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closeCwWin()">取消</a>
				</div>
			</form>
	  </div>	  
	
  </body>

	
</html>
