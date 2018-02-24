package nc.scap.pub.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 
 * @author xulong
 *   帆软"输出"自定义中文
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
	    // Invocable 接口: 允许java平台调用脚本程序中的函数或方法  
	    Invocable inv = (Invocable) engine;  
	    // invokeFunction()中的第一个参数就是被调用的脚本程序中的函数，第二个参数是传递给被调用函数的参数；  
	  return  (String)inv.invokeFunction("cjkEncode", chName);  
	}
}
