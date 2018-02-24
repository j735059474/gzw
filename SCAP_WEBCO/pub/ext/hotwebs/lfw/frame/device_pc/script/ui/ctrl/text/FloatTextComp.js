/**
 * @fileoverview float类型的Text输入控件.  
 *	
 * @author gd
 * @version 5.5
 * @since NC5.0
 *	
 */

FloatTextComp.prototype = new TextComp;
FloatTextComp.prototype.componentType = "FLOATTEXT";
FloatTextComp.prototype.precisionNullType = "nullType";
FloatTextComp.prototype.precisionPositiveType = "positiveType";
FloatTextComp.prototype.precisionNegativeType = "negativeType";

/**
 * 浮点型输入框构造函数
 * @class 浮点型输入框
 * @constructor floatText构造函数
 */
function FloatTextComp(parent, name, left, top, width, position, precision, maxValue, minValue, attrArr, className) {
	if(arguments.length == 0){
		return;
	}
	
	this.precision = getString(precision + "","");
	// 如果precision为空则显示和输入都不控制精度
	if(this.precision == "" || this.precision == "null" || this.precision == "undefined"){
		this.precisionType = this.precisionNullType;
	}else{
		if(getInteger(precision,0) < 0){
			// 如果precision为负数则显示控制有效精度，输入控制精度为precision绝对值
			this.precisionType = this.precisionPositiveType;
			this.precision = this.precision.substring(1);
		}else{
			// 如果precision为正数则显示和输入都控制精度
			this.precisionType = this.precisionNegativeType;
		}
	}
	this.focused = false;
	this.maxValue = getInteger(maxValue, "9999999999999999");
	this.minValue = getInteger(minValue, "-9999999999999999");
	if (attrArr != null) {
		if (attrArr.tip == null || attrArr.tip == "") {
			if (minValue != null && maxValue != null)
				attrArr.tip = this.minValue + "～" + this.maxValue;
			else if (minValue != null)
				attrArr.tip = ">=" + this.minValue;
			else if (maxValue != null)
				attrArr.tip = "<=" + this.maxValue;
		}
	}
	this.base = TextComp;
	this.base(parent, name, left, top, width, "N", position, attrArr, className);
};

/**
 * @private
 */
FloatTextComp.prototype.manageSelf = function() {
	TextComp.prototype.manageSelf.call(this);
	var oThis = this; 
	this.input.onblur = function (e) {
		e = EventUtil.getEvent();
//		if (oThis.disabled == true){
//			stopAll(e);
//			clearEventSimply(e);
//			return;
//		}
		oThis.focused = false;
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				if(typeof(children[i]) != "undefined"){
					children[i].className = children[i].className.replaceStr('input_highlight','input_normal');
				}
			}
			oThis.input.className = oThis.input.className.replaceStr('input_highlight_center_bg','input_normal_center_bg');
		}
		oThis.blur(e);
		if(!oThis.ingrid){
		if(!(typeof(oThis.input.value) == "string" && oThis.input.value.indexOf(",") != -1)){
			oThis.setValue(oThis.input.value);
		}
		}
		oThis.showTip();
		clearEventSimply(e);
	};
	//得到键盘焦点
	this.input.onfocus = function (e) {
		e = EventUtil.getEvent();
//		if (oThis.disabled == true){
//			this.blur();
//			stopAll(e);
//			clearEventSimply(e);
//			return;
//		}
		/*add by zuopf 2012-10-09*/
		//保证光标在最后
		oThis.focused = true;
		var length = oThis.input.value.length;
		if(oThis.input.createTextRange){//IE
			var r = oThis.input.createTextRange();
			r.collapse(true);
			r.moveStart('character',length);
			r.select();
		}else if(oThis.input.setSelectionRange){//Firefox
			oThis.input.setSelectionRange(length,length);
		}
		oThis.focus(e);
//		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onkeyup = function(e) {
		oThis.ctxChanged = true;
		e = EventUtil.getEvent();
		
		oThis.onkeyup(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	   	if (window.pageUI)
			window.pageUI.setChanged(true);
	};
	
};


/**
 * 处理回车事件
 * @private
 */
FloatTextComp.prototype.processEnter = function() {
	var inputValue = this.getValue().trim();
	if (inputValue != "") {
		if(this.precisionType != this.precisionNullType) // precisionType为precisionNullType时不控制精度
			inputValue = this.getFormater().format(inputValue, this.minValue, this.maxValue);
		if (inputValue == "") {
			this.input.value = "";
			this.setMessage("");
			this.setFocus();
		} else {
			this.setCorrectValue(inputValue);
		}
	}	 
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
FloatTextComp.prototype.createDefaultFormater = function() {
		return new DicimalFormater(this.precision, this.minValue, this.maxValue);
};

/**
 * 失去焦点时进行检测
 * @private
 */
FloatTextComp.prototype.blur = function() {	
	if(this.visible == false) return;
//	var value = this.input.value.trim();
	var valueStr = "var value = this.input.value.trim().replace(/\\"+ window.$maskerMeta.NumberFormatMeta.markSymbol +"/g,'').replace(/\\"+ window.$maskerMeta.NumberFormatMeta.pointSymbol +"/g,'.');";
    eval(valueStr);
//	this.newValue = value;
	if (this.dataType == 'N' && value == "") {
		this.setCorrectValue("");
		this.valueChanged(this.oldValue, value);
	}	
	if (this.dataType == 'N' && value != "") {
		if(this.oldValue != value && this.precisionType != this.precisionNullType)
			value = this.getFormater().format(value, this.minValue, this.maxValue);
			this.valueChanged(this.oldValue, value);
		// 检测输入的是否是浮点数,若不是则设置焦点要求用户重新输入
		if (value == "") {
			showVerifyMessage(this, trans("ml_decimalmustbetween", [this.minValue, this.maxValue]));
			this.input.value = "";
			this.setMessage("");
			this.setFocus();
		}
	}
	this.onblur();
};

/**
 * 获得输入焦点
 * @private
 */
FloatTextComp.prototype.focus = function (e) {
	this.warnIcon.style.display = "none";
	if(this.isError && typeof(this.errorMessage) == 'string' && this.errorMessage != ''){
		this.errorCenterDiv.innerHTML = this.errorMessage;
		this.errorMsgDiv.style.display = "block";
	}
	e = EventUtil.getEvent();
	if(this.Div_text.children.length == 3){
		var children = this.Div_text.children;
		for(var i=0;i<children.length;i++){
			children[i].className = children[i].className.replaceStr('input_normal','input_highlight');
		}
		this.input.className = this.input.className.replaceStr('input_normal_center_bg','input_highlight_center_bg');
	}
	this.input.style.color = "black";
	this.input.value = this.newValue;
	this.oldValue = this.newValue;
	if (this.visible == true) {
		this.onfocus(e);
	}
	this.hideTip();	
};

/**
 * 设置获得输入焦点
 * @public
 */
FloatTextComp.prototype.setFocus = function() {
	var oThis = this;
	if (this.visible == true){
		if(this.disabled){
			this.mayFocus=true;
		}else{
//			this.focus();
			if (IS_IE) {
				this.input.focus();
				this.input.select();
			} else {  // firefox等浏览器不能及时执行focus方法
				window.setTimeout(function(){oThis.input.focus();oThis.input.select();}, 50); 
			}
		}
	}
	this.ctxChanged = true;
};


/**
 * 设置精度
 * @param {Number} precision精度
 * @param {Boolean} 是否来自数据集
 * @private
 */
FloatTextComp.prototype.setPrecision = function(precision,fromDs) {
	fromDs = (fromDs == null) ? false : fromDs;
	if (fromDs == true){
		this.precisionFromDs = true;
	}
	//以ds设置的精度为准
	if (this.precisionFromDs != null && this.precisionFromDs == true && fromDs == false)
		return;
	this.precision = getString(precision + "","");
	// 如果precision为空则显示和输入都不控制精度
	if(this.precision == "" || this.precision =="null"){
		this.precisionType = this.precisionNullType;
	}else{
		if(getInteger(precision,0) < 0){
			// 如果precision为负数则显示不控制精度，输入控制精度为precision绝对值
			this.precisionType = this.precisionPositiveType;
			this.precision = this.precision.substring(1);
		}else{
			// 如果precision为正数则显示和输入都控制精度
			this.precisionType = this.precisionNegativeType;
		}
	}
	this.getFormater().precision = this.precision;
	
	var text = this.getValue();
	if(text != ""){
		if(this.precisionType != this.precisionNullType)
			text = this.getFormater().format(text);
		this.setValue(text);
		this.notifyChange(NotifyType.PRECISION, this.precision);	
	}
};

/**
 * 设置值
 * @param {String} text输入值
 * @pulbic
 */
FloatTextComp.prototype.setValue = function(text) {
//	this.oldValue = text;
	var valueStr = "var text = getString(text,'').trim().replace(/\\"+ window.$maskerMeta.NumberFormatMeta.markSymbol +"/g,'').replace(/\\"+ window.$maskerMeta.NumberFormatMeta.pointSymbol +"/g,'.');";
    eval(valueStr);
	if(text != null){
		text.replace(/[^\w|^.|^+|^-]/ig,'')
	}
	var textValue = parseFloat(text);
	if (isNaN(textValue)){
		textValue = "";
		text = "";
	}
//	if (textValue === this.newValue){
//		text = this.getFormater().format(text);
//		this.input.value = text;
//		return;
//	}
	this.oldValue = this.newValue;
	if(isReturning()){ // 此分支为显示  只有为正数的时候控制精度
		if(this.precisionType == this.precisionNegativeType)
			text = this.getFormater().format(text);
		if(this.precisionType == this.precisionPositiveType){ // 为负数的时候控制有效精度
			text = text + "";
			if(text.length - text.indexOf(".") - 1 < this.precision || text.indexOf(".") == -1){// 如果位数不足有效精度
				text = this.getFormater().format(text);
			}else if(text.length - text.indexOf(".") - 1 == this.precision){ // 如果位数等于有效精度
				text = text;
			}else{ // 如果位数大于有效精度
				var overLength =  text.length - text.indexOf(".") - this.precision - 1;
				for(var i = 0; i < overLength; i++){
					if(text.charAt(text.length - 1) == "0"){
						text = text.substring(0,text.length -1);
					}else{
						break;
					}
				}
			}
		}
		this.newValue = text;
	}else{ // 此分支为输入 只有为空的时候不控制精度
		if(this.oldValue != text && this.precisionType != this.precisionNullType)
	    	text = this.getFormater().format(text);
		this.newValue = text;		
	}	
	
	var masker = getMasker(this.componentType);
	if(this.focused){
		this.showValue = this.newValue;
	}else{
		if(masker != null)
			this.showValue = masker.format(this.newValue).value;
	}
	
			
	if (this.showValue == "") {
		this.input.value = "";
		this.setMessage("");
	}else{
		this.input.value = this.showValue; 
		this.setMessage(this.showValue);
	}
	
	if (this.checkTip()) {
		if (this.input.value == "")
			this.showTip();
		else
			this.input.style.color = "black";
	}
	
	if(masker.format(this.newValue).color != null)
		this.input.style.color = "red";
	else
		this.input.style.color = "black";  	
		
//	if(!this.disabled){
		if(this.newValue != this.oldValue){
			this.valueChanged(this.oldValue, this.newValue);
		}else if(typeof(this.oldValue) == "string" && this.oldValue.trim() == "" && this.newValue === 0){
			this.valueChanged(this.oldValue, this.newValue);
		}else if(this.oldValue === 0 && typeof(this.newValue) == "string" && this.newValue.trim() == ""){
			this.valueChanged(this.oldValue, this.newValue);
		}
//	}
	/*if (this.newValue != this.oldValue && !this.disabled)
		this.valueChanged(this.oldValue, this.newValue);*/
			
	this.ctxChanged = true;
};

/**
 * 设置提示信息
 * @param {String} title
 * @public
 */
FloatTextComp.prototype.setTitle = function(title) {
    if (title != null && title != "") {
        if (this.input!=null){
            this.input.title = title;
//            this.input.value = title;
        }
        if(this.Div_gen!=null)
            this.Div_gen.title = title;
    } else if (title == "") {
        if (this.input!=null){
            this.input.title = "";
//        	this.input.value = "";
        }
        if(this.Div_gen!=null)
            this.Div_gen.title = "";
    }
};

/**
 * 这个方法和上面的方法的唯一区别是这个方法设置的值一定保证正确
 * @private 私有方法 
 */
FloatTextComp.prototype.setCorrectValue = function(text) {
	this.setMessage(text);
	this.input.value = text;
};


/**
 * 设置被修改的控件状态
 * @since V6.3
 * @param context 后台发生变化的值，json对象
 * @private
 */
FloatTextComp.prototype.setChangedContext = function(context) {
	TextComp.prototype.setChangedContext.call(this,context);
	if (context.precision != null){
		this.setPrecision(context.precision);
	}	
};

/**
 * 设置最大值
 * @param {Number} maxValue最大值
 * @public
 */
FloatTextComp.prototype.setMaxValue = function(maxValue) {
	if (!isNaN(parseFloat(maxValue))) {
		this.maxValue = parseFloat(maxValue);
	}
	else
		this.maxValue = null;
};

/**
 * 设置最小值
 * @param {Number} minValue最小值
 * @pulbic
 */
FloatTextComp.prototype.setMinValue = function(minValue) {
	if (!isNaN(parseFloat(minValue))) {
		this.minValue = parseFloat(minValue);
	} 
	else
		this.minValue = null;
};
	