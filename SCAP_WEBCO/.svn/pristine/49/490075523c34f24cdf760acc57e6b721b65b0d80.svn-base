package nc.uap.cpb.org.pubview;

import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpRoleGroupVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.vo.pub.SuperVO;

/** 
 */
public class RelateRoleController implements IController {
    private static final long serialVersionUID = 1L;
    public static final String PUBLIC_VIEW_ROLE = "role";
    public static final String ORG_TEXT = "org_text";

    private WindowContext getCurrentWinCtx() {
        return AppLifeCycleContext.current().getApplicationContext()
                .getCurrentWindowContext();
    }

    public void onCancelClick(MouseEvent<?> mouseEvent) {
        AppLifeCycleContext.current().getApplicationContext()
                .getCurrentWindowContext().closeView(PUBLIC_VIEW_ROLE);
    }

    public void onBeforeShow(DialogEvent dialogEvent) {
        LfwView role = AppLifeCycleContext.current().getWindowContext()
                .getViewContext("role").getView();
        ReferenceComp org_text = (ReferenceComp) role.getViewComponents()
                .getComponent("org_text");
        String pk_group = LfwRuntimeEnvironment.getLfwSessionBean()
                .getPk_unit();
        if (pk_group == null)
            return;
        String groupName = null;
        try {
            groupName = ((CpOrgVO) (new PtBaseDAO()).retrieveByPK(
                    CpOrgVO.class, pk_group)).getName();
        } catch (Exception e) {
        }
        org_text.setValue(pk_group);
        org_text.setShowValue(groupName);
    }

    public void onDataLoad_org(DataLoadEvent dataLoadEvent) {
        Dataset ds = dataLoadEvent.getSource();
        CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
            protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
                    String wherePart) throws LfwBusinessException {
                SuperVO[] vos = CpbServiceFacility.getCpOrgRefefenceQry()
                        .getReferenceCpOrgs();
                return vos;
            }
        });
    }

    public void onAfterRowSelect_rolegroup(DatasetEvent datasetEvent) {
        Dataset ds = datasetEvent.getSource();
        String pk_rolegroup = ds.getSelectedRow().getString(
                ds.nameToIndex("pk_rolegroup"));
        Dataset ds_role = AppLifeCycleContext.current().getViewContext()
                .getView().getViewModels().getDataset("ds_role");
        RowSet rowSet = ds_role.getRowSet(pk_rolegroup);
        if (rowSet == null) {
            CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {

                // 重写此方法，避免选中第一行
                @Override
                protected void postProcessChildRowSelect(Dataset ds) {
                    return;
                }

            });
        } else {
            ds_role.setCurrentKey(pk_rolegroup);
        }

    }

    public void onOrgvalueChanged(TextEvent textEvent) {
        LfwView role = AppLifeCycleContext.current().getWindowContext()
                .getViewContext(PUBLIC_VIEW_ROLE).getView();
        Dataset ds = role.getViewModels().getDataset("ds_rolegroup");
        ReferenceComp org_text = (ReferenceComp) role.getViewComponents()
                .getComponent("org_text");
        String pk_org = org_text.getValue();
        CpRoleGroupVO[] vos = null;
        try {
            vos = CpbServiceFacility.getCpRoleGroupQry().getRoleGroupByPkorgs(
                    new String[] { pk_org });
        } catch (CpbBusinessException e) {
            CpLogger.error(e.getMessage(), e);
        }
        new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
        if (ds.getCurrentRowCount() > 0) {
            ds.setSelectedIndex(0);
        }
    }

    public void onRoleDsLoad(DataLoadEvent dataLoadEvent) {
        Dataset ds = dataLoadEvent.getSource();
        CmdInvoker.invoke(new UifDatasetAfterSelectCmd("ds_rolegroup") {
            // 重写此方法，避免选中第一行
            @Override
            protected void postProcessChildRowSelect(Dataset ds) {
                return;
            }

            protected int getPageIndex() {
                Dataset ds = AppLifeCycleContext.current().getViewContext()
                        .getView().getViewModels().getDataset("ds_role");
                return ds.getCurrentRowSet().getPaginationInfo().getPageIndex();
            }
        });
        // ds.setSelectedIndex(-1);
    }
}
