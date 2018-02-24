package nc.uap.cpb.org.impl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpAppsNodeQry;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.menucategory.IMenuItemFilter;
import nc.uap.cpb.org.menuitem.MenuRoot;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.util.CpbUtil;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.cpb.org.vos.MenuItemAdapterVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.cache.BDRbacCacheHelper;
import uap.web.bd.cache.SimpleCpNodeVO;
import uap.web.bd.pub.BDLanguageHelper;
import uap.web.bd.pub.BDUriHelper;
/**
 * 菜单查询服务实现
 * 2011-10-13 上午11:14:22 limingf
 */
public class CpMenuQry implements ICpMenuQry { 
	@SuppressWarnings("unchecked") @Override public CpMenuItemVO[] getMenuItemsByCategory(String pk_menucategory) throws CpbBusinessException {
		if (pk_menucategory == null) {
			return null;
		}
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menuitem where  pk_menucategory=?";
		parameter.addParam(pk_menucategory);
		try {
			List<CpMenuItemVO> list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
			if (list != null && !list.isEmpty()) {
				return list.toArray(new CpMenuItemVO[0]);
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
		return null;
	}
	@SuppressWarnings("unchecked") @Override public CpMenuItemVO[] getMenuItemsByFuncnodes(String[] pk_funnodes) throws CpbBusinessException {
		if (pk_funnodes == null || pk_funnodes.length < 1) {
			return new CpMenuItemVO[] {};
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pk_funnodes.length; i++) {
			sb.append("'").append(pk_funnodes[i]).append("'");
			if (i != pk_funnodes.length - 1)
				sb.append(",");
		}
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menuitem where  pk_funnode in(" + sb.toString() + ")";
		try {
			List<CpMenuItemVO> list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
			if (list != null && !list.isEmpty()) {
				return list.toArray(new CpMenuItemVO[0]);
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
		return new CpMenuItemVO[] {};
	}
	@SuppressWarnings("unchecked") @Override public CpMenuCategoryVO[] getAllMenuCategory() throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menucategory";
		try {
			List<CpMenuCategoryVO> list = (List<CpMenuCategoryVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuCategoryVO.class));
			if (list != null && !list.isEmpty()) {
				return list.toArray(new CpMenuCategoryVO[0]);
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
		return null;
	}
	
	public CpMenuCategoryVO[] getMenuCategory(String where) throws CpbBusinessException{
		PtBaseDAO baseDAO = new PtBaseDAO();
		String sql = "select * from cp_menucategory  where "+where;
		List<CpMenuCategoryVO> list = null;
		try {
			list = (List<CpMenuCategoryVO>) baseDAO.executeQuery(sql, new BeanListProcessor(CpMenuCategoryVO.class));
			if (list == null || list.size() < 1)
				return new CpMenuCategoryVO[] {};
			return list.toArray(new CpMenuCategoryVO[list.size()]);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
	}
	
	public CpMenuItemVO[] getMenuItem(String where) throws CpbBusinessException{
		PtBaseDAO baseDAO = new PtBaseDAO();
		String sql = "select * from cp_menuitem  where "+where;
		List<CpMenuItemVO> list = null;
		try {
			list = (List<CpMenuItemVO>) baseDAO.executeQuery(sql, new BeanListProcessor(CpMenuItemVO.class));
			if (list == null || list.size() < 1)
				return new CpMenuItemVO[] {};
			return list.toArray(new CpMenuItemVO[list.size()]);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
	}
	
	@SuppressWarnings("unchecked") @Override 
	public CpMenuCategoryVO getMenuCategorysByPk(String pk_menucategory) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menucategory where pk_menucategory = ?";
		parameter.addParam(pk_menucategory);
		try {
			List<CpMenuCategoryVO> list = (List<CpMenuCategoryVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuCategoryVO.class));
			if (list != null && !list.isEmpty()) {
				return list.toArray(new CpMenuCategoryVO[0])[0];
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked") @Override 
	public CpMenuCategoryVO[] getMenuCategorysByPks(String[] pk_menucategory) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<pk_menucategory.length;i++){
			sb.append("'").append(pk_menucategory[i]).append("'");
			if(i!=pk_menucategory.length-1)
				sb.append(",");
		}
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menucategory where pk_menucategory in("+sb.toString()+")";
//		parameter.addParam(pk_menucategory);
		try {
			List<CpMenuCategoryVO> list = (List<CpMenuCategoryVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuCategoryVO.class));
			if (list != null && !list.isEmpty()) {
				return list.toArray(new CpMenuCategoryVO[0]);
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
		return new CpMenuCategoryVO[]{};
	}
	
	@SuppressWarnings("unchecked") @Override public CpMenuItemVO getMenuItemsByFuncnode(String pk_funnode) throws CpbBusinessException {
		if (pk_funnode == null) {
			return null;
		}
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menuitem where  pk_funnode=?";
		parameter.addParam(pk_funnode);
		try {
			List<CpMenuItemVO> list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
		return null;
	}
	@SuppressWarnings("unchecked") @Override public MenuItemAdapterVO[] getMenuItems(String pk_menucategory) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select a.* ,b.* from cp_menuitem as a left join  cp_appsnode as b on(a.pk_funnode = b.pk_appsnode) where  a.pk_menucategory=?";
		parameter.addParam(pk_menucategory);
		List<MenuItemAdapterVO> list;
		try {
			list = (List<MenuItemAdapterVO>) dao.executeQuery(sql, parameter, new MenuItemAdapterVOProcessor());
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		return list.toArray(new MenuItemAdapterVO[0]);
	}
	
	
	
	/**
	 * modify by maokun 2012/08/31   加入按节点是否启用查询
	 */	 
	public MenuItemAdapterVO[] getMenuItemsWithPermission(String pk_menucategory, String pk_user,boolean permission,boolean filter) throws CpbBusinessException {
		return getMenuItemsWithPermission(pk_menucategory, pk_user,permission,filter,true);
	}
	public MenuItemAdapterVO[] getMenuItemsWithPermission(String pk_menucategory, String pk_user,boolean permission,boolean filter,boolean orderby) throws CpbBusinessException {
		List<String> list = new ArrayList<String>();
		list.add(pk_menucategory);
		return getMenuItemsWithPermission(list, pk_user,permission,filter,orderby);
	}
	
	public MenuItemAdapterVO[] getMenuItemsWithPermission(List<String> pk_menucategorys,String pk_user,boolean permission,boolean filter,boolean orderby) throws CpbBusinessException{
		if(pk_menucategorys == null || pk_menucategorys.size() < 1) return null;
		List<MenuItemAdapterVO> list = null;
		PtBaseDAO dao = new PtBaseDAO();
		String str = CpbUtil.joinQryArrays(CpbUtil.returnArray(pk_menucategorys));
		
		SQLParameter parameter = new SQLParameter();
		String sql = "select a.* ,b.* from cp_menuitem as a left join  cp_appsnode as b on(a.pk_funnode = b.pk_appsnode) where  a.pk_menucategory in ("+
		str +") AND (activeflag = 'Y' OR activeflag IS NULL) order by a.ordernum asc";
		
		
		
		try {
			list = (List<MenuItemAdapterVO>) dao.executeQuery(sql, parameter, new MenuItemAdapterVOProcessor());
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		if(list==null)
			return new MenuItemAdapterVO[]{};
		//过滤没有权限的节点
		if(permission){
			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
//			CpAppsNodeVO[] permissinnodes = nodeqry.getNodeWithPermission(pk_user);
			Map<String, SimpleCpNodeVO>  map = BDRbacCacheHelper.getRBACExtend().getNodesByUserid(pk_user);
			if(map!=null&&list!=null){				
				
				List<MenuItemAdapterVO> menus = new ArrayList<MenuItemAdapterVO>();
				for(int i=0;i<list.size();i++){				
					String pk_node = list.get(i).getPk_funnode();
					if(StringUtils.isEmpty(pk_node)|| pk_node.equals("~")){
						menus.add(list.get(i));
						continue;
					}
					if(map.get(pk_node)==null)
						continue;
					menus.add(list.get(i));
						//list.remove(i);
				}
				list = menus;
			}
			else
				list = null;
		}
		//根据菜单分组上注册的插件过滤
		if(filter){
			ICpMenuQry menuqry = NCLocator.getInstance().lookup(ICpMenuQry.class);
			CpMenuCategoryVO[] categoryvos = menuqry.getMenuCategorysByPks(pk_menucategorys.toArray(new String[0]));			
			Map<String,List<MenuItemAdapterVO>> map =  splitByMenuAdapterCat(list);
			list = new ArrayList<MenuItemAdapterVO>();
			for(CpMenuCategoryVO categoryvo : categoryvos){
				if(!map.containsKey(categoryvo.getPk_menucategory())) continue;
				List<MenuItemAdapterVO> childlist = map.get(categoryvo.getPk_menucategory());
				if(childlist == null || childlist.size() < 1) continue;
				String filterclass = categoryvo.getFilterclass();
				Object filterIns = null;
				if(filterclass!=null&&!"".equals(filterclass)){
					try {
						filterIns = LfwClassUtil.newInstance(filterclass);
					} catch (Throwable e) {
						CpLogger.error(e.getMessage(), e);
					}
					
				}
				if(filterIns != null && filterIns instanceof IMenuItemFilter){
					IMenuItemFilter excuter = (IMenuItemFilter) LfwClassUtil.newInstance(filterclass);
					MenuItemAdapterVO[] childmenuitems  = null;
					try{
						childmenuitems = excuter.filter(childlist.toArray(new MenuItemAdapterVO[0]));
					}
					catch(Throwable e){
						CpLogger.error("菜单过滤器执行中出错:" + filterclass);
						CpLogger.error(e.getMessage(),e);
					}
					if(childmenuitems != null && childmenuitems.length > 0){
						for(MenuItemAdapterVO childmenu : childmenuitems){
							list.add(childmenu);
						}
					}
				}
				else{
					for(MenuItemAdapterVO childmenu : childlist){
						list.add(childmenu);
					}
				}
			}
		}
		return list.toArray(new MenuItemAdapterVO[0]);
	}
	
	public MenuRoot[] getMenuRootWithPermission(String pk_menucategory,String pk_user,boolean permission
			,boolean filter,boolean escapeNullCategory) throws CpbBusinessException{
		List<MenuRoot> list = null;
		List<String> pk_menucategorys = new ArrayList<String>();
		pk_menucategorys.add(pk_menucategory);
		Map<String, List<MenuRoot>> map = getMenuRootWithPermission(pk_menucategorys,  pk_user,  permission,filter,  escapeNullCategory);
		if(map != null && map.containsKey(pk_menucategory))
			list = map.get(pk_menucategory);
		//list = removeEmptyMenu(list);
		if(list==null)
			return new MenuRoot[]{};
		return list.toArray(new MenuRoot[0]);
	}
	public MenuRoot[] getMenuRootWithPermission(String pk_menucategory,String pk_user,boolean permission,boolean filter) throws CpbBusinessException{		
		return getMenuRootWithPermission(pk_menucategory,pk_user,permission,filter,true);
	}

	@Override
	public Map<String, List<MenuRoot>>  getMenuRootWithPermission(List<String> pk_menucategorys, String pk_user, boolean permission,boolean filter, boolean escapeNullCategory)
			throws CpbBusinessException {
		MenuItemAdapterVO[] menuitemadapters = getMenuItemsWithPermission(pk_menucategorys,pk_user,permission,filter,true);
		List<MenuItemAdapterVO> adaptervolist = Arrays.asList(menuitemadapters);		
		adaptervolist = ComputerAdapterMenuChild(adaptervolist);
		if(escapeNullCategory)
			adaptervolist = removeEmptyAdapterMenu(adaptervolist);

		List<MenuRoot>  list = 	LoadMenuRootFromMenuAdapter(adaptervolist);
		Map<String, List<MenuRoot>> menumap = new HashMap<String, List<MenuRoot>>();
		menumap = splitByMenuCat(list);
		return menumap;
	}
	
	
	@SuppressWarnings("unchecked") @Override 
	public MenuItemAdapterVO[] getMenuItemsByParent(String pk_menuitem, String pk_user,boolean permission,boolean orderby) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();		
		
		String sql1 = "select pk_menuitem,pk_parent from cp_menuitem";
		List<Map<String,String>> pkmaplist = null;
		try {
			pkmaplist = (List<Map<String, String>>) dao.executeQuery(sql1, new MapListProcessor());
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		List<String> pklist = new ArrayList<String>();
		pklist = getPkTrees(pklist,pkmaplist,pk_menuitem);
		//移除自己
		pklist.remove(pk_menuitem);
		StringBuffer sb = new StringBuffer();
		sb.append("''");
		for(int i=0;i<pklist.size();i++){
			sb.append(",'").append(pklist.get(i)).append("'");
//			if(i!=pklist.size()-1)sb.append(",");
		}
		
		String sql = "select a.* ,b.* from cp_menuitem as a left join  cp_appsnode as b on(a.pk_funnode = b.pk_appsnode) where  a.pk_menuitem in ("+sb.toString()+")";
		//排序
		if(orderby)
			sql += " order by a.ordernum asc";
		List<MenuItemAdapterVO> list;
		try {
			list = (List<MenuItemAdapterVO>) dao.executeQuery(sql, parameter, new MenuItemAdapterVOProcessor());
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		if(list==null)
			return new MenuItemAdapterVO[]{};
		//过滤没有权限的节点
		if(permission){
//			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
//			CpAppsNodeVO[] permissinnodes = nodeqry.getNodeWithPermission(pk_user);
			CpAppsNodeVO[] permissinnodes = BDRbacCacheHelper.getRBACExtend().getSimpleNodesByUserid(pk_user);
			if(permissinnodes!=null&&list!=null){
				Map<String,CpAppsNodeVO> map = new HashMap<String,CpAppsNodeVO>();
				for(CpAppsNodeVO nodevo:permissinnodes){
					map.put(nodevo.getPk_appsnode(), nodevo);
				}
				for(int i=list.size()-1;i>=0;i--){
					if(list.get(i).getPk_funnode()==null)
						continue;
					if(map.get(list.get(i).getPk_funnode())==null)
						list.remove(i);
				}
			}
		}
		return list.toArray(new MenuItemAdapterVO[0]);
	}
	
	@SuppressWarnings("unchecked") @Override 
	public CpMenuItemVO[] getCpMenuItemVOsWithPermission(String pk_user,boolean permission,boolean filter) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select a.* from cp_menuitem as a";
		//过滤没有权限的节点
		if(permission){
//			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
//			CpAppsNodeVO[] permissinnodes = nodeqry.getNodeWithPermission(pk_user);
			CpAppsNodeVO[] permissinnodes = BDRbacCacheHelper.getRBACExtend().getSimpleNodesByUserid(pk_user);
			if(permissinnodes==null)
				permissinnodes = new CpAppsNodeVO[] {};
			String[] pk_funnodes = new String[permissinnodes.length];
			for(int i=0;i<permissinnodes.length;i++){
				pk_funnodes[i] = permissinnodes[i].getPk_appsnode();
			}
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<pk_funnodes.length;i++){
				sb.append("'").append(pk_funnodes[i]).append("'");
				if(i==pk_funnodes.length-1)sb.append(",");
			}
			sql +=" where a.pk_funnode in("+sb.toString()+")";				
		}		
		List<CpMenuItemVO> list;
		try {
			list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}		
		if(list==null)
			return new CpMenuItemVO[]{};
		//根据菜单分组上注册的插件过滤
		if(filter){
			ICpMenuQry menuqry = NCLocator.getInstance().lookup(ICpMenuQry.class);
			List<String> categorylist = new ArrayList<String>();
			for(CpMenuItemVO itemvo:list){
				if(!categorylist.contains(itemvo.getPk_menucategory()))
					categorylist.add(itemvo.getPk_menucategory());
			}
			CpMenuCategoryVO[] categoryvos = menuqry.getMenuCategorysByPks(categorylist.toArray(new String[0]));
			CpMenuItemVO[] filterItemvos = list.toArray(new CpMenuItemVO[0]);
			for(int i=0;i<categoryvos.length;i++){
				String filterclass = categoryvos[i].getFilterclass();		
				if(filterclass!=null&&!"".equals(filterclass)){
				
						try{
							if(LfwClassUtil.newInstance(filterclass) instanceof IMenuItemFilter){
								IMenuItemFilter excuter = (IMenuItemFilter) LfwClassUtil.newInstance(filterclass);
								filterItemvos = excuter.filter(filterItemvos);
							}
						}
						catch(Throwable e){
							CpLogger.error("菜单过滤器执行中出错:" + filterclass);
							CpLogger.error(e.getMessage(),e);
						}
		//						filterItemvos = excuter.filter(filterItemvos);	
				}
			}
			return filterItemvos;
		}
		return list.toArray(new CpMenuItemVO[0]);
	}
	
	@SuppressWarnings("unchecked") @Override 
	public CpMenuItemVO[] getCpMenuItemVOsWithPermission(String pk_menucategory, String pk_user,boolean permission,boolean filter) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select a.* from cp_menuitem as a where  a.pk_menucategory=?";
		parameter.addParam(pk_menucategory);
		List<CpMenuItemVO> list;
		try {
			list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		if(list==null)
			return new CpMenuItemVO[]{};
		//过滤没有权限的节点
		if(permission){
//			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
//			CpAppsNodeVO[] permissinnodes = nodeqry.getNodeWithPermission(pk_user);
			CpAppsNodeVO[] permissinnodes = BDRbacCacheHelper.getRBACExtend().getSimpleNodesByUserid(pk_user);
			if(permissinnodes!=null&&list!=null){
				Map<String,CpAppsNodeVO> map = new HashMap<String,CpAppsNodeVO>();
				for(CpAppsNodeVO nodevo:permissinnodes){
					map.put(nodevo.getPk_appsnode(), nodevo);
				}
				//for(int i=0;i<list.size();i++){				
				//	if(map.get(list.get(i).getPk_funnode())==null)
				//		list.remove(i);
				//}
				//houlg修改2015-10-21  上面list既循环又删除。导致索引不对。最终list结果集错误
				if(map!=null&&list!=null){		
					List<CpMenuItemVO> menus=new ArrayList<CpMenuItemVO>();
					for(int i=0;i<list.size();i++){				
						if(map.get(list.get(i).getPk_funnode())==null){
							continue;
						}else{
						menus.add(list.get(i));
						}
					}
					list=menus;
				}
				
			}
		}
		//根据菜单分组上注册的插件过滤
		if(filter){
			ICpMenuQry menuqry = NCLocator.getInstance().lookup(ICpMenuQry.class);
			CpMenuCategoryVO categoryvo = menuqry.getMenuCategorysByPk(pk_menucategory);
			String filterclass = categoryvo.getFilterclass();		
			if(filterclass!=null&&!"".equals(filterclass)){
				try{
					if(LfwClassUtil.newInstance(filterclass) instanceof IMenuItemFilter){
						IMenuItemFilter excuter = (IMenuItemFilter) LfwClassUtil.newInstance(filterclass);
						return excuter.filter(list.toArray(new CpMenuItemVO[0]));	
					}
				}
				catch(Throwable e){
					CpLogger.error("菜单过滤器执行中出错:" + filterclass);
					CpLogger.error(e.getMessage(),e);
				}
			}
		}
		return list.toArray(new CpMenuItemVO[0]);
	}
	
	@SuppressWarnings("unchecked") @Override 
	public CpMenuItemVO[] getCpMenuItemVOsWithPermission(String pk_menucategory, String pk_user,boolean permission,boolean filter,boolean orderby) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select a.* from cp_menuitem as a where  a.pk_menucategory=?";
		//排序
		if(orderby)
			sql += " order by a.ordernum asc";
		parameter.addParam(pk_menucategory);
		List<CpMenuItemVO> list;
		try {
			list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		if(list==null)
			return new CpMenuItemVO[]{};
		//过滤没有权限的节点
		if(permission){
//			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
//			CpAppsNodeVO[] permissinnodes = nodeqry.getNodeWithPermission(pk_user);
			CpAppsNodeVO[] permissinnodes = BDRbacCacheHelper.getRBACExtend().getSimpleNodesByUserid(pk_user);
			if(permissinnodes!=null&&list!=null){
				Map<String,CpAppsNodeVO> map = new HashMap<String,CpAppsNodeVO>();
				for(CpAppsNodeVO nodevo:permissinnodes){
					map.put(nodevo.getPk_appsnode(), nodevo);
				}
				//for(int i=0;i<list.size();i++){				
				//	if(map.get(list.get(i).getPk_funnode())==null)
				//		list.remove(i);
				//}
				//houlg修改2015-10-21  上面list既循环又删除。导致索引不对。最终list结果集错误
				if(map!=null&&list!=null){		
					List<CpMenuItemVO> menus=new ArrayList<CpMenuItemVO>();
					for(int i=0;i<list.size();i++){				
						if(map.get(list.get(i).getPk_funnode())==null){
							continue;
						}else{
						menus.add(list.get(i));
						}
					}
					list=menus;
				}
			}
		}
		//根据菜单分组上注册的插件过滤
		if(filter){
			ICpMenuQry menuqry = NCLocator.getInstance().lookup(ICpMenuQry.class);
			CpMenuCategoryVO categoryvo = menuqry.getMenuCategorysByPk(pk_menucategory);
			String filterclass = categoryvo.getFilterclass();		
			if(filterclass!=null&&!"".equals(filterclass)){
				try{
					if(LfwClassUtil.newInstance(filterclass) instanceof IMenuItemFilter){
						IMenuItemFilter excuter = (IMenuItemFilter) LfwClassUtil.newInstance(filterclass);
						return excuter.filter(list.toArray(new CpMenuItemVO[0]));	
					}
				}
				catch(Throwable e){
					CpLogger.error("菜单过滤器执行中出错:" + filterclass);
					CpLogger.error(e.getMessage(),e);
				}
			}
		}
		return list.toArray(new CpMenuItemVO[0]);
	}
	
	@SuppressWarnings("unchecked") @Override public MenuItemAdapterVO[] getMenuItemsByParent(String pk_parent) throws CpbBusinessException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select a.*,b.* from cp_menuitem as a left join  cp_appsnode as b on(a.pk_funnode = b.pk_appsnode) where a.pk_parent=?";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_parent);
		List<MenuItemAdapterVO> list;
		try {
			list = (List<MenuItemAdapterVO>) dao.executeQuery(sql, parameter, new MenuItemAdapterVOProcessor());
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
		if(list==null)
			return new MenuItemAdapterVO[]{};
		return list.toArray(new MenuItemAdapterVO[0]);
	}
	
	@SuppressWarnings("unchecked") @Override 
	public CpMenuItemVO[] getRootMenuItemsByCategory(String pk_menucategory) throws CpbBusinessException {
		if (pk_menucategory == null) {
		return null;
		}
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from cp_menuitem where  pk_menucategory=? and pk_parent='~'";
		parameter.addParam(pk_menucategory);
		try {
		List<CpMenuItemVO> list = (List<CpMenuItemVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(CpMenuItemVO.class));
		if (list != null && !list.isEmpty()) {
		return list.toArray(new CpMenuItemVO[0]);
		}
		} catch (DAOException e) {
		CpLogger.error(e.getMessage(), e.getCause());
		throw new CpbBusinessException(e);
		}
		return null;
		}

/**
 * 根据菜单分组查询全部菜单
 * modify by lisw 2012.2.13,废除之前逻辑，重新获取菜单 
 * modify by maokun 2012/08/31   加入按节点是否启用查询
 */
	@SuppressWarnings("unchecked") @Override 
	public MenuRoot[] getMenuRoot(String pk_menucategory) throws CpbBusinessException {
		List<MenuRoot> list = null;
			if(!StringUtils.isEmpty(pk_menucategory)){
			List<String> pklist = new ArrayList<String>();
			pklist.add(pk_menucategory);
			Map<String,List<MenuRoot>> map = getMenuRoot(pklist);
			if(map != null && map.containsKey(pk_menucategory))
				list = map.get(pk_menucategory);
		}
		if(list==null)
			return new MenuRoot[]{};
		return list.toArray(new MenuRoot[0]);		
	}
	@Override
	public Map<String, List<MenuRoot>> getMenuRoot(List<String> pk_menucategorys)
			throws CpbBusinessException {
		if(pk_menucategorys == null || pk_menucategorys.size() < 1) return null;
		Map<String, List<MenuRoot>> menumap = new HashMap<String, List<MenuRoot>>(); 
		//MenuItemAdapterVOProcessor
		PtBaseDAO dao = new PtBaseDAO();
		String str = CpbUtil.joinQryArrays(CpbUtil.returnArray(pk_menucategorys));
		String sql = "select * from cp_menuitem  as a left join cp_appsnode as b on a.pk_funnode = b.pk_appsnode where pk_menucategory in ("+
			str +") AND (activeflag = 'Y' OR activeflag IS NULL) order by a.ordernum";
		
		List<MenuItemAdapterVO> adaptervolist = null;
		try {
			SQLParameter parameter = new SQLParameter();
			adaptervolist = (List<MenuItemAdapterVO>) dao.executeQuery(sql, parameter , new MenuItemAdapterVOProcessor());
			ComputerAdapterMenuChild(adaptervolist);
			
			List<MenuRoot> list = 	LoadMenuRootFromMenuAdapter(adaptervolist);
			menumap = splitByMenuCat(list);
			return menumap;
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e.getCause());
			throw new CpbBusinessException(e);
		}
	}
	/**
	 * 计算所有的跟节点
	 * @param adaptervolist
	 * @return
	 */
	private List<MenuRoot> LoadMenuRootFromMenuAdapter(List<MenuItemAdapterVO> adaptervolist){
		List<MenuRoot> rootlist = new ArrayList<MenuRoot>();
		if(adaptervolist != null){
			for(MenuItemAdapterVO vo : adaptervolist){
				String PK_parent = vo.getPk_parent();
				if(PK_parent == null || PK_parent.isEmpty() || PK_parent.equals("~")){
					MenuRoot root = new MenuRoot(vo.getTitle(),vo.getMenuitem().getCode());
					root.setIcon(vo.getMenuitem().getIconpath());
					root.setPk_parent(vo.getPk_menuitem());
					root.setProviderclass(vo.getMenuitem().getProviderclass());
					root.setAdapterVO(vo);
					root.setPk_menucategory(vo.getMenuitem().getPk_menucategory());
					rootlist.add(root);
					
					root.setNodes(vo.getChildrenMenu());
				}	
			}
		}
		return rootlist;
	}
	private Map<String,List<MenuRoot>> splitByMenuCat(List<MenuRoot> list){
		Map<String,List<MenuRoot>> map = new HashMap<String,List<MenuRoot>>();
		if(list != null && list.size() > 0){
			for(MenuRoot menu : list){
				if(menu.getPk_menucategory() == null) continue;
				if(!map.containsKey(menu.getPk_menucategory())){
					List<MenuRoot> newlist = new ArrayList<MenuRoot>();
					newlist.add(menu);
					map.put(menu.getPk_menucategory(), newlist);
				}
				else{
					List<MenuRoot> newlist = map.get(menu.getPk_menucategory());
					newlist.add(menu);
				}
			}
		}
		return map;
	}
	private Map<String,List<MenuItemAdapterVO>> splitByMenuAdapterCat(List<MenuItemAdapterVO> list){
		Map<String,List<MenuItemAdapterVO>> map = new HashMap<String,List<MenuItemAdapterVO>>();
		if(list != null && list.size() > 0){
			for(MenuItemAdapterVO menu : list){
				
				if(menu.getMenuitem() == null || menu.getMenuitem().getPk_menucategory() == null) continue;
				if(!map.containsKey(menu.getMenuitem().getPk_menucategory())){
					List<MenuItemAdapterVO> newlist = new ArrayList<MenuItemAdapterVO>();
					newlist.add(menu);
					map.put(menu.getMenuitem().getPk_menucategory(), newlist);
				}
				else{
					List<MenuItemAdapterVO> newlist = map.get(menu.getMenuitem().getPk_menucategory());
					newlist.add(menu);
				}
			}
		}
		return map;
	}
	/**
	 * 计算出所有的子节点
	 * @param adaptervolist
	 */
	private List<MenuItemAdapterVO> ComputerAdapterMenuChild(List<MenuItemAdapterVO> adaptervolist){
		if(null != adaptervolist){
			for(int i=0;i<adaptervolist.size();i++){
				MenuItemAdapterVO curmenu = adaptervolist.get(i);
				for(int j=0;j<adaptervolist.size();j++){
					if(i== j)
						continue;
					MenuItemAdapterVO childmenu = adaptervolist.get(j);
					if(childmenu.getPk_parent() != null)
						if(childmenu.getPk_parent().equals(curmenu.getPk_menuitem())){
							curmenu.addChildMenu(childmenu);
						}
				}
			}
		}
		//去除重复子结构
		List<MenuItemAdapterVO> tlist = new ArrayList<MenuItemAdapterVO>();
		for (int i = 0; i < adaptervolist.size(); i++) {
			MenuItemAdapterVO curmenu = adaptervolist.get(i);
			if(curmenu.getPk_parent()==null||"".equals(curmenu.getPk_parent())){
				tlist.add(curmenu);
			}
		}
		return tlist;
	}
	private List<MenuRoot> removeEmptyMenu(List<MenuRoot> list){
		List<MenuRoot> tlist = new ArrayList<MenuRoot>();
		if(null != list){
			for(int i=0;i<list.size();i++){
				MenuRoot curmenu = list.get(i);
				List<MenuItemAdapterVO> childs = curmenu.getNodes();
				childs = removeEmptyAdapterMenu(childs);
				if(childs!=null&&childs.size()>0){
					curmenu.setNodes(childs);
					tlist.add(curmenu);
				}
			}
		}
		return tlist;
	}
	private List<MenuItemAdapterVO> removeEmptyAdapterMenu(List<MenuItemAdapterVO> adaptervolist){
		List<MenuItemAdapterVO> alllist = new ArrayList<MenuItemAdapterVO>();
		if(null != adaptervolist){
			for(int i=0;i<adaptervolist.size();i++){
				List<MenuItemAdapterVO> tlist = new ArrayList<MenuItemAdapterVO>();
				MenuItemAdapterVO curmenu = adaptervolist.get(i);
				List<MenuItemAdapterVO> childs = curmenu.getChildrenMenu();
				if(childs!=null&&childs.size()>0){
					curmenu.setChildrenMenu(removeEmptyAdapterMenu(childs));
				}	
				//alllist.addAll(tlist);
				if(curmenu.getChildrenMenu().size()>0||(curmenu.getPk_funnode()!=null&&!"".equals(curmenu.getPk_funnode()))){
					alllist.add(curmenu);
				}	
			}
		}
		return alllist;
	}

	/**
	 * 构造MenuRoot
	 * 
	 * @param menuitemlist
	 * @return
	 * @throws CpbBusinessException
	 */
	@SuppressWarnings("unused") private MenuRoot[] buildMenuRoot(List<CpMenuItemVO> menuitemlist) throws CpbBusinessException {
		if (menuitemlist == null || menuitemlist.size() < 1)
			return new MenuRoot[] {};
		MenuRoot[] roots = new MenuRoot[menuitemlist.size()];
		for (int i = 0; i < menuitemlist.size(); i++) {
			CpMenuItemVO menuitem = menuitemlist.get(i);
			MenuRoot root = new MenuRoot(menuitem.getName(), menuitem.getCode());
			MenuItemAdapterVO[] vos = getMenuItemsByParent(menuitem.getPk_menuitem());
			root.setNodes(Arrays.asList(vos));
			roots[i] = root;
		}
		return roots;
	}
	public class MenuItemAdapterVOProcessor implements ResultSetProcessor {
		private static final long serialVersionUID = 6491811282825432325L;
		public Object handleResultSet(ResultSet rs) throws SQLException {
			String lancode = "";
			try{
				lancode = LfwRuntimeEnvironment.getLangCode();
			}
			catch(Throwable e){
				lancode = InvocationInfoProxy.getInstance().getLangCode();
			}
			
			
			int seq =  BDLanguageHelper.getLangSeqBycode(lancode);
			
			String nameatt = "name" + (seq > 1?seq:"");
			
			List<MenuItemAdapterVO> list = new ArrayList<MenuItemAdapterVO>();
			while (rs.next()) {
				MenuItemAdapterVO menuitemadaptervo = new MenuItemAdapterVO();
				CpMenuItemVO menuitem = new CpMenuItemVO();
				CpAppsNodeVO appsnode = new CpAppsNodeVO();
				menuitem.setCode(rs.getString("code"));
				String name = rs.getString(nameatt);
				if(name == null || name.equals(""))
					name = rs.getString("name");
				if(name == null)
					name = rs.getString("pk_menuitem");
				menuitem.setName(name);
				menuitem.setIconpath(rs.getString("iconpath"));
				menuitem.setIsnotleaf(UFBoolean.valueOf(rs.getString("isnotleaf")));
				menuitem.setMenuitemdes(rs.getString("menuitemdes"));
				menuitem.setPk_funnode(rs.getString("pk_funnode"));
				menuitem.setPk_menucategory(rs.getString("pk_menucategory"));
				menuitem.setPk_parent(rs.getString("pk_parent"));
				menuitem.setPk_menuitem(rs.getString("pk_menuitem"));
				menuitem.setProviderclass(rs.getString("providerclass"));
				menuitem.setIsnewpage(UFBoolean.valueOf(rs.getString("isnewpage")));
				menuitem.setIsofen(UFBoolean.valueOf(rs.getString("isofen")));
				appsnode.setActiveflag(UFBoolean.valueOf(rs.getString("activeflag")));
				appsnode.setI18nname(rs.getString("i18nname"));
				appsnode.setId(rs.getString("id"));
				appsnode.setPk_appscategory(rs.getString("pk_appscategory"));
				appsnode.setPk_appsnode(rs.getString("pk_appsnode"));
				appsnode.setTitle(rs.getString("title"));
				String winid = rs.getString("winid");
				String url = rs.getString("url");
				String newurl = BDUriHelper.rePlaceWindID(url, winid);
				appsnode.setUrl(newurl);
				menuitemadaptervo.setPk_menuitem(menuitem.getPk_menuitem());
				menuitemadaptervo.setMenuitem(menuitem);
				menuitemadaptervo.setPk_parent(menuitem.getPk_parent());
				menuitemadaptervo.setTitle(menuitem.getName());
				menuitemadaptervo.setPk_funnode(menuitem.getPk_funnode());
				menuitemadaptervo.setFunnode(appsnode);
				
				list.add(menuitemadaptervo);
			}
			return list;
		}
	}
	public class MenuRootProcessor implements ResultSetProcessor {
		private static final long serialVersionUID = -8126917569173826972L;
		public Object handleResultSet(ResultSet rs) throws SQLException {
			Map<String, MenuRoot> map = new HashMap<String, MenuRoot>();
			while (rs.next()) {
				String pk_parent = rs.getString("pk_parent");
				MenuRoot root = map.get(pk_parent);
				if (root == null) {
					if (pk_parent == null || "".equals(pk_parent)) {
						String name = rs.getString("name");
						String pk_menuitem = rs.getString("pk_menuitem");
						root = map.get(pk_menuitem);
						if(root == null){
							root = new MenuRoot(rs.getString("name"), rs.getString("code"));
							map.put(pk_menuitem, root);
						}
						root.setPk_parent(pk_menuitem);
						root.setIcon(rs.getString("iconpath"));
					} else {
						root = new MenuRoot(rs.getString("pname"), rs.getString("pcode"));
						root.setPk_parent(rs.getString("pk_parent"));
						root.setIcon(rs.getString("iconpath"));
						map.put(rs.getString("pk_parent"), root);
					}
				}
				if (!(pk_parent == null || "".equals(pk_parent))) {
					MenuItemAdapterVO menuitemadaptervo = new MenuItemAdapterVO();
					CpMenuItemVO menuitem = new CpMenuItemVO();
					CpAppsNodeVO appsnode = new CpAppsNodeVO();
					menuitem.setCode(rs.getString("code"));
					menuitem.setName(rs.getString("name"));
					menuitem.setIconpath(rs.getString("iconpath"));
					menuitem.setIsnotleaf(UFBoolean.valueOf(rs.getString("isnotleaf")));
					menuitem.setMenuitemdes(rs.getString("menuitemdes"));
					menuitem.setPk_funnode(rs.getString("pk_funnode"));
					menuitem.setPk_menucategory(rs.getString("pk_menucategory"));
					menuitem.setPk_parent(rs.getString("pk_parent"));
					menuitem.setPk_menuitem(rs.getString("pk_menuitem"));
					appsnode.setActiveflag(UFBoolean.valueOf(rs.getString("activeflag")));
					appsnode.setI18nname(rs.getString("i18nname"));
					appsnode.setId(rs.getString("id"));
					appsnode.setPk_appscategory(rs.getString("pk_appscategory"));
					appsnode.setPk_appsnode(rs.getString("pk_appsnode"));
					appsnode.setTitle(rs.getString("title"));
					String winid = rs.getString("winid");
					String url = rs.getString("url");
					String newurl = BDUriHelper.rePlaceWindID(url, winid);
					appsnode.setUrl(newurl);
					menuitemadaptervo.setPk_menuitem(menuitem.getPk_menuitem());
					menuitemadaptervo.setMenuitem(menuitem);
					menuitemadaptervo.setPk_parent(menuitem.getPk_parent());
					menuitemadaptervo.setTitle(menuitem.getName());
					menuitemadaptervo.setPk_funnode(menuitem.getPk_funnode());
					menuitemadaptervo.setFunnode(appsnode);
					root.addNode(menuitemadaptervo);
				}
			}
			List<MenuRoot> rootlist = new ArrayList<MenuRoot>();
			Iterator<MenuRoot> ite = map.values().iterator();
			while (ite.hasNext()) {
				rootlist.add(ite.next());
			}
			return rootlist;
		}
	}
	
	private List<String> getPkTrees(List<String> pklist,List<Map<String,String>> pkmaplist,String pk_menuitem){
		if(pkmaplist==null)return null;
		pklist.add(pk_menuitem);
		for(int i=0;i<pkmaplist.size();i++){
			Map<String,String> map = pkmaplist.get(i);
			if(pk_menuitem.equals(map.get("pk_parent"))){
				getPkTrees(pklist,pkmaplist,map.get("pk_menuitem"));
			}
		}
		return pklist;
	}

	@Override
	public MenuItemAdapterVO[] getOfenMenuItems(String[] pk_menucategorys,
			String pk_user, boolean isofen) throws CpbBusinessException {
		if(pk_menucategorys == null || pk_menucategorys.length == 0){
			return null;
		}
		StringBuffer pks = new StringBuffer();
		for(int i=0; i<pk_menucategorys.length; i++){
			if(i == 0){
				pks.append("(");
			}
			if(i > 0){
				pks.append(",");
			}
			pks.append("'" + pk_menucategorys[i] + "'");
			if(i == pk_menucategorys.length - 1){
				pks.append(")");
			}
		}
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select a.* ,b.* from cp_menuitem as a left join  cp_appsnode as b on(a.pk_funnode = b.pk_appsnode) where  a.pk_menucategory in " + pks;
		if(isofen)
			sql += " and isofen='Y' ";
		else sql += " and isofen<>'Y' ";
		sql += "  order by a.ordernum asc ";
		//parameter.addParam(pks);
		List<MenuItemAdapterVO> list;
		try {
			list = (List<MenuItemAdapterVO>) dao.executeQuery(sql, parameter, new MenuItemAdapterVOProcessor());
			if(list==null)
				return new MenuItemAdapterVO[]{};
			List<MenuItemAdapterVO> plist = new ArrayList<MenuItemAdapterVO>();
			//过滤没有权限的节点
			if(pk_user!=null){
				ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
				CpAppsNodeVO[] permissinnodes = nodeqry.getNodeWithPermission(pk_user);
				if(permissinnodes==null)
					permissinnodes = new CpAppsNodeVO[]{};
				Map<String,CpAppsNodeVO> map = new HashMap<String,CpAppsNodeVO>();
				for(CpAppsNodeVO nodevo:permissinnodes){
					map.put(nodevo.getPk_appsnode(), nodevo);
				}
				for(int i=0;i<list.size();i++){
					String pk_funnode = list.get(i).getPk_funnode();
					if(!(pk_funnode==null||"".equals(pk_funnode)||"~".equals(pk_funnode)||map.get(pk_funnode)==null)){
						plist.add(list.get(i));
					}
				}	
				list = plist;
			}			
			ComputerAdapterMenuChild(list);
			return list.toArray(new MenuItemAdapterVO[0]);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public MenuItemAdapterVO[] getReguMenuWithPermission(String pk_user)
			throws CpbBusinessException {
		String sql = "SELECT nickname,pk_funcnode from pt_regularitem where pk_user = ?";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_user);
		List<MenuItemAdapterVO> list = null;
		try {
			list = (List<MenuItemAdapterVO>) new PtBaseDAO().executeQuery(sql, param,new SimpleMenuItemAdapterVOProcessor());
			//系统管理员拥有所有功能节点权限
			if(CpbServiceFacility.getCpUserQry().isSysAdmin(pk_user)){
				if(list != null && list.size() > 0){
					List<String> nodePKlist = new ArrayList<String>(list.size());
					for(int i = 0; i < list.size();i++){				
						String pk_node = list.get(i).getPk_funnode();
						if(StringUtils.isEmpty(pk_node) || "~".equals(pk_node))
							continue;
						nodePKlist.add(pk_node);						
					}
					CpAppsNodeVO[] nodes = ServiceLocator.getService(ICpAppsNodeQry.class).getNodeByPks(nodePKlist.toArray(new String[0]));
					if(nodes != null && nodes.length > 0){
						for(int i = 0;i < list.size(); ++i){				
							String pk_node = list.get(i).getPk_funnode();
							for(int j = 0; j < nodes.length ; ++j)
								if(nodes[j].getPk_appsnode().equals(pk_node))
									list.get(i).setFunnode(nodes[j]);
						}
					}
				}
			}
			else{
				//过滤没有权限的节点
				Map<String, SimpleCpNodeVO>  map = BDRbacCacheHelper.getRBACExtend().getNodesByUserid(pk_user);
				if(map!=null&&list!=null){				
					List<MenuItemAdapterVO> menus = new ArrayList<MenuItemAdapterVO>();
					for(int i=0;i<list.size();i++){				
						String pk_node = list.get(i).getPk_funnode();
						if(StringUtils.isEmpty(pk_node) || map.get(pk_node)==null)
							continue;				
						list.get(i).setFunnode(convertSimpleNode(map.get(pk_node)));
						menus.add(list.get(i));	
					}
					list = menus;
				}
				else
					list = null;
			}
			if(list==null)
				return new MenuItemAdapterVO[0];
			return list.toArray(new MenuItemAdapterVO[0]);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e);
		}

	}
	
	private class SimpleMenuItemAdapterVOProcessor implements ResultSetProcessor{

		@Override
		public Object handleResultSet(ResultSet rs) throws SQLException {
			List<MenuItemAdapterVO> list = new ArrayList<MenuItemAdapterVO>();
			while (rs.next()) {
				MenuItemAdapterVO itemAdapter = new MenuItemAdapterVO();
				itemAdapter.setTitle(rs.getString("nickname"));
				itemAdapter.setPk_funnode(rs.getString("pk_funcnode"));
				list.add(itemAdapter);
			}
			return list;
		}
		
	}
	
	private CpAppsNodeVO convertSimpleNode(SimpleCpNodeVO simpleNode){
		CpAppsNodeVO appsNode = new CpAppsNodeVO();
		appsNode.setPk_appsnode(simpleNode.getPk_appsnode());
		appsNode.setId(simpleNode.getId());
		appsNode.setTitle(simpleNode.getTitle());
		appsNode.setType(simpleNode.getType());
		appsNode.setUrl(simpleNode.getUrl());
		appsNode.setPk_appscategory(simpleNode.getPk_appscategory());
		appsNode.setSpecialflag(simpleNode.getSpecialflag());
		return appsNode;
	}
	
	
}
