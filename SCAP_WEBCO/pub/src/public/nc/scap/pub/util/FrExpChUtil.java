package nc.scap.pub.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 
 * @author xulong
 *   ����"���"�Զ�������
 *
 */
public final class FrExpChUtil {

	
	public static String exp(String chName) throws ScriptException, NoSuchMethodException{
		ScriptEngineManager mgr = new ScriptEngineManager();    
		ScriptEngine engine = mgr.getEngineByExtension("js");  
		String js11 ="function cjkEncode(text) {        "+
				"		    if (text == null) {        "+
				"		        return \"\";        "+
				"		    }        "+
				"		    var newText = \"\";        "+
				"		    for (var i = 0; i < text.length; i++) {        "+
				"		        var code = text.charCodeAt (i);         "+
				"		        if (code >= 128 || code == 91 || code == 93) { "+
				"		            newText += \"[\" + code.toString(16) + \"]\";        "+
				"		        } else {        "+
				"		            newText += text.charAt(i);        "+
				"		        }        "+
				"		    }        "+
				"		    return newText;        "+
				"		}   ";
		engine.eval(js11);  
	    // Invocable �ӿ�: ����javaƽ̨���ýű������еĺ����򷽷�  
	    Invocable inv = (Invocable) engine;  
	    // invokeFunction()�еĵ�һ���������Ǳ����õĽű������еĺ������ڶ��������Ǵ��ݸ������ú����Ĳ�����  
	  return  (String)inv.invokeFunction("cjkEncode", chName);  
	}
}
