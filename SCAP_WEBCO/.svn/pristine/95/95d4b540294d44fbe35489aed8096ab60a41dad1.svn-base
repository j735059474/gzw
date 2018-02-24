package nc.scap.pub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import nc.uap.cpb.log.CpLogger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.file.FileManager;
import nc.uap.oba.word.merger.AbstractDocumentMerger;
import nc.uap.oba.word.merger.ITemplateEntity;
import nc.uap.oba.word.merger.ImportSerializerSeracher;
import nc.uap.oba.word.merger.impl.DataSourceInjection;
import nc.uap.oba.word.merger.impl.DocumentMerger;
import nc.uap.oba.word.merger.impl.IComponentSerializer;
import nc.uap.oba.word.merger.model.node.DataSource;
import nc.uap.oba.word.merger.util.DocumentHelper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import uap.lfw.imp.print.args.IPrintXmlArgs;

public class WordFileOpenUtil {
	public static void openWord(String id, OutputStream out){
		String[] Strs = id.split(",");
		String pk_file = Strs[0];
		//zll 2014-11-28 xml(wordXml文件夹)路径加一层WEB-INF
		String path = LfwRuntimeEnvironment.getRealPath()+"/WEB-INF/wordXml/default.xml";
		OutputStream out_tmp = null;
		String realPath =LfwRuntimeEnvironment.getRealPath();
		String tmp_root = realPath + IPrintXmlArgs.WORDTEMP_DIR+ "/";
		String update_path = tmp_root + UUID.randomUUID().toString()+ ".docx";
		String word_root =  realPath + "/wordformal";
		try{
			boolean b = FileManager.getSystemFileManager("bafile").existInFs(pk_file);
			if(!b){
				File f = new File(word_root);
				if(f.isDirectory()){
					String word_path = word_root + "/tmp_word.docx";
					File file = new File(word_path);
					InputStream ins = new FileInputStream(file);
					FileManager.getSystemFileManager("bafile").ReUpload(pk_file, 0, ins);
					IOUtils.closeQuietly(ins);
				}
			}
			File tmp = new File(tmp_root);
			if(!tmp.isDirectory()){
				tmp.mkdirs();
			}
			//将设计的word模板下载到一个临时文件中
			String tmp_path = tmp_root + UUID.randomUUID().toString() + ".docx";
			out_tmp = new FileOutputStream(tmp_path);
			FileManager.getSystemFileManager("bafile").download(pk_file,out_tmp);
			AbstractDocumentMerger merger = new DocumentMerger();
			ITemplateEntity ite = merger.loadTemplate(tmp_path);
			ite.setOutputFilePath(update_path);
			boolean update = merger.update(ite);
			if (update && ite != null) {	
				tmp_path = update_path;
			}
			merger = new DataSourceInjection();
			ite = merger.loadTemplate(tmp_path);
			if(ite != null){
				ite.setOutputStream(out);
				Document document = DocumentHelper.load(path);
				String nameSpace = document.getDocumentElement().getNamespaceURI();
				ImportSerializerSeracher searcher = new ImportSerializerSeracher(ite.getSerializerRegistry());
				IComponentSerializer serializer = searcher.search(nameSpace);
				if (serializer != null) {
					if(ite.getWTModel().getDataSourceCollection().contains("Root")){
						ite.getWTModel().getDataSourceCollection().remove("Root");
					}
					DataSource dataSource = (DataSource) serializer.deserialize(document);
					dataSource.setName("Root");
					ite.getWTModel().getDataSourceCollection().append(dataSource);
				}
				merger.merging(ite);
			}
		}catch(IOException e){
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}finally{
			if(out_tmp!=null){
				try {
					out_tmp.flush();
					out_tmp.close();
				} catch (IOException e) {
					CpLogger.error(e.getMessage(), e);
					throw new LfwRuntimeException(e.getMessage(), e);
				}
			}
			deleteFileDir(tmp_root);
		}
	}
	/**
	 * 删除文件路径及其包含的文件
	 * @param dir
	 */
	public static void deleteFileDir(String dir){
		if(StringUtils.isEmpty(dir)){
			return;
		}
		int index = dir.lastIndexOf("/");
		if(index!=-1){
			dir = dir.substring(0, index);
		}else{
			return;
		}
		File f = new File(dir);
		if(f.isDirectory()){
			File[] files = f.listFiles();
			if(files!=null && files.length>0){
				for(File file :files){
					file.delete();
				}
			}
			f.delete();
		}
	}
}
