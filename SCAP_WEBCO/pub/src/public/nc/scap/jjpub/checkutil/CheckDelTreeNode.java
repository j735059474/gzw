package nc.scap.jjpub.checkutil;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

public class CheckDelTreeNode {


	BaseDAO dao;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * ��ɾ������,����Ƿ�������
	 * �����������,���׳��쳣 msg
	 * @param ds
	 * @param row
	 * @param fatherpkfield
	 * @param wheresql
	 * @param msg
	 * @throws LfwRuntimeException
	 */
	public void execute(Dataset ds,String msg)throws LfwRuntimeException{
		Row row=ds.getSelectedRow();
		if(row==null)
			return;
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		pkValue=pkValue==null?" ":pkValue;//���Ϊnull��"" �ᵼ��sql �� != ������� ��ѯ�������
		SuperVO svo = (SuperVO) LfwClassUtil.newInstance(ds.getVoMeta());
		String ParentPKField = svo.getParentPKFieldName();
		if(ParentPKField==null||ParentPKField.trim().length()==0)
			throw new LfwRuntimeException("SuperVO "+ds.getVoMeta()+" ���� getParentPKFieldName ���ظ������ֶ�Ϊ��");
		StringBuilder sb=new StringBuilder("select count(1) nnum from ").append(svo.getTableName()).append(" where dr = 0 and ");
		SQLParameter par = new SQLParameter();
		sb.append(ParentPKField).append(" = ?");
		par.addParam(pkValue);
		Object obj=null;
		try {
			obj = getBaseDAO().executeQuery(sb.toString(), par,new ColumnProcessor());
		} catch (DAOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		if(obj!=null && !obj.toString().trim().equals("0")){
			throw new LfwRuntimeException(msg==null?"�����ӽڵ�,ɾ��ʧ��!":msg);
		}
		
	}
	
	
}
