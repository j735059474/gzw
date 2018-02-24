package nc.scap.pub.qry_orgs_listwin;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.vo.bd.defdoc.DefdocVO;
import uap.web.bd.pub.AppUtil;
public class QryorgstypeViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  public void onDataLoad_qryorgstype_ds(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    ds.setLastCondition(" pk_defdoclist = (select b.pk_defdoclist from bd_defdoclist b where b.code = 'scapco_0002') ");
	  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
    String pk_type = (String)ds.getValue(ds.nameToIndex(DefdocVO.PK_DEFDOC));
    String type_name = (String)ds.getValue(ds.nameToIndex(DefdocVO.NAME));
    AppUtil.addAppAttr("pk_type", pk_type);
    AppUtil.addAppAttr("type_name", type_name);
    AppUtil.addAppAttr("fresh_type", "1");
    
    CmdInvoker.invoke(new UifPlugoutCmd(AppLifeCycleContext.current().getViewContext().getView().getId(), "afterSelectedPlugout"));
  }
}
