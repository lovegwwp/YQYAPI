//////////训练类型////////////////////////
var newsTypeData;
$.ajax({
         url:'http://192.168.0.26:8080/SSM/getClsCo.action?bz_type=send_type',
         data:{            
         },
         async:false,
         type:"POST",
         dataType: "json",
         success:function (data) {
            console.log(data);
            newsTypeData = data;
         },
         error:function (err) {
             console.log(err)
         }
     });

function formatNewsType(value,row,index){
   
    for(var i= 0;i<newsTypeData.length;i++){ 
    	var singleNews = newsTypeData[i]
		if(value==null){  
         return "";  
	     }else{  
	       if(value==singleNews.bz_id){  
	           return singleNews.bz_value;  
	       }
	     }  
    }  	 
}



