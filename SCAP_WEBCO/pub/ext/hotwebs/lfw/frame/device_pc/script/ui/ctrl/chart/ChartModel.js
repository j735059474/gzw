/**
 * 统计图表数据模型
 * @param config 图表配置信息
 */
function ChartModel(config) {
	this.config = config;
	this.categories = null;
	this.datasets = null;
	this.colorrange = null;//仪表盘专用，颜色区间
	this.dials = null;//仪表盘专用，指针合集
	this.styles = null;//样式
	this.styleapplies = null;//样式应用
	//地图配置
	this.mapColorRangeSet = null;
	this.mapEntitySet = null;
	this.mapAttr = null;
	
//	this.mapXmldata = "";
	this.ShowType_Map_2D = "FCMap_China2";

	this.mapcolorrange= null;
	this.entity=null;
};
/**
* 构造地图的xml数据
* author:denghw,lisw
*/
ChartModel.prototype.toMapXml=function(){	
	var allStr = '<map animation="1" showBevel="1" includeValueInLabels="1" labelSepChar=": "';
    allStr += 'baseFontSize="12" fillAlpha="70" hoverColor="00FFFF" borderColor="FFFFFF" legendPosition="BOTTOM" showLabels="1">';
	
	allStr += this.mapColorRangeSet.toXml();
	allStr += this.mapEntitySet.toXml();
	
	allStr += '</map>';
	
	return allStr;
	
	
	//return this.mapXmldata;
};
ChartModel.prototype.setColorRangeSet = function(colorRangeSet)
{
	this.mapColorRangeSet = colorRangeSet;
}
ChartModel.prototype.setEntitySet = function(entitySet)
{
	this.mapEntitySet = entitySet;
}
ChartModel.prototype.setMapAttr = function(mapAttr)
{
	this.mapAttr = mapAttr;
}
ChartModel.prototype.toXml = function() {
	// 系列类型
	var seriesType = getString(this.config.seriesType, ChartConfig.SINGLE_SERIES);
	var showType = this.config.showType;
	var bgcolor = this.config.bgcolor;
	/*
	if(!bgcolor)
		bgcolor = "FFFFFF";
	}
	*/
	var showBorder = this.config.showBorder;
	/*
	if(seriesType != ChartConfig.Angular_SERIES && seriesType != ChartConfig.Cylinder_SERIES){
		bgcolor = "ededed,fefefe"
		showBorder = "1";
	}*/
	
	//var str = ""; 
	var str = '';
	if(seriesType == ChartConfig.Map_SERIES){
		return this.toMapXml();
	}else if(seriesType == ChartConfig.Angular_SERIES){
			return this.toAngularXml(bgcolor);
	}else if(seriesType == ChartConfig.Cylinder_SERIES) {
			return this.toCylinderXml(bgcolor);
	}else if(showType == "Radar") {
		if(bgcolor) {
			return this.toRadarXml(bgcolor);
		}
	}

	
	str += '<chart bgColor="FFFFFF"  bgAngle="270" borderThickness = "1" showBorder = "'+showBorder+'" borderColor="dce3e5"  useRoundEdges ="1" '
		+ ' canvasbgColor="FFFFFF"  canvasBorderThickness="1" canvasBorderColor="A5A6A6"   divLineIsDashed="1" divLineColor="E8E8E9" '
		+ ' zeroPlaneColor="000000" zeroPlaneThickness="1"  zeroPlaneAlpha="80" '
		+ ' showAlternateHGridColor="1" AlternateHGridColor="F7F7F7" '
		+ ' showPrintMenuItem="0" showAboutMenuItem="0" aboutMenuItemLabel = "关于用友" aboutMenuItemLink="n-http://www.ufida.com" ' 
		+ ' numberScaleValue="10000,10000" showValues="1" numberScaleUnit ="万,亿" numberPrefix="' + this.config.numberPrefix + '" '
		+ ' drawAnchors  = "'+this.config.drawanchors +'" '
		+ ' showLegend = "' + this.config.showLegend + '" ';
		
	if(this.config.numberSuffix != null)
		str += ' numberSuffix = "' + this.config.numberSuffix + '" ';
			//'labelDisplay="Rotate" slantLabels="1"  '
	if(this.config.labelDisplay == 0){
		str += ' labelDisplay="AUTO" ';
	}
	else if(this.config.labelDisplay == 1){
		str += ' labelDisplay="WARP" ';
	}
	else if(this.config.labelDisplay == 2){
		str += ' labelDisplay="ROTATE" ';
	} 
	else if(this.config.labelDisplay == 3){
		str += ' labelDisplay="ROTATE" slantLabels="1"  ';
	} 
	else if(this.config.labelDisplay == 4){
		str += ' labelDisplay="Stagger" staggerLines="1" ' ;
	} 
	if(!this.config.showbg){
		str +=' bgAlpha="0,0" ';
	}


	if(this.config.clickUrl){
		var clickurl = this.config.clickUrl;
		//str += ' clickURL= "n-' + this.config.captionUrl + '"';
		str += ' clickURL= "' + clickurl + '" ';
	}
	var fontsize1 = 8;
	var fontsize2 = 12;
	var fontsize3 = 12;
	if(seriesType == ChartConfig.Angular_SERIES){
		var scale = this.config.sizescale;
		if(scale > 1.5)
			scale *= 0.8;
		var fontsize1 = parseInt(10 * scale);
		if(fontsize1 < 10)
			fontsize1 = 10
	
		var fontsize2 = parseInt(12 * scale);
		if(fontsize2 < 10)
			fontsize2 = 10;
			
		var fontsize3 = parseInt(12 * scale);
		if(fontsize3 < 12)
			fontsize3 = 12;
		
		if(GALLERY_RENDERER == 'javascript')
			str += ' displayValueDistance="10" minorTMHeight="6" ';
		else
			str += ' displayValueDistance="20" minorTMHeight=" 10" ';
		
		str += ' showShadow="0" fillAngle="45" upperLimit="' +
				this.config.maxvalue + '" lowerLimit= "' +
				this.config.minvalue + '" ' +
				'majorTMNumber="'+ this.config.majorTMNumber	+
				'" majorTMColor="767474" minorTMNumber="1"  ' +
				'minorTMColor="767474" showGaugeBorder="0" placeValuesInside="0" ' +
				'gaugeOuterRadius="63" gaugeInnerRadius="41" gaugeOriginX="93.5" gaugeOriginY="106.5" ' +
				'formatNumberScale="1"  decimalPrecision="2" tickMarkDecimalPrecision="2" ' +
				'pivotRadius="0" showPivotBorder="0" pivotBorderColor="fcf9f9" pivotBorderThickness="17" ' +
				'pivotBorderAlpha="50" pivotFillColor="b5b4b4" pivotFillMix="{818c92},{818c92},{light-10},{light-10},{light-50},{light-60},{eae9e9}" pivotFillRatio="15,5,5,15,50,7,3" pivotFillType="radial" '+
				'gaugeStartAngle="218" gaugeEndAngle="-38" showTickMarks="0" showTickValues="1" '+ 
				'gaugeFillMix="{dark-2},{light-2},{light-2},{light-20},{light-20},{light-80},{light-10},{dark-20}" ' +
				'gaugeFillRatio="5,5,20,30,10,20,5,5" autoScale="1" origW="186" origH="189" adjustTM="0">'
	}
	else if(seriesType == ChartConfig.Dual_SERIES){
		
		str += ' caption="' + this.config.caption + '" ' 
		if(this.config.xAxisName)	
			str += ' xAxisName="' + this.config.xAxisName + '" ';
		if(this.config.yAxisName)	
			str +=  '  PYAxisName="' + this.config.yAxisName  + '" ';
		if(this.config.SYAxisName)
			str += '  SYAxisName="' + this.config.SYAxisName + '"  ';
			
		str += ' showValues="'+ this.config.showvalues +'" ';
		str += ' showLabels="'+ this.config.showLabels +'" ';  
		if(this.config.yAxisMaxValue){
			str += ' PYAxisMaxValue = "' + this.config.yAxisMaxValue + '" ';
		}
		if(this.config.yAxisMinValue){
			str += ' PYAxisMinValue = "' + this.config.yAxisMinValue + '" ';
		}
		if(this.config.syAxisMaxValue){
			str += ' SYAxisMaxValue = "' + this.config.syAxisMaxValue + '" ';
		}
		if(this.config.syAxisMinValue){
			str += ' SYAxisMinValue = "' + this.config.syAxisMinValue + '" ';
		}
		if(this.config.sNumberSuffix){
			str += ' sNumberSuffix = "' + this.config.sNumberSuffix + '" ';	
		}
	
	}
	else if(seriesType == ChartConfig.Cylinder_SERIES){
		str += ' manageResize="1"  bgAlpha="0"  showTickMarks="1" showTickValues="1" showLimits="1" '
		+ ' decmials="0" chartLeftMargin="15" chartRightMargin="15" chartTopMargin="15" autoScale="1" origW="140" origH="180" ';
		str += '  lowerLimit="'+ this.config.cylinder_lowerLimit
			+ '" upperLimit="'+  this.config.cylinder_upperLimit
			+ '"  baseFontColor="'+ this.config.cylinder_color
			+ '" cylFillColor="'+ this.config.cylinder_color
  		    + '" caption="' + this.config.caption 
			+ '"  ';
	}
	else{
		str += ' caption="' + this.config.caption + '" ' ;
		if(this.config.xAxisName)	
			str += ' xAxisName="' + this.config.xAxisName + '" ';
		if(this.config.yAxisName)	
			str +=  ' yAxisName="' + this.config.yAxisName  + '" ';
			
		
		str += ' showValues="'+ this.config.showvalues +'" ';
		str += ' showLabels="'+ this.config.showLabels +'" ';  
		if(this.config.yAxisMaxValue){
			str += ' YAxisMaxValue = "' + this.config.yAxisMaxValue + '" ';
		}
		if(this.config.yAxisMinValue){
			str += ' YAxisMinValue = "' + this.config.yAxisMinValue + '" ';
		}
	}
	if(showType == ChartConfig.ShowType_Doughnut_2D || showType == ChartConfig.ShowType_Doughnut_3D){
		if(this.config.rscale){
			var r = 0;
			if(this.config.offsetwidth > this.config.offsetheight * 1.3){
				r = this.config.offsetheight / 2.8;
			}
			else{
				r = this.config.offsetwidth / 3;
			}
			if(r && r> 0){
				var ir = r/this.config.rscale;
				if(ir > 0)
					str += ' pieRadius="' + r + '" doughnutRadius = "' + ir + '" ';
			}
		}
	}
		
	str += ' >\n';
	
	if (seriesType == ChartConfig.SINGLE_SERIES) {  // 单系列图形
		if (this.datasets != null && this.datasets.length >= 1) {
			str += this.datasets[0].toXml(this.config);
		}
		str += this.getStyleXml();
		str += '<styles>'
    		 + '<definition>'
      		 + '<style name="datalable" type="font" font="宋体"   italic="0" color="000000" bgColor="" isBold="0"/>'      		 
    		 + '</definition>'
    		 + '<application>'
    		 + '<apply toObject="DATALABELS" styles="datalable" />'      		 
    		 + '</application>'
  			 + '</styles>';
	}
	else if (seriesType == ChartConfig.MULTI_SERIES || seriesType == ChartConfig.Dual_SERIES) {  // 多系列图形
		if (this.categories != null) {
			str += "<categories>\n";
			for (var i = 0; i < this.categories.length; i ++) {
				str += this.categories[i].toXml();
			}
			str += "</categories>\n";
			
		}
		if (this.datasets != null) {
			
			for (var i = 0; i < this.datasets.length && i<20; i ++) {
				var dataset = this.datasets[i];
				str += '<dataset seriesName="' + dataset.seriesName + '" ';
				//获取render
				var renderas = this.config.getSeriesRender(dataset.seriesName);
				if(!renderas)
					if(this.config.datasetrender)
						renderas = this.config.datasetrender;
				
				if(renderas)
					str += ' renderAs = "' + renderas + '" ';

				//获取所属Y轴
				var parentYAxis =  this.config.getSeriesParentYAxis(dataset.seriesName);
				if(parentYAxis)
					str += ' parentYAxis = "' + parentYAxis + '" '
				str += ' color = "' + this.config.colorlist[i] + '" ';
				str +=' >\n';
				str += dataset.toXml();
				str += '</dataset>\n';
			}
		}
		str += this.getStyleXml();
		str += '<styles>'
    		 + '<definition>'
      		 + '<style name="datalable" type="font" font="宋体"  italic="0"  color="000000" bgColor="" isBold="0"/>'      		 
    		 + '</definition>'
    		 + '<application>'
    		 + '<apply toObject="DATALABELS" styles="datalable" />'      		 
    		 + '</application>'
  			 + '</styles>';
  			 
	}
	else if(seriesType == ChartConfig.Angular_SERIES){//仪表盘
		if(this.colorrange){
			var points = "<trendpoints> \n";
			str += " <colorRange>\n";
			var trendrmax = 70.0;
			var trendrmin = 58.0;
			for (var i = 0; i < this.colorrange.length; i ++) {				
				str += this.colorrange[i].toXml();
				if(this.colorrange[i].maxvalue && this.config.maxvalue){
					if(this.colorrange[i].maxvalue == this.config.maxvalue){
						continue;
					}
					var pr = 140;
					//仪表盘刻度点半径是 180度线最长，90度最短；需要计算出半径
					var angule =  218 - (this.colorrange[i].maxvalue - this.config.minvalue)/(this.config.maxvalue - this.config.minvalue) * 256.0;
					if(angule <= 0){
						angule *= -1;
					}
					else if(angule < 90){}
					else if(angule < 180){angule = 180 - angule;}
					else {angule = angule -180;};
					
					var trendr = trendrmax - (trendrmax - trendrmin)/90 * angule;
					/**/
					if( this.config.sizescale > 1){
						trendr = trendr/ Math.pow(this.config.sizescale,1/2);
						if(this.config.sizescale > 1.6)
							trendr *= 1.2;
						else
							trendr *= 1.1;
					}
					
					if(this.config.sizescale < 1)
						trendr *=  Math.pow(this.config.sizescale,1/3);
					else
						trendr *=  Math.pow(this.config.sizescale,1/4);
						
					points += "<point startValue='" + this.colorrange[i].maxvalue
							+ "' color='666666' thickness='0' alpha='0' valueInside='0' radius='" 
							+ trendr + "' /> \n";
				}
			}
			str += " </colorRange>\n";
			points += "</trendpoints>\n";
			if(this.config.showpoint == 1)
				str += points;
		}
		if(this.dials){
			
			str += " <dials> \n"
			for (var i = 0; i < this.dials.length; i ++) {
				str += this.dials[i].toXml(this.config.dialradius);
				
			}
			str += " </dials>\n";
			
		}
		
		str += ''
			+ '<annotations origW="186" origH="189" autoScale="1">'	
					
			+ '<annotationGroup id="Grp1" scaleImages="1">'
			+ '<annotation type="image" URL="' + window.frameGlobalPath + '/ui/ctrl/chart/angular.png" yScale="100" xScale="100"/>'
			+ '</annotationGroup>'
			+ '<annotationGroup id="Grp2" scaleImages="1" showBelowChart="0">'
			+ '<annotation type="image" URL="' +window.frameGlobalPath + '/ui/ctrl/chart/pivot.png" yScale="100" xScale="100"/>'
			+ '</annotationGroup>'
			
			+ '<annotationGroup id="Grp3" >'
			+ '<annotation type="arc" color="' 
			//+ this.dials[0].color + ',EEEEEE" x="93.5" y="106.5" radius="63" innerRadius="42" startAngle="240" endAngle="300" fillRatio="90,10"  fillPattern="Radial" showBorder="0"/>'
			+ this.dials[0].color + '" x="93.5" y="106.5" radius="63" innerRadius="42" startAngle="240" endAngle="300" fillRatio="100"  fillPattern="Radial" showBorder="0"/>'
    		+ '</annotationGroup>';
/*
		str += '<annotations origW="315" origH="315" autoScale="1">'
			+ '<annotationGroup xPos="155" yPos="148" showBelow="1">'
			+ '<annotation type="circle" color="EBF0F4,85898C,484C4F,C5C6C8" fillRatio="30,30,30,10" fillAngle="270" radius="150" fillPattern="linear" />'
			+ '<annotation type="circle"  color="8E8E8E,83878A,E7E7E7" fillAngle="270" radius="135" fillPattern="linear" />'
			+ '<annotation type="circle"  color="07476D,19669E,186AA6,D2EAF6" fillRatio="5,45,40,10" fillAngle="270" radius="132" fillPattern="linear" />'
			+ '<annotation type="circle"  color="07476D,19669E,07476D" fillRatio="5,90,5" fillAngle="270" radius="125" fillPattern="linear" />'
			+ '</annotationGroup>';
		 	*/
		 if(this.config.dialValue){
		 	/*
			str += '<annotationGroup xPos="118.7" yPos="157.2" showBelowChart="0">'
		 		+ '<annotation type="text"  label="' 
		 		//+  this.config.dialValue + '" fontColor="e4e4e6" fontSize="'
		 		+  this.config.dialValue + '" fontColor="ebebed" fontSize="'
		 		+ fontsize2 + '" isBold="1" font="Arial" />'
    			+ '</annotationGroup>';
		 	*/
		 	
		 	var nmsuffix = "";
		 	if(this.config.numberSuffix != null)
				nmsuffix = this.config.numberSuffix;
		 	str += '<annotationGroup xPos="93.5" yPos="157" showBelowChart="0">'
		 		+ '<annotation type="text"  label="' 
		 		//+  this.config.dialValue + '" fontColor="e4e4e6" fontSize="'
		 		+  this.config.dialValue + nmsuffix
		 		+ '" fontColor="ebebed" fontSize="'
		 		+ fontsize2 + '" isBold="1" font="Arial" />'
    			+ '</annotationGroup>';
    		
		 }
		 if(this.config.caption){
		 	 var captionsize;
		 	 if(this.config.captionFont)
		 	 	captionsize = this.config.captionFont.size;
		 	 //if(!captionsize)
		 	 	captionsize = fontsize3;
		 	 str += '<annotationGroup xPos="93.5" yPos="15" showBelowChart="0">'
		 	 	+ '<annotation type="text" label="' 
		 	 	+ this.config.caption +'" fontColor="676666"  fontSize="'
		 	 	+ captionsize + '" isBold="1"/>'
    			+ '</annotationGroup>';		
		 }
		 str += "</annotations>";
		 str += '<styles>'
    		 + '<definition>'
      		 + '<style name="values" type="font"  size="' 
      		 + fontsize1 +'" color="585656" bgColor="" />'
      		 + '<style name="limitvalues" type="font"  size="'
      		 + fontsize2 +'" color="585656"  bgColor="" />'
    		 + '</definition>'
    		 + '<application>'
    		 + '<apply toObject="TICKVALUES" styles="values" />'
      		 + '<apply toObject="LIMITVALUES" styles="limitvalues" />'
      		 + '<apply toObject="TRENDVALUES" styles="limitvalues" />'
    		 + '</application>'
  			 + '</styles>';
	}
	else if(seriesType == ChartConfig.Cylinder_SERIES){//水晶柱图
		str += '<value>'+this.config.cylinder_value+'</value>'
//			+ '<annotations>'
//			+ '<annotationGroup showBelow="1">'
//			+ '<annotation type="rectangle"  x="$chartStartX+1" y="$chartStartY+1" toX="$chartEndX-1" toY="$chartEndY-1" color="FFFFFF" alpha="100" showBorder="1" borderColor="CC0000" borderThickness="2" radius="10"/>'
//			+ '</annotationGroup>'
//			+ '</annotations>';
			var fontColor = this.config.captionFont.color;
        var fontSize = this.config.captionFont.size;
        var fontStyle = this.config.captionFont.font;
        str += '<styles>'
               + '<definition>'
               + '<style name="Cylinder_series_fontStyle" type="font"  font="'+fontStyle+'"  italic="0" color="'+fontColor+'" size="'+fontSize+'" isBold="0"/>'                  
               + '</definition>'
               + '<application>'
               + '<apply toObject="Caption" styles="Cylinder_series_fontStyle" />'                   
               + '</application>'
                      + '</styles>';

	}
	str += " </chart>\n";
	return str;
};

ChartModel.prototype.getStyleXml=function(){
	var str = "<styles> \n";
	if(this.styles){
		str += "<definition> \n";
		for(var i=0;i< this.styles.length;i++){
			str += this.styles[i].toXml();
		}
		str += "</definition> \n";
	}
	if(this.styleapplies){
		str += "<application> \n";
		for( var i=0;i< this.styleapplies.length;i++){
			str += this.styleapplies[i].toXml();
		}
		str += "</application> \n"; 
	}
	str += "</styles> \n";
	return str;
};

ChartModel.prototype.addCategory = function(cate) {
	if(this.categories == null)
		this.categories = new Array;
	this.categories.push(cate);
};

ChartModel.prototype.addChartDataset = function(ds) {
	if(this.datasets == null)
		this.datasets = new Array;
	this.datasets.push(ds);
};
ChartModel.prototype.addColorRange = function(range){
	if(this.colorrange == null)
		this.colorrange = new Array;
	this.colorrange.push(range);
};
ChartModel.prototype.addDial = function(dial){
	if(this.dials == null)
		this.dials = new Array;
	this.dials.push(dial);
};
ChartModel.prototype.getChartDataset = function(dsname) {
	var ds = null;
	if(this.datasets != null)
		for(var i=0;i<this.datasets.length;i++ ){
			if(this.datasets[i].seriesName == dsname){
				 ds = this.datasets[i];
				 break;
			}
		}
	return ds;
};
ChartModel.prototype.addStyle = function(style){
	if(this.styles == null)
		this.styles = new Array;
	this.styles.push(style);	
};
ChartModel.prototype.addStyleApply = function(apply){
	if(this.styleapplies == null)
		this.styleapplies = new Array;
		
	var flag = false;
	for(var i=0;i<this.styleapplies.length;i++){
		if(this.styleapplies[i].toObject == apply.toObject){
			this.styleapplies[i] = apply;
			flag = true;
			break;
		}
	}
	if(!flag)
		this.styleapplies.push(apply);
};
ChartModel.prototype.getStyleApply = function(toobject){
	var curapply = null;
	if(this.styleapplies){
		for(var i=0;i<this.styleapplies.length;i++){
			if(this.styleapplies[i].toObject == toobject){
				curapply = this.styleapplies[i];
				break;
			}			
		}
	}
	return curapply;
};
/**
 * 分组信息
 */
function Category() {
	this.label = null;
};

Category.prototype.toXml = function() {
	var str = '<category label="' + this.label + '"';
	str += ' />\n';
	return str;
};

/**
 * 图表数据集合
 */
function ChartDataset(seriesName) {
	this.seriesName = seriesName;
	this.chartsets = null;
};

ChartDataset.prototype.toXml = function(config) {
	var str = "";
	debug;
	if (this.chartsets != null) {
		for (var i = 0; i < this.chartsets.length ; i ++) {
			if(config ){
				var colorindex = i %  config.colorlist.length;
				str += this.chartsets[i].toXml(config.colorlist[colorindex]);
			}
			else
				str += this.chartsets[i].toXml();
		}
	}
	return str;
};

ChartDataset.prototype.addSet = function(set) {
	if (this.chartsets == null)
		this.chartsets = new Array;
	this.chartsets.push(set);
};

/**
 * 数据
 */
function ChartSet() {
	this.label = null;
	this.value = null;
	this.link = null;
	this.name = null;
	this.toolText = null; 
	this.displayvalue = null;
	//TODO
//	this.toolTip = null;
//	this.showLable = "1";
};

ChartSet.prototype.toXml = function(color) {
	var str = '<set value="' + this.value + '"';
	if (this.label != null)
		str += ' label="' + this.label + '"';
		
	if (this.name != null)
		str += ' name="' + this.name + '"';
	if (this.link != null)
		str += ' link = "' + this.link + '"';
	if(color)
		str += ' color = "' + color + '"';
	
	
	if (this.displayvalue != null)
		str += ' displayValue ="' + this.displayvalue  + '"';
		
	if (this.toolText  != null)
		str += ' toolText ="' + this.toolText  + '"';	
	
	
		

//if (this.toolTip != null)
//		str += ' toolTip="' + this.toolTip + '"';
//	if (this.showLable != null)
//		str += ' showLable="' + this.showLable + '"';
	str += ' />\n';
	return str;
};

/**
 * 字体
 * @param {} font 字体名称
 * @param {} size 大小
 * @param {} color 颜色
 */
function ChartFont(font,size,color,bgcolor){
	this.font = font;
	this.size = size;
	this.color  = color;
	this.bgcolor = bgcolor;
};
/**
 * 样式表
 */
function ChartStyle(){
	this.name = 'captionstyle';
	this.type = 'font';
	this.font = null;
	this.size = null;
	this.color = null;
	this.bgcolor = null; 
};
ChartStyle.prototype.setFont= function(name,font){
	this.name = name;
	this.type = 'font';
	this.font = font.font;
	this.size = font.size;
	this.color = font.color;
	this.bgcolor =font.bfcolor;
};
ChartStyle.prototype.toXml = function(){
	var str = '<style  name = "' + this.name;
		str += '" type = "' + this.type + '"';
		if(this.font)
			str += ' font = "' + this.font + '" '
		if(this.size)
			str += ' size = "' + this.size + '" '
		if(this.color)
			str += ' color = "' + this.color + '" '
		if(this.bgcolor)
			str += ' bgcolor = "' + this.bgcolor + '" '
		str += ' bold="1" /> \n'
	return str;
};
/**
 * 样式表应用
 */
function ChartStyleApply(){
	this.toObject = null;
	this.styles = null;	
};
ChartStyleApply.prototype.toXml = function(){
	var str = '<apply toObject="'+ this.toObject +'" '
		str += ' styles="' + this.styles + '" /> \n';
	return str;
};
ChartStyleApply.prototype.addStyles=function(style){
	if(style){
		if(this.styles)
			this.styles += "," + style
		else
			this.styles = style;
	}
};
/**
 * 对地图的图例进行封装,代表图例对象的组合
 * denghw
 */
function MapColorRangeSet()
{
	this.colorRangeSet = new Array;
};
MapColorRangeSet.prototype.addColorRange = function(mapColorRange)
{
	if(this.colorRangeSet == null)
	    this.colorRangeSet = new Array;
	this.colorRangeSet.push(mapColorRange);
};
MapColorRangeSet.prototype.toXml = function()
{
	var str = '<colorRange>';
	for(var i=0; i < this.colorRangeSet.length; i++)
	{
		str += this.colorRangeSet[i].toXml();
	}
	str += '</colorRange>';
	
	return str;
};

/**
 * 对地图的图例进行封装,代表一个图例对象
 * denghw
 */
function MapColorRange(minValue,maxValue,colorString,displayValue,alpha)
{
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.colorString = colorString;
	this.displayValue = displayValue;
	this.alpha = alpha;
};
MapColorRange.prototype.toXml = function()
{
	var str = '<color ';
	str += 'minValue="'+this.minValue+'" maxValue="'+this.maxValue+'" color="'+this.colorString;
	str += '" displayValue="'+this.displayValue+'" alpha="'+this.alpha+'"';
	
	str += '/>';
	
	return str;
};

/**
 * 对地图的所有区域进行封装,代表区域对象的组合
 * denghw
 */
function MapEntitySet()
{
	this.mapEntitySet = new Array;
};
MapEntitySet.prototype.addEntity = function(mapEntity)
{
	if(this.mapEntitySet == null)
		this.mapEntitySet = new Array;
	this.mapEntitySet.push(mapEntity);
};
MapEntitySet.prototype.toXml = function()
{
	var str = '<data>';
	for(var i=0; i < this.mapEntitySet.length; i++)
	{
		str += this.mapEntitySet[i].toXml();
	}
	str += '</data>';
	
	return str;
};

/**
 * 对地图的区域进行封装,代表一个区域对象
 * denghw
 */
function MapEntity(Id,value,displayValue,toolText,color,link)
{
	this.Id = Id;
	this.value = value;
	this.displayValue = displayValue;
	this.toolText = toolText;
	this.color = color;
	this.link =link;
};
MapEntity.prototype.toXml = function()
{
	var str = '<entity ';
	str += 'id="CN.'+this.Id+'" value="'+this.value+'" displayValue="'+this.displayValue;
	str += '" toolText="'+this.toolText+'" color="'+this.color+'"';
	if(this.link){
		str += ' link="'+this.link+'" ';
	}
	str += '/>';
	
	return str;
};

