package com.scap.pub.attlist;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.ctrl.filemgr.LfwFileDsVO;
import nc.uap.ctrl.filemgr.UifFileDownloadCmd;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.cmd.UifFileUploadCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.util.LfwClassUtil;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.bs.framework.common.NCLocator;
import nc.vo.bd.defdoc.DefdocVO;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.cpb.log.CpLogger;
import nc.uap.portal.log.ScapLogger;
import java.text.DecimalFormat;
import nc.uap.ctrl.filrmgr.IOccupyOperate;
import nc.uap.lfw.core.comp.MenubarComp;
import org.apache.commons.lang.StringUtils;
import java.util.regex.Pattern;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import java.util.ArrayList;
import nc.uap.lfw.file.FileManager;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.file.UploadFileHelper;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.uap.lfw.file.vo.LfwFileVO;
import uap.web.bd.pub.AppUtil;
import nc.scap.def.util.ScapDefUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.file.FillFileInfoHelper;

public class AttachViewCtrl {
	private static final long serialVersionUID = 1L;
	private static final long ID = 5L;
	private static final String sysID = "bafile";
	private static final String MAIN_VIEW_ID = "attachlist";
	private static final String MAIN_DS_ID = "attach_ds";
	private AttachParam attachParam;

	public void loadAttach(Map<String, Object> keys) {
		ScapLogger.console("loadAttachList");// 日志
		AttachParam ap = (AttachParam) keys.get(IGlobalConstants.ATTACH_PARAM);
		if (ap == null)
			ap = new AttachParam(null);
		String billitem = ap.getBillItem();
		String aaaaaaaaaaaaaaaaa = getWfmBillItem();
		if (billitem == null || billitem.equals("")) {
			billitem = getWfmBillItem();
		}
//		String billtype = null;
		String billtype = ap.getBillType();
		LfwView view = CpWinUtil.getView(billtype==null?"attachlist":billtype);
		view.addAttribute(new ExtAttribute(
				LfwFileConstants.BILLTYPE,billtype));
		view.addAttribute(new ExtAttribute(IGlobalConstants.ATTACH_BILL_ID,
				billitem));
		view.addAttribute(new ExtAttribute(IGlobalConstants.ATTACH_CAN_UPLOAD,
				getStrByBool(ap.getCanUpload())));
		view.addAttribute(new ExtAttribute(
				IGlobalConstants.ATTACH_CAN_DOWNLOAD, getStrByBool(ap
						.getCanDownload())));
		view.addAttribute(new ExtAttribute(IGlobalConstants.ATTACH_CAN_DEL,
				getStrByBool(ap.getCanDel())));
		refshData(billitem, billtype);
	}

	public void onDataLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
	}

	public void refshData(String billitem, String billtype) {
		String sysid = getSysId();
		String filemgr = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.Par_FILEMANAGER);
		FileManager filemanager = getFileManager(filemgr, sysid);
		try {
			LfwFileVO[] files = filemanager.getAttachFileByItemID(billitem,
					billtype);
			Dataset ds = AppLifeCycleContext.current().getWindowContext()
					.getViewContext(billtype==null?"attachlist":billtype).getView().getViewModels()
					.getDataset("attach_ds");
			ds.clear();
			bindFiletoDS(files, ds);
		} catch (LfwBusinessException e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
		initMenuState(billtype==null?"attachlist":billtype);
	}

	/**
	 * 获取文件管理器
	 * 
	 * @author taoye 2013-7-22
	 */
	private FileManager getFileManager(String filemanager, String sysid) {
		FileManager fileManager = null;
		if (StringUtils.isNotBlank(filemanager)) {
			fileManager = (FileManager) LfwClassUtil.newInstance(filemanager);
		} else if (StringUtils.isNotBlank(sysid))
			fileManager = FileManager.getSystemFileManager(sysid);
		return fileManager;
	}

	/**
	 * 将文件信息更新至文件列表数据集
	 * 
	 * @author taoye 2013-7-22
	 */
	public void bindFiletoDS(LfwFileVO[] files, Dataset ds)
			throws CpbBusinessException {
		ds.clear();
		List<LfwFileDsVO> list = new ArrayList<LfwFileDsVO>();
		if (files != null) {
			for (LfwFileVO file : files) {
				LfwFileDsVO vo = new LfwFileDsVO();
				vo.setId(file.getPk_lfwfile());
				vo.setName(getSimpleName(file.getDisplayname(),
						file.getFiletypo()));
				vo.setType(file.getFiletypo());
				vo.setSize(getSizeStr(file.getFilesize()));
				vo.setLastmodified(file.getLastmodifytime());
				vo.setFilemanager(file.getFilemgr());
				String pk_user = file.getLastmodifyer();
				if (null != pk_user && !pk_user.equals("")) {
					CpUserVO user = NCLocator.getInstance()
							.lookup(ICpUserQry.class).getUserByPk(pk_user);
					if (user != null) {
						vo.setLastmodifier(user.getUser_code());
					} else {
						vo.setLastmodifier(pk_user);
					}
				}
				list.add(vo);

			}
		}
		new SuperVO2DatasetSerializer().serialize(
				(SuperVO[]) list.toArray(new LfwFileDsVO[0]), ds);
		ds.getCurrentRowData().getRows();
	}

	/**
	 * 获取不带文件类型的文件名
	 * 
	 * @author taoye 2013-7-22
	 */
	private String getSimpleName(String filename, String type) {
		if (null == filename || filename.equals(""))
			return "";
		if (null == type || type.equals(""))
			return filename;
		if (type.equals("NaN")) {
			return filename;
		}
		String simplename = "";
		Pattern pattern = Pattern.compile("\\." + type + "$");
		simplename = pattern.matcher(filename).replaceAll("");

		return simplename;
	}

	/**
	 * 获取文件大小字符串
	 * 
	 * @author taoye 2013-7-22
	 */
	private String getSizeStr(long size) {
		String sizestr = size + "B";
		if (size <= 0)
			sizestr = size + "B";
		else {
			sizestr = size + "";
			int sizelen = sizestr.length();
			if (sizelen <= 3)
				sizestr = size + "B";
			else if (sizelen > 3 && sizelen <= 6) {
				double newsize = size / Math.pow(10.0, 3);
				DecimalFormat dicf = new DecimalFormat("0.00");
				sizestr = dicf.format(newsize) + "K";
			} else if (sizelen > 6 && sizelen <= 9) {
				double newsize = size / Math.pow(10.0, 6);
				DecimalFormat dicf = new DecimalFormat("0.00");
				sizestr = dicf.format(newsize) + "M";
			} else {
				double newsize = size / Math.pow(10.0, 9);
				DecimalFormat dicf = new DecimalFormat("0.00");
				sizestr = dicf.format(newsize) + "G";
			}
		}
		return sizestr;
	}

	public void onUpload(MouseEvent<?> mouseEvent) {
		String title = "文件上传";
		String sysid = getSysId();
		LfwView view = CpWinUtil.getView(((GridComp)(mouseEvent.getSource())).getView().getId());

		// String billitem = getWfmBillItem();
		ExtAttribute att = view
				.getExtendAttribute(IGlobalConstants.ATTACH_BILL_ID);
		// if(billitem == null || billitem.equals("")) {
		if (att == null || att.getValue() == null || att.getValue().equals("")
				|| att.getValue().equals("null")) {
			throw new RuntimeException("未选中单据或单据未保存，不能上传附件！");
		}
		// }
		String billitem = (String) att.getValue();

		AppLifeCycleContext.current().setParameter("billitem", billitem);
		ExtAttribute billtype = (ExtAttribute) view
				.getExtendAttribute(LfwFileConstants.BILLTYPE);
		AppUtil.addAppAttr(LfwFileConstants.BILLTYPE, billtype==null?null:(String)billtype.getValue());
		AppLifeCycleContext.current().setParameter(LfwFileConstants.BILLTYPE, billtype==null?null:(String)billtype.getValue());
		String fileext = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter(LfwFileConstants.FILEEXT);
		String filedesc = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.FILEDESC);
		String sizelimit = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.SIZE_LIMIT);
		String bamodule = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.Par_Bamodule);

		// CmdInvoker.invoke(new UifFileUploadCmd("文件上传", null));
		Map<String, String> map = UploadFileHelper.BuildDefaultPamater(sysid,
				billitem, (String)billtype.getValue(), bamodule);
		//litao没有任何文件类型限制时，有安全隐患
		map.put(LfwFileConstants.FILEEXT, fileext == null ? "*.jpg;*.png;*.docx;*.doc;*.xlsx;*.xlsm;*.xls;*.ppt;*.pptx;*.pdf;*.zip;*.ZIP;*.rar;*.RAR" : fileext);
		map.put(LfwFileConstants.FILEDESC, filedesc == null ? "自定义文件" : filedesc);
		map.put(LfwFileConstants.SIZE_LIMIT, sizelimit == null ? "50M" : sizelimit);
		// 传入参数
//		map.put(LfwFileConstants.FILEEXT, fileext == null ? "" : fileext);
//		map.put(LfwFileConstants.FILEDESC, filedesc == null ? "" : filedesc);
//		map.put(LfwFileConstants.SIZE_LIMIT, sizelimit == null ? "" : sizelimit);
		map.put(LfwFileConstants.CALLBACK_METHOD, "tbcall");

		UifFileUploadCmd filecmd = new UifFileUploadCmd(title, map);
		CmdInvoker.invoke(filecmd);
	}

	public void refshDataByScript(ScriptEvent event) {
		String billitem = AppLifeCycleContext.current().getParameter(
				LfwFileConstants.BILLITEM);
		Object billtype = AppUtil.getAppAttr(LfwFileConstants.BILLTYPE);
		refshData(billitem, billtype==null?null:(String)billtype);
		doExtend();
		AppUtil.addAppAttr(LfwFileConstants.BILLTYPE, null);
	}

	private void initMenuState(String viewId) {
		LfwView view = CpWinUtil.getView(viewId);
		GridComp grid = (GridComp) view.getViewComponents().getComponent(
				"attach_grid");
		MenubarComp menubar = grid.getMenuBar();
		Dataset ds = view.getViewModels().getDataset("attach_ds");
		Row selectedRow = ds.getSelectedRow();
		boolean canUpload = Boolean.parseBoolean(view
				.getExtendAttribute(IGlobalConstants.ATTACH_CAN_UPLOAD)
				.getValue().toString());
		boolean canDownload = Boolean.parseBoolean(view
				.getExtendAttribute(IGlobalConstants.ATTACH_CAN_DOWNLOAD)
				.getValue().toString());
		boolean canDel = Boolean.parseBoolean(view
				.getExtendAttribute(IGlobalConstants.ATTACH_CAN_DEL).getValue()
				.toString());
		MenuItem upload = menubar.getItem("attach_grid$HeaderBtn_Upload");
		MenuItem download = menubar.getItem("attach_grid$HeaderBtn_Download");
		MenuItem delete = menubar.getItem("attach_grid$HeaderBtn_Delete");
		upload.setEnabled(canUpload);
		download.setEnabled(canDownload);
		delete.setEnabled(canDel);
		if (selectedRow == null) {
			download.setEnabled(false);
			delete.setEnabled(false);
		}
	}

	private void doExtend() {
		String controlclzz = (String) LfwRuntimeEnvironment
				.getWebContext()
				.getWebSession()
				.getOriginalParameter(
						LfwFileConstants.Filemgr_Para_OperateClazz);
		;
		IOccupyOperate op = null;
		if (controlclzz == null || "".equals(controlclzz))
			return;
		try {
			op = (IOccupyOperate) Class.forName(controlclzz).newInstance();
		} catch (InstantiationException e) {
			CpLogger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			CpLogger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			CpLogger.error(e.getMessage(), e);
		}
		if (op != null)
			op.handleWfmInfo();
	}

	public void onDownload(MouseEvent<?> mouseEvent) {
		LfwView main = CpWinUtil.getView("attachlist");
		Dataset ds = CpWinUtil.getDataset(main, "attach_ds");
		String sysid = getSysId();
		boolean isextend = true;
		if (ds != null) {
			Row row = ds.getSelectedRow();
			if (row != null) {
				isextend = false;
				String filepk = row.getString(ds.nameToIndex("id"));
				if (filepk != null)
					CmdInvoker
							.invoke(new UifFileDownloadCmd(
									sysid,
									row.getString(ds
											.nameToIndex(LfwFileConstants.Par_FILEMANAGER)),
									filepk));
			}
			doExtend();
		}
		if (isextend) {
			AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("imp",
							"FileMgrController-000003")/* 请先选择文件? */);
		}

	}

	public void onDelete(MouseEvent<?> mouseEvent) {
		String viewId = ((GridComp)mouseEvent.getSource()).getView().getId();
		LfwView main = CpWinUtil.getView(viewId);
		Dataset ds = CpWinUtil.getDataset(main, "attach_ds");
		String sysid = getSysId();
		boolean isextend = true;
		if (ds != null) {
			Row row = ds.getSelectedRow();
			if (row != null) {
				isextend = false;
				AppInteractionUtil.showConfirmDialog(
						NCLangRes4VoTransl.getNCLangRes().getStrByID("imp",
								"FileMgrController-000000")/* 提示 */,
						NCLangRes4VoTransl.getNCLangRes().getStrByID("imp",
								"FileMgrController-000001")/* 确定要删除选中的文件? */);
				if (AppInteractionUtil.getConfirmDialogResult().booleanValue()) {
					String filepk = row.getString(ds.nameToIndex("id"));
					if (filepk != null) {
						// FileManager filemanager = FileManager
						// .getSystemFileManager(sysid);
						FileManager filemanager = getFileManager(
								row.getString(ds
										.nameToIndex(LfwFileConstants.Par_FILEMANAGER)),
								sysid);
						try {
							filemanager.delete(filepk);
							ds.removeRow(row);
						} catch (Exception e) {
							CpLogger.error(e);
						}
					}
				}
			}
			doExtend();
		}
		if (isextend) {
			AppInteractionUtil.showMessageDialog(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("imp",
							"FileMgrController-000003")/* 请先选择文件? */);
		}
		initMenuState(viewId);
	}

	public void onView(MouseEvent<?> mouseEvent) {
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.getCurrentWindowContext()
				.popView(
						"view",
						DialogConstant.MAX_WIDTH,
						DialogConstant.MAX_HEIGHT,
						NCLangRes4VoTransl.getNCLangRes().getStrByID("imp",
								"FileMgrController-000003")/* 在线查看 */, true,
						false);
	}

	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		// CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		initMenuState(ds.getView().getId());
	}

	public String getSysId() {
		return LfwFileConstants.SYSID_BAFILE;
	}

	public String getStrByBool(boolean flag) {
		if (flag) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 加载附件类型
	 * 
	 * @author taoye 2013-8-6
	 */
	public void onTypeLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode("scapco_attach_type");
		for (DefdocVO vo : vos) {
			Row row = ds.getEmptyRow();
			row.setValue(ds.nameToIndex("id"), vo.getPk_defdoc());
			row.setValue(ds.nameToIndex("value"), vo.getCode());
			row.setValue(ds.nameToIndex("name"), vo.getName());
			ds.addRow(row);
		}
		MenubarComp menubar = CpWinUtil.getMenuBarComp(
				CpWinUtil.getView(MAIN_VIEW_ID), "attachtype_menu");
		MenuItem item = menubar.getItem("operate");
		item.setEnabled(false);
	}

	public String getWfmBillItem() {
		String billitem = (String) AppUtil
				.getAppAttr(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem);
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		if (StringUtils.isNotBlank(taskPk)) {
			String tbillitem = WfmTaskUtil.getBillItemByTaskpk(taskPk);
			if (StringUtils.isNotBlank(tbillitem)
					&& !tbillitem.equals(billitem)) {
				billitem = tbillitem; // 只有一样时才取流程实例上的billitem，否则取传入的
			}
		} else {
			if (StringUtils.isBlank(billitem)) {
				billitem = FillFileInfoHelper.getOrCreateItem();
			}
		}
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem,
				billitem);
		AppUtil.addAppAttr(LfwFileConstants.Filemgr_Para_OperateClazz,
				"nc.uap.wfm.filemgr.OccupyOperateImp");
		return billitem;
	}

	/**
	 * 选中附件类型
	 * 
	 * @author taoye 2013-8-6
	 */
	public void onAfterTypeSelect(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		MenubarComp menubar = CpWinUtil.getMenuBarComp(
				CpWinUtil.getView(MAIN_VIEW_ID), "attachtype_menu");
		MenuItem item = menubar.getItem("operate");
		item.setEnabled(true);
		String x = "";
	}

	public void onTypeAdd(MouseEvent mouseEvent) {

		String x = "";
	}

	public void onTypeEdit(MouseEvent mouseEvent) {
		String x = "";
	}

	public void onTypeDel(MouseEvent mouseEvent) {
		String x = "";
	}
}
