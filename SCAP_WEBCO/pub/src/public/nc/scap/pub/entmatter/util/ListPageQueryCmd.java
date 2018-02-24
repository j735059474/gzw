package nc.scap.pub.entmatter.util;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Dataset;
import nc.vo.pub.SuperVO;

/**
 * list页面的数据查询命令
 * 这个类的主要作用是对审核页面和管理页面的数据进行过滤
 *
 * 数据查询区分页面类型   管理页面、查看页面、审核页面
 *
 * @author lpf
 *
 */
public class ListPageQueryCmd extends UifDatasetLoadCmd {

	private String simpleWhere;

	private String orderBy;

	private final String flowtype;

	public ListPageQueryCmd(Dataset ds,String flowtype) {
		super(ds);
		this.flowtype = flowtype;
	}

	public ListPageQueryCmd(Dataset ds,String flowtype,String orderBy) {
		super(ds);
		this.flowtype = flowtype;
		this.orderBy = orderBy;
	}

	public ListPageQueryCmd(Dataset ds,String flowtype,String simpleWhere,String orderBy) {
		super(ds);
		this.simpleWhere = simpleWhere;
		this.flowtype = flowtype;
		this.orderBy = orderBy;
	}

	private String getFuncTypeFilter(SuperVO vo, Dataset ds,String pagecode){
		String func_type = MatterUtil.getAddressParameter("func_type");
		return "func_type='"+func_type+"'";
	}
	private String getApproveFilter(SuperVO vo, Dataset ds,String pagecode){
		// 审核页面的sql过滤条件
		String pkFieldName = ds.getPrimaryKeyField();
		// 审核页面只能看到当前用户需要审核的单据
		if ("approve".equals(pagecode)) {
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
			return pkFieldName+" in " +
					"(select pk_frmins from wfm_task " +
					"where pk_owner='"+pk_user+"' and state in ('State_Run','State_Plmnt') " +
							"and pk_flowtype='"+flowtype+"')";
		}else if ("manager".equals(pagecode)) {
			return " formstate is null ";
		}else if ("release".equals(pagecode)) {
			return " formstate = 'End' ";
		}
		return "1=1";
	}
	private String getDeptFilter(SuperVO vo, Dataset ds,String pagecode){
		//审核和查看功能节点能看所有数据
		if ("manager".equals(pagecode)) {
			// 企业填报的数据,加上office过滤条件
			String report_user = MatterUtil.getAddressParameter("report_user");
			if (StringUtils.isNotBlank(report_user) && "qy".equals(report_user)){
				String pkOrg = CntUtil.getCntOrgPk();
				return "office='~' and PK_ORG = '" + pkOrg + "'";
			}
			// 处室填报
			String dept = MatterUtil.getCntDept(null);
			return "office='"+dept+"'";
		}else if ("browse".equals(pagecode)) {
			// 企业查看的数据,加上office过滤条件
			String report_user = MatterUtil.getAddressParameter("report_user");
			if (StringUtils.isNotBlank(report_user) && "qy".equals(report_user)){
				String pk_dep_name = getDeptName();
				if( null!=pk_dep_name && pk_dep_name.equals("委领导")){
					return "office='~'";
				} else {
					String pkOrg = CntUtil.getCntOrgPk();
					return "office='~' and PK_ORG = '" + pkOrg + "'";
				}
			}
			// 处室查看数据
			String pk_dep_name = getDeptName();
			if( null!=pk_dep_name && pk_dep_name.equals("委领导")){
				return "office !='~'";
			} else {
				String dept = MatterUtil.getCntDept(null);
				return "office='"+dept+"'";
			}
		}else if ("release".equals(pagecode)) {
			// 发布数据
			String pk_dep_name = getDeptName();
			if( null!=pk_dep_name && pk_dep_name.equals("委领导")){
				return "office !='~'";
			} else {
				String dept = MatterUtil.getCntDept(null);
				return "office='"+dept+"'";
			}
		}
		return "1=1";
//		// 企业填报的数据,加上office过滤条件
//		String report_user = MatterUtil.getAddressParameter("report_user");
//		if (StringUtils.isNotBlank(report_user) && "qy".equals(report_user)){
//			return "office='~'";
//		} else {
//			return "office !='~'";
//		}
	}
	private String getUserFilter(SuperVO vo, Dataset ds,String pagecode){
		String pk_user = CntUtil.getCntUserPk();
		String result = " 1=1 ";
		String sql = "select name from org_dept where dr = '0' and PK_DEPT =(select PK_DEPT from CP_USER where cuserid ='"+pk_user+"')";
		String pk_dep_name = (String)ScapDAO.executeQuery(sql, new ColumnProcessor());
		if ("manager".equals(pagecode)) {
			if( null!=pk_dep_name && pk_dep_name.equals("委领导")){

			} else {
				result = " FILL_USER = '" + pk_user + "' ";
			}
		}
		return result;
	}

	protected String getSqlWhere(SuperVO vo, Dataset ds,String pagecode){
		String where = "1=1";
		if(simpleWhere!=null){
			where = simpleWhere;
		}

		String functypeFilter = getFuncTypeFilter(vo,ds,pagecode);
		where += " and "+functypeFilter;

		String approveFilter = getApproveFilter(vo,ds,pagecode);
		where += " and "+approveFilter;

		String deptFilter = getDeptFilter(vo,ds,pagecode);
		where += " and "+deptFilter;

		String userFilter = getUserFilter(vo,ds,pagecode);
		where += " and "+userFilter;
		return where;
	}

	@Override
	public final String postProcessQueryVO(SuperVO vo, Dataset ds) {
		String pagecode = MatterUtil.getPageCode();

		String where = getSqlWhere(vo,ds,pagecode);

		String lastCondition = ds.getLastCondition();
		if(lastCondition==null||!lastCondition.equals(where)){
	    	ds.setCurrentKey(Dataset.MASTER_KEY + UUID.randomUUID());
	  	  	ds.setLastCondition(where);
	    }

		ds.setLastCondition(where);
		return where;
	}

	@Override
	public final String postOrderPart(Dataset ds) {
		if (orderBy == null || orderBy.trim().isEmpty()) {
			return " order by ts desc";
		}
		return " order by "+orderBy;
	}
	private String getDeptName() {
		String pk_user = CntUtil.getCntUserPk();
		String result = " 1=1 ";
		String sql = "select name from org_dept where dr = '0' and PK_DEPT =(select PK_DEPT from CP_USER where cuserid ='"+pk_user+"')";
		String pk_dep_name = (String)ScapDAO.executeQuery(sql, new ColumnProcessor());
		return pk_dep_name;
	}
}
