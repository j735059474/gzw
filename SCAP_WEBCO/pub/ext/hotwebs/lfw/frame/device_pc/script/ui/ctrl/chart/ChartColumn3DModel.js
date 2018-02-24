/**
 *仪表盘 预置样式
 */
ChartModel.prototype.toColumn3DXml = function(bgcolor) {
	if(bgcolor == "column3d_country_comparison") return this.toCountryComparisonXml();
	else return this.toCountryComparisonXml();
}

ChartModel.prototype.toCountryComparisonXml = function() {	
	var conf = this.config;
	var str = '';
	var absWidth = conf.offsetwidth / 2;
	var absHeight = conf.offsetheight / 2 + conf.offsetheight / 20; 
	var dataString =
		'<chart palette="2" caption="Country Comparison" showLabels="1" showvalues="0" decimals="0" numberPrefix="$" \n\
			clustered="0" exeTime="1.5" showPlotBorder="0" zGapPlot="30" zDepth="90" divLineEffect="emboss" startAngX="10" \n\
			endAngX="18" startAngY="-10" endAngY="-40"> \n\
				<categories> \n\
					<category label="Austria" /> \n\
					<category label="Brazil" /> \n\
					<category label="France" /> \n\
					<category label="Germany" /> \n\
					<category label="USA" /> \n\
				</categories> \n\
				<dataset seriesName="1998" color="8BBA00" showValues="0"> \n\
					<set value="45000.65" /> \n\
					<set value="44835.76" /> \n\
					<set value="18722.18" /> \n\
					<set value="77557.31" /> \n\
					<set value="92633.68" /> \n\
				</dataset> \n\
				<dataset seriesName="1997" color="F6BD0F" showValues="0"> \n\
					<set value="57401.85" /> \n\
					<set value="41941.19" /> \n\
					<set value="45263.37" /> \n\
					<set value="117320.16" /> \n\
					<set value="114845.27" /> \n\
				</dataset> \n\
				<dataset seriesName="1996" color="AFD8F8" showValues="0"> \n\
					<set value="25601.34" /> \n\
					<set value="20148.82" /> \n\
					<set value="17372.76" /> \n\
					<set value="35407.15" /> \n\
					<set value="38105.68" /> \n\
				</dataset> \n\
				<styles> \n\
					<definition> \n\
						<style name="captionFont" type="font" size="15" /> \n\
					</definition> \n\
					<application> \n\
						<apply toObject="caption" styles="captionfont" /> \n\
					</application> \n\
				</styles> \n\
		</chart>';
		
	return dataString;
};

