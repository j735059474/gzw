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

public class UifANZipDownloadCmd extends UifCommand{
  
  private String url = null;
  
  private String file;
  private String filename;
  
  private Map<String, String> paramMap;
  
  /**
   * 打包下载
   * @param file  要压缩的目录或文件
   * 
   */
  public UifANZipDownloadCmd(String file,String filename) {
    this.file = file;
    this.filename = filename;
  }
  


  /**
   * 打包下载
   * @param srcDirPath  要压缩的目录或文件
   * @param targetDirPath 要输出的目录
   * @param zipFileName 压缩文件名
   * @param paramMap  预留参数map
   */
  public UifANZipDownloadCmd(String file,String FileName,Map<String, String> paramMap) {
    this.file = file;
    this.filename = FileName;
    this.paramMap = paramMap;
  }
  

  @Override
  public void execute() {
    String encode = "GBK";
    if(this.file == null || "".equals(this.file))
      throw new LfwRuntimeException("file is not null !");
    String requrl = LfwRuntimeEnvironment.getRootPath() + "/pt/zip/downloadByIPk?";
    StringBuffer parmSb = new StringBuffer();
    parmSb.append(IScapFileConstants.PKFILE + "=" +file);
    if(this.filename!=null&&!"".equals(this.filename))
        parmSb.append("&" + IScapFileConstants.ZipFileName + "=" +filename);
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
