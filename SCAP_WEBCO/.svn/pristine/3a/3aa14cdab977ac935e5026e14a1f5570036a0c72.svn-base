package com.scap.pub.format;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.uap.portal.log.ScapLogger;

public class FormatUtil {

	/**
	 * 格式化数据方法
	 * @param pattern 分隔符号
	 * @param value 值
	 * @return
	 */
	public static String format(String pattern, Object value) {

		DecimalFormat df = null; // 声明一个DecimalFormat类的对象

		df = new DecimalFormat(pattern); // 实例化对象，传入模板

		String str = df.format(value); // 格式化数字
		ScapLogger.console(str);
		return str;
	}
	/**
	 * 千分位格式化数据
	 * @param value
	 * @return
	 */
	public static String thousandsFormat(Object value){
		return format("###,###,###,###,###.#", value);
	}
	/**
	 * 千分位格式化数据带金钱符号
	 * @param value
	 * @return
	 */
	public static String thousandsFormatWithSign(Object value){
		return format("###,###,###,###.#￥", value);
	}
	/**
	 * 千分之标识
	 * @param value
	 * @return
	 */
	public static String permillFormat(Object value){
		return format("###.###\u2030", value);
	}
	/**
	 * 百分比格式化
	 * @param value
	 * @return
	 */
	public static String PercentFormat(Object value){
		return format("##.#%", value);
	}
	
	/**
	 * 值为空是，填充“--”
	 * @param l为List<Map>形式的数据
	 */
	public static void ListMapValueNotNull(List l){
		if(l==null||l.size()==0){
		   return;	
		}
		Iterator it = l.iterator();
		while(it.hasNext()){
			Map m = (Map) it.next();
			MapValueNotNull(m);
		}
	}
	/**
	 * 值为空是，填充“--”
	 * @param l为List<value>形式的数据
	 */
	public static void ListValueNotNull(List l){
		for(int i=0 ; i < l.size() ;i++){
			if(l.get(i)==null)
				l.set(i, " ");
		}
	}
	/**
	 * 值为空是，填充“--”
	 * @param m Map形式的数据转换
	 */
	public static void MapValueNotNull(Map m){
		Set<String> set = m.keySet();
		Iterator<String> setIt = set.iterator();
		while(setIt.hasNext()){
			Object o = setIt.next();
			if(m.get(o)==null){
				m.put(o, " ");
			}
		}
	}
}