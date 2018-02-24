package nc.scap.pub.selfquery.reference.model;

import nc.scap.pub.selfquery.reference.control.JSReportOrgTreeRefController;
import uap.lfw.ref.model.LfwTreeRefModel;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;
import uap.web.bd.org.reference.CpOrgTreeRefGroupVO;

public class JSReportOrgTreeRefModel extends LfwTreeRefModel{

	@Override
	public LfwTreeRefGroupVO getGroupVO() {

		return new  CpOrgTreeRefGroupVO();
	}

	@Override
	public String getControllerClazzName() {
		
		return JSReportOrgTreeRefController.class.getName();
	}

}

