package nc.scap.pub.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import nc.uap.cpb.log.CpLogger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifFileUploadCmd;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.file.LfwFileConstants;
import nc.vo.ml.NCLangRes4VoTransl;

public class UifExcelImportCmd extends UifCommand{
	public static final String EXCEL_PATH = "excel_imp_path";
	/*
	 * 上传之后调用的类路径
	 */
	private String ctrlClazz = null;
	
	private String widgetId= null;
	/**
	 * 
	 * @param ctrlClazz 上传之后调用的类路径
	 * @param widgetId 上传对应的widgetId
	 * 
	 */
	public UifExcelImportCmd(String ctrlClazz, String widgetId){
		this.ctrlClazz = ctrlClazz;
		this.widgetId = widgetId;
	}
	@Override
	public void execute() {
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			paramMap.put(LfwFileConstants.UPLOAD_URL, URLEncoder.encode(LfwRuntimeEnvironment.getRootPath() + "/pt/sysexcelfile/upload?exectrl=" + ctrlClazz + "&widgetId=" + this.widgetId + "&", "UTF-8"));
			paramMap.put(LfwFileConstants.CALLBACK_METHOD, "uploadedExcelFile");
			paramMap.put(LfwFileConstants.CLOSEDIALOG, "true");
			paramMap.put(LfwFileConstants.UPLOADED_ALERT, "false");
			paramMap.put(LfwFileConstants.FILEEXT, "*.xlsx;*.xls;*.xlsm");
			paramMap.put(LfwFileConstants.FILEDESC, "*.xlsx;*.xls;*.xlsm");
			CmdInvoker.invoke(new UifFileUploadCmd(NCLangRes4VoTransl.getNCLangRes().getStrByID("imp", "UifExcelImportCmd-000000")/*选择导入文件*/, paramMap));
		} 
		catch (UnsupportedEncodingException e) {
			CpLogger.error(e);
		}
	}

}
