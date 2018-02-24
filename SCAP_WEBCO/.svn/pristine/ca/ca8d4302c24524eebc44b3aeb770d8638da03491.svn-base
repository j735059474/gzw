/**
 *仪表盘 预置样式
 */
ChartModel.prototype.toAngularXml = function(bgcolor) {
	if(bgcolor == "angular_steel_casing") return this.toSteelCasingXml();
	else if(bgcolor == "angular_revenue_gauge") return this.toRevenueGaugeXml();
	else if(bgcolor == "angular_external_image") return this.toExternalImageXml();
	else return this.toSteelCasingXml();
}

ChartModel.prototype.toSteelCasingXml = function() {	
	var conf = this.config;
	var str = '';
	str += this.toColorRangeXml();
	str += this.toDialsetXml();
	var absWidth = conf.offsetwidth / 2;
	var absHeight = conf.offsetheight / 2 + conf.offsetheight / 20; 
	var dataString =
		'<chart manageResize="1" origW="'+conf.offsetwidth+'" origH="'+conf.offsetheight+'" bgColor="FFFFFF" \n\
				upperLimit="'+this.colorrange[this.colorrange.length-1].maxvalue+'" lowerLimit="'+this.colorrange[0].minvalue+'" \n\
				baseFontColor="FFFFFF" majorTMNumber="11" majorTMColor="FFFFFF"  majorTMHeight="8" minorTMNumber="5" \n\
				minorTMColor="FFFFFF" minorTMHeight="3" toolTipBorderColor="FFFFFF" toolTipBgColor="333333" gaugeOuterRadius="100" \n\
				gaugeStartAngle="225" gaugeEndAngle="-45" placeValuesInside="1" gaugeInnerRadius="80%" annRenderDelay="0" gaugeFillMix="" \n\
				pivotRadius="10" showPivotBorder="0" pivotFillMix="{CCCCCC},{333333}" pivotFillRatio="50,50" showShadow="0" \n\
				gaugeOriginX="'+absWidth+'" gaugeOriginY="'+absHeight+'">'+str+'\n\
			<annotations>\n\
				<annotationGroup x="'+absWidth+'" y="'+absHeight+'" showBelow="1">\n\
					<annotation type="circle" x="0" y="0" radius="145" fillColor="CCCCCC,111111"  fillPattern="linear" fillAlpha="100,100"  fillRatio="50,50" fillAngle="-45"/>\n\
					<annotation type="circle" x="0" y="0" radius="120"  fillColor="111111,cccccc"  fillPattern="linear" fillAlpha="100,100"  fillRatio="50,50" fillAngle="-45"/>\n\
					<annotation type="circle" x="0" y="0" radius="110"  color="666666"/>\n\
				</annotationGroup>\n\
				<annotationGroup showBelowChart="0"> \n\
					<annotation type="text" x="'+absWidth+'" y="20" label="'+conf.caption+'" fontColor="676666" fontSize="16" isBold="1" /> \n\
				</annotationGroup> \n\
			</annotations>\n\
		</chart>'
		
	return dataString;
};

ChartModel.prototype.toRevenueGaugeXml = function() {	
	var conf = this.config;
	var str = '';
	str += this.toColorRangeXml();
	str += this.toDialsetXml();
	var absWidth = conf.offsetwidth / 2;
	var absHeight = conf.offsetheight - (conf.offsetheight / 4); 
	var dataString = 
	'<chart manageResize="1" origW="'+conf.offsetwidth+'" origH="'+conf.offsetheight+'"  palette="2" bgAlpha="0" bgColor="AEC0CA,FFFFFF" numberSuffix="" \n\
	 	showBorder="0" basefontColor="FFFFDD" chartTopMargin="5" chartBottomMargin="5" \n\
		toolTipBgColor="009999" gaugeFillMix="{dark-10},{light-70},{dark-10}" gaugeFillRatio="3" pivotRadius="8" gaugeOuterRadius="120" gaugeInnerRadius="70%"  \n\
		gaugeOriginX="'+absWidth+'" gaugeOriginY="'+absHeight+'" trendValueDistance="5" tickValueDistance="3" manageValueOverlapping="1" autoAlignTickValues="1">'+str+'\n\
   	<annotations>\n\
      <annotationGroup id="Grp1" showBelow="1" showShadow="1">\n\
         <annotation type="rectangle" x="$chartStartX+5" y="$chartStartY+5" toX="$chartEndX-5" toY="$chartEndY-5" radius="10" fillColor="333333, 453269" showBorder="0" /> \n\
         <annotation type="text" x="'+absWidth+'" y="20" label="'+conf.caption+'" fontColor="CCCCCC" fontSize="14"/> \n\
      </annotationGroup>\n\
   	</annotations>\n\
   	<styles>\n\
      <definition>\n\
         <style name="RectShadow" type="shadow" strength="3" /> \n\
		 		 <style name="trendvaluefont" type="font" bold="1" borderColor="FFFFDD" /> \n\
      </definition>\n\
      <application>\n\
         <apply toObject="Grp1" styles="RectShadow" /> \n\
		  	 <apply toObject="Trendvalues" styles="trendvaluefont" /> \n\
      </application> \n\
   	</styles>\n\
	</chart>'; 
return dataString;
};

ChartModel.prototype.toExternalImageXml = function() {	
	var conf = this.config;
	var absWidth = conf.offsetwidth / 2;
	var absHeight = conf.offsetheight - (conf.offsetheight / 4); 
	var str = '';
	str += this.toColorRangeXml();
	str += this.toDialsetXml();
	var dataString =
	'<chart manageResize="1" origW="'+conf.offsetwidth+'" origH="'+conf.offsetheight+'"  manageValueOverlapping="1" autoAlignTickValues="1"  bgColor="AEC0CA,FFFFFF" fillAngle="45" \n\
		majorTMNumber="10" majorTMHeight="8" showGaugeBorder="0" gaugeOuterRadius="140" gaugeOriginX="'+absWidth+'" gaugeOriginY="'+absHeight+'" gaugeInnerRadius="2" \n\
		formatNumberScale="1" numberPrefix=""  decmials="2" tickMarkDecimals="1" pivotRadius="17" showPivotBorder="1" pivotBorderColor="000000" \n\
		pivotBorderThickness="5" pivotFillMix="FFFFFF,000000" tickValueDistance="10" >'+str+' \n\
			<annotations>\n\
				<annotationGroup x="'+absWidth+'" y="'+absHeight+'">\n\
					<annotation type="circle" x="0" y="2.5" radius="150" startAngle="0" endAngle="180" fillPattern="linear" fillAsGradient="1" fillColor="dddddd,666666" fillAlpha="100,100"  fillRatio="50,50" fillAngle="0" showBorder="1" borderColor="444444" borderThickness="2"/>\n\
					<annotation type="circle" x="0" y="0" radius="145" startAngle="0" endAngle="180" fillPattern="linear" fillAsGradient="1" fillColor="666666,ffffff" fillAlpha="100,100"  fillRatio="50,50" fillAngle="0" />\n\
				</annotationGroup>\n\
    		<annotationGroup id="Grp1" showBelow="1" showShadow="1">\n\
       		<annotation type="rectangle" x="$chartStartX+5" y="$chartStartY+5" toX="$chartEndX-5" toY="$chartEndY-5" radius="10" fillColor="333333, 453269" showBorder="0" />\n\
       		<annotation type="text" x="'+absWidth+'" y="20" label="'+conf.caption+'" fontColor="CCCCCC" fontSize="14"/>\n\
    		</annotationGroup>\n\
			</annotations>\n\
		</chart>'; 
return dataString;
};