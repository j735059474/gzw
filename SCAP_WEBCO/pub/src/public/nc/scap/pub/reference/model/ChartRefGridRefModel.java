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
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_chart";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "code";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "name";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"code","name","type","pk_chart_ds","caption","groupname","seriesname","xaxisname","yaxisname","captionurl","captionfunction","numberprefix","numbersuffix","captionfont","xaxisfont","xlabelfont","yaxisfont","ylabelfont","legendfont","bgcolor","labeldisplay","showvalues","showlabels","showlegend","yaxismaxvalue","yaxisminvalue","isshowbg","isshowborder","majottmnumber","showpoint","rendertype","pyaxis","syaxis","rscale"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"����","����","����","����Դ","ͼ�����","��������ʾ����","ͳ������ʾ����","������ʾ����","������ʾ����","��������","���⺯��","ͳ�ƽ������ǰ׺","�����׺","��������","��������","����̶�����","��������","����̶�����","ͼ������","����ɫ","�̶���ʾ��ʽ","�Ƿ���ʾ��ֵ","�Ƿ���ʾ�̶�","�Ƿ���ʾͼ��","�������ֵ","������Сֵ","�Ƿ���ʾ����","�Ƿ���ʾ�߿�","���������ʾ����","�Ƿ���ʾ�ؼ�������","ͼ������","��Y��ͳ����","��Y��ͳ����","������"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_chart"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}