/**
 * @fileoverview WebFrame Editor控件定义文件
 * @author gd, dengjt, guoweic
 * @version NC6.0
 * 
 * 注：一个页面只能有一个EditorComp控件！（因为设置了window.currentEditor）
 */
EditorComp.prototype = new BaseComponent;
EditorComp.prototype.componentType = "EDITOR";

/**
 * 高级文本编辑器构造函数
 * 
 * @class 高级文本编辑器
 * @constructor
 * @param{Array} hideBarIndices 一维数组,指定要隐藏的工具条
 * @param{Array} hideImageIndices 二维数组,指定要隐藏的每行的按钮,形式如[[0,1],[],[2]]
 * @param hideBarIndices 需要隐藏的行
 */
function EditorComp(parent, name, left, top, width, height, position, className, toolbarType, attrObj) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "cms_editor");
	//Full或者Custom
	this.toolbarType = getString(toolbarType, "Custom"); 
	//是否是只读模式
	this.readOnly = false;
	if (attrObj != null) {
		this.sysid = attrObj.sysid;
		this.baModule = attrObj.baModule; 
	}
	window.clickHolders.push(this);	
	/*
	this.imageNode = null;
	this.liNode = null;
	this.hiddenContent = null;
	this.frame = null;
	this.hideBarIndices = hideBarIndices;
	this.hideImageIndices = hideImageIndices;
	this.charset = "UTF-8";
	// 当前模式 0 代码,1 design
	this.mode = 1;
	this.toolbars = new Array;
	this.btDesign = null;
	this.btView = null;
	this.btHtml = null;
	this.filterScript = false;
	// 用来记录当前是否初始化完成。防止iframe多线程操作出问题
	this.initialized = false;
	// 当前编辑器是否可以编辑
	this.disabled = false;
	this.value = "";
	// 在页面上设置当前编辑器对象
	window.currentEditor = this;
	// redo栈，当执行undo时，将undo前的信息保存在栈中
	this.redoStack = new Array();
	*/
	this.create();
};

/**
 * 扩展CKEDITOR,为CKEDITOR的原型增加设置是否只读的方法
 */
EditorComp.prototype.extendCKEditor = function(){
	var editor = this.editor;
	
    var cancelEvent = function(evt) {
        evt.cancel();
    };	
    
	editor.setReadOnly = function(isReadOnly){
        this[isReadOnly ? 'on': 'removeListener']('key', cancelEvent, null, null, 0);
        this[isReadOnly ? 'on': 'removeListener']('selectionChange', cancelEvent, null, null, 0);

        // 置为失效所有wysiwyg模式下的命令
        var command, commands = this._.commands,
        mode = this.mode;
        for (var name in commands) {
            command = commands[name];
            if (isReadOnly) {
                command.disable();
            } else {
                command[command.modes[mode] ? 'enable': 'disable']();
            }
            this[isReadOnly ? 'on': 'removeListener']('state', cancelEvent, null, null, 0);
        }
        var i, j, k;
        if (this.toolbox){
	        var toolbars = this.toolbox.toolbars;
	        for (i = 0; i < toolbars.length; i++) {
	            var toolbarItems = toolbars[i].items;
	            for (j = 0; j < toolbarItems.length; j++) {
	                var combo = toolbarItems[j].combo;
	                if (combo) {
	                    combo.setState(isReadOnly ? CKEDITOR.TRISTATE_DISABLED: CKEDITOR.TRISTATE_OFF);
	                }
	                var button = toolbarItems[j].button;
	                if (button && button.createPanel) {
	                    button.setState(isReadOnly ? CKEDITOR.TRISTATE_DISABLED: CKEDITOR.TRISTATE_OFF);
	                }
	            }
	        }
        }
	};
};

/**
 * 创建整体显示对象
 * 
 * @private
 */
EditorComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = document.createElement("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	/*
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	*/
	this.Div_gen.style.width =  this.width;
	/*
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	*/
	this.Div_gen.style.height =  this.height;
	// guoweic: modify end
	this.Div_gen.style.position = this.position;
	this.Div_gen.className = this.className;
	this.Div_gen.style.overflow = "auto";
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
	this.createFrame();
};

/**
 * 创建编辑器的toolbar,内容区,操作bar
 * 
 * @private
 */
EditorComp.prototype.manageSelf = function() {
	/*
	this.textarea = $ce("TEXTAREA");
	this.textarea.style.width = "100%";
	this.textarea.style.height = "100%";
	this.Div_gen.appendChild(this.textarea);
	this.contentId = this.id + "_content";
	this.textarea.name = this.contentId;
	this.Div_gen.innerHTML = '<textarea name="' + this.contentId + '" style="width:100%;height:30px;"></textarea>';
	this.Div_gen.innerHTML = '<textarea name="' + this.contentId + '" style="width:100%;height:100%;visibility:hidden;">ddd</textarea>';
	this.createToolBars();
	this.initEditor();
	this.createOperateBars();
	*/
	this.contentId = this.id + "_content";
	this.Div_gen.innerHTML = '<textarea name="' + this.contentId + '" style="width:100%;height:30px;"></textarea>';
};

/**
 * 创建编辑器
 * 
 * @private create()的时候调用
 */
EditorComp.prototype.createFrame = function() {
	var oThis = this;
	CKEDITOR.basePath = window.frameGlobalPath + "/ui/editor/";
	var spanHeight = 0;
	if (this.toolbarType == "Full")
		spanHeight = this.Div_gen.offsetHeight - 113;
	else if (this.toolbarType == "Custom")	
		spanHeight = this.Div_gen.offsetHeight -113;
//	var spanWidth = this.Div_gen.offsetWidth - 5;
	//TODO 当有label时，需重新计算宽度
	var spanWidth = "100%;border:0px;padding:0px";
	var langCookie = getCookie('LA_K1');
	var lang = 'zh-cn';
	if (langCookie == 'english') // 英文
		lang = 'en';
	else if (langCookie == 'tradchn') //中文繁体
		lang = 'zh';
	else if (langCookie == 'french') //法文
		lang = 'fr';
			
	if (spanHeight > 0){
		CKEDITOR.replace(
			this.contentId,
			{
				height:spanHeight,width:spanWidth,toolbar:this.toolbarType,language:lang,
				filebrowserUploadUrl : window.globalPath + '/ckeditor/uploader?Type=File',
				filebrowserImageUploadUrl : window.globalPath + '/ckeditor/uploader?Type=Image',
				filebrowserFlashUploadUrl : window.globalPath + '/ckeditor/uploader?Type=Flash'
			}
		);
	} else {
		CKEDITOR.replace(
			this.contentId,
			{
				language:lang,width:spanWidth,
				filebrowserUploadUrl : window.globalPath + '/ckeditor/uploader?Type=File',
				filebrowserImageUploadUrl : window.globalPath + '/ckeditor/uploader?Type=Image',
				filebrowserFlashUploadUrl : window.globalPath + '/ckeditor/uploader?Type=Flash'
			}
		);
	}
	var editor1 = CKEDITOR.instances[this.contentId];
	oThis.editor = editor1;
	
	//扩展CKEditor
	oThis.extendCKEditor();
	
	//判断是否是只读模式，如果不是再注册onblur事件
	if (oThis.readOnly) {
		//等editor初始化后设置只读
		window.setTimeout(function(){
			oThis.editor.setReadOnly(true);
		},500);
	} else {
		editor1.on("blur", function(e) {
			oThis.onblur(e);
		});
	}

	/*
	KindEditor.ready(
		function(K) {
			var editor1 = K.create('textarea[name="' + oThis.contentId + '"]', {allowFileManager : true});
			oThis.editor = editor1;
			editor1.afterBlur = function(e){
				oThis.onblur(e);
			}
		}
	);
	*/
};


/**
 * 清除内容
 * 
 * @private
 */
EditorComp.prototype.cleanHtml = function() {
	// guoweic: modify start 2009-11-24
	var win = this.editorWindow;
	if (IS_IE) {
		var fonts = win.document.body.all.tags("FONT");
	} else {
		win = this.frame.contentWindow;
		var fonts = win.document.getElementsByTagName("FONT");
	}
	// guoweic: modify end
	var curr;
	for ( var i = fonts.length - 1; i >= 0; i--) {
		curr = fonts[i];
		if (curr.style.backgroundColor == "#ffffff")
			curr.outerHTML = curr.innerHTML;
	}

};

/**
 * 插入内容
 * 
 * @private
 */
EditorComp.prototype.oblog_InsertSymbol = function(str1) {
	this.editorWindow.focus();
	// guoweic: modify start 2009-11-23
	var oblog_selection;
	if(this.oblog_selection){
		oblog_selection = this.oblog_selection;
	}else{
		oblog_selection = this.oblog_selectRange();
	}
	
	if (IS_IE){
		var oRange = oblog_selection.createRange();
		// EditorComp.currectSelectRange(oRange,EditorComp.$oRange);
		oRange.pasteHTML(str1);
	}
		
	else {
        var spanElement = document.createElement("span");
        spanElement.innerHTML = str1;
		EditorComp.insertElement(this.frame.contentWindow, spanElement);
	}
	// guoweic: modify end
};

/**
 * 设值
 */
EditorComp.prototype.setValue = function(value) {
	if (!this.editor) return;
	if (value != null){
		value = decodeURIComponent(value);
		this.editor.setData(value);
	} else {
		this.editor.setData("");
	}
};

/**
 * 追加值
 */
EditorComp.prototype.appendValue = function(value) {
	if(value != null){
		if(this.editor){
			var text = this.getText();
			text = text + value;
			this.editor.setData(text);
		}
	}
};

/**
 * 插入值
 */
EditorComp.prototype.insertValue = function(value) {
	if(value != null){
		if(this.editor)
			this.editor.insertHtml(value);
	}
};

/**
 * 获取editor的内容
 */
EditorComp.prototype.getValue = function() {
	if(this.editor){
		try{
			return encodeURIComponent(this.editor.getData());
		}catch(e){}
	}
	return null;
};

/**
 * 获取editor中的文本
 */
EditorComp.prototype.getText = function() {
	if(this.editor){
		//if(this.editor.html)
		return this.editor.document.getBody().getText();
	}
	return null;
};

/**
 * 设置可编辑状态
 */
EditorComp.prototype.setActive = function(active) {
	var oThis = this;
	window.setTimeout(function(){
		oThis.readOnly = !active;
		//oThis.editor.execCommand('ReadonlyCmd');
		if(oThis.readOnly == true){
			oThis.editor.setReadOnly(true);
		}else{
			oThis.editor.setReadOnly(false);
		}
	},1000);
};

/**
 * 用户自定义blur事件
 * 
 * @private
 */
EditorComp.prototype.onblur = function(e) {
	var focusEvent = {
			"obj" : this.editor,
			"event" : e
		};
//	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
	this.doEventFunc("onBlur", focusEvent);
	
};

/**
 * editor之外点击事件
 */
EditorComp.prototype.outsideClick = function(e) {
	this.onblur();
};

/**
 * 设置被修改的控件状态
 * @since V6.3
 * @param context 后台发生变化的值，json对象
 */
EditorComp.prototype.setChangedContext = function(context) {
	if (context.enable != null)
		this.setActive(context.enable);
	if (context.readOnly != null)
		this.setActive(!context.readOnly);
	if (context.value != null && context.value != this.getValue())
		this.setValue(context.value);
};