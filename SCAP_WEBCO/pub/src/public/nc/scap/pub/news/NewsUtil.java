package nc.scap.pub.news;

import nc.jdbc.framework.processor.ArrayProcessor;
import nc.scap.pub.news.wfm.WfmFlwFormVO;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.NewsVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.exe.WfmCmd;
import uap.web.bd.pub.AppUtil;

public class NewsUtil {
	

	/**
	 * 插入并提交提交首页新闻
	 */
	public static void submitNewsVO(NewsVO vo) {
		
		ScapDAO.insertVO(vo);
		WfmFlwFormVO formCtx = new WfmFlwFormVO();
		for(String attr : vo.getAttributeNames()) {
			Object val = vo.getAttributeValue(attr);
			formCtx.setAttributeValue(attr, val);
		}
		
		// 流程form
		getApplicationContext().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
		// 流程类型
		getApplicationContext().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, getFlwTypePk());
		// 设置任务pk
		//getApplicationContext().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, null);
		
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TransmitUserPk, "");
		CmdInvoker.invoke(new WfmCmd());
	}
	
	/**
	 * 提交重大事项
	 */
	public static void submitMajorIssue(String title, String content, String pk_group, String pk_org, String pk_user) {
		NewsVO vo = new NewsVO();
		vo.setTitle(title);
		vo.setContent(content);
		vo.setPk_group(pk_group);
		vo.setPk_org(pk_org);
		vo.setBillmaker(pk_user);
		vo.setType("4");
		
		submitNewsVO(vo);
	}
	
	/**
	 * 获得单据类型PK
	 */
	public static String getFlwTypePk() {
		Object[] pk_flwtype = (Object[]) ScapDAO.executeQuery("select pk_flwtype from wfm_flwtype where typecode = 'pub_news'", new ArrayProcessor());
		return pk_flwtype.length == 1 ? (String) pk_flwtype[0] : "0001ZG100000000F17SQ";
	}

	protected static ApplicationContext getApplicationContext() {
		return AppLifeCycleContext.current().getApplicationContext();
	}
}
