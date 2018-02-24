package nc.uap.wfm.action;
import java.io.IOException;
import java.io.PrintWriter;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.wfm.contanier.ProDefsContainer;
import nc.uap.wfm.engine.IWfmFormOper;
import nc.uap.wfm.engine.TaskProcessUI;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.itf.IWfmFlwTypeQry;
import nc.uap.wfm.itf.IWfmProDefQry;
import nc.uap.wfm.logger.WfmLogger;
import nc.uap.wfm.model.HumAct;
import nc.uap.wfm.model.ProDef;
import nc.uap.wfm.utils.WfmClassUtil;
import nc.uap.wfm.utils.WfmConstant;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;
import nc.vo.jcom.xml.XMLUtil;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
@Servlet(path = "/PaWfm") public class PaWfmAction extends BaseAction {
	@SuppressWarnings("restriction") @Action(method = "POST") 
	public void open() {
		String proDefPk = request.getParameter(WfmConstant.PARAM_PRODEFPK);
		String portId = request.getParameter("port_id");
		WfmProdefVO wfmProdefVO =null;
		String fullUrl="";
		try {
			wfmProdefVO = NCLocator.getInstance().lookup(IWfmProDefQry.class).getProDefVOByProDefPk(proDefPk);
		} catch (WfmServiceException e) {
			WfmLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		ProDef proDef=null;
		WfmFlwTypeVO  flwTypeVo=null;
		if(wfmProdefVO==null||StringUtils.isEmpty(wfmProdefVO.getProcessstr())){
			//未定义
			fullUrl="@NOPRODEF@";
		}else{
			proDef = ProDefsContainer.getProDef(wfmProdefVO);
			
			String flwTypePk = wfmProdefVO.getFlwtype();
			flwTypeVo=NCLocator.getInstance().lookup(IWfmFlwTypeQry.class).getFlwTypeVoByPk(flwTypePk);
			
			String serverClass = flwTypeVo.getServerclass();
			IWfmFormOper formOper = (IWfmFormOper) WfmClassUtil.loadClass(serverClass);
			((ProDef) proDef).setFlwtype(flwTypeVo);
			if(proDef.getPorts()==null||proDef.getPorts().size()==0||proDef.getPorts().get(portId)==null){
				//未保存
				fullUrl="@NOSAVE@";
			}
		
			if(StringUtils.isEmpty(fullUrl))
				fullUrl=getUIUrlByProdef(formOper,proDef,portId);
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			WfmLogger.error(e1.getMessage(), e1);
			throw new LfwRuntimeException(e1.getMessage());
		}
		Document document = this.buildXml(fullUrl);
		XMLUtil.printDOMTree(out, document, 0, "UTF-8");
		out.println();
	}
	private String getUIUrlByProdef(IWfmFormOper formOper,ProDef proDef,String portId){
		TaskProcessUI ui = formOper.getPersonlizationUrl(proDef,(HumAct) proDef.getPorts().get(portId));
		String width="";
		String height="";
		String title="";
		String url="";
		if(ui==null){
			return "@NOURL@";
		}else{
			if(ui.getWidth()!=null)
				width="null".equals(ui.getWidth())?"650":ui.getWidth();
			if(ui.getHeight()!=null)
				height="null".equals(ui.getHeight())?"1100":ui.getHeight();
			if(ui.getTitle()!=null)
				title="null".equals(ui.getTitle())?"":ui.getTitle();
			 url=ui.getUrl();
			 //将UI高度、宽度拼接到URL中
			 String fullUrl=url+"@p_p@height="+height+"@p_p@width="+width+"@p_p@title="+title;
			 return fullUrl;
		}
		
	}
	private Document buildXml(String url) {
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Root");
		doc.appendChild(root);
		Element node = doc.createElement("Url");
		node.setTextContent(url);
		root.appendChild(node);
		return doc;
	}
}
