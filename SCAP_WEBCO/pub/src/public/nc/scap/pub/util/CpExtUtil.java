package nc.scap.pub.util;

import java.net.MalformedURLException;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.portal.log.ScapLogger;
import nc.vo.org.DeptVO;
import net.extsoft.webservice.service.Generic;

import com.caucho.hessian.client.HessianProxyFactory;

/** 
 * 外部事物工具类
 * @author liyuchen
 */
public class CpExtUtil {
	
	public static Generic getGeneric(String url){
		// 创建hession工厂
		HessianProxyFactory factory = new HessianProxyFactory();
		Generic service = null;
		try {
			// 获取服务
			service = (Generic) factory.create(Generic.class, url);
		} catch (MalformedURLException e) {
			ScapLogger.error(e.getMessage());
		}
		return service;
	}
	/** 同步部门  */
	public static void syncOneLevelDept(String domainName) throws Exception{
		String elinkURL = ScapSysinitUtil.getSysinitStrByCode("elinkpath");
		Generic service = CpExtUtil.getGeneric(elinkURL);
		BaseDAO basedao = new BaseDAO();
		basedao.retrieveByPK(CpOrgVO.class, IGlobalConstants.ROOT_PK_ORG);
		if(!service.existDepartment(domainName, IGlobalConstants.ROOT_PK_ORG)){
			service.addDepartment(domainName, IGlobalConstants.ROOT_PK_ORG,"国资委", null, null);
			DeptVO[] corpDept = CpOrgUtil.getCorpDept(IGlobalConstants.ROOT_PK_ORG);
			if(corpDept!=null&&corpDept.length!=0){
				for(Integer j = 0;j<corpDept.length;j++){
					if(service.existDepartment(domainName,corpDept[j].getPk_dept())){
						service.updateDepartmentName(domainName, corpDept[j].getPk_dept(),corpDept[j].getName());
					}else{
						service.addDepartment(domainName, corpDept[j].getPk_dept(), corpDept[j].getName(), IGlobalConstants.ROOT_PK_ORG,String.valueOf(j));
					}
				}
			}
		}
		String condition = "nvl(dr,0)=0 and pk_fatherorg='"+IGlobalConstants.ROOT_PK_ORG+"' and orglevel='2' order by code";
		List<CpOrgVO> list = (List<CpOrgVO>)basedao.retrieveByClause(CpOrgVO.class, condition);
		for(int i = 0;i<list.size();i++){
			CpOrgVO cpOrgVO = list.get(i);
			String pk_org = cpOrgVO.getPk_org();
			if(service.existDepartment(domainName,pk_org)){
				service.updateDepartmentName(domainName, pk_org, cpOrgVO.getName());
			}else{
				service.addDepartment(domainName, pk_org, cpOrgVO.getName(), IGlobalConstants.ROOT_PK_ORG, cpOrgVO.getCode());
			}
			DeptVO[] corpDept = CpOrgUtil.getCorpDept(pk_org);
			if(corpDept!=null&&corpDept.length!=0){
				for(Integer j = 0;j<corpDept.length;j++){
					if(service.existDepartment(domainName,corpDept[j].getPk_dept())){
						service.updateDepartmentName(domainName, corpDept[j].getPk_dept(),corpDept[j].getName());
					}else{
						service.addDepartment(domainName, corpDept[j].getPk_dept(), corpDept[j].getName(), pk_org,String.valueOf(j));
					}
				}
			}
		}
	}
	/**
	 *  同步人员
	 *  @author liyuchen 2014-01-11
	 *  @param domainName = 域名称
	 */
	public static void syncPerson(String domainName) throws Exception{
		BaseDAO baseDAO = new BaseDAO();
		String elinkURL = ScapSysinitUtil.getSysinitStrByCode("elinkpath");
		Generic service = CpExtUtil.getGeneric(elinkURL);
		String condition = "nvl(dr,0)=0";
		List<CpUserVO> list = (List<CpUserVO>)baseDAO.retrieveByClause(CpUserVO.class, condition);
		for(CpUserVO cpUserVO:list){
			String cuserid = cpUserVO.getUser_code();
			String password = "123qwe";	//暂时写死.因为密码加密无法传输，解决后更换新方法;
			String pk_dept = cpUserVO.getPk_org();
			if(service.existDepartment(domainName, pk_dept)){
				pk_dept = cpUserVO.getPk_dept();
			}
			try{				
				if(service.existEmployee(domainName, cuserid)){
					//编辑人员
					service.editEmployee(domainName, cuserid, cpUserVO.getUser_name(), null, password, null, null, null, pk_dept, null);
				}else{
					//添加人员
					service.addEmployeeEx(domainName, cuserid, cpUserVO.getUser_name(), null, password, null, null, null, pk_dept, null);
				}
			}catch(Exception e){
				ScapLogger.error(e.getMessage());
			}
		}
	}
	
	
}
