package nc.scap.pubview.orgtree.ctrl;

import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.pubview.mode.ModeOrgFilter;
import nc.uap.cpb.org.pubview.mode.ModeOrgHelper;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.SuperVO;
import uap.web.bd.pub.AppUtil;

public class ScapPtOrgTreeController {
	private static final long serialVersionUID = 1L;
	private static final long ID = 5L;

	public void onDataLoad_orgds(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		ds.setOrderByPart(" order by code asc");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {

			protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
					String wherePart, String orderPart)
					throws LfwBusinessException {

				CpOrgVO[] vos = null;
				String sql = "";
				boolean isFilterOrg = CpOrgUtil.isCompanyOrg(CntUtil
						.getCntOrgPk());
				if (isFilterOrg)
					sql = "SELECT * FROM CP_ORGS where "
							+ getFilterOrgWhereSql();
				else
					sql = "SELECT * FROM CP_ORGS where dr=0  and orglevel ='2' and pk_orglevel2='~' START WITH  PK_ORG = '"
							+ CntUtil.getCntOrgPk()
							+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";
				if (orderPart != null) {
					sql += orderPart;
				}
				try {
					vos = CRUDHelper.getCRUDService().queryVOs(sql,
							CpOrgVO.class, null, null);
					pinfo.setPageSize(vos.length);
					if (ScapSysinitUtil.isShowShortName()) {
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
	}

	public void onTreeNodeClick(TreeNodeEvent treeNodeEvent) {
		TreeViewComp trc = treeNodeEvent.getSource();
		String dsId = trc.getDataset();
		Dataset ds = AppUtil.getCntViewCtx().getView().getViewModels()
				.getDataset(dsId);
		String pk_org = (String) ds.getValue("pk_org");
		AppUtil.addAppAttr("pk_org", pk_org);
	}

	public void onAfterRowSelect_orgds(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}

	/**
	 * 获取过滤组织sql语句
	 * 
	 * @author yhl 2014-4-15
	 */
	public String getFilterOrgWhereSql() {
		String curuserpk = LfwRuntimeEnvironment.getLfwSessionBean()
				.getPk_user();
		String curGrouppk = LfwRuntimeEnvironment.getLfwSessionBean()
				.getPk_unit();
		ModeOrgFilter filter = (ModeOrgFilter) AppLifeCycleContext.current()
				.getApplicationContext()
				.getAppAttribute(ModeOrgHelper.ModeFilter_AttID);
		if (filter == null) {
			filter = new ModeOrgFilter();
		}
		filter.setIsneedGroup(false);// 不显示集团
		// String
		// wheresql=" dr=0 START WITH  pk_org = '"+AppUtil.getPk_org()+"' CONNECT BY PRIOR pk_org=pk_fatherorg";//默认条件为当前登录用户所在组织及下级组织
		String wheresql = "";// 默认条件为当前登录用户所在组织及下级组织
		try {
			wheresql = ModeOrgHelper.buildOrgSql(curuserpk, curGrouppk, filter);
		} catch (CpbBusinessException e) {
			e.printStackTrace();
		} catch (LfwBusinessException e) {
			e.printStackTrace();
		}
		return wheresql;
	}
}
