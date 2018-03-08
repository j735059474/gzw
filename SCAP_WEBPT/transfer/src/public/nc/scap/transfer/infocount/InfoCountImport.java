package nc.scap.transfer.infocount;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uap.web.bd.pub.AppUtil;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.scap.pt.vos.ScapptInfoCountBVO;
import nc.scap.pt.vos.ScapptInfoCountHVO;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.ctrl.excel.UifExcelImportCmd;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.vo.pub.VOStatus;

import com.sz.common.excel.ExcelSheetConfig;
import com.sz.common.excel.ExcelToVoUtil;
import com.sz.common.excel.Property;
import com.sz.common.excel.Type;
import com.sz.common.exception.ExcelException;

public class InfoCountImport {
	public void onUploadedExcelFile(ScriptEvent e) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String relativePath = ctx.getParameter(UifExcelImportCmd.EXCEL_PATH);
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String fullPath = appPath + "/" + relativePath;
		ExcelSheetConfig sconfig1 = new ExcelSheetConfig();
		sconfig1.setBeginRow(5);

		Map<String, String[]> maps1 = new LinkedHashMap<String, String[]>() {
			{
				put(ScapptInfoCountBVO.PROJECT, new String[] { "项目", "STRING" });
				put(ScapptInfoCountBVO.ROWCOUNT,
						new String[] { "行次", "STRING" });
				put(ScapptInfoCountBVO.JEZS, new String[] { "交易宗数", "STRING" });
				put(ScapptInfoCountBVO.CJJE, new String[] { "成交金额", "STRING" });
				put(ScapptInfoCountBVO.SJ_ZCZE,
						new String[] { "资产总额", "STRING" });
				put(ScapptInfoCountBVO.SJ_JZC, new String[] { "净资产", "STRING" });
				put(ScapptInfoCountBVO.PG_ZCZE,
						new String[] { "资产总额", "STRING" });
				put(ScapptInfoCountBVO.PG_JZC, new String[] { "净资产", "STRING" });
				put(ScapptInfoCountBVO.ZRBDGPZ, new String[] { "转让标的评估值",
						"STRING" });
				put(ScapptInfoCountBVO.JYZJZ, new String[] { "交易增(减)值",
						"STRING" });
				put(ScapptInfoCountBVO.JYZJL, new String[] { "交易增(减)值率(%)",
						"STRING" });
			}
		};

		int j = 0;
		for (Map.Entry<String, String[]> entry : maps1.entrySet()) {
			String[] strs = entry.getValue();
			Property prop = new Property(j, entry.getKey(), strs[0],
					Type.valueOf(strs[1].trim()));
			sconfig1.addProp(prop);
			j++;
		}
		try {
			List<ScapptInfoCountBVO> listm1 = new ArrayList<ScapptInfoCountBVO>();
			ExcelToVoUtil<ScapptInfoCountBVO> sutil1 = new ExcelToVoUtil<ScapptInfoCountBVO>();
			ScapptInfoCountBVO vo1 = new ScapptInfoCountBVO();
			listm1 = sutil1.excelToVOList(sconfig1, fullPath, vo1);

			if (listm1 != null && listm1.size() > 0) {
				ScapptInfoCountHVO mainvo = (ScapptInfoCountHVO) AppUtil
						.getAppAttr("importmain");
				for (ScapptInfoCountBVO tmpvo : listm1) {
					tmpvo.setStatus(VOStatus.NEW);
					tmpvo.setPk_infocount(mainvo.getPk_infocount());
				}
				// 删除之前导入数据
				ScapDAO.getBaseDAO().deleteByClause(ScapptInfoCountBVO.class,
						"pk_infocount='" + mainvo.getPk_infocount() + "'");
				mainvo.setId_scappt_infocount_b(listm1
						.toArray(new ScapptInfoCountBVO[] {}));
				IUifCpbService cpbService = NCLocator.getInstance().lookup(
						IUifCpbService.class);
				try {
					cpbService.insertOrUpdateSuperVO(mainvo, false);
				} catch (CpbBusinessException e1) {
					mainvo.setStatus(VOStatus.UPDATED);
					try {
						cpbService.updateSuperVO(mainvo, false);
					} catch (CpbBusinessException e2) {
						// TODO 自动生成的 catch 块
						e2.printStackTrace();
					}
				}
				AppInteractionUtil.showShortMessage("导入成功,数据正在刷新..");
				new nc.scap.transfer.infocount.InfocountCardWinMainViewCtrl()
						.onAfterRowSelect(new DatasetEvent(new Dataset(
								"scappt_infocount")));
			} else {
				AppInteractionUtil.showShortMessage("未获取Excel数据!");
			}
		} catch (ExcelException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (DAOException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
		}

	}
}