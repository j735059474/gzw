package nc.scap.pub.workreport;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.scap.pub.itf.IGlobalConstants;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.scap.pub.util.CpOrgUtil;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.scap.sysinit.util.ScapSysinitUtil;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Dataset;
public class WorkReportOrgTreeViewController {
  private static final String OUT_VIEW="workreport_orgtree";
  private static final String TREE_PLUGOUT="orgtreeout";
  public void onTreeNodeClick(  TreeNodeEvent treeNodeEvent){
    TreeViewComp trc = treeNodeEvent.getSource();
                String dsId = trc.getDataset();
                Dataset ds = AppUtil.getCntViewCtx().getView().getViewModels()
                                .getDataset(dsId);
                String pk_org = (String) ds.getValue("pk_org");
                if(pk_org==null)
                        AppUtil.addAppAttr("pk_org", IGlobalConstants.ORG_TREE_ROOT);//组织数根节点显示值
                else
                        AppUtil.addAppAttr("pk_org", pk_org);
                Map<String,Object> paramMap = new HashMap<String,Object>();
//              paramMap.put("pk_org", pk_org);
                CmdInvoker.invoke(new UifPlugoutCmd(OUT_VIEW, TREE_PLUGOUT, paramMap));
  }
  public void onDataLoad_orgds(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
                /*
                 * 组织树调出数据加系统参数控制，如果系统参数设置组织过滤则根据当前登录用户对应角色关联组织调出数据
                 * 
                 */
                String wheresql="";//
                boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
                if(isFilterOrg)
                        wheresql = CpOrgUtil.getFilterOrgWhereSql();
                else{
//                      wheresql = " orglevel ='2' and pk_orglevel2='~'";
//                      wheresql = " (orglevel ='2' and pk_orglevel2='~') or (orglevel ='2' and pk_orglevel2 is null) or (orglevel ='2' and pk_orglevel2 = '')";
//                      wheresql = " 1=1 ";
                        wheresql = " (orglevel ='2' and enablestate='2' and def13 is not null) ";
                }
                ds.setOrderByPart(" order by code asc");
                ds.setLastCondition(wheresql);
                CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}
