/**
 * 图表数据模型转换器
 * @author guoweic
 */
function ChartModelConvertor(dataset, config,chart) {
	this.dataset = dataset;
	this.config = config;
	this.chart = chart;	
};

/**
 * 转换
 */
ChartModelConvertor.prototype.convert = function() {
	// 图表显示系列类型
	var seriesType = this.config.seriesType;
	// 分组列
	var groupColumn = this.config.groupColumn;	
	// 统计列
	var seriesColumn = this.config.seriesColumns;
	//数据列
	var dataColumn = this.config.dataColumn;
	// 将dataset数据分组统计计算
	//var groupedDatas = this.calculateDatas(seriesType,groupColumn, seriesColumns,dataColumn);
	// 转换为图表模型
	//var model = this.convertToModel(seriesType, groupedDatas, seriesNames);
	var model = this.generatorModel(seriesType,groupColumn, seriesColumn,dataColumn);
	return model;
};
/**
 * 计算model
 */
ChartModelConvertor.prototype.generatorModel = function(seriesType,groupColumn, seriesColumn,dataColumn){
	var model = new ChartModel(this.config);
	var rows = this.dataset.getRows();
	if(!rows)
		return model;
	if(this.config.captionUrl && this.config.captionFunc){
		this.config.clickUrl = "JavaScript: isJavaScriptCall=true;ChartComp_DrillData('"
			 		+ this.chart.widget.id+"','"
			 		+ this.chart.id +"','');";
	}
	
	this.config.offsetwidth = this.chart.Div_gen.offsetWidth;
	this.config.offsetheight = this.chart.Div_gen.offsetHeight;	
	if(this.config.offsetwidth == 0)
		if(document.getElementById(this.chart.Div_gen.id))
			this.config.offsetwidth = document.getElementById(this.chart.Div_gen.id).offsetWidth;
	if(this.config.offsetheight == 0)
		if(document.getElementById(this.chart.Div_gen.id))
			this.config.offsetheight = document.getElementById(this.chart.Div_gen.id).offsetHeight;
		
	
	//设置数据,如果是单序列模型	
	if (seriesType == ChartConfig.SINGLE_SERIES) {
		this.ConverSingleSeriesModel(model,rows,groupColumn, seriesColumn,dataColumn);
	}
	else if (seriesType == ChartConfig.MULTI_SERIES ){
		this.ConverMutilSeriesModel(model,rows,groupColumn, seriesColumn,dataColumn);
	}
	else if(seriesType == ChartConfig.Dual_SERIES ){
		this.ConverMutilSeriesModel(model,rows,groupColumn, seriesColumn,dataColumn);
	}
	else if(seriesType == ChartConfig.Angular_SERIES){//仪表盘
		this.ConverAngularModel(model,rows,groupColumn, seriesColumn,dataColumn);
	}
	else if(seriesType == ChartConfig.Cylinder_SERIES){//水晶柱图
		this.ConverCylinderModel(model,rows,groupColumn, seriesColumn,dataColumn);
	}
	else if(seriesType == ChartConfig.Map_SERIES){//地图
		this.ConverMapModel(model,rows,groupColumn, seriesColumn,dataColumn);
	}
	
	
	return model;
};
/**
 * 转换单序列数据
 */
ChartModelConvertor.prototype.ConverSingleSeriesModel = function(model,rows,groupColumn, seriesColumn,dataColumn){
	var categoryIndex = this.dataset.nameToIndex(groupColumn);
	var dataindex = this.dataset.nameToIndex(dataColumn);
	var showvalueindex = this.dataset.nameToIndex(this.config.tooltextColumn);
	var displayvalueindex = this.dataset.nameToIndex(this.config.displayvalueColumn);
	
	//单序列模型直接读取数据到model ds
	var chartDS = new ChartDataset();
	chartDS.seriesName = "singleSeries";
	for(var i=0;i< rows.length;i++){
		var row = rows[i];
		var set = new ChartSet();
		if(this.config.showType == "Pyramid" )//金字塔图xml格式略有区别
			set.name = row.getCellValue(categoryIndex);
		else
			set.label = row.getCellValue(categoryIndex);
		set.value = row.getCellValue(dataindex);
		if(showvalueindex > -1){
			 var tooltext = row.getCellValue(showvalueindex);
			 if(tooltext)
			 	set.toolText = tooltext;
		}
		if(displayvalueindex > -1){
			var disv = row.getCellValue(displayvalueindex);
			 if(disv)
			 	set.displayvalue = disv;
		}
		if(this.config.isdrill){
		 		set.link = "JavaScript: isJavaScriptCall=true;ChartComp_DrillData('"
		 		+ this.chart.widget.id+"','"
		 		+ this.chart.id +"','"
		 		+ row.rowId+ "');";
			}
		chartDS.addSet(set);
	}
	this.ConvertStyle(model);
	model.addChartDataset(chartDS) ;	
};

/**
 * 转换多序列数据
 */
ChartModelConvertor.prototype.ConverMutilSeriesModel=function(model,rows,groupColumn, seriesColumn,dataColumn){
	//多序列数据
		/**
		 * 1.读取并生成分组
		 * 2.读取所有序列
		 * 3.初始化全部数据
		 * 4.读取全部行并填充
		 */
		 var categoryIndex = this.dataset.nameToIndex(groupColumn);
		 var dataindex = this.dataset.nameToIndex(dataColumn);
		 var categorys = new HashMap();		 
		 var series = new  HashMap();
		 var seriesIndex = this.dataset.nameToIndex(seriesColumn);
		 var showvalueindex = this.dataset.nameToIndex(this.config.tooltextColumn);
		 var displayvalueindex = this.dataset.nameToIndex(this.config.displayvalueColumn);
		 
		 //读取分组、序列
		 for(var i=0;i< rows.length;i++){
		 	var row = rows[i];
		 	var curcat = row.getCellValue(categoryIndex);
		 	var curseries = row.getCellValue(seriesIndex);
		 	if(curcat){
		 		if(!categorys.containsKey(curcat)){
		 			categorys.put(curcat,null);
		 			var cate = new Category();
					cate.label = curcat;
					model.addCategory(cate);
		 		}
		 	}
		 	if(curseries){
		 		if(!series.containsKey(curseries)){
		 			series.put(curseries,new ChartDataset(curseries));
		 		}		 		
		 	}
		 }
		 
		 //初始化数据，防止空数据占位
		 var sernames = series.keySet();
		 var categorys = categorys.keySet();
		 for(var i =0;i< sernames.length;i++){
		 	var chartds = series.get(sernames[i]);
		 	for(var j=0;j< categorys.length;j++){
		 		var set = new ChartSet();
		 		set.value = 0;
		 		chartds.addSet(set);
		 	}		 	
		 }
		 // 填写真实数据
		 for(var i=0;i< rows.length;i++){
		 	var row = rows[i];
		 	var curcat = row.getCellValue(categoryIndex);
		 	var curseries = row.getCellValue(seriesIndex);
		 	var curdata = row.getCellValue(dataindex);
		 	
		 	var chartds = series.get(curseries);//当前数据集
		 	//获取分组索引
		 	var curicatindex = 0;
		 	for(var j=0;j< categorys.length;j++){
		 		if(categorys[j] == curcat){
		 			curicatindex = j;
		 			break; 
		 		}
		 	}
		 	chartds.chartsets[curicatindex].value = curdata;
		 	
		 	if(showvalueindex > -1){
				 var tooltext = row.getCellValue(showvalueindex);
				 if(tooltext)
				 	chartds.chartsets[curicatindex].toolText = tooltext;
			}
			if(displayvalueindex > -1){
				var disv = row.getCellValue(displayvalueindex);
				 if(disv)
				 	chartds.chartsets[curicatindex].displayvalue = disv;
			}
		 	if(this.config.isdrill){
		 		chartds.chartsets[curicatindex].link = "JavaScript: isJavaScriptCall=true;ChartComp_DrillData('"
		 		+ this.chart.widget.id+"','"
		 		+ this.chart.id +"','"
		 		+ row.rowId+ "');";
			}
		 }
		 this.ConvertStyle(model);
		 
		 for(var i =0;i< sernames.length;i++){
		 	var chartds = series.get(sernames[i]);
		 	model.addChartDataset(chartds);
		 }
		 
};
/**
 * 转换仪表盘
 */
ChartModelConvertor.prototype.ConverAngularModel = function(model,rows,groupColumn, seriesColumn,dataColumn){	

	var srcwidth = 186;
	var srcheight = 189;
	
	var widthscale = this.config.offsetwidth/srcwidth ;
	var heightscale = this.config.offsetheight/srcheight ;
	this.config.sizescale = widthscale > heightscale?heightscale : widthscale;
	
	this.config.maxvalue = null;
	this.config.minvalue = null;	
	
	//填充数据
	var typeindex = this.dataset.nameToIndex(ChartConfig.Angular_Column_Type);
	var startindex = this.dataset.nameToIndex(ChartConfig.Angular_Column_Start);
	var endindex = this.dataset.nameToIndex(ChartConfig.Angular_Column_End);
	var colorindex = this.dataset.nameToIndex(ChartConfig.Angular_Column_Color);
	for(var i=0;i< rows.length;i++){
		var row = rows[i];
	 	var type = row.getCellValue(typeindex);
	 	if(type == ChartConfig.Angular_Type_Range){
	 		var range = new ColorRange();
	 		range.minvalue = Number(row.getCellValue(startindex));
			range.maxvalue = Number(row.getCellValue(endindex));
			if(this.config.maxvalue == null)
				this.config.maxvalue = range.maxvalue;
			else if(this.config.maxvalue < range.maxvalue)
				this.config.maxvalue = range.maxvalue;
			if(this.config.minvalue == null)
				this.config.minvalue = range.minvalue;
			if(this.config.minvalue > range.minvalue)
				this.config.minvalue = range.minvalue;			
			range.color = row.getCellValue(colorindex);
			model.addColorRange(range);
	 	}
	 	else if(type == ChartConfig.Angular_Type_Dial){
	 		var dial = new Dialset();
	 		dial.value = row.getCellValue(startindex);
	 		dial.color = row.getCellValue(colorindex);	 		
	 		if(this.config.isdrill){
		 		dial.link = "JavaScript: isJavaScriptCall=true;ChartComp_DrillData('"
		 		+ this.chart.widget.id+"','"
		 		+ this.chart.id +"','"
		 		+ row.rowId+ "');";
			}
			model.addDial(dial);
	 	}
	}
	if(this.config.maxvalue == null)
		this.config.maxvalue = 0;
	if(this.config.minvalue == null)
		this.config.minvalue = 0;
	if(model.dials){
		for(var i=0;i< model.dials.length ;i++){
			var dial = model.dials[i];
			if(!dial.color && model.colorrange && model.colorrange.length){
				for(var j=model.colorrange.length ; j>0;j-- ){
					if(dial.value >= model.colorrange[j-1].minvalue){
						dial.color =  model.colorrange[j-1].color;
						break;
					}
				}
			}
		}
		if(model.dials.length == 1){
			this.config.dialValue = model.dials[0].value; 
			this.config.dialColor = model.dials[0].color;
		}
	}
};
ChartModelConvertor.prototype.ConverCylinderModel = function(model,rows,groupColumn, seriesColumn,dataColumn){	
	var typeindex = this.dataset.nameToIndex(ChartConfig.Cylinder_Column_ValueTYPE);
	var valueindex = this.dataset.nameToIndex(ChartConfig.Cylinder_Column_VALUE);
	for(var i=0;i< rows.length;i++){
		var row = rows[i];
		var type = row.getCellValue(typeindex);
		var val = row.getCellValue(valueindex);
	 	if(type == ChartConfig.Cylinder_TYPE_UPPERLIMIT){
	 		this.config.cylinder_upperLimit =val;
	 	}
	 	else if(type == ChartConfig.Cylinder_TYPE_LOWERLIMIT){
	 		this.config.cylinder_lowerLimit=val;
	 	}
	 	else if(type == ChartConfig.Cylinder_TYPE_VALUE){
	 		this.config.cylinder_value =val;
	 	}
	 	else if(type == ChartConfig.Cylinder_TYPE_COLOR){
	 		this.config.cylinder_color= val;
	 	}
	}
	
}
ChartModelConvertor.prototype.ConvertStyle = function(model){
	//标题字体
	if(this.config.captionFont){
		this.AddStyleByfont("captionstyle","CAPTION",this.config.captionFont,model);
	}
	//X轴字体
	if(this.config.xAxisFont){
		this.AddStyleByfont("xaxisstyle","XAXISNAME",this.config.xAxisFont,model);
	}
	//X轴刻度
	if(this.config.xLabelFont){
		this.AddStyleByfont("xaxislabelstyle","DATALABELS",this.config.xLabelFont,model);
	}
	//y轴字体
	if(this.config.yAxisFont){
		this.AddStyleByfont("yaxisstyle","YAXISNAME",this.config.yAxisFont,model);
	}
	//y轴刻度
	if(this.config.yLabelFont){
		this.AddStyleByfont("yaxislabelstyle","VLINELABELS",this.config.yLabelFont,model);
	}
	//图例
	if(this.config.legendFont){
		this.AddStyleByfont("legendFontstyle","LEGEND",this.config.legendFont,model);
	}
};
ChartModelConvertor.prototype.AddStyleByfont = function(stylename,applyname,font,model){
	var style = new ChartStyle();
	style.setFont(stylename,font)
	model.addStyle(style);
	var apply = model.getStyleApply(applyname);
	if(null == apply) 
		apply = new ChartStyleApply();
	apply.toObject = applyname;
	apply.addStyles(stylename);
	model.addStyleApply(apply);
};
/**
* 将dataset中的数据封装为客户端的具体对象
* author:denghw,lisw
*/
ChartModelConvertor.prototype.ConverMapModel = function(model,rows,groupColumn, seriesColumn,dataColumn)
{
    var mapEntitySet = new MapEntitySet();
    var mapColorRangeSet = new MapColorRangeSet();
    var mapAttrString = '';
    
	var typeIndex = this.dataset.nameToIndex('type');
	
	for(var i=0; i < rows.length; i++)
	{
		var row = rows[i];
		var type = row.getCellValue(typeIndex);
		if(type == 'entity')
		{
			var entity = this.ConvertMapEntity(this.dataset,row);
			mapEntitySet.addEntity(entity);
		}
		else if(type == 'colorRange')
		{
			var colorRange = this.ConvertMapColorRange(this.dataset,row);
			mapColorRangeSet.addColorRange(colorRange);
		}
		else if(type == 'map')
		{
			for(var k=1; k <= 5 ; k++)
			{
				var key = row.getCellValue(this.dataset.nameToIndex('attr'+k));
				var value = row.getCellValue(this.dataset.nameToIndex('value'+k));
				if(key != '' && key != 'undefined')
				{
					mapAttrString += key;
					mapAttrString += '="'+value+'" ';
				}
			}
		}
	}
	
	model.setColorRangeSet(mapColorRangeSet);
	model.setEntitySet(mapEntitySet);
	model.setMapAttr(mapAttrString);
};
/**
 * 将dataset中的entity类型转换为客户端的MapEntity对象。
 * author:denghw
 */
ChartModelConvertor.prototype.ConvertMapEntity = function(dataset,row)
{
	var id = null;
	var value = null;
	var displayValue = null;
	var toolText = null;
	var color = null;
	var link = null;
	for(var i=1; i <=5; i++)
	{
		var attr = row.getCellValue(this.dataset.nameToIndex('attr'+i));
		if(attr == 'id')
		{
		    id = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'value')
		{
		    value = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'toolText')
		{
		    toolText = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'displayValue')
		{
		    displayValue = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'color')
		{
		    color = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		if(this.config.isdrill){
		 		link = "JavaScript: isJavaScriptCall=true;ChartComp_DrillData('"
		 		+ this.chart.widget.id+"','"
		 		+ this.chart.id +"','"
		 		+ row.rowId+ "');";
			}
	}
	var entity = new MapEntity(id,value,displayValue,toolText,color,link);
	
	return entity;
};
/**
 * 将dataset中的colorRange类型转换为客户端的MapColorRange对象。
 * author:denghw
 */
ChartModelConvertor.prototype.ConvertMapColorRange= function(dataset,row)
{
	var minValue = null;
	var maxValue = null;
	var colorString = null;
	var displayValue = null;
	var alpha = null;
		
	for(var i=1; i <=5; i++)
	{
		var attr = row.getCellValue(this.dataset.nameToIndex('attr'+i));
		if(attr == 'minValue')
		{
		    minValue = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'maxValue')
		{
		    maxValue = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'colorString')
		{
		    colorString = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'displayValue')
		{
		    displayValue = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
		else if(attr == 'alpha')
		{
		    alpha = row.getCellValue(this.dataset.nameToIndex('value'+i));
		}
	}
	var colorRange = new MapColorRange(minValue,maxValue,colorString,displayValue,alpha);
	
	return colorRange;
};
