package nc.uap.ctrl.pa.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.pa.itf.IPaPublicQryService;
import nc.uap.lfw.core.ctrlfrm.DevicePhase;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.ICpDeviceQry;
import nc.uap.lfw.core.vo.CpDeviceVO;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;

public class PaPublicQryServiceImpl implements IPaPublicQryService{

	@Override
	public UwTemplateVO getTemplateVOByPK(String pk_template)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			return (UwTemplateVO) dao.retrieveByPK(UwTemplateVO.class, pk_template);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}

	@Override
	public Collection<UwTemplateVO> getTemplateVOByCondition(String condition)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		
		try {
			return dao.retrieveByClause(UwTemplateVO.class, condition);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}

	@Override
	public UwViewVO getViewVO(String viewId, String pk_template)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		UwViewVO vo = null;
		try {
			String condition = "pk_template = '" + pk_template + "' and viewid = '" + viewId + "'";
			Collection<UwViewVO> viewVOs =dao.retrieveByClause(UwViewVO.class, condition);
			if(viewVOs != null){
				Iterator<UwViewVO> it = viewVOs.iterator();
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
			
			if(vo != null)
				return vo;
			
			return null;
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}

	@Override
	public List<UwViewVO> getViewVOsByCondition(String condition)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		List<UwViewVO> viewVos = new ArrayList<UwViewVO>();
		 try {
			Collection<UwViewVO> vos = dao.retrieveByClause(UwViewVO.class, condition);
			Iterator<UwViewVO> it = vos.iterator();
			while(it.hasNext()){
				viewVos.add(it.next());
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage(), e);
		}
		if(viewVos != null && viewVos.size() > 0)
			return viewVos;
		else
			return null;
	}

	@Override
	public String getTemplatePkByBusiidAndAppId(String busiId, String appId)
			throws LfwBusinessException {
		String condition = "busiid = '" + busiId + "' and appid = '" + appId + "'";
		BaseDAO dao = new BaseDAO();
		UwTemplateVO vo = null;
		try {
			Collection<UwTemplateVO> vos = dao.retrieveByClause(UwTemplateVO.class, condition);
			Iterator<UwTemplateVO> it = vos.iterator();
			if(vos != null){
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage(), e);
		}
		
		if(vo == null)
			return null;
		else
			return vo.getPrimaryKey();
	}

	@Override
	public List<UwTemplateVO> getUwTemplateVOsByFuncnodAndDevice(
			String funcnodePk, DevicePhase phase) {
		ICpDeviceQry qry = NCLocator.getInstance().lookup(ICpDeviceQry.class);
		List<UwTemplateVO> templates = new ArrayList<UwTemplateVO>();
		try {
			String sqlWhere = "name = '" + phase.toString() + "'";
			CpDeviceVO devVo = qry.getDeviceVoByCondition(sqlWhere);
			
			if(devVo != null){
				String deviceName = devVo.getName();
				
				String condition = "pk_funcnode = '" + funcnodePk + "'" + "devicename = '" + deviceName;
				Collection<UwTemplateVO> templateList = this.getTemplateVOByCondition(condition);
				Iterator<UwTemplateVO> it = templateList.iterator();
				while(it.hasNext()){
					templates.add(it.next());
				}
			}
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		
		return templates;	
	}

	@Override
	public UwViewVO[] getViewByTemplateId(String pk_template)
			throws LfwBusinessException {
		IPaPublicQryService service = ServiceLocator.getService(IPaPublicQryService.class);
		List<UwViewVO> increVOs = null;
		String condition = "pk_template = '" + pk_template + "' order by viewid";
		increVOs = service.getViewVOsByCondition(condition);
		return increVOs != null  ? increVOs.toArray(new UwViewVO[0]) : null;
	}

	@Override
	public List<UwTemplateVO> getTemplateByBusParam(String appId, String winId,
			String busId, Map<String, String> paramMap)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		String cond = null;
		
		String cond0 = appId == null ? "appid = '~'" : " appid = '" + appId + "'";
		String cond1 = winId == null ? " and windowid = '~' " : " and windowid = '" + winId + "' ";
		String cond2 = busId == null ? " and busiid = '~' " : " and busiid = '" + busId + "' " ;
		
		String pk_prodef = null;
		String portId = null;
		String ext1 = null;
		String ext2 = null;
		String ext3 = null;
		String ext4 = null;
		String ext5 = null;
		
		if(paramMap == null)
			cond = cond0 + cond1 + cond2;
		
		else{
			pk_prodef = paramMap.get("pk_prodef");
			String cond3 = pk_prodef == null ? " and pk_prodef = '~' " : " and pk_prodef = '" + pk_prodef + "' ";
			
			portId = paramMap.get("port_id");
			String cond4 = portId == null ? " and port_id = '~' " : " and port_id = '" + portId + "' ";
			
			ext1 = paramMap.get("ext1");
			String cond5 = ext1 == null ? " and ext1 = '~' " : " and ext1 = '" + ext1 + "' ";
			ext2 = paramMap.get("ext2");
			String cond6 = ext2 == null ? " and ext2 = '~' " : " and ext2 = '" + ext2 + "' ";;
			ext3 = paramMap.get("ext3");
			String cond7 = ext3 == null ? " and ext3 = '~' " : " and ext3 = '" + ext3 + "' ";;
			ext4 = paramMap.get("ext4");
			String cond8 = ext4 == null ? " and ext4 = '~' " : " and ext4 = '" + ext4 + "' ";;
			ext5 = paramMap.get("ext5");
			String cond9 = ext5 == null ? " and ext5 = '~' " : " and ext5 = '" + ext5 + "' ";;
			
			String pk_funcnode = paramMap.get("pk_funcnode");
			String cond10 = pk_funcnode == null ? " and pk_funcnode = '~' " : " and pk_funcnode = '" + pk_funcnode + "'";
		
			cond = cond0 + cond1 + cond2 + cond3 + cond4 + cond5 + cond6 + cond7 + cond8 + cond9 + cond10;
		}
				
		UwTemplateVO vo = null;
		
		try {
			List<UwTemplateVO> templateVOs = (List<UwTemplateVO>)dao.retrieveByClause(UwTemplateVO.class, cond);
			if(templateVOs != null && templateVOs.size() > 0)
				return templateVOs;
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public UwTemplateVO[] qryTemplateByDimensions(
			LinkedHashMap<String, String> dimensions)
			throws LfwBusinessException {
		StringBuffer wsb = new StringBuffer(" 1 = 1 ");
		if(dimensions == null || dimensions.size() == 0)
			return null;
		Iterator<String> it = dimensions.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String val = dimensions.get(key);
			pingSQL(wsb, key, val);
		}
		Collection<UwTemplateVO> tmps = getTemplateVOByCondition((wsb.toString()));
		return 	tmps.toArray(new UwTemplateVO[0]);
	}
	
	private void pingSQL(StringBuffer wsb, String key, String val) {
		if(StringUtils.isEmpty(val)){
			wsb.append(" and ").append(key).append("= '~'");
		}
		else{
			wsb.append(" and ").append(key).append("='");
			wsb.append(val.replace("'", "")).append("' ");
		}
	}

	@Override
	public String qryDftTplPKAndWinId(String pk, String winId) {
		String condition = "issystemplate = 'Y' and windowid = '" + winId + "' and pk_funcnode = (select pk_funcnode from uw_template where pk_template = '"+ pk + "')";
		try {
			Collection<UwTemplateVO> cols = this.getTemplateVOByCondition(condition);
			UwTemplateVO[] list = cols.toArray(new UwTemplateVO[0]);
			if(list != null && list.length > 0)
				return list[0].getPk_template();
		} catch (LfwBusinessException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String qrySysTplPKByFunPKAndWinId(String pk_funcnode, String winId) {
		String condition = "issystemplate = 'Y' and windowid = '" + winId + "' and pk_funcnode = '" + pk_funcnode + "'";
		BaseDAO dao = new BaseDAO();
		try {
			Collection<UwTemplateVO> vos = dao.retrieveByClause(UwTemplateVO.class, condition);
			UwTemplateVO[] list = vos.toArray(new UwTemplateVO[0]);
			if(list != null && list.length > 0){
				return list[0].getPk_template();
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return null;
	}
   
	@Override
	public String qryNOSysTplPKByFunPKAndWinId(String pk_funcnode, String winId) {
		String condition = " windowid = '" + winId + "' and pk_funcnode = '" + pk_funcnode + "'";
		BaseDAO dao = new BaseDAO();
		try {
			Collection<UwTemplateVO> vos = dao.retrieveByClause(UwTemplateVO.class, condition);
			UwTemplateVO[] list = vos.toArray(new UwTemplateVO[0]);
			if(list != null && list.length > 0){
				return list[0].getPk_template();
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return null;
	}
}
