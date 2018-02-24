package nc.scap.pub.reference.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.pub.reference.control.ChartRefGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ChartRefGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ChartRefGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_chart";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "code";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "name";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"code","name","type","pk_chart_ds","caption","groupname","seriesname","xaxisname","yaxisname","captionurl","captionfunction","numberprefix","numbersuffix","captionfont","xaxisfont","xlabelfont","yaxisfont","ylabelfont","legendfont","bgcolor","labeldisplay","showvalues","showlabels","showlegend","yaxismaxvalue","yaxisminvalue","isshowbg","isshowborder","majottmnumber","showpoint","rendertype","pyaxis","syaxis","rscale"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"编码","名称","类型","数据源","图表标题","分组列显示名称","统计列显示名称","横轴显示文字","纵轴显示文字","标题链接","标题函数","统计结果数字前缀","结果后缀","标题字体","横轴字体","横轴刻度字体","纵轴字体","纵轴刻度字体","图例字体","背景色","刻度显示方式","是否显示数值","是否显示刻度","是否显示图例","纵轴最大值","纵轴最小值","是否显示背景","是否显示边框","标记数字显示个数","是否显示关键点数字","图形类型","主Y轴统计列","次Y轴统计列","环比例"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_chart"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}