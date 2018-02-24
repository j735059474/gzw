package nc.scap.pub.util;

public class CpStrUtil {
	
	public static String blankStr(Object obj) {
		String result = "";
		if(obj != null) {
			result = obj.toString();
		}
		return result;
	}
	/**
	 * ƴin sql
	 * @param inValues
	 * @return
	 */
	public static String buildSqlPartOfIn(String[] inValues){
		int size = inValues != null ? inValues.length : 0;
		if(size > 0){
			StringBuffer resultBuf = new StringBuffer();
			for(int i = 0; i < size; i++){
				if(inValues[i] == null || inValues[i].equals("")){
					continue;
				}
				resultBuf.append(",");
				resultBuf.append("'");
				resultBuf.append(inValues[i]);
				resultBuf.append("'");
			}
			if(resultBuf.length() > 0){
				return resultBuf.substring(1);
			}
		}
		return "";
	}
}
