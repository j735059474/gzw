package nc.modulemap.toolkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Toolkit {
	private static final String cs = "UTF-8";
	public static Properties loadUserOccupyModuleMap(File file){
		Properties prop  = new Properties();
		String text = loadUserOccupyModuleMapString(file);
		if(text != null){
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes(cs));
				prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return prop;
	}
	public static String  loadUserOccupyModuleMapString(File file){
		if(!file.exists()){
			return null;
		}
		String text = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = fin.read(buf))!= -1) {
				baos.write(buf, 0, len);
			}
			buf = Encode.decode(baos.toByteArray());
	        text = new String(buf,cs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(fin != null){
				try {
					fin.close();
				} catch (IOException e) {}
			}
		}
		return text;
	}
	public static void storeUserOccupyModuleMapString(File file , String text) throws Exception{
		byte[] buf = text.getBytes(cs);
		buf = Encode.encode(buf);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(buf);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(fos != null){
				fos.close();
			}
		}
		
	}
}
