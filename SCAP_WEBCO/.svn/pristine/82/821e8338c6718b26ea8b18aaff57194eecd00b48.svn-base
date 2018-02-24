package nc.scap.pub.multiCompSelect;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.KeyEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.SuperVO;
import uap.web.bd.pub.AppUtil;
public class MultiCompSelectViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  private static String PLUGOUT_ID="afterOkplugout";
  public void onCancel(  MouseEvent mouseEvent){
    closeView();
  }
  public void closeView(){
		AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().getWindow().setHasChanged(false);
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
  }
  public void onOk(  MouseEvent mouseEvent){
	  closeView();
		CmdInvoker.invoke(new UifPlugoutCmd(AppUtil.getCntView().getId(),
				PLUGOUT_ID));

  }
  public void onQueryEnter(  KeyEvent keyEvent){
    
  }
  public void onDataLoad_multiorgs(  DataLoadEvent dataLoadEvent){

	    Dataset ds = dataLoadEvent.getSource();
			ds.setOrderByPart(" order by code asc");
			CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {

				protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
						String wherePart, String orderPart)
						throws LfwBusinessException {
					CpOrgVO[] vos = null;
					String sql="";
					boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
					if(isFilterOrg)
						sql = "SELECT * FROM CP_ORGS where "+CpOrgUtil.getFilterOrgWhereSql();
					else
						sql = "SELECT * FROM CP_ORGS where dr=0  and orglevel ='2' START WITH  PK_ORG = '"
								+ CntUtil.getCntOrgPk()
								+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";
					if(orderPart!=null){
						sql += orderPart;
					}
					try {
						vos = CRUDHelper.getCRUDService().queryVOs(sql,CpOrgVO.class, null, null);
						pinfo.setPageSize(vos.length);
						if(ScapSysinitUtil.isShowShortName()){
							for (CpOrgVO cpOrgVO : vos) {
								cpOrgVO.setName(cpOrgVO.getDef11());
							  }
						}
					} catch (LfwBusinessException e1) {
						e1.printStackTrace();
					}
					return vos;
				}
			});
//		Dataset ds = dataLoadEvent.getSource();
//		
//		
//		Dataset orgds = AppUtil.getCntView().getViewModels()
//				.getDataset("multiCompSelect");
//		orgds.getLastCondition();
//		orgds.setLastCondition(" orglevel = '2' and enablestate=2 and dr=0 ");
//		
//		//移除参数OPEN_BILL_ID
//		String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
//		LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().remove(AppConsts.OPEN_BILL_ID);
//		
//		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
//		//还原参数 OPEN_BILL_ID
//		LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);

  }
}
