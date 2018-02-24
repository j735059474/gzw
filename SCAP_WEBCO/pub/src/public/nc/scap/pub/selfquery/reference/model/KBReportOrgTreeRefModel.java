package nc.scap.pub.selfquery.reference.model;

import nc.scap.pub.selfquery.reference.control.KBReportOrgTreeRefController;
import uap.lfw.ref.model.LfwTreeRefModel;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;
import uap.web.bd.org.reference.CpOrgTreeRefGroupVO;
/**
 * �챨Эͬ��ǰ����+ҵ��Ԫ  ����model
 * @author houlg
 *
 */
public class KBReportOrgTreeRefModel extends LfwTreeRefModel{

	@Override
	public LfwTreeRefGroupVO getGroupVO() {

		return new  CpOrgTreeRefGroupVO();
	}

	@Override
	public String getControllerClazzName() {
		
		return KBReportOrgTreeRefController.class.getName();
	}

}
