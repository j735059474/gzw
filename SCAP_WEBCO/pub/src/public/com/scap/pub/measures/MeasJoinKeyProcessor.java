package com.scap.pub.measures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.ProcessorUtils;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;

public class MeasJoinKeyProcessor extends BaseProcessor {

	private static final long serialVersionUID = 8490122156034290322L;
	
	private MeasureVO[] measures;
	private KeyGroupVO keyGroupVO;

	public MeasJoinKeyProcessor(KeyGroupVO keyGroupVO, MeasureVO[] measures) {
		super();
		this.measures = measures;
		this.keyGroupVO = keyGroupVO;
	}

	@Override
	public Object processResultSet(ResultSet rs) throws SQLException {
		Map<String, Object> newRow = new LinkedHashMap<String, Object>();
		KeyVO[] keyVOs = keyGroupVO.getKeys();
		if (rs.next()) {

			
			Map<String, Object> oldRow = ProcessorUtils.toMap(rs);//oldRow.get("keyword2")
			
			for (int i = 0; i < keyVOs.length;) {

				KeyVO keyVO = keyVOs[i++];
				String colName = "keyword" + i;
				if (oldRow.containsKey(colName)) {
					newRow.put(keyVO.getName(), oldRow.get(colName));
				}
			}
			// FIXME 如果指标名和关键字名重复会覆盖掉关键字
			for (MeasureVO measure : measures) {
				if (oldRow.containsKey(measure.getDbcolumn())) {
					newRow.put(measure.getName(), oldRow.get(measure.getDbcolumn()));
				}
			}
		}
		return newRow;
	}
}