package nc.scap.dsm.dynamiccombo;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;
import nc.vo.pub.lang.UFDate;

public class Material_Year extends DynamicComboData{
	BaseDAO dao=null;
	BaseDAO getBaseDAO(){
		if(dao==null)
			dao=new BaseDAO();
		return dao;
	}
	
	static int minyear=0;
	int getMinYear(){
		if(minyear == 0){
			String sql="select min(year) year from scapjj_material_h ";
			try {
				Object obj=getBaseDAO().executeQuery(sql, new ColumnProcessor());
				if(obj!=null&&obj.toString().length()>0)
					minyear=Integer.valueOf(obj.toString())-1;
			} catch (DAOException e) {
				minyear=0;
			}
			if(minyear==0)
				minyear=new UFDate(System.currentTimeMillis()).getYear()-1;
		}
		return minyear;
	}
	
	@Override
	protected CombItem[] createCombItems() {
		int yeadbegin=getMinYear();
		int yearend=new UFDate(System.currentTimeMillis()).getYear()+4;
		int size=yearend-yeadbegin+1;
		CombItem[] items=new CombItem[size];
		for (int i = 0; i < size; i++) {
			items[i] = new CombItem();
			items[i].setText(String.valueOf(yeadbegin+i));
			items[i].setValue(String.valueOf(yeadbegin+i));
		}
		
		return items;
	}

	
}
