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
		<title>系统更新首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
	<!-- 	<script type="text/javascript" src="js/newsDic.js"></script> -->
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
	     
	  } );
    
      function reXtgxFlash(){
         //reload:重新执行url，condition是url中的参数  
            $("#searchXtgxFm").form("clear"); 
             $("#xtgxDg").datagrid({
	           url:"getXtgx.action"      	     
	         }); 

        
        }
      function doXtgxSearch(){
	      $("#xtgxDg").datagrid({
	           url:"getXtgxBy.action",      
	           queryParams: {  
	              titles1: $("#titles1").val(),  	            
	          }  
	      });  
	   } 
    function addXtgxWin(){
         setXtgxClear();
         $("#xtgxDg").datagrid("uncheckAll");         
         $( "#addXtgx" ).window("open").window("setTitle", "新增");
        // $( "#addXtgx" ).window("open"); 
      }
    function closeXtgxWin(){
         $( "#addXtgx" ).window("close");
         $("#xtgxFm").form("clear"); 
      }

    function saveXtgx(){
	     var grid = $("#xtgxDg"); 
	     var fm = $("#xtgxFm");	   
	     var addWin = $( "#addXtgx" );
	     var fValue = $("#myFile").val();  
 	     /* fm.form("submit", {
	    	 type: "post",
	    	 url: "${pageContext.request.contextPath}/addNews.action",
	    	 success: function(data) {
	    		 
	    	 }
	     });  */
	    if(checkFileExt3(fValue)){
	    	 commonSaveUpload(fm,"${pageContext.request.contextPath}/addXtgx.action",addWin,grid);  
      	 }else{
      	 	$.messager.alert("系统提示", "上传文件类型不合法！");
      	 }        
     }
     
    
    function deleteXtgx() {
        var grid = $("#xtgxDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delXtgx.action","确认删除所选数据吗？");
     }
    
    function editXtgxWin(){
       var grid = $("#xtgxDg"); 
	   var fm = $("#xtgxFm");
	   var addWin = $( "#addXtgx" );  
       openEditWin(grid,addWin,fm);
     }
     
     function setXtgxClear(){
        $("#id").val("0");
     	$("#src").val("");
	    $("#contents").val("请填写");
	    $("#titles").val("");
     }
 
    </script>  
	<body class="easyui-layout">
		<table id="xtgxDg" title="更新列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="getXtgx.action"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<!-- <th field="id" width="50">id</th> -->
					<th field="type" width="80" formatter="formatXtgxType">更新类型</th>
					<th field="titles" width="150">主题</th>
					<th field="byteSize" width="100">文件大小</th>
					<th field="src" width="250">文件路径</th>
					<th field="contents" width="850">更新内容</th>
					<th field="cjsj" width="150">创建时间</th>
				<!-- 	<th field="xgsj" width="150">修改时间</th> -->

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<div style="padding:3px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addXtgxWin()">新增更新</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit"  onclick="editXtgxWin()">修改更新</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteXtgx()">删除更新</a>
		    </div>
		    <div style="padding:3px">
		        <form id="searchXtgxFm">  
					<span>标题:</span>
					<input id="titles1" name="titles1" style="line-height:18px;border:1px solid #95b9e7">&nbsp;&nbsp;					
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doXtgxSearch()">搜索</a>&nbsp;&nbsp;				
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reXtgxFlash()">刷新</a>
				 </form>
			</div>
		</div>
       <div id="addXtgx" class="easyui-window" title="新增更新"  closed = "true" style="width:500px;height:400px;">
			 <form method="post" id="xtgxFm"  enctype="multipart/form-data" text-align:left>
                <table cellspacing="8px;"> 
                   <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr> 
                    <tr> 
                        <td>更新类型：</td>
                        <td> 
                        <input id="type" name="type" class="easyui-combobox" data-options="    
					            valueField: 'bz_id',    
						        textField: 'bz_value',    
						        url: 'getClsCo.action?bz_type='+'xtgx_type',
						        method:'get',
						        required:true,    
                                panelHeight:'auto'
                              "/> &nbsp;<span style="color: red">*</span>
                        </td>
                    </tr>               
                    <tr>
                        <td>标题：</td>
                        <td><input type="text" id="titles" name="titles"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>
                    <tr>
                        <td>图片：</td>
                        <td><input type="file" id="myFile" name="myFile"
                            class="easyui-validatebox" required="true" />&nbsp;<span
                            style="color: red">*</span>
                        </td>
                    </tr>                 
                    <tr>
                       <td>内容：</td>
                        <td><textarea class="easyui-textbox" required="true" id="contents" name="contents" data-options="multiline:true" value="请填写" style="width:300px;height:100px;white-space:pre-wrap"></textarea>
                        </td>
                    </tr>                
                        
                </table>
				<div style="padding:5px;text-align:center;">
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveXtgx()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closeXtgxWin()">取消</a>
				</div>
			</form>
	  </div>
	  	  
	<style>
	  	#xtgxFm{
	  		margin:0 auto !important;
	  		width: 400px
	  	}
	   .datagrid-btable tr{height: 32px;}
	  	
	  </style>
  </body>

	
</html>
