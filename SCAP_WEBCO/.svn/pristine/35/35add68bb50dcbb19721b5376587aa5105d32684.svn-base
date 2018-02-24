/**
 * 
 * 鎻愮ず淇℃伅鎺т欢
 * 
 * 娉ㄦ剰锛氬浜庡搴﹀拰楂樺害浠ュ強鏂囧瓧鍙樺寲鏈?粓閮借璋冪敤setStyle锛岄?杩噑etStyle浠ュ強setTextStyle瀵规樉绀烘晥鏋滆繘琛岃皟鏁达紝鍏朵粬鍦版柟涓嶈繘琛岃皟鏁?
 * @author guoweic
 * @version NC6.0
 * 
 */
MessageComp.prototype = new BaseComponent;
MessageComp.prototype.componentType = "MESSAGE";

MessageComp.prototype.IMG_LEFT = 10;
MessageComp.prototype.IMG_WIDTH = 40;
MessageComp.prototype.IMG_HEIGHT = 40;
MessageComp.DEFAULT_X = 30;
MessageComp.DEFAULT_Y = 30;
MessageComp.DEFAULT_IMG_URL = window.themePath  + "/ui/ctrl/message/images/img.png";

MessageComp.showPositionArr = ['top-left','top-center','top-right','center','bottom-left','bottom-center','bottom-right'];


/**
 * 鎻愮ず淇℃伅鎺т欢鏋勯?鏂规硶
 * 
 * @class 鎻愮ず淇℃伅鎺т欢
 * 
 * @param name 鍚嶇О
 * 
 * @param left
 * @param top
 * @param width 瀹藉害
 * @param height 楂樺害
 * @param text 鏄剧ず鏂囧瓧
 * @param showPosition 鏄剧ず浣嶇疆绫诲瀷 ("top-left","top-center","top-right","center","bottom-left","bottom-center","bottom-right")
 * @param x 妯悜鍋忓樊
 * @param y 绾靛悜鍋忓樊
 * @param className 绫诲悕
 * @extends BaseComponent
 */
function MessageComp(name, left, top, width, height, text, attr) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.minWidth = 250;  //鏈?皬瀹藉害
	this.minHeight = 100; //鏈?皬楂樺害
	this.width = getString(width, "");
	this.height = getString(height, "");
	// 璁剧疆瀹藉害楂樺害鐘舵?
	MessageComp.setWidthHeightFlag(this);
	
	this.x = "0";
	this.y = "0";
	this.showPosition = "center";
	this.className = "message_div";
	this.isOpacity = true;
	this.hasImg = true;
	this.imgUrl = MessageComp.DEFAULT_IMG_URL;
	this.isNew = false;
	
	this.text = text;
	if(attr != null){
		this.x = getString(attr.x, "0");
		this.y = getString(attr.y, "0");
		this.showPosition = getString(attr.showPosition, "center");
		this.className = getString(attr.className, "message_div");
		this.isOpacity = getBoolean(attr.isOpacity, true);
		this.hasImg = getBoolean(attr.hasImg, true);
		this.imgUrl = getString(attr.imgUrl, MessageComp.DEFAULT_IMG_URL);
		this.isNew = getBoolean(attr.isNew, false);
	}
	this.parentOwner = window.top.document.body;
	this.create();
};

/**
 * @private
 */
MessageComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
    var frmstr = '<iframe src="" style="position:absolute; visibility:inherit; top:4px; left:4px; width:100%;height:100%; z-index:-1; border:none;" frameborder="0"></<iframe>';
    this.Div_gen.innerHTML = frmstr;
	
//	this.Div_gen.id = this.id;
    this.Div_gen.id = 'message';
	this.Div_gen.className = this.className;
	this.Div_gen.style.display = "none";
	this.Div_gen.style.zIndex = "-1";
	this.Div_gen.style.left = "0px";
	this.Div_gen.style.top = "0px";
	this.Div_gen.style.minWidth = this.minWidth + "px";
	this.Div_gen.style.minHeight = this.minHeight + "px";
	if(!this.nullWidth){
		if(this.preWidth)
			this.Div_gen.style.width = this.width;
		else
			this.Div_gen.style.width = this.width + "px";
	}else{
		this.Div_gen.style.width = "100%";
	}
	if(!this.nullHeight){
		if(this.preHeight)
			this.Div_gen.style.height = this.height;
		else
			this.Div_gen.style.height = this.height + "px";
	}else{
		this.Div_gen.style.height = "100%";
	}
		
	// 鍒涘缓鏁翠綋DIV
	this.createMessageDiv();
	
	
	// 鍒涘缓鍥剧墖DIV
	this.createImgDiv();
	
	// 鍒涘缓鍏抽棴鎸夐挳
	this.closeImg = $ce("DIV");
	this.closeImg.className = "message_close_img";
	this.messageDiv.appendChild(this.closeImg);
	this.closeImg.onclick = function() {
		oThis.hideMsg();
		if(typeof MessageCompAfterClose=== "function"){
				MessageCompAfterClose(oThis.id);
		}
		if(oThis.isNew){
			// 鍒犻櫎鏁扮粍涓搴旀帶浠?
			if(window.$_messageCompArr){
				if(window.$_messageCompArr.length){
					for(var i = 0; i < window.$_messageCompArr.length; i++){
						if(window.$_messageCompArr[i] == oThis){
							window.$_messageCompArr.splice(i, 1);
							window.$_messageCompHeightArr.splice(i, 1);
						}
					}
				}
			}
			if(window.$_messageComp){
				if(window.$_messageComp == oThis){
					window.$_messageComp = null;
				}
			}
			oThis.destroySelf();
		}
	};
	this.closeImg.onmouseover = function(){
		this.className = "message_close_img_over";
	};
	this.closeImg.onmouseout = function(){
		this.className = "message_close_img";
	};
	
	// 鍒涘缓娓愰殣DIV
	this.createBottomDiv();
	
	// 鍒涘缓鎻愮ず淇℃伅DIV
	this.cteateTextDiv();
	
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
		
	addResizeEvent(this.Div_gen, function() {
		MessageComp.messageCompResize();
	});
};


/**
 * 鍒涘缓鏁翠綋DIV
 * @private
 */
MessageComp.prototype.createMessageDiv = function(){
	this.messageDiv = $ce("DIV");
	this.messageDiv.className = 'background_div';
	this.messageDiv.style.minWidth = this.minWidth + "px";
	this.messageDiv.style.minHeight = this.minHeight + "px";
	if(!this.nullWidth){
		if(this.preWidth)
			this.messageDiv.style.width = this.width;
		else
			this.messageDiv.style.width = this.width + "px";
	}else{
		this.messageDiv.style.width = "100%";
	}
	if(!this.nullHeight){
		if(this.preHeight)
			this.messageDiv.style.height = this.height;
		else
			this.messageDiv.style.height = this.height + "px";
	}else{
		this.messageDiv.style.height = "100%";
	}
	this.Div_gen.appendChild(this.messageDiv);
	 
	this.bgLeftTopDiv = $ce("DIV");
	this.bgLeftTopDiv.className = 'bg_left_top_div';
	this.bgCenterTopDiv = $ce("DIV");
	this.bgCenterTopDiv.className = 'bg_center_top_div';
	this.bgRightTopDiv = $ce("DIV");
	this.bgRightTopDiv.className = 'bg_right_top_div';
	
	this.bgLeftCenterDiv = $ce("DIV");
	this.bgLeftCenterDiv.className = 'bg_left_center_div';
	this.bgCenterDiv = $ce("DIV");
	this.bgCenterDiv.className = 'bg_center_div';
	this.bgRightCenterDiv = $ce("DIV");
	this.bgRightCenterDiv.className = 'bg_right_center_div';
	this.bgLeftBottomDiv = $ce("DIV");
	this.bgLeftBottomDiv.className = 'bg_left_bottom_div';
	this.bgCenterBottomDiv = $ce("DIV");
	this.bgCenterBottomDiv.className = 'bg_center_bottom_div';
	this.bgRightBottomDiv = $ce("DIV");
	this.bgRightBottomDiv.className = 'bg_right_bottom_div';
	
	this.messageDiv.appendChild(this.bgLeftTopDiv);
	this.messageDiv.appendChild(this.bgCenterTopDiv);
	this.messageDiv.appendChild(this.bgRightTopDiv);
	
	this.messageDiv.appendChild(this.bgLeftCenterDiv);
	this.messageDiv.appendChild(this.bgCenterDiv);
	this.messageDiv.appendChild(this.bgRightCenterDiv);
	
	this.messageDiv.appendChild(this.bgLeftBottomDiv);
	this.messageDiv.appendChild(this.bgCenterBottomDiv);
	this.messageDiv.appendChild(this.bgRightBottomDiv);
};


/**
 * 鍒涘缓鍥剧墖DIV
 * @private
 */
MessageComp.prototype.createImgDiv = function(){
	if(this.hasImg){
		if(!this.imgDiv){
			this.imgDiv = $ce("DIV");
			this.imgDiv.style.left = this.IMG_LEFT + "px";
			this.imgDiv.style.width = this.IMG_WIDTH + "px";
			this.imgDiv.style.height = this.IMG_HEIGHT + "px";
			this.imgDiv.style.position = "absolute";
			this.imgDiv.style.top = "50%";
			this.bgCenterDiv.appendChild(this.imgDiv);
		}
		if(!this.imgNode){
			this.imgNode = $ce("IMG");
			this.imgNode.src = this.imgUrl;
			this.imgNode.style.width = this.IMG_WIDTH + "px";
			this.imgNode.style.height = this.IMG_HEIGHT + "px";
			this.imgNode.style.top = "-50%";
			this.imgNode.style.position = "relative";
			this.imgDiv.appendChild(this.imgNode);
		}
	}
};

/**
 * 鍒涘缓娓愰殣DIV
 * @private
 */
MessageComp.prototype.createBottomDiv = function(){
	// 濡傛灉娓愰殣
	if(this.isOpacity){
		if(!this.bottomMsg){
			this.bottomMsg = $ce("DIV");
			this.bottomMsg.className = "bottom_msg_div";
			this.bgCenterDiv.appendChild(this.bottomMsg);
		}
		if(!this.bottomTime){
			this.bottomTime = $ce("SPAN");
			this.bottomTime.innerHTML = "3";
		}
		if(!this.bottomText){
			this.bottomText = $ce("SPAN");
			this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [3]),"秒钟后自动消失");
			var bottomWidth = getTextWidth(getString(trans("ml_messagecomp_tip1", [3]),"秒钟后自动消失"));
			this.bottomMsg.appendChild(this.bottomText);
			/*鏍规嵁娓愰殣鎻愮ず鍐呭鏀瑰彉瀹藉害*/
			bottomWidth = bottomWidth + 100; 
			if (this.minWidth < bottomWidth){
				this.minWidth = bottomWidth;
			}
		}
	}
};


/**
 * 鍒涘缓鎻愮ず淇℃伅DIV
 * @private
 */
MessageComp.prototype.cteateTextDiv = function(){
	// 鎻愮ず淇℃伅鏁翠綋DIV 涓ゅ眰div鏄负浜嗗鐞嗗眳涓?
	this.textWholeDiv = $ce("DIV");
	this.textWholeDiv.style.top = "50%";
	this.textWholeDiv.style.position = "absolute";
	
	this.textDiv = $ce("DIV");
	this.textDiv.style.top = "-50%";
	this.textDiv.style.position = "relative";
	this.textDiv.style.overflow = "auto";
	
	// realTextDiv鏄负浜嗘樉绀烘粴鍔ㄦ潯
	this.realTextDiv = $ce("DIV");
	this.realTextDiv.className = "message_text_div";
	this.realTextDiv.style.position = "relative";
	this.realTextDiv.innerHTML = this.text;
	
	this.textDiv.appendChild(this.realTextDiv);
	this.textWholeDiv.appendChild(this.textDiv);
	this.bgCenterDiv.appendChild(this.textWholeDiv);
};



MessageComp.prototype.setWidth = function(width){
	this.width = width;
	MessageComp.setWidthHeightFlag(this);
	
};


MessageComp.prototype.setHeight = function(height){
	this.height = height;
	MessageComp.setWidthHeightFlag(this);
};

/**
 * 鏄剧ず鎻愮ず淇℃伅
 * @public
 */
MessageComp.showMessage = function(text, attr) {
	debugger;
	var width = "";
	var height = "";
	var isNew = false;
	var isOpacity = true;
	var hasImg = false;
	var name = "";
	if(attr != null){
		width = getString(attr.width, "");
		height = getString(attr.height, "");
		isNew = getBoolean(attr.isNew, false);
		isOpacity = getBoolean(attr.isOpacity, true);
		hasImg = getBoolean(attr.hasImg, true);
		name = getString(attr.id, "");
	}
	
	// isNew鏃堕渶瑕佸垱寤烘柊鐨刴essageComp
	if (!window.$_messageComp || isNew || window.$_messageComp.isNew == true) {
		window.$_messageComp = new MessageComp(name, 0, 0, width, height, text, attr);
	} else {
		// 鐢变簬鏄惁娓愰殣浠ュ強鏄惁瀛樺湪鍥剧墖闇?瀵筪iv灞傛缁撴瀯鍙戠敓鏀瑰彉锛屽洜姝ら渶瑕侀噸鏂板垱寤烘柊鐨刴essageComp
		if(window.$_messageComp){
			if(window.$_messageComp.isOpacity != isOpacity || window.$_messageComp.hasImg != hasImg){
				if (window.$_messageComp.timeoutFunc)
					clearTimeout(window.$_messageComp.timeoutFunc); 
				window.$_messageComp.destroySelf();
				window.$_messageComp = null;
				window.$_messageComp = new MessageComp(name, 0, 0, width, height, text, attr);
			}
		}
		// 闇?鍏堝皢鎺т欢闅愯棌锛屽惁鍒欎細鍑虹幇涓?棯涓?棯鐨勬儏鍐?
		window.$_messageComp.hideMsg();
		if(attr != null){
			window.$_messageComp.x = getString(attr.x, "0");
			window.$_messageComp.y = getString(attr.y, "0");
			window.$_messageComp.showPosition = getString(attr.showPosition, "center");
			window.$_messageComp.className = getString(attr.className, "message_div");
			window.$_messageComp.Div_gen.className = window.$_messageComp.className;
			window.$_messageComp.isOpacity = getBoolean(attr.isOpacity, true);
			window.$_messageComp.hasImg = getBoolean(attr.hasImg, true);
			window.$_messageComp.imgUrl = getString(attr.imgUrl, MessageComp.DEFAULT_IMG_URL);
			if(window.$_messageComp.imgNode){
				window.$_messageComp.imgNode.src = window.$_messageComp.imgUrl;
			}
			window.$_messageComp.isNew = getBoolean(attr.isNew, false);
		}
		window.$_messageComp.setWidth(width);
		window.$_messageComp.setHeight(height);
		window.$_messageComp.setText(text);
		window.$_messageComp.resetWidthHeight();
	}
	window.$_messageComp.showMsg();
	// 鐢变簬showMsg涓缃甦isplay涓篵lock瀛樺湪寤惰繜锛屽彧鏈変负block鏃惰绠楃殑浣嶇疆鎵嶆纭洜姝ゆ澶勫鍔犲欢杩熴?
	setTimeout(function (){
		if(window.$_messageComp)
			window.$_messageComp.setStyle();
	},90);
	// 濡傛灉isNew涓簍rue鍒欏皢window.$_messageComp鍙婂叾鏄剧ず楂樺害鏀惧叆鏁扮粍涓紝骞惰皟鐢ㄩ噸鏂板竷灞?
	if(isNew)
		setTimeout(function (){
			if(window.$_messageComp)
				MessageComp.addNewMessage(window.$_messageComp);
		},90);
		
};


/**
 * 璁剧疆鏄剧ず澶у皬鍜屼綅缃?
 * @private
 */
MessageComp.prototype.setStyle = function() {
	// 棣栧厛鏍规嵁鏂囧瓧鍐呭瀵瑰搴﹁繘琛屼慨鏀?
	this.setTextStyle();
	
	/*this.bgLeftTopDiv.style.filter = "alpha(opacity=0)";
	this.bgCenterTopDiv.style.filter = "alpha(opacity=0)";
	this.bgRightTopDiv.style.filter = "alpha(opacity=0)";
	this.bgLeftCenterDiv.style.filter = "alpha(opacity=0)";
	this.bgCenterDiv.style.filter = "alpha(opacity=0)";
	this.bgRightCenterDiv.style.filter = "alpha(opacity=0)";
	this.bgLeftBottomDiv.style.filter = "alpha(opacity=0)";
	this.bgCenterBottomDiv.style.filter = "alpha(opacity=0)";
	this.bgRightBottomDiv.style.filter = "alpha(opacity=0)";
	this.messageDiv.style.filter = "alpha(opacity=0)";*/
	setOpacity(this.bgLeftTopDiv, 100);
	setOpacity(this.bgCenterTopDiv, 100);
	setOpacity(this.bgRightTopDiv, 100);
	setOpacity(this.bgLeftCenterDiv, 100);
	setOpacity(this.bgCenterDiv, 100);
	setOpacity(this.bgRightCenterDiv, 100);
	setOpacity(this.bgLeftBottomDiv, 100);
	setOpacity(this.bgCenterBottomDiv, 100);
	setOpacity(this.bgRightBottomDiv, 100);
	setOpacity(this.messageDiv, 100);
		
	var left = 0;
	var top1 = 0;  
	// 鏄剧ず浣嶇疆
//	var tempWidth = this.messageDiv.offsetWidth;
	var tempWidth = this.Div_gen.offsetWidth;
	if (tempWidth <= 0)
		tempWidth = getInteger(parseInt(this.width), 120);
		
	/*閮ㄥ垎鎯呭喌涓媡op璁＄畻鏃秚op.document.body.clientHeight - tempHeight涓?,鍥犳瀵箃empHeight杩涜淇敼*/
	/*var tempHeight = 0;
	if (isPercent(this.width))
		tempHeight = this.Div_gen.offsetHeight;  
	else
		tempHeight = getInteger(parseInt(this.height), 120);*/
//	var tempHeight = this.messageDiv.offsetHeight;
	var tempHeight = this.Div_gen.offsetHeight;
	if(tempHeight <= 0)
		tempHeight = getInteger(parseInt(this.height), 120);
		
	
	if (this.showPosition == "top-left") {
		left = MessageComp.DEFAULT_X + (this.x == null ? 0 : getInteger(this.x));
		top1 = MessageComp.DEFAULT_Y + (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "top-center") {
		left = (top.document.body.clientWidth - tempWidth - (this.x == null ? 0 : getInteger(this.x)))/2;
		top1 = MessageComp.DEFAULT_Y + (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "top-right") {
		left = top.document.body.clientWidth - tempWidth - MessageComp.DEFAULT_X - (this.x == null ? 0 : getInteger(this.x));
		top1 = MessageComp.DEFAULT_Y + (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "center") {
		left = (top.document.body.clientWidth - tempWidth - (this.x == null ? 0 : getInteger(this.x)))/2;
		top1 = (top.document.body.clientHeight - tempHeight - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y)))/2;
	} else if (this.showPosition == "bottom-left") {
		left = MessageComp.DEFAULT_X + (this.x == null ? 0 : getInteger(this.x));
		top1 = top.document.body.clientHeight - tempHeight - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "bottom-center") {
		left = (top.document.body.clientWidth - tempWidth - (this.x == null ? 0 : getInteger(this.x)))/2;
		top1 = top.document.body.clientHeight - tempHeight - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y));
	} else { // bottom-right
		left = top.document.body.clientWidth - tempWidth - MessageComp.DEFAULT_X - (this.x == null ? 0 : getInteger(this.x));
		top1 = top.document.body.clientHeight - tempHeight - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y));
	}
	this.Div_gen.style.left = left + "px";
	this.Div_gen.style.top = top1 + "px";
	/*this.Div_gen.style.position = "absolute";
	this.Div_gen.style.border = "1px solid";*/
	
	
	
};
/**
 * 璁剧疆鎻愮ず淇℃伅瀹藉害鍜岄珮搴?
 * @public
 */
MessageComp.prototype.setTextStyle = function() {
	// 鎻愮ず妗嗙殑鏁翠綋瀹藉害
	var wholeWidth = 0;
	// 濡傛灉璁剧疆浜嗗搴﹀苟涓斾笉涓虹櫨鍒嗘瘮,鏁翠綋瀹藉害涓鸿缃搴?
	if(!isNull(this.width) && this.preWidth == false)
		wholeWidth = this.width;
	// 濡傛灉璁剧疆浜嗗搴﹀苟涓斾负鐧惧垎姣?鏁翠綋瀹藉害涓簅ffsetWidth
	if(!isNull(this.width) && this.preWidth == true)
		wholeWidth = this.Div_gen.offsetWidth;
	// 濡傛灉娌℃湁璁剧疆瀹藉害
	if(isNull(this.width)){
		wholeWidth = this.realTextDiv.offsetWidth + 35; //宸﹀彸鐣欑櫧浠ュ強鍙夊彿鍥炬爣
		if(this.hasImg)
			wholeWidth = wholeWidth + this.IMG_LEFT + this.IMG_WIDTH + 5;
	}
	if(wholeWidth < this.minWidth)
		wholeWidth = this.minWidth;
	if(wholeWidth > document.body.clientWidth)
		wholeWidth = document.body.clientWidth;
	if(isNull(this.width)){
		this.Div_gen.style.width = wholeWidth + "px";
		this.messageDiv.style.width = wholeWidth + "px";
	}
	// 璁剧疆鎻愮ず淇℃伅瀹藉害
	var textWidth = wholeWidth - 35;// 鍑忓幓宸﹀彸鐣欑櫧浠ュ強鍙夊彿鍥炬爣
	this.textWholeDiv.style.left = "10px";
	if(this.hasImg){
		textWidth = textWidth - this.IMG_LEFT - this.IMG_WIDTH - 5;
		this.textWholeDiv.style.left = this.IMG_LEFT + this.IMG_WIDTH + 5 + "px";
	}
	this.textWholeDiv.style.width = textWidth + "px";
	this.textDiv.style.width = textWidth + "px";
	//IE8下边框显示有问题需要对iframe宽度和高度进行修改
	if(IS_IE8){
		this.Div_gen.children[0].style.width = wholeWidth - 8 + "px";
	}
	
	// 鎻愮ず妗嗙殑鏁翠綋楂樺害
	var wholeHeight = 0;
	// 濡傛灉璁剧疆浜嗛珮搴﹀苟涓斾笉涓虹櫨鍒嗘瘮,鏁翠綋瀹藉害涓鸿缃搴?
	if(!isNull(this.height) && this.preHeight == false)
		wholeHeight = this.height;
	// 濡傛灉璁剧疆浜嗛珮搴﹀苟涓斾负鐧惧垎姣?鏁翠綋瀹藉害涓簅ffsetWidth
	if(!isNull(this.height) && this.preHeight == true)
		wholeHeight = this.Div_gen.offsetHeight;
	// 濡傛灉娌℃湁璁剧疆楂樺害
	if(isNull(this.height)){
		wholeHeight = this.realTextDiv.offsetHeight + 22; //涓婁笅鐣欑櫧
		if(this.isOpacity)
			wholeHeight = wholeHeight + 30;
	}
	// 楂樺害涓嶈兘灏忎簬鏈?皬楂樺害涓嶈兘澶т簬灞忓箷楂樺害
	if(wholeHeight < this.minHeight)
		wholeHeight = this.minHeight;
	if(wholeHeight > document.body.clientHeight)
		wholeHeight = document.body.clientHeight;
	if(isNull(this.height)){
		this.Div_gen.style.height = wholeHeight + "px";
		this.messageDiv.style.height = wholeHeight + "px";
	}
	
	var textHeight = wholeHeight - 22;// 鍑忓幓涓婁笅鐣欑櫧
	if(this.isOpacity){
		textHeight = textHeight - 30;
	}
	var realTextDivHeight = this.realTextDiv.offsetHeight;
	if(this.textDiv.scrollWidth > this.textDiv.offsetWidth){//鍑虹幇浜嗘粴鍔ㄦ潯闇?鑰冭檻婊氬姩鏉￠珮搴?
		if(textHeight > (realTextDivHeight + 16)){
			textHeight = realTextDivHeight + 16;//涓轰簡灞呬腑骞朵笖鑰冭檻鍑虹幇婊氬姩鏉＄殑鎯呭喌
		}
	}else{
		if(textHeight > (realTextDivHeight)){
			textHeight = realTextDivHeight;//涓轰簡灞呬腑
		}
	}
	
	this.textWholeDiv.style.height = textHeight + "px";
	this.textDiv.style.height = textHeight + "px";
	//IE8下边框显示有问题需要对iframe宽度和高度进行修改
	if(IS_IE8){
		this.Div_gen.children[0].style.height = wholeHeight - 8 + "px";
	} 
};

/**
 * 璁剧疆鏄剧ず鏂囧瓧
 * @public
 */
MessageComp.prototype.setText = function(text) {
	this.text = text;
	if (text != null){
		this.realTextDiv.innerHTML = this.text;
	}
};

/**
 * 灏嗗搴﹀拰楂樺害鎭㈠鍘熷?鐢ㄤ簬閲嶆柊璁＄畻
 * @public
 */
MessageComp.prototype.resetWidthHeight = function() {
	this.Div_gen.style.left = "0px";
	this.Div_gen.style.top = "0px";
	if(!this.nullWidth){
		if(this.preWidth)
			this.Div_gen.style.width = this.width;
		else
			this.Div_gen.style.width = this.width + "px";
	}else{
		this.Div_gen.style.width = "100%";
	}
	
	if(!this.nullWidth){
		if(this.preWidth)
			this.messageDiv.style.width = this.width;
		else
			this.messageDiv.style.width = this.width + "px";
	}else{
		this.messageDiv.style.width = "100%";
	}
	if(!this.nullHeight){
		if(this.preHeight)
			this.Div_gen.style.height = this.height;
		else
			this.Div_gen.style.height = this.height + "px";
	}else{
		this.Div_gen.style.height = "100%";
	}
	
	if(!this.nullHeight){
		if(this.preHeight)
			this.messageDiv.style.height = this.height;
		else
			this.messageDiv.style.height = this.height + "px";
	}else{
		this.messageDiv.style.height = "100%";
	}
	this.textDiv.style.width = "";
	this.textWholeDiv.style.width = "";
	
};


/**
 * 澧炲姞鏂扮殑鎻愮ず淇℃伅
 * @public
 */
MessageComp.addNewMessage = function(messageComp) {
	if(messageComp){
		if(!window.$_messageCompArr)
			window.$_messageCompArr = new Array();
		if(!window.$_messageCompHeightArr)
			window.$_messageCompHeightArr = new Array();
		window.$_messageCompArr.push(messageComp);
		var messageCompHeight = messageComp.getObjHtml().offsetHeight + (messageComp.y == null ? 0 : getInteger(messageComp.y));
		window.$_messageCompHeightArr.push(messageCompHeight);
		MessageComp.messageCompResize(messageComp.showPosition);
	}
};

/**
 * 鏍规嵁灞忓箷楂樺害杩涜璋冩暣
 * @public
 */
MessageComp.messageCompResize = function(showPosition){
	if(showPosition){
		MessageComp.messageCompResizeFun(showPosition);
	}else{
		// 濡傛灉娌℃湁浼犲叆showPosition 鍒欏惊鐜墍鏈夌殑showPosition杩涜澶勭悊
		for(var i = 0; i < MessageComp.showPositionArr.length; i++){
			MessageComp.messageCompResizeFun(MessageComp.showPositionArr[i])
		}
	}
};

/**
 * 鏍规嵁灞忓箷楂樺害杩涜璋冩暣鐨勫叿浣撳疄鐜?
 * @public
 */
MessageComp.messageCompResizeFun = function(showPosition){
	if(!window.$_messageCompHeightArr || !window.$_messageCompHeightArr.length || window.$_messageCompHeightArr.length <= 0)
		return;
	// 灞忓箷楂樺害
	var screenHeight = document.body.clientHeight;
	// 鍓╀綑楂樺害
	var screenH = screenHeight;
	// top鍊艰绠?
	var totalTop = 0;
	var ArrLength = window.$_messageCompHeightArr.length;
	if(showPosition == "center"){
		screenH = screenH / 2;
	}
	window.$_messageCompShowArr = new Array();
	window.$_messageCompShowHeightArr = new Array();
	for(var i = ArrLength - 1; i >= 0; i--){
		var messageComp = window.$_messageCompArr[i];
		if(messageComp.showPosition == showPosition){
			screenH -= window.$_messageCompHeightArr[i];
			if(screenH >= 0){ // 鍒氬ソ瓒冲鏄剧ず鍒扮i涓?
				window.$_messageCompArr[i].showMsg();
				window.$_messageCompArr[i].Div_gen.style.display = "block";
				window.$_messageCompArr[i].setStyle();
				window.$_messageCompShowArr.unshift(window.$_messageCompArr[i]);
				window.$_messageCompShowHeightArr.unshift(window.$_messageCompHeightArr[i]);
			}
			if(screenH < 0){
				window.$_messageCompArr[i].hideMsg();
			}
		}
	}
	if(showPosition == "top-left" || showPosition == "top-center" || showPosition == "top-right"){
		totalTop = 0 + MessageComp.DEFAULT_Y;
		for(var j = 0; j < window.$_messageCompShowArr.length; j++){
			window.$_messageCompShowArr[j].Div_gen.style.top = totalTop + "px";
			totalTop +=  window.$_messageCompShowHeightArr[j];
		}
	}
	if(showPosition == "bottom-left" || showPosition == "bottom-center" || showPosition == "bottom-right"){
		totalTop = screenHeight - MessageComp.DEFAULT_Y;
		for(var j = 0; j < window.$_messageCompShowArr.length; j++){
			totalTop -= window.$_messageCompShowHeightArr[j];
			window.$_messageCompShowArr[j].Div_gen.style.top = totalTop + "px";
		}
	}
	if(showPosition == "center"){
		totalTop = (screenHeight  - MessageComp.DEFAULT_Y) / 2;
		for(var j = 0; j < window.$_messageCompShowArr.length; j++){
			if(j == 0)
				totalTop -= window.$_messageCompShowHeightArr[j]/2;
			else
				totalTop -= window.$_messageCompShowHeightArr[j];
			window.$_messageCompShowArr[j].Div_gen.style.top = totalTop + "px";
		}
	}
};

/**
 * 鏄剧ず鎻愮ず淇℃伅锛堝厛鏄剧ず鍚庨殣钘忥級
 * @private
 */
MessageComp.prototype.showMsg = function() {
	var oThis = this;
	// IE8涓嬩繚瀛樻垚鍔熸彁绀烘椂鏄剧ず涓や釜妗嗭紝鐜鎱㈢殑鏃跺?鐪嬬殑姣旇緝娓呮锛屽洜姝ゅ姞寤惰繜
	if (this.Div_gen.style.display == "none") {
		setTimeout(function (){
			oThis.Div_gen.style.display = "block"; 
			if(typeof MessageCompAfterShow === "function"){
				MessageCompAfterShow(oThis.id);
			}
		},90);
	}
	this.Div_gen.style.zIndex = getZIndex();
	if(this.isOpacity){
		this.createBottomDiv();
	  //陶冶修改
		//this.step = 3;
		//this.speed = 150;
		this.bottomTime.innerHTML = "3";
		this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [5]),"秒钟后自动消失");
	  //陶冶修改
		//this.changeOpacity(100, 50, this.step, this.speed);
		this.changeOpacity(100, 5);
	}
};

/**
 * 鐢╯etTimeout寰幆鍑忓皯閫忔槑搴?
 * @private
 */
MessageComp.prototype.changeOpacity = function(opacityValue, displayTime) {
	var oThis = this;
//	if (this.Div_gen.style.display == "none") {
//		this.Div_gen.style.display = "block";
//	}
	if (displayTime == 0) {  // 闅愯棌鎺т欢
		this.bottomTime.innerHTML = "0";
		this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [0]),"秒钟后自动消失");
		this.Div_gen.style.display = "none";
		if(this.isNew){
			// 鍒犻櫎鏁扮粍涓搴旀帶浠?
			for(var i = 0; i < window.$_messageCompArr.length; i++){
				if(window.$_messageCompArr[i] == this){
					window.$_messageCompArr.splice(i, 1);
					window.$_messageCompHeightArr.splice(i, 1);
				}
			}
			if(window.$_messageComp == this){
				window.$_messageComp = null;
			}
			this.destroySelf();
		}
		return;
	}
	this.bottomTime.innerHTML = displayTime;
	this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [displayTime]),"秒钟后自动消失");
	setOpacity(this.bgLeftTopDiv, opacityValue);
	setOpacity(this.bgCenterTopDiv, opacityValue);
	setOpacity(this.bgRightTopDiv, opacityValue);
	setOpacity(this.bgLeftCenterDiv, opacityValue);
	setOpacity(this.bgCenterDiv, opacityValue);
	setOpacity(this.bgRightCenterDiv, opacityValue);
	setOpacity(this.bgLeftBottomDiv, opacityValue);
	setOpacity(this.bgCenterBottomDiv, opacityValue);
	setOpacity(this.bgRightBottomDiv, opacityValue);
	setOpacity(this.textWholeDiv, opacityValue);
	setOpacity(this.textDiv, opacityValue);
	setOpacity(this.messageDiv, opacityValue);
//	if (this.Div_gen.style.display == "none") {
//		this.Div_gen.style.display = "block";
	
	if (this.timeoutFunc)
		clearTimeout(this.timeoutFunc);
  this.timeoutFunc = setTimeout( function() {
		oThis.changeOpacity(opacityValue, displayTime - 1);
	}, 1000);
};
//MessageComp.prototype.changeOpacity = function(startValue, endValue, step, speed) {
//	var oThis = this;
//	if (this.Div_gen.style.display == "none") {
//		this.Div_gen.style.display = "block";
//	}
//	if (startValue <= 0) {  // 闅愯棌鎺т欢
//		this.bottomTime.innerHTML = "0";
//		this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [0]),"秒钟后自动消失");
//		this.Div_gen.style.display = "none";
//		if(this.isNew){
			// 鍒犻櫎鏁扮粍涓搴旀帶浠?
//			for(var i = 0; i < window.$_messageCompArr.length; i++){
//				if(window.$_messageCompArr[i] == this){
//					window.$_messageCompArr.splice(i, 1);
//					window.$_messageCompHeightArr.splice(i, 1);
//				}
//			}
//			if(window.$_messageComp == this){
//				window.$_messageComp = null;
//			}
//			this.destroySelf();
//		}
//		return;
//	}else if(startValue <= 40){
//		this.bottomTime.innerHTML = "1";
//		this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [1]),"秒钟后自动消失");
//	}else if(startValue <= 70){
//		this.bottomTime.innerHTML = "2";
//		this.bottomText.innerHTML = getString(trans("ml_messagecomp_tip1", [2]),"秒钟后自动消失");
//	}
//	setOpacity(this.bgLeftTopDiv, startValue);
//	setOpacity(this.bgCenterTopDiv, startValue);
//	setOpacity(this.bgRightTopDiv, startValue);
//	setOpacity(this.bgLeftCenterDiv, startValue);
//	setOpacity(this.bgCenterDiv, startValue);
//	setOpacity(this.bgRightCenterDiv, startValue);
//	setOpacity(this.bgLeftBottomDiv, startValue);
//	setOpacity(this.bgCenterBottomDiv, startValue);
//	setOpacity(this.bgRightBottomDiv, startValue);
//	setOpacity(this.textWholeDiv, startValue);
//	setOpacity(this.textDiv, startValue);
//	setOpacity(this.messageDiv, startValue);
//	if (this.Div_gen.style.display == "none") {
//		this.Div_gen.style.display = "block";
//	
//	if (this.timeoutFunc)
//		clearTimeout(this.timeoutFunc);
//	this.timeoutFunc = setTimeout( function() {
//		oThis.changeOpacity(startValue - step, endValue, step, speed);
//	}, speed);
//};

/**
 * 鐩存帴闅愯棌鎻愮ず淇℃伅
 * @private
 */
MessageComp.prototype.hideMsg = function() {
	if (this.timeoutFunc)
		clearTimeout(this.timeoutFunc);
	this.Div_gen.style.display = "none";
};

/**
 * 璁剧疆閫忔槑搴?
 * @private
 */
function setOpacity(obj, value) {
		if (document.all) {
			if (value == 100) {
				obj.style.filter = "";
			} else {
				if(IS_IE10_ABOVE){
					obj.style.opacity = value / 100;
				}else {
					obj.style.filter= "progrid:DXImageTransform.microsoft.Alpah(opacity=" + value + ")";
				}
				/*//IE7缁檖ng璁剧疆婊ら暅鏈塨ug(IE7涓嬫棤娓愬彉鏁堟灉)
				if(!IS_IE7)
					obj.style.filter = "alpha(opacity=" + value + ")";*/
			}
		} else {
			obj.style.opacity = value / 100;
		}
};
/**
 * 璁剧疆瀹藉害楂樺害鐘舵?
 * @private
 */
MessageComp.setWidthHeightFlag = function(obj) {
// 澶勭悊瀹藉害鍜岄珮搴︽槸鍚︿负绌?
	if(isNull(obj.width)){
		obj.nullWidth = true;
	}else{
		obj.nullWidth = false;
	}
	if(isNull(obj.height)){
		obj.nullHeight = true;
	}else{
		obj.nullHeight = false;
	}
	// 澶勭悊瀹藉害鍜岄珮搴︽槸鍚︿负鐧惧垎姣?
	if(obj.width.indexOf("%") != -1){
		obj.preWidth = true;
	}else{
		obj.preWidth = false;
	}
	if(obj.height.indexOf("%") != -1){
		obj.preHeight = true;
	}else{
		obj.preHeight = false;
	}
};

/** 
 * 销毁控件（子类中如果必要须重写该方法）
 */
MessageComp.prototype.destroySelf = function() {
	this.spliceArr();
	if(window.$_messageComp){
		if(window.$_messageComp == this){
			window.$_messageComp = null;
		}
	}
	BaseComponent.prototype.destroySelf.call(this);
};

/** 
 * 删除控件时对数组进行处理
 */
MessageComp.prototype.spliceArr = function() {
	if(window.$_messageCompArr){
		if(window.$_messageCompArr.length){
			for(var i = 0; i < window.$_messageCompArr.length; i++){
				if(window.$_messageCompArr[i] == this){
					window.$_messageCompArr.splice(i, 1);
					window.$_messageCompHeightArr.splice(i, 1);
				}
			}
		}
	}
};