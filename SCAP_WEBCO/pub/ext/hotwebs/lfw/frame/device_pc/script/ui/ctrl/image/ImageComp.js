/**
 * @fileoverview ImagaComp控件.
 * 用图片取代按钮的外观,功能完全等同于按钮.	
 * 
 * @author guoweic
 * @version NC6.0
 * @since NC5.5
 *
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修改图片Div_gen的float属性设置 . guoweic <b>modified</b>
 * 
 */

ImageComp.prototype = new BaseComponent;
ImageComp.prototype.componentType = "IMAGE";	

/**
 * ImageComp控件构造函数
 * @class 图片控件
 * @param refImg1 图片处于未选中效果样式图片的绝对路径
 * @param refImg2 图片处于选中效果的样式图片的绝对路径
 * @param attrObj.inactiveImg 图片处于禁用效果的样式图片的绝对路径
 * @constructor
 * @extends BaseComponent
 */
function ImageComp(parent, name, refImg1, left, top, width, height, alt, refImg2, attrObj) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	
	// 如果没有传入的width或者height则按照实际图片的大小显示
	if (width == '' || width == null || isNaN(parseInt(width)))
		this.width = "auto";		
	if (height == '' || height == null || isNaN(parseInt(height)))
		this.height = "auto";		
	// 修改默认图片为空白
	this.refImg1 = getString(refImg1, window.themePath + "/ui/basic/images/transparent.gif");
	this.refImg2 = getString(refImg2, null);
	this.alt = alt;
	this.parentOwner = parent;
	
	this.position = "relative";
	// 标示图片是否处于激活状态	
	this.disabled = false;
	// 标示图片是否处于可见状态
	this.visible = true;
	this.boolFloatRight = false;	
	this.boolFloatLeft = false;
	this.inactiveImg = this.refImg1;
	//可以上传
	this.canUpload = false;
	this.maxShow = false;
	if (attrObj != null) {
		this.position = getString(attrObj.position, this.position);
		this.disabled = getBoolean(attrObj.disabled, this.disabled);
		this.visible = getBoolean(attrObj.visible, this.visible);
		this.canUpload = getBoolean(attrObj.canUpload, this.canUpload);
		this.sysid = attrObj.sysid;
		this.baModule = attrObj.baModule; 
		if (attrObj.boolFloatRight != null && attrObj.boolFloatRight == true) {
			this.boolFloatRight = attrObj.boolFloatRight;		
			this.position = "relative";
		}
		if (attrObj.boolFloatLeft != null && attrObj.boolFloatLeft == true) {
			this.boolFloatLeft = attrObj.boolFloatLeft;
			this.position = "relative";			
		}
		this.inactiveImg = getString(attrObj.inactiveImg, this.refImg1);
		this.maxShow = getBoolean(attrObj.maxShow, this.maxShow);
	}
	
	if(this.sysid == null)
	  this.sysid = "bafile";
	  
	this.create(); 
};

/**
 * ImageComp的主体创建函数
 * @private
 */
ImageComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	
	if(!this.visible){
		this.hideV();
	}
	if (this.boolFloatLeft) {
		// 适应firefox
		this.Div_gen.style[ATTRFLOAT] = "left";
		// 适应IE
		this.Div_gen.style.styleFloat = "left";
	} 
	if (this.boolFloatRight) {
		// 适应firefox
		this.Div_gen.style[ATTRFLOAT] = "right";
		// 适应IE
		this.Div_gen.style.styleFloat = "right";
	}
	
	this.Div_gen.onselectstart = function() { 
		return false; 
	};
	
	if (this.parentOwner)
	 	this.placeIn(this.parentOwner);
	if (window.editMode != null && window.editMode == true){
		this.Div_gen.appendChild(document.createTextNode(trans("ml_image")/*"图片"*/));
		return;
	}
	 	
};

/**
 * @private
 */
ImageComp.prototype.manageSelf = function() {
	var oThis = this;
	
	//创建上传删除按钮区
	this.optionDiv = $ce("DIV");
	this.Div_gen.appendChild(this.optionDiv);
	this.optionDiv.style.position = "absolute";
	this.optionDiv.style.bottom = "0px";
	this.optionDiv.style.width = "100%";
	this.optionDiv.style.textAlign = "center";
	this.optionDiv.style.display = "none";
	var a = $ce("a");
	a.style.paddingRight = "10px";
	a.href = "javascript:void(0)";
	a.innerHTML = trans("ml_upload_upload")/*"上传"*/;
	a.onclick = function(e){
		document.onclick();
		oThis.uploadFile();
		e = EventUtil.getEvent();
		stopAll(e);
	};
	this.optionDiv.appendChild(a);
	var da = $ce("a");
	da.href = "javascript:void(0)";
	da.innerHTML = trans("ml_upload_delete")/*"删除"*/;
	da.onclick = function(e){
//		oThis.currentFileId = fileId;
		showConfirmDialog(trans("ml_image_isDelete")/*是否删除选定的图片？*/, ImageComp.deleteFile,null, oThis, null, null,trans("ml_upload_delete")/*"删除"*/,trans("ml_upload_notdelete")/*"不删除"*/,trans("ml_upload_delete")/*"删除"*/);
		e = EventUtil.getEvent();
		stopAll(e);
	};
	this.optionDiv.appendChild(da);
	

	
	
	//创建普通状态图片	
	this.img1 = $ce("IMG");
	//add by jizhg 解决图片自动伸缩问题
	this.img1.style.width="100%";
	this.img1.style.height="100%";
//	this.img1.src = this.refImg1;
	this.setImg1(this.refImg1);
	
	this.Div_gen.appendChild(this.img1);
	if (this.width == "auto" && !IS_IE)
		this.Div_gen.style.width = this.img1.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height == "auto" && !IS_IE)
		this.Div_gen.style.height = this.img1.height + "px";
	else{
		this.Div_gen.style.height = this.height;
	}
	if(window.editMode)
		this.Div_gen.style.minHeight = "15px";
	
	//ie下宽高不能100%撑开
//	if (IS_IE){
//		this.img1.style.width="100%";
//		this.img1.style.height="100%";
//	}
	// 如果设置了width,height,则按照指定大小显示
	if (this.width != "auto" && this.width != "100%" || this.height != "auto" && this.height != "100%"){
  		if(this.width != "auto" && this.width != "100%")
   			this.img1.style.width = "100%";
     	if(this.height != "auto" && this.height != "100%")
      		this.img1.style.height = "100%"; 
 	}
 	if(this.maxShow == true){
 		this.img1.style.width = "100%";
 		this.img1.style.height = "100%";
 	}
	//else
  	//	this.img1.style.position = "auto";		
	// 设置鼠标移动到图片上的提示信息
	if (this.alt != null) 
		this.Div_gen.title = this.alt;
 	
 	// 如果在装载过程中发生了错误,则调用该处理函数
	this.img1.onerror = function (e) {
	};
	
	if (this.refImg2 != "") {					   
		this.Div_gen.onmouseout = function(e) {											   
		   if (!oThis.disabled) {
			   e = EventUtil.getEvent();
			   oThis.setImg1(oThis.refImg1);
//			   oThis.img1.src = oThis.refImg1; 
			   this.style.cursor = "default";
			   oThis.onmouseout(e);
				// 删除事件对象（用于清除依赖关系）
			   clearEventSimply(e);			
		   }
		};				   
	}
  
	this.Div_gen.onmouseover = function(e) {						
		e = EventUtil.getEvent();
				   
		if (!oThis.disabled) {
		   this.style.cursor = "pointer";  
		   // 更换图片为激活状态图片
		   if (oThis.refImg2) {						  
			 oThis.img1.src = oThis.refImg2;
		   }
		} else {
			this.style.cursor = "default";
		}
		oThis.onmouseover(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};  
	 
	// 图片点击事件处理   
	this.Div_gen.onclick = function(e) {
		e = EventUtil.getEvent();
		if (!oThis.disabled) {
			oThis.onclick(e);						  
			stopEvent(e);
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.Div_gen.oncontextmenu = function(e) {
		oThis.oncontextmenu(e);
	};
};

ImageComp.prototype.uploadFile = function() {
	pageUI.afterImageUpload = function(pk){
		  //var url = window.globalPath + '/pt/file/down?id=' + pk;
		  if (pageUI._ImageComp != null){
//		   var url = getFileService().execute("getDownUrl", "$S_" + pk, "$S_" + pageUI._ImageComp.sysid);
		   pageUI._ImageComp.setValue(pk);
		   pageUI._ImageComp = null;
		  }
	 };
	var url =window.globalPath +  "/core/file.jsp?pageId=file&method=pageUI.afterImageUpload&isalert=false&multi=false&closeDialog=true";
	if(this.sysid != null)
		url += "&sysid=" + this.sysid;
	if(this.baModule != null)
		url += "&baModule=" + this.baModule;
	pageUI._ImageComp = this;
	showDialog(url, "上传图片", 450 ,430, "file select", null,{isShowLine:false}) ;
};


ImageComp.deleteFile = function(imageComp) {
	if (imageComp.refImg1 == null) return;
	try{
	 	getFileService().execute("deleteFile", "$S_" + imageComp.pk, "$S_" + imageComp.sysid);
	}catch(e){}
 	imageComp.setValue("");
//	var fileId = imageComp.refImg1;
//	getFileService().execute("deleteFile", "$S_" + fileId);
};


/**
 * 设置控件边界值.子控件可根据实际情况覆盖此函数
 * @param left   控件左侧X坐标
 * @param top    控件顶部Y坐标
 * @param width  控件的宽度
 * @param height 控件的高度
 * @public
 */
ImageComp.prototype.setBounds = function(left, top, width, height) {	
	// 改变数据对象的值
	this.left = getInteger(left, 0);
	this.top = getInteger(top, 0);
	if (width == "auto")
		this.width = "auto"; 
	else		
		this.width = getString(convertWidth(width), "100%");
	if (height == "auto")
		this.height = "auto";
	else	
		this.height = getString(convertHeight(height), "100%");
	// 改变显示对象的值
	this.getObjHtml().style.left = this.left + "px";	
	this.getObjHtml().style.top = this.top + "px";
	this.getObjHtml().style.width = this.width;
	this.getObjHtml().style.height = this.height;
};

/**
 * 改变显示图片
 * @param {string} src1 未选中状态图片src
 * @param {string} src2 选中状态图片src
 * @public
 */
ImageComp.prototype.changeImage = function(src1, src2) {
	this.refImg1 = src1;
	this.setImg1(src1);
	if (this.refImg2 != '') 
		this.refImg2 = src2;
//	if(this.img1){
//		this.img1.src = this.refImg1;
//		if (isNull(this.refImg1))
//			this.img1.style.display = "none";
//		else	
//			this.img1.style.display = "";
//		this.img1.removeAttribute("width");
//		this.img1.removeAttribute("height");
//	}
};

ImageComp.prototype.setImg1 = function(src1) {
	if(this.img1){
		this.img1.src = src1;
		if (isNull(src1))
			this.img1.style.display = "none";
		else	
			this.img1.style.display = "";
		this.img1.removeAttribute("width");
		this.img1.removeAttribute("height");
	}
};


/**
 * 改变提示信息
 * @param {String} alt
 * @public
 */
ImageComp.prototype.changeAlt = function(alt) {
	this.Div_gen.title = alt;
};

/**
 * 设置此图片控件的激活状态
 * @param {Boolean} isActive true表示处于激活状态,否则表示禁用状态
 * @public
 */
ImageComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {	
		this.mouseoutFunc = this.Div_gen.onmouseout;
		this.mouseoverFunc = this.Div_gen.onmouseover;
		this.clickFunc = this.Div_gen.onclick;
		if (this.Div_gen.ondblclick)
			this.dbclickFunc = this.Div_gen.ondblclick;
		this.contextmenuFunc = this.Div_gen.oncontextmenu;
		this.Div_gen.onmouseout = function(){};
		this.Div_gen.onmouseover = function(){};
		this.Div_gen.ondblclick = function(){};
		this.Div_gen.onclick = function(){};
		this.Div_gen.oncontextmenu = function(){};
		this.disabled = true;
		//变换图片风格为禁用状态
//		this.img1.src = this.inactiveImg;
		this.setImg1(this.inactiveImg);
		this.optionDiv.style.display = "none";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		if (this.mouseoutFunc)
			this.Div_gen.onmouseout = this.mouseoutFunc;
		if (this.mouseoverFunc)	
			this.Div_gen.onmouseover = this.mouseoverFunc;
		if (this.clickFunc)	
			this.Div_gen.onclick = this.clickFunc;
		if (this.dblclickFunc)
			this.Div_gen.ondblclick = this.dblclickFunc;
		if (this.contextmenuFunc)	
			this.Div_gen.oncontextmenu = this.contextmenuFunc;
		this.disabled = false;
//		this.img1.src = this.refImg1;
		this.setImg1(this.refImg1);
		if (this.canUpload == true)
			this.optionDiv.style.display = "block";
	}
	this.notifyChange(NotifyType.ENABLE, !this.disabled);
};

/**
 * 得到输入框的激活状态
 * @return {Boolean} isActive
 * @public
 */
ImageComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 鼠标移出事件
 * @private
 */
ImageComp.prototype.onmouseout = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseout", mouseEvent);
};

/**
 * 鼠标移入事件
 * @private
 */
ImageComp.prototype.onmouseover = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", mouseEvent);
};

/**
 * 单击事件
 * @private
 */
ImageComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", mouseEvent);
};

/**
 * 设置可见
 * @param {Boolean} visible 
 * @public
 */
ImageComp.prototype.setVisible = function(visible) {
	if (this.visible == visible) return;
	if(visible)
		this.showV();
	else
		this.hideV();
	this.notifyChange(NotifyType.VISIBLE, visible);	
};


/**
 * @description 设置被修改的控件状态
 * @since V6.3
 * @param {Object} context 后台发生变化的值，json对象
 * @private
 */
ImageComp.prototype.setChangedContext = function(context) {
	if (context.enable != null)
		this.setActive(context.enable);
	if (context.visible != null && context.visible != this.visible){
		this.visible = context.visible;
		if (this.visible == true)		
			this.showV();
		else
			this.hideV();
	}	
	if (context.image1 != null && context.image1 != this.refImg1) {
		this.refImg1 = context.image1;
		this.setImg1(this.refImg1);
//		this.img1.src = this.refImg1;
	}
	if (context.image2 != null && context.image2 != this.refImg2) {
		this.refImg2 = context.image2;
	}
};