package nc.scap.pub.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import uap.lfw.core.ml.LfwResBundle;
@Servlet(path = "/sysexcelfile")
public class ExclTempFileAction extends BaseAction{
	private static MultipartResolver multipartResolver = new CommonsMultipartResolver();
	/**
	 * 获得MultipartHttpServletRequest
	 * 
	 * @return
	 * @throws MultipartException
	 */
	private static MultipartHttpServletRequest getMultipartResolver(HttpServletRequest request) throws MultipartException {
		
			((CommonsMultipartResolver) multipartResolver).setDefaultEncoding("UTF-8");
		
		return multipartResolver.resolveMultipart(request);
	}
	/**
	 * 上传文件
	 */
	@Action(method = Keys.POST)
	public void upload() {
		InputStream input = null;
		FileOutputStream fout = null;
		try {
			MultipartHttpServletRequest req = getMultipartResolver(request);
			MultipartFile file = req.getFile("Filedata");
			input = file.getInputStream();
			String importDir = "importfiles";
			String dirStr = ContextResourceUtil.getCurrentAppPath() + importDir;
			File dir = new File(dirStr);
			if(!dir.exists())
				dir.mkdirs();
			String fileName = file.getOriginalFilename();//UUID.randomUUID().toString() + file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()-1);
			String path = dirStr + "/" + fileName;
			if(input != null){
				fout = new FileOutputStream(path);
				byte[] bytes = new byte[4096];
				int count = input.read(bytes);
				while(count > 0){
					fout.write(bytes, 0, count);
					count = input.read(bytes);
				}
			}
			
			String ctrlClazz = req.getParameter("exectrl");
			String widgetId = req.getParameter("widgetId");
			String method = req.getParameter("execmethod");
			if(method == null || method.equals(""))
				print(ctrlClazz + "," + importDir + "/" + fileName + "," + widgetId);
			else
				print(ctrlClazz + "," + importDir + "/" + fileName + "," + widgetId + "," + method);
			
		} catch (Exception e) {
			LfwLogger.error(LfwResBundle.getInstance().getStrByID("bc", "TempFileAction-000005")/*文件上传失败*/, e);
			print(LfwResBundle.getInstance().getStrByID("bc", "TempFileAction-000006")/*文件上传失败:*/);
			print(e.getMessage());
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(fout);
		}
	}	

}
