package nc.scap.pub.util;

import java.lang.reflect.Method;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.cpb.log.CpLogger;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.SuperVO;

public class CpVOUtil<T> {
	
	/**
	 * 将指定行数据转义为VO,T代表需要转义的目标VO类型
	 * 使用用法1：CpVOUtil<T> util = new CpVOUtil<T>(); T vo = util.getVO(dataset, row, new T());
	 * 使用方法2：T vo = new CpVOUtil<T>().getVO(dataset, row, new T());
	 * @author taoye 2013-7-24
	 */
	public T getVO(Dataset ds, Row row, T t) {
		List<Field> fields = ds.getFieldSet().getFieldList();
			for(Field field : fields) {
				String key = field.getField();
				if(key == null || key.equals("") || key.equals("status")) {
					continue;
				}
				//modify by liyuchen 2013-07-31 修改try-catch代码块的位置
				try {
					Class<?> type = t.getClass().getDeclaredField(key).getType();
					Method method = t.getClass().getMethod(getMethodName("set", key), type);
					method.invoke(t, row.getValue(fields.indexOf(field)));
				} catch (NoSuchFieldException e) {
					ScapLogger.error("转换异常！数据集中的某个字段Field在元数据中未找到，field is null！" + key, e);
				} catch (Exception e) {
					ScapLogger.error("转换异常！行数据转换VO异常，请联系公用模块开发者！", e);
				}
				//modify by liyuchen 2013-07-31 修改 try-catch代码块的位置
			}
		return t;
	}
	
	/**
	 * 将指定行数据转义为VO,T代表需要转义的目标VO类型
	 * 使用用法1：CpVOUtil<T> util = new CpVOUtil<T>(); T vo = util.getVO(dataset, row, new T());
	 * 使用方法2：T vo = new CpVOUtil<T>().getVO(dataset, row, new T());
	 * @author taoye 2013-7-24
	 */
	public T getVO(String json, T t) {
		String[] keyvalues = json.split(";");
		for(String keyvalue : keyvalues) {
			if(!keyvalue.contains("=") || keyvalue.endsWith("=")) {
				continue;
			}
			String key = keyvalue.split("=")[0];
			String value = keyvalue.split("=")[1];
			try {
				Class<?> type = t.getClass().getDeclaredField(key).getType();
				Method method = t.getClass().getMethod(getMethodName("set", key), type);
				method.invoke(t, value);
			} catch (NoSuchFieldException e) {
				ScapLogger.error("转换异常！数据集中的某个字段Field在元数据中未找到，field is null！" + key, e);
			} catch (Exception e) {
				ScapLogger.error("转换异常！行数据转换VO异常，请联系公用模块开发者！", e);
			}
		}
		
		
		
//		List<Field> fields = ds.getFieldSet().getFieldList();
//			for(Field field : fields) {
//				String key = field.getField();
//				if(key == null || key.equals("") || key.equals("status")) {
//					continue;
//				}
//				//modify by liyuchen 2013-07-31 修改try-catch代码块的位置
//				try {
//					Class<?> type = t.getClass().getDeclaredField(key).getType();
//					Method method = t.getClass().getMethod(getMethodName("set", key), type);
//					method.invoke(t, row.getValue(fields.indexOf(field)));
//				} catch (NoSuchFieldException e) {
//					ScapLogger.error("转换异常！数据集中的某个字段Field在元数据中未找到，field is null！" + key, e);
//				} catch (Exception e) {
//					ScapLogger.error("转换异常！行数据转换VO异常，请联系公用模块开发者！", e);
//				}
//				//modify by liyuchen 2013-07-31 修改 try-catch代码块的位置
//			}
		return t;
	}
	
	/**
	 * 批量将行数据转化为对应VO数组，具体用法参照getVO(dataset ds, Row row, T t)
	 * @author taoye 2013-7-24
	 */
	public T[] getVOs(Dataset ds, Row[] rows, T[] ts) {
		for(int x = 0; x < rows.length; x++) {
			getVO(ds, rows[x], ts[x]);
		}
		return ts;
	}
	
	/**
	 * 将指定VO转义为行数据,T代表用来转义的目标VO类型,注意：调用getRowWithRefshData方法已经自动将Row插入数据集中，返回Row对象只是为了方便用户需要后续操作！！！不需要用户再次手动插入数据集!!!
	 * 使用用法1：CpVOUtil<T> util = new CpVOUtil<T>(); T vo = util.getRowWithRefshData(dataset, row, new T());
	 * 使用方法2：T vo = new CpVOUtil<T>().getRowWithRefshData(dataset, t);
	 * @author taoye 2013-7-24
	 */
	public Row getRow(Dataset ds, T t) {
		Row row = ds.getEmptyRow();
		List<Field> fields = ds.getFieldSet().getFieldList();
		try {
			for(Field field : fields) {
//				String key = field.getField(); //chenmeng1  2015-3-9  审计am 导出excel时更改
				String key = field.getId();
				if(key == null || key.equals("") || key.equals("status")||key.contains("_name")) {
					continue;
				}
				int index = ds.nameToIndex(field.getId());
//				Class<?> type = t.getClass().getDeclaredField(key).getType();
				Method method = t.getClass().getMethod(getMethodName("get", key));
				Object value = method.invoke(t);
				if(value != null)
					row.setValue(index, value);
			}
		} catch (NoSuchMethodException e) {
			ScapLogger.error("转换异常！数据集中的某个字段的get方法在元数据中未找到，field is null！", e);
		} catch (Exception e) {
			ScapLogger.error("转换异常！行数据转换VO异常，请联系公用模块开发者！", e);
		}
		return row;
	}
	
	/**
	 * 将指定VO转义为行数据,T代表用来转义的目标VO类型,注意：调用getRowWithRefshData方法已经自动将Row插入数据集中，返回Row对象只是为了方便用户需要后续操作！！！不需要用户再次手动插入数据集!!!
	 * 使用用法1：CpVOUtil<T> util = new CpVOUtil<T>(); T vo = util.getRowWithRefshData(dataset, row, new T());
	 * 使用方法2：Row row = new CpVOUtil<T>().getRowWithRefshData(dataset, t);
	 * @author taoye 2013-7-24
	 */
	public Row getRowWithRefshData(Dataset ds, T t) {
		Row row = ds.getEmptyRow();
		List<Field> fields = ds.getFieldSet().getFieldList();
		try {
			for(Field field : fields) {
				String key = field.getField();
				if(key == null || key.equals("") || key.endsWith("status")) {
					continue;
				}
				if(key.equals("layout_container")) {
					System.out.println("");
				}
				int index = fields.indexOf(field);
//				Class<?> type = t.getClass().getDeclaredField(key).getType();
				Method method = t.getClass().getMethod(getMethodName("get", key));
				Object value = method.invoke(t);
				if(value != null)
					row.setValue(index, value);
			}
			ds.addRow(row);
		} catch (NoSuchMethodException e) {
			ScapLogger.error("转换异常！数据集中的某个字段的get方法在元数据中未找到，field is null！", e);
		} catch (Exception e) {
			ScapLogger.error("转换异常！行数据转换VO异常，请联系公用模块开发者！", e);
		}
		return row;
	}
	
	/**
	 * 将指定VO数组转义为行数据数组,T代表用来转义的目标VO类型,注意：调用getRowsWithRefshData方法已经自动将Row数组插入数据集中，返回Row对象只是为了方便用户需要后续操作！！！不需要用户再次手动插入数据集!!!
	 * 使用用法1：CpVOUtil<T> util = new CpVOUtil<T>(); T vo = util.getRowWithRefshData(dataset, ts);
	 * 使用方法2：T vo = new CpVOUtil<T>().getRow(dataset, row, ts);
	 * @author taoye 2013-7-24
	 */
	public Row[] getRowsWithRefshData(Dataset ds, T[] ts) {
		Row[] rows = new Row[ts.length];
		for(int x = 0; x < rows.length; x++) {
			rows[x] = getRowWithRefshData(ds, ts[x]);
		}
		return rows;
	}
	
	/**
	 * 获取方法名 oper为“set”或者“get”或者其他前缀，fieldName为列名，自动首字母大写
	 * @author taoye 2013-7-24
	 */
	public String getMethodName(String oper, String fieldName) {
		return oper + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	/** 
	 * 批量保存所有行
	 * @param dataset 需要保存的数据集,需要将提交模式设置为："提交所有行"，否则会出现空行现象
	 * @author liyuchen 2013-11-25
	 **/
	public void saveAllRow(Dataset dataset) throws Exception{
		Row[] rows = dataset.getAllRow();
		if(rows==null||rows.length==0){
			return;
		}
		Dataset2SuperVOSerializer<SuperVO> serializer = new Dataset2SuperVOSerializer<SuperVO>();
		IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
		SuperVO[] vos = serializer.serialize(dataset, rows);
		for(SuperVO vo : vos) { 
			try {
				cpbService.insertOrUpdateSuperVO(vo,false);
			} catch (Exception e) {
				ScapLogger.error(e.getMessage());
				CpLogger.error("批量保存出错!");
				throw e;
			}
		}
	}
	
	/**
	 * 
	 * 批量保存多行
	 * @param dataset 需要保存的数据集,需要将提交模式设置为："提交所有行"，否则会出现空行现象
	 * @author pos 2014-8-22
	 */
	public void saveRows(Dataset dataset,SuperVO[] vos) throws Exception{
		if(vos==null||vos.length==0){
			return;
		}
//		Dataset2SuperVOSerializer<SuperVO> serializer = new Dataset2SuperVOSerializer<SuperVO>();
		IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
//		SuperVO[] vos = serializer.serialize(dataset, rows);
		cpbService.insertSuperVOs(vos,false);
	}
	
	/**
	 * 
	 * 批量删除多行
	 * @param dataset 需要保存的数据集,需要将提交模式设置为："提交所有行"，否则会出现空行现象
	 * @author pos 2014-8-22
	 */
	public void deleteVOs(SuperVO[] vos) throws Exception{
		if(vos==null||vos.length==0){
			return;
		}
		IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
		cpbService.deleteSuperVOs(vos, true, true);

	}
}
