package nc.scap.transfer.trs.wfm;

import nc.scap.def.util.ScapDefUtil;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.wfm.context.PwfmContext;
import nc.uap.wfm.engine.IFrmNumBillGen;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.lang.UFDate;
import uap.web.bd.pub.AppUtil;

public class TrsBillNumGen implements IFrmNumBillGen {

	@Override
	public String getValue(String flowTypePk, String taskPk) {
		ScapptApplyHVO mainvo = (ScapptApplyHVO) PwfmContext
				.getCurrentBpmnSession().getFormVo();
		StringBuffer sb = new StringBuffer("[����] ");
		// pos 1001ZG10000000009Y22 ��׼,1001ZG10000000009Y23
		// ���ȱ���,1001ZG10000000009Y24 �º󱸰�
		sb.append(ScapPtMethod.getBillcnt(mainvo));
		String str = "";
		String ctype = mainvo.getCprocesstype(); // ����������
		if (ctype != null && !"".equals(ctype)) {
			DefdocVO defvo = ScapDefUtil.getDefDocVo(ctype);
			if (defvo.getCode().equals(PtConstants.SP_SHBA)
					|| defvo.getCode().equals(PtConstants.SP_SQBA)) {
				str = " �� ";
			} else {
				str = " �� ";
			}
		}
		CpOrgVO orgvo = CpOrgUtil.getOrgVoByPk(CntUtil.getCntGroupPk());
		sb.append(str + " ")
				.append(orgvo.getDef11()==null?orgvo.getName():orgvo.getDef11()+ " ")
				.append(new UFDate().getYear() + " ")
				.append(ScapPtMethod.getWFMBillNum(CpPubMethod
						.getFlwTypePk(ScapPtMethod.getNodeCode(mainvo))));
		return sb.toString();
	}

	@Override
	public boolean isBefore(String flowTypePk, String taskPk) {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public String genFrmNumBillByFrmDefPk_RequiresNew(String flowTypePk) {
		// TODO �Զ����ɵķ������
		return null;
	}

}
