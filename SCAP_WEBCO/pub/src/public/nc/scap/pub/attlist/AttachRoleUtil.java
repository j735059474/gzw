package nc.scap.pub.attlist;
import java.util.ArrayList;
import java.util.List;

import nc.scap.pub.attlist.comp.IAttachRoleCompConstant;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.AttachGroupVO;
import nc.scap.pub.vos.AttachRoleVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.portal.log.ScapLogger;
import uap.web.bd.pub.AppUtil;

public class AttachRoleUtil {
	
	public static interface AttachGroupFilter {
		
		public boolean filter(AttachGroupVO vo);
	}
	
	public static void refreshWidget(boolean needRoles, AttachGroupFilter filter) {
		// 根据当前节点主键、单据主键获取AttachGroupVO
		String nodecode = (String) AppUtil.getAppAttr("nodecode");
		if (nodecode == null) {
			nodecode = (String) LfwRuntimeEnvironment.getWebContext().getAttribute("nodecode");
			
		}
		CpAppsNodeVO[] nodes = (CpAppsNodeVO[]) ScapDAO.retrieveByCondition(CpAppsNodeVO.class, "id='" + nodecode +"'");
		String pk_node = "~";
		if (nodes != null && nodes.length == 1) {
			pk_node = nodes[0].getPk_appsnode();
		}
		String pk_billitem = AttachRoleUtil.getPkBillItem();
		
		if (pk_billitem == null) {
			pk_billitem = generatePK();
			AttachRoleUtil.setPkBillItem(pk_billitem);
		}
		
		AttachGroupVO[] attachGroups = AttachRoleDao.retrieveAttachGroupByNodePK(pk_node);
		attachGroups = attachGroups == null ? new AttachGroupVO[0] : attachGroups;
		
		// 获取当前view, tabComp, 所有tabCompItem
		ViewContext viewCtx = AppUtil.getCntWindowCtx().getViewContext("attachrole");
		if (viewCtx!=null){ 
			UITabComp tabComp = (UITabComp) viewCtx.getUIMeta().findChildById("uiTabComp");
			List<UILayoutPanel> tabItemList = tabComp.getPanelList();

			// 根据查询到的AttachGroupVO显示tabCompItem
			int firstIdx = -1;
			for (int i = 0; i < tabItemList.size(); i++) {
				UITabItem tabItem = (UITabItem) tabItemList.get(i);
				if (i < attachGroups.length) {
					AttachGroupVO attachGroup = attachGroups[i];
					tabItem.setText(attachGroup.getName());
					
					boolean isValid = filter.filter(attachGroup);
					tabItem.setVisible(isValid);
					if (isValid) {
						firstIdx = i;
					}
					if (needRoles) loadAttachRoles(tabItem, attachGroup);
				} else {
					tabItem.setVisible(false);
					continue;
				}
			}
			
			if (firstIdx != -1) {
				tabComp.setCurrentItem(Integer.toString(firstIdx));
			}
		}
		
	}
	
	public static void refreshWidget(boolean needRoles) {
		refreshWidget(needRoles, new AttachGroupFilter() {
			@Override
			public boolean filter(AttachGroupVO vo) {
				return true;
			}
		});
	}

	/**
	 * 将AttachGroupVO关联的AttachRoleVO加载到UITabItem的GridComp中
	 */
	public static void loadAttachRoles(UITabItem tabItem, AttachGroupVO attachGroup) {
		
		LfwView view = AppUtil.getCntWindow().getView(tabItem.getWidgetId());
		Dataset ds = view.getViewModels().getDataset(tabItem.getElement().getId() + "_ds");
		AttachRoleVO[] attachRoles = AttachRoleDao.retrieveAttachRoleByGroup(attachGroup.getPk_attach_group());

		ds.clear();
		for (AttachRoleVO vo : attachRoles) {
			Row row =  ds.getEmptyRow();
			for (String field : vo.getAttributeNames()) {
				row.setValue(ds.nameToIndex(field), vo.getAttributeValue(field));
			}
			String pk_billitem = AttachRoleUtil.getPkBillItem();
			// 查询当前上传附件数量赋值到FIELD_CURRENT_COUNT
			
			
			LfwFileVO[] fileVOs = AttachRoleUtil.getFiles(pk_billitem, vo);
			row.setValue(ds.nameToIndex(IAttachRoleCompConstant.FIELD_CURRENT_COUNT), fileVOs == null ? 0 : fileVOs.length);
			ds.addRow(row);
		}
	}

	/**
	 * 获取当前单据主键，如果当前单据没有调用setPkBillItem()方法，会返回一个随机产生的主键
	 */
	public static String getPkBillItem() {
		return (String) AppUtil.getAppAttr(IAttachRoleCompConstant.ATTR_TEMP_BILL_ITEM);
	}
	
	/**
	 * 将pk_billitem值设为AttachRoleComp能获取的当前单据主键
	 */
	public static void setPkBillItem(String pk_billitem) {
		AppUtil.addAppAttr(IAttachRoleCompConstant.ATTR_TEMP_BILL_ITEM, pk_billitem);
	}
	
	/**
	 * 所有已上传附件
	 */
	public static LfwFileVO[] getFiles(String pk_billitem) {
		try {
			return FileManager.getSystemFileManager(LfwFileConstants.SYSID_BAFILE).getAttachFileByItemID(pk_billitem);
		} catch (LfwBusinessException e) {
			ScapLogger.error("获取当前节点所有已上传附件出错 " + e.getMessage(), e);
		}
		return new LfwFileVO[0];
	}
	
	/**
	 * 某一附件规则中上传的附件
	 */
	public static LfwFileVO[] getFiles(String pk_billitem, AttachRoleVO attachrole) {
		try {
			return FileManager.getSystemFileManager(LfwFileConstants.SYSID_BAFILE).getAttachFileByItemID(pk_billitem, attachrole.getPk_attach_role());
		} catch (LfwBusinessException e) {
			ScapLogger.error("获取当前节点在某一附件规则中上传的附件出错 " + e.getMessage(), e);
		}
		return new LfwFileVO[0];
	}
	
	/**
	 * 某一类附件规则中上传的附件
	 */
	public static List<LfwFileVO> getFiles(String pk_billitem, AttachGroupVO attachgroup) {
		List<LfwFileVO> result = new ArrayList<LfwFileVO>();
		AttachRoleVO[] attachroles = AttachRoleDao.retrieveAttachRoleByGroup(attachgroup.getPk_attach_group());
		if (attachroles != null) {
			for (AttachRoleVO vo : attachroles) {
				LfwFileVO[] files = getFiles(pk_billitem, vo);
				if (files != null) {
					for (LfwFileVO fileVO : files) {
						result.add(fileVO);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 校验上传文件数量
	 */
	public static void validate() throws LfwRuntimeException {
		for (Dataset ds : AppUtil.getCntWindow().getView("attachrole").getViewModels().getDatasets()) {
			for (Row row : ds.getAllRow()) {
				String title = row.getString(ds.nameToIndex(IAttachRoleCompConstant.FIELD_NAME));
				int max = row.getInt(ds.nameToIndex(IAttachRoleCompConstant.FIELD_MAX_COUNT));
				int min = row.getInt(ds.nameToIndex(IAttachRoleCompConstant.FIELD_MIN_COUNT));
				int cnt = row.getInt(ds.nameToIndex(IAttachRoleCompConstant.FIELD_CURRENT_COUNT));
				if (cnt > max) {
					throw new LfwRuntimeException("附件：" + title + "上传文件数量超过最大值");
				} else if (cnt < min) {
					throw new LfwRuntimeException("附件：" + title + "上传文件数量不足");
				}
			}
		}
	}
	
	/**
	 * 设置附件列表权限
	 */
	public static void setFileMgrStatus(FileMgrConstant... constants) {
		if (constants != null) {
			int state = 0;
			String paramState = (String) AppUtil.getAppAttr(IAttachRoleCompConstant.FILE_MGR_CONSTANTS);
			try {
				state = Integer.parseInt(paramState);
			} catch(Exception e) {}
			for (FileMgrConstant constant : constants) {
				state = state | constant.value;
			}
			AppUtil.addAppAttr(IAttachRoleCompConstant.FILE_MGR_CONSTANTS, Integer.toString(state));
		}
	}
	
	/**
	 * 检查当前附件列表是否包含某一权限
	 */
	public static boolean checkFileMgrStatus(FileMgrConstant constant) {
		int state = 0;
		String paramState = (String) AppUtil.getAppAttr(IAttachRoleCompConstant.FILE_MGR_CONSTANTS);
		try {
			state = Integer.parseInt(paramState);
		} catch(Exception e) {
			return true;
		}
		return (state & constant.value) == constant.value;
	}
	
	private static String generatePK() {
		String datasource = LfwRuntimeEnvironment.getDatasource();
		String primaryKey = PtBaseDAO.generatePK(datasource);
		return primaryKey;
	}
	
	public static enum FileMgrConstant{
		
		/**
		 * 可查看状态
		 */
		VIEW(1),
		/**
		 * 可编辑
		 */
		EDIT(2),
		/**
		 * 可删除
		 */
		DELETE(4),
		/**
		 * 可下载
		 */
		DOWNLOAD(8),
		/**
		 * 可上传
		 */
		UPLOAD(16),
		/**
		 * 可以使用高拍仪
		 */
		SCAN(32);
		
		int value;
		FileMgrConstant(int val) {
			value = val;
		}
	}
}
