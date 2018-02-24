package nc.scap.pub.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.RuntimeEnv;
import nc.jdbc.framework.processor.MapProcessor;
import nc.scap.pub.itf.IScapFileConstants;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.lfw.util.JsURLDecoder;
import nc.uap.portal.log.ScapLogger;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Servlet(path = "/zip")
public class ZipDownloadAction extends BaseAction {

	private static MultipartResolver multipartResolver = new CommonsMultipartResolver();

	/**
	 * 执行目录压缩后并下载的方法
	 *
	 * @param srcDirPath
	 *            要压缩的目录 全路径
	 * @param targetDirPath
	 *            目标目录
	 * @param zipFileName
	 *            指定的压缩文件名
	 * srcDirPath 为必传参数，targetDirPath、zipFileName为可选择传入参数，如果这两个参数不传，以srcDirPath来生成，如果传入这两个参数传入的优先级高
	 */
	@Action(method = Keys.POST)
	public void download(
			@Param(name = IScapFileConstants.SrcDirPath) String srcDirPath,
			@Param(name = IScapFileConstants.TargetDirPath) String targetDirPath) {

		String zipFileName = "";
		String fileExt = ".zip";
		String encode = "GBK";
		if (srcDirPath == null || srcDirPath.length() <= 0) {
			ScapLogger.error("要压缩的目录srcDirPath不能为空");
			return;
		}
		try {
			srcDirPath = new String((srcDirPath).getBytes("iso-8859-1"), encode);
			if(srcDirPath.endsWith("\\")){
				srcDirPath = srcDirPath.substring(0, srcDirPath.length()-1);
			}
			if (!new File(srcDirPath).exists()) {
				ScapLogger.error("要压缩的目录不存在,请指定正确的目录(目录传入方式如:srcDirPath=D:\\DEMO)");
				return;
			}
			targetDirPath = new String((targetDirPath).getBytes("iso-8859-1"),encode);
			if (targetDirPath == null || targetDirPath.length() <= 0){
					targetDirPath = srcDirPath.substring(0,srcDirPath.lastIndexOf("\\"));
			}else{
				if(targetDirPath.endsWith("\\")){
					targetDirPath = targetDirPath.substring(0, targetDirPath.length()-1);
				}
			}
			if (!new File(targetDirPath).exists()) {
				new File(targetDirPath).mkdir();
			}
			zipFileName = srcDirPath.substring(srcDirPath.lastIndexOf("\\") + 1).equals("") ? "archive.zip": srcDirPath.substring(srcDirPath.lastIndexOf("\\") + 1)+ fileExt;
			if (request.getParameter(IScapFileConstants.ZipFileName) != null && !"".equals(request.getParameter(IScapFileConstants.ZipFileName))) {
				zipFileName = new String(request.getParameter(IScapFileConstants.ZipFileName).getBytes("iso-8859-1"),encode);
					if (zipFileName.lastIndexOf(".") == -1)
						zipFileName = zipFileName + fileExt;
			}
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			ScapLogger.error(e.getMessage());
		}

		String targetAllPathName = targetDirPath + File.separator + zipFileName;
		ZipOutputStream zs=null;
		try {
			 zs=new ZipOutputStream(new FileOutputStream(targetAllPathName));
			String pks= request.getParameter("pk_orgs");
			String istask= request.getParameter("istask");
			List<CpOrgVO> list=null;
			if(pks!=null){
				 list =(List<CpOrgVO>) ScapDAO.getBaseDAO().retrieveByClause(CpOrgVO.class, "pk_org in ("+pks+")");
			}
			if("Y".equals(istask)){
				ScapZipHelper.zipDir(new File(srcDirPath), zs, "",list,true);
			}else
			// 压缩文件
			ScapZipHelper.zipDir(new File(srcDirPath), zs, "");
		} catch (FileNotFoundException e) {
			ScapLogger.error("文件未找到"+e.getMessage());
		} catch (IOException e) {
			ScapLogger.error("io异常"+e.getMessage());
		} catch (DAOException e) {
			ScapLogger.error("查询异常"+e.getMessage());
		}finally{
			if(zs!=null){
				try {
					zs.close();
				} catch (IOException e) {
					ScapLogger.error(e.getMessage());
				}
			}
		}
//		try {
//			down(zipFileName); //继承FileSystemAction，重写down方法，定义自己的filemanager 重写download方法
//		} catch (IOException e) {
//			// TODO 自动生成的 catch 块
//			ScapLogger.error(e.getMessage());
//		}
		// 下载压缩后的文件，
		downloadFile(targetAllPathName, request, response);
		try {
			if (new File(targetAllPathName).exists()) {
				clearReadonly(targetAllPathName);
				System.gc();
				Runtime.getRuntime().exec("cmd /c del " + targetAllPathName);//下载后删除压缩文件
			}
		} catch (Exception e) {
			ScapLogger.error(e.getMessage());
		}
	}


	public static void clearReadonly(String absolutePath){
       try {
            Runtime.getRuntime().exec("cmd /c attrib "+absolutePath+" -r -a -s -h");
            System.out.println("文件: "+absolutePath+" 已去除只读属性！");
            try {
		        Thread.sleep(1000); //程序休眠1秒钟
		      } catch (InterruptedException e1) {
		    	  System.out.println("文件: "+absolutePath+" 去除只读属性失败！");
		       e1.printStackTrace();
		      }
        }catch(IOException e) {
            ScapLogger.error(e.getMessage());
        }
 }

	/**
	 * 该方法完成将服务器端文件保存到客户端的功能
	 *
	 * @param downloadDir
	 *            //要下载的zip文件完整文件名
	 * @param requset
	 *            //封装客户端请求的request对象
	 * @param response
	 *            //封装了响应内容的response对象
	 * @return //保存的结果，true成功，false失败
	 */
	private boolean downloadFile(String downloadDir,
			HttpServletRequest request, HttpServletResponse response) {
		boolean isSucessful = true; // 判断是否下载成功

		/* 截取文件路径中的文件名 */
		String fileName = downloadDir
				.substring(downloadDir.lastIndexOf("\\") + 1);
		FileInputStream downloadFile = null;

		try {
			downloadFile = new FileInputStream(downloadDir);
			// 设置头信息请求内容类型和长度
			// response.setContentType("application/octet-stream");
//			response.setContentType("application/x-msdownload");
			response.setContentType("application/zip;charset=GBK");
			response.setContentLength(downloadFile.available());
			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ new String(fileName.getBytes("GBK"), "ISO8859-1") + "\"");

//			response.setHeader("Pragma","No-cache");
//			response.setHeader("Cache-Control","no-cache");
//			response.setDateHeader("Expires", 0);
		} catch (UnsupportedEncodingException e1) {
			isSucessful = false;
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			ScapLogger.error(e.getMessage());
		} catch (IOException e) {
			ScapLogger.error(e.getMessage());
		}

		try {
			// downloadFile = new FileInputStream(downloadDir);
			int len;
			// 创建文件下载的缓冲区
			byte[] buffer = new byte[downloadFile.available()];
			// 获取文件下载的输出流
			ServletOutputStream out = response.getOutputStream();

			while ((len = downloadFile.read(buffer)) !=-1) {
				out.write(buffer, 0, len);
				out.flush();
			}

			out.close();
		} catch (IOException e) {
			System.out.println("faild");
			isSucessful = false;
			ScapLogger.error(e.getMessage());
		}  finally {
			try {
				if (downloadFile != null) {
					downloadFile.close();
				}
			} catch (Exception e2) {
				LfwLogger.error(e2.getMessage(),e2);
			}
		}

		return isSucessful;
	}

	/**
	 * 获得MultipartHttpServletRequest
	 *
	 * @return
	 * @throws MultipartException
	 */
	private static MultipartHttpServletRequest getMultipartResolver(
			HttpServletRequest request) throws MultipartException {
		((CommonsMultipartResolver) multipartResolver)
				.setDefaultEncoding("UTF-8");
		return multipartResolver.resolveMultipart(request);
	}
	/**
	 * scapan中根据主键下载库里面的xml,执行目录压缩后并下载的方法
	 * @param zipFileName 指定的压缩文件名
	 * pkfile 为必传参数，
	 */
	@Action(method = Keys.POST)
	public void downloadByIPk(
			@Param(name = IScapFileConstants.PKFILE) String pkfile) {

		String tableName = request.getParameter("tableName");
		String pkfield = request.getParameter("pkfield");

		String zipFileName = "";
		String fileExt = ".zip";
		String encode = "GBK";
		Map m =	(Map) ScapDAO.executeQuery("select xmlfile,xmlname from "+ tableName +" where "+ pkfield +"='"+pkfile+"'", new MapProcessor());
	    if(m == null)   return ;

		Object xmlfile= m.get("xmlfile");
	    String filename= (String) m.get("xmlname");
		zipFileName = filename == null ? "an.zip" : filename.substring(0,filename.lastIndexOf("."))+fileExt;
		String targetAllPathName =RuntimeEnv.getInstance().getNCHome() + IScapFileConstants.PortalFileStoreAN;

		File f=new File(targetAllPathName);
		if(!f.exists()){
			f.mkdirs();
		}
		targetAllPathName += zipFileName;
		ZipOutputStream zs = null;
		try {
			 zs=new ZipOutputStream(new FileOutputStream(targetAllPathName));
			 ScapZipHelper.addFile2Zip(xmlfile, filename, System.currentTimeMillis(), zs, "");
		} catch (FileNotFoundException e) {
			ScapLogger.error("文件未找到"+e.getMessage());
		} catch (IOException e) {
			ScapLogger.error("io异常"+e.getMessage());
		} finally{
			if(zs!=null){
				try {
					zs.close();
				} catch (IOException e) {
					ScapLogger.error(e.getMessage());
				}
			}
		}
		// 下载压缩后的文件，
		downloadFile(targetAllPathName, request, response);
		try {
			if (new File(targetAllPathName).exists()) {
				clearReadonly(targetAllPathName);
				System.gc();
				Runtime.getRuntime().exec("cmd /c del " + targetAllPathName);//下载后删除压缩文件
			}
		} catch (Exception e) {
			ScapLogger.error(e.getMessage());
		}
	}

	/**
	 * scapan中根据主键下载库里面的xml,执行目录压缩后并下载的方法
	 * @param zipFileName 指定的压缩文件名
	 * pkfile 为必传参数，
	 */
	@Action(method = Keys.POST)
	public void downloadExcel(
			@Param(name = IScapFileConstants.ZipFileName) String zipFileName) {
//		zipFileName = JsURLDecoder.decode(zipFileName, "GBK");
//		filename = request.getParameter(IScapFileConstants.ZipFileName);
		String pk_org= request.getParameter("pk_org");
		String vyear = request.getParameter("vyear");
		String flag = request.getParameter("flag");
		String name = "";
		if ("0".equals(flag)) {
			name = "年度计划";
		} else if ("1".equals(flag)){
			name = "年度完成情况";
		}
		/* 截取文件路径中的文件名 */
		zipFileName = ((CpOrgVO)ScapDAO.retrieveByPK(CpOrgVO.class, pk_org)).getName()+vyear+name + ".xls";
		String expNcPathName = LfwRuntimeEnvironment.getRealPath()+ "exportExcl\\"+zipFileName+"";
		FileInputStream downloadFile = null;

		try {
			downloadFile = new FileInputStream(expNcPathName);
			// 设置头信息请求内容类型和长度
			// response.setContentType("application/octet-stream");
//			response.setContentType("application/x-msdownload");
			response.setContentType("application/msexcel;charset=GBK");
			response.setContentLength(downloadFile.available());
			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ new String(zipFileName.getBytes("GBK"), "ISO8859-1") + "\"");

//			response.setHeader("Pragma","No-cache");
//			response.setHeader("Cache-Control","no-cache");
//			response.setDateHeader("Expires", 0);
		} catch (UnsupportedEncodingException e1) {
//			isSucessful = false;
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			ScapLogger.error(e.getMessage());
		} catch (IOException e) {
			ScapLogger.error(e.getMessage());
		}

		try {
			// downloadFile = new FileInputStream(downloadDir);
			int len;
			// 创建文件下载的缓冲区
			byte[] buffer = new byte[downloadFile.available()];
			// 获取文件下载的输出流
			ServletOutputStream out = response.getOutputStream();

			while ((len = downloadFile.read(buffer)) !=-1) {
				out.write(buffer, 0, len);
				out.flush();
			}

			out.close();
		} catch (IOException e) {
			System.out.println("faild");
//			isSucessful = false;
			ScapLogger.error(e.getMessage());
		}  finally {
			try {
				if (downloadFile != null) {
					downloadFile.close();
				}
			} catch (Exception e2) {
				LfwLogger.error(e2.getMessage(),e2);
			}
		}
//		String zipFileName = "";
//		String fileExt = ".zip";
//		String encode = "GBK";
//		Map m =	(Map) ScapDAO.executeQuery("select xmlfile,xmlname from "+ tableName +" where "+ pkfield +"='"+pkfile+"'", new MapProcessor());
//	    if(m == null)   return ;
//
//		Object xmlfile= m.get("xmlfile");
//	    String filename= (String) m.get("xmlname");
//		zipFileName = filename == null ? "an.zip" : filename.substring(0,filename.lastIndexOf("."))+fileExt;
//		String targetAllPathName =RuntimeEnv.getInstance().getNCHome() + IScapFileConstants.PortalFileStoreAN;
//
//		File f=new File(targetAllPathName);
//		if(!f.exists()){
//			f.mkdirs();
//		}
//		targetAllPathName += zipFileName;
//		ZipOutputStream zs = null;
//		try {
//			 zs=new ZipOutputStream(new FileOutputStream(targetAllPathName));
//			 ScapZipHelper.addFile2Zip(xmlfile, filename, System.currentTimeMillis(), zs, "");
//		} catch (FileNotFoundException e) {
//			ScapLogger.error("文件未找到"+e.getMessage());
//		} catch (IOException e) {
//			ScapLogger.error("io异常"+e.getMessage());
//		} finally{
//			if(zs!=null){
//				try {
//					zs.close();
//				} catch (IOException e) {
//					ScapLogger.error(e.getMessage());
//				}
//			}
//		}
		// 下载压缩后的文件，
		try {
			if (new File(expNcPathName).exists()) {
				clearReadonly(expNcPathName);
				System.gc();
				Runtime.getRuntime().exec("cmd /c del " + expNcPathName);//下载后删除压缩文件
			}
		} catch (Exception e) {
			ScapLogger.error(e.getMessage());
		}
	}





	/**
	 * 根据文件名称和全路径下载附件（用于帆软报表）
	 */
	@Action(method = Keys.POST)
	public void downloadAttachFile() {
		String filename = request.getParameter("filename"); //文件名称
	    String filepath = request.getParameter("filepath"); //文件下载全路径
	    filepath = JsURLDecoder.decode(filepath, "GBK");
		FileInputStream downloadFile = null;

		try {
			downloadFile = new FileInputStream(filepath);
			// 设置头信息请求内容类型和长度
			// response.setContentType("application/octet-stream");
//			response.setContentType("application/x-msdownload");
			response.setContentType("application/msexcel;charset=GBK");
			response.setContentLength(downloadFile.available());
			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ new String(filename.getBytes("GBK"), "ISO8859-1") + "\"");

//			response.setHeader("Pragma","No-cache");
//			response.setHeader("Cache-Control","no-cache");
//			response.setDateHeader("Expires", 0);
		} catch (UnsupportedEncodingException e1) {
//			isSucessful = false;
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			ScapLogger.error(e.getMessage());
		} catch (IOException e) {
			ScapLogger.error(e.getMessage());
		}

		try {
			// downloadFile = new FileInputStream(downloadDir);
			int len;
			// 创建文件下载的缓冲区
			byte[] buffer = new byte[downloadFile.available()];
			// 获取文件下载的输出流
			ServletOutputStream out = response.getOutputStream();

			while ((len = downloadFile.read(buffer)) !=-1) {
				out.write(buffer, 0, len);
				out.flush();
			}

			out.close();
		} catch (IOException e) {
			System.out.println("faild");
//			isSucessful = false;
			ScapLogger.error(e.getMessage());
		}  finally {
			try {
				if (downloadFile != null) {
					downloadFile.close();
				}
			} catch (Exception e2) {
				LfwLogger.error(e2.getMessage(),e2);
			}
		}
		// 下载压缩后的文件，
		try {
			if (new File(filepath).exists()) {
				clearReadonly(filepath);
				System.gc();
//				Runtime.getRuntime().exec("cmd /c del " + filepath);//下载后删除压缩文件
			}
		} catch (Exception e) {
			ScapLogger.error(e.getMessage());
		}
	}
	////继承FileSystemAction，重写down方法，定义自己的filemanager 重写download方法
//	/**
//	 * 下载文件
//	 *
//	 * @param id
//	 *            文件主键
//	 * @throws IOException
//	 */
//	@Action
//	public void down(String targetAllPathName) throws IOException {
//		response.addHeader("Cache-Control", "no-cache");
//		try {
//
//			DownFromServer(targetAllPathName);
//
//		} catch (Exception e) {
//			throw new LfwRuntimeException(LfwResBundle.getInstance()
//					.getStrByID("bc", "DocFileAction-000000")/* 文件下载失败 */, e);
//		}
//	}
//
//	public void DownFromServer(String targetAllPathName) throws Exception {
//
//		OutputStream out = null;
//		try {
//			response.addHeader("Content-Disposition", "attachment;filename=\""
//					+ new String(targetAllPathName.getBytes("GBK"), "ISO8859-1") + "\"");
//
//			out = response.getOutputStream();
//			getFileManager().download(targetAllPathName, out);
//			out.flush();
////			response.flushBuffer();
//		} finally {
//			IOUtils.closeQuietly(out);
//		}
//	}
//
//	/**
//	 * 获得文件管理
//	 *
//	 * @return
//	 */
//	public FileManager getFileManager() {
//		String sysid=IScapFileConstants.SYSID;
//		if (fileManager == null) {
//			if (StringUtils.isNotBlank(sysid))
//				fileManager = FileManager.getSystemFileManager(sysid);
////			if (null == fileManager)
////				fileManager = FileManager.getSystemFileManager();
//		}
//		fileManager.setBamodule(bamodule);
//		return fileManager;
//	}



	// /**
	// * 对要下载的目录进行压缩
	// *
	// * @param downloadDir
	// * 要压缩的目录，以web工程目录为根路径
	// * @param zipFileName
	// * 要生成的目标zip文件名,后缀名必须是.zip
	// * @return
	// */
	// private String packageDir(String realPath, String zipFileName) {
	//
	// // String realPath = null; //最终的输出文件名
	// File basePath = null; // 代表输出文件的根目录
	//
	// // 以输出的zip文件操作系统路径构造文件输出流
	// BufferedOutputStream bos = null;
	// try {
	// // realPath = this.getServletContext().getRealPath(downloadDir);
	// if (zipFileName == null) {
	// // 如果未指定文件名，则以目录名作为输出文件名
	// bos = new BufferedOutputStream(new FileOutputStream(realPath
	// + ".zip"));
	// } else {
	// bos = new BufferedOutputStream(new FileOutputStream(realPath
	// + File.separator + zipFileName));
	// }
	//
	// basePath = new File(realPath);
	//
	// try {
	// if (new File(realPath + File.separator + zipFileName).exists()) {
	// Runtime.getRuntime().exec(
	// "cmd /c delete" + realPath + File.separator
	// + zipFileName);
	// // System.out.println("del" + realPath + File.separator +
	// // zipFileName);
	// }
	// } catch (Exception e) {
	// ScapLogger.error(e.getMessage());
	// }
	// } catch (IOException e) {
	// return null;
	// }
	// // 构造输出zip文件的ZipOutputStream对象
	// ZipOutputStream zo = new ZipOutputStream(bos);
	//
	// /**
	// * 执行文件压缩，对子目录实行递归压缩，并要求输出空目录
	// */
	// if (!zip(realPath, basePath, zo, zipFileName, true, false)) {
	// return null;
	// }
	//
	// try {
	// // zo.closeEntry();
	// zo.close();
	// } catch (IOException e) {
	// ScapLogger.error(e.getMessage());
	// }
	//
	// return realPath + File.separator + zipFileName;
	// }
	//
	// /**
	// * 执行目录压缩的方法
	// *
	// * @param path
	// * 要压缩的目录或文件
	// * @param basePath
	// * 如果path为目录，则该参数为new File(path)对象实例，
	// * 作用是使输出的zip文件以此目录作为根目录；如果path为 文件，则该参数为null
	// * @param zipFileName
	// * 指定的压缩文件名
	// * @param isRecursive
	// * 是否递归压缩子目录，如果path为文件，给参数应为false
	// * @param isOutBlankDir
	// * 是否输出空目录。要输出空目录则basePath不为空
	// * @return isSucessful 是否压缩成功
	// */
	// private boolean zip(String path, File basePath, ZipOutputStream zo,
	// String zipFileName, boolean isRecursive, boolean isOutBlankDir) {
	//
	// boolean isSucessful = true; // 压缩操作是否成功
	//
	// File inFile = new File(path); // 创建要压缩的文件对象
	// File[] files = new File[0]; // 创建保存目录及子目录内容的File数组
	//
	// /**
	// * 如果inFile是目录，则返回目录内所有的内容 否则仅保存当前文件在文件数组内
	// */
	// if (inFile.isDirectory()) {
	// files = inFile.listFiles();
	// } else if (inFile.isFile()) {
	// files = new File[1];
	// files[0] = inFile;
	// }
	//
	// byte[] buffer = new byte[2048]; // 创建输出文件缓冲区
	// int len;
	//
	// /* 压缩文件或目录 */
	// for (File file : files) {
	// String pathName = null; // 被压缩文件的文件名
	// if (basePath != null) {
	// if (basePath.isDirectory()) { // 如果压缩的是目录
	// // 截取目录内当前被遍历文件的文件名
	// pathName = file.getPath().substring(
	// basePath.getPath().length() + 1);
	// } else { // 如果压缩的是文件
	// // 截取文件名
	// pathName = file.getPath().substring(
	// basePath.getParent().length() + 1);
	// }
	// } else {
	// // 获得文件名
	// pathName = file.getName();
	// }
	//
	// /* 如果是目录，则处理空目录并递归处理子目录，否则直接对文件进行打包压缩 */
	// if (file.isDirectory()) {
	// /* 压缩空目录 */
	// if (isOutBlankDir && basePath != null) {
	// try {
	// zo.putNextEntry(new ZipEntry(pathName + File.separator));
	// } catch (IOException e) {
	// ScapLogger.error(e.getMessage());
	// return false;
	// }
	// }
	// /* 递归处理子目录 */
	// if (isRecursive) {
	// zip(file.getPath(), basePath, zo, zipFileName, isRecursive,
	// isOutBlankDir);
	// }
	// } else {
	// FileInputStream fin;
	// try {
	// // 创建指向文件的输入流
	// fin = new FileInputStream(file);
	// // 添加压缩条目
	// if (!pathName.equals(zipFileName)) {
	// zo.putNextEntry(new ZipEntry(pathName));
	// }
	// /* 向压缩条目内写入文件内容 */
	// while ((len = fin.read(buffer)) > 0) {
	// zo.write(buffer, 0, len);
	// }
	// zo.closeEntry();
	// fin.close();
	// } catch (Exception e) {
	// ScapLogger.error(e.getMessage());
	// return false;
	// }
	// }
	// }
	// return isSucessful;
	// }
}
