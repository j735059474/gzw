package nc.uap.ctrl.pa.itf;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.ctrlfrm.DevicePhase;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;

public interface IPaPublicQryService {
	
	/**
	 * 通过pk查找模板对应的vo
	 * @param pk_template
	 * @return
	 * @throws LfwBusinessException
	 */
	public UwTemplateVO getTemplateVOByPK(String pk_template)throws LfwBusinessException;
	
	/**
	 * 根据所给条件查询模板vo
	 * @param condition
	 * @return
	 * @throws LfwBusinessException
	 */
	public Collection<UwTemplateVO> getTemplateVOByCondition(String condition) throws LfwBusinessException;

	/**
	 * 通过viewId和模板pk查找对应的view
	 * @param viewId
	 * @param pk_template
	 * @return
	 * @throws LfwBusinessException
	 */
	public UwViewVO getViewVO(String viewId, String pk_template) throws LfwBusinessException;
	
	/**
	 * 通过condition查询view的vo集合
	 * @param condition
	 * @return
	 * @throws LfwBusinessException
	 */
	public List<UwViewVO> getViewVOsByCondition(String condition)throws LfwBusinessException;
	
	/**
	 * 根据业务参数获取模板
	 * @param appId
	 * @param winId
	 * @param busId
	 * @param paramMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public List<UwTemplateVO> getTemplateByBusParam(String appId, String winId, String busId, Map<String, String> paramMap)throws LfwBusinessException;
	/**
	 * 通过业务ID和appId得到template的pk
	 * @param busiId
	 * @param appId
	 * @return
	 * @throws LfwBusinessException
	 */
	public String getTemplatePkByBusiidAndAppId(String busiId, String appId) throws LfwBusinessException;
	
	/**
	 * 根据功能节点PK和设备名称查找模板
	 */
	public List<UwTemplateVO> getUwTemplateVOsByFuncnodAndDevice(String funcnodePk, DevicePhase phase);
	
	/**
	 * 根据模板pk获取对应view
	 * @param pk_template
	 * @return
	 * @throws LfwBusinessException
	 */
	public UwViewVO[] getViewByTemplateId(String pk_template) throws LfwBusinessException;
	
	/**
	 * Query template according to the dimensions
	 * @param dimensions Optional dimension
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO[] qryTemplateByDimensions(LinkedHashMap<String, String> dimensions) throws LfwBusinessException;
	
	/**
	 * 通过模板PK和windowID获取系统模板PK
	 * @param pk
	 * @param winId
	 * @return
	 */
	public String qryDftTplPKAndWinId(String pk, String winId);
	
	/**
	 * 通过功能节点PK和windowID获取系统模板PK
	 * @param pk_funcnode
	 * @param winId
	 * @return
	 */
	
	public String qrySysTplPKByFunPKAndWinId(String pk_funcnode, String winId);
	/**
	 * 通过功能节点PK和windowID获取模板PK
	 * @param pk_funcnode
	 * @param winId
	 * @return
	 */
	public String qryNOSysTplPKByFunPKAndWinId(String pk_funcnode, String winId);
}
