/**
 * @fileoverview   
 * @author gd
 * @version 5.5 
 */
IntegerTextComp.prototype = new TextComp;
IntegerTextComp.prototype.componentType = "INTEGERTEXT";

/**
 * 整形输入框
 * @class Integer类型的Text输入控件
 * @constructor integerText构造函数
 * @author gd
 * @version 1.2
 */
function IntegerTextComp(parent, name, left, top, width, position, maxValue, minValue, attrArr, className) {	
	this.base = TextComp;
	this.maxValue = getInteger(maxValue, "999999999999999");
	this.minValue = getInteger(minValue, "-999999999999999");
	if (attrArr != null) {
		if (attrArr.tip == null || attrArr.tip == "") {
			if ((minValue != null && (minValue != -999999999999999 && minValue != -2147483648))
			&& (maxValue != null && (maxValue != 999999999999999 && maxValue != 2147483647 )))
				attrArr.tip = this.minValue + "～" + this.maxValue;
			else if (minValue != null && (minValue != -999999999999999 && minValue != -2147483648))
				attrArr.tip = ">=" + this.minValue;
			else if (maxValue != null && (maxValue != 999999999999999 && maxValue != 2147483647 ))
				attrArr.tip = "<=" + this.maxValue;
		}
	}
	this.base(parent, name, left, top, width, "I", position, attrArr, className);
};   

/**
 * 处理回车事件
 * @private
 */
IntegerTextComp.prototype.processEnter = function() {
	 var inputValue = this.getValue().trim();
	 if (inputValue != "") {	 
	 	//加true是为了区别失去焦点后验证最小值为大于零的情况
		 inputValue = this.getFormater().format(inputValue,true);
		 if (inputValue == "") {
//			 showVerifyMessage(this, trans("ml_integermustbetween", [this.minValue, this.maxValue]));
//			 showVerifyMessage(this, "只能输入整形数字");
		 	//显示大小范围提示
		 	if(!this.ingrid)
				this.setErrorPosition();
			
			this.errorCenterDiv.innerHTML = trans("ml_integermustbetween", [this.minValue, this.maxValue]);
			this.errorMsgDiv.style.display = "block";
		 
			 this.input.value = ""; 
			 this.setFocus();
		 } else {
			this.setMessage(inputValue);
			this.input.value = inputValue;
		 }
	 }	 
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
IntegerTextComp.prototype.createDefaultFormater = function() {
	return new IntegerFormater(this.minValue, this.maxValue);
};

/**
 * 失去焦点时进行检测
 * @private
 */
IntegerTextComp.prototype.verify = function(oldValue) {
	if (this.newValue == "" && this.input.value != "") {
//		showVerifyMessage(this, trans("ml_integermustbetween", [this.minValue, this.maxValue]));
		//显示大小范围提示	
		if(!this.ingrid)
			this.setErrorPosition();
		this.errorCenterDiv.innerHTML = trans("ml_integermustbetween", [this.minValue, this.maxValue]);
		this.errorMsgDiv.style.display = "block";
		this.input.value = "";
		this.setMessage("");
//		return false;
	} 
//	return true;	
};


/**
 * 设置被修改的控件状态
 * @since V6.3
 * @param context 后台发生变化的值，json对象
 * @private
 */
IntegerTextComp.prototype.setChangedContext = function(context) {
	TextComp.prototype.setChangedContext.call(this,context);
	if (context.maxValue != null){
		this.setIntegerMaxValue(context.maxValue);
	}
	if (context.minValue != null){
		this.setIntegerMinValue(context.minValue);
	}
};

/**
 * 设置最大值
 * @param {Number} 最大值
 * @public
 */
IntegerTextComp.prototype.setIntegerMaxValue = function(maxValue) {
	if (maxValue != null) {	
		// 判断maxValue是否是数字
		if (isNumber(maxValue)) {	
			if (parseInt(maxValue) >= -999999999999999 && parseInt(maxValue) <= 999999999999999) {
				this.maxValue = maxValue;
				this.notifyChange(NotifyType.MAXVALUE, this.maxValue);
			}
		}	
	}
};

/**
 * 设置最小值
 * @param {Number} 最小值
 * @public
 */
IntegerTextComp.prototype.setIntegerMinValue = function(minValue) {
	if (minValue != null) {	
		// 判断minValue是否是数字
		if (isNumber(minValue)) {	
			if ((parseInt(minValue) >= -999999999999999) && (parseInt(minValue) <= 999999999999999)) {  
				this.minValue = minValue;
				this.notifyChange(NotifyType.MINVALUE, this.minValue);
			}
		}
	}
};

/**
 * 设置值
 * @param {String} 输入值
 * @public
 */
IntegerTextComp.prototype.setValue = function(text) {
	text = text == null?"":text + "";
	var valueStr = "var text = text.replace(/\\"+ window.$maskerMeta.NumberFormatMeta.markSymbol +"/g,'');";
    eval(valueStr);
	if (!checkIntegerIsValid(text, null, null)) {
		text = "";
		this.input.value = "";
		this.setMessage("");
//		this.oldValue = "";
	}
	TextComp.prototype.setValue.call(this, text + "");
};

