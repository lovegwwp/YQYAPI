/**
 * grid的选择校验
 * 
 * @param {}
 *            grid
 */
function gridSelectedValid(grid) {
	var row = grid.datagrid("getSelected");
	if (row != null) {
		return row;
	} else {
		$.messager.alert("提示", "您尚未选择数据！");
		return false;
	}
}

/**
 * grid的勾选校验
 */
function gridCheckedValid(grid) {
	var rows = grid.datagrid("getChecked");
	if (rows.length > 0) {
		return $.map(rows, function(n) {
			return n.id;
		}).join(",");
	} else {
		$.messager.alert("提示", "您尚未勾选数据！");
		return false;
	}
}

/**
 * grid的勾选校验
 */
function gridCheckedValid2(grid) {
	var rows = grid.datagrid("getChecked");
	if (rows.length=1) {
		return $.map(rows, function(n) {
			return n.macId;
		}).join(",");
	} else if(rows.length > 1) {
		$.messager.alert("提示", "请勾选数据单挑数据！");
		return false;
	}else if(rows.length = 0) {
		$.messager.alert("提示", "您尚未勾选数据！");
		return false;
	}
}


/**
 * 通用删除/禁用
 * 支持批量操作
 */
function commonBatchOperate(grid, url,str) {
	var ids = gridCheckedValid(grid);
	if (!ids) {
		return;
	}
	//"您确定要删除这<font color=red>"
    //+ selectedRows.length + "</font>条数据吗？"
	$.messager.confirm("操作确认", str, function(r) {
		if (r) {
			$.ajax({
				type : "post",
				url : url,
				data : {
					strIds : ids
				},
				success : function(data) {
					/** 如果删除成功，刷新grid数据* */
					if (data.status) {
						$.messager.progress("close");
//						$("#" + modelName + "_grid").datagrid("reload");
						grid.datagrid("reload");
						$.messager.alert("系统提示：", "操作成功！");
					} else {
                        $.messager.alert("系统提示", "操作失败，请联系系统管理员！");
                    }
				}
			});
		}
	});
}


/**
 * 通用==修改状态
 * 支持批量操作
 */
function commonBatchOperate2(grid, url,str) {
	var ids = gridCheckedValid2(grid);
	if (!ids) {
		return;
	}
	//"您确定要删除这<font color=red>"
    //+ selectedRows.length + "</font>条数据吗？"
	$.messager.confirm("操作确认", str, function(r) {
		if (r) {
			$.ajax({
				type : "post",
				url : url,
				data : {
					macIds : ids
				},
				success : function(data) {
					/** 如果删除成功，刷新grid数据* */
					if (data.status) {
						$.messager.progress("close");
//						$("#" + modelName + "_grid").datagrid("reload");
						grid.datagrid("reload");
						$.messager.alert("系统提示：", "操作成功！");
					} else {
                        $.messager.alert("系统提示", "操作失败，请联系系统管理员！");
                    }
				}
			});
		}
	});
}



/**
 * 通用保存
 *
 */
function commonSaveOperate(fm,url,addWin,grid) {
	var data = fm.serialize();
	//console.log(data);
    if(fm.form("validate")){ 
          $.ajax({  
                  type: "post" ,  
                  url: url ,   
                  cache:false ,  
                  data:data ,  
                  dataType:"json" , 
                  /* contentType:"application/json;charset=utf-8",  */
                  success:function(result){  
                      //1 关闭窗口  
                	  addWin.window("close"); 
                      //2刷新datagrid   
                	  grid.datagrid("reload");  
                	  setFmValue() ;
                      //3 提示信息
                      $.messager.alert(result.status, result.message);  
                    /*  / $.messager.show({  
                          title:result.status ,   
                          msg:result.message  
                      }); */  
                  } ,  
                  error:function(result){                             
                      $.messager.alert(result.status, result.message);   
                  }  
              });  
          } else {  
              $.messager.alert("系统提示", "数据验证不通过，请注意！");
          }  
	
}

/**
 *  上传文件保存
 *
 */
function commonSaveUpload(fm,url,addWin,grid) {
	//var data = fm.serialize();
	//console.log(data);
    if(fm.form("validate")){ 
    	fm.form("submit", {
	    	 type: "post",
	    	 url: url,
	    	 success: function(result) {
	    	  var result = eval('(' + result + ')');  
	    		//1 关闭窗口  
           	  addWin.window("close"); 
                 //2刷新datagrid   
           	  grid.datagrid("reload");  
                 //3 提示信息
              $.messager.alert(result.status, result.message);  
             } ,  
             error:function(result){ 
            	 var result = eval('(' + result + ')');  
                 $.messager.alert(result.status, result.message);   
             }  
	     });
    }else {  
              $.messager.alert("系统提示", "数据验证不通过，请注意！");
          }  
	
}

function openEditWin(grid,editWin,fm){
	 var selectedRows = grid.datagrid("getSelections");
     if (selectedRows.length != 1) {
         $.messager.alert("系统提示", "请选择一条要编辑的数据！");
         return;
     }
     var row = selectedRows[0];
     editWin.window("open").window("setTitle", "编辑信息");
     fm.form("load", row);
}

//设置
function setFmValue(){
    $("#account").val(""); 
    $("#uname").val(""); 
    $("#job").val(""); 
    $("#age").val("");
    $("#abstracts").val("请填写"); 
    $("#zd").val("请填写"); 
    $("#skills").val("请填写");
    $("#emails").val("");
 	$("#bz_type").val("");
    $("#bz_id").val("");
    $("#bz_value").val("");
    $("#pid").val("");
    $("#macId").val("");
    $("#talkTime").val("");
}  

   //文件后缀名校验
 function checkFileExt(filename)
  {
     var flag = false; //状态
     var arr = ["jpg","png","gif","ico"];
     //取出上传文件的扩展名
     var index = filename.lastIndexOf(".");
     var ext = filename.substr(index+1);
     //循环比较
     for(var i=0;i<arr.length;i++)
     {
      if(ext == arr[i])
      {
       flag = true; //一旦找到合适的，立即退出循环
       break;
      }
     }
  return flag;
}
 
 
 //文件后缀名校验
 function checkFileExt3(filename)
  {
     var flag = false; //状态
     var arr = ["war"];
     //取出上传文件的扩展名
     var index = filename.lastIndexOf(".");
     var ext = filename.substr(index+1);
     //循环比较
     for(var i=0;i<arr.length;i++)
     {
      if(ext == arr[i])
      {
       flag = true; //一旦找到合适的，立即退出循环
       break;
      }
     }
  return flag;
}
 
 
 //视频后缀名校验
 function checkFileExt2(filename)
  {
     var flag = false; //状态
     var arr = ["mp4","wmv","Ogg","MPGE4"];
     //取出上传文件的扩展名
     var index = filename.lastIndexOf(".");
     var ext = filename.substr(index+1);
     //循环比较
     for(var i=0;i<arr.length;i++)
     {
      if(ext == arr[i])
      {
       flag = true; //一旦找到合适的，立即退出循环
       break;
      }
     }
  return flag;
}
 
 
 
	 function formatNumShow(value,row,index){
 		if(value==null){  
             return "";  
         }else{  
           if(value==1){  
               return "管理员";  
           }else{  
               return "普通用户";  
           }  
         }  
 		 
	 }
	 
	 function formatJy(value,row,index){
 		if(value==null){  
             return "";  
         }else{  
           if(value==1){  
               return "正常";  
           }else{  
               return "禁用";  
           }  
         }  
 		 
 }
	 
	 function formatNumSex(value,row,index){
 		if(value==null){  
             return "";  
         }else{  
           if(value==1){  
               return "男";  
           }else{  
               return "女";  
           }  
         }  
 		 
 }
	 
	 function formatIsYes(value,row,index){
	 		if(value==null){  
	             return "";  
	         }else{  
	           if(value==1){  
	               return "是";  
	           }else if(value==0){  
	               return "否";  
	           }else if(value==2){  
	               return "故障";  
	           }else if(value==3){  
	               return "送维修";  
	           }else if(value==4){  
	               return "报废";  
	           }     
	        }  
	 		 
	 }
	
	 //充值账户
	 function formatZhType(value,row,index){
	 		if(value==null){  
	             return "";  
	         }else{  
	           if(value==1){  
	               return "病人";  
	           }else if(value==2){  
	               return "医生";  
	           }else if(value==3){  
	               return "单位";  
	           }    
	         }  
	 		 
	 }
	 
	//支付类型
	 function formatZfType(value,row,index){
		 if(value==null){  
             return "";  
         }else{  
           if(value==1){  
               return "支付宝";  
           }else if(value==2){  
               return "微信";  
           }else if(value==3){  
               return "其他";  
           }    
         }  
	 }
	 
	//支付类型
	 function formatXtgxType(value,row,index){
		 if(value==null){  
             return "";  
         }else{  
           if(value==1){  
               return "病人端";  
           }else if(value==2){  
               return "医生端";  
           }else if(value==3){  
               return "服务端";  
           }    
         }  
	 }
	 
		//支付状态0 未知状态 1预下单状态 2支付成功 3交易超时 4交易失败 5等待付款 
	 function formatCzzt(value,row,index){
		 if(value==null){  
             return "";  
         }else{  
           if(value==0){  
               return "未知状态";  
           }else if(value==1){  
               return "预下单";  
           }else if(value==2){  
               return "支付成功";  
           }else if(value==3){  
               return "交易超时";  
           }else if(value==4){  
               return "交易失败";  
           }else if(value==5){  
               return "等待付款";  
           }    
         }  
	 }
	 
	//供使用者调用  
	 function trim(s){  
	     return trimRight(trimLeft(s));  
	 }  
	 //去掉左边的空白  
	 function trimLeft(s){  
	     if(s == null) {  
	         return "";  
	     }  
	     var whitespace = new String(" \t\n\r");  
	     var str = new String(s);  
	     if (whitespace.indexOf(str.charAt(0)) != -1) {  
	         var j=0, i = str.length;  
	         while (j < i && whitespace.indexOf(str.charAt(j)) != -1){  
	             j++;  
	         }  
	         str = str.substring(j, i);  
	     }  
	     return str;  
	 }  

	 //去掉右边的空白 www.2cto.com   
	 function trimRight(s){  
	     if(s == null) return "";  
	     var whitespace = new String(" \t\n\r");  
	     var str = new String(s);  
	     if (whitespace.indexOf(str.charAt(str.length-1)) != -1){  
	         var i = str.length - 1;  
	         while (i >= 0 && whitespace.indexOf(str.charAt(i)) != -1){  
	            i--;  
	         }  
	         str = str.substring(0, i+1);  
	     }  
	     return str;  
	 } 
	 
  function myTrim(str){
	  if (str=="") {
		return ""
	  }else{
	  return  trimRight(trimLeft(str));
	  }
  }
	 
	 function checkMacId(macID){
		 var isValidate = false;
		 //1.验证19位长度
		 var macIDStr = myTrim(macID);
		 if(macIDStr.length!=19){			 
			 alert("设备号长度不合法！");
			 return isValidate;
		 }
		//2.判断格式是否正确
		 var re = 0; 
		 re = macIDStr.indexOf("-");
		 if (re <=0) {
			 alert("设备号格式不合法！");
			 return isValidate;
		}
		//3.分割数组，进行下一步校验
		 var strs= new Array(); //定义一数组
		 var str ="";
		 strs=macIDStr.split("-"); //字符分割
		 if (strs.length!=4) {
			 alert("设备号格式不合法！");
			 return isValidate;
		}
		 //4.校验每个字符串长度and进行正则匹配
		 var patern=/^\w{4}$/; 
		 for (i=0;i<strs.length ;i++ )
		 {
			 str =myTrim(strs[i]);
			 //校验每个字符串长度
			 if(str.length!=4) {
				 alert("设备号格式不合法！");
				 return isValidate;
			}
			 //进行正则匹配
			 if (!patern.exec(str)) {
				 alert("设备号格式不合法,应有数字和字母构成！");
				 return isValidate;
			}
		 }
		 isValidate = true;
		 return isValidate;
	 }
	 
	 
	 
	
	
