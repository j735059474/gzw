package nc.uap.ctrl.tpl.print.action;

import java.io.IOException;

import uap.lfw.imp.print.base.CpPrintTemplateManager;

import nc.scap.pub.print.ScapAnalyseUtil;
import nc.scap.pub.util.WordFileOpenUtil;
import nc.uap.ctrl.tpl.exp.TplBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.file.action.FileSystemAction;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;

@Servlet(path = "/word/open")
public class WordOpenAction extends FileSystemAction{

	@Action
	public void down(@Param(name = "id") String id) throws IOException {
		try{
			WordFileOpenUtil.openWord(id, response.getOutputStream());
		}catch (IOException e) {
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	/**
	 * @param id
	 *            pk_file �� fileName��nodesName ��ɵ��ַ���
	 * @throws IOException
	 */
	@Action
	public void wdown(@Param(name = "id") String id) throws IOException {
		try {	
//			ICpPrintTemplateService printTemplate = NCLocator.getInstance()
//					.lookup(ICpPrintTemplateService.class);
//			// ���������������󣬵õ���Ӧ���ϲ�word���ģ���xml����Դ
//			printTemplate.merger(id, response.getOutputStream());
			ScapAnalyseUtil s = new ScapAnalyseUtil();
			s.getMerger(id, response.getOutputStream());
		} catch (TplBusinessException e) {
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}