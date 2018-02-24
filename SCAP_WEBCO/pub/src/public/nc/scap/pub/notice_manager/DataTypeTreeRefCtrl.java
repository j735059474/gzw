package nc.scap.pub.notice_manager;

import org.apache.commons.lang.StringUtils;

import nc.scap.pub.itf.IGlobalConstants;
import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

/**
* MatarialTypeTreeRefCtrl
* @author Administrator
*
*/
public class DataTypeTreeRefCtrl extends LfwSqlTreeReferenceController {

	@Override
	public LfwRefSqlVO getTreeSqlVO() {
	  	String data_type_lianxiren = (String)uap.web.bd.pub.AppUtil.getAppAttr(IGlobalConstants.APPATTR_DATA_TYPE_LIANXIREN);
		// 通知下发画面的card画面选择资料类型
	  	String whereSql = " 1=1";
	  	if (StringUtils.isNotBlank(data_type_lianxiren)){
	  		whereSql = whereSql + " and  PK_DATETYPE_H in (" + data_type_lianxiren + ")";
	  	} else {
	  		whereSql = whereSql + " and 1=2";
	  	}
	  	LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName(" (select * from  SCAPJJ_DATETYPE_H where " + whereSql + " ) SCAPJJ_DATETYPE_H");
		vo.setRealTableNames("SCAPJJ_DATETYPE_H");
		return vo;
	}
}
