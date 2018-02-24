/**
 *仪表盘 预置样式
 */
ChartModel.prototype.toRadarXml = function(bgcolor) {
	if(bgcolor == "radar_variance_analysis") return this.toVarianceAnalysisXml();
	else return this.toCountryComparisonXml();
}

ChartModel.prototype.toVarianceAnalysisXml = function() {	
	var conf = this.config;
	var str = '';
	var absWidth = conf.offsetwidth / 2;
	var absHeight = conf.offsetheight / 2 + conf.offsetheight / 20; 
	var dataString =
		'<chart caption="Variance Analysis" bgColor="FFFFFF" radarFillColor="FFFFFF" plotFillAlpha="5" \n\
			plotBorderThickness="2" anchorAlpha="100" numberPrefix="$" numDivLines="2" legendPosition="RIGHT"> \n\
				<categories font="Arial" fontSize="11"> \n\
				<category label="Jan"/> \n\
				<category label="Feb"/> \n\
				<category label="Mar"/> \n\
				<category label="Apr"/> \n\
				<category label="May"/> \n\
				<category label="Jun"/> \n\
				<category label="Jul"/> \n\
				<category label="Aug"/> \n\
				<category label="Sep"/> \n\
				<category label="Oct"/> \n\
				<category label="Nov"/> \n\
				<category label="Dec"/> \n\
				</categories> \n\
				<dataset seriesname="Products" color="CD6AC0" anchorSides="6" anchorRadius="2" anchorBorderColor="CD6AC0" anchorBgAlpha="0"> \n\
				<set value="1127654"/> \n\
				<set value="1226234"/> \n\
				<set value="1299456"/> \n\
				<set value="1311565"/> \n\
				<set value="1324454"/> \n\
				<set value="1357654"/> \n\
				<set value="1296234"/> \n\
				<set value="1359456"/> \n\
				<set value="1391565"/> \n\
				<set value="1414454"/> \n\
				<set value="1671565"/> \n\
				<set value="1134454"/> \n\
				</dataset> \n\
				<dataset seriesname="Services" color="0099FF" anchorSides="10" anchorBorderColor="0099FF" anchorBgAlpha="0" anchorRadius="2"> \n\
				<set value="534241"/> \n\
				<set value="556728"/> \n\
				<set value="575619"/> \n\
				<set value="676713"/> \n\
				<set value="665520"/> \n\
				<set value="634241"/> \n\
				<set value="656728"/> \n\
				<set value="675619"/> \n\
				<set value="776713"/> \n\
				<set value="865520"/> \n\
				<set value="976713"/> \n\
				<set value="665520"/> \n\
				</dataset> \n\
				<styles> \n\
				<definition> \n\
				<style name="myCaptionFont" type="font" font="Arial" size="14" color="666666" bold="1" underline="1"/> \n\
				</definition> \n\
				<application> \n\
				<apply toObject="Caption" styles="myCaptionFont"/> \n\
				</application> \n\
				</styles> \n\
		</chart>';
		
	return dataString;
};

