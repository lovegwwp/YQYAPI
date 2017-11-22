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
		<title>新闻管理首页</title>
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="js/newsDic.js"></script>
	</head>
	 <script type="text/javascript"> 
	  
	  $( function() {	
	     
	  } );
    
      function reNewsFlash(){
         //reload:重新执行url，condition是url中的参数  
            $("#searchNewsFm").form("clear"); 
             $("#newsDg").datagrid({
	           url:"getNews.action"      	     
	         }); 

        
        }
      function doNewsSearch(){
	      $("#newsDg").datagrid({
	           url:"getNewsBy.action",      
	           queryParams: {  
	              titles1: $("#titles1").val(),  	            
	          }  
	      });  
	   } 
    function addNewsWin(){
         setClear();
         $("#newsDg").datagrid("uncheckAll");         
         $( "#addNews" ).window("open").window("setTitle", "新增");
         //$( "#addNews" ).window("open"); 
      }
    function closeNewsWin(){
         $( "#addNews" ).window("close");
         $("#newsFm").form("clear"); 
      }

    function saveNews(){
	     var grid = $("#newsDg"); 
	     var fm = $("#newsFm");	   
	     var addWin = $( "#addNews" );
	     var fValue = $("#myFile").val();  
 	     /* fm.form("submit", {
	    	 type: "post",
	    	 url: "${pageContext.request.contextPath}/addNews.action",
	    	 success: function(data) {
	    		 
	    	 }
	     });  */
	    if(checkFileExt(fValue)){
	    	 commonSaveUpload(fm,"${pageContext.request.contextPath}/addNews.action",addWin,grid);  
      	 }else{
      	 	$.messager.alert("系统提示", "上传文件类型不合法！");
      	 }        
     }
     
    
    function deleteNews() {
        var grid = $("#newsDg"); 
     	commonBatchOperate(grid, "${pageContext.request.contextPath}/delNews.action","确认删除所选数据吗？");
     }
    
    function editNewsWin(){
       var grid = $("#newsDg"); 
	   var fm = $("#newsFm");
	   var addWin = $( "#addNews" );  
       openEditWin(grid,addWin,fm);
     }
     
     function setClear(){
        $("#id").val("0");
     	$("#pics").val("");
	    $("#contents").val("请填写");
	    $("#titles").val("");
     }
 
    </script>  
	<body class="easyui-layout">
		<table id="newsDg" title="新闻列表" class="easyui-datagrid" style="width:1750px;height:865px"
			url="getNews.action"
			toolbar="#toolbar" pagination="true" rownumbers="true"  singleSelect="false">
			<thead>
				<tr>
				    <th field="ck" checkbox="true"></th>
					<!-- <th field="id" width="50">id</th> -->
					<th field="type" width="80" formatter="formatNewsType">发送类型</th>
					<th field="titles" width="150">标题</th>
					<th field="byteSize" width="100">图片大小</th>
					<th field="pics" width="250">图片</th>
					<th field="contents" width="850">新闻内容</th>
					<th field="cjsj" width="150">创建时间</th>
				<!-- 	<th field="xgsj" width="150">修改时间</th> -->

				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding:3px">
			<div style="padding:3px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"   onclick="addNewsWin()">新增新闻</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit"  onclick="editNewsWin()">修改新闻</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"   onclick="deleteNews()">删除新闻</a>
		    </div>
		    <div style="padding:3px">
		        <form id="searchNewsFm">  
					<span>标题:</span>
					<input id="titles1" name="titles1" style="line-height:18px;border:1px solid #95b9e7">&nbsp;&nbsp;					
					<a href="#" class="easyui-linkbutton" iconCls="icon-search"  onclick="doNewsSearch()">搜索</a>&nbsp;&nbsp;				
					<a href="#" class="easyui-linkbutton" iconCls="icon-reload"  onclick="reNewsFlash()">刷新</a>
				 </form>
			</div>
		</div>
       <div id="addNews" class="easyui-window" title="新增新闻"  closed = "true" style="width:500px;height:400px;">
			 <form method="post" id="newsFm"  enctype="multipart/form-data" text-align:left>
                <table cellspacing="8px;"> 
                   <tr>                       
                        <td>
                        	<input type="hidden" id="id" name="id" value="0"/>
                        </td>
                    </tr> 
                    <tr> 
                        <td>发送类型：</td>
                        <td> 
                        <input id="type" name="type" class="easyui-combobox" data-options="    
					            valueField: 'bz_id',    
						        textField: 'bz_value',    
						        url: 'getClsCo.action?bz_type='+'send_type',
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
					<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveNews()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" icon="icon-cancel"  onclick="closeNewsWin()">取消</a>
				</div>
			</form>
	  </div>
	  	  
	<style>
	  	#newsFm{
	  		margin:0 auto !important;
	  		width: 400px
	  	}
	  .datagrid-btable tr{height: 32px;}
	  	
	  </style>
  </body>

	
</html>
