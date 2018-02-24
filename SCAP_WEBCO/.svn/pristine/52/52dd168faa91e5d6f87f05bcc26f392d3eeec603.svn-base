package nc.ui.bd.ref.model;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

import nc.itf.org.IOrgConst;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefDocEdit;
import nc.ui.bd.ref.IRefMaintenanceHandler;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.region.RegionVO;

/**
 * 行政区划属性参照
 * 
 * @since 6.1
 * @version 2012-2-13 上午10:23:17
 * @author 王志强
 */
public class ScapRegionDefaultRefTreeModel extends RegionDefaultRefTreeModel {


	@Override
	protected String getEnvWherePart() {
		if (StringUtils.isEmpty(getPk_country())) {
			
				return "1=1";
		} else {
			return "pk_country='" + getPk_country() + "'";
		}
	}

}
