/**
 * @fileoverview Text输入控件基类,所有的输入控件均继承此控件
 * DataType: A:字符,R:reference 
 * @author gd, guoweic
 * @version 6.3
 * @since 5.5 
 */
TextComp.prototype = new BaseComponent;
TextComp.prototype.componentType = "TEXT";

TextComp.prototype.inputClassName_init = IS_IE7? "text_input" : "input_normal_center_bg text_input";
TextComp.prototype.inputClassName_inactive = IS_IE7? "text_input_inactive" : "input_disable_center_bg text_input_inactive";
TextComp.prototype.inputClassName_readonly = IS_IE7? "text_input_inactive" : "input_readonly_center_bg text_input_inactive";
/*显示文字和文本之间间距*/
TextComp.prototype.label_input_margin = 10;
/**
 * @description 文本输入框控件构造函数
 * @class 文本输入框控件基类
 * @constructor TextComp的构造函数
 */
function TextComp(parent, name, left, top, width, dataType, position, attrArr, className) {
	if (arguments.length == 0)
		return;
	
	this.base = BaseComponent;
	this.base(name, left, top, width, "");  
	this.dataType = getString(dataType, "A");		
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	if (this.dataType == "P")
		this.inputType= "password";
	else if (this.dataType == "HI")
		this.inputType = "hidden";
   	else if (this.dataType == "C")
   		this.inputType = "checkbox";
   	else if (this.dataType == "F") {
   		this.inputType = "file";
   	}
   	else
		this.inputType= "text";
	
	//是否大写
	this.uppercase = false;
	//输入框默认大小
	this.maxSize = -1;
	this.value = getString(this.value, "");
	this.disabled = false;
	this.readOnly = false;
	this.nullable = true;
	//准备Focus 
	this.mayFocus = false;
	// 当前是否是校验失败状�?
	this.isError = false;
	// 当前输入框校验状态，包括：BaseComponent.ELEMENT_ERROR、BaseComponent.ELEMENT_WARNING、BaseComponent.ELEMENT_NORMAL、BaseComponent.ELEMENT_SUCCESS，默认为BaseComponent.ELEMENT_SUCCESS
	this.checkResult = BaseComponent.ELEMENT_NORMAL;
	this.message;
	// 错误信息（包括title和错误提示）
	this.errorMessage;
	this.focused = false;
	//设计态
	this.designModel = false;
	if (attrArr != null) {   
		this.maxSize = getInteger(attrArr.maxSize, this.maxSize);	
		this.disabled = getBoolean(attrArr.disabled, false);	 
		this.nullable = getBoolean(attrArr.nullable, true);
		this.value = getString(attrArr.value, this.value);		
		this.readOnly = getBoolean(attrArr.readOnly, false);
		if(IS_IE8){
			if(this.readOnly){
				this.readOnly = false;
				this.disabled = true;
			}
		}
		this.tabIndex = getInteger(attrArr.tabIndex, -1);
		this.tip = getString(attrArr.tip, "");
		//提示信息
		this.showTipMessage = getString(attrArr.showTipMessage, "");
		this.designModel = getBoolean(attrArr.designModel, false);
		// 输入辅助提示信息
		this.inputAssistant = getString(attrArr.inputAssistant, "");
		if (attrArr.height != null)
			this.height = convertHeight(attrArr.height);
		
		//TODO 标签属性
		if (this.dataType != "C" && this.dataType != "RA") {
			this.labelText = getString(attrArr.labelText, "");
			if ("" != this.labelText)
				this.hasLabel = true;
			this.labelAlign = getString(attrArr.labelAlign, "left");
			this.labelWidth = getInteger(attrArr.labelWidth, 0);
			if (0 == this.labelWidth && "" != this.labelText)
				this.labelWidth = getTextWidth(this.labelText);
		}
		
		// 是否阻止document.click();方法的执行（用于在打开的Div中定义该输入框的情况�?
		this.stopHideDiv = attrArr.stopHideDiv == true ? true : false;
		
	}	  
	// 只有字符串类型才需要此处理
	if (this.dataType == 'A' || this.dataType == 'P') {
		if( this.maxSize != -1) {
			// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
			if (this.value.lengthb() > this.maxSize)
				this.value = this.value.substrCH(this.maxSize);
		}		
	}
	//让控件失去焦点的外部事件回掉
	if(IS_IPAD){
		window.clickHolders.push(this);
	}
	
	this.className = getString(className, "text_div");
	this.create();
};

/**
 * @description 设置编辑公式
 * @param {String} editFormular
 * @private
 */
TextComp.prototype.setEditFormular = function(editFormular) {
	this.editFormular = editFormular;
};

/**
 * @description 设置验证公式
 * @param {String} validateFormular
 * @private
 */
TextComp.prototype.setValidateFormular = function(validateFormular) {
	this.validateFormular = validateFormular;
};

/**
 * @description 设置提示信息
 * @param {String} tip
 */
TextComp.prototype.setTip = function(tip) {
	this.tip = tip;
};

/**
 * @description 对newvalue的前处理，供子类覆写
 * @param {String} value
 * @return {String} value
 * @private
 */
TextComp.prototype.postProcessNewValue = function(value) {
	return value;
};

/**
 * @description 真正的创建函数
 * @private
 */
TextComp.prototype.create = function() {		
	var oThis = this;
	this.Div_gen = $ce("DIV");
	
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width == null ? '120px' : this.width;
	this.Div_gen.style.height = "100%";
  	
  	this.initErrorMsg();
  	
	if (this.parentOwner) 
		this.placeIn(this.parentOwner);
	
	if (this.readOnly)
		this.setReadOnly(this.readOnly);
	
	this.ctxChanged = false;
};

/**
 * @description TextComp的二级回掉函数
 * @private
 */
TextComp.prototype.manageSelf = function() {
	
	this.Div_text = $ce("DIV");
	this.Div_text.id = this.id + "_textdiv";
	this.Div_text.className = this.className;
	this.Div_text.style.position = "relative";
	this.Div_text.style.top = "0px";
	// 解决部分IE9上边框不显示的问题
//	if(IS_IE9){
//		this.Div_text.style.top = "1px";
//	}
	var width = 0;
	if (isPercent(this.width))
		width = this.Div_gen.offsetWidth;
	else
		width = getInteger(parseInt(this.width), 120);
	if(width == 0){
		if(this.parentOwner.style.width && this.parentOwner.style.width.indexOf("%") == -1){
			width = parseInt(this.parentOwner.style.width,10);
		}
		if(width == 0){
			width = 120;
		}
	}
	this.Div_text.style.width = (width - 4) < 0?0:(width - 4) + "px";
	if(IS_FF)
		this.Div_text.style.height = 23 +"px";
	if (this.hasLabel)
		this.Div_text.style.width = (width - this.labelWidth - this.label_input_margin) < 0?0:(width - this.labelWidth - this.label_input_margin) + "px";
  		this.Div_text.style.overflow = "hidden";

	if (this.hasLabel) {
		this.labelDiv = $ce("DIV");
		this.labelDiv.style.width = (this.labelWidth) < 0?0:(this.labelWidth) + "px";
		
		if (this.labelAlign == "left") {
			this.labelDiv.style[ATTRFLOAT] = "left";
			this.Div_text.style[ATTRFLOAT] = "right";
			this.Div_gen.appendChild(this.labelDiv);
		  	this.Div_gen.appendChild(this.Div_text);
		} else if (this.labelAlign == "right") {
			this.Div_text.style[ATTRFLOAT] = "left";
			this.labelDiv.style[ATTRFLOAT] = "right";
		  	this.Div_gen.appendChild(this.Div_text);
			this.Div_gen.appendChild(this.labelDiv);
		}
		
		this.label = new LabelComp(this.labelDiv, this.id + "_label", "0px", "3px", this.labelText, "relative", "textcomp_normallabel");
	} else {
		this.Div_gen.appendChild(this.Div_text);
	}
	
	var width = parseInt(this.Div_text.style.width, 10);
	width = width < 10 ? 10 : width;
//	if (IS_IPAD){
//		width = width - 10;
//	}
	var oThis = this;
	this.input = $ce("INPUT");
	this.input.type = this.inputType;
	if(!IS_STANDARD && (this.input.type == 'text' || this.input.type == 'password')){
		this.input.style.paddingTop = "4px";
	}
	if(IS_IPAD || IS_SAFARI){
		this.input.style.paddingLeft = "0px";
		this.input.style.paddingRight = "0px"
	}
	
	var left_div = $ce("DIV");
	left_div.className = "input_normal_left_bg";
	this.Div_text.appendChild(left_div);
	
	var center_div = $ce("DIV");
	center_div.className = "input_normal_center_div_bg";
	center_div.style.width = (width - 3*2) + "px";//3*2左右边框图片宽度
	center_div.appendChild(this.input);
	this.Div_text.appendChild(center_div);
	if(IS_IE7){
		this.input.style.marginTop = "3px";
	}
	
	var right_div = $ce("DIV");
	right_div.className = "input_normal_right_bg";
	this.Div_text.appendChild(right_div);
	
	// 数字型控件的数字居右显示
	if (this.dataType == "I" || this.dataType == "N"){
		this.input.style.textAlign = "right";
		this.input.style.imeMode = "disabled";//禁用输入法
	}
	this.input.name = this.id;
	if (this.tabIndex != -1)
		this.input.tabIndex = this.tabIndex;
	if (this.maxSize != -1)
		this.input.maxLength = this.maxSize;
	
	this.input.readOnly = this.readOnly;

	/** Ios **/
//	if(IS_IOS)
//		this.input.style.width = (width - 6 - (IS_IPAD?10:0)) + "px" ;
//	else
//		this.input.style.width = (width - 10 - (IS_IPAD?10:0)) + "px";
		this.input.style.width = (width - 10) + "px";

	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = width - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
//		this.input.style.width = (centerWidth - 2*2 - (IS_IPAD?10:0)) + "px";//2*2 input输入框距离左右间�?
		this.input.style.width = (centerWidth - 2*2) + "px";//2*2 input输入框距离左右间�?
	}
	/********/
	if (!IS_IE6)
		this.input.style.height = "100%";
	this.input.className = this.inputClassName_init;//"input_normal_center_bg text_input";
	
	if (this.disabled) {
		this.input.disabled = true;	
//		this.input.readOnly = true;	
	   	this.input.className = this.inputClassName_inactive;//"input_normal_center_bg text_input_inactive";
	   	if(this.Div_text != null){
			this.Div_text.className = this.className + " text_inactive_bgcolor";
			if(this.Div_text.children.length == 3){
				var children = this.Div_text.children;
				for(var i=0;i<children.length;i++){
					if(typeof(children[i]) != "undefined"){
						children[i].className = children[i].className.replaceStr('input_normal','input_disable');
					}
				}
			}
	   	}
	}
	//在编辑状态将input置为不可见
	if(window.editMode){
		this.input.style.visibility = "hidden";
	}
	this.input.onkeyup = function(e) {
		oThis.ctxChanged = true;
		e = EventUtil.getEvent();
		//处理输入中文的情况，中文输入过程中不会触发onkeyPress
		//只校验长度 如果超长之后对长度进行处理
		if (oThis.dataType == 'A' || oThis.dataType == 'P') {
			if (oThis.maxSize != -1) {
				var text = oThis.input.value;
				// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
				if (text.lengthb() > oThis.maxSize){
					text = text.substrCH(oThis.maxSize);
					oThis.input.value = text;
				}
			}
		}
		oThis.onkeyup(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	   	if (window.pageUI)
			window.pageUI.setChanged(true);
	};
	this.input.onkeydown = function(e) {
		oThis.ctxChanged = true;
		e = EventUtil.getEvent();
		var keyCode = e.lfwKey;
				
		if (oThis.onkeydown(e) == false) {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		var con = (keyCode == 13 || (keyCode == 9 && e.shiftKey) || keyCode == 9);
		if (this.readOnly == true && !con) {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return false;
		}
		if (keyCode == 8 || keyCode == 46)
			oThis.haschanged(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	//当用户按下一个键或放开一个键时调用
	this.input.onkeypress = function (e) {
		oThis.ctxChanged = true;
		e = EventUtil.getEvent();
		var keyCode = e.lfwKey;
		// readOnly时不允许输入字母和汉字
		// "enter"键即使在readOnly下也是允许输入
		var con = (keyCode == 13 || (keyCode == 9 && e.shiftKey) || keyCode == 9);
		// readOnly时不允许输入
		if (this.readOnly == true && !con) {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return false;
		}
		//得到格式化器
		var formater = oThis.getFormater();
		//获取输入字符
		if (keyCode == 13) {
//			oThis.newValue = oThis.input.value;
			if(oThis.dataType == "N"){// floattextcomp:precisionType为precisionNullType时不控制精度
				if(oThis.precisionType != oThis.precisionNullType){
					oThis.newValue = oThis.getFormater().format(oThis.input.value);		
				}else{
					oThis.newValue = oThis.input.value;
				}
			}else{
				oThis.newValue = oThis.getFormater().format(oThis.input.value);		
			}
			if (oThis.processEnter)
				oThis.processEnter();
			//form里的控件不blur(因为下个element获得焦点，而自身blur)
			if (typeof(oThis.fieldId) == 'undefined')
				oThis.input.blur();
			// IE下blur之后不会立刻执行onblur因此onenter要延迟执行
			if(IS_IE){
				setTimeout(function(){oThis.onenter(e);}, 100);	
			}else{
				oThis.onenter(e);	
			}
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			//回车后失去焦点
			return true;
		}
		//for firefox
		else if (keyCode == 8) {
			oThis.haschanged(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return true;
		}
		//ctrl + v
		else if (keyCode == 118 && e.ctrlKey == true){
			clearEventSimply(e);
			return true;
		}
		
		// firefox中忽略校�?左方向键、右方向键、Tab�?
		if (!IS_IE && (keyCode == 37 || keyCode == 39 || keyCode == 9)) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return true;
		}
		var key;
		if (keyCode) {	
			//从字符编码创建一个字符串,字符串中的每个字符串都由单独的数字Unicode编码指定
			key = String.fromCharCode(keyCode);
		}
		//IE下增加录入时即校验的特性，在firefox下还无法实现（guoweic: 已实现）
		var currValue = oThis.input.value;
		if (oThis.dataType != "F") {
			var aggValue = insertAtCursor(this, key);
		}
		//guoweic: firefox中insertAtCursor方法只能得到输入前的�?
		if (!IS_IE && aggValue == "") {
			aggValue = key;
		}
		//this.focus(e);
		if (IS_IE) {
			if(currValue == ""){
				oThis.input.value = "";
			}else{
				// guoweic: 该方法只有IE支持
				document.execCommand("undo");
			}
		}
		// 获取keyValue,currValue,aggValue,
		if(oThis.dataType == "N"){// floattextcomp:precisionType为precisionNullType时不控制精度
			if(oThis.precisionType != oThis.precisionNullType){
				if (formater.valid(key, aggValue, currValue) == false) {
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
					return false;
			   	}
			}
		}else{
			if (formater.valid(key, aggValue, currValue) == false) {
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return false;
		   	}
		}
	   	//验证完成输入合法性后调用程序的内部处�?
	   	if (oThis.afterValid(keyCode, key) == false) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
	   		return false;
	   	}
		// 删除事件对象（用于清除依赖关系）
	   	if (window.pageUI)
			window.pageUI.setChanged(true);
		clearEventSimply(e);
	   	return true;
	};
	
	//得到键盘焦点时调用
	this.input.onfocus = function (e) {
		e = EventUtil.getEvent();
//		if (oThis.disabled == true){
//			this.blur();
//			stopAll(e);
//			clearEventSimply(e);
//			return;
//		}
		oThis.focused = true;
		oThis.warnIcon.style.display = "none";
		if(oThis.isError && typeof(oThis.errorMessage) == 'string' && oThis.errorMessage != ''){
			oThis.errorCenterDiv.innerHTML = oThis.errorMessage;
			if(!oThis.noShowErrorMsgDiv){
				oThis.errorMsgDiv.style.display = "block";
			}
		}
		
		//获得焦点时把最大值最小值范围提示隐藏
		if(oThis.isError == false && oThis.dataType == 'I')
			oThis.errorMsgDiv.style.display = "none";
			
		if (oThis.input.value != oThis.newValue){
			var value = oThis.postProcessNewValue(oThis.newValue);
			oThis.input.value = value;
		}
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				children[i].className = children[i].className.replaceStr('input_normal','input_highlight');
			}
			oThis.input.className = oThis.input.className.replaceStr('input_normal_center_bg','input_highlight_center_bg');
		}
		
		/*add by zuopf 2012-10-09*/
		//保证光标在最后
		var length = oThis.input.value.length;
		if(oThis.input.createTextRange){//IE
			var r = oThis.input.createTextRange();
			r.collapse(true);
			r.moveStart('character',length);
			r.select();
		}else if(oThis.input.setSelectionRange){//Firefox
			oThis.input.setSelectionRange(length,length);
		}
		
		/********/
		oThis.focus(e);			
		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//失去键盘焦点时调用
	this.input.onblur = function (e) {
		e = EventUtil.getEvent();
//		if (oThis.disabled == true){
//			stopAll(e);
//			clearEventSimply(e);
//			return;
//		}
		oThis.focused = false;
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				if(typeof(children[i]) != "undefined"){
					children[i].className = children[i].className.replaceStr('input_highlight','input_normal');
				}
			}
			oThis.input.className = oThis.input.className.replaceStr('input_highlight_center_bg','input_normal_center_bg');
		}
		/********/
		oThis.blur(e);
		//var newValue = oThis.getFormater().format(this.value);
		if(!oThis.ingrid){
			oThis.setValue(oThis.input.value);
		}
		oThis.showTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onselect = function (e) {
		e = EventUtil.getEvent();
		oThis.onselect(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onmouseover = function (e) {
		e = EventUtil.getEvent();
		oThis.onmouseover(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onclick = function(e) {
		oThis.warnIcon.style.display = "none";
		if(oThis.isError && typeof(oThis.errorMessage) == 'string' && oThis.errorMessage != ''){
			oThis.errorCenterDiv.innerHTML = oThis.errorMessage;
			if(!oThis.noShowErrorMsgDiv){
				oThis.errorMsgDiv.style.display = "block";
			}
		}
		e = EventUtil.getEvent();
		oThis.onclick(e);
		window.clickHolders.trigger = oThis;
		document.onclick(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onpaste = function(e){
		if (IS_SAFARI){
			var text = e.clipboardData && e.clipboardData.getData ? e.clipboardData.getData('text/plain') : window.clipboardData && window.clipboardData.getData ? window.clipboardData.getData('Text') : false;
			if (text)
				oThis.setValue(text);
			return false;
		}
		if (oThis.onPaste){
			var result = oThis.onPaste.call(oThis, e);
			if (result == false){
				document.onclick(e);
				return false;
			}
			else
				return true;
		}
		else
			return true;
	};
	//设置初始
	if (this.value != null)
		this.setValue(this.value);
	else
		this.showTip();

};

/**
 * @description 设置当前是否是校验失败状态
 * @param {String} error
 * @private
 */
TextComp.prototype.setError = function(error) {
	this.isError = error;
};

TextComp.prototype.setValgin = function(valgin) {
	this.valgin = valgin;
	this.Div_gen.style.display = "table-cell";
	this.Div_gen.style.verticalAlign = "middle";
};

/**
 * @description 校验是否有默认提示信息
 * @private
 */
TextComp.prototype.checkTip = function() {
	if (this.tip != null && this.tip != "" && this.input.type == "text")
		return true;
	else
		return false;
};

/**
 * @description 显示提示信息
 * @private
 */
TextComp.prototype.showTip = function() {
	if (this.checkTip()) {
		if (this.input.value == "") {
			if(this.isRequired)
				this.input.value = this.tip + "   ";			
			else
				this.input.value = this.tip;
			this.input.style.color = "gray";
		}
	}
};

/**
 * @decription 隐藏提示信息
 * @private
 */
TextComp.prototype.hideTip = function() {
	if (this.checkTip()) {
		if (this.input.value == this.tip) {
			this.input.value = "";
			this.input.style.color = "black";
		}
	}
};

/**
 * 设置控件的格式化
 * @param formater 格式化器实例
 * @private
 */
TextComp.prototype.setFormater = function(formater) {
	this.formater = formater;	
};

/**
 * @private
 */
TextComp.prototype.setTabIndex = function(index) {
	this.input.tabIndex = index;
};

/**
 * 得到格式化器
 * @private
 */
TextComp.prototype.getFormater = function() {
	if(this.formater == null)
		return	this.createDefaultFormater();
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
TextComp.prototype.createDefaultFormater = function() {
	return new StringFormater(this.maxSize);
};

/**
 * 输入验证合法�?要做的事�?比如处理回车事件�?子类必须覆盖此函�?
 * @param key 键盘输入的字�?
 * @param keyCode 键盘码�?
 * @private
 */
TextComp.prototype.afterValid = function(keyCode, key) {
};

/**
 * 设置组件大小及位�?
 */
TextComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(convertWidth(width), "100%");
	this.height = getString(convertHeight(height), "100%");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	
	if (this.Div_text != null) {
		var tempWidth = 0;
		if (isPercent(this.width))
			tempWidth = this.Div_gen.offsetWidth;
		else
			tempWidth = getInteger(parseInt(this.width), 120);
		
		this.Div_text.style.width = tempWidth - 4 + "px";
		if (this.hasLabel)
			this.Div_text.style.width = tempWidth - this.labelWidth - this.label_input_margin + "px";
			
		//this.Div_text.style.height = this.height - 4 + "px";
		var pixelWidth = parseInt(this.Div_text.style.width, 10);
//		if (IS_IPAD){
//			pixelWidth = pixelWidth - 10;
//		}
		/*add by chouhl 2012-1-10*/
		if(this.Div_text.children.length == 3){
			var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
			this.Div_text.children[1].style.width = centerWidth + "px";
//			this.input.style.width = (centerWidth - 2*2 - (IS_IPAD?10:0)) + "px";//2*2 input输入框距离左右间�? cwh ipad
			this.input.style.width = (centerWidth - 2*2 ) + "px";//2*2 input输入框距离左右间�? cwh ipad
		}
		/********/
		//this.input.style.height = this.height - 4 + "px";
	}
};

/**	
 * 控件外部右键点击事件回调函数。须关闭此控件的下拉菜单
 * @private
 */
TextComp.prototype.outsideContextMenuClick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("outsideContextMenuClick", MouseListener.listenerType, mouseEvent);
	this.doEventFunc("outsideContextMenuClick", mouseEvent);
};

/**	
 * 控件外部点击事件回调函数。须关闭此控件的下拉菜单
 * @private
 */
TextComp.prototype.outsideClick = function(e) {
	if (window.clickHolders.trigger == this)
		return;
	//控件外部点击事件回调函数。需失去焦点
	if(IS_IPAD){
		this.input.blur(e);
	}
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("outsideClick", MouseListener.listenerType, mouseEvent);
	this.doEventFunc("outsideClick", mouseEvent);
};

/**
 * 选择事件
 * @private
 */
TextComp.prototype.onselect = function () {
	if(this.maxSize != -1)
		this.input.value.maxLength = (this.maxSize + 1);
	
	var simpleEvent = {
			"obj" : this
		};
//	this.doEventFunc("onselect", TextListener.listenerType, simpleEvent);
	this.doEventFunc("onselect", simpleEvent);
};

/**
 * 得到键盘焦点�?
 * @private	
 */
TextComp.prototype.focus = function (e) {	
	// 输入框必须先获得焦点然后再调用select()方法(并不是所有的浏览器都要求这样,但安全起�?最好每次都先调用focus())
	// this.input.select();
//	this.oldValue = this.input.value;
	this.oldValue = this.newValue;
	// 为避免tab键进入密码框时不能输入字符的bug
	if (this.visible == true) {
		if (this.dataType == "P") {	
			this.input.select();
		}
		this.onfocus(e);
	}
};
		   
/**
 * 失去焦点时进行输入的类型检验
 * @private
 */
TextComp.prototype.blur = function(e) {
	if (this.visible == true) {
		this.input.value = this.input.value.trim();
		var value = this.input.value;
		if (this.dataType!='P') {
			if(this.showTipMessage && this.showTipMessage != null)
				this.setMessage(this.showTipMessage);
			else
				this.setMessage(value);
		}
		if(this.dataType != 'I'){
			this.newValue = this.getFormater().format(value);
		}else{
			//integer类型时验证最小值小于零的情况
			this.newValue = this.getFormater().format(value,true);
		}
		var verifyR = this.verify(value);
		
		if(this.dataType == 'I')
			this.input.value = this.newValue;
		
//		if(verifyR == null || verifyR){
			if (this.newValue != this.oldValue)
				this.valueChanged(this.oldValue, this.newValue);
//		}
   		this.onblur(e);
   		if (this.nullable == false && isNull(this.newValue) && this.isError == false){
   			this.setError(true);
   			this.setErrorStyle();
//   			this.setWarningStyle();
//   			this.setErrorMessage(trans('ml_thisfieldcannotnull'));
   		}
	}
};

/**
 * @description 验证方法，供子类覆写
 * @param {String} oldValue
 * @return {Boolean}
 * @private
 */
TextComp.prototype.verify = function(oldValue) {
	return true;
};

/**
 * @description 设置提示信息
 * @param {String} message
 * @private
 */
TextComp.prototype.setMessage = function(message) {
	if (!this.isError) {
		this.message = message;
		this.errorMessage = "";
		this.setTitle(message);
	}
};

/**
 * @description 设置错误提示信息
 * @param {String} errorMessage
 * @private
 */
TextComp.prototype.setErrorMessage = function(errorMessage) {
	if (this.isError) {
		this.message = "";
		this.errorMessage = errorMessage;
		this.errorCenterDiv.innerHTML = this.errorMessage;
		//this.setTitle(errorMessage);
	}
};

/**
 * @description 设置标题
 * @modify licza 统一设置标题显示方式,子类可以覆盖实现自定义显示方式
 * @private
 */
TextComp.prototype.setTitle = function(title) {
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
 * @description 设置输入框的label
 * @param {String} text
 * @pulbic
 */
TextComp.prototype.setLabelText = function(text) {
       	var label = this.getLabel();
        if (label)
        	label.changeText(text);
          else{//动态创建label
        	if (!isNull(text)) {
				this.labelText = text;  
				this.hasLabel = true;
				this.labelDiv = $ce("DIV");
				this.labelWidth = getTextWidth(this.labelText);
				this.setLabelTextWidth(this.labelWidth);
				if (this.labelAlign == "left") {
					this.labelDiv.style[ATTRFLOAT] = "left";
					this.Div_text.style[ATTRFLOAT] = "right";
					this.Div_gen.appendChild(this.labelDiv);
				} else if (this.labelAlign == "right") {
					this.Div_text.style[ATTRFLOAT] = "left";
					this.labelDiv.style[ATTRFLOAT] = "right";
					this.Div_gen.appendChild(this.labelDiv);
				}		
				this.label = new LabelComp(this.labelDiv, this.id + "_label", "0px", "3px", this.labelText, "relative", "textcomp_normallabel");
        	}
        }	
        this.labelText = text;
};

/**
 * @description 设置是否显示label
 * @param {Boolean} isShowLabel
 * @public 
 */
TextComp.prototype.isShowLabel = function(isShowLabel) {
	if(!isShowLabel){
		this.labelDiv.style.display = "none";
		this.labelDiv.style.width = "0px";
		this.Div_gen.style.width = this.Div_text.style.width;		
	}
};

/**
 * @description 设置labe相对于输入框的左右位置
 * @param {String} aligh labe左右位置
 * @pulbic
 */
TextComp.prototype.setLabelTextAligh = function(aligh) {
	if (this.hasLabel) {  	
		if ("left" == aligh && this.labelAlign != "left") {
			this.labelDiv.style[ATTRFLOAT] = "left";
			this.Div_text.style[ATTRFLOAT] = "right";
			this.labelAlign = "left";
			this.notifyChange(NotifyType.TEXTALIGN, this.labelAlign);
		} else if ("right" == aligh && this.labelAlign != "right") {
			this.Div_text.style[ATTRFLOAT] = "left";
			this.labelDiv.style[ATTRFLOAT] = "right";
			this.labelAlign = "right";
			this.notifyChange(NotifyType.TEXTALIGN, this.labelAlign);
		}
	}
};

/**
 * @description 设置输入框label的长度
 * @param {String} width 输入框label的长度
 * @pulbic
 */
TextComp.prototype.setLabelTextWidth = function(width) {
	if (this.hasLabel){
		if (isPercent(width))
			var tempWidth = this.labelDiv.offsetWidth;
		else
			tempWidth = getInteger(parseInt(width));
		this.labelWidth = tempWidth;
		this.notifyChange(NotifyType.WIDTH, this.labelWidth);
		this.labelDiv.style.width =  tempWidth + "px";
//		var allWidth = this.Div_gen.offsetWidth;
		var allWidth = this.parentOwner.offsetWidth;
		this.Div_text.style.width = allWidth - this.labelWidth - this.label_input_margin + "px";
		var pixelWidth = parseInt(this.Div_text.style.width, 10);
//		if (IS_IPAD){
//			pixelWidth = pixelWidth - 10;
//		}
		/*add by chouhl 2012-1-10*/
		if(this.Div_text.children.length == 3){
			var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
			this.Div_text.children[1].style.width = centerWidth + "px";
//			this.input.style.width = (centerWidth - 2*2 - (IS_IPAD?10:0)) + "px";//2*2 input输入框距离左右间�?//cwh ipad处理
			this.input.style.width = (centerWidth - 2*2 ) + "px";//2*2 input输入框距离左右间�?//cwh ipad处理
		}
	}
};

/**
 * @description 设置输入框文字字体
 * @param {String} family文字字体
 * @public
 */
TextComp.prototype.setFamily = function(family) {
	this.family = family;
	this.input.style.fontFamily = this.family;
};

/**
 * @description 设置输入框文字字体大小
 * @param {String} size文字字体大小
 * @public
 */
TextComp.prototype.setSize = function(size) {
	this.size = size;
	this.input.style.fontSize = this.size + "px";
};

/**
 * @description 设置输入框文字颜色
 * @param {String} color 文字颜色
 * @public
 */
TextComp.prototype.setColor = function(color) {
	this.color = color;
	this.input.style.color = this.color;
};

/**
 * @description 设置输入框文字字体粗细（normal, bold, bolder, lighter, 100-900）
 * @param {String} weight文字字体粗细
 * @public 
 */
TextComp.prototype.setWeight = function(weight) {
	this.weight = weight;
	this.input.style.fontWeight = this.weight;
};

/**
 * @description 设置文字样式（normal, italic, oblique）
 * @param {String} style 文字样式
 * @public 
 */
TextComp.prototype.setStyle = function(style) {
	this.style = style;
	this.input.style.fontStyle = this.style;
};

/**
 * @description 设置输入框背景色
 * @param {String} bgcolor 背景色
 * @public 
 */
TextComp.prototype.setBgcolor = function(bgcolor) {
	this.bgcolor = bgcolor;
	
};

/**
 * @description 设置校验结果
 * @param checkResult 校验结果类型，包括：BaseComponent.ELEMENT_ERROR、BaseComponent.ELEMENT_WARNING、BaseComponent.ELEMENT_NORMAL、BaseComponent.ELEMENT_SUCCESS
 * @private
 */
TextComp.prototype.setCheckResult = function(checkResult) {
	this.checkResult = checkResult;
};

/**
 * @description 显示浮动的提示信息
 * @private
 */
TextComp.prototype.showFloatMessageDiv = function() {
	// 要显示的信息
	var text = "";
	// 提示信息类型，包括：BaseComponent.ELEMENT_ERROR、BaseComponent.ELEMENT_WARNING、BaseComponent.ELEMENT_NORMAL、BaseComponent.ELEMENT_SUCCESS
	var messageType = this.checkResult;
	if (messageType == BaseComponent.ELEMENT_SUCESS) {  // 当前状态为成功，则直接返回
		return;
	}
	else if (messageType == BaseComponent.ELEMENT_ERROR || messageType == BaseComponent.ELEMENT_WARNING) {  // 获取错误提示或警告提�?
		if (this.isError && this.errorMessage != null && this.errorMessage != "") {
			text = this.errorMessage;
		} else {
			return;
		}
	} 
	else if (messageType == BaseComponent.ELEMENT_NORMAL) {  // 获取常规输入辅助提示
		if (this.inputAssistant != null && this.inputAssistant != "") {
			text = this.inputAssistant;
		} else {
			return;
		}
	}
	// 计算文字宽度
	var textWidth = getTextWidth(text, this.className + "_FLOAT_MESSAGE_TEXT");
	if (textWidth == null || textWidth < 150)
		textWidth = 150;
	if (!window.floatMessageDiv) {  // 创建浮动提示�?
		// 信息浮动�?
		var floatMessageDiv = $ce("div");
		floatMessageDiv.style.display = "none";
		floatMessageDiv.style.position = "absolute";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法�?	
//		floatMessageDiv.style.zIndex = "999999";
		floatMessageDiv.style.zIndex = getZIndex();
		window.floatMessageDiv = floatMessageDiv;
		document.body.appendChild(floatMessageDiv);
		
		// 左侧DIV
		var leftDiv = $ce("div");
		leftDiv.className = "div_left";
		window.floatMessageDiv.appendChild(leftDiv);
		
		// 右侧DIV
		var rightDiv = $ce("div");
		rightDiv.className = "div_right";
		window.floatMessageDiv.appendChild(rightDiv);
		
		// 中间DIV
		var centerDiv = $ce("div");
		centerDiv.className = "div_center";
		window.floatMessageDiv.appendChild(centerDiv);
		window.floatMessageDiv.centerDiv = centerDiv;
		
		// 箭头DIV
		var arrowDiv = $ce("div");
		arrowDiv.className = "div_arrow";
		window.floatMessageDiv.appendChild(arrowDiv);
		
		// 文字DIV
		var textDiv = $ce("div");
		textDiv.className = "div_text";
		window.floatMessageDiv.appendChild(textDiv);
		window.floatMessageDiv.textDiv = textDiv;
		
	}
	// 重新调整样式、大小、显示位置、显示内�?
	if (messageType == BaseComponent.ELEMENT_ERROR)  // 设置错误提示样式
		window.floatMessageDiv.className = this.className + "_float_message_div_error";
	else if (messageType == BaseComponent.ELEMENT_WARNING)  // 设置警告提示样式
		window.floatMessageDiv.className = this.className + "_float_message_div_warning";
	else if (messageType == BaseComponent.ELEMENT_NORMAL)  // 设置常规提示样式
		window.floatMessageDiv.className = this.className + "_float_message_div_normal";
	// 重新调整大小
	window.floatMessageDiv.style.width = (textWidth + getCssHeight(this.className + "_FLOAT_MESSAGE_LEFT_WIDTH") + getCssHeight(this.className + "_FLOAT_MESSAGE_RIGHT_WIDTH")) + "px";
	window.floatMessageDiv.centerDiv.style.width = textWidth + "px";
	// 重新调整显示位置
//	window.floatMessageDiv.style.top = (compOffsetTop(this.Div_gen, document.body) - getCssHeight(this.className + "_FLOAT_MESSAGE_HEIGHT")) + "px";
//	window.floatMessageDiv.style.left = (compOffsetLeft(this.Div_gen, document.body) - 20) + "px";
	window.floatMessageDiv.style.top = (compOffsetTop(this.Div_gen, document.body) + this.Div_gen.offsetHeight) + "px";
	window.floatMessageDiv.style.left = compOffsetLeft(this.Div_gen, document.body) + "px";
	// 重新调整内容
	window.floatMessageDiv.textDiv.innerHTML = text;
	// 显示
	window.floatMessageDiv.style.display = "block";
};

/**
 * @description 隐藏浮动的提示信息
 * @private
 */
TextComp.prototype.hideFloatMessageDiv = function() {
	if (window.floatMessageDiv) {
		window.floatMessageDiv.style.display = "none";
	}
};

/**
 * @description 获得输入焦点
 * @private
 */
TextComp.prototype.setFocus = function() {
	if (this.visible == true){
		if(this.disabled){
			this.mayFocus=true;
		}else{
			var oThis = this;
			if (IS_IE) {
				this.input.focus();
				this.input.select();
			} else {  // firefox等浏览器不能及时执行focus方法
				window.setTimeout(function(){if (oThis.input){ oThis.input.focus();oThis.input.select();}}, 50); 
			}
		}
	}
	this.ctxChanged = true;
};

/**
 * @description 得到value
 * @return {String} value
 * @private
 */
TextComp.prototype.getValue = function() {
	//if (this.checkTip()) {
	//	if (this.input.value == this.tip && this.input.style.color != "black")
	//		return "";
	//}
	if(this.newValue != null){
		return this.newValue;
	}else if(this.value != null){
		return this.value;
	}else{
		return "";
	}
};

/**
 * @description 设置值时要检测类
 * @param {String} text
 * @private
 */
TextComp.prototype.setValue = function(text) {
	text = getString(text, "");
	// 只有字符串和密码类型才需要此处理
	if (this.dataType == 'A' || this.dataType == 'P') {
		if (this.maxSize != -1) {
			// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
			if (text.lengthb() > this.maxSize)
				text = text.substrCH(this.maxSize);
		}
	}
	// 记录旧
	this.oldValue = this.newValue;
//	this.oldValue = this.input.value;
	this.newValue = text;
	this.maskValue();
	//解决ie8下有时不显示valueu问题
	if (IS_IE8)
		this.input.value = "0";
	if(this.focused){
		this.input.value = this.newValue;
		if(this.dataType == 'D'){
			this.input.value = this.postProcessNewValue(this.newValue);
		}
	}
	else
		this.input.value = this.showValue;
	
	//显示格式化后Tip
	if (this.dataType != 'P') {
		if(this.showTipMessage && this.showTipMessage != null)
				this.setMessage(this.showTipMessage);
			else
				this.setMessage(this.showValue);
	}
	
	if (this.checkTip()) {
		if (this.input.value == "")
			this.showTip();
		else
			this.input.style.color = "black";
	}
	
	if(this.textColor != null)
		this.input.style.color = "red";
		
	if(this.componentType == "FLOATTEXT"){
		if(this.newValue.indexOf(',') != -1)
			return;
	}
	if (this.newValue != this.oldValue)
		if (!this.disabled)   //TODO
			this.valueChanged(this.oldValue, this.newValue);
	this.ctxChanged = true;
	this.notifyChange(NotifyType.VALUE, this.newValue);
};

/**
 * @description 在末尾插入字符（包括退格键，用于直接通过虚拟键盘进行设值操作）
 * @param charCode 字符Unicode编码
 * @private
 */
TextComp.prototype.addCharCode = function(charCode) {
	if (charCode == null)
		return;
	// 原始�?
	var oldValue = this.getValue();
	if (oldValue != null)
		oldValue = oldValue.toString();
	else
		oldValue = "";
	// 新�?
	var newValue = "";
	// 得到格式化器
	var formater = this.getFormater();
	if (charCode == 8) {  // 退格键
		if (oldValue == "") {
			return;
		} else {
			newValue = oldValue.substring(0, oldValue.length - 1);
			this.setValue(newValue);
		}
	} else {  // 其它�?
		// 字符真实�?
		var charValue = String.fromCharCode(charCode);
		newValue = oldValue + charValue;
		
		// 获取keyValue,currValue,aggValue,
		if (formater.valid(charValue, newValue, oldValue) == false)
			return;
	   	// 验证完成输入合法性后调用程序的内部处�?
	   	if (this.afterValid(charCode, charValue) == false)
	   		return;
		
		this.setValue(newValue);
	}
};

/**
 * @description 获取显示值
 * @return {String} showValue
 */
TextComp.prototype.getShowValue = function() {
	return this.showValue;
};

/**
 * @descripiton 设置显示值
 * @param {String} text
 */
TextComp.prototype.setShowValue = function(text) {
	this.showValue = text;
	this.input.value = text;
};

/**
 * @description 获取显示值
 * @private
 */
TextComp.prototype.maskValue = function(){
	var masker = getMasker(this.componentType);
	if(masker != null){
//		this.showValue = toColorfulString(masker.format(this.newValue));
		this.showValue = masker.format(this.newValue).value;
		this.textColor = masker.format(this.newValue).color;
	}
	else
		this.showValue = this.newValue;
};

/**
 * @description 设置此输入框控件的激活状态
 * @param {Boolean} isActive true表示处于激活状态，否则表示禁用状态
 * @public
 */
TextComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.disabled = true;
		// 清除只读状态
//		if (this.readOnly) 
//			this.setReadOnly(false); 
		// 如果是radio使用disabled后续修改为图片
//		if(this.input.type == "radio"){
//			this.input.disabled = true;
//		}
		this.input.disabled = true;
		if(IS_IPAD){
			this.input.style.color= "#000000";
		}
//		this.input.readOnly = true;
		this.input.className = this.inputClassName_inactive;//"input_normal_center_bg text_input_inactive";
		if (this.Div_text != null){
			this.Div_text.className = this.className + " " + this.className + "_inactive_bgcolor";
			if(this.Div_text.children.length == 3){
				var children = this.Div_text.children;
				for(var i=0;i<children.length;i++){
					if(typeof(children[i]) != "undefined"){
						children[i].className = children[i].className.replaceStr('input_normal','input_disable');
						//为了避免先获得焦点则替换不成功
						if(this.focused = true)
							children[i].className = children[i].className.replaceStr('input_highlight','input_disable');
						//为了避免先设置了readonly则替换不成功
						if(this.readOnly)
							children[i].className = children[i].className.replaceStr('input_readonly','input_disable');
					}
				}
			}
		}
	}
	// 控件处于禁用状态变为激活状状
	else if (this.disabled == true && isActive == true) {
		this.disabled = false;
		// 清除只读状态
//		if (this.readOnly) 
//			this.setReadOnly(false);
		// 如果是radio使用disabled后续修改为图片
//		if(this.input.type == "radio"){
//			this.input.disabled = false;
//		}
		this.input.disabled = false;
//		this.input.readOnly = false;
		this.input.className = this.inputClassName_init;//"input_normal_center_bg text_input";
		if (this.Div_text != null){
			this.Div_text.className = this.className;
			if(this.Div_text.children.length == 3){
				var children = this.Div_text.children;
				for(var i=0;i<children.length;i++){
					if(typeof(children[i]) != "undefined"){
						children[i].className = children[i].className.replaceStr('input_disable','input_normal');
					}
				}
			}
		}
		if(this.mayFocus){
			this.input.focus();
			this.mayFocus=false;
		}
	}
	this.ctxChanged = true;
	this.notifyChange(NotifyType.ENABLE, !this.disabled);
};

/**
 * @description 得到输入框的激活状态
 * @return {Boolean} isActive
 * @private
 */
TextComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * @description 检测是否是整数
 * @param date
 * @private
 */
TextComp.prototype.checkInteger = function(data) {
	//确定传入参数的合法�?
	 if (data == null || data == "")
		return false;
	 else if (!isNumber(data))
		return false;
	 
	 return true;		
};

/**
 * @description 设置最大值
 * @param size
 * @pirvate
 */
TextComp.prototype.setMaxSize = function(size) {
	this.maxSize = parseInt(size,10);
};

/**
 * @description 设置只读状态
 * @param {Boolean} readOnly
 * @public
 */
TextComp.prototype.setReadOnly = function(readOnly) {
	if(IS_IE8){
		this.setActive(!readOnly);
		return;
	}
	this.input.readOnly = readOnly;
	this.readOnly=readOnly;
	if (readOnly) {
		this.input.className = this.inputClassName_readonly;
		//  text_div_readonly与 text_div 属性一致，并且这样设置导致隐藏因此不设置text_div_readonly
		if(this.Div_text != null){
			this.Div_text.className = this.className + " " + "text_div_readonly";
			if(this.Div_text.children.length == 3){
				var children = this.Div_text.children;
				for(var i=0;i<children.length;i++){
					if(typeof(children[i]) != "undefined"){
						children[i].className = children[i].className.replaceStr('input_normal','input_readonly');
					}
				}
			}
		}
	} else {
		this.input.className = this.inputClassName_init;
		if(this.Div_text != null){
			this.Div_text.className = this.className;
			if(this.Div_text.children.length == 3){
				var children = this.Div_text.children;
				for(var i=0;i<children.length;i++){
					if(typeof(children[i]) != "undefined"){
						children[i].className = children[i].className.replaceStr('input_readonly','input_normal');
					}
				}
			}
		}
	}
	this.ctxChanged = true;
	this.notifyChange(NotifyType.READONLY, this.readOnly);
};

/**
 * @description 设置出错时样式
 * @private
 */
TextComp.prototype.setErrorStyle = function() {
	if (!this.readOnly && this.isError) {
		if (this.Div_text != null){
			this.Div_text.className = this.className + " " + "text_div_error";
		}
		if(this.componentType == "REFERENCETEXT"){
			this.warnIcon.style.right = "25px";
		}else if(this.componentType == "LANGUAGECOMBOBOX"){
			this.warnIcon.style.right = "25px";
		}else if(this.componentType == "COMBOBOX"){
			this.warnIcon.style.right = "25px";
		}else if(this.componentType == "DATETEXT"  || this.componentType == "YEARMONTHTEXT" || this.componentType == "YEARTEXT" || this.componentType == "MONTHTEXT"){
			this.warnIcon.style.right = "25px";
		}else{
			this.warnIcon.style.right = "10px";
		}
		if(IS_IPAD){
			this.warnIcon.style.right = (parseInt(this.warnIcon.style.right, 10) + 10) + "px";
		}
//		this.errorMsgDiv.style.display = "none";
		this.warnIcon.style.display = "block";
	}
};

/**
 * @description 设置警告时样式
 * @private
 */
TextComp.prototype.setWarningStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null)
			this.Div_text.className = this.className + " " + this.className + "_warning_bgcolor";
	}
};

/**
 * @description 设置聚焦时样式
 * @private
 */
TextComp.prototype.setFocusStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null && -1 == this.Div_text.className.indexOf("_error_bgcolor") && -1 == this.Div_text.className.indexOf("_warning_bgcolor"))
			this.Div_text.className = this.className + " " + this.className + "_focus_bgcolor";
	}
};

/**
 * @description 设置焦点移出时样式
 * @private
 */
TextComp.prototype.setBlurStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null && -1 == this.Div_text.className.indexOf("_error_bgcolor") && -1 == this.Div_text.className.indexOf("_warning_bgcolor"))
			this.Div_text.className = this.className;
	}
};

/**
 * @description 设置普通样式（校验通过后样式）
 * @private
 */
TextComp.prototype.setNormalStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null){
			this.Div_text.className = this.className;
		}
//		this.errorMsgDiv.style.display = "none";
		this.warnIcon.style.display = "none";
	}
};

/**
 * @description 设置错误提示框位置
 * @private 
 */
TextComp.prototype.setErrorPosition = function(parentElement, left, top) {
	if(typeof(parentElement) != 'undefined' && typeof(left) == 'number' && typeof(top) == 'number'){
		parentElement.appendChild(this.errorMsgDiv);
		this.errorMsgDiv.style.top = top + "px";
		this.errorMsgDiv.style.left = left + "px";
	}else if(typeof(parentElement) != 'undefined'){//inputDialogComp使用
		var currParentElement = this.parentOwner;
		var tempTop = 0;
		var tempLeft = 0;
		while(typeof(currParentElement) == "object"){
			if(currParentElement.className && currParentElement.className == parentElement.className){
				parentElement.appendChild(this.errorMsgDiv);
				
				this.errorMsgDiv.style.visibility = "hidden";
				this.errorMsgDiv.style.display = "block";
				this.errorMsgDiv.style.top = tempTop + 5 - this.errorMsgDiv.offsetHeight + "px";
				this.errorMsgDiv.style.left = tempLeft + 10 +"px";
				this.errorMsgDiv.style.display = "none";
				this.errorMsgDiv.style.visibility = "visible";
				break;
			}
			if(typeof(currParentElement.offsetLeft) == "number"){
				tempLeft += currParentElement.offsetLeft;
			}
			if(typeof(currParentElement.offsetTop) == "number"){
				tempTop += currParentElement.offsetTop;
			}
			if(typeof(currParentElement.offsetParent) == "object"){
				currParentElement = currParentElement.offsetParent;
			}else if(typeof(currParentElement.parentOwner) == "object"){
				currParentElement = currParentElement.parentOwner;
			}else{
				currParentElement = currParentElement.parentNode;
			}
		}
	}else{
		parentElement = this.parentOwner;
		var tempTop = 0;
		var tempLeft = 0;
		while(typeof(parentElement) == "object"){
			if(parentElement.id && parentElement.id.indexOf('_um') != -1){
				parentElement.appendChild(this.errorMsgDiv);
				
				this.errorMsgDiv.style.visibility = "hidden";
				this.errorMsgDiv.style.display = "block";
				this.errorMsgDiv.style.top = tempTop + this.Div_gen.offsetTop - this.errorMsgDiv.offsetHeight + "px";
				this.errorMsgDiv.style.left = tempLeft + this.Div_gen.offsetLeft + 10 +"px";
				if(this.componentType != "INTEGERTEXT")
					this.errorMsgDiv.style.display = "none";
				this.errorMsgDiv.style.visibility = "visible";
				break;
			}
			if(typeof(parentElement.offsetLeft) == 'number'){
				tempLeft += parentElement.offsetLeft;
			}
			if(typeof(parentElement.offsetTop) == 'number'){
				tempTop += parentElement.offsetTop;
				parentElement = parentElement.offsetParent;
				continue;
			}
			if(typeof(parentElement.parentOwner) == "object"){
				parentElement = parentElement.parentOwner;
			}else{
				parentElement = parentElement.parentNode;
			}
		}	
	}
};

/**
 * @description 获取Label对象
 */
TextComp.prototype.getLabel = function() {
	return this.label;
};

/**
 * 聚焦事件
 * @private
 */
TextComp.prototype.onblur = function (e) {
	this.setBlurStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
	this.doEventFunc("onBlur", focusEvent);
};

/**
 * 单击事件
 * @private
 */
TextComp.prototype.onclick = function (e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
	this.doEventFunc("onclick", mouseEvent);
};

/**
 * 聚焦事件
 * @private
 */
TextComp.prototype.onfocus = function (e) {
	this.setFocusStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
	this.doEventFunc("onFocus", focusEvent);
};

/**
 * 改变事件
 * @private
 */
TextComp.prototype.haschanged = function (e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onTextChanged", KeyListener.listenerType, keyEvent);
	this.doEventFunc("onTextChanged", keyEvent);
};

/**
 * 回车事件
 * @private
 */
TextComp.prototype.onenter = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onEnter", KeyListener.listenerType, keyEvent);
	this.doEventFunc("onEnter", keyEvent);
};

/**
 * 按键事件
 * @private
 */
TextComp.prototype.onkeydown = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onKeyDown", KeyListener.listenerType, keyEvent);
	this.doEventFunc("onKeyDown", keyEvent);
};

/**
 * 按键抬起事件
 * @private
 */
TextComp.prototype.onkeyup = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onKeyUp", KeyListener.listenerType, keyEvent);
	this.doEventFunc("onKeyUp", keyEvent);
};

/**
 * 粘贴之前的事件
 * @param {} pasteEvent
 */
//TextComp.prototype.onBeforePaste = function(e) {
//	var gridEvent = {
//		"obj" : this
////		"data" : clipboardData.getData('Text')
//	};
//	this.doEventFunc("onBeforePaste", GridListener.listenerType, gridEvent);
//};

/**
 * 鼠标移入事件
 * @private
 */
TextComp.prototype.onmouseover = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onMouseOver", MouseListener.listenerType, mouseEvent);
	this.doEventFunc("onMouseOver", mouseEvent);
};

/**
 * 值改变事件
 * @private
 */
TextComp.prototype.valueChanged = function(oldValue, newValue) {
	var valueChangeEvent = {
			"obj" : this,
			"oldValue" : oldValue,
			"newValue" : newValue
		};
//		pageUI.setChanged(true);
//	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
	this.notifyChange(NotifyType.VALUE, this.newValue);
	this.doEventFunc("valueChanged", valueChangeEvent);
	if(this.editFormular || this.validateFormular)
			execFormula(this.widget.id, null, this.id);
};


/**
 * @description 设置被修改的控件状态
 * @since V6.3
 * @param context 后台发生变化的值，json对象
 * @private
 */
TextComp.prototype.setChangedContext = function(context) {
	if(context.enable != null)
		this.setActive(context.enable);		
	
	if(context.focus != null)
		this.setFocus();
		
	if(context.readOnly != null && this.readOnly != context.readOnly)
		this.setReadOnly(context.readOnly);
	
	if (context.value != null && context.value != this.input.value){
		this.setValue(context.value);
	}
	
	if(context.visible != null && this.visible != context.visible)
		this.setVisible(context.visible);	
		
	if(context.text != null && this.labelText != context.text)
		this.setLabelText(context.text);
		
	if(context.isShowLabel != null)
		this.isShowLabel(context.isShowLabel);
	if(context.designModel != null)
		this.setDesignModel(context.designModel);
	
	if(context.textAlign != null && this.labelAlign != context.textAlign)
		this.setLabelTextAligh(context.textAlign);
		
	if(context.width != null && this.labelWidth != context.width)
		this.setLabelTextWidth(context.width);
};

/**
 * @description 设置可见属性方法
 * @param {Booelan} visible
 * @public
 */
TextComp.prototype.setVisible = function(visible) {
	if(visible)
		this.showV();
	else
		this.hideV();	
	this.notifyChange(NotifyType.VISIBLE, visible);
};

/**
 * @description 设置是否为设计态
 * @param {Booelan} visible
 * @public
 */
TextComp.prototype.setDesignModel = function(designModel) {
	this.designModel = designModel;
	this.notifyChange("designModel", designModel);
};

/**
 * @descripiton 初始化错误提示信息
 * @private
 */
TextComp.prototype.initErrorMsg = function(){
	var oThis = this;
	this.errorMsgDiv = $ce("DIV");
	this.errorMsgDiv.style.display = "none";
	this.errorMsgDiv.className = "error_msg_div";
	
	var errorLeftDiv = $ce("DIV");
	errorLeftDiv.className = "error_left_div";
	this.errorMsgDiv.appendChild(errorLeftDiv);
	
	var errorCenterDiv = $ce("DIV");
	errorCenterDiv.className = "error_center_div";
	this.errorMsgDiv.appendChild(errorCenterDiv);
	
	var errorRightDiv = $ce("DIV");
	errorRightDiv.className = "error_right_div";
	this.errorMsgDiv.appendChild(errorRightDiv);
	
	this.errorCenterDiv = $ce("DIV");
	this.errorCenterDiv.className = "error_content_div";
	this.errorMsgDiv.appendChild(this.errorCenterDiv);
	
	this.errorMsgDiv.onclick = function(e){
		//----解决叉掉提示信息后页面混乱问题,没有解决切换页签提示信息自动消失by yhl ----start------
		//oThis.input.onclick(e);
		//if(oThis.visible != false)
	//		oThis.input.focus();
              //------------end------------------------
		this.style.display = "none";
	};
	
	this.Div_gen.appendChild(this.errorMsgDiv);
	
	this.warnIcon = $ce("DIV");
	this.warnIcon.style.display = "none";
	this.warnIcon.className = "warn_icon";
	this.warnIcon.onmouseover = function(e){
		//oThis.errorMsgDiv.style.display = "block";
	};
	this.warnIcon.onmouseout = function(e){
		//oThis.errorMsgDiv.style.display = "none";
	};
	
	this.Div_gen.appendChild(this.warnIcon);
	
	this.closeIcon = $ce("DIV");
	this.closeIcon.className = "up_close_normal";
	this.closeIcon.onmouseover = function(e){
		this.className = "down_close_press";
	};
	this.closeIcon.onmouseout = function(e){
		this.className = "up_close_normal";
	};
	this.closeIcon.onmouseup = function(e){
		this.className = "up_close_normal";
		oThis.errorMsgDiv.style.display = "none";
	};
	this.errorMsgDiv.appendChild(this.closeIcon);

};

