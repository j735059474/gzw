package uap.web.bd.org.reference;

import nc.scap.sysinit.util.ScapSysinitUtil;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

/**
 * 协同集团树形参照参数VO
 * 
 * @author maokun
 * 
 */
public class CpOrgTreeRefGroupVO extends LfwTreeRefGroupVO {
	private static final long serialVersionUID = -1600057832076367514L;
	@Override
	public String[] getReplaceHiddenFieldCodes() {
		// TODO 自动生成的方法存根
		return super.getReplaceHiddenFieldCodes();
	}

	@Override
	public String getPkField() {
		return "pk_org";
	}

	@Override
	public String getNameField() {
		return "name";
	}

	@Override
	public String[] getFieldCodes() {
		/**
		 * 判断是显示全名还是显示简称
		 */
		if (ScapSysinitUtil.isShowShortName()) {
			return new String[] { "pk_org", "pk_fatherorg", "code", "def11","name" };
		}
		return new String[] { "pk_org", "pk_fatherorg", "code", "name"};
	}

	@Override
	public String getCodeField() {
		return "code";
	}

	@Override
	public String getFatherField() {
		return "pk_fatherorg";
	}

	@Override
	public String getChildField() {
		return "pk_org";
	}
	/**
	 * 数据集隐藏字段集合   默认为pk字段
	 * @return
	 */
	@Override
	public String[] getHiddenFieldCodes(){
		return new String[]{getPkField()};
	}

}
