package nc.scap.pub.attlist;

import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.AttachGroupVO;
import nc.scap.pub.vos.AttachRoleVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.vo.pub.SuperVO;

public class AttachRoleDao {

	/**
	 * 根据节点pk获取附件规则分类
	 */
	public static AttachGroupVO[] retrieveAttachGroupByNodePK(String pk_node) {

		SuperVO[] vos = ScapDAO.retrieveByCondition(AttachGroupVO.class, "node like '%" + pk_node + "%'");
		return vos == null ? new AttachGroupVO[0] : (AttachGroupVO[]) vos;
	}

	/**
	 * 根据节点编码获取附件规则分类
	 */
	public static AttachGroupVO[] retrieveAttachGroupByNodeCode(String nodecode) {
		CpAppsNodeVO[] nodes = (CpAppsNodeVO[]) ScapDAO.retrieveByCondition(CpAppsNodeVO.class, "id='" + nodecode +"'");
		String pk_node = "~";
		if (nodes != null && nodes.length == 1) {
			pk_node = nodes[0].getPk_appsnode();
		}
		return retrieveAttachGroupByNodePK(pk_node);
	}
	
	/**
	 * 根据AttachGroup主键获取AttachRole
	 */
	public static AttachRoleVO[] retrieveAttachRoleByGroup(String pk_attach_group) {
		
		SuperVO[] vos = ScapDAO.retrieveByCondition(AttachRoleVO.class, "pk_attach_group='" + pk_attach_group + "'");
		return vos == null ? new AttachRoleVO[0] : (AttachRoleVO[]) vos;
	}
}
