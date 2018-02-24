package nc.scap.pub.vos;

import nc.md.model.impl.MDEnum;
import nc.md.model.IEnumValue;

public class ScapSmsTaskStatusEnum extends MDEnum {

	public ScapSmsTaskStatusEnum(IEnumValue enumvalue) {
		super(enumvalue);
	}

	/** δ��� */
	public static final String  NOT_FINISHED = "NOT_FINISHED";

	/** ����� */
	public static final String FINISHED = "FINISHED";
}
