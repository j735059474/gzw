package nc.scap.pub.index.portlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.NewsVO;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.ui.TaskPaginationQueryImpl;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class BigNewsPortlet extends PtBasePortlet {

	private static final String TASK_LIST = "TASK_LIST";

	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		Map<String,Object> root = new HashMap<String,Object>();
		NewsVO[] result = (NewsVO[]) ScapDAO.retrieveByCondition(NewsVO.class, " type = '4' and formstate = 'End' ", " ts desc ");
		NewsVO[] article_list = null;
		
		if(result != null) {
			if(result.length > 9) {
				article_list = new NewsVO[9];
				for(int n = 0; n < 9; n++) {
					article_list[n] = result[n];
				}
			}else {
				article_list = result;
			}
			
			for(NewsVO article : article_list) {
				Date today = new Date(new Date().getTime() - 3*24*3600*1000);
				boolean recently = article.getBillmakedate().after(new UFDateTime(today));
				if(recently) {
					article.setTitle(article.getTitle() + "<span class=\'newicon\'></span>");
				}
			}
		}
		
//		String[] articles = {"沈建平副主任到农发集团调研", "一季度省属企业有效投资达166亿元同比增长25%", 
//				"桑均尧副主任赴省铁路集团开展'进企业'促发展活动" , "2013年我省国有企业主要指标再创历史新高",
//				"马波声主任赴浙江产权交易所有线公司开展大走访调研", "沈建平副主任到综合公司调研指导工作",
//				"2013年我委指导省市企业履行社会责任区的明显成效", "桑均尧副主任赴浙江产权交易所调研", "潘小波副主任一行赴广东省、深证市国资委调研"};
//		
//		for(int x = 0; x < 9; x++) {
//			NewsVO a = new NewsVO();
//			a.setPk_news("123456789");
//			a.setTitle(articles[x]);
//			a.setBillmakedate(new UFDateTime(new Date()));
//			article_list[x] = a;
//		}
		
		String html = null;
		
		String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
		root.put("RES_PATH", resourcePath);
//		if(result.size() > 0){
			try {
				root.put("WINID", request.getWindowID());
				root.put("ARTICLE_LIST", article_list);
				String template = "portlets/BigNewsPortlet.html";
//				html = FreeMarkerTools.contextTemplatRender(template, root);
				html = FreeMarkerTools.render(template, root);
			} catch (Exception e) {
				html = NCLangRes4VoTransl.getNCLangRes().getStrByID("pint", "MyWflPortlet-000000")/*获取待办流程失败:*/ + e.getMessage();
				PortalLogger.error(html, e);
			}
//		}else{
//			String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("pint", "MyWflPortlet-000001");/*暂无相关信息*/
//			html = "<div style='text-align: center; line-height: 65px; width: 100%; font-family: 宋体; height: 65px; color: #666666; font-size: 12px;'>"+ msg +"</div>";
//		}
		response.getWriter().print(html);
//		response.getWriter().flush();
		
	}

}
