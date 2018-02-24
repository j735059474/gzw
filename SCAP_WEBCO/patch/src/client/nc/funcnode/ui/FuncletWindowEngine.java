package nc.funcnode.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.UserExit;
import nc.bs.logging.Logger;
import nc.desktop.ui.FuncPowerTreeSupport;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.bs.FuncletModel;
import nc.funcnode.bs.IOpenNodeRCService;
import nc.login.vo.AttachedProps;
import nc.login.vo.NCSession;
import nc.sfbase.client.ClientToolKit;
import nc.sfbase.toolkit.ComponentUILock;
import nc.sfbase.vo.NCEnv;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.ExtTabbedPane;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.sm.task.TaskGroup;
import nc.vo.org.GroupVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.operatelog.OperateLogVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.uap.rbac.profile.FuncNodePermInfoWrapper;
import nc.vo.uap.rbac.profile.FunctionPermProfileManager;
import nc.vo.uap.rbac.profile.IFunctionPermProfile;

class FuncletWindowEngine {
	static class OpenParam {
		public OpenParam(FuncWindowType funcWindowType, FuncletInitData initData, Component parent, Component invoker,
				boolean synOpen, boolean modal, FuncletLinkListener linkListener) {
			super();
			this.funcWindowType = funcWindowType;
			this.initData = initData;
			this.parent = parent;
			this.invoker = invoker;
			this.synOpen = synOpen;
			this.modal = modal;
			this.linkListener = linkListener;

		}

		public OpenParam(FuncWindowType funcWindowType, FuncletInitData initData, Component parent, Component invoker,
				boolean synOpen, boolean modal, FuncletLinkListener linkListener, Dimension size) {
			this(funcWindowType, initData, parent, invoker, synOpen, modal, linkListener);
			this.size = size;

		}

		public OpenParam(FuncWindowType funcWindowType, FuncletInitData initData, Component parent, Component invoker,
				boolean synOpen, boolean modal, FuncletLinkListener linkListener, Dimension size,
				String[] busiActiveCodes) {
			this(funcWindowType, initData, parent, invoker, synOpen, modal, linkListener, size);
			this.busiActiveCodes = busiActiveCodes;

		}
		public OpenParam(FuncWindowType funcWindowType, FuncletInitData initData, Component parent, Component invoker,
				boolean synOpen, boolean modal, FuncletLinkListener linkListener, FuncletListener funcletListener, Dimension size,
				String[] busiActiveCodes) {
			this(funcWindowType, initData, parent, invoker, synOpen, modal, linkListener, size, busiActiveCodes);
			this.funcletListener = funcletListener;

		}

		public FuncWindowType funcWindowType = FuncWindowType.TAB_PANEL;
		public FuncletInitData initData = null;
		public Component parent = null;
		public Component invoker = null;
		public boolean synOpen = false;
		public boolean modal = false;
		public FuncletLinkListener linkListener = null;
		public FuncletListener funcletListener = null;
		public Dimension size = null;
		public String[] busiActiveCodes = null;
		public String windowTitle = null;
	}

	private FuncRegisterVO frVO = null;
	private AttachedProps response = null;
	private AttachedProps request = null;
	private static int openningCount = 0;
	public FuncletWindowEngine(FuncRegisterVO frVO) {
		super();
		if(frVO == null){
			throw new RuntimeException("funcRegisterVO can't be null");
		}
		this.frVO = frVO;
	}

	public void openNode(final OpenParam openParam) {
		final Runnable run = new Runnable() {
			@Override
			public void run() {
//				Cursor oldCursor = null;
				if (openParam.invoker != null){
//					oldCursor = openParam.invoker.getCursor();
//					openParam.invoker.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					ComponentUILock.lockComponent(openParam.invoker);
				}
				try {
					synchronized (frVO.getFuncode()) {
						openNodeImple(openParam);
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				} finally{
					if (openParam.invoker != null){
//						openParam.invoker.setCursor(oldCursor);
						ComponentUILock.unlockComponent(openParam.invoker);
					}
					
				}

			}
		};
		boolean synOpen = openParam.synOpen;
		if (synOpen) {
			run.run();
		} else {
			 new Thread(run, "open node " + frVO.getFuncode()).start();
//			SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
//				@Override
//				protected Object doInBackground() throws Exception {
//					run.run();
//					return null;
//				}
//			};
//			worker.execute();
		}
	}
	private String dealOpenNodeForLogMonitor(){
		String text = "#busiaction:Open Node-"+frVO.getFun_name();
//		UserExit.getInstance().setBusiaction("打开节点-"+frVO.getFun_name());
		Logger.debug(text);
		return text;
		
	}
	private void openNodeImple(OpenParam openParam) throws Exception {
//		Component invoker = openParam.invoker;
		try {
			String logMsg = "opening node:"+frVO.getFuncode()+" / "+frVO.getFun_name() +" / "+frVO.getOwn_module();
			Logger.debug(logMsg);
			Component parent = openParam.parent;
			IFuncletWindow window = findOpenedWindow(openParam);
			if (window != null) {
				showWindowInEDT(window);
				if (openParam.initData != null) {
					try {
						window.getCurrFunclet().initData(openParam.initData);
					} catch (Throwable th) {
						Logger.error(th.getMessage(), th);
					}
				}
				return;
			}
			UserExit.getInstance().setBusiaction(NCLangRes.getInstance().getStrByID("sfbase", "FWEngine-0000", null, new String[]{frVO.getFun_name()})/*打开节点-{0}*/);
			if (!canOpen(openParam)) {
				return;
			}
			openningCount++;
			if (isTopLevelComp()) {

			} else {
				String logText = dealOpenNodeForLogMonitor();
				long t1 = System.currentTimeMillis();
				FuncletWidgetContainer fwContainer = createFuncletWidgetContainer(openParam);
				long t2 = System.currentTimeMillis() - t1;
				String text = logText + " cost time "+t2;
				Logger.debug(text);
				if (fwContainer != null) {
					String windowTitle = openParam.windowTitle;
					if(windowTitle == null || windowTitle.trim().length() == 0){
						windowTitle = FuncPowerTreeSupport.getFuncNodeDisplayName(frVO);// .getFunTransStr(frVO.getFun_code(),
					}
					// frVO.getFun_name());
					FuncWindowType type = openParam.funcWindowType;
					if (type.equals(FuncWindowType.FRAME)) {
						window = new FuncNodeFrame(windowTitle, fwContainer);
						FuncNodeFrame frame = (FuncNodeFrame) window;
						if (openParam.size != null) {
							frame.setSize(openParam.size);
						}
						// frame.setLocationRelativeTo(openParam.parent);
						setWindowToScreenCenter(frame);
					} else if (type.equals(FuncWindowType.DIALOG)) {
						boolean modal = openParam.modal;
						Window owner = null;
						Component comp = openParam.parent;
						if (comp != null)
							owner = ClientToolKit.getWindowFromComponent(comp);// JOptionPane.getFrameForComponent(comp);
						window = new FuncNodeDialog(owner, windowTitle, modal, fwContainer);
						FuncNodeDialog dlg = ((FuncNodeDialog) window);
						if (openParam.size != null) {
							dlg.setSize(openParam.size);
						}
						// dlg.setLocationRelativeTo(dlg.getParent());
						setWindowToScreenCenter(dlg);
					} else if (type.equals(FuncWindowType.FORCEMODALDLG)) {
						Window owner = null;
						Component comp = openParam.parent;
						if (comp != null)
							owner = ClientToolKit.getWindowFromComponent(comp);// JOptionPane.getFrameForComponent(comp);
						window = new FuncNodeForceModalDlg(owner, windowTitle, fwContainer);
						FuncNodeForceModalDlg dlg = (FuncNodeForceModalDlg) window;
						if (openParam.size != null) {
							dlg.setSize(openParam.size);
						}
						// dlg.setLocationRelativeTo(dlg.getParent());
						setWindowToScreenCenter(dlg);
					} else {
						ExtTabbedPane tabbedPane = WorkbenchEnvironment.getInstance().getWorkbench()
								.getWorkSpaceTabbedPane();
						if (parent != null && parent instanceof ExtTabbedPane) {
							tabbedPane = (ExtTabbedPane) parent;
						}
						window = new FuncNodePanel(tabbedPane, fwContainer,windowTitle);
					}
					showWindowInEDT(window);
					showAlterFilesInThread(window);
				}
			}
		} catch (Throwable thr) {
			Logger.error(thr.getMessage(), thr);
		} finally {
			openningCount--;
		}
	}
	private void showWindowInEDT(final IFuncletWindow window ){
		Runnable run = new Runnable() {
			@Override
			public void run() {
				window.showWindow();
			}
		};
		if(SwingUtilities.isEventDispatchThread()){
			run.run();
		}else{
			SwingUtilities.invokeLater(run);
		}
	}
	private void setWindowToScreenCenter(Window w) {
		Dimension size = w.getSize();
		Dimension scsize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (scsize.width - size.width) / 2;
		int y = (scsize.height - size.height) / 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		w.setLocation(x, y);
	}

	private void showAlterFilesInThread(final IFuncletWindow window) {
		if (WorkbenchEnvironment.getInstance().isUser()) {
			new Thread() {
				public void run() {
					String[][] files = getAlterFiles();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						Logger.error(e.getMessage(), e);
					}
					showAlterFiles(files, window);
				}
			}.start();
		}
	}
	
	private String[][] getAlterFiles() {
		String files[][] = null;
		try {
			files = (String[][]) response.getAttachedProp("_AlertFile");
		} catch (Exception e) {
			Logger.error("Error", e);
		}
		return files;
	}

	private void showAlterFiles(String[][] files, IFuncletWindow window) {
		// 预警文件
		if (files != null && files.length > 0) {
			Logger.debug("==== Funcnode Alter Files ====");
			HashMap<String, URL> hm = new HashMap<String, URL>();
			for (int i = 0; i < files.length; i++) {
				String[] alterFile = files[i];
				try {
					Logger.debug(alterFile[0] + ":" + alterFile[1]);
					URL url = convertAlterFileStringToURL(alterFile[1]);
					hm.put(alterFile[0], url);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);;
				}
			}
			if (hm.size() == 1) {
				ClientToolKit.showDocument(hm.values().iterator().next(), "_blank");
			} else {
				TaskGroup tg = window.getTaskGroupFactory().getTaskGroup(NCLangRes.getInstance().getStrByID("sfbase", "FWEngine-0001", null, new String[]{frVO.getFun_name()})/*预警文件:{0}*/);
				tg.removeAllAction();

				Iterator<String> iter = hm.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					final URL url = hm.get(key);
					Action a = new AbstractAction(key) {
						private static final long serialVersionUID = 1L;

						public void actionPerformed(ActionEvent e) {
							ClientToolKit.showDocument(url, "_blank");
						}
					};
					tg.addAction(a);
				}
			}
			Logger.debug("======== End =========");
		} else {
			Logger.debug("========No alert files!============");
		}
		//

	}

	private URL convertAlterFileStringToURL(String file) throws Exception {
		StringBuilder sbUrl = new StringBuilder(ClientToolKit.getServerURL()).append("service/ShowAlertFileServlet?");
		sbUrl.append("dsName=").append(WorkbenchEnvironment.getInstance().getDSName());
		sbUrl.append("&fileName=").append(file);
		URL url = new URL(sbUrl.toString());
		return url;
	}

	private boolean isTopLevelComp() throws Exception {
		String clsName = frVO.getClass_name();
		Class<?> cls = Class.forName(clsName);
		if (Window.class.isAssignableFrom(cls)) {
			return true;
		}
		return false;
	}
	
	private FuncletWidgetContainer createFuncletWidgetContainer(OpenParam openParam) {
		FuncletWidgetContainer fwContainer = null;
		try {
			FuncletModel funcletModel = null;
			if (response != null) {
				funcletModel = response.getAttachedProp(FuncletModel.class);
				if(funcletModel!=null){
				FuncNodePermInfoWrapper wrapper = funcletModel.getFuncNodePermInfoWrapper();
				
				String userCode = WorkbenchEnvironment.getInstance().getLoginUser().getUser_code();
				IFunctionPermProfile profile = FunctionPermProfileManager.getInstance().getProfile(userCode);
				if (profile != null)
					profile.updateFuncNodeDetailPermInfo(wrapper,openParam.busiActiveCodes);
				}
				// FunctionPermProfileManager.getInstance().updateProfile(userCode,profile);
			}
			fwContainer = FuncletWidgetContainerFactory.createFuncletWidgetContainer(frVO, funcletModel,
					openParam.initData, openParam.linkListener, openParam.funcletListener);
		} catch (PreOpenCheckException e) {
			String msg = e.getMessage();
			FuncletWidgetContainerFactory.unregisterServerInfo(frVO.getFuncode(), frVO.getOwn_module());
			fwContainer = null;
			if (msg != null && msg.trim().length() > 0) {
				Frame frame = JOptionPane.getFrameForComponent(openParam.parent);
				MessageDialog.showHintDlg(frame, NCLangRes.getInstance().getStrByID("sfbase", "sfbase-0002")/*提示*/, msg);
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			FuncletWidgetContainerFactory.unregisterServerInfo(frVO.getFuncode(), frVO.getOwn_module());
		}
		return fwContainer;

	}

	private IFuncletWindow findOpenedWindow(OpenParam openParam) {
		if (FuncWindowType.FORCEMODALDLG.equals(openParam.funcWindowType)) {
			return null;
		}
		String funcCode = frVO.getFuncode();
		IFuncletWindow window = WorkbenchEnvironment.getInstance().findOpenedFuncletWindow(funcCode);
		return window;
	}

	/**
	 * 检查该节点是否允许被打开,返回true表示可以打开,返回false表示不允许打开
	 * 
	 * @param retrVO
	 * @return
	 */
	private boolean canOpen(OpenParam openParam) {
		boolean canOpen = true;
		try {
			fetchOpenNodeRCResponse(openParam);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);;
			return false;
		}
		//modified by jizhg 企业报表权限跳过
		if(this.frVO.getFuncode().startsWith("18")||this.frVO.getFuncode().startsWith("E9")){
			return true;
		}
		// 检查产品授权数：
		if (NCEnv.isControlLicense() && !FuncWindowType.FORCEMODALDLG.equals(openParam.funcWindowType)) {
			/**
			 * 写回0,表示可以打开 写回1,表示产品使用达到最大授权数
			 */
			int retr = (Integer) response.getAttachedProp("checkLicenseResult");
			if (retr == 1) {
				MessageDialog.showErrorDlg(WorkbenchEnvironment.getInstance().getWorkbench(), NCLangRes.getInstance()
						.getStrByID("smcomm", "UPP1005-000019")/* 错误 */, NCLangRes.getInstance().getStrByID("smcomm",
						"UPP1005-000085")/*
										 * 该产品的用户数已达到产品授权数！
										 */);
				canOpen = false;
			}
		}
		return canOpen;
	}

	private void fetchOpenNodeRCResponse(OpenParam openParam) throws Exception {
		if (response == null) {
			request = getOpenNodeRCRequest(openParam);
			try {
				IOpenNodeRCService service = NCLocator.getInstance().lookup(IOpenNodeRCService.class);
				response = service.unitedOpenNodeRC(request);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(WorkbenchEnvironment.getInstance().getWorkbench(), NCLangRes.getInstance()
						.getStrByID("smcomm", "UPP1005-000019")/* 错误 */, NCLangRes.getInstance().getStrByID("smcomm",
						"UPP1005-000082")/*
										 * 在打开节点调用UnitRCBO_Client.
										 * openMainFrame方法时发生错误 ！
										 */);
				throw e;
			}
		}

	}

	private OperateLogVO getFuncNodeOperateLogVO() {
		WorkbenchEnvironment env = WorkbenchEnvironment.getInstance();
		long time = System.currentTimeMillis();
		OperateLogVO logVO = new OperateLogVO();
		logVO.setType(OperateLogVO.ENTERNODE);
		logVO.setFunccode(frVO.getFuncode());
		logVO.setFuncname(frVO.getFun_name());
		logVO.setPk_funcnode(frVO.getPrimaryKey());
		logVO.setIp(env.getSession().getClientHostIP());
		logVO.setLogdate(new UFDate(time));
		logVO.setLogtime(new UFTime(time));
		if (env.getGroupVO() != null) {
			logVO.setPk_group(env.getGroupVO().getPrimaryKey());
		}
		logVO.setPk_user(env.getLoginUser().getPrimaryKey());
		logVO.setUser_name(env.getLoginUser().getUser_name());
		logVO.setUsertype(env.getLoginUser().getUser_type());
		return logVO;
	}

	private AttachedProps getOpenNodeRCRequest(OpenParam openParam) {
		if (request == null) {
			WorkbenchEnvironment env = WorkbenchEnvironment.getInstance();
			String userId = env.getLoginUser().getPrimaryKey();
			String userCode = env.getLoginUser().getUser_code();
			String userName = env.getLoginUser().getUser_name();
			NCSession session = env.getSession();
			GroupVO groupVO = env.getGroupVO();
			String dsName = "";
			BusiCenterVO bcVO = env.getLoginBusiCenter();
			if (bcVO != null) {
				dsName = bcVO.getDataSourceName();
			}
			request = new AttachedProps();
			request.putAttachProp("funcWindowType", openParam.funcWindowType.getValue());
			request.putAttachProp(frVO);
			request.putAttachProp("dsName", dsName);
			request.putAttachProp("userID", userId);
			request.putAttachProp("userCode", userCode);
			request.putAttachProp("userName", userName);
			request.putAttachProp("sessionID", session.getSessionID());
			request.putAttachProp("pkGroup", groupVO == null ? "" : groupVO.getPk_group());
			request.putAttachProp(env.getUserType());
			request.putAttachProp("busiActiveCodeArray", openParam.busiActiveCodes);
			request.putAttachProp(getFuncNodeOperateLogVO());
			request.putAttachProp("busiDate", env.getBusiDate());
			// //////////////
			IFunctionPermProfile profile = FunctionPermProfileManager.getInstance().getProfile(
					env.getLoginUser().getUser_code());
			boolean isFuncSubInfoavailable = profile == null ? false : profile
					.isFuncSubInfoavailable(frVO.getFuncode(), openParam.busiActiveCodes);
			request.putAttachProp("isFuncSubInfoavailable", isFuncSubInfoavailable);
		}
		return request;
	}
	public static boolean hasOpenning(){
		return openningCount > 0;
	}
}
