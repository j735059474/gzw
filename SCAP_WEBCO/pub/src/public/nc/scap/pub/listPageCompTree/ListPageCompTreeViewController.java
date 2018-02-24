package nc.scap.pub.listPageCompTree;
import java.util.HashMap;

import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.portal.log.PortalLogger;
import nc.vo.pub.SuperVO;
import uap.web.bd.pub.AppUtil;
public class ListPageCompTreeViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  public void onDataLoad_cp_orgs_ds(  DataLoadEvent dataLoadEvent){
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
					sql = "SELECT * FROM CP_ORGS where def13 is not null and "+CpOrgUtil.getFilterOrgWhereSql();
				else
					sql = "SELECT * FROM CP_ORGS where def13 is not null and  dr=0  and orglevel ='2' START WITH  PK_ORG = '"
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
  }
  public void onTreeNodeClick(  TreeNodeEvent treeNodeEvent){
	  Dataset ds = CpWinUtil.getDataset("org_ds");
	    Row[] rows= ds.getAllSelectedRows();
	    String str=null;
	    for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			String pk_org = (String) row.getValue(ds.nameToIndex("pk_org"));
			if(str==null){
				str="'"+pk_org+"'";
			}else{
				str+=",'"+pk_org+"'";	
			}
		}
	    AppUtil.addAppAttr("pk_org", str);
  }
  public void onAfterRowSelect_orgds(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
    Row[] rows= ds.getAllSelectedRows();
    String str=null;
    for (int i = 0; i < rows.length; i++) {
		Row row = rows[i];
		String pk_org = (String) row.getValue(ds.nameToIndex("pk_org"));
		if(str==null){
			str="'"+pk_org+"'";
		}else{
			str+=",'"+pk_org+"'";	
		}
	}
    AppUtil.addAppAttr("pk_org", str);
  }
  public void afterSelNodeChange(  TreeNodeEvent treeNodeEvent){
    PortalLogger.console("aaaa");
  }
  public void SelectSubCompChanged(  TextEvent textEvent){
	  CheckBoxComp comp = (CheckBoxComp)textEvent.getSource(); 
	  boolean checked = comp.isChecked();
	  TreeViewComp orgTree = (TreeViewComp)comp.getView().getViewComponents().getComponent("org_tree");
	  if (checked) {
		  orgTree.setCheckBoxModel(1);
	  } else {
		  orgTree.setCheckBoxModel(0);
	  }
  }
  public void SelectAllCompChanged(  TextEvent textEvent){
	  CheckBoxComp comp = (CheckBoxComp)textEvent.getSource(); 
	  boolean checked = comp.isChecked();
	  Dataset orgTree = (Dataset)comp.getView().getViewModels().getDataset("org_ds");
	  if (checked) {
			RowSet[] rowSets = orgTree.getRowSets();
			for (int i = 0; i < rowSets.length; i++) {
				RowSet rowSet = rowSets[i];
				RowData[] rowDatas = rowSet.getRowDatas();
				for (int j = 0; j < rowDatas.length; j++) {
					RowData rowData = rowDatas[j];
					Integer[] selectIndices = new Integer[rowData.getRows().length];
					for (int q = 0; q < selectIndices.length; q++) {
						selectIndices[q] = q;
					}
					rowData.setRowSelectIndices(selectIndices);
				}
			}
	  } else {
		  orgTree.setAllRowUnSelect();
	  }
  }
  public void onAfterRowUnSelect(  DatasetEvent datasetEvent){
	  Dataset ds = datasetEvent.getSource();
	    Row[] rows= ds.getAllSelectedRows();
	    String str=null;
	    for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			String pk_org = (String) row.getValue(ds.nameToIndex("pk_org"));
			if(str==null){
				str="'"+pk_org+"'";
			}else{
				str+=",'"+pk_org+"'";	
			}
		}
	    AppUtil.addAppAttr("pk_org", str);
	    HashMap<String, Object> paramMap = new HashMap<String, Object>();
		UifPlugoutCmd plugcmd = new UifPlugoutCmd("listPageCompTree",
				"orgtreeout", paramMap);
		plugcmd.execute();
  }

}
