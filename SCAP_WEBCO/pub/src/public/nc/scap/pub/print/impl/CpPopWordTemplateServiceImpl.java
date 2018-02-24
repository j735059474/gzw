package nc.scap.pub.print.impl;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.exp.TplBusinessException;
import nc.uap.ctrl.tpl.gz.meta.CandidateMetaUtils;
import nc.scap.pub.print.ICpPopWordTemplateService;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateInnerQryService;
import nc.uap.ctrl.tpl.print.ICpPrintXmlService;
import nc.uap.ctrl.tpl.print.TemplateConstantArgs;
import nc.uap.ctrl.tpl.print.base.CpPrintConditionVO;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateTotalVO;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateVO;
import nc.uap.ctrl.tpl.print.init.CpBarCoder;
import nc.uap.ctrl.tpl.print.init.CpPrintTemplateUtils;
import nc.uap.ctrl.tpl.print.init.ICPPrintExtendService;
import nc.uap.ctrl.tpl.print.init.ICpFreeFormTemplatePrintService;
import nc.uap.ctrl.tpl.print.init.IDefaultTempalteConst;
import nc.uap.ctrl.tpl.print.init.ImageUtils;
import nc.uap.ctrl.tpl.print.init.PrintAbstractService;
import nc.uap.ctrl.tpl.qry.base.QuerySchemeUtils;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.formular.LfwFormulaParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import uap.lfw.imp.math.IPrintConditonDisplay;
import uap.lfw.imp.print.args.IPrintXmlArgs;
import uap.lfw.imp.print.base.PrintConditionFieldNameImpl;
import uap.lfw.imp.print.base.PrintPdfHelper;
import uap.lfw.imp.print.base.PrintSysVarManager;

public class CpPopWordTemplateServiceImpl implements ICpPopWordTemplateService {

	@Override
	public void open(String pk_template,ICpFreeFormTemplatePrintService freePrint,String title,String xmlValue) throws TplBusinessException {
		//在模板选择后打开officeControl控件，然后生产xml文件，并将发送一个url请求
		ICpPrintTemplateInnerQryService service = NCLocator.getInstance().lookup(ICpPrintTemplateInnerQryService.class);
		ICpPrintXmlService printXml = NCLocator.getInstance().lookup(ICpPrintXmlService.class);
		IPrintConditonDisplay displayService = new PrintConditionFieldNameImpl();
		CpPrintTemplateVO printVo = null;
		CpPrintTemplateVO sysPrintVo = null;
		String metaClass = null;
		CpPrintConditionVO[] vos = null;
		CpPrintTemplateTotalVO totalVo = null;
		CpPrintTemplateTotalVO sys_totalVo = null;
		String fileName = null;
		String path = null;
		String pk_file = null;
		String printUrl = null;
		String printPk = null;
		String combineUrl = null;
		try{
			String rootPath = LfwRuntimeEnvironment.getRealPath();
			totalVo = service.getPrintTotalVO(pk_template);
			printVo = (CpPrintTemplateVO) totalVo.getParentVO();
			//PrintAbstractService.images.clear();
			if (null != printVo) {
				pk_file = printVo.getPk_file();
			}
			if(CandidateMetaUtils.isEmpty(pk_file)){
				throw new TplBusinessException(NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPopWordTemplateServiceImpl-000001")/*没有上传模板*/);
			}
			
			if(!printVo.getIssys().booleanValue()){
				String nodeCode = printVo.getNodecode();
				String pk_print_systemplate = service.getSysPrintTemplatePkByNode(nodeCode, null);
				if(StringUtils.isNotEmpty(pk_print_systemplate)){
					sys_totalVo = service.getPrintTotalVO(pk_print_systemplate);
					if(sys_totalVo != null){
						vos = (CpPrintConditionVO[]) sys_totalVo.getChildrenVO();
						sysPrintVo = (CpPrintTemplateVO) sys_totalVo.getParentVO();
						metaClass = sysPrintVo.getMetaclass();
					}
				}
			}else{
				vos = (CpPrintConditionVO[]) totalVo.getChildrenVO();
			}
			String root = "Root";
			List<String[]> mainList = new ArrayList<String[]>();
			List<String[]> systemList = new ArrayList<String[]>();
			List<String[]> varExpressList = new ArrayList<String[]>();
			List<String> allList = new ArrayList<String>();
			boolean is_display_history = printVo.getIs_flowhistory_display()==null?false:printVo.getIs_flowhistory_display().booleanValue();
			if(freePrint!=null){
				if(freePrint instanceof ICPPrintExtendService){
					((ICPPrintExtendService) freePrint).setpk_template(pk_template);
					((ICPPrintExtendService) freePrint).setPrintConditions(vos);
				}
				String[] printUrls = freePrint.getValue(IDefaultTempalteConst.PRINT_URL, null);
				String[] printPks = freePrint.getValue(IDefaultTempalteConst.PRINT_PK, null);
				String[] combineUrls = freePrint.getValue(IDefaultTempalteConst.COMBINE_URL, null);
				if(printUrls!=null && printUrls.length>0){
					printUrl = printUrls[0];
				}
				if(printPks!=null && printPks.length>0){
					printPk = printPks[0];
				}
				if(combineUrls != null && combineUrls.length > 0){
					combineUrl = combineUrls[0];
				}
				allList = freePrint.getAllFields();
				boolean isNull = false;
				if(allList==null || allList.size()==0){
					isNull = true;
					allList = new ArrayList<String>();
				}
				for (CpPrintConditionVO vo : vos) {
					String tableType = vo.getTabletype();
					String[] field_value = new String[4];
					String varExpress = vo.getVarexpress();
					if(!is_display_history){
						if(varExpress.lastIndexOf(TemplateConstantArgs.FLOW_HISTORY_FH)!=-1){
							continue;
						}
					}else{
						if(varExpress.lastIndexOf(TemplateConstantArgs.FLOW_HISTORY_FH)!=-1){
							allList.add(varExpress);
						}
					}
					String consultCode = vo.getConsultcode();
					boolean userDefflag = vo.getUserdefflag()==null?false:vo.getUserdefflag().booleanValue();
					int datatype = vo.getDatatype();
					String fieldVlaue = null;
					boolean isDefine = false;
					if(isNull){
						allList.add(varExpress);
					}
					if(!StringUtils.isEmpty(consultCode)){
						isDefine = true;
					}
					String varName = vo.getVarname();
					String resId = vo.getResid();
					String langDir = vo.getLangdir();
					if(StringUtils.isNotBlank(varName) && varName.lastIndexOf(".") != -1){
						varName = varName.substring(varName.lastIndexOf(".") + 1);
					}
					varName = QuerySchemeUtils.translate(resId, varName, langDir);
					if(TemplateConstantArgs.MASTER.equals(tableType) || TemplateConstantArgs.DETAIL.equals(tableType)){
						field_value[0] = varExpress;
						int sp1 = varExpress.indexOf(".");
						String pId = varExpress;
						if(sp1 != -1){
							pId = varExpress.substring(0, sp1);
						}
						varName = displayService.getFieldName(metaClass, varExpress, varName, pId)[0];
						field_value[1] = varName;
						boolean isLive = isNull;
						if(!isNull){
							isLive = isLive(varExpress,allList);
						}
						String[] values = null;
						if(isLive){
							String formulaExp = vo.getFormula();
							values = freePrint.getValue(varExpress,formulaExp);
							if(StringUtils.isNotBlank(formulaExp)){
								values = LfwFormulaParser.getResult(formulaExp, values, varExpress);
							}
						}
						String valueStr = "";
						if(ImageUtils.IMAGE.equalsIgnoreCase(consultCode) && values ==null){
							values = new String[]{"","1"};
						}
						if(values!=null && values.length>0){
							for(int index=0;index<values.length;index++){
								if(index==0){
									String titleName = values[0];
									if(CandidateMetaUtils.isEmpty(titleName)){
										continue;
									}
								}
								String value = values[index];
								if(userDefflag){
									if(isDefine && !StringUtils.isEmpty(value)){
										try{
											if(datatype == 5){
												value = CpPrintTemplateUtils.getRefName(value, consultCode);
											}else if(datatype == 6){
												value = CpPrintTemplateUtils.getComboData(value, consultCode);
											}
										}catch(Throwable e){
											CpLogger.error(e.getMessage(),e);
											throw new LfwRuntimeException("Parse value error,value is : " + value);
										}
										
										if(StringUtils.isEmpty(value)){
											value = values[index];
										}
									}
								}
								if(ImageUtils.BARCODE.equalsIgnoreCase(consultCode) || ImageUtils.D_BARCODE.equalsIgnoreCase(consultCode) || ImageUtils.IMAGE.equalsIgnoreCase(consultCode)){
									byte[] bytes = null;
									String format = "png";
									if(freePrint instanceof ICPPrintExtendService){
										bytes = ((ICPPrintExtendService) freePrint).getImage(varExpress,value,consultCode);
									}else{
										if(ImageUtils.BARCODE.equalsIgnoreCase(consultCode) && StringUtils.isNotEmpty(value)){ //条形码
											CpBarCoder barCode = CpBarCoder.createBarCode(CpBarCoder.TYPE_CODE128);
											Image image = barCode.getBarCode(value);
											if(image!=null){
												bytes = ImageUtils.getBytesByImage(image, format);
											}
										}else if(ImageUtils.D_BARCODE.equalsIgnoreCase(consultCode) && StringUtils.isNotEmpty(value)){  //二维码
											CpBarCoder barCode = CpBarCoder.createBarCode(CpBarCoder.TYPE_PDF417);
											Image image = barCode.getBarCode(value);
											if(image!=null){
												bytes = ImageUtils.getBytesByImage(image, format);
											}
										}
									}
									if(bytes!=null){
										//PrintAbstractService.images.put(varExpress, bytes);
									}
								}
								valueStr += value + "&&,";
							}
							int sp = valueStr.lastIndexOf("&&,");
							if(sp!=-1){
								valueStr = valueStr.substring(0,sp);	
							}
						}
						fieldVlaue = valueStr;
						field_value[2] = fieldVlaue==null?"":fieldVlaue;
						field_value[3] = vo.getTabcode();
						if(fieldVlaue!=null && isLive){
							mainList.add(field_value);
						}else{
							allList.remove(varExpress);
						}
					}else if(TemplateConstantArgs.SYSTEM.equals(tableType) || TemplateConstantArgs.FORMULA.equals(tableType)){
						String express = vo.getVarexpress();
						field_value[0] = express;
						varName = PrintSysVarManager.getDisplayName(express, varName);
						field_value[1] = varName;
						field_value[2] = "";
						systemList.add(field_value);
						boolean isLive = isLive(express,allList);
						if(!isLive){
							allList.add(express);
						}
					}
				}
			}
			
			fileName = UUID.randomUUID().toString() + IPrintXmlArgs.XML_FILE_FLAG;
			String tmp_root = rootPath + IPrintXmlArgs.WORDTEMP_DIR;
			createDirectory(tmp_root);
			path = tmp_root + "/" + fileName;
			//生成xml文件
			printXml.createPrintXml(path,true);
			printXml.setRootElementName(root);
			printXml.setBodyElement(mainList,systemList,varExpressList);
			printXml.startWritePrintXml(false,false);
			boolean isPrintPDF = PrintPdfHelper.isPrintPdf();
			//打开officeControl控件，发送一个url请求
			String openurl = LfwRuntimeEnvironment.getRootPath()
					+ "/pt/word/open/wdown?id=" + pk_file;
			if(StringUtils.isEmpty(title) || "null".equals(title)){
				title = NCLangResOnserver.getInstance().getStrByID("uapprinttemplate", "CpPopWordTemplateServiceImpl-000000")/*模板打开*/;
			}
			if(isPrintPDF){
				String js = " window.open('"+openurl+"')";
				AppLifeCycleContext.current().getApplicationContext().addExecScript(js);
			}else{
				String url = LfwRuntimeEnvironment.getRootPath()
						+ "/core/word.jsp?pageId=officeedit&openurl=" + openurl
						+ "&canopen=true&noieopen=true"; //&readonly=true
				if(StringUtils.isNotEmpty(printPk)){
					url += "&combineids=" + printPk;
				}
				if(StringUtils.isNotEmpty(combineUrl)){
					url += "&combineurl=" + combineUrl;
				}
				if(StringUtils.isNotEmpty(printUrl)){
					url += "&printurls="+ printUrl;
				}
				
				AppLifeCycleContext.current().getApplicationContext()
						.popOuterWindow(url, title, "900", "800",ApplicationContext.TYPE_DIALOG);
			}
			
		}catch (TplBusinessException e) {
			CpLogger.error(e.getMessage(),e);
			throw new TplBusinessException(e.getMessage(),e);
		}
	}
	
	private void createDirectory(String temp){
		File tmp = new File(temp);
		try{
			//先清除目录下的文件
			FileUtils.deleteDirectory(tmp);
			//然后新建一个空目录
			if(!tmp.isDirectory()){
				tmp.mkdirs();
			}
		}catch(Exception e){
			CpLogger.error(e.getMessage(), e);
		}
	}
	
	
	private boolean isLive(String varExpress,List<String> list){
		if(list!=null && list.size()>0){
			Iterator<String> it = list.iterator();
			while(it.hasNext()){
				String value = it.next();
				if(value!=null && value.equals(varExpress)){
					return true;
				}
			}
		}	
		return false;
	}
}
