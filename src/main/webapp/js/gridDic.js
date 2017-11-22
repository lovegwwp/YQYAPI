//////////训练类型////////////////////////
var xlTypeData;
$.ajax({
         url:'http://192.168.0.26:8080/SSM/getClsCo.action?bz_type=xlxm_type',
         data:{            
         },
         async:false,
         type:"POST",
         dataType: "json",
         success:function (data) {
            console.log(data);
            xlTypeData = data;
         },
         error:function (err) {
             console.log(err)
         }
     });

function formatXlType(value,row,index){
   
    for(var i= 0;i<xlTypeData.length;i++){ 
    	var singleType = xlTypeData[i]
		if(value==null){  
         return "";  
	     }else{  
	       if(value==singleType.bz_id){  
	           return singleType.bz_value;  
	       }
	     }  
    }  	 
}

//////////视频类型////////////////////////
var vedioTypeData;
$.ajax({
    url:'http://192.168.0.26:8080/SSM/getClsCo.action?bz_type=vedio_type',
    data:{            
    },
    async:false,
    type:"POST",
    dataType: "json",
    success:function (data) {
       console.log(data);
       vedioTypeData = data;
    },
    error:function (err) {
        console.log(err)
    }
});


function formatVedioType(value,row,index){
	   
    for(var i= 0;i<vedioTypeData.length;i++){ 
    	var singleVedio = vedioTypeData[i]
		if(value==null){  
         return "";  
	     }else{  
	       if(value==singleVedio.bz_id){  
	           return singleVedio.bz_value;  
	       }
	     }  
    }  	 
}