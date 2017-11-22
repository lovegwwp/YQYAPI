//////////训练类型////////////////////////
var cwTypeData;
$.ajax({
         url:'http://192.168.0.26:8080/SSM/getClsCo.action?bz_type=zfsz_type',
         data:{            
         },
         async:false,
         type:"POST",
         dataType: "json",
         success:function (data) {
            console.log(data);
            cwTypeData = data;
         },
         error:function (err) {
             console.log(err)
         }
     });

function formatCwzfType(value,row,index){
   
    for(var i= 0;i<cwTypeData.length;i++){ 
    	var singleCw = cwTypeData[i]
		if(value==null){  
         return "";  
	     }else{  
	       if(value==singleCw.bz_id){  
	           return singleCw.bz_value;  
	       }
	     }  
    }  	 
}



