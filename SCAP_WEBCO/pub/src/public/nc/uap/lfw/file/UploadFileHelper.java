package nc.uap.lfw.file;

import java.util.HashMap;
import java.util.Map;

public class UploadFileHelper {
	public static Map<String, String> BuildDefaultPamater(String sysid,String billitem,String billtype
			,String bamodule){
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(LfwFileConstants.BILLTYPE, billtype == null ? "" : billtype);
		map.put(LfwFileConstants.BILLITEM, billitem == null ? "" : billitem);
		map.put(LfwFileConstants.SYSID, sysid == null ? LfwFileConstants.SYSID_BAFILE: sysid);
		map.put(LfwFileConstants.Par_Bamodule, bamodule == null ? "" : bamodule);
		/**
		 * 默认允许多文件上传
		 */
		map.put(LfwFileConstants.MULTI, "true");
		/**
		 * 默认自动上传
		 */
		map.put(LfwFileConstants.AUTO, "true");
		/**
		 * 默认上传完毕关闭当前窗口
		 */
		map.put(LfwFileConstants.CLOSEDIALOG, "true");
		/**
		 * 是否立即上传
		 */
		map.put(LfwFileConstants.ISQUICK, "true");
		/**
		 * 是否弹出上传完毕提示信息
		 * 
		 */
		map.put(LfwFileConstants.UPLOADED_ALERT, "true");
		return map;
	}
}
