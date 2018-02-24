package nc.scap.pub.workreport.model;

import java.util.List;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.def.util.ScapDefUtil;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.WorkReportVO;
import nc.scap.pub.workreport.util.MenubarUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scap.work_report_type.Work_report_type;
import uap.web.bd.pub.AppUtil;

public class WorkreportPageModel extends PageModel {

    @Override
    protected void initPageMetaStruct() {
        super.initPageMetaStruct();

        String id = getPageMeta().getId();

        LfwView main = getPageMeta().getView("main");

        if ((id != null) && (id.indexOf("listwin") != -1)) {
            String node_type = (String) LfwRuntimeEnvironment.getWebContext()
                    .getOriginalParameter(IGlobalConstants.NODE_TYPE);
            if (node_type == null || "".equals(node_type)) {
                throw new LfwRuntimeException("功能节点未设置\""
                        + IGlobalConstants.NODE_TYPE + "\"参数!");
            }

             String business_type = (String)
             LfwRuntimeEnvironment.getWebContext()
             .getOriginalParameter(ScapCoConstants.BUSINESS_TYPE);
             String report_type = (String) LfwRuntimeEnvironment.getWebContext()
 					.getOriginalParameter("report_type");

//            String business_type = "01";

            String pk_defdoc = null;
            if (business_type != null) {
                String defCode = "scapco_0003";
                DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
                if (vos != null && vos.length != 0) {
                    /*
                     * vos[i].getName(); vos[i].getPk_defdoc();
                     * vos[i].getCode();
                     */
                    String itemValue = null;
                    String code = null;
                    for (int i = 0; i < vos.length; i++) {
                        code = vos[i].getCode();
                        if (code != null && code.equals(business_type)) {
                            // 取得定义值
                            pk_defdoc = vos[i].getPk_defdoc();
                        }
                    }
                }
            }

            if (business_type == null || "".equals(business_type)) {
                throw new LfwRuntimeException("功能节点未设置 \""
                        + ScapCoConstants.BUSINESS_TYPE + "\"参数!");
            } else if (business_type != null && pk_defdoc == null) {
                throw new LfwRuntimeException("功能节点设置的 \""
                        + ScapCoConstants.BUSINESS_TYPE + "\"参数的值未定义!");
            }
            
            String condition = "select " + Work_report_type.PK_WORK_REPORT_TYPE
					+ " from scapco_work_reportType where "
					+ Work_report_type.YE_TYPE + " = '" + pk_defdoc + "'";
			String pkWorkReportType = (String) ScapDAO.executeQuery(condition,
					new ColumnProcessor());
			if (pkWorkReportType == null || pkWorkReportType.length() == 0) {
				throw new LfwRuntimeException(
						"业务类型对应的报告类型不存在，请使用[通知管理]-[报告类型管理]功能新建报告类型!");
			}

			if (report_type == null || report_type.length() == 0) {

			} else {
				// 检索出business_type及 report_type参数 对应的第一个report_type的pk
				condition += " and " + Work_report_type.REPORT_CODE + " = '"
						+ report_type + "'";
				pkWorkReportType = (String) ScapDAO.executeQuery(condition,
						new ColumnProcessor());
				if (pkWorkReportType == null || pkWorkReportType.length() == 0) {
					throw new LfwRuntimeException(
							"功能节点设置的 \"report_type\"参数的值未定义!");
				}
			}

            AppUtil.addAppAttr(IGlobalConstants.NODE_TYPE, node_type);
            AppUtil.addAppAttr(ScapCoConstants.BUSINESS_TYPE, pk_defdoc);
            AppUtil.addAppAttr("report_type", pkWorkReportType);

            String page_type = (String) LfwRuntimeEnvironment.getWebContext()
                    .getOriginalParameter("page_type");
            AppUtil.addAppAttr("page_type", page_type);
            UIMeta uimeta = (UIMeta) this.getUIMeta();
            String pk_org = CntUtil.getCntOrgPk();
            UIElement orgElement = (UIElement) UIElementFinder.findElementById(
                    uimeta, "g_p_2");
            UIElement orgtree = (UIElement) UIElementFinder.findElementById(
                    uimeta, "g_p_4");

            if (orgElement != null) {
                orgElement.setVisible(false);
            }
            if (orgtree != null) {
                orgtree.setVisible(false);
            }
            if (CpOrgUtil.isCompanyOrg(pk_org)) {
                if (orgElement != null) {
                    orgElement.setVisible(true);
                }
                if (orgtree != null) {
                    orgtree.setVisible(false);
                }

                // 组织自动加载参数取得
                LfwView modeorg = getPageMeta().getView("modeorg");
                AppUtil.addAppAttr(ScapCoConstants.ORG_AUTO_LOAD, modeorg
                        .getExtendAttributeValue(ScapCoConstants.ORG_AUTO_LOAD));
            } else {
                if (orgElement != null) {
                    orgElement.setVisible(false);
                }
                if (orgtree != null) {
                    orgtree.setVisible(true);
                }
            }

            // 快速查询view及Dataset取得
            LfwView simplequery = getPageMeta().getView("simplequery");
            Dataset sqds = simplequery.getViewModels().getDataset("mainds");
            UFDate now = new UFDate();
            Field yearField = sqds.getFieldSet().getField("year");
            Field monthField = sqds.getFieldSet().getField("month");
            Field monthStartField = sqds.getFieldSet().getField("month_start");
            Field monthEndField = sqds.getFieldSet().getField("month_end");
            Field stateField = sqds.getFieldSet().getField("state");

            // 快速查询检索值设定
            if (yearField != null) {
                String yearStr = String.valueOf(now.getYear());
                sqds.setValue("year", yearStr);
            }
            if (monthField != null) {
                String monthStr = String.valueOf(now.getMonth());
                sqds.setValue("month", monthStr);
            }
            if (monthStartField != null) {
                String monthStartStr = String
                        .valueOf((now.getMonth() - 2) < 0 ? 1
                                : now.getMonth() - 2);
                sqds.setValue("month_start", monthStartStr);
            }
            if (monthEndField != null) {
                String monthEndStr = String.valueOf(now.getMonth());
                sqds.setValue("month_end", monthEndStr);
            }

            if (ScapCoConstants.ADD.equals(node_type)) {
                AppUtil.addAppAttr(ScapCoConstants.NODE_TYPE, node_type);

                MenubarUtil.hideMenubar(main, "menubar", new String[] {
                        ScapCoConstants.ADD, ScapCoConstants.EDIT,
                        ScapCoConstants.DEL, ScapCoConstants.LOOK,
                        ScapCoConstants.SUBMIT, ScapCoConstants.RECYCLE });

            } else if (ScapCoConstants.APPROVE.equals(node_type)) {
                AppUtil.addAppAttr(ScapCoConstants.NODE_TYPE, node_type);

                MenubarUtil.hideMenubar(main, "menubar", new String[] {
                        ScapCoConstants.APPROVE, ScapCoConstants.RECEIVE,
                        ScapCoConstants.ROLLBACK, ScapCoConstants.REAPPROVE });

                if (stateField != null) {
                    StaticComboData cd = (StaticComboData) simplequery
                            .getViewModels().getComboData("comb_state_value");
                    cd.removeComboItem("0");
                    cd.removeComboItem("2");
                    sqds.setValue("state", "1");
                }

            } else if (ScapCoConstants.LOOK.equals(node_type)) {
                AppUtil.addAppAttr(ScapCoConstants.NODE_TYPE, node_type);
                MenubarUtil.hideMenubar(main, "menubar",
                        new String[] { ScapCoConstants.LOOK });

                // 查询页面表格改为单选状态
                GridComp gridComp = (GridComp) main.getViewComponents()
                        .getComponent("workreport_grid");
                gridComp.setMultiSelect(false);
                gridComp.setMultiSelectShow(false);

                // 快速查询检索值设定
                if (stateField != null) {
                    StaticComboData cd = (StaticComboData) simplequery
                            .getViewModels().getComboData("comb_state_value");
                    cd.removeComboItem("0");
                    cd.removeComboItem("1");
                    cd.removeComboItem("2");
                    sqds.setValue("state", "3");
                }
            }

        } else if ((id != null) && (id.indexOf("cardwin") != -1)) {

            String method_type = (String) (String) LfwRuntimeEnvironment
                    .getWebContext().getOriginalParameter(
                            IGlobalConstants.METHOD_TYPE);

            if (ScapCoConstants.ADD.equals(method_type)
                    || ScapCoConstants.EDIT.equals(method_type)) {
                MenubarUtil.hideMenubar(main, "menubar", new String[] {
                        ScapCoConstants.SAVE, ScapCoConstants.SUBMIT,
                        ScapCoConstants.BACK });
                setFEEnable(main, "workreport_form", new String[] {
                        "input_date", "input_user_user_name", "pk_org_name",
                        WorkReportVO.STATE, WorkReportVO.HANDLE_STATE,
                        WorkReportVO.SUBMIT_DATE, "business_type_name" }, false);

                HideFEdField(main, "workreport_form",
                        new String[] { WorkReportVO.APPROVER,
                                "approver_user_name",
                                WorkReportVO.APPROVE_DATE,
                                WorkReportVO.APPROVE_OPINION });

            } else if (ScapCoConstants.APPROVE.equals(method_type)
                    || ScapCoConstants.REAPPROVE.equals(method_type)) {
                MenubarUtil.hideMenubar(main, "menubar", new String[] {
                        ScapCoConstants.SAVE, ScapCoConstants.RECEIVE,
                        ScapCoConstants.ROLLBACK, ScapCoConstants.BACK });
                setFEEnable(main, "workreport_form", new String[] {
                         WorkReportVO.YEAR,
                         WorkReportVO.MONTH,
                         "business_type_name",
                         "report_type",
                         "report_type_report_type",
                         "report_type_report_title",
                         "pk_notice_notice_title",
                         WorkReportVO.REPORT_TITLE,
                         WorkReportVO.REPORT_CONTENT,
                        "phone", "mobile", "pk_org_name", WorkReportVO.INPUT_DATE,
                        "input_user_user_name", WorkReportVO.APPROVE_DATE,
                        "approver_user_name", WorkReportVO.MEMO,
                        WorkReportVO.SUBMIT_DATE,
                        WorkReportVO.STATE, WorkReportVO.HANDLE_STATE }, false);
            } else if (ScapCoConstants.LOOK.equals(method_type)) {
                MenubarUtil.hideMenubar(main, "menubar",
                        new String[] { ScapCoConstants.BACK });
            }
        }
    }

    /**
     * 隐藏表单字段
     * 
     * @param view
     * @param formId
     * @param hideItems
     */
    public void HideFEdField(LfwView view, String formId, String[] hideItems) {
        if (view == null) {
            return;
        }
        FormComp fc = (FormComp) view.getViewComponents().getComponent(formId);
        if (fc != null && hideItems != null) {
            for (int i = 0; i < hideItems.length; i++) {
                FormElement fe = fc.getElementById(hideItems[i]);
                if (fe != null) {
                    fe.setVisible(false);
                }
            }
        }
    }

    /**
     * 设置表单字段是否可编辑
     * 
     * @param view
     * @param items
     * @param flag
     */
    public void setFEEnable(LfwView view, String formId, String[] items,
            boolean flag) {

        if (view == null) {
            return;
        }

        FormComp fc = (FormComp) view.getViewComponents().getComponent(formId);
        if (fc != null && items != null) {
            for (int i = 0; i < items.length; i++) {
                FormElement fe = fc.getElementById(items[i]);
                if (fe != null) {
                    fe.setEnabled(flag);
                    if(!flag) {
                        fe.setNullAble(true);
                    }
                }
            }
        }
    }

    private void setGridButtonEnable(LfwView main, String gridId, boolean flag) {
        if (main == null) {
            return;
        }
        //
        GridComp grid = (GridComp) main.getViewComponents()
                .getComponent(gridId);
        if (grid != null) {
            List<MenuItem> items = grid.getMenuBar().getMenuList();
            for (MenuItem item : items) {
                item.setEnabled(flag);
                continue;
            }
        }

    }

    private void setGridButtonVisible(LfwView main, String gridId, boolean flag) {
        if (main == null) {
            return;
        }
        //
        GridComp grid = (GridComp) main.getViewComponents()
                .getComponent(gridId);
        if (grid != null) {
            List<MenuItem> items = grid.getMenuBar().getMenuList();
            for (MenuItem item : items) {
                item.setVisible(flag);
                continue;
            }
        }

    }

    private void setGridButtonVisible(LfwView main, String gridId,
            boolean flag, String[] exceptMenuitemNames) {
        if (main == null) {
            return;
        }
        //
        GridComp grid = (GridComp) main.getViewComponents()
                .getComponent(gridId);
        if (grid != null) {
            List<MenuItem> items = grid.getMenuBar().getMenuList();
            for (MenuItem item : items) {
                if (item != null) {
                    item.setVisible(flag);
                    String id = item.getId();
                    if (id != null) {
                        for (String menuitemName : exceptMenuitemNames) {
                            if (id.equals(menuitemName)) {
                                item.setVisible(!flag);
                            }
                        }
                    }
                }
            }
        }

    }
}
