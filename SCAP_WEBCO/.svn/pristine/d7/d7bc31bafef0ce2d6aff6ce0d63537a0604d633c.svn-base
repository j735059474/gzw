/**
 * @fileoverview 此控件是对html textarea的封装,可以设置rows和cols
 * 
 * @author gd, guoweic
 * @version 6.0
 * @since 5.5 
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

TextAreaComp.prototype = new BaseComponent;
TextAreaComp.prototype.componentType = "TEXTAREA";
TextAreaComp.prototype.inactivate = "inactivate";
TextAreaComp.prototype.highlight = "highlight";
TextAreaComp.prototype.readonly = "readonly";

/**
 * TextArea控件的构造函数
 * @class TextArea控件
 * @param rows 该元素有多少行高
 * @param cols 列宽(以字符记)
 * @param readOnly 若为true,则用户不能编辑任何显示的文本
 */
function TextAreaComp(parent, name, left, top, rows, cols, position, readOnly, value, width, height, tip, className, attrArr) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.value = getString(value, "");
	this.rows = rows;
	this.cols = cols;
	this.position = getString(position, "absolute");
	this.readOnly = getBoolean(readOnly, false);
	this.disabled = false;
	this.tip = getString(tip, "");
	this.className = getString(className, "text_div");
	this.maxSize = -1;
	if(attrArr){
		this.maxSize = getInteger(attrArr.maxSize, this.maxSize);
	}
	if( this.maxSize != -1) {
		// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
		if (this.value.lengthb() > this.maxSize)
			this.value = this.value.substrCH(this.maxSize);
	}	
	this.parentOwner = parent;
	//将TextAreaComp控件放入clickHolder,注册相应的动作事件
	window.clickHolders.push(this);	
	
	this.create();
};

/**
 * 创建textarea
 * @private
 */
TextAreaComp.prototype.create = function() {	
	var oThis = this;
	//创建显示div对象
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id;
	this.Div_gen.className = this.className;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	if (this.position != "relative"){
		this.Div_gen.style.position = this.position;
	}
	this.Div_gen.style.height = this.height;
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.borderWidth = "0px";
	this.Div_gen.style.overflow = "hidden";

	if (this.parentOwner) 
		this.placeIn(this.parentOwner);	
};

TextAreaComp.prototype.manageSelf = function() {
	var oThis = this;
	//创建textarea对象
	this.textArea = $ce("textarea");
	this.textArea.className = "text_area";
	this.textArea.style.resize = "none"; 
	this.Div_gen.appendChild(this.textArea);
	if (this.cols){
		this.textArea.cols = this.cols;
		if (typeof (this.width) == "undefined") {
			this.Div_gen.style.width = this.textArea.offsetWidth + 6+ "px";
			if(this.parentOwner)
				this.parentOwner.style.width = this.textArea.offsetWidth + 6 + "px"; 
		}
	}
	else{
//		var width = 0;
//		if (isPercent(this.width)){
//			width = this.Div_gen.offsetWidth;
//		}
//		else{
//			width = getInteger(parseInt(this.width), 120);
//		}
//		this.textArea.style.width = (width - 6) < 0 ? 0 : (width - 6) + "px";
		
		if (isPercent(this.width)){
			this.textArea.style.width = "98%";
		}else{
			this.textArea.style.width = (getInteger(parseInt(this.width), 120) - 6) < 0 ? 0 : (getInteger(parseInt(this.width), 120) - 6) + "px";
		}
		
	}
	if (this.rows){
		this.textArea.rows = this.rows;
		if (typeof (this.height) == "undefined") {
			this.Div_gen.style.height = this.textArea.offsetHeight + 6 + "px";
			if(this.parentOwner)
				this.parentOwner.style.height = this.textArea.offsetHeight +6+ "px"; 
		}
	}
	else{
		if (!isPercent(this.height)){
			this.textArea.style.height = (parseInt(this.height) - 6) + "px";
		}
		else{
			if (IS_IE){
				this.textArea.style.height = "90%";
			}
			else{
				this.textArea.style.height = "99%";
			}
		}
	}
	
	if (this.readOnly == true){
		this.setReadOnly(this.readOnly);
	}

	//设置初始值
	if (this.value != null){
		this.setValue(this.value);
	}
	else{
		this.showTip();
	}
	
	
	
	//获得键盘焦点时调用
	this.textArea.onfocus = function (e) {	 
		e = EventUtil.getEvent();
		oThis.oldValue = oThis.getValue();
		if (this.readOnly == false){
			oThis.Div_gen.className = oThis.className + " " + oThis.highlight;
		}
		oThis.onfocus(e);
		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//失去键盘焦点时调用
	this.textArea.onblur = function (e) {
		e = EventUtil.getEvent();
		oThis.newValue = oThis.getValue();
		//失去焦点时控制maxLength
//		if(oThis.newValue != ""){
//			 var beforeFormatL = oThis.newValue.length;
//			 oThis.newValue = oThis.getFormater().format(oThis.newValue);
//			 var afterFormatL = oThis.newValue.length;
//			 if (beforeFormatL > afterFormatL){
//					showVerifyMessage(this, trans("ml_thevaluebeyondthemaxlength")); 
//			 }
//		}
		this.value = oThis.newValue;
		oThis.setTitle(this.value);
		oThis.setValue(this.value);
		if (oThis.newValue != oThis.oldValue)
			oThis.valueChanged(oThis.oldValue, oThis.newValue);
		if (this.readOnly == false){
			oThis.Div_gen.className = oThis.className;
		}
		oThis.onblur(e);
		oThis.showTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onclick = function(e) {
		window.clickHolders.trigger = oThis;
		document.onclick(e);
		e = EventUtil.getEvent();	
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onkeydown = function(e) {
		// readOnly时不允许输入
		//if(this.readOnly == true)
			//return false;
		e = EventUtil.getEvent();
		
		//处理输入中文的情况，中文输入过程中不会触发onkeyPress
		//只校验长度 如果超长之后对长度进行处理
		if (oThis.maxSize != -1) {
			var text = oThis.textArea.value;
			// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
			if (text.lengthb() > oThis.maxSize){
				text = text.substrCH(oThis.maxSize);
				oThis.textArea.value = text;
			}
		}	
		
		// 调用用户的方法
		oThis.onkeydown(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onkeyup = function(e) {
		e = EventUtil.getEvent();
		
		//处理输入中文的情况，中文输入过程中不会触发onkeyPress
		//只校验长度 如果超长之后对长度进行处理
		if (oThis.maxSize != -1) {
			var text = oThis.textArea.value;
			// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
			if (text.lengthb() > oThis.maxSize){
				text = text.substrCH(oThis.maxSize);
				oThis.textArea.value = text;
			}
		}
//		oThis.onkeyup(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	   	if (window.pageUI)
			window.pageUI.setChanged(true);
		
	};
	
	this.textArea.onkeypress = function(e) {
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
			oThis.newValue = oThis.getFormater().format(oThis.textArea.value);
			if (oThis.processEnter)
				oThis.processEnter();
			//form里的控件不blur(因为下个element获得焦点，而自身blur)
			if (typeof(oThis.fieldId) == 'undefined')
				oThis.textArea.onblur();
//			oThis.onenter(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			//回车后失去焦点
			return true;
		}
		//for firefox
		else if (keyCode == 8) {
			//oThis.haschanged(e);
			// 删除事件对象（用于清除依赖关系）
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
		var currValue = oThis.textArea.value;
//		if (oThis.dataType != "F") {
//			var aggValue = insertAtCursor(this, key);
//		}
//		//guoweic: firefox中insertAtCursor方法只能得到输入前的�?
//		if (!IS_IE && aggValue == "") {
//			aggValue = key;
//		}
//		//this.focus(e);
//		if (IS_IE) {
//			// guoweic: 该方法只有IE支持
//			document.execCommand("undo");
//		}
		//输入时不控制maxLength，改为回车和失去焦点后控制
		// 获取keyValue,currValue,aggValue,
		if (formater.valid(key, currValue, currValue) == false) {
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
	
	this.textArea.onselect = function(e) {	
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onselectstart = function(e) {
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
};

/**
 * @private
 */
TextAreaComp.prototype.setError = function() {
	
};

/**
 * 校验是否有默认提示信息
 * @private
 */
TextAreaComp.prototype.checkTip = function() {
	if (this.tip != null && this.tip != "")
		return true;
	else
		return false;
};

/**
 * 显示提示信息
 * @private
 */
TextAreaComp.prototype.showTip = function() {
	if (this.checkTip()) {
		if (this.textArea.value == "") {
			this.textArea.value = this.tip;
			this.textArea.style.color = "gray";
		}
	}
};

/**
 * 隐藏提示信息
 * @private
 */
TextAreaComp.prototype.hideTip = function() {
	if (this.checkTip()) {
		if (this.textArea.value == this.tip) {
			this.textArea.value = "";
			this.textArea.style.color = "black";
		}
	}
};

/**
 * 设置大小及位置
 * @param width 像素大小
 * @param height 像素大小
 * @public 
 */
TextAreaComp.prototype.setBounds = function(left, top, width, height) {
	// 改变数据对象的值
	this.left = left;
	this.top = top;
	this.width = getString(convertWidth(width), this.Div_gen.offsetWidth + "px");
	this.height = getString(convertHeight(height), this.Div_gen.offsetHeight + "px");
	
	// 改变显示对象的值
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	//在设计态form中存在textarea的时候会导致form展示出现问题
	/*if (window.editMode != null && window.editMode == true){
		this.Div_gen.parentNode.style.left = this.left + "px";; 	
		this.Div_gen.parentNode.style.top = this.top + "px";
		
		this.Div_gen.parentNode.style.width = this.width;
		this.Div_gen.parentNode.style.height = this.height;
		if (isPercent(this.width))
			this.Div_gen.style.width = "100%";
		else	
			this.Div_gen.style.width = this.width;
		if (isPercent(this.height))	
			this.Div_gen.style.height = "100%";
		else	
			this.Div_gen.style.height = this.height;
	}
	else{*/
		this.Div_gen.style.width = this.width;
		this.Div_gen.style.height = this.height;
	/*}*/
	
	var tempWidth = 0;
	if (isPercent(this.width))
		tempWidth = this.Div_gen.offsetWidth;
	else
		tempWidth = getInteger(parseInt(this.width), 120);
	if (IS_IE8)
		this.textArea.style.width = (tempWidth - 8) < 0 ? 0 : (tempWidth - 8) + "px";
	else	
		this.textArea.style.width = (tempWidth - 6) < 0 ? 0 : (tempWidth - 6) + "px";

	var tempHeight = 0;
	if (isPercent(this.height)){
		tempHeight = this.Div_gen.offsetHeight;
		this.textArea.style.height = (tempHeight - 6) + "px";	
	}
	else{
		tempHeight = getInteger(parseInt(this.height), 120);
		this.textArea.style.height = (tempHeight - 6) < 0 ? 0 : (tempHeight - 6) + "px";	
	}
};

/**
 * 得到textarea中的文本内容
 * @public 
 */
TextAreaComp.prototype.getValue = function() {
	if (this.checkTip()) {
		if (this.textArea.value == this.tip && this.textArea.style.color != "black")
			return "";
	}
	return this.textArea.value;
};

/**
 * 设置textarea中的文本内容
 * @param {String} value
 * @public
 */
TextAreaComp.prototype.setValue = function(value) {
//	this.oldValue = this.getValue();
	value = getString(value, "");
	if (this.maxSize != -1) {
		// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
		if (value.lengthb() > this.maxSize)
			value = value.substrCH(this.maxSize);
	}
	this.textArea.value = value;
	if (this.checkTip()) {
		if (this.textArea.value == "")
			this.showTip();
		else
			this.textArea.style.color = "black";
	}
	this.newValue = value;
	if (this.newValue != this.oldValue)
		this.valueChanged(this.oldValue, this.newValue);
	this.setTitle(this.newValue);
	this.notifyChange(NotifyType.VALUE, this.newValue);
};

TextAreaComp.prototype.setTitle = function(title) {
	this.textArea.title = title;
};

/**
 * 设置此TextArea控件的激活状态
 * @param {Boolean} isActive true表示处于激活状态,否则表示禁用状态
 * @pulbic
 */
TextAreaComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		//TODO 临时办法，解决ie下设置禁用后，textarea滚动条不能用问题
		if (IS_IE)
			this.textArea.readOnly = true;
		else	
			this.textArea.disabled = true;
		this.disabled = true;
		this.Div_gen.className = this.className + " " + this.inactivate;
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {	
		if (IS_IE)
			this.textArea.readOnly = false;
		else	
			this.textArea.disabled = false;
		this.disabled = false;
		this.Div_gen.className = this.className;
	}
	this.notifyChange(NotifyType.ENABLE, !this.disabled);
};

/**
 * 设置只读状态
 * @pram {Boolean} readOnly
 * @public
 */
TextAreaComp.prototype.setReadOnly = function(readOnly) {
	this.textArea.readOnly = readOnly;
	this.readOnly = readOnly;
	if (readOnly) {
		this.Div_gen.className = this.className + " " + this.readOnly;
	} else {
		this.Div_gen.className = this.className;
	}
	this.notifyChange(NotifyType.READONLY, this.readOnly);
};

/**
 * 设置聚焦
 * @pulbic
 */
TextAreaComp.prototype.setFocus = function() {
	if (IS_IE) {
		this.textArea.focus();
		this.textArea.select();
	} else {  // firefox等浏览器不能及时执行focus方法
		var oThis = this;
		window.setTimeout(function(){oThis.textArea.focus();oThis.textArea.select();}, 50); 
	}
};

/**
 * 设置rows
 * @pulbic
 */
TextAreaComp.prototype.setRows = function(rows) {
	this.rows = rows;
	this.textArea.rows = this.rows;
	if (typeof(this.height) == "undefined"){
		this.Div_gen.style.height = this.textArea.offsetHeight + 6 + "px";
		if(this.parentOwner)
			this.parentOwner.style.height = this.textArea.offsetHeight +6+ "px";
	}
	this.notifyChange(NotifyType.ROWS, this.rows);
};


/**
 * 设置cols
 * @pulbic
 */
TextAreaComp.prototype.setCols = function(cols) {
	this.cols = cols;
	this.textArea.cols = this.cols;
	if (typeof(this.widht) == "undefined"){
		this.Div_gen.style.width = this.textArea.offsetWidth + 6+ "px";
		if(this.parentOwner)
			this.parentOwner.style.width = this.textArea.offsetWidth + 6 + "px";
	}
	this.notifyChange(NotifyType.COLS, this.cols);
};


/**
 * 得到输入框的激活状态
 * 
 */
TextAreaComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * private
 * @param {} e
 */
TextAreaComp.prototype.outsideClick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("outsideClick", MouseListener.listenerType, mouseEvent);
	this.doEventFunc("outsideClick", mouseEvent);
};

/**
 * 聚焦事件
 * @private
 */
TextAreaComp.prototype.onfocus = function (e) {
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
	this.doEventFunc("onFocus", focusEvent);
};

/**
 * 焦点移出事件
 * @private
 */
TextAreaComp.prototype.onblur = function (e) {
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
	this.doEventFunc("onBlur", focusEvent);
};

/**
 * 按键事件
 * @private
 */
TextAreaComp.prototype.onkeydown = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
//	this.doEventFunc("onkeydown", KeyListener.listenerType, keyEvent);
	this.doEventFunc("onkeydown", keyEvent);
};

/**
 * 值改变事件
 * @private
 */
TextAreaComp.prototype.valueChanged = function(oldValue, newValue) {
	var valueChangeEvent = {
			"obj" : this,
			"oldValue" : oldValue,
			"newValue" : newValue
		};
//	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
	this.doEventFunc("valueChanged", valueChangeEvent);
};

/**
 * 设置被修改的控件状态
 * @since V6.3
 * @param context 后台发生变化的值，json对象
 */
TextAreaComp.prototype.setChangedContext = function(context) {
	if (context.enable != null)
		this.setActive(context.enable);
	if (context.readOnly != null && this.readOnly != context.readOnly)
		this.setReadOnly(context.readOnly);
	if (context.value != null && context.value != this.textArea.value)
		this.setValue(context.value);
	if (context.visible != null){
		if(context.visible)
			this.showV();
		else
			this.hideV();	
	}
	
	if (context.maxLength){
		this.maxSize = getInteger(parseInt(context.maxLength), -1);
	}
	if (context.focus != null)
		this.setFocus();
	if(context.rows != null && context.rows != this.rows)
		this.setRows(context.rows);
	if(context.cols != null && context.cols != this.cols)
		this.setCols(context.cols);
};


/**
 * 得到格式化器
 * @private
 */
TextAreaComp.prototype.getFormater = function() {
	if(this.formater == null)
		return	this.createDefaultFormater();
};


/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
TextAreaComp.prototype.createDefaultFormater = function() {
	return new StringFormater(this.maxSize);
};


TextAreaComp.prototype.setMaxSize = function(size) {
	this.maxSize = parseInt(size,10);
};
///**
// * 处理回车事件
// * @private
// */
//TextAreaComp.prototype.processEnter = function() {
//	 var inputValue = this.getValue();
//	 if (inputValue == null)
//	 	inputValue = "";
//	 else	
//	 	inputValue = inputValue.trim();
//	 var beforeFormatL = inputValue.length;
//	 value = this.getFormater().format(inputValue);
//	 var afterFormatL = value.length;
//	 if (beforeFormatL > afterFormatL)
//		showVerifyMessage(this, trans("ml_thevaluebeyondthemaxlength"));
//	 this.setMessage(value);
//	 this.textArea.value = value;
//};

/**
 * @description 设置提示信息
 * @param {String} message
 * @private
 */
//TextAreaComp.prototype.setMessage = function(message) {
//	if (!this.isError) {
//		this.message = message;
//		this.errorMessage = "";
//		this.setTitle(message);
//	}
//};