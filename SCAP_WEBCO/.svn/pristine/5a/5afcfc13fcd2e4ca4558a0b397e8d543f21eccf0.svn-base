/**
 * 水箱图 预置样式
 */
ChartModel.prototype.toCylinderXml = function(bgcolor) {
	if(bgcolor == "cylinder_operating_capacity") return this.toOperatingCapacityXml();
	else if(bgcolor == "cylinder_tanker_capacity") return this.toTankerCapacityXml();
	else if(bgcolor == "cylinder_custom_border") return this.toCustomBorderXml();
	else return this.toCustomBorderXml();
}

ChartModel.prototype.toOperatingCapacityXml = function() {
	var conf = this.config;
	var str = '';
	var absWidth = conf.offsetwidth / 2;
	var numberSuffix = '';
	if(conf.numberSuffix)
		 numberSuffix = conf.numberSuffix;	
	var func = "";
	if(conf.captionFunc!=null){
		func = "javascript:onDrill('"+conf.captionUrl+"')";
	}
	var dataString = 
		'<chart manageResize="1" bgColor="FFFFFF" lowerLimit="'+conf.cylinder_lowerLimit+'" upperLimit="'+conf.cylinder_upperLimit+'" \n\
			 cylFillColor="'+conf.cylinder_color+'" tickMarkDistance="5" numberSuffix="'+numberSuffix+'" \n\
			 chartLeftMargin="60" chartRightMargin="60" chartTopMargin="80" clickUrl="'+func+'"> \n\
        <value>'+conf.cylinder_value+'</value> \n\
        <annotations> \n\
        	<annotationGroup showBelow="1"> \n\
       			<annotation type="text" x="'+absWidth+'" y="30" label="'+conf.caption+'" fontColor="333333" fontSize="14" isBold="1"/>\n\
          </annotationGroup> \n\
        </annotations> \n\
		</chart>';
	return dataString;
};
ChartModel.prototype.toTankerCapacityXml = function() {	
	var conf = this.config;
	var str = ''; 
	var absWidth = conf.offsetwidth / 2;
	var labelX = (conf.offsetwidth / 2) - 50;
	var labelY = conf.offsetheight - 75;
	var numberSuffix = '';
	if(conf.numberSuffix)
		 numberSuffix = conf.numberSuffix;
		 var func = "";
	if(conf.captionFunc!=null){
		func = "javascript:onDrill('"+conf.captionUrl+"')";
	}
	var dataString = 
	'<chart manageResize="1" bgColor="FFFFFF" lowerLimit="'+conf.cylinder_lowerLimit+'" upperLimit="'+conf.cylinder_upperLimit+'" \n\
		showTickMarks="0" numberSuffix="'+numberSuffix+'" showLimits="0" tickValueDistance="20" decmials="0" tickMarkDecmials="0" \n\
		cylFillColor="'+conf.cylinder_color+'" cylRadius="45" showValue="1" chartLeftMargin="'+(absWidth-45)+'" chartRightMargin="'+(absWidth-45)+'" \n\
		chartTopMargin="60" chartBottomMargin="80"  clickUrl="'+func+'"> \n\
	    <value>'+conf.cylinder_value+'</value> \n\
	    <annotations> \n\
		    <annotationGroup x="'+labelX+'" y="'+labelY+'" scaleText="1"> \n\
          <annotation type="rectangle" x="0" y="0" toX="100" toY="60" radius="0" fillcolor="333333" fillAlpha="5"/> \n\
	        <annotation type="line" x="0" y="0" toY="60" color="333333" thickness="2"/> \n\
	        <annotation type="line" x="100" y="0" toY="60" color="333333"  thickness="2"/> \n\
	        <annotation type="line" x="0" y="0" toX="5" color="333333"  thickness="2"/> \n\
	        <annotation type="line" x="0" y="60" toX="5" color="333333"  thickness="2"/> \n\
	        <annotation type="line" x="95" y="0" toX="100" color="333333"  thickness="2"/> \n\
	        <annotation type="line" x="95" y="60" toX="100" color="333333"  thickness="2"/> \n\
	        <annotation type="text" label="最小值：'+conf.cylinder_lowerLimit+numberSuffix+'" font="Verdana" x="5" y="5" align="left" vAlign="bottom" fontcolor="333333" fontSize="10" isBold="0"/> \n\
	        <annotation type="text" label="最大值：'+conf.cylinder_upperLimit+numberSuffix+'" font="Verdana" x="5" y="20" align="left" vAlign="bottom" fontcolor="333333" fontSize="10"/> \n\
	        <annotation type="text" label="当前：'+conf.cylinder_value+numberSuffix+'" font="Verdana" x="5" y="35" align="left" vAlign="bottom" fontcolor="333333" fontSize="10" isbold="0"/> \n\
		    </annotationGroup> \n\
		    <annotationGroup scaleText="1"> \n\
		    	<annotation type="text" x="'+absWidth+'" y="30" label="'+conf.caption+'" fontColor="333333" fontSize="12" isBold="1"/>\n\
		    </annotationGroup> \n\
    </annotations> \n\
		</chart>'; 
return dataString;
};
ChartModel.prototype.toCustomBorderXml = function() {
	var conf = this.config;
	var str = '';
	var absWidth = conf.offsetwidth / 2;
	var numberSuffix = '';
	if(conf.numberSuffix)
		 numberSuffix = conf.numberSuffix;
	var func = "";
	if(conf.captionFunc!=null){
		func = "javascript:onDrill('"+conf.captionUrl+"')";
	}
	var dataString =
		'<chart manageResize="1" bgColor="FFFFFF" bgAlpha="0" showBorder="1" showTickMarks="0" \n\
			lowerLimit="'+conf.cylinder_lowerLimit+'" upperLimit="'+conf.cylinder_upperLimit+'"  \n\
			showTickValues="0" showLimits="1" numberSuffix="'+numberSuffix+'" decmials="0" cylFillColor="'+conf.cylinder_color+'" baseFontColor="'+conf.cylinder_color+'" \n\
			chartLeftMargin="60" chartRightMargin="60" chartTopMargin="80" clickUrl="'+func+'"> \n\
        <value>'+conf.cylinder_value+'</value> \n\
        <annotations> \n\
        	<annotationGroup showBelow="1"> \n\
       			<annotation type="text" x="'+absWidth+'" y="30" label="'+conf.caption+'" fontColor="333333" fontSize="14" isBold="1"/>\n\
          </annotationGroup> \n\
        </annotations> \n\
		</chart>';
	return dataString;
};