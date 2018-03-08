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
		StringBuffer sb = new StringBuffer("[交易] ");
		// pos 1001ZG10000000009Y22 核准,1001ZG10000000009Y23
		// 事先备案,1001ZG10000000009Y24 事后备案
		sb.append(ScapPtMethod.getBillcnt(mainvo));
		String str = "";
		String ctype = mainvo.getCprocesstype(); // 审批流类型
		if (ctype != null && !"".equals(ctype)) {
			DefdocVO defvo = ScapDefUtil.getDefDocVo(ctype);
			if (defvo.getCode().equals(PtConstants.SP_SHBA)
					|| defvo.getCode().equals(PtConstants.SP_SQBA)) {
				str = " 备 ";
			} else {
				str = " 核 ";
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
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public String genFrmNumBillByFrmDefPk_RequiresNew(String flowTypePk) {
		// TODO 自动生成的方法存根
		return null;
	}

}
