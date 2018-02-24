package uap.bs.bap.fs.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.cpb.org.util.CpbUtil;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import uap.bs.bap.fs.storage.DiskFileStoreHandler;
import uap.bs.bap.fs.storage.IFileStorageHandler;
import uap.pub.bap.fs.FileStorageUtil;
import uap.pub.bap.fs.domain.FileHeader;
import uap.pub.bap.fs.domain.FileStorageException;
import uap.pub.bap.fs.domain.IFileStorageConst;
import uap.pub.bap.fs.domain.ext.BaFileStoreExt;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.perfwatch.PerfWatch;

/**
 * �ļ����䣨�ϴ������أ���servlet
 * 
 * @author chenfeic
 * 
 */

@SuppressWarnings("serial")
public class FileTransferRESTService extends FileStorageRESTService {

	private static final String SPLITER = "||";

	private static MultipartResolver multipartResolver = new CommonsMultipartResolver();;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		PerfWatch pw = new PerfWatch(
				"�ϴ������ļ�servlet FileTransferRESTService.doGet()");// ���ܻ��У����������˹����ܲ��ܹ���
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		pw.appendMessage("����ʱ��Ϊ�� " + dateFormat.format(new Date()));
		try {
			DiskFileStoreHandler handler = new DiskFileStoreHandler();
			String moduleId = getModuleId(req);
			String strBill = req.getParameter(IFileStorageConst.PAR_ISBILL);
			boolean isBill = Boolean.valueOf(strBill);
			pw.appendMessage("ģ��ţ�" + moduleId);
			if (isBill) {
				String billItem = req
						.getParameter(IFileStorageConst.PAR_BILLITEM);
				if (!StringUtils.isEmpty(billItem)) {
					FileHeader header = getHeaderByBill(handler, moduleId,
							billItem);
					// �� ����MINE���ٴ��������
					if (header != null) {
						addFileName(req, resp, false, header);
						pw.appendMessage("�ļ�PK:" + header.getPath());
					}
					downloadByBill(resp, handler, header);
				}
			} else {
				String strView = req.getParameter(IFileStorageConst.PAR_ISVIEW);
				boolean isView = Boolean.valueOf(strView);
				// ����MINE
				String filePath = getFileIdByURI(req);
				FileHeader fileHeader = handler.getDaoService()
						.getFileHeaderByPath(moduleId, filePath);
				if (fileHeader != null) {
					addFileName(req, resp, isView, fileHeader);
				}
				pw.appendMessage("�ļ�PK:" + filePath);
				super.doGet(req, resp);
			}
		} catch (Exception e) {
			AppDebug.error("�����ļ�����" + e);
			pw.appendMessage(e);
			throw new FileStorageException("Download file error! "
					+ e.getMessage());
		} finally {
			pw.stop();
		}

	}

	private void addFileName(HttpServletRequest req, HttpServletResponse resp,
			boolean isView, FileHeader fileHeader)
			throws UnsupportedEncodingException {
		if (StringUtils.isBlank(req.getHeader("Content-Disposition"))) {
			String fileType = fileHeader.getFileType();
			String contentType = FileTypeHelper.getContentType(fileType);
			if (StringUtils.isEmpty(contentType)) {
				contentType = "application/octet-stream";
			}
			String fileName = fileHeader.getFileName();
			if (!StringUtils.isEmpty(fileType)) {
				fileName = fileName + "." + fileType;
			}
			fileName = new String(fileName.getBytes(), "ISO-8859-1");
			resp.setContentType(contentType);
			if (!contentType.equals("application/pdf") || !isView) {
				resp.addHeader("Content-Disposition", (isView ? contentType
						: "attachment" + ";filename=" + fileName));
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String path = getModulePath(req);
		if (!path.startsWith(PATH_FILES)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		PerfWatch pw = new PerfWatch(
				"�ϴ������ļ�servlet FileTransferRESTService.doPost()");
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		pw.appendMessage("�ϴ�ʱ��Ϊ�� " + dateFormat.format(new Date()));

		InputStream input = null;
		// ObjectOutputStream oos = null;
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		try {
			DiskFileStoreHandler handler = new DiskFileStoreHandler();
			String moduleId = getModuleId(req);
			MultipartHttpServletRequest request = getMultipartResolver(req);
			Map<String, MultipartFile> fileMap = request.getFileMap();
			List<MultipartFile> files = new ArrayList<MultipartFile>();
			if (MapUtils.isNotEmpty(fileMap)) {
				files.addAll(fileMap.values());
			} else {
				throw new FileStorageException("The file is NULL");
			}
			String iscover = request
					.getParameter(IFileStorageConst.PAR_ISCOVER);
			String filepk = request.getParameter(IFileStorageConst.PAR_FILEPK);
			if (StringUtils.isEmpty(moduleId)) {
				moduleId = request.getParameter(IFileStorageConst.PARA_MODULE);
			}
			String currUser = request
					.getParameter(IFileStorageConst.ATTRIBUTE_CURR_USER);

			FileHeader fileHeader = null;
			if (!StringUtils.isEmpty(filepk)) {
				fileHeader = handler.getDaoService().getFileHeaderByPath(
						moduleId, filepk);
			}

			boolean override = iscover != null
					&& Boolean.TRUE.toString().equals(iscover);

			MultipartFile file = (MultipartFile) files.get(0);
			String fileName = file.getOriginalFilename();
			//--------start--У���ϴ����ļ�����,���Ϸ����ļ����Ͳ������ϴ�,��403����----------modify yhl 2015-06-29-------------------------------------------
			//���Ϸ����ļ�����Ĭ������  { "exe","js","bat","vbs","aspx","asp","cs","cmd","reg" }
			String fileType = getFileType(fileName);//�ļ�����
			if(!checkFileType(fileType)){
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			//--------end-------------------------------------------------------

			input = file.getInputStream();
			pw.appendMessage("�ϴ���Ϊ��" + FileStorageUtil.getCurrentUser(currUser));

			if (fileHeader != null) {
				// fileHeader.setFileSize((file.getSize()));
				// fileHeader.setName(fileName);
				// handler.getFileHeaderDao().updateFileHeader(fileHeader);//����Ԫ����
				if (!StringUtils.isEmpty(fileName)
						&& !fileName.equals(fileHeader.getName())) {
					handler.renameFile(moduleId, filepk, fileName, currUser);// �������ļ�
				}
				handler.updateFile(moduleId, filepk, currUser, input);// �����ļ�
				pw.appendMessage("�����ļ�����");
				pw.appendMessage("ģ���Ϊ��" + moduleId);
				pw.appendMessage("�ļ�PKΪ��" + filepk);
			} else {
				BaFileStoreExt ext = buildExts(request);
				pw.appendMessage("�ϴ��ļ�����");
				pw.appendMessage("ģ���Ϊ��" + moduleId);
				pw.appendMessage("�ļ���Ϊ��" + fileName);
				fileHeader = handler.writeFile(moduleId, fileName, input,
						override, currUser, ext);
				pw.appendMessage("�ļ�PKΪ��" + fileHeader.getPath());
			}

			String[] rtn = new String[] { fileHeader.getPath(), fileName,
					Long.valueOf(file.getSize()).toString(),
					FileStorageUtil.getFileType(fileName),
					new UFDateTime().toString(),
					FileStorageUtil.getCurrentUser(currUser), null };
			String join = StringUtils.join(rtn, SPLITER);

			// oos = new ObjectOutputStream(resp.getOutputStream());
			// oos.writeObject(join);
			//
			// if (oos != null) {
			// oos.flush();
			// }

			out.append(join);
			out.flush();

			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			AppDebug.error("�������" + e);
			pw.appendMessage(e);
			throw new FileStorageException("upload file error:"
					+ e.getMessage());
		} finally {
			closeInputStream(input);
			out.close();
			pw.stop();
		}
	}

	private BaFileStoreExt buildExts(MultipartHttpServletRequest request) {
		String billType = getParameter(request, IFileStorageConst.PAR_BILLTYPE);
		String billItem = getParameter(request, IFileStorageConst.PAR_BILLITEM);
		String group = getParameter(request, "group");
		String category = getParameter(request, "category");
		String createstatus = getParameter(request, "createstatus");
		String ext1 = getParameter(request, "ext1");
		String ext2 = getParameter(request, "ext2");
		String ext3 = getParameter(request, "ext3");
		String ext4 = getParameter(request, "ext4");
		String ext5 = getParameter(request, "ext5");
		BaFileStoreExt ext = new BaFileStoreExt();
		ext.setPk_billitem(billItem);
		ext.setPk_billtype(billType);
		ext.setCategory(category);
		ext.setPk_group(group);
		ext.setCreatestatus(createstatus);
		ext.setExt1(ext1);
		ext.setExt2(ext2);
		ext.setExt3(ext3);
		ext.setExt4(ext4);
		ext.setExt5(ext5);
		return ext;
	}

	private String getParameter(MultipartHttpServletRequest request,
			String param) {
		String value = request.getParameter(param);
		if (value == null) {
			value = "";
		}
		return value;
	}

	private FileHeader getHeaderByBill(IFileStorageHandler handler,
			String moduleId, String billItem) {
		BaFileStoreExt ext = new BaFileStoreExt();
		ext.setPk_billitem(billItem);
		FileHeader fileHeader = null;
		FileHeader[] fileHeaders = handler.queryHeaders(moduleId, ext);
		if (ArrayUtils.isEmpty(fileHeaders)) {
			return null;
		} else {
			fileHeader = fileHeaders[0];
		}
		return fileHeader;
	}

	private void downloadByBill(HttpServletResponse resp,
			IFileStorageHandler handler, FileHeader fileHeader) {
		if (fileHeader == null) {
			return;
		}
		String filePath = fileHeader.getPath();
		InputStream in = handler.readFile(fileHeader.getModule(),
				fileHeader.getPath());
		if (in == null) {// �յ��ļ���
			return;
		}
		ServletOutputStream outputStream = null;
		try {
			outputStream = resp.getOutputStream();
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			if (outputStream != null) {
				outputStream.flush();
			}
		} catch (IOException e) {
			AppDebug.error("GET�������" + e);
			throw new FileStorageException("Get request error:"
					+ e.getMessage());
		} finally {
			closeOutputStream(outputStream);
			closeInputStream(in);
		}

	}

	private String getModuleId(HttpServletRequest req) {
		String path = getRelativePath(req);
		int index = path.indexOf(PATH_FILES);
		if (index == 0) {
			throw new FileStorageException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001006_0", "08001006-0325")/*
																			 * @res
																			 * "ģ���Ϊ��"
																			 */);
		}
		String module = path.substring(1, index);
		if (IFileStorageConst.DEFAULT_MODULE.equals(module)) {
			module = "";
		}
		return module;
	}

	/**
	 * ��ȡ�ļ���PK��filePath��---������get��ʽ
	 * 
	 * @param req
	 * @return
	 */
	protected String getFileIdByURI(HttpServletRequest req) {
		String path = getRelativePath(req);
		int index = path.indexOf(PATH_FILES);
		String noModelPath = path.substring(index);// ����ģ��ŵ�·��
		String fileId = null;// �ļ��߼�·��
		int filePos = noModelPath.substring(1).indexOf("/");
		if (filePos != -1) {
			fileId = noModelPath.substring(filePos + 2); // ͨ��uri����ֱ�ӵõ�·��
		}
		return fileId;
	}

	private static MultipartHttpServletRequest getMultipartResolver(
			HttpServletRequest request) throws MultipartException {

		((CommonsMultipartResolver) multipartResolver)
				.setDefaultEncoding("UTF-8");

		return multipartResolver.resolveMultipart(request);
	}
	
	/*
	 * У���ϴ��ļ����ͣ����Ϸ����ļ����Ͳ������ϴ��� ���Ϸ����ļ���������{ "exe","js","bat","vbs","aspx","asp","cs","cmd","reg" }
	 * ����ǲ��Ϸ����ļ����ͷ���false,���򷵻�true
	 */
	private boolean checkFileType(String fileType) {
		String[] filetypes = new String[] { "exe","js","bat","vbs","aspx","asp","cs","cmd","reg" };
		if (CpbUtil.joinQryArrays(filetypes).indexOf(fileType.toLowerCase()) != -1) {
			return false;
		} else {
			return true;
		}

	}

	/*
	 * ͨ���ļ�����ȡ�ļ�����
	 */
	 public String getFileType(String fileName)
	    {
	      String filetypo = "NaN";
	    int beginIndex = fileName.lastIndexOf(".");
	     if (beginIndex > 0) {
	        filetypo = fileName.substring(beginIndex + 1);
	     }
	     return filetypo;
	   }
}
