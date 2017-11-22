<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
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
		<title>后台管理首页</title>
		<link rel="icon" href="img/mst.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="img/mst.ico" type="image/x-icon" />
		
		<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	</head>
	<body class="easyui-layout">
		<!-- 北 -->
		<!-- <div data-options="region:'north',border:false" style="height: 36px; border: 0px; padding: 0px; overflow: hidden;"> -->
		<div data-options="region:'north',border:false" style="height: 35px;width:100%  ; border: 0px; padding: 0px; overflow: hidden;">
			<div class="accordion" style="text-align: right; height: 100%;width:100%  ; background-color: #438EB9;">
				<div style="margin: auto;">
					<!-- <img src="img/toptop.png" width="1920"  height="35" style="margin: auto;border: 0px;" border="0" /> -->
					<img src="img/toptop.png"  style="margin: auto;border: 0px; width:100%  ;height:100% " border="0" />
				</div>
			</div>
			
		</div>
		<!-- 西 -->
		<div data-options="region:'west',split:true,title:' '"style="width: 155px;">
		<!-- <div data-options="region:'west',split:true,title:' '"style="width: 100%;"> -->
			<div class="easyui-layout" data-options="fit:true">
				<div id="pic" data-options="region:'north',split:false" style="height: 140px; padding: 4px;">
					<img alt="" src="img/nan.jpg" width="130px;">
					<%-- ${ sessionScope.username}   <shiro:authenticated>欢迎 [<shiro:principal/>]</shiro:authenticated>--%>
					<div align="center">欢迎 [${ sessionScope.username}]</div>
					<div align="center" id="showDate"></div>
				</div>
				<div data-options="region:'center'">
						<div id="frame_accordion" class="easyui-accordion" data-options="fit:true">  
						  <%--  <shiro:hasAnyRoles name="admin,user">  --%>    					  
							    <div title="代理人管理管理" data-options="" style="overflow:auto; style="padding:10px;">  
							        <ul id="tree_dlr" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>初级代理人</span>  
					                    </li>  
					                    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>中级代理人</span>  
					                    </li>  
					                    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>高级代理人</span>  
					                    </li>  
									</ul>  
							    </div>  
							    <div title="提货管理" style="padding:10px;">  
							        <ul id="tree_th" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>提货点列表</span>  
					                    </li> 
					                     <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>提货商品列表</span>  
					                    </li>  
									</ul>  
							    </div>
							<%-- </shiro:hasAnyRoles>  
							 <shiro:hasAnyRoles name="admin">   --%> 
							    <div title="代理支付管理" style="padding:10px;">  
							        <ul id="tree_dlzf" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">    
					                        <span>代理费设置</span>  
					                    </li> 					                   
									</ul>  
							    </div>
							    <div title="pv管理" style="padding:10px;">  
							        <ul id="tree_pv" class="easyui-tree" >  									  
					                    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>pv设置</span>  
					                    </li>  
									</ul>  
							    </div>  						    
							    <div title="代理积分管理" style="padding:10px;">  
							        <ul id="tree_dljf" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>积分设置</span>  
					                    </li>  
									</ul>  
							    </div>						      
							    <div title="首次代言管理" style="padding:10px;">  
							        <ul id="tree_dlf" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>代言费设置</span>  
					                    </li> 					                     
									</ul>  
							    </div>   
							    <div title="返还周期管理" style="padding:10px;">  
							        <ul id="tree_fhzq" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>返还周期设置</span>  
					                    </li> 					                     
									</ul>  
							    </div> 
							    <div title="代言人管理奖设置" style="padding:10px;">  
							        <ul id="tree_glj" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>管理奖设置</span>  
					                    </li>  					                    
									</ul>  
							    </div>
							     <div title="代言人辅导奖设置" style="padding:10px;">  
							        <ul id="tree_fdj" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>辅导奖设置</span>  
					                    </li>  					                    
									</ul>  
							    </div>
							     <div title="市场奖人员管理" style="padding:10px;">  
							        <ul id="tree_scjry" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>人员设置</span>  
					                    </li> 					                     					                    
									</ul>  
							    </div>
							    <div title="市场奖设置" style="padding:10px;">  
							        <ul id="tree_scj" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>市场奖设置</span>  
					                    </li> 
					                   <!--  <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>封顶设置</span>  
					                    </li>   -->					                    
									</ul>  
							    </div>
							    <div title="分销奖设置" style="padding:10px;">  
							        <ul id="tree_fxj" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>分销奖设置</span>  
					                    </li>  					                    
									</ul>  
							    </div>
							    <div title="每日封顶设置" style="padding:10px;">  
							        <ul id="tree_fd" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>每日封顶</span>  
					                    </li>  					                    
									</ul>  
							    </div>
							    <div title="奖金比例管理" style="padding:10px;">  
							        <ul id="tree_ptbl" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>比例设置</span>  
					                    </li>  					                    
									</ul>  
							    </div>
						    <%--  </shiro:hasAnyRoles>
						     <shiro:hasAnyRoles name="admin">     --%>							    
							     <div title="财务管理" style="padding:10px;">  
							        <ul id="tree_cw" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>金额列表</span>  
					                    </li> 				                 
									</ul>  
							    </div>
						   <%--   </shiro:hasAnyRoles>	 --%>					    
							  <div title="系统设置" style="padding:10px;">  
							        <ul id="tree_xtsz" class="easyui-tree" >  
							        <%--  <shiro:hasAnyRoles name="admin">   --%>
								          <!--  <li  data-options="iconCls:'icon-client_account_template'">  
						                        <span>训练计划设置</span>  
						                    </li>  -->
						                  <!--   <li  data-options="iconCls:'icon-client_account_template'">  
						                        <span>常量设置</span>  
						                    </li>  -->
										    <li  data-options="iconCls:'icon-client_account_template'">  
						                        <span>账号设置</span>  
						                    </li> 
					                 <%--  </shiro:hasAnyRoles>  --%>
					                    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>密码修改</span>  
					                    </li> 					                    
					                    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>退出</span>  
					                    </li> 				                 
									</ul>  
							  </div> 
							   <div title="日志管理" style="padding:10px;">  
							        <ul id="tree_rz" class="easyui-tree" >  
									    <li  data-options="iconCls:'icon-client_account_template'">  
					                        <span>日志</span>  
					                    </li>  
									</ul>  
							    </div>  
							    						       
						</div> 
				</div>
			</div>
		</div>
		<!-- 南 -->
		<div data-options="region:'south',border:false" style="height: 23px; background-color: #f3f3f3;text-align: right;">
			@版权所有2017&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<!-- 中 -->
		<div data-options="region:'center'">
			<div class="easyui-tabs" id="frame_tabs" style="overflow: hidden;background-color: #f3f3f3;"
				data-options="fit:true,border:false,plain:true">
				<div title="首页" style="overflow: hidden;">
					<jsp:include page="/home.jsp"></jsp:include>
				</div>
			</div>
		</div>
	</body>
	
	<style>
		span{
		    <!--color:blue;-->
		    font-size:40px
		}
 

	</style>

	<script type="text/javascript">

		$(function(){			
			setInterval("getTime();",1000);
		})
		//取得系统当前时间
		function getTime(){
			var myDate = new Date();
			var date = myDate.toLocaleDateString();
			var hours = myDate.getHours();
			var minutes = myDate.getMinutes();
			var seconds = myDate.getSeconds();
			$("#showDate").html(date+" "+hours+":"+minutes+":"+seconds);
		}
	
		$("#tree_rz").tree( {
			 onSelect : function(node) {  
                openMenuTow(node);  
            } 
		});
		$("#tree_ptbl").tree( {
			 onSelect : function(node) {  
                openMenuTow(node);  
            } 
		});
		$("#tree_fd").tree( {
			 onSelect : function(node) {  
                openMenuTow(node);  
            } 
		});
		$("#tree_xtsz").tree( {
			 onSelect : function(node) {  
                openMenuTow(node);  
            } 
		});
		$("#tree_fxj").tree( {
			 onSelect : function(node) {  
                openMenuTow(node);  
            } 
		});
		$("#tree_scj").tree( {
			 onSelect : function(node) {  
                openMenuTow(node);  
            } 
		});
		
		$("#tree_scjry").tree( {
			 onSelect : function(node) {  
               openMenuTow(node);  
           } 
		});
		
		$("#tree_cw").tree( {
			 onSelect : function(node) {  
              openMenuTow(node);  
          } 
		});
		$("#tree_fdj").tree( {
			 onSelect : function(node) {  
             openMenuTow(node);  
         } 
		});
		
		$("#tree_glj").tree( {
			 onSelect : function(node) {  
            openMenuTow(node);  
        } 
		});
		
		$("#tree_fhzq").tree( {
			 onSelect : function(node) {  
           openMenuTow(node);  
       } 
		});
		
		$("#tree_dlf").tree( {
			 onSelect : function(node) {  
          openMenuTow(node);  
      } 
		});
		
		
		$("#tree_dljf").tree( {
			 onSelect : function(node) {  
         openMenuTow(node);  
     } 
		});
		
		$("#tree_pv").tree( {
			 onSelect : function(node) {  
        openMenuTow(node);  
    } 
		});
		
		$("#tree_dlzf").tree( {
			 onSelect : function(node) {  
       openMenuTow(node);  
   } 
		});
		
		$("#tree_th").tree( {
			 onSelect : function(node) {  
      openMenuTow(node);  
  } 
		});
		
		$("#tree_dlr").tree( {
			 onSelect : function(node) {  
     openMenuTow(node);  
 } 
		});
		
		
		
		function openMenuTow(node){

			 //树型菜单的名字   
            var noteText = $(".tree-title", node.target).text();  
            var exist_tab = $('#frame_tabs').tabs('getTab', noteText);  
            //判断是否已经打开该选项卡  
            if (exist_tab) {  
                $('#frame_tabs').tabs('select', noteText);  
                return;  
            } else {  
                var content = "";
                if(noteText=="医生列表"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="doc.action" style="width:100%;height:100%;"></iframe>';
                }else if(noteText=="初级代理人"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="pat.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="中级代理人"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="dev.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="高级代理人"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="devErr.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="提货点列表"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="thd.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="提货商品列表"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="thdsp.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="代理费设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="dyf.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="pv设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="pvsz.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="代理积分设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="dyjf.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="积分设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="jfsz.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="返还周期设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="fhzq.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="管理奖设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="glj.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="辅导奖设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="fdj.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="人员设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="patOnTj.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="市场奖设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="scj.action" style="width:100%;height:100%;"></iframe>';

                }/* else if(noteText=="封顶设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="patAddrTj.action" style="width:100%;height:100%;"></iframe>';

                } */else if(noteText=="分销奖设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="fxj.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="比例设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="jjbl.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="金额列表"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="cwzf.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="每日封顶"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="mrfd.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="账号设置"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="accounts.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="密码修改"){
                	content = '<iframe scrolling="auto" frameborder="0"  src="rePwd.action" style="width:100%;height:100%;"></iframe>';

                }else if(noteText=="退出"){
                	//content = '<iframe scrolling="auto" frameborder="0"  src="tcxt.action" style="width:100%;height:100%;"></iframe>';
                	window.parent.location.href='/SSM/shiro-tcxt.action';
                }else{
                	content = '<div style="font-size: 35px; text-align: center;margin-top: 100px;">系统维护升级中！</br>'+
                	'<div style="font-size:20px;">如果觉得无聊,我们可以去<a href="http://www.aqee.net/docs/the-quiet-place/" target="_blank">一个安静的地方</a></div></div>';
                }
                $('#frame_tabs').tabs('add', {  
                    'id' : 'tab',  
                    title : noteText,  
                    fit : true,  
                    content : content,  
                    closable : true  
                });  
                //获取最后一个tabs 在新加的选项卡后面添加"关闭全部"  
                var li = $(".tabs-wrap ul li:last-child");  
                $("#close").remove();  
                li.after("<li id='close'><a class='tabs-inner' href='javascript:void()' onClick='javascript:closeAll()'>关闭全部</a></li>");  
            }  
		}

		function closeAll() {  
            $(".tabs li").each(function(index, obj) {  
                  //获取所有可关闭的选项卡  
                  var tab = $(".tabs-closable", this).text();  
                  $(".easyui-tabs").tabs('close', tab);  
            });  
            $("#close").remove();//同时把此按钮关闭  
        }  
	</script>

</html>
