package nc.scap.chart.util;

import nc.uap.lfw.core.comp.*;
public class ChartConfigUtil {

	public static ChartConfig getChartConfig(String key){
		ChartConfig config = null;
		if(key.equals(ChartConfig.ShowType_Column_2D)){
			config = new ChartConfigColumn2D();
		}else if(key.equals(ChartConfig.ShowType_Bar_2D)){
			config = new ChartConfigBar2D();
		}else if(key.equals(ChartConfig.ShowType_MSBar_2D)){
			config = new ChartConfigMSBar2D();
		}else if(key.equals(ChartConfig.ShowType_MSBar_3D)){
			config = new ChartConfigMSBar3D();
		}else if(key.equals(ChartConfig.ShowType_StackedBar_2D)){
			config = new ChartConfigStackedBar2D();
		}else if(key.equals(ChartConfig.ShowType_StackedBar_3D)){
			config = new ChartConfigStackedBar3D();
		}else if(key.equals(ChartConfig.ShowType_MSColumn_2D)){
			config = new ChartConfigMSColumn2D();
		}else if(key.equals(ChartConfig.ShowType_MSColumn_3D)){
			config = new ChartConfigMSColumn3D();
		}else if(key.equals(ChartConfig.ShowType_Pie_2D)){
			config = new ChartConfigPie2D();
		}else if(key.equals(ChartConfig.ShowType_Pie_3D)){
			config = new ChartConfigPie3D();
		}else if(key.equals(ChartConfig.ShowType_Area_2D)){
			config = new ChartConfigArea2D();
		}else if(key.equals(ChartConfig.ShowType_MSArea_2D )){
			config = new ChartConfigMSArea2D();
		}else if(key.equals(ChartConfig.ShowType_Radar)){
			config = new ChartConfigRadar();
		}else if(key.equals(ChartConfig.ShowType_Stacked_2D)){
			config = new ChartConfigStack2D();
		}else if(key.equals(ChartConfig.ShowType_Stacked_3D)){
			config = new ChartConfigStack3D();
		}else if(key.equals(ChartConfig.ShowType_StackedArea_2D)){
			config = new ChartConfigStackedArea2D();
		}else if(key.equals(ChartConfig.ShowType_MSCombi_3D)){
			config = new ChartConfigMSCombi3D();
		}else if(key.equals(ChartConfig.ShowType_MSCombi_2D)){
			config = new ChartConfigMSCombi2D();
		} else if(key.equals(ChartConfig.ShowType_Pyramid )){
			config = new ChartConfigPyramid();
		}else if(key.equals(ChartConfig.ShowType_Line_2D)){
			config = new ChartConfigLine2D();
		}else if(key.equals(ChartConfig.ShowType_MSLine)){
			config = new ChartConfigMSLine();
		}else if(key.equals(ChartConfig.ShowType_AngularGauge )){
			config = new ChartConfigAngularGauge();
		}else if(key.equals(ChartConfig.ShowType_MSSpline)){
			config = new ChartConfigMSSpline();
		}else if(key.equals(ChartConfig.ShowType_Cylinder)){
			config = new ChartConfigCylinder();
		}else if(key.equals(ChartConfig.ShowType_Map_China2)){
			config = new ChartConfigMapChina();
		}
		return config;
	}



//无法匹配到的图类型有：
//单序列饼图2D   ShowType_Doughnut_2D    
//单序列饼图3D   ShowType_Doughnut_3D
//二维双Y轴线、列、面积 复合图  ShowType_MSCombiDY_3D
// 双Y三维列线复合图   ShowType_MSColumnLineDY_3D
//双Y二维堆积图复合图  ShowType_MSStackedColumnLineDY_2D
//单序列类型    Series_Single_Type 
//多序列类型    Series_Mutil_Type
//仪表盘类型    Series_Angular_Type 
//双Y轴类型     Series_DualY_Type
//水晶柱图      Series_Cylinder_Type
//地图类型      Series_MaP_Type


/**
 * 根据指定DS获得数据源定义信息,若获取错误则返回null
 * @author 姚广
 * @create at 2013-8-28 下午4:43:11    
 * @param ds
 * @return
 */
//public static ScapChartDsVO getDsInfo( Dataset ds ) {
//	//根据id获取数据集参数
//	String dsid = ds.getId();
//	ScapChartDsVO[] dsvo = (ScapChartDsVO[]) ScapDAO.retrieveByCondition( ScapChartDsVO.class, "code = '"+dsid+"'");
//	if (dsvo != null) {
//		return dsvo[0];
//	}	else {
//		ScapLogger.error("=======================没有找到code为"+dsid+"的数据源定义");
//		return null;
//	}
//	
//}
	
	
}

