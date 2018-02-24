package nc.scap.pub.work_type_cardwin;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
public class TimeDefineInfoViewController {
  private static final long serialVersionUID=1L;
  public void beforeShow(  DialogEvent dialogEvent){
    LfwView main = AppLifeCycleContext.current().getViewContext().getView();
		Dataset masterDs = main.getViewModels().getDataset("timeDefineInfoDs");
		masterDs.clear();
		Row emptyRow = masterDs.getEmptyRow();
		String info1 = "报告一般分为：年报、季报、月报、旬报、日报.\n" +
				"催报时间格式约定：\n" +
				"[Y].[Q].[MM].[DD]，共8位，中间用英文逗号相隔。";
		String info2 = 
				"Y \n" +
				"代表年度，表示‘每X年一报’，用1位字符表示。\n" +
				"字典项为：\n" +
				"Y     表示每年\n" +
				"0     表示非年报\n" +
				"1-9   表示每X年\n" +
				"Q\n" +
				"代表季度，用1位字符表示。\n" +
				"字典项为：\n" +
				"Q  表示每季度\n" +
				"0	表示非季报\n" +
				"MM\n" +
				"代表月份，用2位字符表示。\n" +
				"字典项为：\n" +
				"MM  表示每月\n" +
				"00    表示非月报\n" +
				"01-12  表示第几个月\n" +
				"M1-M3  表示每季度的第几个月\n" +
				"DD\n" +
				"代表日期，用2位字符表示。\n" +
				"字典项为：\n" +
				"DD  表示每天\n" +
				"01-31 表示几号\n" +
				"D3 表示旬\n";
		String info3 = 
				"年报\n" +
				"如：\n" +
				"每年1月5号之前填报，表示为：Y.0.01.05\n" +
				"每3年一次，并且1月5号之前填报，表示为：3.0.01.05\n" +
				"季报\n" +
				"如：\n" +
				"每季度的第一个月5号，表示为：0.Q.M1.05\n" +
				"月报\n" +
				"如：\n" +
				"每月5号，表示为：0.0.MM.05\n" +
				"旬报\n" +
				"如：\n" +
				"每旬，表示为：0.0.MM.D3\n" +
				"日报\n" +
				"如:\n" +
				"每天，表示为： 0.0.00.DD\n";
		emptyRow.setValue(0,info1);
		emptyRow.setValue(1,info2);
		emptyRow.setValue(2,info3);
		FormComp formcomp = (FormComp) main.getViewComponents()
				.getComponent("timeDefineInfoView");
		formcomp.getElementById("info1").setValue(info1);
		formcomp.getElementById("info1").setVisible(
				true);
		masterDs.addRow(emptyRow);
		masterDs.setRowSelectIndex(masterDs.getRowIndex(emptyRow));
		masterDs.setEnabled(true);
  }
}
