package nc.scap.pub.selfquery.reference.control;

import nc.scap.pub.util.CpFdUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;

import org.apache.commons.lang.StringUtils;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class FXGKReportOrgTreeRefController extends LfwSqlTreeReferenceController {

	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
		String tbSql = "";
		String year_mon[] = CpFdUtil.getQryYearMonth("FX");
	
		tbSql=CpFdUtil.getReportSql("∑Áœ’", year_mon[0], year_mon[1]);
		sqlvo.setTableName("("+tbSql+") cp_orgs");
		sqlvo.setRealTableNames("cp_orgs");
		sqlvo.setOrderByPart("code");
		return sqlvo;

	}

}
