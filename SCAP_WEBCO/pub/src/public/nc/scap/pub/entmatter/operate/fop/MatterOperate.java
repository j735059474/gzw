package nc.scap.pub.entmatter.operate.fop;

public class MatterOperate extends nc.scap.pub.entmatter.operate.MatterOperate{

	/**
	 * 可见字段
	 * @param pro_action
	 * @return
	 */
	@Override
	public String[] visiableEles(String pro_action){
		return null;
	}
	
	/**
	 * 可修改字段
	 * @param pro_action
	 * @return
	 */
	@Override
	public String[] editableEles(String pro_action){
		return null;
	}

	/**
	 * 不可空字段
	 * @param pro_action
	 * @return
	 */
	@Override
	public String[] notnullableEles(String pro_action){
		return new String[]{"report_title","effect_type_name","matter_class_name"};
	}
}
