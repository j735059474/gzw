 package nc.scap.pub.zip;
 
 import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.OrgVO;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
 
 public class ScapZipHelper
 {
   public static final int BUFFER_SIZE = 2156;
   private static final char SEP = 47;
 
   public void zipDir(File dirOrFile2Zip, File destZip)
     throws IOException
   {
     if ((dirOrFile2Zip == null) || (destZip == null))
       throw new IllegalArgumentException();
     FileOutputStream fout = new FileOutputStream(destZip);
     try
     {
       zipDir(dirOrFile2Zip, fout);
     }
     catch (IOException ioe)
     {
     }
     finally
     {
       fout.close();
     }
   }
 
   public static void zipDir(File dirOrFile2Zip, OutputStream out) throws IOException
   {
     ZipOutputStream jout = new ZipOutputStream(out);
     try
     {
       zipDir(dirOrFile2Zip, jout, null);
     }
     catch (IOException ioe)
     {
     }
     finally
     {
       jout.close();
     }
   }
 
   public static void zipDir(File dirOrFile2zip, ZipOutputStream zos, String prefix)
     throws IOException
   {
     if (!(dirOrFile2zip.isDirectory()))
     {
    	 addFile2Zip(dirOrFile2zip, zos, prefix);
    	 return;
     }
 
     File[] subFiles = dirOrFile2zip.listFiles();
     if (subFiles == null)
       return;
     for (File subfile : subFiles)
     {
       if (subfile.isDirectory())
       {
         zipDir(subfile, zos, prefix + subfile.getName() + '/');
       }
       else
       {
         addFile2Zip(subfile, zos, prefix);
       }
     }
    // zos.close();//防止winrar打不开压缩后的文件
   }
   public static void zipDir(File dirOrFile2zip, ZipOutputStream zos, String prefix,List<CpOrgVO> names,boolean tag)
		     throws IOException
		   {
		     if (!(dirOrFile2zip.isDirectory()))
		     {
		    	 addFile2Zip(dirOrFile2zip, zos, prefix);
		       return;
		     }
		 
		     File[] subFiles = dirOrFile2zip.listFiles();
		     if (subFiles == null)
		       return;
		     for (File subfile : subFiles)
		     {
		       if (subfile.isDirectory())
		       {
		    	   if( existsName(names,subfile.getName())&&tag){
		            zipDir(subfile, zos, prefix + subfile.getName() + '/',names,tag);
		    	   }
		       }
		       else
		       {
		    	  // if(existsName(names,subfile.getName())){
		    		   addFile2Zip(subfile, zos, prefix); 
		    	  // }
		        
		       }
		     }
		    // zos.close();//防止winrar打不开压缩后的文件
		   }
   
   private static  boolean existsName(List<CpOrgVO> names,String name){
	   boolean flag=false;
	   if(names!=null&&names.size()>0){
		   for (CpOrgVO cpOrgVO : names) {
			   if(cpOrgVO.getName().equals(name)){
				   flag= true;
			   }
		} 
	   }else{
		   return true;
	   }
	   return flag;
   }
   public static void addFile2Zip(File dirOrFile2zip, ZipOutputStream zos, String prefix)
     throws FileNotFoundException, IOException
   {
     FileInputStream fis = null;
     try
     {
       fis = new FileInputStream(dirOrFile2zip);
 
       String eName = dirOrFile2zip.getName();
       addFile2Zip(fis, eName, dirOrFile2zip.lastModified(), zos, prefix);
     }
     catch (IOException ioe)
     {
     }
     finally
     {
       if (fis != null)
         fis.close();
     }
   }
 
   public static void addFile2Zip(File dirOrFile2zip, String name, ZipOutputStream zos, String prefix)
     throws IOException
   {
     FileInputStream fis = null;
     try
     {
       fis = new FileInputStream(dirOrFile2zip);
       addFile2Zip(fis, name, dirOrFile2zip.lastModified(), zos, prefix);
     }
     catch (IOException ioe)
     {
     }
     finally
     {
       if (fis != null)
         fis.close();
     }
   }
 
   public static void addFile2Zip(byte[] bs, String name, long lastModified, ZipOutputStream zos, String prefix) throws IOException
   {
     if (bs == null)
     {
       throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapadp", "0uapadp0135"));
     }
     ByteArrayInputStream input = new ByteArrayInputStream(bs);
     addFile2Zip(input, name, lastModified, zos, prefix);
     input.close();
   }
 
   public static void addFile2Zip(InputStream input, String name, long lastModified, ZipOutputStream zos, String prefix) throws IOException
   {
     byte[] mBuffer = new byte[2156];
     int mByteCount = 0;
     ZipEntry entry = new ZipEntry(prefix + name);
     entry.setTime(lastModified);
     zos.putNextEntry(entry);
 
     while ((mByteCount = input.read(mBuffer)) != -1)
     {
       zos.write(mBuffer, 0, mByteCount);
     }
     zos.flush();
     zos.closeEntry();
   }
 
   public static void addFile2Zip(Object obj, String name, long lastModified, ZipOutputStream zos, String prefix) throws IOException
   {
     if (obj == null)
       return;
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutputStream oos = new ObjectOutputStream(baos);
     oos.writeObject(obj);
     baos.close();
     oos.close();
     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
     addFile2Zip(bais, name, lastModified, zos, prefix);
     bais.close();
   }
 
//   public static void addFile2Zip(Properties obj, String name, long lastModified, ZipOutputStream zos, String prefix) throws IOException
//   {
//     if (obj == null)
//       return;
//     ByteArrayOutputStream baos = new ByteArrayOutputStream();
//     obj.store(baos, "");
//     baos.close();
//     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//     addFile2Zip(bais, name, lastModified, zos, prefix);
//     bais.close();
//   }
// 
//   public static void copyZipStream(ZipOutputStream zos, ZipInputStream zis)
//     throws IOException
//   {
//     copyZipStream(zos, zis, new String[0]);
//   }
 
//   public static void copyZipStream(ZipOutputStream zos, ZipInputStream zis, String[] skipNames)
//     throws IOException
//   {
//     ZipEntry ze = null;
//     while ((ze = zis.getNextEntry()) != null)
//     {
//       String fname = ze.getName();
//       if (skipNames != null)
//       {
//         for (String skipName : skipNames)
//         {
//           if (!(fname.equals(skipName)))
//             continue;
//           zis.closeEntry();
//         }
// 
//       }
// 
//       byte[] buffer = new byte[2156];
//       int length = 0;
//       ZipEntry e = new ZipEntry(fname);
//       e.setTime(ze.getTime());
//       zos.putNextEntry(e);
//       while ((length = zis.read(buffer)) > 0)
//       {
//         zos.write(buffer, 0, length);
//       }
//       zis.closeEntry();
//       zos.closeEntry();
//     }
//   }
 
//   public static Collection<String> getZipEntryNames(File zipFile) throws IOException
//   {
//     ZipInputStream input = new ZipInputStream(new FileInputStream(zipFile));
//     try {
//       Collection localCollection = getZipEntryNames(input);
// 
//       return localCollection; } finally { input.close();
//     }
//   }
// 
//   public static Collection<String> getZipEntryNames(ZipInputStream zis) throws IOException
//   {
//     ZipEntry ze = null;
//     ArrayList names = new ArrayList();
//     while ((ze = zis.getNextEntry()) != null)
//     {
//       names.add(ze.getName());
//       zis.closeEntry();
//     }
//     return names;
//   }
 
//   public static InputStream getZipEntryInputStream(InputStream input, String name) throws IOException
//   {
//     ZipInputStream zis = new ZipInputStream(input);
//     ByteArrayInputStream bais = null;
//     ZipEntry ze = null;
//     while ((ze = zis.getNextEntry()) != null)
//     {
//       if (ze.getName().equals(name))
//       {
//         byte[] buffer = new byte[2156];
//         int length = 0;
//         ByteArrayOutputStream baos = new ByteArrayOutputStream();
// 
//         while ((length = zis.read(buffer)) > 0)
//         {
//           baos.write(buffer, 0, length);
//         }
//         baos.close();
//         bais = new ByteArrayInputStream(baos.toByteArray());
//         zis.closeEntry();
//         break;
//       }
//       zis.closeEntry();
//     }
//     zis.close();
//     return bais;
//   }
 
//   public static byte[] getZipEntryBytes(InputStream input, String name)
//     throws IOException
//   {
//     ZipInputStream zis = new ZipInputStream(input);
//     ByteArrayOutputStream baos = null;
//     ZipEntry ze = null;
//     while ((ze = zis.getNextEntry()) != null)
//     {
//       if (ze.getName().equals(name))
//       {
//         byte[] buffer = new byte[2156];
//         int length = 0;
//         baos = new ByteArrayOutputStream();
// 
//         while ((length = zis.read(buffer)) > 0)
//         {
//           baos.write(buffer, 0, length);
//         }
//         zis.closeEntry();
//         break;
//       }
//       zis.closeEntry();
//     }
//     zis.close();
//     if (baos != null)
//     {
//       baos.close();
//       return baos.toByteArray();
//     }
// 
//     return null;
//   }
// 
   public static void writeBytes2ZipEntry(InputStream input, long lastModified, ZipOutputStream zos, String name)
     throws IOException
   {
     byte[] mBuffer = new byte[2156];
     int mByteCount = 0;
     ZipEntry entry = new ZipEntry(name);
     entry.setTime(lastModified);
     zos.putNextEntry(entry);
 
     while ((mByteCount = input.read(mBuffer)) != -1)
     {
       zos.write(mBuffer, 0, mByteCount);
     }
     zos.flush();
     zos.closeEntry();
   }
 
//   public static Properties getProperties(InputStream input, String name)
//     throws IOException
//   {
//     InputStream zeinput = getZipEntryInputStream(input, name);
//     Properties properties = null;
//     if (zeinput != null)
//     {
//       properties = new Properties();
//       properties.load(zeinput);
//       zeinput.close();
//     }
//     return properties;
//   }    //解压指定zip文件 
   
 }
