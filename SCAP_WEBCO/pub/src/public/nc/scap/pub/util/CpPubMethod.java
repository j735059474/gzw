package nc.scap.pub.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.vos.QryOrgsVO;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ViewRule;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.serializer.impl.Datasets2RichVOSerializer;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.uif.delegator.IDataValidator;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UIView;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.facility.WfmServiceFacility;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.lfw.ref.util.LfwReferenceUtil;
import uap.web.bd.pub.AppUtil;

/***
 * ���������÷�������
 * 
 * @author wupengl
 * 
 */
public class CpPubMethod {

	/***
	 * 
	 * ���ô���url����
	 * 
	 * @param para
	 * @return
	 */
	public static String getWebParter(String para) {
		return LfwRuntimeEnvironment.getWebContext().getOriginalParameter(para);
	}

	/***
	 * APPӦ�ü�����
	 * 
	 * @param para
	 * @return
	 */
	public static String getAppParter(String para) {
		return LfwRuntimeEnvironment.getWebContext().getAppSession()
				.getOriginalParameter(para);
	}

	/***
	 * �����������
	 * 
	 * @param uimeta
	 */
	public static void hidnWfmPanle(UIMeta uimeta) {
		UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
		UISplitter split = (UISplitter) UIElementFinder.findElementById(uimeta,
				"mainWinSpliter");
		uimeta.removeElement(split);
		uimeta.setElement(mainView);
	}

	/***
	 * ���ؼ����������ư�ť
	 * 
	 * @param pageMeta
	 * @param str
	 */
	public static void hidnWfmBtn(LfwWindow pageMeta, String[] str) {
		LfwView pubview_simpleexetask = pageMeta
				.getView("pubview_simpleexetask");
		CpUIctrl.hideMenuBarReal(pubview_simpleexetask, "simpleExeMenubar", str);
	}

	/**
	 * ������̱���
	 */
	public static void resetWfmParameter() {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TaskPk, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FolwTypePk, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProdefPk, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProInsPk, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ScratchPad, null);
		AppUtil.addAppAttr(WfmConstants.BILLSTATE, null);
		AppUtil.addAppAttr(WfmConstants.AttachFileList_Temp_Billitem, null);
		AppUtil.addAppAttr(WfmConstants.RETURN_PK_TASK, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_BillID, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, null);
	}

	/**
	 * �������������Ϣ
	 * 
	 * @param props
	 * @param paramMap
	 * @param pkValue
	 */
	public static void setWfmInfo(Map<String, String> paramMap, String pkValue,
			String nodecode, String wfmoper) {
		resetWfmParameter();
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FolwTypePk,
				getFlwTypePk(nodecode));
		try {
			WfmProdefVO[] proDef = WfmServiceFacility.getProDefQry()
					.getProDefByByWhere(
							"flwtype='" + getFlwTypePk(nodecode)
									+ "' and isnotstartup='Y'");
			if (proDef != null && proDef.length > 0) {
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProdefPk,
						proDef[0].getPk_prodef());
			}
		} catch (WfmServiceException e) {
			LfwLogger.error("��ȡ���̶������");
		}
		// ���ݱ�pk�����������pk
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		try {
			WfmTaskVO task = getLasterTaskByBillIdAndUser(pkValue, pk_user,
					wfmoper);
			if (task != null) {
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TaskPk,
						task.getPk_task());
				paramMap.put(WfmConstants.WfmUrlConst_TaskPk, task.getPk_task());

			}
		} catch (WfmServiceException e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

	/**
	 * ���ݽڵ�����ȡ��Ӧ���̶���(ʹ��ȱ��:������Բ�ͬ�������õ����̻�ȡ���Ե����̶���)
	 * 
	 * @param node
	 * @return
	 */
	public static String getFlwTypePk(String node) {
		// ͨ�����ܽڵ����ֵ��ȡ�������Ͷ���vo
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select a.* from wfm_flwtype a, cp_appsnode b where b.id='"
				+ node
				// + "' and b.pk_appsnode=a.pageid and a.pk_org='"
				// + getPk_group() + "'";
				+ "' and b.pk_appsnode=a.pageid";
		try {
			List<WfmFlwTypeVO> list = (List<WfmFlwTypeVO>) dao.executeQuery(
					sql, new BeanListProcessor(WfmFlwTypeVO.class));
			if (list == null || list.size() == 0) {
				return null;
			}
			WfmFlwTypeVO wfmVo = list.get(0);
			// ��ȡ��������pk
			String flwType = wfmVo.getPk_flwtype();
			return flwType;
		} catch (DAOException e) {
			throw new LfwRuntimeException("��������û���ҵ�");
		}
	}

	/**
	 * ���ݲ�������,��ȡ�û�����
	 * 
	 * @param billId
	 * @param pk_user
	 * @param operation
	 * @return
	 * @throws WfmServiceException
	 */
	public static WfmTaskVO getLasterTaskByBillIdAndUser(String billId,
			String pk_user, String operation) throws WfmServiceException {
		if (billId == null || billId.length() == 0) {
			return null;
		}
		String sql = "";
		if (operation != null && !"wf".equals(operation)) {
			sql = "SELECT * FROM wfm_task WHERE pk_frmins = ? and pk_owner = ? and state='State_Run' order by ts desc";
		} else {
			sql = "SELECT * FROM wfm_task WHERE pk_frmins = ? and pk_owner = ?  order by ts desc";
		}
		SQLParameter param = new SQLParameter();
		param.addParam(billId);
		param.addParam(pk_user);
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<WfmTaskVO> vos = (List<WfmTaskVO>) dao.executeQuery(sql,
					param, new BeanListProcessor(WfmTaskVO.class));
			if (vos != null && vos.size() > 0) {
				return vos.get(0);
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new WfmServiceException(e.getMessage());
		}
		return null;
	}

	/**
	 * ���̽��Ȳ鿴�Ľ���ʽ
	 * 
	 * @param dataset
	 * @param nodecode
	 */
	public static void onFlow(String dataset, String nodecode) {
		String windowId = "wfm_flowhistory";
		String isNcWorkFlow = (String) AppUtil.getAppAttr("NC");
		Map<String, String> paramMap = new HashMap<String, String>();

		if ("Y".equals(isNcWorkFlow))
			windowId = "pfinfo";
		else {
			isExistFloInf(dataset, nodecode);
			String taskPk = WfmTaskUtil.getTaskPkFromSession();
			Task task = (Task) WfmTaskUtil.getTaskFromDB(taskPk);
			if (taskPk != null && !"".equals(taskPk)) {
				String proInsPK = task.getRootProIns().getPk_proins();
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProInsPk, proInsPK);
			}
		}
		String title = "���̽���";
		OpenProperties props = new OpenProperties();
		props.setWidth("900");
		props.setHeight("650");
		props.setTitle(title);
		props.setOpenId(windowId);
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}

	/**
	 * ���̽��Ȳ鿴�Ľ���ʽ
	 * 
	 * @param datasetStr
	 * @param nodecode
	 */
	public static void isExistFloInf(String datasetStr, String nodecode) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset(datasetStr);
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("��ѡ�д��༭����");
		Row row = ds.getSelectedRow();
		String billId = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FolwTypePk,
				getFlwTypePk(nodecode));
		String taskPk = null;
		if (StringUtils.isNotBlank(billId)) {
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
					.getPk_user();
			WfmTaskVO task = null;
			try {
				task = getLasterTaskByBillIdAndUser(billId, pk_user, "wf");

			} catch (WfmServiceException e) {
				// TODO Auto-generated catch block
				LfwLogger.error(e.getMessage(), e);
			}
			if (task != null) {
				taskPk = task.getPk_task();
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TaskPk,
						task.getPk_task());
			} else
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TaskPk, null);
		}
		try {
			String flowTypePk = WfmTaskUtil.getFlowTypePkFromSession();
			WfmProdefVO[] proDef;
			proDef = WfmServiceFacility.getProDefQry().getProDefByByWhere(
					"flwtype='" + flowTypePk + "' and isnotstartup='Y'");
			if (proDef != null && proDef.length > 0) {
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProdefPk,
						proDef[0].getPk_prodef());
			}

			String prodefPk = WfmTaskUtil.getProdefPkFromSession();
			if ((taskPk != null && !"".equals(taskPk))
					|| (flowTypePk != null && !"".equals(flowTypePk))
					|| (prodefPk != null && !"".equals(prodefPk))) {
			} else {
				throw new LfwRuntimeException("û�ж�������");
			}
		} catch (WfmServiceException e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
		}
	}

	/**
	 * chenmeng1 2014-07-02 �б���Ƭ������ť�Ĵ��������Ժ��ٿ�Ƭ���б������������ֱ�ӵ��ø÷��� ע��:
	 * 1.�������������ǰ������beforeShow()����ȡ�����´����ע��,�ô����λ����onBeforeRowAdd()������档
	 * Ҳ��������������ǰ������һ������ String pk_primarykey = generatePk();
	 * row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
	 * pk_primarykey); FillFileInfoHelper.resetItem(pk_primarykey);
	 * FillFileInfoHelper.fillFileInfo(masterDs, row);
	 * 
	 * 2.ע�͵�onSave()��������doValidate()�����һ���ִ���,ֻ�������²��� protected void
	 * doValidate(Dataset masterDs, List<Dataset> detailDs) throws
	 * LfwValidateException { super.doValidate(masterDs, detailDs); }
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public static void onAttchFile(Dataset ds) {
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
		// if (taskPk == null || taskPk.equals("")) {
		// this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
		// WfmConstants.BILLSTATE_BROWSE);
		// }
		CpWfmUtilFacade.setWfmBillState(primaryKeyValue, taskPk);
		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater(
				LfwFileConstants.SYSID_BAFILE, primaryKeyValue,
				CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "����";
		if (wfmParam != null && !wfmParam.isEmpty()) {
			param.putAll(wfmParam);
		}
		param.put(LfwFileConstants.BILLITEM, primaryKeyValue);
		CmdInvoker.invoke(new UifAttachCmd(title, param));
	}

	/***
	 * ��Ϣ����
	 * 
	 * @param contenttype
	 * @param subject
	 * @param content
	 * @param pk_group
	 * @param pk_cpusers
	 * @param msgType
	 * @param msgSourceType
	 */
	public static NCMessage createMsg(String contenttype, String subject,
			String content, String pk_group, String pk_cpusers, String msgType,
			String msgSourceType, String billId, String billChildId,
			String billpkOrg, String selftype) {
		NCMessage ncmsg = new NCMessage();
		MessageVO msgvo = new MessageVO();
		msgvo.setContenttype(contenttype);
		msgvo.setSubject(subject);
		msgvo.setContent(content);
		msgvo.setPk_group(pk_group);
		msgvo.setReceiver(pk_cpusers);
		msgvo.setSendtime(new UFDateTime());
		msgvo.setMsgtype(msgType);
		msgvo.setSender("NC_USER0000000000000");
		msgvo.setMsgsourcetype(msgSourceType);
		msgvo.setBillid(billId);
		msgvo.setBilltype(billChildId);
		msgvo.setDetail(billpkOrg);
		msgvo.setPk_detail(selftype);
		ncmsg.setMessage(msgvo);
		return ncmsg;
	}

	/***
	 * ��Ϣ����
	 * 
	 * @param msgs
	 */
	public static void msgSend(NCMessage[] msgs) {
		try {
			ScapLogger.debug("��Ϣ��ʼ����");
			// ֧��ͬʱ��������ŷ�����Ϣ��������NCMessage[] ����
			MessageCenter.sendMessage(msgs);
			ScapLogger.debug("��Ϣ���ͳɹ�");
		} catch (Exception e) {
			throw new LfwRuntimeException("��Ϣ����ʧ��");
		}
	}

	public static void onAttachFile_Attach(MouseEvent mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
		if (taskPk == null || taskPk.equals("")) {
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.addAppAttribute(WfmConstants.BILLSTATE,
							WfmConstants.BILLSTATE_BROWSE);
		}
		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater(
				LfwFileConstants.SYSID_BAFILE, primaryKeyValue,
				CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "����";
		CmdInvoker.invoke(new UifAttachCmd(title, param));
	}

	/**
	 * ��ȡ�����й�����������
	 * 
	 * @param richVO
	 * @param detailDss
	 */
	public static void fillCachedDeletedVO(SuperVO richVO, Dataset[] detailDss) {
		if (LfwCacheManager.getSessionCache() == null)
			return;
		if (richVO.getPrimaryKey() == null)
			return;
		// ɾ�����뻺���е�����
		List<SuperVO> delBodyVoList = (List<SuperVO>) LfwCacheManager
				.getSessionCache().get(richVO.getPrimaryKey());
		int dbvSize = delBodyVoList != null ? delBodyVoList.size() : 0;
		if (dbvSize == 0)
			return;

		Dataset2SuperVOSerializer<SuperVO> ser = new Dataset2SuperVOSerializer<SuperVO>();
		Datasets2RichVOSerializer rser = new Datasets2RichVOSerializer();

		int dLen = detailDss != null ? detailDss.length : 0;
		for (int i = 0; i < dLen; i++) {
			Dataset detailDs = detailDss[i];
			if (detailDs == null) {
				continue;
			}
			String delRowForeignKey = richVO.getPrimaryKey() + "_"
					+ detailDs.getId();
			// ��ds��ɾ������
			List<Row> delRowList = (List<Row>) LfwCacheManager
					.getSessionCache().get(delRowForeignKey);
			if (delRowList == null || delRowList.size() == 0) {
				continue;
			}
			// ��ɾ�����������л�
			SuperVO[] vos = ser.serialize(detailDs,
					delRowList.toArray(new Row[0]));
			int len = vos != null ? vos.length : 0;
			if (len > 0) {
				rser.add(richVO, vos, detailDs.getVoMeta());
			}

			LfwCacheManager.getSessionCache().remove(delRowForeignKey);
		}

		rser.updateStatus(richVO, delBodyVoList);

		LfwCacheManager.getSessionCache().remove(richVO.getPrimaryKey());
	}
	
	/**
	 * ������������ �ݴ����ύ���ݼ��ж�
	 * @param widget
	 * @param masterDs
	 * @param detailDs
	 * @param notNullBodyList
	 * @param bodyNotNull
	 * @throws LfwValidateException
	 */
	public static void doValidate(LfwView widget, Dataset masterDs,
			List<Dataset> detailDs, List<String> notNullBodyList,
			boolean bodyNotNull) throws LfwValidateException {
		IDataValidator validator = new DefaultDataValidator();
		validator.validate(masterDs, widget);
		if (detailDs != null) {
			int size = detailDs.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Dataset ds = detailDs.get(i);
					validator.validate(ds, widget);
					if (notNullBodyList != null
							&& notNullBodyList.contains(ds.getId())) {
						doSingleValidateBodyNotNull(ds);
					}
				}
				if (bodyNotNull) {
					doValidateBodyNotNull(detailDs,bodyNotNull);
				}
			}
		}
	}

	public static void doSingleValidateBodyNotNull(Dataset detailDs)
			throws LfwValidateException {
		boolean hasBody = false;
		if (detailDs.getCurrentRowData() == null) {
			hasBody = false;
		}
		if (detailDs.getCurrentRowCount() > 0) {
			hasBody = true;
		}
		if (!hasBody) {
			throw new LfwValidateException(detailDs.getCaption()
					+ NCLangRes4VoTransl.getNCLangRes().getStrByID("pub",
							"UifSaveCmd-000002")/* �������ݲ���Ϊ�� */);
		}
	}

	public static void doValidateBodyNotNull(List<Dataset> detailDs,boolean bodyNotNull)
			throws LfwValidateException {
		int size = detailDs.size();
		for (int i = 0; i < size; i++) {
			Dataset ds = detailDs.get(i);
			// �˱������ݲ���Ϊ��
			if (bodyNotNull) {
				if (ds.getCurrentRowData() == null) {
					throw new LfwValidateException(ds.getCaption()
							+ NCLangRes4VoTransl.getNCLangRes().getStrByID(
									"pub", "UifSaveCmd-000003")/* �ı������ݲ���Ϊ�� */);
				}
			}
		}
	}
	/**
	 * ��ȡurl�Ĳ���ֵ
	 * 
	 * @param urlParam
	 * @return
	 */
	public static String getUrlParamValue(String urlParam) {
		String urlParamValue = null;
		urlParamValue = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(urlParam);
		return urlParamValue;
	}

	/**
	 * ��ȡ��ͼ��ָ���˵�
	 * 
	 * @param view
	 * @param menuID
	 * @return
	 */
	public static MenubarComp getViewMenubarComp(LfwView view, String menuID) {
		MenubarComp mc = null;
		if (view != null) {
			mc = view.getViewMenus().getMenuBar(menuID);
		}
		return mc;
	}
/**
 * ��ȡ���Ĳ˵�
 * @param gc
 * @return
 */
	public static MenubarComp getGridMenubarComp(GridComp gc) {
		MenubarComp mc = null;
		if (gc != null) {
			mc = gc.getMenuBar();
		}
		return mc;
	}
	/**
	 * ��ȡ��ͼ��ָ����ť
	 * 
	 * @param view
	 * @param menuID
	 * @return
	 */
	public static ButtonComp getViewButtonComp(LfwView view, String buttonID) {
		ButtonComp mc = null;
		if (view != null) {
			mc = (ButtonComp)view.getViewComponents().getComponent(buttonID);
		}
		return mc;
	}
	public static WebComponent getViewWebComponentComp(LfwView view, String ID) {
		WebComponent mc = null;
		if (view != null) {
			mc = (WebComponent)view.getViewComponents().getComponent(ID);
		}
		return mc;
	}
	/**
	 * �Ƴ��б�ҳ����֯
	 * 
	 * @param uimeta
	 */
	public static void removeOrgView(UIMeta uimeta) {
		UIFlowvPanel simplequeryView = (UIFlowvPanel) UIElementFinder
				.findElementById(uimeta, "g_p_2");
		simplequeryView.setVisible(false);
	}

	/**
	 * �Ƴ��������ҳ��
	 * 
	 * @param uimeta
	 */
	public static void removeSimpleQueryView(UIMeta uimeta) {
		UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
		UIView simplequeryView = (UIView) UIElementFinder.findElementById(
				uimeta, "pubview_simpleexetask");
		uimeta.removeElement(simplequeryView);
		uimeta.setElement(mainView);
	}

	public static void setPluginSubmitRule(LfwView editView, String pluginId,
			String dsid) {
		PluginDesc PluginDesc = editView.getPluginDesc(pluginId);
		EventSubmitRule esr = new EventSubmitRule();
		ViewRule vr = new ViewRule();
		vr.setId(editView.getId());
		Dataset[] dataSets = editView.getViewModels().getDatasets();
		for (Dataset dss : dataSets) {
			if (!(dss instanceof MdDataset)) {
				continue;
			}
			DatasetRule dsRule = new DatasetRule();

			if (dss.getId().equals(dsid)) {
				dsRule.setId(dss.getId());
				dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
			} else {
				dsRule.setId(dss.getId());
				dsRule.setType(DatasetRule.TYPE_ALL_LINE);
			}
			vr.addDsRule(dsRule);
		}
		esr.addViewRule(vr);
		PluginDesc.setSubmitRule(esr);
	}

	/**
	 * ��ȡ��ͼ��ָ�����
	 * 
	 * @param viewID
	 * @param gridID
	 * @return
	 */
	public static GridComp getViewGridComp(LfwView view, String gridID) {
		GridComp mc = null;
		if (view != null) {
			mc = (GridComp) view.getViewComponents().getComponent(gridID);
		}
		return mc;
	}
	
	/**
	 * ��ȡָ���ı�
	 * @param view
	 * @param formID
	 * @return
	 */
	public static FormComp getViewFormComp(LfwView view, String formID) {
		FormComp mc = null;
		if (view != null) {
			mc = (FormComp) view.getViewComponents().getComponent(formID);
		}
		return mc;
	}

	
	public static void setFormFieldState(FormComp fc ,String field,boolean flag){
		if(fc==null){
			return;
		}
		FormElement fe=fc.getElementById(field);
		if(fe!=null){
			fe.setEnabled(flag);
		}
	}
	public static void setGirdFieldState(GridComp fc ,String field,boolean flag){
		if(fc==null){
			return;
		}
		GridColumn fe=(GridColumn) fc.getColumnByField(field);
		if(fe!=null){
			fe.setEditable(flag);
		}
	}
	/**
	 * ��ȡ��ͼ��ָ���˵�
	 * 
	 * @param viewID
	 * @param menuID
	 * @return
	 */
	public static MenubarComp getViewMenubarComp(String viewID, String menuID) {
		MenubarComp mc = null;
		LfwView view = getView(viewID) == null ? null : getView(viewID);
		if (view != null) {
			mc = view.getViewMenus().getMenuBar(menuID);
		}
		return mc;
	}

	public static void setSubmitBtnEnabled(boolean flag) {
		MenubarComp pubbar = getViewMenubarComp("pubview_simpleexetask",
				"simpleExeMenubar");
		setItemStateEnabled(pubbar, "btn_ok", flag);
		setItemStateEnabled(pubbar, "btn_save", flag);
		setItemStateEnabled(pubbar, "link_addattach", flag);
		// setItemStateEnabled(pubbar, "allFlow", flag);
		// pubbar.getItem("btn_ok").setEnabled(flag);
	}

	/**
	 * ����ָ���˵��İ�ť�Ƿ���ã�
	 * 
	 * @param mb
	 * @param itemid
	 * @param flag
	 */
	public static void setItemStateEnabled(MenubarComp mb, String itemid,
			boolean flag) {
		if (mb == null)
			return;
		MenuItem it1 = mb.getItem(itemid);
		if (it1 != null)
			it1.setEnabled(flag);
	}

	public static void setChildItemVisible(String fatherId, String childId) {
		MenubarComp mc = LfwRuntimeEnvironment.getWebContext().getPageMeta()
				.getView("main").getViewMenus().getMenuBar("menubar");
		List<MenuItem> gcs = mc.getElementById(fatherId).getChildList();
		if (gcs != null && gcs.size() > 0) {
			for (MenuItem mi : gcs) {
				if (mi.getId().equals(childId)) {
					mi.setVisible(false);
				}
			}
		}
	}

	public static void setBtnEnabledAfterSubmit(MenubarComp mb, boolean flag) {
		setSubmitBtnEnabled(false);
		setItemStateEnabled(mb, IGlobalConstants.BTN_SAVE, false);
		setItemStateEnabled(mb, IGlobalConstants.BTN_DEL, false);
		setItemStateEnabled(mb, IGlobalConstants.BTN_EDIT, false);
		setItemStateEnabled(mb, IGlobalConstants.BTN_COPY, false);
		setItemStateEnabled(mb, IGlobalConstants.BTN_STOP, false);
		// setItemStateEnabled(mb, IGlobalConstants.b, false);
	}

	/**
	 * ��ȡָ����ͼ
	 * 
	 * @param viewName
	 * @return
	 */
	public static LfwView getView(String viewName) {
		return AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext().getViewContext(viewName).getView();
	}
	
	public static String getOrgSql(){
		String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
		String where=" and 1=1 ";
		if("AH".equals(provinceId)||"HN".equals(provinceId)){
			where+=" and pk_orglevel2 = '~'  ";
		}
		String sql = "SELECT * FROM CP_ORGS where dr=0  and orglevel ='2' and enablestate='2' "+where+" START WITH  PK_ORG = '"
				+ CntUtil.getCntOrgPk()
				+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";
		return sql;
	}

	
}
