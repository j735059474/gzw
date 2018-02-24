/**
 * 统计图表控件（基于FusionCharts）
 * 
 * @fileoverview 
 * 
 * @auther
 * @version NC6.0
 *  
 */
var GALLERY_RENDERER = "flash";
var tmpChart = new FusionCharts("Column2D.swf", "tmpChartId", "560", "400", "0", "0");
var NO_FLASH = tmpChart.options.renderer=="javascript";
tmpChart.dispose();
delete tmpChart;
var Chartindex = 1;
tmpFlash = null;
if(NO_FLASH || /GALLERY_RENDERER=javascript/i.test(document.cookie) )
{
	GALLERY_RENDERER = 'javascript';
}
FusionCharts.setCurrentRenderer(GALLERY_RENDERER);

ChartComp.prototype = new BaseComponent;
ChartComp.prototype.componentType = "CHART";

/**
 * 构造方法
 * @class
 */
function ChartComp(parent, name, left, top, width, height, chartconfig, position, className) {
	debugger;
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	var seriesType = getString(chartconfig.seriesType, ChartConfig.SINGLE_SERIES);
//	if(seriesType == ChartConfig.Angular_SERIES){
//		width = "186px";
//		height = "189px";
//	}
	
	this.base(name, left, top, width, height);	
	this.parentOwner = parent;
	this.chartconfig = chartconfig;
	this.position = getString(position, "relative");
	this.className = getString(className, "chart_div");	
	this.create();
	this.isshow  = false;
	this.chartindex =Chartindex;
};

/**
 * 主体创建函数
 */
ChartComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id + "_div";
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	//this.Div_gen.className = this.className;
	//this.Div_gen.style.background = "yellow";
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 二级回调函数
 */
ChartComp.prototype.manageSelf = function() {
	if(document.getElementById(this.Div_gen.id))
		this.createChart();
//	this.chart.onclick = function() {
//		oThis.onclick();
//	};
};
ChartComp.prototype.createChart = function(){
	Chartindex += 1;
	this.chart = FusionCharts(this.id);
	if(this.chart){//为解决fusionchart 在IE下吃内存的问题
		this.chart.dispose();
	}
	this.chartindex = Chartindex;
	//this.chart = new FusionCharts(window.frameGlobalPath + "/ui/ctrl/chart/fusionchart/swf/" + this.chartconfig.showType + ".swf", this.id + this.chartindex + "", this.Div_gen.offsetWidth, this.Div_gen.offsetHeight, "0", "0");
	this.chart = new FusionCharts(window.frameGlobalPath + "/ui/ctrl/chart/fusionchart/swf/" + this.chartconfig.showType + ".swf", this.id, this.Div_gen.offsetWidth, this.Div_gen.offsetHeight, "0", "0");
	this.chart.addEventListener("BeforeLinkedItemOpen",ChartComp_BeforeLinkedItemOpen);
	this.chart.addEventListener("BeforeLinkedItemOpen",ChartComp_Error);
};

ChartComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

ChartComp.prototype.onModelChanged = function(event) {
	var g = this.owner;
	// 行选中时
	if (RowSelectEvent.prototype.isPrototypeOf(event)) {
	}
	// 行选中撤销事件
	else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
	}
	// cell数据改变时
	else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
	}
	// 违反校验规则的事件
	else if (DataCheckEvent.prototype.isPrototypeOf(event)) {
		
	}
	// 整页数据更新
	else if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		this.convertData();
		this.show();
	}
	// 插入新数据行
	else if (RowInsertEvent.prototype.isPrototypeOf(event)) {
		
	}
	// 删除行
	else if (RowDeleteEvent.prototype.isPrototypeOf(event)) {
		
	}
	else if (StateClearEvent.prototype.isPrototypeOf(event)) {
	}
};
/**
 * 行钻取暂用dataset行选中实现
 */
ChartComp.prototype.drillData = function(rowid){
	if(this.chartconfig.captionUrl){
		var js = this.chartconfig.captionFunc + "('" 
			+ this.chartconfig.captionUrl + "','" +  this.id+ "','" 
			+ this.chartconfig.caption +  "') ";
		eval(js);
	}
	else if(this.dataset){
		 var index = this.dataset.getRowIndexById(rowid);
		 this.dataset.setRowSelected(index);
	}
};
ChartComp.prototype.OpenTitle = function(url,chrtid){
	
};
function ChartComp_BeforeLinkedItemOpen(e,args){
	stopAll(e);
};
function FC_Error(event,arg){	
	return false;
};
function ChartComp_Error(event,arg){
	
};
/**
 * 公共钻取函数
 */
function ChartComp_DrillData(WidgetID,CmpID,rowid){
	var widget =  pageUI.getWidget(WidgetID);
	var chartcmp = widget.getComponent(CmpID);
	chartcmp.drillData(rowid);
};
/**
 * 绑定数据集
 */
ChartComp.prototype.setDataset = function(dataset) {
	this.dataset = dataset;
	dataset.bindComponent(this);
};

/**
 * 将dataset数据转换为图表所需数据
 */
ChartComp.prototype.convertData = function() {
	try
	{
		var convertor = new ChartModelConvertor(this.dataset, this.chartconfig,this);
		var chartModel = convertor.convert();
		var rows = this.dataset.getRows();
		if(rows){
			var xml = chartModel.toXml();
			//this.createChart();
			this.setDataXML(xml);
		}
		else
			this.setDataXML("");
	}
	catch(e){
		Logger.error(e);
	}
	
};
/**
*	刷新数据
*/
ChartComp.prototype.refreshData = function(){
		this.convertData();
		this.show();
};
/**
 * 设置XML数据
 */
ChartComp.prototype.setDataXML = function(xml) {
	this.chart.setXMLData(xml);
};

/**
 * 设置XML文件地址
 */
ChartComp.prototype.setDataURL = function(url) {
	this.chart.setDataURL(url);
};

/**
 * 显示控件
 */
ChartComp.prototype.show = function() {
		
		//if(this.isshow)
			//return;
		var rows = this.dataset.getRows();
		if(rows){
			try{
				this.chart.render(this.Div_gen.id);
				//this.isshow = true;
			}
			catch(e){
				
				//Logger.error(e);
			}
		}
	
};


/**
 * 是否激活
 */
ChartComp.prototype.setActive = function(flag) {
};

/**
 * 设置图像控件的显隐
 * 
 * @param  visible 
 */
ChartComp.prototype.setVisible = function(visible) {
	var temp = true;
	if(this.Div_gen.children && this.Div_gen.children.length > 0){
		if(this.Div_gen.children[0].tagName && this.Div_gen.children[0].tagName == "OBJECT"){
			//如果图像控件嵌入的是flash，显示和隐藏功能，使用flash本身标签的属性设置。
			temp = false;
		}
	}
	if(temp){
		if(visible == false){
			this.Div_gen.style.display = "none";
		}else{
			this.Div_gen.style.display = "";
		}
	}else{
		if(this.Div_gen.style.display == "none"){
			this.Div_gen.style.display = "block";
		}
		if(visible == false){
			this.Div_gen.children[0].style.visibility = "hidden";
		}else{
			this.Div_gen.children[0].style.visibility = "visible";
		}
	}
};
/**
 * 销毁控件
 * 
 * @private
 */
ChartComp.prototype.destroySelf = function() {
/****/
	delete this.chartconfig ; 
	this.chartconfig = null;
	if(this.chart){
		//this.chart.dispose();
		delete this.chart; 
	}
	BaseComponent.prototype.destroySelf.call(this);
};

// 单系列图形
ChartConfig.SINGLE_SERIES = "single-series";
// 多系列图形
ChartConfig.MULTI_SERIES = "multi-series";
//仪表盘
ChartConfig.Angular_SERIES = "angular";
//双Y轴类型
ChartConfig.Dual_SERIES = "dual";
//地图类型
ChartConfig.Map_SERIES = "map";
//
ChartConfig.Cylinder_SERIES = "cylinder";

//仪表盘
//名称列
ChartConfig.Angular_Column_Type = "type"; 
//开始值列
ChartConfig.Angular_Column_Start = "start";
//结束值列
ChartConfig.Angular_Column_End = "end";
//颜色列
ChartConfig.Angular_Column_Color = "color";
//颜色区间
ChartConfig.Angular_Type_Range = "range";
//指针
ChartConfig.Angular_Type_Dial = "dial";

//水晶柱图
//名称列
ChartConfig.Cylinder_Column_VALUE = "value";
ChartConfig.Cylinder_Column_ValueTYPE = "type";
	
ChartConfig.Cylinder_TYPE_UPPERLIMIT="upperLimit";
ChartConfig.Cylinder_TYPE_LOWERLIMIT="lowerLimit";
ChartConfig.Cylinder_TYPE_VALUE="value";
ChartConfig.Cylinder_TYPE_COLOR="color";

//显示类型
//环形2D
ChartConfig.ShowType_Doughnut_2D = "Doughnut2D";
//环形3D
ChartConfig.ShowType_Doughnut_3D = "Doughnut3D";
/**
 * 
 */
function ChartConfig(showType, seriesType, caption, numberPrefix, groupColumn, groupName, seriesColumns, seriesNames, xAxisName, yAxisName,datacolumn,isdrill) {
	// 显示图像类型
	this.showType = showType;
	// 图表显示系列类型（ChartConfig.SINGLE_SERIES、ChartConfig.MULTI_SERIES）
	this.seriesType = seriesType;
	// 图表标题
	this.caption = caption;
	// 统计结果数字前缀
	this.numberPrefix = numberPrefix;
	// 分组列
	this.groupColumn = groupColumn;
	// 分组列图表显示名称
	this.groupName = groupName;
	// 统计列
	if (seriesColumns != null)
		this.seriesColumns = seriesColumns.split(",");
	// 统计列图表显示名称
	if (seriesNames != null)
		this.seriesNames = seriesNames.split(",");
	// 横轴显示文字
	this.xAxisName = xAxisName;
	// 纵轴显示文字
	this.yAxisName = yAxisName;
	//数据列
	this.dataColumn =  datacolumn;
	//tooltext列
	this.tooltextColumn = null;
	//显示值列
	this.displayvalueColumn = null;
	
	//是否钻取
	this.isdrill = isdrill;
	
	this.offsetwidth = null;
	this.offsetheight = null;
	this.sizescale = null;  
	
	//仪表盘属性
	//仪表盘外半径
	this.outradius = null;
	//仪表盘内半径
	this.innerradius = null;
	//指针半径
	this.dialradius = null;
	//最大值
	this.maxvalue = null;
	//最小值
	this.minvalue = null;
	//将控件的宽度的20分之一
	this.saclevalue = null;
	//位置调节
	this.gaugeOriginX = null;
	this.gaugeOriginY = null;
	this.dialValue = null;
	this.dialColor = null;
	this.datasetrender = null;
	
	this.seriesRenders = null;
	this.seriesParentYAxis = null;
	
	this.SYAxisName = null;
	//标题字体
	this.captionFont =null;
	//X轴字体
	this.xAxisFont = null;
	//X轴刻度
	this.xLabelFont = null;
	//y轴字体
	this.yAxisFont = null;
	//y轴刻度
	this.yLabelFont = null;
	//图例
	this.legendFont = null;
	//bgcolor
	this.bgcolor = null;
	//标题连接
	this.captionUrl = null; 
	this.captionFunc = null;
	this.clickUrl = null;
	
	//PYAxisMaxValue,SYAxisMinValue,SYAxisMaxValue,PYAxisMinValue
	//yAxisMinValue ,yAxisMaxValue
	this.yAxisMaxValue = null;
	this.yAxisMinValue = null;
	this.syAxisMaxValue = null;
	this.syAxisMinValue = null;
	
	//values	
	this.numberSuffix = null;
	/**
	 * 0 : AUTO : 自动
	 * 1 : WARP 横向 自动隐藏 
	 * 2 : ROTATE ： 竖向
	 * 3 : slantLabels 竖向 45度
	 * 4 : Stagger line 2 横向 2层
	 * 	 * 
	 */
	this.labelDisplay = 0;
	
	this.showvalues = 0;
	this.showLabels = 0;
	this.drawanchors = 0;
	this.showLegend = 1;
	
	this.showbg = true;
	this.showBorder = 1;
	//仪表盘
	this.majorTMNumber = 1;
	this.showpoint =0;
	
	//pie rscale
	this.rscale = null;
	
	this.colorlist = new Array("45B3D4","FFD000","A6D04B",
		"E086DE","F99D61","6484FD","BF8403","4BD09C","FA91BB",
		"D4B3FD","FDD493","F36062","056C8B","CFAA02","3DA01E",
		"C52E30","874CE0","FE8900","2143C4","117C73");
	
	//水晶柱图
	this.cylinder_upperLimit =0;
	this.cylinder_lowerLimit=0;
	this.cylinder_value =0;
	this.cylinder_color= "000000" ;
};
/**
 * 设置render
 * @param {} render
 */
ChartConfig.prototype.SetRender = function(render){
	this.datasetrender = render;
};
/**
 * 增加序列级render
 */
ChartConfig.prototype.addSeriesRender= function(seriesName,renderType){
	if(this.seriesRenders == null){
		this.seriesRenders = new HashMap();		
	}
	this.seriesRenders.put(seriesName,renderType);
};

/**
 * 获取序列级render
 * @param {} seriesName
 * @return {}
 */
ChartConfig.prototype.getSeriesRender = function(seriesName){
	var value = null
	if(this.seriesRenders)
		if(this.seriesRenders.containsKey(seriesName))
			value  = this.seriesRenders.get(seriesName);
	return value;
};
/**
 * 增加序列级所属Y轴
 */
ChartConfig.prototype.addSeriesParentYAxis= function(seriesName,parentYAxis){
	if(this.seriesParentYAxis == null){
		this.seriesParentYAxis = new HashMap();		
	}
	this.seriesParentYAxis.put(seriesName,parentYAxis);
};

/**
 * 获取序列级所属Y轴
 * @param {} seriesName
 * @return {}
 */
ChartConfig.prototype.getSeriesParentYAxis = function(seriesName){
	var value = null
	if(this.seriesParentYAxis)
		if(this.seriesParentYAxis.containsKey(seriesName))
			value  = this.seriesParentYAxis.get(seriesName);
	return value;
};
//
/**
 * 设置SY轴名称
 * @param {} sYAxisName
 */
ChartConfig.prototype.setSYAxisName = function(sYAxisName){
	this.SYAxisName = sYAxisName;
};
//标题字体
ChartConfig.prototype.setCaptionFont = function(font){
	this.captionFont = font
};
//X轴字体
ChartConfig.prototype.setxAxisFont = function(font){
	this.xAxisFont = font
};
//X轴刻度
ChartConfig.prototype.setxLabelFont = function(font){
	this.xLabelFont = font
};
//Y轴字体
ChartConfig.prototype.setyAxisFont = function(font){
	this.yAxisFont = font
};
//y轴刻度
ChartConfig.prototype.setyLabelFont = function(font){
	this.yLabelFont = font
};
//图例
ChartConfig.prototype.setylegendFont = function(font){
	this.legendFont = font
};
//bgcolor
ChartConfig.prototype.setbgcolor = function(bgcolor){
	this.bgcolor = bgcolor
};
ChartConfig.prototype.setCaptionUrl = function(captionurl){
	this.captionUrl = captionurl
};
ChartConfig.prototype.setCaptionFunc = function(captionFunc){
	this.captionFunc = captionFunc
};

ChartConfig.prototype.setRScale = function(rscale){
	this.rscale = rscale;
};
ChartConfig.prototype.setmajorTMNumber = function(mtm){
	if(mtm)	
		this.majorTMNumber = mtm;
};
ChartConfig.prototype.setshowpoint = function(showpoint){
		this.showpoint = showpoint;
};
ChartConfig.prototype.setlabelDisplay = function(labelDisplay){
		this.labelDisplay = labelDisplay;
};
ChartConfig.prototype.setnumberSuffix = function(numberSuffix){
	this.numberSuffix = numberSuffix;
};
ChartConfig.prototype.setshowvalues = function(showvalues){
	this.showvalues = showvalues;
};
ChartConfig.prototype.setshowLabels = function(showLabels){
	this.showLabels = showLabels;
};

ChartConfig.prototype.setshowLegend = function(showLegend){
	this.showLegend = showLegend;
};

ChartConfig.prototype.setdrawAnchors = function(drawanchors){
	this.drawanchors = drawanchors;
};
ChartConfig.prototype.setyAxisMaxValue = function(yAxisMaxValue){
	this.yAxisMaxValue = yAxisMaxValue;
};
ChartConfig.prototype.setyAxisMinValue = function(yAxisMinValue){
	this.yAxisMinValue = yAxisMinValue;
};
ChartConfig.prototype.setsyAxisMaxValue = function(syAxisMaxValue){
	this.syAxisMaxValue = syAxisMaxValue;
};
ChartConfig.prototype.setsyAxisMinValue = function(syAxisMinValue){
	this.syAxisMinValue = syAxisMinValue;
};
ChartConfig.prototype.setsNumberSuffix = function(sNumberSuffix){
	this.sNumberSuffix = sNumberSuffix;
};
ChartConfig.prototype.setcolors = function(colors){
	this.colorlist = colors.split(",");
};
ChartConfig.prototype.settooltextColumn = function(tooltextColumn){
	this.tooltextColumn = tooltextColumn;
};
ChartConfig.prototype.setdisplayvalueColumn = function(displayvalueColumn){
	this.displayvalueColumn = displayvalueColumn;
};
ChartConfig.prototype.setdisplayvalueColumn = function(displayvalueColumn){
	this.displayvalueColumn = displayvalueColumn;
};
ChartConfig.prototype.setshowbg = function(showbg){
	this.showbg = showbg;
};
ChartConfig.prototype.setshowBorder = function(showBorder){
	this.showBorder = showBorder;
};