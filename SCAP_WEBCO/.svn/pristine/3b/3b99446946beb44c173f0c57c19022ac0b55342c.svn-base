package nc.scap.pub.index;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import nc.message.vo.MessageVO;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.NewsVO;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;

@Servlet(path = "/indexAction")
public class IndexAction extends BaseAction{
	
	@Action(method = Keys.POST)
	public void getArticleById(){
		String pkNews = request.getParameter("pkNews");
		NewsVO news = (NewsVO) ScapDAO.retrieveByPK(NewsVO.class, pkNews);
		if(news != null) {
			try {
				Object content = news.getContent();
				String c = "";
				if(content != null) {
					c = URLDecoder.decode(content.toString(), "UTF-8");
					
				}
				print(c);	
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Action(method = Keys.POST)
	public void getWarnById() {
		String pkMessage = request.getParameter("pkWarn");
		MessageVO msg = (MessageVO) ScapDAO.retrieveByPK(MessageVO.class, pkMessage);
		if(msg != null) {
			Object content = msg.getContent();
			if(content != null && !content.equals("")) {
				String c = content.toString();
				print(c);	
			}
		}
	}
}
