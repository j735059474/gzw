package nc.scap.jjpub.pubview;

import nc.scap.jjpub.util.JjUtil;
import nc.scap.jjpub.util.SqlUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.KeyEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.vo.pub.SuperVO;
import uap.web.bd.pub.AppUtil;

public class SelectUserbyOrgViewController {
	private static final long serialVersionUID = 1L;
	private static final long ID = 5L;
	private static String PLUGOUT_ID = "afterOkplugout";

	/**
	 * ��ȡ��ǰWindowContext
	 * 
	 * @return
	 */
	private WindowContext getCurrentWinCtx() {
		return AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext();
	}

	public void onCancel(MouseEvent mouseEvent) {
		closeView();
	}

	public void closeView() {
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}

	public void onOk(MouseEvent mouseEvent) {
		closeView();
		CmdInvoker.invoke(new UifPlugoutCmd(AppUtil.getCntView().getId(),
				PLUGOUT_ID));
	}

	public void onDataLoad_orgs(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		
		
		Dataset orgds = AppUtil.getCntView().getViewModels()
				.getDataset("cp_orgs_1");
		ds.setOrderByPart(" order by code asc");
		orgds.getLastCondition();
		orgds.setLastCondition(" orglevel = '2' and enablestate=2 and dr=0 ");
		
		//�Ƴ�����OPEN_BILL_ID
		String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
		LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().remove(AppConsts.OPEN_BILL_ID);
		
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
		//��ԭ���� OPEN_BILL_ID
		LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
	}

	public void onAfterRowSelect_orgs(DatasetEvent datasetEvent) {
		
		Dataset orgds = AppUtil.getCntView().getViewModels()
				.getDataset("cp_orgs_1");
		String pk_org = orgds.getSelectedRow().getString(
				orgds.nameToIndex("pk_org"));

		Dataset ds_user = AppLifeCycleContext.current().getViewContext()
				.getView().getViewModels().getDataset("cp_userpsn");
		// ����������
		ds_user.setExtendAttribute("ORDER_PART", "user_code");
		String condition = " user_type != 0 and user_type != 2 and enablestate = 2 and dr=0 and pk_org='"
				+ pk_org + "'";
		ds_user.getLastCondition();
		ds_user.setLastCondition(condition);
		Dataset ds = datasetEvent.getSource();

		String oldKey = (String) AppLifeCycleContext.current()
				.getApplicationContext()
				.getAppAttribute("treeeindex:" + pk_org);
		RowSet rowSet = ds_user.getRowSet(oldKey);
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		// ���δ���ع���ҳ����
		if (rowSet == null) {
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
				protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
					return ds.getLastCondition();
				}

				// ��д�˷���������ѡ�е�һ��
				protected void postProcessChildRowSelect(Dataset ds) {
					return;
				}
			});
			Dataset userDs = AppLifeCycleContext.current().getViewContext()
					.getView().getViewModels().getDataset("cp_userpsn");
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.addAppAttribute("treeeindex:" + pk_org,
							userDs.getCurrentKey());
		} else {
			// �Ѿ����ع����߻���
			ds_user.setCurrentKey(oldKey);
		}
		
		AppUtil.addAppAttr(ISQUERY, "N");
	}
    final static String ISQUERY="isquery"; 
	public void onEnterQuery(KeyEvent keyEvent) {
		TextComp text = (TextComp) AppLifeCycleContext.current()
				.getViewContext().getView().getViewComponents()
				.getComponent("queryInput");
		String str = text.getValue().trim();
		// where���� enablestate =2 ��ʾ���� user_type =0��2��ʾ���Ź���Ա�͹�˾����Ա
		String where = " user_type != 0 and user_type != 2 and enablestate = 2 and dr=0 and";
		Dataset userDs = AppLifeCycleContext.current().getViewContext()
				.getView().getViewModels().getDataset("cp_userpsn");
		userDs.setLastCondition(where
				+ SqlUtil.sqlLike(new String[] { "user_code", "user_name",
						"szdw", "deptname" }, str, "or"));
		//�Ƴ�����OPEN_BILL_ID
		String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
		LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(userDs.getId()));
		//��ԭ���� OPEN_BILL_ID
		LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
		AppUtil.addAppAttr(ISQUERY, "Y");
	}

	public void onDataLoad_userpsn(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
//		String flag=JjUtil.getStr(AppUtil.getAppAttr(ISQUERY));
		String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
//		if("Y".equals(flag)){
		//�Ƴ�����OPEN_BILL_ID
			LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
//		}
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
//		if("Y".equals(flag)){
		//��ԭ���� OPEN_BILL_ID
			LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
//		}
	}

	public void beforeShow(DialogEvent dialogEvent) {
		
		
	}
}
