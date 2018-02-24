package nc.scap.pub.util;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmProinsUtil;
import nc.uap.wfm.utils.WfmUtilFacade;
public class CpWfmUtilFacade extends WfmUtilFacade {
     /**
      * �����Ƿ��ύ������  trueΪ���ݽ�����������false��δ�ύ������
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
	 *����//����״̬�����̬������̬������̬��ֻ��
	 * @param pk_formIns
	 * @return
	 */
	public static void setWfmBillState(String pk_formIns,String taskPk ){
		 AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		if(isExistStartByPkFormIns(pk_formIns)){
			// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
			if (taskPk == null || taskPk.equals("")) {
			AppUtil.addAppAttr(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
			}
		}
	}
}
