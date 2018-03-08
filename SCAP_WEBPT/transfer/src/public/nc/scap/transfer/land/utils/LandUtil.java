package nc.scap.transfer.land.utils;


import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.facility.WfmServiceFacility;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmTaskVO;

import org.apache.commons.lang.StringUtils;

public class LandUtil {
	//卡片子表按钮隐藏
	public static void setGridBtnFalse(LfwView view, String gridId, String[] btns){
		setGridBtnState(view, gridId, btns , false);
	}

	public static void setGridBtnState(LfwView view, String gridId, String[] btns, boolean state) {
		GridComp gc = (GridComp) view.getViewComponents().getComponent(gridId);
		MenubarComp mbc_c = gc.getMenuBar();
		if(mbc_c==null)return;
		for(String btn : btns){
			if(mbc_c.getItem(btn)!=null){
				mbc_c.getItem(btn).setVisible(state);
			}
		}
	}
	/********************************************************************************/
	//列表按钮隐藏
	public static void setBtnFalse(LfwView view , String menuBar , String[] btns){
		setBtnState(view , menuBar , btns , false);
	}

	public static void setBtnState(LfwView view, String menuBar,
			String[] btns, boolean state) {
		if(view!=null){
			MenubarComp mbc = view.getViewMenus().getMenuBar(menuBar);
			if(mbc==null)return;
			for(String btn : btns){
				if(mbc.getItem(btn)!=null){
					mbc.getItem(btn).setVisible(state);
				}
			}
		}
	}
	/********************************************************************************/
	
	//卡片字段隐藏
	public static void setItemsFalse(FormComp fc, String[] items){
		setItemsState(fc , items , false);
	}

	public static void setItemsState(FormComp fc, String[] items, boolean state) {
		if(fc==null)return;
		for(String item : items){
			if(fc.getElementById(item)!=null){
				fc.getElementById(item).setVisible(state);
			}
		}
	}
	//卡片字段置灰
	public static void setItemsEnFalse(FormComp fc, String[] items){
		setItemsEnState(fc , items , false);
	}
	public static void setItemsEnTrue(FormComp fc, String[] items){
		setItemsEnState(fc , items , true);
	}

	public static void setItemsEnState(FormComp fc, String[] items, boolean state) {
		if(fc==null)return;
		for(String item : items){
			if(fc.getElementById(item)!=null){
				fc.getElementById(item).setEnabled(state);
			}
		}
	}
	/********************************************************************************/
	public static void ctrlMainBtn(String gcId ,String itemId,boolean flag){
		MenubarComp mbc_c = AppLifeCycleContext.current().getViewContext()
				.getView().getViewMenus().getMenuBar(gcId);
		mbc_c.getItem(itemId).setEnabled(flag);	
	}
	
	public static void setBtnEnTrue(LfwView view , String menuBar , String[] btns){
		setBtnEnState(view , menuBar , btns , true);
	}
	/********************************************************************************/
	//列表按钮置灰
	public static void setBtnEnFalse(LfwView view , String menuBar , String[] btns){
		setBtnEnState(view , menuBar , btns , false);
	}

	public static void setBtnEnState(LfwView view, String menuBar,
			String[] btns, boolean state) {
		MenubarComp mbc = view.getViewMenus().getMenuBar(menuBar);
		if(mbc==null)return;
		for(String btn : btns){
			if(mbc.getItem(btn)!=null){
				mbc.getItem(btn).setEnabled(state);
			}
		}
	}
	/********************************************************************************/
	//卡片片段隐藏
	public static void removeGridPanle(UIMeta um,String[] flowvIds,String[] panelIds) {
		for (int i=0; i<flowvIds.length;i++){
			removeGridPanle(um, flowvIds[i], panelIds[i]);
		}
	}
	
	public static void removeGridPanle(UIMeta um,String flowvId,String panelId) {
		UIFlowvPanel uiFP = (UIFlowvPanel) um.findChildById(flowvId);//"scapav_state_attchfile_flowvpane2"
		UIPanel uiTI = (UIPanel) uiFP.findChildById(panelId);//"scapav_state_expert_panelPanel"
		uiFP.removeElement(uiTI);
	}
	/**
	  * 判断字符串是否是整数
	  */
	public static boolean isInteger(String value) {
		boolean flag = true ;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 指定用户的指定任务状态所有单据主键，返回例如 ('主键','主键','主键')
	 * 
	 * @param pk_user
	 *            默认当前用户
	 * @param state
	 *            空时查询指定用户的所有任务
	 * @param pk_frmins单据主键名称
	 *            ，即主字段名称
	 * @return
	 */
	public static String getAllWfmTaskByUserPK(String pk_user, String state,
			String PKFieldName, String flowTypePk) {
		WfmFlwTypeVO flowTypeVo = null;
		if (StringUtils.isNotBlank(flowTypePk)) {
			flowTypeVo = WfmServiceFacility.getFlwTypeQry().getFlwTypeVoByPk(
					flowTypePk);
		}

		if (StringUtils.isBlank(pk_user)) {
			pk_user = CntUtil.getCntUserPk();
		}
		String runWfmTask = " 1=1 ";
		BaseDAO baseDao = ScapDAO.getBaseDAO();
		String sql = " 1=1 ";
		if (flowTypeVo != null) {
			sql += "  and flowtypename='" + flowTypeVo.getTypename() + "'";
		}

		if (StringUtils.isBlank(state))
			sql += " and isnull(dr,0)=0  and pk_owner ='" + pk_user
					+ "' order by ts";
		else {
			sql += " and  isnull(dr,0)=0 and state ='" + state
					+ "' and pk_owner ='" + pk_user + "' order by ts";
		}

		List<WfmTaskVO> wts = null;
		try {
			wts = (List<WfmTaskVO>) baseDao.retrieveByClause(WfmTaskVO.class,
					sql);
			if ((wts != null) && (wts.size() != 0)) {
				for (WfmTaskVO wfmTaskVO : wts) {
					if (runWfmTask.equals(" 1=1 "))
						runWfmTask = "  and " + PKFieldName + " in ( '"
								+ wfmTaskVO.getPk_frmins() + "'";
					else
						runWfmTask = runWfmTask + ",'"
								+ wfmTaskVO.getPk_frmins() + "'";
				}
				runWfmTask += " )";
			}
		} catch (Exception e) {
			ScapLogger
					.error("==============ScapPM Error, PubMethod.getAllWfmTaskByUserPK :"
							+ e.getMessage());
		}
		if (runWfmTask.equals(" 1=1 ")) {
			return " and 1=2";
		}
		return runWfmTask;
	}
	
	
	
}
