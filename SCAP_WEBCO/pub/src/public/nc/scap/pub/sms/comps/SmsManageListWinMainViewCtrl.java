package nc.scap.pub.sms.comps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.scap.pub.sms.SmsManageService;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.sms.tools.ScapSmsExtentionUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;

/**
 * 信息列表默认逻辑
 */
public class SmsManageListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {

	private static final String MAIN_VIEW_ID = "main";
	private static final String CARD_WIN_ID = "smsManageComps.smsManage_cardwin";

	/**
	 * 主数据加载逻辑
	 */
	public void onDataLoad(DataLoadEvent event) {
		Dataset ds = event.getSource();
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds));
	}

	/**
	 * 主数据选中逻辑
	 */
	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}

	/**
	 * 新增
	 */
	public void onAdd(MouseEvent mouseEvent) {
		OpenProperties props = new OpenProperties(CARD_WIN_ID, "编辑");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
	}

	/**
	 * 编辑
	 */
	public void onEdit(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "编辑");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
	}

	/**
	 * 删除
	 */
	public void onDelete(MouseEvent mouseEvent) {
		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
	}

	/**
	 * 发送
	 */
	public void onSendTask(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待发送任务");
		}

		Row row = ds.getSelectedRow();
		String pkTask = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new LfwRuntimeException("未启用或启用了多个短信扩展点");
		}
		ISmsExtentionService ext = exts.get(0);

		ScapSmsTaskVO task = null;
		try {
			task = SmsManageService.findTask(pkTask, ScapSmsStatusEnum.NOT_SENT, ScapSmsStatusEnum.FAILED);
			ext.send(task);
		} catch (Exception e) {
			throw new LfwRuntimeException("发送失败", e);
		}

		try {
			ScapDAO.updateVO(task);
			if (task.getSmses() != null) {
				ScapDAO.updateVOArray(task.getSmses());
			}
		} catch (Exception e) {
			throw new LfwRuntimeException("回写短信过程出现异常", e);
		}

		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(getMasterDsId()));
		CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()));
	}

	/**
	 * 外部触发刷新
	 */
	public void doRefresh(Map<?, ?> keys) {
		TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		if (selRow != null) {
			Dataset ds = this.getMasterDs();
			String sign = this.getOperator();
			if (AppConsts.OPE_EDIT.equals(sign)) {
				Row savedRow = ds.getSelectedRow();
				copyTranslateRow2Row(selRow, savedRow, ds);
			} else if (AppConsts.OPE_ADD.equals(sign)) {
				Row savedRow = ds.getEmptyRow();
				savedRow = copyTranslateRow2Row(selRow, savedRow, ds);
				ds.addRow(savedRow);
			}
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		}
	}

	private Row copyTranslateRow2Row(TranslatedRow translatedRow, Row row, Dataset ds) {
		String[] rowKeyStrings = translatedRow.getKeys();
		for (int i = 0; i < rowKeyStrings.length; i++) {
			String rowKeyString = rowKeyStrings[i];
			int colIndex = ds.nameToIndex(rowKeyString);
			if (colIndex != -1)
				row.setValue(colIndex, translatedRow.getValue(rowKeyString));
		}
		return row;
	}

	/**
	 * 查询plugin
	 */
	public void doQueryChange(Map<?, ?> keys) {
		FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere()));
	}

	@Override
	protected String getMasterDsId() {
		return "ScapSmsTaskVO";
	}
}
