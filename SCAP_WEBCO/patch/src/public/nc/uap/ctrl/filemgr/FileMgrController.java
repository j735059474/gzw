package nc.uap.ctrl.filemgr;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.util.CpbUtil;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.ctrl.constant.FileMgrConstant;
import nc.uap.ctrl.excel.CpExcelModel;
import nc.uap.ctrl.excel.UifExcelImportUtil;
import nc.uap.ctrl.filrmgr.IOccupyOperate;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifFileUploadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctrl.WindowController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.ml.LfwResBundle;

public class FileMgrController implements WindowController, Serializable {

	
	
	private static final long serialVersionUID = 7532916478964732880L;
	
	
	public void sysWindowClosed(PageEvent event) {
		LfwRuntimeEnvironment.getWebContext().destroyWebSession();
	}

	public void onUpload(MouseEvent<?> mouseEvent) {
		String title ="文件上传";
		String sysid = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.SYSID);
		if(sysid  == null||sysid.equals(""))
			sysid = getSysId();
		String billitem = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLITEM);
		String billtype = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLTYPE);
		String fileext = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.FILEEXT);
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
		Map<String, String> map = UploadFileHelper.BuildDefaultPamater(sysid, billitem, billtype, bamodule);
		
//		map.put(LfwFileConstants.FILEEXT, fileext == null ? "" : fileext);
//		map.put(LfwFileConstants.FILEDESC, filedesc == null ? "" : filedesc);
//		map.put(LfwFileConstants.SIZE_LIMIT, sizelimit == null ? "" : sizelimit);
		// 传入参数
		//litao没有任何文件类型限制时，有安全隐患
		map.put(LfwFileConstants.FILEEXT, fileext == null ? "*.jpg;*.png;*.docx;*.doc;*.xlsx;*.xlsm;*.xls;*.ppt;*.pptx;*.pdf;*.zip;*.ZIP;*.rar;*.RAR" : fileext);
		map.put(LfwFileConstants.FILEDESC, filedesc == null ? "自定义文件" : filedesc);
		map.put(LfwFileConstants.SIZE_LIMIT, sizelimit == null ? "50M" : sizelimit);
		map.put(LfwFileConstants.CALLBACK_METHOD, "tbcall");
		
		UifFileUploadCmd filecmd = new UifFileUploadCmd(title, map);
		CmdInvoker.invoke(filecmd);
	}

	public void onUploadedExcelFile(ScriptEvent event) {
		CpExcelModel model = new UifExcelImportUtil().parseExcelByEnv();
	}

	public void onDownload(MouseEvent<?> mouseEvent) {
		LfwView main = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = main.getViewModels().getDataset("fileds");
		String sysid = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.SYSID);
		if(sysid  == null)
			sysid = getSysId();
		boolean isextend = true;
		if (ds != null) {
			Row row = ds.getSelectedRow();
			if (row != null) {
				isextend = false;
				String filepk = row.getString(ds.nameToIndex("id"));
				if (filepk != null)
					CmdInvoker.invoke(new UifFileDownloadCmd(sysid,row.getString(ds.nameToIndex(LfwFileConstants.Par_FILEMANAGER)), filepk));
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
		LfwView main = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset ds = main.getViewModels().getDataset("fileds");
		String sysid = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.SYSID);
		if(sysid  == null)
			sysid = getSysId();
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
//						FileManager filemanager = FileManager
//								.getSystemFileManager(sysid);
						FileManager filemanager = getFileManager(row.getString(ds.nameToIndex(LfwFileConstants.Par_FILEMANAGER)), sysid);
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
		initMenuState();
	}

	public void onModify(MouseEvent<?> mouseEvent) {

	}

	public void onScan(MouseEvent<?> mouseEvent) {
		OpenProperties winProps = new OpenProperties();
		winProps.setOpenId("pubview_scanview");
		winProps.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("imp",
				"FileMgrController-000002")/* 高拍仪 */);
		winProps.setWidth("577");
		winProps.setHeight("443");
		winProps.setPopclose(true);
		winProps.setButtonZone(false);
		AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext().popView(winProps);

		String sysid = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.SYSID);
		if(sysid  == null)
			sysid = getSysId();
		String billitem = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLITEM);
		String billtype = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLTYPE);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("url", "/pt/file/upload?");
		paramMap.put(LfwFileConstants.BILLTYPE, billtype == null ? "" : billtype);
		paramMap.put(LfwFileConstants.BILLITEM, billitem == null ? "" : billitem);
		paramMap.put(LfwFileConstants.SYSID, sysid == null ? "" : sysid);
		paramMap.put("sys_datasource", "");
		paramMap.put("filemanager", "");
		new UifPlugoutCmd("main", "main_scanview_plugout", paramMap).execute();
		doExtend();
	}

	public void onView(MouseEvent<?> mouseEvent) {
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.getCurrentWindowContext()
				.popView("view", DialogConstant.MAX_WIDTH,
						DialogConstant.MAX_HEIGHT, NCLangRes4VoTransl.getNCLangRes().getStrByID("imp",
						"FileMgrController-000003")/* 在线查看 */, true, false);
	}

	public void onDataLoad_fileds(DataLoadEvent dataLoadEvent) {
		refshData();
	}

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
	}

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

	public void refshDataByScript(ScriptEvent event) {
		refshData();
		doExtend();
	}

	public void refshData() {
		String sysid = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.SYSID);
		if(sysid  == null || "".equals(sysid))
			sysid = getSysId();
		String billitem = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLITEM);
		String billtype = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLTYPE);
		String filemgr = (String) LfwRuntimeEnvironment.getWebContext()
		.getWebSession()
		.getOriginalParameter(LfwFileConstants.Par_FILEMANAGER);
		FileManager filemanager = getFileManager(filemgr, sysid);
		try {
			LfwFileVO[] files = filemanager.getAttachFileByItemID(billitem,
					billtype);
			Dataset ds = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView().getViewModels()
					.getDataset("fileds");
			ds.clear();
			bindFiletoDS(files, ds);
		} catch (LfwBusinessException e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

	public void changeFileName(ScriptEvent event) {
		String pk_file = AppLifeCycleContext.current().getParameter("pk_file");// 参数
		String newname = AppLifeCycleContext.current().getParameter("newname");// 参数
		String filemanager = AppLifeCycleContext.current().getParameter("filemanager");// 参数
//		String sysid = (String) LfwRuntimeEnvironment.getWebContext()
//				.getWebSession()
//				.getOriginalParameter(LfwFileConstants.SYSID);

		FileManager fileManager = (FileManager) LfwClassUtil.newInstance(filemanager);
		try {
			LfwFileVO filevo = fileManager.getFileVO(pk_file);
			if (null != filevo) {
				filevo.setDisplayname(newname + "." + filevo.getFiletypo());
				filevo.setLastmodifytime(new UFDateTime());
				filevo.setLastmodifyer(CpbUtil.getCntUserCode());
				fileManager.updateVo(filevo);
				Dataset ds = AppLifeCycleContext.current().getWindowContext()
						.getViewContext("main").getView().getViewModels()
						.getDataset("fileds");
				Row row = ds.getSelectedRow();
				row.setValue(ds.nameToIndex("name"), newname);
				row.setValue(ds.nameToIndex("lastmodified"),
						filevo.getLastmodifytime());
				row.setValue(ds.nameToIndex("lastmodifier"),
						filevo.getLastmodifyer());
			}
		} catch (Exception e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
		doExtend();
	}

	public void btnokonclick(MouseEvent mouseEvent) {
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}

	public void btncancelonclick(MouseEvent mouseEvent) {
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}

	public void onbeforeShow(DialogEvent dialogEvent) {
		initMenuState();
	}

	public void pluginmain_plugin(Map keys) {
		refshData();
	}

	public void onAfterRowSelect(DatasetEvent se) {
		initMenuState();
	}

	private void initMenuState() {

		LfwView main = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		MenubarComp menubar = main.getViewMenus().getMenuBar("opemenu");
		Dataset ds = main.getViewModels().getDataset("fileds");
		Row selectedRow = ds.getSelectedRow();
		String state = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter("state");
		String usescanable = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter("usescanable");
		String printable = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter("printable");

		String editonlieable = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter("editonlieable");

		AppLifeCycleContext.current().getApplicationContext()
				.setClientAttribute("editonlieable", editonlieable);
		AppLifeCycleContext.current().getApplicationContext()
				.setClientAttribute("printable", printable);

		int cstate = -1;
		try {
			if (state != null && !"".equals(state))
				cstate = Integer.parseInt(state);
			else
				cstate = -1;
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			cstate = 0;
		}

		if (-1 == cstate && selectedRow != null) {
			menubar.getItem("upload").setEnabled(true);
			menubar.getItem("download").setEnabled(true);
			menubar.getItem("btn_delete").setEnabled(true);
			menubar.getItem("modify").setEnabled(true);
			menubar.getItem("view").setEnabled(true);
		}
		if (0 == cstate) {
			menubar.getItem("upload").setEnabled(false);
			menubar.getItem("download").setEnabled(false);
			menubar.getItem("btn_delete").setEnabled(false);
			menubar.getItem("modify").setEnabled(false);
			menubar.getItem("view").setEnabled(false);
		} else {
			if ((FileMgrConstant.State_Upload & cstate) == FileMgrConstant.State_Upload) {
				menubar.getItem("upload").setEnabled(true);
				//menubar.getItem("scan").setEnabled(true);
			} else {
				menubar.getItem("upload").setEnabled(false);
				//menubar.getItem("scan").setEnabled(false);
			}
			if ((FileMgrConstant.State_Dowload & cstate) == FileMgrConstant.State_Dowload
					&& selectedRow != null) {
				menubar.getItem("download").setEnabled(true);
			} else {
				menubar.getItem("download").setEnabled(false);
			}
			if ((FileMgrConstant.State_Delete & cstate) == FileMgrConstant.State_Delete
					&& selectedRow != null) {
				menubar.getItem("btn_delete").setEnabled(true);
			} else {
				menubar.getItem("btn_delete").setEnabled(false);
			}
			if ((FileMgrConstant.State_Modify & cstate) == FileMgrConstant.State_Modify&& selectedRow != null) {
				menubar.getItem("modify").setEnabled(true);
			} else {
				menubar.getItem("modify").setEnabled(false);
			}
			if ((FileMgrConstant.State_View & cstate) == FileMgrConstant.State_View&& selectedRow != null) {
				menubar.getItem("view").setEnabled(true);
			} else {
				menubar.getItem("view").setEnabled(false);
			}

		}
//		if ("true".equals(usescanable)) {
//			menubar.getItem("scan").setEnabled(true);
//		} else {
//			menubar.getItem("scan").setEnabled(false);
//		}
		if (selectedRow == null) {
			menubar.getItem("download").setEnabled(false);
			menubar.getItem("modify").setEnabled(false);
			menubar.getItem("view").setEnabled(false);
		}
	}

	private void doExtend() {
		String controlclzz = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.Filemgr_Para_OperateClazz);
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
	
	private FileManager getFileManager(String filemanager,String sysid) {
		FileManager fileManager = null;
		if(StringUtils.isNotBlank(filemanager)){
			fileManager = (FileManager) LfwClassUtil.newInstance(filemanager);
		}
		else if(StringUtils.isNotBlank(sysid))
			fileManager = FileManager.getSystemFileManager(sysid);
		return fileManager;
	}
	private String getSysId(){
		return LfwFileConstants.SYSID_BAFILE;
	}
}
