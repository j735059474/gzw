	function ScapStyleRender(){
	}	
	ScapStyleRender.render = function(row, col, value, header, cell,styles,type) { 
	 if (type=="decimal"){
	   DecimalRender.render.call(this,row, col, value, header, cell);
	   }else{
	     DefaultRender.render.call(this, row, col, value, header, cell);
	   }
	   this.setCellStyle=function(cell){
        for (i=0;i<styles.length;i++){
           var  temparr=styles[i];
           var style=temparr.split(":");
           cell.style[style[0]]=style[1];
        }
     }
	 };
	 
	 function CbDecimalRender(){
	 }
	CbDecimalRender.render = function(row, col, value, header, cell) { 
	  var styles=["backgroundColor:#E4EEFA","fontStyle:italic","fontWeight:bold","fontFamily:'ºÚÌå'"];
	  ScapStyleRender.render.call(this,row, col, value, header, cell,styles,"decimal");
	 };

	 	function CbFontWeightRender(){
	   }
	   CbFontWeightRender.render = function(row, col, value, header, cell) { 
	    var styles=["backgroundColor:#E4EEFA","fontWeight:bold","color:#FF0000","fontSize:11"];
	    ScapStyleRender.render.call(this,row, col, value, header, cell,styles);
	   };

	function   EnabledColorRender(){
	}
	  EnabledColorRender.render = function(row, col, value, header, cell) { 
	    var styles=["backgroundColor:#E8E8E8"];
	    ScapStyleRender.render.call(this,row, col, value, header, cell,styles);
		  this.setCellStyle(cell);
	   };