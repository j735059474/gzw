package nc.uap.ctrl.file;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nc.uap.cpb.log.CpLogger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.FileTypeHelper;
import nc.uap.lfw.file.vo.FileTypeVO;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.util.LfwClassUtil;

import org.apache.commons.lang.StringUtils;

public class DefaultFileViewUtil implements IFileViewUtil {

	@Override
	public List<FileViewVO> loadFiles(String billitem, String billtype,String sysid)
		throws LfwBusinessException {
		List<FileViewVO> list = new ArrayList<FileViewVO>();
		FileManager filemanager = FileManager.getSystemFileManager(sysid);
		  try {
			  String editonlieable = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("editonlieable");
			  String printable = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("printable");
			  String readOnly = "true";
			  if (StringUtils.isNotBlank(editonlieable) && editonlieable.equals("true"))
				  readOnly = "false";
			  String fileprint = "false";
			  if (StringUtils.isNotBlank(printable) && printable.equals("true"))
				  fileprint = "true";
			  LfwFileVO[] files =  filemanager.getAttachFileByItemID(billitem, billtype);
			  for(LfwFileVO file : files){
				  FileManager manager = null;
				  if(StringUtils.isNotBlank(file.getFilemgr()))
					  manager = (FileManager) LfwClassUtil.newInstance(file.getFilemgr());
				  else manager = filemanager;
				  FileViewVO view = new FileViewVO();
				  view.setCode(file.getPk_lfwfile());
				  view.setName(file.getDisplayname());
				  view.setPreurl(LfwRuntimeEnvironment.getRootPath() + "/pt/file/preview?sysid="+(sysid== null?"":sysid) +"&id=" +file.getPk_lfwfile() );
				  FileTypeVO ftvo= FileTypeHelper.getFiletype(file.getFiletypo());
				  if(null == ftvo)
					  view.setViewType("");
				  else
					  view.setViewType(ftvo.getEdittype());
				  String url = "";
				  if(view.getViewType() == null){
//					  url = LfwRuntimeEnvironment.getRootPath() + "/pt/file/down?sysid="+(sysid== null?"":sysid) +"&id=" +file.getPk_lfwfile();
					  url = manager.buildViewUrl(file);
				  }
				  else if(FileTypeHelper.ViewType_Office.equals(view.getViewType())){
					  url = LfwRuntimeEnvironment.getRootPath() + "/core/word.jsp?pageId=officeedit&readonly=" + readOnly + "&fileprint=" + fileprint + "&url=" + file.getPk_lfwfile() + "&sysid="+(sysid== null?"":sysid);
				  }
//				  else if(FileTypeHelper.ViewType_Txt.equals(view.getViewType()))
//					  url = LfwRuntimeEnvironment.getRootPath() + "/pt/file/txt?sysid="+(sysid== null?"":sysid) +"&id=" +file.getPk_lfwfile();
				  else if(FileTypeHelper.ViewType_Pdf.equals(view.getViewType()))
//					  url = manager.buildDownUrl(file);
					  url = manager.buildViewUrl(file);
//					  url = LfwRuntimeEnvironment.getRootPath() + "/pt/file/down?id=" +file.getPk_lfwfile() + "sysid=" + sysid== null?"":sysid ;
				  else if ("gw".equalsIgnoreCase(view.getViewType()) || "sep".equalsIgnoreCase(view.getViewType())) { // 书生电子签章
					  url = LfwRuntimeEnvironment.getRootPath() + "/scap/page/gwreader.jsp?id=" + file.getPk_lfwfile() + "&sysid=" + (sysid == null ? "" : sysid);
				  }
				  else
					  url = manager.buildViewUrl(file);
//					  url = LfwRuntimeEnvironment.getRootPath() + "/pt/file/down?sysid="+(sysid== null?"":sysid) +"&id=" +file.getPk_lfwfile();
				  view.setUrl( URLEncoder.encode(url,"utf-8"));
				  list.add(view);
			  }
		  }
		  catch(Exception e){
			  CpLogger.error(e);
		  }
		return list;
	}

	@Override
	public void changename(String sysid, String code, String newname) throws LfwBusinessException {
		FileManager filemanager = FileManager.getSystemFileManager(sysid);
		LfwFileVO file = filemanager.getFileVO(code);
		file.setFilename(newname);
		filemanager.updateVo(file);
		
	}

	@Override
	public void delete(String sysid, String code) throws LfwBusinessException {
		FileManager filemanager = FileManager.getSystemFileManager(sysid);
		try {
			filemanager.delete(code);
		} catch (Exception e) {
			CpLogger.error(e);
			throw new LfwBusinessException(e);
		}
	}
	
}
