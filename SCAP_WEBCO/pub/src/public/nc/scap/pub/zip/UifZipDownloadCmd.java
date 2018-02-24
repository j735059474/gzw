package nc.scap.pub.zip;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nc.scap.pub.itf.IScapFileConstants;
import nc.uap.cpb.log.CpLogger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;

import org.apache.commons.lang.StringUtils;

public class UifZipDownloadCmd extends UifCommand{
	
	private String url = null;
	
	private String srcDirPath;
	private String targetDirPath;
	private String zipFileName;
	private Map<String, String> paramMap;
	
	/**
	 * �������
	 * @param srcDirPath  Ҫѹ����Ŀ¼���ļ�
	 * targetDirPath��zipFileName ����srcDirPath����
	 */
	public UifZipDownloadCmd(String srcDirPath) {
		this.srcDirPath = srcDirPath;
	}
	
	/**
	 * �������
	 * @param srcDirPath  Ҫѹ����Ŀ¼���ļ�
	 * @param targetDirPath Ҫ�����Ŀ¼���ļ�
	 */
	public UifZipDownloadCmd(String srcDirPath,String targetDirPath) {
		this.srcDirPath = srcDirPath;
		this.targetDirPath = targetDirPath;
	}
	
	/**
	 * �������
	 * @param srcDirPath  Ҫѹ����Ŀ¼���ļ�
	 * @param targetDirPath Ҫ�����Ŀ¼
	 * @param zipFileName ѹ���ļ���
	 */
	public UifZipDownloadCmd(String srcDirPath,String targetDirPath,String zipFileName) {
		this.srcDirPath = srcDirPath;
		this.targetDirPath = targetDirPath;
		this.zipFileName = zipFileName;
	}
	
	/**
	 * �������
	 * @param srcDirPath  Ҫѹ����Ŀ¼���ļ�
	 * @param targetDirPath Ҫ�����Ŀ¼
	 * @param zipFileName ѹ���ļ���
	 * @param paramMap  Ԥ������map
	 */
	public UifZipDownloadCmd(String srcDirPath,String targetDirPath,String zipFileName,Map<String, String> paramMap) {
		this.srcDirPath = srcDirPath;
		this.targetDirPath = targetDirPath;
		this.zipFileName = zipFileName;
		this.paramMap = paramMap;
	}
	

	@Override
	public void execute() {
		String encode = "GBK";
		if(this.srcDirPath == null || "".equals(this.srcDirPath))
			throw new LfwRuntimeException("srcDirPath is not null !");
		String requrl = LfwRuntimeEnvironment.getRootPath() + "/pt/zip/download?";
		StringBuffer parmSb = new StringBuffer();
		try {
			parmSb.append(IScapFileConstants.SrcDirPath + "=" + URLEncoder.encode(this.srcDirPath, encode));
			if(this.targetDirPath!=null&&!"".equals(this.targetDirPath))
				parmSb.append("&" + IScapFileConstants.TargetDirPath + "=" + URLEncoder.encode(this.targetDirPath, encode));
			if(this.zipFileName!=null && !"".equals(this.zipFileName))
				parmSb.append("&" + IScapFileConstants.ZipFileName + "=" + URLEncoder.encode(this.zipFileName, encode));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
//		sb.append("&");
		String parmurl=parmSb.toString();
		if(paramMap != null){
			Iterator<Entry<String, String>> it = paramMap.entrySet().iterator();
			while(it.hasNext()){
				parmurl += "&";
				Entry<String, String> entry = it.next();
				try {
					parmurl += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),encode);
				} catch (UnsupportedEncodingException e) {
					ScapLogger.error(e.getMessage());
				}
				if(it.hasNext())
					parmurl += "&";
			}
		}
		try{
			if(!StringUtils.isBlank(requrl+parmurl)){
			  String js = "sysDownloadFile('" +requrl+parmurl+ "');";
			  AppLifeCycleContext.current().getApplicationContext().addExecScript(js);
			}
		  }catch (Exception e) {
			CpLogger.error(e);
		  }
	}

}
