/**
 * 仪表盘颜色区间
 */
function ColorRange(){
	this.minvalue = null;
	this.maxvalue = null;
	this.color = null;
};
ColorRange.prototype.toXml = function(){
	var str = '<color ';
	if(this.minvalue)
		str += ' minvalue = "' + this.minvalue + '"';
	else
		str += ' minvalue = "0"';
	if(this.maxvalue)
		str += ' maxvalue = "' + this.maxvalue + '"';
	else
		str += ' minvalue = "100"';
	if(this.color)
		str += ' code = "' + this.color + '"';
	str += '/>\n';
	return str;
};

/**
 * 指针数据
 */
function Dialset(){
	this.value = null;
	this.color = null;	
	this.link = null;
};
Dialset.prototype.toXml = function(radius){
	var str = '<dial ';
	if(this.value)
		str += ' value="' + this.value + '" ';
	str += ' radius = "'+radius+'" ';		
	//if(this.color)
		//str += ' bgColor = "000000,' + this.color + ',000000" ';
		str += ' bgColor = "454444,292828,454444" ';
	if(this.link )
		str += ' link = "' + this.link + '" ';
	str += ' borderAlpha="100" baseWidth="10" topWidth="1"  '
	str += ' /> \n'; 
	return str;
};

/**
 * 拼装颜色区间语句和指针语句
 */
ChartModel.prototype.toColorRangeXml = function () {
	str = '<colorRange>';
	for (var i = 0; i < this.colorrange.length; i ++) {				
				str += this.colorrange[i].toXml();
	}
	str+='</colorRange>';
	return str;
}
ChartModel.prototype.toDialsetXml = function() {	
	str = '';
	if(this.dials){
			str += " <dials> \n"
			for (var i = 0; i < this.dials.length; i ++) {
				str += this.dials[i].toXml(this.config.offsetwidth / 3);
			}
			str += " </dials>\n";
	}
	return str;
}
/**
 * 调用后台钻取方法
 */
function onDrill(dataset_id){
	var proxy = new ServerProxy(null, null, false);
			proxy.addParam('clc','com.scap.pub.basechart.impl.ChartController');
			proxy.addParam('m_n', 'onDrill');
			proxy.addParam('datasetid', dataset_id);
			showDefaultLoadingBar();
			proxy.execute();
	}
