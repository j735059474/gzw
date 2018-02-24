package nc.scap.pub.util;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmProinsUtil;
import nc.uap.wfm.utils.WfmUtilFacade;
public class CpWfmUtilFacade extends WfmUtilFacade {
     /**
      * 单据是否提交审批流  true为单据进入审批流，false则未提交过流程
      * @param pk_formIns
      * @return
      */
	public static boolean isExistStartByPkFormIns(String pk_formIns){
		boolean flag=false;
		Object proins=WfmProinsUtil.getProinsByPkFormIns(pk_formIns);
		if(proins!=null){
			flag= true;
		} 
		return flag;
	}
	/**
	 *设置//单据状态：浏览态、审批态、传阅态、只读
	 * @param pk_formIns
	 * @return
	 */
	public static void setWfmBillState(String pk_formIns,String taskPk ){
		 AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		if(isExistStartByPkFormIns(pk_formIns)){
			// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
			if (taskPk == null || taskPk.equals("")) {
			AppUtil.addAppAttr(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
			}
		}
	}
}
