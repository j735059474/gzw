package nc.scap.pub.workreport.ref.control;

import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.sqlvo.ILfwRefSqlVO;
import uap.lfw.ref.vo.LfwRefSqlVO;
import uap.web.bd.pub.AppUtil;

public class ReportTypeGridRefCtrl extends LfwSqlGridReferenceController {

    @Override
    public ILfwRefSqlVO getGridSqlVO() {
        String pk_business_type = (String)AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE);
        StringBuilder refWhere = new StringBuilder("");
        if(pk_business_type!=null) {
                refWhere.append(" YE_TYPE = '")
                .append(pk_business_type)
                .append("' ");
        } else {
                refWhere.append(" 1=1 ");
        }

        
        LfwRefSqlVO vo = new LfwRefSqlVO();
        vo.setTableName("(select * from scapco_work_reportType where "+refWhere.toString()+") scapco_work_reportType");
        vo.setRealTableNames("scapco_work_reportType");
        vo.setOrderByPart("ts");
        vo.setUseCache(false);
//        vo.setTableName("scapco_work_reportType");
//        vo.setRealTableNames("scapco_work_reportType");
//        vo.setOrderByPart(refWhere.toString());
        return vo;
    }

}
