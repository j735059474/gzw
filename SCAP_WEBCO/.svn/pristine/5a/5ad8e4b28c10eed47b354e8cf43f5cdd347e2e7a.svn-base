package nc.scap.dsm.material_listwin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.util.CpbUtil;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.scap.jjpub.util.JjUtil;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.scapjj.pub.IConst4scapjj;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.DataLoadEvent;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Dataset;
import nc.vo.scapjj.dsm.Datetype_HVO;

public class DatatypeTreeViewController {
	private static final long serialVersionUID = 1L;
	private static final long ID = 5L;
	private static final String PLUGOUT_ID = "selectRowAfterPlugout";

	public void onDataLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		String dataTypeCode = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("data_type");
		String[] dyPks = JjUtil.getDataTypePksByUseridAndNodeTypeOrDataType(dataTypeCode);//获取当前登陆用户可以看到的本级资料类型pk数组
		String fatherAndSameLevelDataTypeIds = JjUtil.getAllFatherAndSameLevelDataTypeIds(dyPks);//根据 资料类型主键数组 通过递归的方式 获取该资料类型主键数组里所有资料类型主键及其所有上级资料类型的pk
		ds.setLastCondition(" pk_datetype_h in ("+ fatherAndSameLevelDataTypeIds +")");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
		CmdInvoker.invoke(new UifPlugoutCmd(AppLifeCycleContext.current()
				.getViewContext().getView().getId(), PLUGOUT_ID));
	}
	
	

	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		// Dataset ds = datasetEvent.getSource();
		// CmdInvoker.invoke(new
		// UifPlugoutCmd(AppLifeCycleContext.current().getViewContext().getView().getId(),PLUGOUT_ID));
	}
	
	public void onTreeNodeClick(TreeNodeEvent treeNodeEvent) {
		TreeViewComp trc = treeNodeEvent.getSource();
		String dsId = trc.getDataset();
		Dataset ds = AppUtil.getCntViewCtx().getView().getViewModels()
				.getDataset(dsId);
		String pk_datetype_h = (String) ds.getValue("pk_datetype_h");
		if (pk_datetype_h == null)
			AppUtil.addAppAttr("pk_datetype_h", IGlobalConstants.ORG_TREE_ROOT);// 组织数根节点显示值
		else
			AppUtil.addAppAttr("pk_datetype_h", pk_datetype_h);

		CmdInvoker.invoke(new UifPlugoutCmd(AppLifeCycleContext.current()
				.getViewContext().getView().getId(), PLUGOUT_ID));
	}
}
