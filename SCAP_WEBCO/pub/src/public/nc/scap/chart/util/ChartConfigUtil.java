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



//�޷�ƥ�䵽��ͼ�����У�
//�����б�ͼ2D   ShowType_Doughnut_2D    
//�����б�ͼ3D   ShowType_Doughnut_3D
//��ά˫Y���ߡ��С���� ����ͼ  ShowType_MSCombiDY_3D
// ˫Y��ά���߸���ͼ   ShowType_MSColumnLineDY_3D
//˫Y��ά�ѻ�ͼ����ͼ  ShowType_MSStackedColumnLineDY_2D
//����������    Series_Single_Type 
//����������    Series_Mutil_Type
//�Ǳ�������    Series_Angular_Type 
//˫Y������     Series_DualY_Type
//ˮ����ͼ      Series_Cylinder_Type
//��ͼ����      Series_MaP_Type


/**
 * ����ָ��DS�������Դ������Ϣ,����ȡ�����򷵻�null
 * @author Ҧ��
 * @create at 2013-8-28 ����4:43:11    
 * @param ds
 * @return
 */
//public static ScapChartDsVO getDsInfo( Dataset ds ) {
//	//����id��ȡ���ݼ�����
//	String dsid = ds.getId();
//	ScapChartDsVO[] dsvo = (ScapChartDsVO[]) ScapDAO.retrieveByCondition( ScapChartDsVO.class, "code = '"+dsid+"'");
//	if (dsvo != null) {
//		return dsvo[0];
//	}	else {
//		ScapLogger.error("=======================û���ҵ�codeΪ"+dsid+"������Դ����");
//		return null;
//	}
//	
//}
	
	
}

