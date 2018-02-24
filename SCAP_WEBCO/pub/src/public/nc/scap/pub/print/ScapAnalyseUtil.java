package nc.scap.pub.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.ml.NCLangResOnserver;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.WordFileXml;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.exp.TplBusinessException;
import nc.scap.pub.print.ICpPopWordTemplateService;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateInnerQryService;
import nc.uap.ctrl.tpl.print.TemplatePubViewController;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateVO;
import nc.uap.ctrl.tpl.print.init.ICpFreeFormTemplatePrintService;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctrlfrm.DevicePhase;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.exception.LfwLicenseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.oba.word.merger.AbstractDocumentMerger;
import nc.uap.oba.word.merger.ITemplateEntity;
import nc.uap.oba.word.merger.impl.DocumentMerger;
import nc.uap.oba.word.merger.model.node.DataSource;
import nc.uap.oba.word.merger.model.node.NodeList;
import nc.uap.portal.log.ScapLogger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.imp.print.args.IPrintTempalteConstants;
import uap.lfw.imp.print.base.PrintPdfHelper;
import uap.lfw.imp.print.event.PrintTemplatePictureCallBack;
import uap.web.bd.pub.session.IWebBDLicenseProvider;

public class ScapAnalyseUtil {
	/**
	 * 解析合并模板套打Xml
	 * @author liyuchen
	 * 2013-09-06
	 * @param filePath xml存放路径
	 * @param list     按照xml编写存放数据,单行数据存map，多行数据存List<Map>
	 */
	public static String mergerXml(String filePath,List list){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try{
			//获取xml文件
			document = saxReader.read(new File(filePath));
			//获取根节点
			Element root = document.getRootElement();
			//获得根节点的子节点
			List<Element> elements = root.elements();
			if(elements.size()<1){
				throw new RuntimeException("xml文件没有定义规则");
			}
			//多表文件
			if(elements.size()>1){
				for(int i = 0;i<list.size();i++){
					//如果是单表,直接遍历xml进行赋值
					if(list.get(i) instanceof Map){
						//获得对应顺序的xml模板
						Element element = elements.get(i);
						Map map = (Map)list.get(i);
						for(Iterator iter = element.elementIterator();iter.hasNext();){
							Element e = (Element)iter.next();
							e.setText(map.get(e.getName().toLowerCase())==null?"":map.get(e.getName().toLowerCase()).toString());
						}
					}//如果是多行数据，需要进行填充xml
					else if(list.get(i) instanceof List){
						//获得对应的Element
						Element element = elements.get(i);
						//获得对应的list对象
						List<Map> subList = (List<Map>)list.get(i);
						for(int j = 0;j<subList.size();j++){
							//第一条数据填充element元素
							if(j==0){
								Map map = subList.get(j);
								for(Iterator iter = element.elementIterator();iter.hasNext();){
									Element e = (Element)iter.next();
									e.setText(map.get(e.getName().toLowerCase())==null?"":map.get(e.getName().toLowerCase()).toString());
								}
							}
							//如果是1对多的关系,第二条以后的数据,增加element元素
							else{
								Element tableElement = root.addElement(element.getName());
								for(Iterator iter = element.elementIterator();iter.hasNext();){
									Element e = (Element)iter.next();
									Element propertyElement = tableElement.addElement(e.getName());
									propertyElement.addAttribute("uapDisplayName", e.attributeValue("uapDisplayName"));
									propertyElement.setText(subList.get(j).get(e.getName().toLowerCase())==null?"":subList.get(j).get(e.getName().toLowerCase()).toString());
								}
							}
						}
					}
				}
			}//单表文件
			else{
				if(list.size()!=0){
					//获取二级元素
					Element element = elements.get(0);
					Map map = new HashMap();
					try {
						map = (Map)list.get(0);
					} catch (Exception e) {
						ScapLogger.error(e.getMessage());
						throw new RuntimeException("没有上传解析数据");
					}
					for(Iterator iter = element.elementIterator();iter.hasNext();){
						Element subElement = (Element)iter.next();
						//将值放进xml中
						subElement.setText(map.get(subElement.getName().toLowerCase())==null?"":map.get(subElement.getName().toLowerCase()).toString());
					}
				}
			}
		}catch(Exception e){
			ScapLogger.error(e.getMessage());
			if(e instanceof FileNotFoundException){
				throw new RuntimeException("未找到"+filePath);
			}
			
			if(e.getMessage()!=null&&!"".equals(e.getMessage())){
				throw new RuntimeException(e.getMessage());
			}else{
				throw new RuntimeException("解析xml出错!");
			}
		}
		//将xml放进作用域
		Map map = WordFileXml.getInstance().getMap();
		map.put(CntUtil.getCntUserPk(), document.asXML());
		//ScapLogger.console(document.asXML());
		return document.asXML();
	}
	/**
	 * 合并报表并输出
	 */
	public void merger(String id, HttpServletResponse response)
			throws TplBusinessException {
		//进行模板合并输出
		OutputStream out = null;
		try{
			if(PrintPdfHelper.isPrintPdf()){
				response.addDateHeader("Last-Modified", System.currentTimeMillis());
				response.addHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Encoding", "gzip");
			}
			out = response.getOutputStream();
			getMerger(id, out);
		}catch(Exception e){
			CpLogger.error(e.getMessage(), e);
			throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000001")/*文件下载失败*/, e);
		}
	}
	public void getMerger(String id, OutputStream out) throws TplBusinessException{
		Calendar cal = Calendar.getInstance();
		long start = cal.getTimeInMillis();
		String path = null;
		//设置一个临时目录
		String tmp_root = LfwRuntimeEnvironment.getRealPath() + "/wordtemp";
		try {
			String[] str = id.split(",");
			String pk_file = str[0];
			createDirectory(tmp_root);
			//将设计的word模板下载到一个临时文件中
			String tmp_path = tmp_root + "/" + UUID.randomUUID().toString()+ ".docx";
			String update_path = tmp_root + "/" + UUID.randomUUID().toString()+ ".docx";
			String wordPath = tmp_root + "/" + UUID.randomUUID().toString() + ".docx";
			OutputStream out_tmp = null;
			try {
				out_tmp = new FileOutputStream(tmp_path);
				 String sysid = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter(LfwFileConstants.SYSID);
			   if(sysid==null || ("").equals(sysid))
						sysid = LfwFileConstants.SYSID_Default;
				//通过文件pk_template和临时路径，将设计的word模板下载到文件中
			    try{
			    	FileManager.getSystemFileManager(sysid).download(pk_file,out_tmp);
			    }catch (Exception e) {
			    	FileManager.getSystemFileManager("bafile").download(pk_file, out_tmp);
				}
				AbstractDocumentMerger merger = new DocumentMerger();
				printDate(start,"print_word---load file start======");
				ITemplateEntity ite = merger.loadTemplate(tmp_path);
				ite.setOutputFilePath(update_path);
				printDate(start,"print_word---update file start======");
				boolean update = merger.update(ite);
				printDate(start,"print_word---update file finish======");
				if (update && ite != null) {
					ite = merger.loadTemplate(update_path);
				}
				if(ite!=null){
					String dataSourceName = null;
					NodeList<DataSource> sourceNodes = ite.getWTModel().getDataSourceCollection().getDataSources();
					if(sourceNodes!=null && sourceNodes.getCount()>0){
						Iterator<DataSource> its = sourceNodes.iterator();
						while(its.hasNext()){
							DataSource source = its.next();
							if(source!=null){
								dataSourceName = source.getDisplayName();
								break;
							}
						}
					}
					if(StringUtils.isEmpty(dataSourceName)){
						dataSourceName = "Root";
					}
					printDate(start,"print_word---load xml DataSource file start======");
					Map map = WordFileXml.getInstance().getMap();
					Object object = map.get(CntUtil.getCntUserPk());
					ite.getDataRegistry().registByXmlData(dataSourceName, object.toString());
					printDate(start,"print_word---load xml DataSource file finsih======");
					
					ite.setOutputStream(out);
					//modify by liyuchen 2013-11-12 修改word无法编辑问题
//					ite.getSettings().setPublishAsFinal(true);
					//modify by liyuchen 2013-11-12 修改word无法编辑问题 end
					//ite.getCallBackRegistry().regist(new PrintTemplatePictureCallBack(null,null,null));
					printDate(start,"print_word---merger start======");
					merger.merging(ite);
					printDate(start, "print_word---finish ======");
				}
				out.flush();
			} catch (Exception e) {
				CpLogger.error(e.getMessage(), e);
				throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000001")/*文件下载失败*/, e);
			} finally {
				if (null != out_tmp) {
					out_tmp.flush();
					out_tmp.close();
				}
				deleteFile(tmp_path);
				deleteFile(update_path);
				deleteFile(wordPath);
			}
		} catch (Exception e1) {
			throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000001")/*文件下载失败*/, e1);
		} finally {
			try {
				if (null != out) {
					out.flush();
					out.close();
				}
			} catch (Exception e2) {
				CpLogger.error(e2.getMessage(), e2);
				throw new TplBusinessException(e2.getMessage(), e2);
			} finally {
				if(StringUtils.isNotEmpty(path)){
					deleteFile(path);
				}
//				deleteDirectory(tmp_root);
			}
		}
	}
	/** 
	 *合并报告重载方法.只做存储处理
	 * @author liyuchen
	 * @param pk_file
	 */
	public String getMerger(String pk_file) throws TplBusinessException{
		Calendar cal = Calendar.getInstance();
		long start = cal.getTimeInMillis();
		String path = null;
		//设置一个临时目录
		String tmp_root = LfwRuntimeEnvironment.getRealPath() + "/wordtemp";
		//设置一个存放生成后word的目录
		String wordformal = LfwRuntimeEnvironment.getRealPath() + "/wordformal";
		try {
			createDirectory(tmp_root);
			createDirectory(wordformal);
			//将设计的word模板下载到一个临时文件中
			String tmp_path = tmp_root + "/" + UUID.randomUUID().toString()+ ".docx";
			String update_path = tmp_root + "/" + UUID.randomUUID().toString()+ ".docx";
			String wordPath = tmp_root + "/" + UUID.randomUUID().toString() + ".docx";
			path  = wordformal +"/" +UUID.randomUUID().toString()+".docx";
			OutputStream out_tmp = null;
			try {
				out_tmp = new FileOutputStream(tmp_path);
				//通过文件pk_template和临时路径，将设计的word模板下载到文件中
				FileManager.getSystemFileManager(LfwFileConstants.SYSID_Default).download(pk_file,out_tmp);
				AbstractDocumentMerger merger = new DocumentMerger();
				printDate(start,"print_word---load file start======");
				ITemplateEntity ite = merger.loadTemplate(tmp_path);
				ite.setOutputFilePath(path);
				printDate(start,"print_word---update file start======");
				boolean update = merger.update(ite);
				printDate(start,"print_word---update file finish======");
				if (update && ite != null) {
					ite = merger.loadTemplate(path);
				}
				if(ite!=null){
					String dataSourceName = null;
					NodeList<DataSource> sourceNodes = ite.getWTModel().getDataSourceCollection().getDataSources();
					if(sourceNodes!=null && sourceNodes.getCount()>0){
						Iterator<DataSource> its = sourceNodes.iterator();
						while(its.hasNext()){
							DataSource source = its.next();
							if(source!=null){
								dataSourceName = source.getDisplayName();
								break;
							}
						}
					}
					if(StringUtils.isEmpty(dataSourceName)){
						dataSourceName = "Root";
					}
					printDate(start,"print_word---load xml DataSource file start======");
					Map map = WordFileXml.getInstance().getMap();
					Object xmlValue = map.get(CntUtil.getCntUserPk());
					ite.getDataRegistry().registByXmlData(dataSourceName, xmlValue.toString());
					printDate(start,"print_word---load xml DataSource file finsih======");
					
					//modify by liyuchen 2013-11-12 修改word无法编辑问题
//					ite.getSettings().setPublishAsFinal(true);
					//modify by liyuchen 2013-11-12 修改word无法编辑问题 end
					//ite.getCallBackRegistry().regist(new PrintTemplatePictureCallBack(null,null,null));
					printDate(start,"print_word---merger start======");
					merger.merging(ite);
					printDate(start, "print_word---finish ======");
				}
			} catch (Exception e) {
				CpLogger.error(e.getMessage(), e);
				throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000001")/*文件下载失败*/, e);
			} finally {
				if (null != out_tmp) {
					out_tmp.flush();
					out_tmp.close();
				}
				deleteFile(tmp_path);
				deleteFile(update_path);
				deleteFile(wordPath);
			}
			return path;
		} catch (Exception e1) {
			throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000001")/*文件下载失败*/, e1);
		} finally {
//				deleteDirectory(tmp_root);
		}
	}
	private void printDate(long start, String msg) {
		Calendar cal = Calendar.getInstance();
		long c = cal.getTimeInMillis() - start;
		CpLogger.console(msg + c);
	}
	private void createDirectory(String temp){
		File tmp = new File(temp);
		if(!tmp.isDirectory()){
			tmp.mkdirs();
		}
	}
	/** 根据路径删除目录下制定文件s */
	private void deleteFile(String path){
		String rootPath = path.substring(0, path.lastIndexOf("/"));
		String fileName = path.substring(path.lastIndexOf("/") + 1);
		File file = new File(rootPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (fileName.equals(f.getName())) {
					f.delete();
				}
			}
		}
	}
	public static void print(ICpFreeFormTemplatePrintService freePrint,String xmlValue) throws TplBusinessException {
		ScapAnalyseUtil s = new ScapAnalyseUtil();
		s.print(freePrint, null, null, null,xmlValue);
	}
	public static void printdefault(ICpFreeFormTemplatePrintService freePrint,String xmlValue,String modelCode, String nodecode,String defmodelcode) throws TplBusinessException {
		DevicePhase devicePhase = LfwRuntimeEnvironment.getDevicePhase();
		if(!"PC".equals(devicePhase.getDevtype())){
			throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000002")/*不支持该设备打印*/);
		}
		//模板打印按钮触发
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		if (ctx == null) {
			return;
		}
		if(StringUtils.isEmpty(nodecode)){
			//获得节点id
			nodecode = (String) ctx.getApplicationContext().getAppAttribute(AppControlPlugin.NODECODE);
			if(null!=nodecode){
				LfwRuntimeEnvironment.getWebContext().getAppSession().addOriginalParameter(AppControlPlugin.NODECODE, nodecode);
			}
			if(null==nodecode){
				nodecode = LfwRuntimeEnvironment.getWebContext().getAppSession().getOriginalParameter(AppControlPlugin.NODECODE);
			}
		}
		ICpPrintTemplateInnerQryService service = NCLocator.getInstance()
				.lookup(ICpPrintTemplateInnerQryService.class);
		try {
			//先按各年度取模板，若没取到则取默认模板
			CpPrintTemplateVO ptVos = service.getTemplateVOByModelCode(modelCode);
			if(ptVos==null){
				ptVos = service.getTemplateVOByModelCode(defmodelcode);
			}
			
			CpPrintTemplateVO sysVo = service.getSysPrintTemplateVOPkByNode(nodecode, null);
			if(sysVo==null){
				throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
			}else{
				 if(ptVos!=null ){
					ICpPopWordTemplateService popService = NCLocator.getInstance().lookup(ICpPopWordTemplateService.class);
					popService.open(ptVos.getPk_print_template(), freePrint,null,xmlValue);
				}else{
					ICpPopWordTemplateService popService = NCLocator.getInstance().lookup(ICpPopWordTemplateService.class);
					popService.open(sysVo.getPk_print_template(), freePrint,null,xmlValue);
				}
			}		
		} catch (TplBusinessException e) {
			CpLogger.error(e.getMessage(),e);
			throw new TplBusinessException(e.getMessage(),e);
		}
	}
	
	public void print(ICpFreeFormTemplatePrintService freePrint,String nodekey, String nodecode, String title,String xmlValue)
		throws TplBusinessException {
		//IPad环境中不支持打印
		DevicePhase devicePhase = LfwRuntimeEnvironment.getDevicePhase();
		if(!"PC".equals(devicePhase.getDevtype())){
			throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPrintTemplateServiceImpl-000002")/*不支持该设备打印*/);
		}
		//模板打印按钮触发
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		if (ctx == null) {
			return;
		}
		if(StringUtils.isEmpty(nodecode)){
			//获得节点id
			nodecode = (String) ctx.getApplicationContext().getAppAttribute(AppControlPlugin.NODECODE);
			if(null!=nodecode){
				LfwRuntimeEnvironment.getWebContext().getAppSession().addOriginalParameter(AppControlPlugin.NODECODE, nodecode);
			}
			if(null==nodecode){
				nodecode = LfwRuntimeEnvironment.getWebContext().getAppSession().getOriginalParameter(AppControlPlugin.NODECODE);
			}
		}
		ICpPrintTemplateInnerQryService service = NCLocator.getInstance()
				.lookup(ICpPrintTemplateInnerQryService.class);
		try {
			CpPrintTemplateVO[] ptVos = service.getPrintTemplates(nodecode,nodekey);
			CpPrintTemplateVO sysVo = service.getSysPrintTemplateVOPkByNode(nodecode, null);
			if(sysVo==null){
				throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
			}else{
				//多模板的时候，弹出选择框进行模板选择
				if(ptVos!=null && ptVos.length>1){
					ctx.getApplicationContext().addAppAttribute(IPrintTempalteConstants.PT_VOS, ptVos);
					ctx.getApplicationContext().addAppAttribute(IPrintTempalteConstants.TITLE, title);
					List<ICpFreeFormTemplatePrintService> list = new ArrayList<ICpFreeFormTemplatePrintService>();
					list.add(freePrint);
					ctx.getApplicationContext().addAppAttribute(IPrintTempalteConstants.FREEPRINTS, list.toArray(new ICpFreeFormTemplatePrintService[0]));
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put(IPrintTempalteConstants.MODEL, IPrintTempalteConstants.PUBLIC_VIEW_MODEL);
					OpenProperties openProperties = new OpenProperties();
					openProperties.setOpenId(IPrintTempalteConstants.PRINT_TEMPLATE);
					openProperties.setTitle(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "TemplatePubViewController-000000")/*模板选择*/);
					openProperties.setWidth(TemplatePubViewController.TEMPLATE_WIDTH);
					openProperties.setHeight(TemplatePubViewController.TEMPLATE_HEIGHT);
					openProperties.setParamMap(paramMap);
					ctx.getViewContext().navgateTo(openProperties);
//					ctx.getApplicationContext().dynNavgateTo(IPrintTempalteConstants.PRINT_TEMPLATE, NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "TemplatePubViewController-000000")/*模板选择*/, TemplatePubViewController.TEMPLATE_WIDTH, TemplatePubViewController.TEMPLATE_HEIGHT, paramMap);
				}else if(ptVos!=null && ptVos.length==1){
					ICpPopWordTemplateService popService = NCLocator.getInstance().lookup(ICpPopWordTemplateService.class);
					popService.open(ptVos[0].getPk_print_template(), freePrint,title,xmlValue);
				}else{
					ICpPopWordTemplateService popService = NCLocator.getInstance().lookup(ICpPopWordTemplateService.class);
					popService.open(sysVo.getPk_print_template(), freePrint,title,xmlValue);
				}
			}		
		} catch (TplBusinessException e) {
			CpLogger.error(e.getMessage(),e);
			throw new TplBusinessException(e.getMessage(),e);
		}
	}
	public static String generateLecture(String modelCode) throws Exception{
		ScapAnalyseUtil s = new ScapAnalyseUtil();
		//模板打印按钮触发
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		if (ctx == null) {
			throw new RuntimeException("模板处理类为空!无法生成");
		}
		String nodecode = (String) ctx.getApplicationContext().getAppAttribute(AppControlPlugin.NODECODE);
		ICpPrintTemplateInnerQryService service = NCLocator.getInstance().lookup(ICpPrintTemplateInnerQryService.class);
		
		//pos 2014-07-15 跟据编号来查找套打模板，编号是以年度来命名，如：“2014”；原来的方法不适用多个套打版本，它只会取得默认的模板。
//		CpPrintTemplateVO sysVo = service.getSysPrintTemplateVOPkByNode(nodecode, null);
		CpPrintTemplateVO sysVo = service.getTemplateVOByModelCode(modelCode);
		
		if(sysVo==null){
			ScapLogger.error(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
			//throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
		  return null;
		}
		String pk_file = sysVo.getPk_file();
		String path = s.getMerger(pk_file);
		File f = new File(path);
		FileInputStream in = new FileInputStream(f);
		//获得文件可读部分的总大小
		Long size = (long) in.available();
		FileManager mgr = FileManager.getSystemFileManager("bafile");
		String filename = f.getName();
		String nowpk_file = mgr.upload(filename, null, pk_file, size, in);
		ScapLogger.error("路径是："+f.getAbsolutePath()+"主键是"+nowpk_file);
		return nowpk_file;
	}
	public static String generateLecture(String modelCode,String filename) throws Exception{
		ScapAnalyseUtil s = new ScapAnalyseUtil();
		//模板打印按钮触发
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		if (ctx == null) {
			throw new RuntimeException("模板处理类为空!无法生成");
		}
		String nodecode = (String) ctx.getApplicationContext().getAppAttribute(AppControlPlugin.NODECODE);
		ICpPrintTemplateInnerQryService service = NCLocator.getInstance().lookup(ICpPrintTemplateInnerQryService.class);
		
		//pos 2014-07-15 跟据编号来查找套打模板，编号是以年度来命名，如：“2014”；原来的方法不适用多个套打版本，它只会取得默认的模板。
//		CpPrintTemplateVO sysVo = service.getSysPrintTemplateVOPkByNode(nodecode, null);
		CpPrintTemplateVO sysVo = service.getTemplateVOByModelCode(modelCode);
		
		if(sysVo==null){
			ScapLogger.error(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
			//throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
		  return null;
		}
		String pk_file = sysVo.getPk_file();
		String path = s.getMerger(pk_file);
		File f = new File(path);
		FileInputStream in = new FileInputStream(f);
		//获得文件可读部分的总大小
		Long size = (long) in.available();
		FileManager mgr = FileManager.getSystemFileManager("bafile");
		//String filename = f.getName();
		String nowpk_file = mgr.upload(filename, null, pk_file, size, in);
		return nowpk_file;
	}
	public static String generateLecture() throws Exception{
		ScapAnalyseUtil s = new ScapAnalyseUtil();
		//模板打印按钮触发
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		if (ctx == null) {
			throw new RuntimeException("模板处理类为空!无法生成");
		}
		String nodecode = (String) ctx.getApplicationContext().getAppAttribute(AppControlPlugin.NODECODE);
		ICpPrintTemplateInnerQryService service = NCLocator.getInstance().lookup(ICpPrintTemplateInnerQryService.class);
		
		
		CpPrintTemplateVO sysVo = service.getSysPrintTemplateVOPkByNode(nodecode, null);
		
		if(sysVo==null){
			throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("imp", "CpPrintTemplateServiceImpl-000000")/*系统打印模板未初始化*/);
		}
		String pk_file = sysVo.getPk_file();
		String path = s.getMerger(pk_file);
		File f = new File(path);
		FileInputStream in = new FileInputStream(f);
		//获得文件可读部分的总大小
		Long size = (long) in.available();
		FileManager mgr = FileManager.getSystemFileManager("bafile");
		String filename = f.getName();
		String nowpk_file = mgr.upload(filename, null, pk_file, size, in);
		return nowpk_file;
	}
}
