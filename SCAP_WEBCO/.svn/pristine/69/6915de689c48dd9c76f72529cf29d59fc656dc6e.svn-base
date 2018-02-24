/**
 * @fileoverview
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b> 4.修正功能缺陷 . guoweic <b>modified</b>
 */
// 表头的默认单行高度
GridComp.HEADERROW_HEIGHT = 31;
// grid数据行的默认高度
GridComp.ROW_HEIGHT = 24;
// 固定选择列的宽度值(isMultiSelWithBox为true)
GridComp.SELECTCOLUM_WIDTH = 30;
// 单元格底边高度
GridComp.CELL_BOTTOM_BORDER_WIDTH = 1;
// grid分页条的高度
GridComp.PAGEBAR_HEIGHT = 32;
// 列的左边界宽
GridComp.COLUMN_LEFT_BORDER_WIDTH = 0;
// 合计单元格的padding
GridComp.SUMCELL_PADDING = 10;
// 行状态列宽
GridComp.ROWSTATE_COLUMN_WIDTH = 13;
// 多选列宽
GridComp.MULTISEL_COLUMN_WIDTH = 30;
// "合计" 所在Div所占列宽
GridComp.SUMROW_DIV_WIDTH = 34;
GridComp.COlUMWIDTH_DEFAULT = 70;
GridComp.SCROLLBAE_HEIGHT = 17;
//cell单元格左padding
GridComp.CELL_LEFT_PADDING = 10;
GridComp.CELL_RIGHT_PADDING = 10;
// "无数据"提示DIV所占列高
GridComp.NOROW_DIV_HEIGHT = 34;
//自动扩展表头的每一份的最小宽度
GridComp.EXPANDHEADER_MINWIDTH = 100;


//复选策略
//只设置自身
GridComp.CHECKBOXMODEL_SELF = 0;
//设置自己和子
GridComp.CHECKBOXMODEL_SELF_SUB = 1;
//设置自己和子和父
GridComp.CHECKBOXMODEL_SELF_SUB_PARENT = 2;


GridComp.prototype = new BaseComponent;
GridComp.prototype.componentType = "GRIDCOMP";
/**
 * Grid控件构造函数
 * 
 * @class 最大限度的利用html + css实现的轻量级表格控件类<br>
 *        <b>Grid特性：</b><br>
 *        1、懒加载模式装载数据,一次装载一屏<br>
 *        2、采用MVC模式，model层和view层分离<br>
 *        3、支持锁定列，可以动态指定固定在最左侧的列<br>
 *        4、支持无限分级的多表头，表头可以动态拖动<br>
 *        5、功能强大的渲染器，grid内置了常用的渲染器，可将真实值渲染为个性化外观的显示值<br>
 *        6、支持多种编辑类型，可以利用控件库的多种编辑控件编辑cell的值<br>
 *        7、支持合计行，浮动在表格最下方，可以自动获取指定合计列的值<br>
 *        8、支持前后台分页，分页时显示分页操作栏<br>
 *        <b>组件皮肤设置说明：</b><br>
 *        grid.css文件用来控制此控件的外观<br>
 *        <br>
 *        <b>Grid控件使用方法介绍：</b><br>
 *        <b>1、使用lfw框架的用法</b><br>
 *        (1)前台jsp页面中写明 &lt;lfw:grid
 *        id="">&lt;/lfw:grid>，id为引用的后台配置文件中配置的此Grid的id。<br>
 *        (2)在后台配置文件中配置Grid的各个属性信息<br>
 *        <b>Grid属性说明：</b><br>
 *        id------------Grid控件的id，jsp文件的控件id必须和这个id相同才能引用到此控件<br>
 *        dataset-------Grid控件所绑定的dataset的id<br>
 *        editable------此Grid控件整体是否可以编辑<br>
 *        width---------Grid控件的宽度，单位可以是"px"或者"%"两种<br>
 *        height--------Grid控件的高度，单位可以是"px"或者"%"两种<br>
 *        multiSelect---是否可以选择多行，多行选择则显示第一列的固定选择列，否则不显示第一行的固定选择列<br>
 *        <b>Grid列的属性说明：</b><br>
 *        id--------------Grid列控件的id<br>
 *        field-----------所引用的dataset中的字段的id<br>
 *        columEditable---此列是否可以编辑(true|false)。要使columEditable设置为false有效，Grid的editable属性必须设置为true<br>
 *        width-----------此列的宽度<br>
 *        dataType--------此列的数据类型<br>
 *        editorType------请参阅JsExtensions.js中的数据类型定义<br>
 *        renderType------此列的渲染器类型<br>
 *        isHidden--------是否引藏此列<br>
 *        columBgColor----列的背景色<br>
 *        textAlign-------文字对齐方式<br>
 *        textColor-------文字颜色<br>
 *        isFixedHeader---是否是固定列<br>
 *        refComboData----如果editorType是CheckBox类型，refCombodata表示所引用的ComboData的id<br>
 *        refNode---------如果editorType是Reference类型，此属性表示引用的参照节点的id<br>
 *        <br>
 * 
 * <b>&lt;Grid id="" dataset="" editable="" width="" height=""
 * multiSelect=""&gt;<br>
 * &lt;Column id="" field="" columEditable="" width="" dataType="" editorType=""
 * renderType="" isHidden="" columBgColor="" textAlign="" textColor=""
 * isFixedHeader="" refComboData="" refNode=""/><br>
 * ---<br>
 * &lt;Events> &lt;Event name=""> &lt;/Event> &lt;/Events><br>
 * &lt;/Grid></b><br>
 * <br>
 * <b>2、使用js，直接通过new的用法生成grid实例</b><br>
 * var grid = new GridComp(parent, name, left, top, width, height, position,
 * editable, isMultiSelect);<br>
 * var model = new GridCompModel();<br>
 * <b>// 单表头的构建方法</b><br>
 * var header1 = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>null</b>, <b>null</b>,
 * <b>false</b>);<br>
 * model.addHeader(header1);<br>
 * <b>// 多表头的构建方法</b><br>
 * var multiHeader = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>null</b>, <b>null</b>,
 * <b>true</b>);<br>
 * model.addHeader(multiHeader);<br>
 * var subHeader1 = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>multiHeader</b>,
 * <b>multiHeader</b>, <b>true</b>);<br>
 * var subHeader2 = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>multiHeader</b>,
 * <b>multiHeader</b>, <b>true</b>);<br>
 * model.setDataSet(dataset);<br>
 * grid.setModel(model);<br>
 * grid.create();<br>
 * <br>
 * <b>constants常量池中存放的常量：(当grid隐藏时可以从此中取得需要的grid的常量)</b><br>
 * <b>3、继续开发grid和增加方法时需要注意的问题</b><br>
 * (1)如果header不是basicHeader且是顶层header，那么header.basicHeaders属性存储着basicHeaders。
 * 
 * @constructor Grid构造函数
 * @author gd, guoweic
 * @version NC6.0
 * @param parent
 *            父控件对象
 * @param name
 *            控件名称
 * @param left
 *            控件左部x坐标
 * @param top
 *            控件顶部y坐标
 * @param width
 *            控件宽度
 * @param height
 *            控件高度
 * @param position
 *            定位属性
 * @param editable
 *            整体是否可以编辑
 * @param isMultiSelWithBox
 *            多行选择模式选择；1、显示第一列的固定选择列的多行选择模式 2、支持ctrl和shift形式的多选模式
 * @param isShowNumCol
 *            是否显示数字列
 * @param isShowSumRow
 *            是否显示合计行
 * @param paginationObj
 *            分页对象，存储了分页所需的信息，包括pageSize,client(true:前台分页,false:后台分页)
 * @param className
 *            css类名
 * @param isPagenationTop
 *            翻页工具条是否在顶端
 * @param oddType
 *            判断单双行类型，“0”为单双行各一行交错排列；“1”为单行1行，双行2行交错排列
 * @param isGroupWithCheckbox
 *            分组显示时，多选框是否分组
 * @param isShowHeader
 *            是否显示表头
 */
function GridComp(parent, name, left, top, width, height, position, editable,
		isMultiSelWithBox, isShowNumCol, isShowSumRow, attr,
		groupHeaderIds, sortable, className, isPagenationTop, showColInfo,
		oddType, isGroupWithCheckbox, isShowHeader, extendCellEditor, rowRender, currentLanguageCode) {
	
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parent = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "grid_div");
	this.rowRender = rowRender == null ? DefaultRowRender : rowRender;
	this.currentLanguageCode = currentLanguageCode;	
	this.extendCellEditor = extendCellEditor == null ? CellEditor : extendCellEditor;
	if (IS_IE7 || IS_IE9)
		GridComp.ROWSTATE_COLUMN_WIDTH = 14;
	// 初始化静态常量
	this.initStaticConstant();
	// 标示此grid是否可以编辑
	this.editable = getBoolean(editable, true);
	// 当前选中行的索引(数组)
	this.selectedRowIndice = null;
	// 表头数组(最上层header的集合,多表头情况指最上层的header)
	//this.headers = null;
	// 表头基础数组(最下层header的集合,多表头情况指最下层包括真正有用信息的header),此数组是根据headers内部生成的
	this.basicHeaders = new Array();
	// grid行高度
	this.rowHeight = GridComp.ROW_HEIGHT;
	this.headerRowHeight = GridComp.HEADERROW_HEIGHT;
	// 数据区真正的宽度
	this.realWidth = 0;
	// 编辑控件.针对每种类型控件初始化一个控件,然后将控件动态展示到编辑单元格上,这样做主要是展现效率问题.
	this.compsMap = null;
	// 保存当前激活的cell
	this.currActivedCell = null;
	// 保存当前选中的cell
	this.selectedCell = null;
	// 调用addOneRow()第一次出现竖直滚动条的标志
	this.firstVScroll = false;
	// 保存所有的静态表头headers
	this.defaultFixedHeaders = null;
	// 保存所有的动态表头headers
	this.defaultDynamicHeaders = null;
	// 当前显示编辑控件
	this.showComp = null;
	// 标示控件是否被初始化过
	this.compsInited = false;
	// 是否多行选择(多行选择则显示第一列的固定选择列,否则不显示第一行的固定选择列)
	this.isMultiSelWithBox = getBoolean(isMultiSelWithBox, false);
	// 是否显示数字行号
	this.isShowNumCol = getBoolean(isShowNumCol, false);
	// 是否显示合计行
	this.isShowSumRow = getBoolean(isShowSumRow, false);
	// 分组的列
	this.groupHeaderIds = getString(groupHeaderIds, "");
	// this.groupHeaderIds = "pk_branch_showName,degree,sex";
	if (this.groupHeaderIds != "")
		this.groupHeaderIds = this.groupHeaderIds.split(",");
	// 整体是否可以排序
	this.sortable = getBoolean(sortable, true);
	this.pageSize = -1;
	this.flowmode = false;
	this.autoRowHeight = false;
	//行高自适应时 记录每行最小高度
	this.rowMinHeight = new Array();
	this.defaultRowMinHeight = new Array();
	// 是否运行态
	this.isRunMode = false;
	// 是否简单分页
	this.isSimplePagination = false;
	// 是否显示功能按钮
	this.isShowImageBtn = false;
	// 自定义功能按钮
	this.selfDefImageBtnRender = null;
	//粘贴事件
	this.onPaste = null;
	// 描述信息
	this.descArray = null;
	//设置默认复选策略
	this.checkBoxModel = GridComp.CHECKBOXMODEL_SELF; 
	this.showTree = true; 
	
	this.canCopy = true;
	this.autoExpand = 0; // 0为true，1为false
	this.hasBorder = false;
	// 解析grid属性对象
	if (attr != null) {
		this.pageSize = getInteger(attr.pageSize, this.pageSize);
		this.flowmode = attr.flowmode;
		this.isRunMode = getBoolean(attr.isRunMode, this.isRunMode);
		this.isSimplePagination = getBoolean(attr.isSimplePagination, this.isSimplePagination);
		this.isShowImageBtn = getBoolean(attr.isShowImageBtn, this.isShowImageBtn);
		this.autoRowHeight = getBoolean(attr.autoRowHeight, this.autoRowHeight);
		this.canCopy = getBoolean(attr.canCopy, this.canCopy);
		this.selfDefImageBtnRender = attr.selfDefImageBtnRender;
		this.onPaste = attr.onPaste; 
		this.showForm = getBoolean(attr.showForm, false);
		this.showTree = getBoolean(attr.showTree, true);
		this.isMultiSelectShow = attr.isMultiSelectShow;
		if(attr.descArray instanceof Array){
			this.descArray = attr.descArray;
		}else if(typeof(attr.descArray) != 'undefined'){
			this.descArray = new Array();
			this.descArray.push(attr.descArray);
		}
		this.defaultLangCode = getInteger(attr.defaultLangCode , this.currentLanguageCode);
		this.autoExpand = getInteger(attr.autoExpand , 0);
		this.hasBorder = getBoolean(attr.hasBorder, this.hasBorder);
	}
	// 翻页条是否在上面
	this.isPagenationTop = getBoolean(isPagenationTop, true);

	// 是否可显示“显示列”和“锁定列”菜单
	this.showColInfo = getBoolean(showColInfo, true);

	// 判断单双行类型，“0”为单双行各一行交错排列；“1”为单行2行，双行1行交错排列
	this.oddType = getString(oddType, "0");
	this.oddType = "0";

	// 分组显示时，多选框是否分组
	this.isGroupWithCheckbox = getBoolean(isGroupWithCheckbox, true);
	// 是否显示表头，默认为显示
	this.isShowHeader = getBoolean(isShowHeader, true);
	
	// 存储页面显示焦点行索引
	this.focusIndex = -1;

	// 注册外部回掉函数
	window.clickHolders.push(this);
	this.outerDivId = "data_outer_scroll"+this.getId();
	this.keepScroll = false;
	this.treeLevel = null;
};

/**
 * 创建gird控件的整体外层div
 * 
 * @private
 */
GridComp.prototype.create = function() {
	// 创建最外层包容div
	if (this.wholeDiv == null){
		this.initWholeDiv();
		// 描述信息
		this.initDescArrayDiv();
		// 功能按钮
		this.initImageBtn();
		// 用户自定义功能按钮
		if(this.isRunMode && typeof(this.selfDefImageBtnRender) == 'function'){
			this.selfDefImageBtnRender.call(this, this);
		}
		
		this.initOuterDiv();
		//初始化错误提示对象
		this.initWholeErrorMsgDiv();
	}
	this.setGridDescContent();
	if (this.parent)
		this.placeIn(this.parent);
};

/**
 * 销毁控件
 * 
 * @private
 */
GridComp.prototype.destroySelf = function() {
//	this.headers = null;
	
	this.basicHeaders = null;
	
	// 销毁模型
	if (this.model) {
		this.model.destroySelf();
		this.model = null;
	}
	
	// 销毁编辑控件
	if (this.compsMap) {
		var comps = this.compsMap.values();
		for (var i = 0; i < comps.length; i++) {
			var comp = comps[i];
			comp.destroySelf();
		}
		this.compsMap.clear();
		this.compsMap = null;
	}
	BaseComponent.prototype.destroySelf.call(this);
};

/**
 * 返回grid的显示对象
 */
GridComp.prototype.getObjHtml = function() {
	return this.wholeDiv;
};

/**
 * 创建Grid框架各个组成部分
 * 
 * @private
 */
GridComp.prototype.manageSelf = function() {
	var oThis = this;
	// 在此之前必须完成model的初始化工作,否则下面的框架初始化没法完成
	if (this.model == null)
		return;
	if(this.showForm){
		this.paintFormData();
	}else{
		this.paintData();
	}
	// 如果控件可编辑,则创建编辑控件
	if (this.editable) {
		// 缓初始化编辑控件
		setTimeout("GridComp.initEditCompsForGrid('" + this.id + "')", 100);
	}

	// 重新设置自动表头的宽度
	if(!this.showForm){
		setTimeout("GridComp.processAutoExpandHeadersWidth('" + this.id + "','"+this.outerDivId+"')", 350);
	}
};

/**
 * 判断是否为双数行
 * 
 * @private
 */
GridComp.prototype.isOdd = function(index) {
	if (index == null) return false;
	if (this.oddType == "0") {
		return index % 2 == 1;
	} else if (this.oddType == "1") {
		return index % 3 != 0;
	}
};

GridComp.prototype.initWholeDiv = function(){
	if(!this.wholeDiv){
		this.wholeDiv = $ce("div");
	}
	this.wholeDiv.id = "gridWholeDiv";

	this.wholeDiv.className = "whole_grid_div";
	
	// chrome下不会触发GRID_END在整体上添加监听
	if(IS_CHROME){
		this.wholeDiv.onmouseup = function(){destroyDargObj();};
	}
};

GridComp.prototype.initDescArrayDiv = function(){
	if(!this.descDiv){
		this.descDiv = $ce("DIV");
		this.descDiv.className = "desc_div";
		
		this.descBKLeftDiv = $ce("DIV");
		this.descBKLeftDiv.className = "desc_bk_left_div";
		this.descDiv.appendChild(this.descBKLeftDiv);
		this.descBKMiddleDiv = $ce("DIV");
		this.descBKMiddleDiv.className = "desc_bk_middle_div";
		this.descDiv.appendChild(this.descBKMiddleDiv);
		this.descBKRightDiv = $ce("DIV");
		this.descBKRightDiv.className = "desc_bk_right_div";
		this.descDiv.appendChild(this.descBKRightDiv);

		this.wholeDiv.appendChild(this.descDiv);
	}
};

GridComp.prototype.setGridDescContent = function(descMsg){
	this.descBKMiddleDiv.innerHTML = '';
	if(descMsg instanceof Array){
		this.descArray = descMsg;
	}else if(typeof(descMsg) != 'undefined'){
		this.descArray = new Array();
		this.descArray.push(descMsg);
	}
	this.descBKMiddleDiv.title = "";
	if(this.descArray && this.descArray.length > 0){
		for(var i=0; i<this.descArray.length; i++){
			var desc = $ce("FONT");
			desc.className = 'desc_msg';
			desc.innerHTML = this.descArray[i];
			if(i == 0){
				desc.style.paddingLeft = '5px';
			}
			if(i == this.descArray.length - 1){
				desc.style.borderRight = 'none';
			}
			this.descBKMiddleDiv.appendChild(desc);
//			if (i == (this.descArray.length - 1))
//				this.descBKMiddleDiv.title = this.descBKMiddleDiv.title + this.descArray[i];
//			else	
//				this.descBKMiddleDiv.title = this.descBKMiddleDiv.title + this.descArray[i] + " | ";
		}
		this.descDiv.style.display = 'block';
		GridComp.gridResize(this.id);
	}
};

GridComp.prototype.initImageBtn = function(){	
	if(typeof(this.headerBtnDiv) == 'undefined'){
		this.headerBtnDiv = $ce("div");
		this.headerBtnDiv.className = "headerbtnbar_div";
		this.wholeDiv.appendChild(this.headerBtnDiv);
	}
	if(this.isShowImageBtn){
		this.headerBtnDiv.style.display = "block";
	}else{
		this.headerBtnDiv.style.display = "none";
	}
	if(!this.isRunMode){//如果是非运行态,隐藏功能按钮区.
		this.headerBtnDiv.style.display = "none";
	}
	this.menubarComp = new MenuBarComp(this.headerBtnDiv, "gridMenuBar", 0, 4, "100%", "100%", null, "white_menubar_div",{align:'right'});
	this.menubarComp.centerDiv.style.width = "auto";
	//this.menubarComp.centerDiv.style[ATTRFLOAT] = "right";
	this.menubarComp.Div_gen.style[ATTRFLOAT] = "right";
};

/**
 * 设置树表复选模式
 * 
 * @param checkBoxModel 复选模式
 */
GridComp.prototype.setCheckBoxModel = function(checkBoxModel) {
	this.checkBoxModel = parseInt(checkBoxModel);
};


GridComp.prototype.setHeaderBtnVisible = function(visible){
	this.isShowImageBtn = (visible == true) ? true : false;
	if (visible == true)	
		this.headerBtnDiv.style.display = "block";
	else if (visible == false)	
		this.headerBtnDiv.style.display = "none";
	GridComp.gridResize(this.id);
};

GridComp.prototype.addHeaderBtn = function(id, caption, imgSrc){
	if(!this.isShowImageBtn){
		return;
	}
	
	this.menubarComp.addMenu(id, caption, caption, imgSrc, null, false, null);
};

GridComp.prototype.getHeaderBtn = function(id){
	if(!this.isShowImageBtn){
		return;
	}
	
	return this.menubarComp.getMenu(id);
};

GridComp.prototype.removeHeaderBtn = function(id){
	if(!this.isShowImageBtn){
		return;
	}
	
	if(this.menubarComp.menuItems && this.menubarComp.menuItems != null && this.menuItems.values() && this.menuItems.values().length > 0){
		var items = this.menuItems.values();
		for(var i=0; i<items.length; i++){
			if(items[i].id && items[i].id == id){
				items[i].destroySelf();
				break;
			}
		}
	}
};

GridComp.prototype.removeAllHeaderBtn = function(){
	if(!this.isShowImageBtn){
		return;
	}
	
	this.menubarComp.destroySelf();
};

/*
 * 测试Render
 */
function selfDefHeaderBtnRender(grid){
	//grid.removeHeaderBtn("gridHeaderBtn_Add");
	//grid.removeHeaderBtn("gridHeaderBtn_Edit");
	//grid.removeHeaderBtn("gridHeaderBtn_Delete");
	grid.removeAllHeaderBtn();
	grid.addHeaderBtn("gridHeaderBtn_Test", "测试", window.themePath + "/ui/ctrl/menu/images/whitemenu/toolbar_icons/add_normal.png");
}

/**
 * 初始化全局错误提示对象
 */
GridComp.prototype.initWholeErrorMsgDiv = function(errorBoldMsg, errorMsg){
	var oThis = this;

	this.errorMsgDiv = $ce("DIV");
	this.errorMsgDiv.id = "error_whole_msg_id";
	this.errorMsgDiv.style.display = "none";
	this.errorMsgDiv.className = "error_whole_msg_div";
	
	//九宫格
	this.wholeMsgDiv = $ce("DIV");
	this.wholeMsgDiv.className = "whole_msg_div";
	this.errorMsgDiv.appendChild(this.wholeMsgDiv);
	//左上
	var leftTopDiv = $ce("DIV");
	leftTopDiv.className = "bg_left_top";
	this.wholeMsgDiv.appendChild(leftTopDiv);
	//上
	var topMiddleDiv = $ce("DIV");
	topMiddleDiv.className = "bg_top_middle";
	this.wholeMsgDiv.appendChild(topMiddleDiv);
	//右上
	var rightTopDiv = $ce("DIV");
	rightTopDiv.className = "bg_right_top";
	this.wholeMsgDiv.appendChild(rightTopDiv);
	//右中
	var rightMiddleDiv = $ce("DIV");
	rightMiddleDiv.className = "bg_right_middle";
	this.wholeMsgDiv.appendChild(rightMiddleDiv);
	//右下
	var rightBottomDiv = $ce("DIV");
	rightBottomDiv.className = "bg_right_bottom";
	this.wholeMsgDiv.appendChild(rightBottomDiv);
	//下
	var bottomMiddleDiv = $ce("DIV");
	bottomMiddleDiv.className = "bg_bottom_middle";
	this.wholeMsgDiv.appendChild(bottomMiddleDiv);
	//左下
	var leftBottomDiv = $ce("DIV");
	leftBottomDiv.className = "bg_left_bottom";
	this.wholeMsgDiv.appendChild(leftBottomDiv);
	//左中
	var leftMiddleDiv = $ce("DIV");
	leftMiddleDiv.className = "bg_left_middle";
	this.wholeMsgDiv.appendChild(leftMiddleDiv);
	
	this.errorCenterDiv = $ce("DIV");
	this.errorCenterDiv.className = "error_center_up_div";
	this.wholeMsgDiv.appendChild(this.errorCenterDiv);
	
	this.errorMsg = $ce("DIV");
	this.errorMsg.className = "errorMsg";
	this.errorCenterDiv.appendChild(this.errorMsg);
	/*
	this.errorBoldMsg = $ce("B");
	if(errorBoldMsg){
		this.errorMsg.innerHTML = errorBoldMsg;
	}
	this.errorMsg.appendChild(this.errorBoldMsg);
	if(errorMsg){
		this.errorMsg.appendChild(document.createTextNode(errorMsg));
	}*/
	
	this.warningIcon = $ce("DIV");
	this.warningIcon.className = "warning";
	this.wholeMsgDiv.appendChild(this.warningIcon);
	
	this.closeIcon = $ce("DIV");
	this.closeIcon.className = "close_normal";
	this.closeIcon.onmouseover = function(e){
		this.className = "close_press";
	};
	this.closeIcon.onmouseout = function(e){
		this.className = "close_normal";
	};
	this.closeIcon.onmouseup = function(e){
		this.className = "close_normal";
		oThis.errorMsgDiv.style.display = "none";
	};
	this.wholeMsgDiv.appendChild(this.closeIcon);
	
	this.wholeDiv.appendChild(this.errorMsgDiv);
};

///**
// * 隐藏整体错误信息框
// */
//GridComp.hideErrorMsg = function(widgetId, componentId){
//	var widget = pageUI.getWidget(widgetId);
//	if(widget){
//		var component = widget.getComponent(componentId);
//		if(component && GridComp.prototype.componentType == component.componentType){
//			if(component.errorMsgDiv){
//				component.errorMsgDiv.style.display = "none";
//			}
//		}
//	}
//};

GridComp.prototype.hideErrorMsg = function(){
	if(this.errorMsgDiv){
		this.errorMsgDiv.style.display = "none";
	}
};

GridComp.prototype.setWholeErrorPosition = function(){
	
};

GridComp.prototype.onImageBtnClick = function(fun){
	/*
	if(window.editMode) return;
		var proxy = new ServerProxy(null,null,true);
		proxy.addParam('el', '2');
		proxy.addParam('source_id', this.id);
		proxy.addParam('m_n', fun);
		proxy.addParam('widget_id', this.widget.id);
		
		var sbr = new SubmitRule();
		var pWdRule = new WidgetRule(this.widget.id);
		var dsRule = new DatasetRule(this.model.dataset, "ds_all_line");
		pWdRule.addDsRule(this.model.dataset, dsRule);
		
		sbr.addWidgetRule(this.widget.id,pWdRule);
		proxy.execute();
	*/
};
/**
 * 初始化静态常量 创建GridComp时调用
 * 
 * @private
 */
GridComp.prototype.initStaticConstant = function() {
	GridComp.HEADERROW_HEIGHT = getCssHeight(this.className + "_HEADERROW_HEIGHT");
	GridComp.ROW_HEIGHT = getCssHeight(this.className + "_ROW_HEIGHT");
	GridComp.SELECTCOLUM_WIDTH = getCssHeight(this.className + "_SELECTCOLUM_WIDTH");
	GridComp.CELL_BOTTOM_BORDER_WIDTH = getCssHeight(this.className + "_CELL_BOTTOM_BORDER_WIDTH");
};

/**
 * 外部click事件发生时的回调函数.用来隐藏当前显示的控件
 * 
 * @private
 */
GridComp.prototype.outsideClick = function(e) {
	if (e && e.calendar) return;
	if (this.showComp) {
		if (window.clickHolders.trigger == this.showComp) // 显示参照DIV情况
			return;
		this.hiddenComp();
	}
	this.hideTipMessage(true);
	this.hideenColumnContentMenu();
	
	//隐藏提示信息
	if(typeof(this.compsMap) != 'undefined' && this.compsMap != null){
		var comps = this.compsMap.values();
		if(comps && comps.length > 0){
			for(var i=0; i<comps.length; i++){
				if(comps[i].errorMsgDiv){
					//integer类型不隐藏,只能点关闭按钮关闭
					if(comps[i].componentType != "INTEGERTEXT")
						comps[i].errorMsgDiv.style.display = "none";
			
				}
			}
		}
	}
};

/**
 * 外部鼠标滚轮事件发生时的回调函数.用来隐藏当前显示的控件
 * 
 * @private
 */
GridComp.prototype.outsideMouseWheelClick = function(e) {
	e = EventUtil.getEvent();
	if (e && e.calendar) return;
	if (this.showComp) {
		if (window.clickHolders.trigger == this.showComp) // 显示参照DIV情况
			return;
		this.hiddenComp();
	}
	this.hideenColumnContentMenu();
	this.hideTipMessage(true);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 数据区点击后执行方法
 * 
 * @private
 */
GridComp.prototype.click = function(e) {
	if(IS_IPAD){
		if(this.showComp != null){
			if(this.showComp.blur){
				this.showComp.blur();
			}
		}
	}
	document.onclick(e);
	// 隐藏已经显示出来的设置按钮
	this.hideenColumnContentMenu();
	// grid整体禁用直接返回
	if (this.isGridActive == false) return;
	// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
	var cell = this.getRealCell(e);
	var columDiv = cell.parentNode;
	var rowIndex = this.getCellRowIndex(cell);
	var colIndex = cell.colIndex;
	// 只处理点击动态数据区和静态数据区cell的情况,点击数据区的其他地方不处理
	if (columDiv == null || (columDiv.parentNode != null && columDiv.parentNode.id != "dynamicDataDiv")) return;
	// 首先隐藏掉上一个显示出的控件
	if (this.showComp != null) 
		this.hiddenComp();
	this.setFocusIndex(rowIndex);
	// 行选中之前调用用户的方法,返回false将不允许点击下一行
	if (this.onBeforeRowSelected(rowIndex, this.getRow(rowIndex)) == false) return;
	if (this.isMultiSelWithBox){
//		if ( this.editable != true || this.basicHeaders[colIndex].columEditable != true)
			this.selectColumDiv.children[rowIndex].children[0].onmousedown(e);
	}
	else 
		this.processCtrlSel(false, rowIndex);
	// 不管整体能否编辑都要将点击的cell传给用户,征求用户处理意见(参数:cellItem, rowId,
	// columId)(比如用户想显示一些提示信息)
	if (this.onCellClick(cell, rowIndex, colIndex) == false) {
		stopDefault(e);
		return;
	}
	// 如果当前列可编辑，单元格编辑前调用的方法
	if (this.model.dataset.editable == true && this.basicHeaders[colIndex].columEditable == true) {
		if (this.onBeforeEdit(rowIndex, colIndex) == false) 
			return;
	}
	// 调用公有方法激活真正相应cell的控件
	this.setCellSelected(cell, e.ctrlKey);
	// 改变当前点击的cell的外观
	this.changeSelectedCellStyle(rowIndex);
	
	if(typeof(this.compsMap) != 'undefined' && this.compsMap != null){
		var comp = null;
		var comps = this.compsMap.values();
		if(comps && comps.length > 0){
			for(var i=0; i<comps.length; i++){
				if(typeof(cell.editorType) == 'string' && comps[i].componentType == cell.editorType.toUpperCase()){
					comp = comps[i];
					break;
				}
			}
		}
		if(comp != null){
			var warningIcon = cell.warningIcon;
			if(typeof(warningIcon) == 'undefined'){
				warningIcon = $ce("DIV");
				warningIcon.className = "cellwarning";
				cell.warningIcon = warningIcon;
				cell.style.position = "relative";
			}
			cell.appendChild(warningIcon);
			if (typeof(cell.errorMsg) == "string" && cell.errorMsg != "") {
				if(typeof(comp.setError) == 'function'){
					comp.setError(true);
				}
				if(typeof(comp.setErrorMessage) == 'function'){
					comp.setErrorMessage(cell.errorMsg);
				}
				if(typeof(comp.setErrorStyle) == 'function'){
					comp.setErrorStyle();
				}
				if(typeof(comp.setErrorPosition) == 'function'){
					var top = cell.offsetTop;
					if(this.headerDiv && this.headerDiv.offsetHeight){
						top += this.headerDiv.offsetHeight;
					}
					if(this.outerDiv && this.outerDiv.offsetTop > 0){
						top += this.outerDiv.offsetTop;
					}
					var showColIndex = this.getShowColIndex(colIndex);
					var left = cell.offsetWidth * (showColIndex) + 10;
					comp.setErrorPosition(this.wholeDiv, left, top - 31);
				}
				warningIcon.style.display = "block";	
			}else{
				if(typeof(comp.setError) == 'function'){
					comp.setError(false);
				}
				if(typeof(comp.setErrorMessage) == 'function'){
					comp.setErrorMessage("");
				}
				if(typeof(comp.setNormalStyle) == 'function'){
					comp.setNormalStyle();
				}
				warningIcon.style.display = "none";
			}
		}
	}
};

/**
 * 数据区的事件处理
 * 
 * @private
 */
GridComp.prototype.attachEvents = function() {
	var oThis = this;
	// 监测整体数据区的双击事件
	this.dataOuterDiv.ondblclick = function(e) {
		e = EventUtil.getEvent();
		// grid整体禁用直接返回
		if (oThis.isGridActive == false)
			return;
		// 整体能编辑不支持双击事件
		if (oThis.editable)
			return;
		// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
		var cell = oThis.getRealCell(e);
		if (cell == null || cell == this) // 点击的是空白区域
			return;
		var rowIndex = oThis.getCellRowIndex(cell);
		oThis.onRowDblClick(rowIndex, oThis.getRow(rowIndex));
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	// 阻止事件上传,避免点击鼠标右键显示设置menu
	this.dataOuterDiv.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		// 先执行点击方法，选中该行
		oThis.click(e);
		var result = oThis.onDataOuterDivContextMenu(e);
		if (result == false)
			stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	// 监测整体数据区的点击事件
	this.dataOuterDiv.onclick = function(e) {
		e = EventUtil.getEvent();
		// 点击执行方法
		oThis.click(e);
		oThis.hideTipMessage(true);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	// 监测整体数据区键盘事件
	this.dataOuterDiv.onkeydown = function(e) {
		e = EventUtil.getEvent();
		// grid整体禁用直接返回
		if (oThis.isGridActive == false) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		// 首先隐藏掉上一个显示出的控件
		if (oThis.showComp != null)
			oThis.hiddenComp();
		var cell = oThis.selectedCell;
		if (cell == null)
			cell = getTarget(e);
		var ch = e.lfwKey;
		// 当前没有选中的单元格，直接返回
		if (cell.tagName.toLowerCase() != "div") {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		var rowIndex = oThis.getCellRowIndex(cell);
		var colIndex = cell.colIndex;
		// 下移
		if (ch == 40) {
			if (!oThis.isMultiSelWithBox) { // 单选表格
				if (rowIndex == null) { // 当前没有选中单元格
					var selIndexs = oThis.getSelectedRowIndice();
					if (selIndexs == null || selIndexs.length == 0)
						rowIndex = -1;
					else
						rowIndex = selIndexs[0];
					// 选中下一行
					oThis.model.setRowSelected(rowIndex + 1);
				} else if (rowIndex + 1 <= oThis.getRowsNum() - 1) {
					cell = oThis.getCell(rowIndex + 1, colIndex);
					oThis.setCellSelected(cell);
					// 选中下一行
					oThis.model.setRowSelected(rowIndex + 1);
				}
			}
		}
		// 上移
		else if (ch == 38) {
			if (!oThis.isMultiSelWithBox) { // 单选表格
				if (rowIndex == null) { // 当前没有选中单元格
					var selIndexs = oThis.getSelectedRowIndice();
					if (selIndexs == null || selIndexs.length == 0) {
						// 删除事件对象（用于清除依赖关系）
						clearEventSimply(e);
						return;
					} else
						rowIndex = selIndexs[0];
					// 选中下一行
					if (rowIndex > 0)
						oThis.model.setRowSelected(rowIndex - 1);
				} else if (rowIndex > 0) {
					// 选中上一行单元格
					cell = oThis.getCell(rowIndex - 1, colIndex);
					oThis.setCellSelected(cell);
					// 选中上一行
					oThis.model.setRowSelected(rowIndex - 1);
				}
			}
		}
		// 左移
		else if (ch == 37) {
			if (!oThis.isMultiSelWithBox && rowIndex != null) { // 单选表格
				var tmpCell = oThis.getCell(rowIndex, colIndex - 1);
				var tmpRowIndex = oThis.getCellRowIndex(tmpCell);
				if (tmpCell == null)
					tmpCell = oThis.getVisibleCellByDirection(cell, -1);
				if (tmpCell != null) {
					oThis.setCellSelected(tmpCell);
					if (cell.rowIndex != tmpRowIndex) // 选中新行
						oThis.model.setRowSelected(tmpRowIndex);
					// 改变当前点击的cell的外观
					oThis.changeSelectedCellStyle(tmpRowIndex);
				}
			}
		}
		// 右移
		else if (ch == 39) {
			if (!oThis.isMultiSelWithBox && rowIndex != null) { // 单选表格
				var tmpCell = oThis.getCell(rowIndex, colIndex + 1);
				var tmpRowIndex = oThis.getCellRowIndex(tmpCell);
				if (tmpCell == null) {
					if (rowIndex + 1 <= oThis.getRowsNum() - 1) {
					}
					tmpCell = oThis.getVisibleCellByDirection(cell, 1);
				}
				if (tmpCell != null) {
					oThis.setCellSelected(tmpCell);
					if (cell.rowIndex != tmpRowIndex) // 选中新行
						oThis.model.setRowSelected(tmpRowIndex);
					// 改变当前点击的cell的外观
					oThis.changeSelectedCellStyle(tmpRowIndex);
				}
			}
		}
		// 开放Ctrl C
		if (!e.ctrlKey) {
			stopDefault(e);
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 固定列的双击事件(改变整体的行的外观)
	this.fixedColumDiv.ondblclick = function(e) {
		// grid整体禁用直接返回
		if (oThis.isGridActive == false)
			return;
		// 整体能编辑不支持双击事件
		if (oThis.editable)
			return;

		e = EventUtil.getEvent();

		// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
		var cell = oThis.getRealCell(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		if (cell == null || cell == this) { // 点击的是空白区域
			return;
		}
		var rowIndex = oThis.getCellRowIndex(cell);
		oThis.onRowDblClick(cell.rowIndex, oThis.getRow(rowIndex));
	};

	// 固定列的点击事件(改变整体的行的外观)
	this.fixedColumDiv.onclick = function(e) {

		// 隐藏已经显示出来的设置按钮
		oThis.hideenColumnContentMenu();

		// grid整体禁用直接返回
		if (oThis.isGridActive == false)
			return;

		e = EventUtil.getEvent();

		// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
		var cell = oThis.getRealCell(e);
		// 点击行标区直接返回
		if (cell.id == "rowNumDiv" || cell.id == "fixedColum") {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}

		// 点击行标div,行状态div直接返回
		if (cell.parentNode.id.startWith("numline") || cell.id.startWith("numline")
				|| cell.parentNode.id.startWith("sumRowDiv") || cell.id.startWith("sumRowDiv")
				|| cell.parentNode.id == "lineStateColumDiv") {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		// 改变当前选中行的外观
		var columDiv = cell.parentNode;
		var rowIndex = oThis.getCellRowIndex(cell);
		var colIndex = cell.colIndex;
		// 点击的是固定选择列
		if (columDiv.id == "fixedSelectColum") {
		} else {
			// 首先隐藏掉上一个显示出的控件
			if (oThis.showComp != null)
				oThis.hiddenComp();
			// 行选中之前调用用户的方法
			if (oThis.onBeforeRowSelected(rowIndex, oThis.getRow(rowIndex)) == false) {
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}
			// 不管整体能否编辑都要将点击的cell传给用户,征求用户处理意见(参数:cellItem, rowId,
			// columId)(比如用户想显示一些提示信息)
			if (oThis.onCellClick(cell, rowIndex, colIndex) == false) {
				// 多选模式下只改变选中行的外观
				if (oThis.isMultiSelWithBox) {
					// oThis.rowSelected(rowIndex);
				} else {
					oThis.processCtrlSel(false, rowIndex);
				}
				stopDefault(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}

			// 单元格编辑前调用的方法
			if (oThis.onBeforeEdit(rowIndex, colIndex) == false) {
				oThis.rowSelected(rowIndex);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}

			// 调用公有方法激活真正相应cell的控件
			oThis.setCellSelected(cell, e.ctrlKey);
			if (columDiv.parentNode.id == "fixedDataDiv") {
				// 多选模式下只改变选中行的外观
				if (oThis.isMultiSelWithBox)
					//oThis.rowSelected(rowIndex);
					// 多选状态,调用checkbox事件.
					if (oThis.model.treeLevel == null)
						oThis.selectColumDiv.children[rowIndex].children[0].onmousedown(e);
				else {
					oThis.processCtrlSel(false, rowIndex);
				}
			}
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		}
	};

	// 滚动事件,使固定列固定
	this.dataOuterDiv.onscroll = function(e) {
		//修改编辑控件位置
		if(oThis.currActivedCell && oThis.showComp){
			if (oThis.currActivedCell.editorType == EditorType.TEXTAREA) {
				var cell = oThis.currActivedCell; 
				var comp = oThis.showComp;
				var bodyWidth = document.body.offsetWidth;
				var bodyHeight = document.body.offsetHeight;
				if (bodyWidth < 100 || bodyHeight < 200){
					comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
							compOffsetTop(cell, document.body) - compScrollTop(cell, document.body) - 1, cell.offsetWidth - GridComp.CELL_RIGHT_PADDING
							- 1, cell.offsetHeight);
				}
				else{
					var compLeft = compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING;
					var compTop = compOffsetTop(cell, document.body) - compScrollTop(cell, document.body) - 1;
					if (parseInt(compLeft) + 200 > bodyWidth)
						compLeft = bodyWidth - 200;
					if (parseInt(compTop) + 100 > bodyHeight)
						compTop = bodyHeight - 100;
					comp.setBounds(compLeft, compTop, "200", "100");
				}					
			}
			else{
				oThis.showComp.setBounds(compOffsetLeft(oThis.currActivedCell, document.body) + GridComp.CELL_LEFT_PADDING,
						compOffsetTop(oThis.currActivedCell, document.body) - compScrollTop(oThis.currActivedCell, document.body), oThis.currActivedCell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
						oThis.currActivedCell.offsetHeight);
				oThis.showComp.setFocus();
			}
		}
				
		e = EventUtil.getEvent();

		// 滚动时的缓加载数据
		e.triggerObj = oThis;
		handleScrollEvent(e);

		// 滚动时隐藏掉当前显示的控件
		if (oThis.showComp != null) {
			if (IS_IE || oThis.autoScroll != true)
				oThis.hiddenComp();
			else
				// firefox的程序自动设置scroll时，不隐藏编辑控件
				oThis.autoScroll = false;
		}
		var src = getTarget(e);
		var iScrollLeft = src.scrollLeft;
		if(oThis.dynamicHeaderDiv.oldLeft == null){
			oThis.dynamicHeaderDiv.oldLeft = parseInt(oThis.dynamicHeaderDiv.style.left);
		}
		oThis.dynamicHeaderDiv.style.left = (oThis.dynamicHeaderDiv.oldLeft - iScrollLeft) + "px";
		// 动态列headerDiv
		if (oThis.dynamicHeaderDiv.defaultWidth + iScrollLeft > 0)
			oThis.dynamicHeaderDiv.style.width = (oThis.dynamicHeaderDiv.defaultWidth + iScrollLeft)
					+ "px";
		// 合计行的div,"合计"
		if (oThis.sumRowDiv) {
			oThis.dynSumRowDataDiv.style.left = oThis.sumRowDiv.offsetWidth + "px";
		}
		
		if(oThis.isMultiSelWithBox){
			var scrollTop = src.scrollTop;
			if(oThis.selectColumDiv){
				oThis.selectColumDiv.style.top = (-1 * scrollTop) + "px";
			}
		}

		e.triggerObj = null;
		clearEventSimply(e);
	};

	// 增加统一的resize事件处理必须给对象一个id
	this.outerDiv.id = oThis.id + "_outerdiv";
	// 给outerDiv增加onresize方法
	addResizeEvent(this.outerDiv, function() {
		GridComp.gridResize(oThis.id);
	});
	// 弹出窗口中包含grid，在编辑控件显示时，滚动滚动条（body第一个子元素的滚动条），隐藏编辑控件。
	if(document.body.children[0]){
		if(!document.body.children[0].gridMap){
			document.body.children[0].gridMap = new HashMap();
		}
		document.body.children[0].gridMap.put(this.id, this);
		document.body.children[0].onscroll = function(e){
			// 滚动时隐藏掉当前显示的控件
			var grids = this.gridMap.values();
			for(var i=0; i<grids.length; i++){
				if (grids[i].showComp != null) {
					if (grids[i].autoScroll != true)
						grids[i].hiddenComp();
					else
						grids[i].autoScroll = false;
				}
			}
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
	}
};

/**
 * 设置dataOuterDiv的横向滚动条位置
 * 
 * @private
 */
GridComp.prototype.setScrollLeft = function(scrollLeft) {
	// 系统自动调整标志（非手工拖动）
	if (!IS_IE) this.autoScroll = true;
	if (this.showComp) {
		if (window.clickHolders.trigger == this.showComp) // 显示参照DIV情况
			return;
		this.hiddenComp();
	}
	this.dataOuterDiv.scrollLeft = scrollLeft;
};

/**
 * 设置dataOuterDiv的纵向滚动条位置
 * 
 * @private
 */
GridComp.prototype.setScrollTop = function(scrollTop) {
	// 系统自动调整标志（非手工拖动）
	if (!IS_IE)
		this.autoScroll = true;
	if (this.showComp) {
		if (window.clickHolders.trigger == this.showComp) // 显示参照DIV情况
			return;
		this.hiddenComp();
	}
	this.dataOuterDiv.scrollTop = scrollTop;
};

/**
 * grid自动调整大小逻辑
 * 
 * @param gridId
 *            当前调整大小grid的id
 * @private
 */
GridComp.gridResize = function(gridId) {
	var grid = window.objects[gridId];
	if (grid == null) return;
	var outerDiv = grid.wholeDiv;
	var barHeight = GridComp.SCROLLBAE_HEIGHT;
	if(outerDiv.style.height != '100%'){
		outerDiv.style.height = "100%";
	}
	grid.height = "100%";
	if(outerDiv.style.width != '100%'){
		outerDiv.style.width = "100%";
	}	
	grid.width = '100%';
	if (grid.showComp) {
		/*if (window.clickHolders.trigger == grid.showComp) // 显示参照DIV情况
			return;*/
		grid.hiddenComp();
	}
	try {
		if(!grid.flowmode){
			var height = outerDiv.offsetHeight;
			if(grid.descDiv)
				height = height - grid.descDiv.offsetHeight;
			if(grid.headerBtnDiv)	
				height = height - grid.headerBtnDiv.offsetHeight;
			if(height > 0){
				//grid.outerDiv.style.height = height + "px";
			}
			if(grid.pageSize != -1 && grid.needShowNoRowsDiv)
				height = height - grid.constant.headerHeight - GridComp.NOROW_DIV_HEIGHT;
			else if(grid.pageSize != -1 && !grid.needShowNoRowsDiv)
				height = height - grid.constant.headerHeight - GridComp.PAGEBAR_HEIGHT;
			else
				height = height - grid.constant.headerHeight;
			if(height > 0){
				if (grid.fixedColumDiv)
					grid.fixedColumDiv.style.height = height + "px";
				grid.dataOuterDiv.style.height = height + "px";	
			}
		}
		// 如果此时grid大小和上次不发生变化,则不会真正的重新调整grid大小
		var cond1 = (grid.constant.outerDivWidth != null && grid.constant.outerDivWidth == outerDiv.offsetWidth);
		var cond2 = (grid.constant.outerDivHeight != null && grid.constant.outerDivHeight == outerDiv.offsetHeight);
		if (cond1 && cond2)
			return;

		grid.constant.outerDivWidth = outerDiv.offsetWidth;
		grid.constant.outerDivHeight = outerDiv.offsetHeight;
		grid.constant.fixedHeaderDivWidth = grid.fixedHeaderDiv.offsetWidth;

		if (grid.width.indexOf("%") != -1) {
			var fixedHeaderWidth = grid.constant.fixedHeaderDivWidth;
			var currWidth = grid.constant.outerDivWidth;
			var fixedColumDivWidth = grid.fixedColumDiv.offsetWidth;
			grid.dataOuterDiv.style.width = (currWidth - fixedColumDivWidth) + "px";// - 1;

			// 垂直滚动
			if (grid.isVScroll()) {
				grid.headerDiv.style.width = (currWidth - barHeight) + "px";// - 1;
				grid.headerDiv.defaultWidth = currWidth - barHeight;// - 1;

				var dynHeaderWidth = currWidth - fixedHeaderWidth - barHeight;// - 2;
				if (dynHeaderWidth > 0)
					grid.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
				grid.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
			} 
			else {
				grid.headerDiv.style.width = currWidth + "px";// - 2;
				grid.headerDiv.defaultWidth = currWidth;// - 2;

				var dynHeaderWidth = currWidth - fixedHeaderWidth;// - 3;
				if (dynHeaderWidth > 0)
					grid.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
				grid.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
			}
		}
		grid.setScrollLeft(0);
		// 调整合计行的top
		if (grid.isShowSumRow && grid.sumRowDiv) {
			/*
			if (grid.pageSize > 0 && grid.isPagenationTop == false) // 有分页条并且分页条在下方
				grid.sumRowDiv.style.top = (grid.constant.outerDivHeight
						- GridComp.SCROLLBAE_HEIGHT - GridComp.PAGEBAR_HEIGHT - 20)
						+ "px";
			else
				grid.sumRowDiv.style.top = (grid.constant.outerDivHeight
						- GridComp.SCROLLBAE_HEIGHT - 12)
						+ "px";
			
			if (grid.pageSize > 0 && grid.isPagenationTop == false) // 有分页条并且分页条在下方
				grid.dynSumRowContentDiv.style.top = (grid.constant.outerDivHeight
						- barHeight - GridComp.PAGEBAR_HEIGHT - 20) + "px";
			else
				grid.dynSumRowContentDiv.style.top = (grid.constant.outerDivHeight - barHeight - 12) + "px";
			*/
			// 调整放置合计行的div的宽度
			if(grid.dynamicColumDataDiv && grid.dynamicColumDataDiv.offsetWidth > 0){
				grid.dynSumRowContentDiv.style.width = (grid.dynamicColumDataDiv.offsetWidth) + "px";
			}else{
				grid.dynSumRowContentDiv.style.width = (grid.dynamicHeaderDiv.offsetWidth) + "px";
			}
		}
		// 调整自动扩展表头的宽度,采用缓画,只有gridResize结束后才调整表头自动扩展列的宽度
		if (grid.stForAutoExpand != null)
			clearTimeout(grid.stForAutoExpand);
		grid.stForAutoExpand = setTimeout(
				"GridComp.processAutoExpandHeadersWidth('" + gridId + "','"+this.outerDivId+"')", 100);
	} catch (e) {

	}
};

/**
 * 得到真正的cell对象(render的时候用户可能在cell中加上自己的div，所以点击的不一定是cell)
 * 
 * @private
 */
GridComp.prototype.getRealCell = function(e) {
	var cell = getTarget(e);
	if (cell.editorType == null) {
		var pNode = cell.parentNode;
		while (pNode != null) {
			if (pNode.editorType != null) {
				cell = pNode;
				break;
			}
			pNode = pNode.parentNode;
		}
	}
	return cell;
};

/**
 * 设置grid数据行单行高度
 * 
 * @param{Integer} height
 * @private
 */
GridComp.prototype.setRowHeight = function(height) {
	height = parseInt(height);
	if (height < 10)
		height = 10;
	this.rowHeight = height;
};

/**
 * 设置grid表头单行高度
 * 
 * @param{Integer} height
 * @private
 */
GridComp.prototype.setHeaderRowHeight = function(height) {
	height = parseInt(height);
	if (height < 10)
		height = 10;
	this.headerRowHeight = height;
};

/**
 * 根据行,列索引得到cell 注意:如果此列是隐藏列则继续向下获取不隐藏的最近的一个 cell,此cell可能位于下一行中
 * 
 * @param rowIndex
 *            行索引值
 * @param colIndex
 *            列索引值
 */
GridComp.prototype.getCell = function(rowIndex, colIndex) {
	rowIndex = parseInt(rowIndex);
	colIndex = parseInt(colIndex);
	if (rowIndex < 0 || rowIndex > this.getRowsNum() - 1)
		return null;
	if (colIndex < 0 || colIndex > this.basicHeaders.length - 1)
		return null;
	if (this.basicHeaders[colIndex] != null
			&& this.basicHeaders[colIndex].isHidden == false)
		return this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
	return null;
};

/**
 * 获取该cell的前一个或者上一个可见cell
 * 
 * @param direction
 *            -1,向左;1,向右
 * @private
 */
GridComp.prototype.getVisibleCellByDirection = function(cell, direction) {
	var rowIndex = this.getCellRowIndex(cell);
	var colIndex = cell.colIndex;
	// 尝试获取下一个可见cell
	if (direction == 1) {
		// 首先获取本行内的下一个可见cell
		for (var j = colIndex + 1, count = this.basicHeaders.length; j < count; j++) {
			if (this.basicHeaders[j].isHidden == false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}
		// 如果本行内没有了可见cell,则继续向下面的行搜寻可见cell
		for (var i = rowIndex + 1, rowNum = this.getRowsNum(); i < rowNum; i++) {
			for (var j = 0, count = this.basicHeaders.length; j < count; j++) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	// 尝试获取上一个可见cell
	else if (direction == -1) {
		for (var j = colIndex - 1; j >= 0; j--) {
			if (this.basicHeaders[j].isHidden == false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		for (var i = rowIndex - 1; i >= 0; i--) {
			for (var j = this.basicHeaders.length - 1; j >= 0; j--) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	return null;
};

/**
 * 获取该cell的前一个或者上一个可编辑cell
 * 
 * @param direction
 *            -1,向左;1,向右
 * @private
 */
GridComp.prototype.getEditableCellByDirection = function(cell, direction) {
	if (this.editable == false)
		return null;
	var rowIndex = this.getCellRowIndex(cell);
	var colIndex = cell.colIndex;
	// 尝试获取下一个可见cell
	if (direction == 1) {
		// 首先获取本行内的下一个可见cell
		for (var j = colIndex + 1, count = this.basicHeaders.length; j < count; j++) {
			if (this.basicHeaders[j].isHidden == false
					&& this.basicHeaders[j].columEditable == true
					&& this.onBeforeEdit(rowIndex, j) != false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		// 如果本行内没有了可见cell,则继续向下面的行搜寻可见cell
		for (var i = rowIndex + 1, rowNum = this.getRowsNum(); i < rowNum; i++) {
			for (var j = 0, count = this.basicHeaders.length; j < count; j++) {
				if (this.basicHeaders[j].isHidden == false
						&& this.basicHeaders[j].columEditable == true
						&& this.onBeforeEdit(i, j) != false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	// 尝试获取上一个可见cell
	else if (direction == -1) {
		for (var j = colIndex - 1; j >= 0; j--) {
			if (this.basicHeaders[j].isHidden == false
					&& this.basicHeaders[j].columEditable == true
					&& this.onBeforeEdit(rowIndex, j) != false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		for (var i = rowIndex - 1; i >= 0; i--) {
			for (var j = this.basicHeaders.length - 1; j >= 0; j--) {
				if (this.basicHeaders[j].isHidden == false
						&& this.basicHeaders[j].columEditable == true
						&& this.onBeforeEdit(i, j) != false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	return null;
};

/**
 * 设置cell选中，同时让隐藏的cell显示出来，如果该列能够编辑则激活相应的编辑控件
 * 若选择模式为单选模式则同时通知model行选中，多行选择模式下如果是checkbox多选仅改变行的选中外观，如果是ctrl多行则改变选中的多行
 * 
 * @private
 */
GridComp.prototype.setCellSelected = function(cell, ctrl, shift) {
	var oThis = this;
	if (cell == null)
		return;
		
	if (this.isMultiSelWithBox && !this.editable){
		//多选并且不是编辑态,不设置cell选中.
		return;
	}

	var cellClassName = cell.className;
	// 第一次没有选中的cell
	if (this.oldCell == null) {
		if (this.basicHeaders[cell.colIndex] != null
				&& this.basicHeaders[cell.colIndex].isFixedHeader)
			cell.className += " fixedcell_select";
		else
			cell.className += " cell_select";
		this.oldCell = cell;
		this.oldClassName = cellClassName;
	}
	// 有选中的cell了
	else {
		// 选中cell和当前cell不是同一个cell
		if (this.oldCell != cell) {
			this.oldCell.className = this.oldClassName;
			var oldRowIndex = this.getCellRowIndex(this.oldCell);
			var isOdd = this.isOdd(oldRowIndex);
			var curHeader = this.basicHeaders[this.oldCell.colIndex];

			if (!this.isMultiSelWithBox) {
				if (this.selectedRowIndice != null
						&& this.selectedRowIndice
								.indexOf(oldRowIndex) != -1) {
					if (curHeader != null && curHeader.isFixedHeader)
						this.oldCell.className = isOdd
								? "fixed_gridcell_odd fixedcell_select"
								: "fixed_gridcell_even fixedcell_select";
					else
						this.oldCell.className = isOdd
								? "gridcell_odd cell_select"
								: "gridcell_even cell_select";
				} else {
					if (curHeader != null && curHeader.isFixedHeader)
						this.oldCell.className = isOdd
								? "fixed_gridcell_odd"
								: "fixed_gridcell_even";
					else
						this.oldCell.className = isOdd
								? "gridcell_odd"
								: "gridcell_even";
				}
			} else {
				if (curHeader != null && curHeader.isFixedHeader)
					this.oldCell.className = isOdd
							? "fixed_gridcell_odd"
							: "fixed_gridcell_even";
				else
					this.oldCell.className = isOdd
							? "gridcell_odd"
							: "gridcell_even";
			}
			
			if(this.hasBorder)
				$(this.oldCell).addClass("cellExtendCss");

			this.oldCell = cell;
			this.oldClassName = cellClassName;
		}
	}
	// 记录当前选中的cell
	this.selectedCell = cell;
	// 让隐藏的cell显示出来
	this.letCellVisible(cell);
	// 如果该列可以编辑设置cell编辑控件激活
	if (this.editable == true && this.basicHeaders[cell.colIndex].columEditable == true){
		if(IS_IE9){//在IE9滚动事件之后执行
			setTimeout(function(){oThis.setCellActive(cell)},100);
		}else{
			this.setCellActive(cell);
		}
	}
	// 如果列不能编辑设置cell的焦点,注意该处必须设置焦点,避免cell失去焦点而导致不能接收键盘事件
	else
		cell.focus();
	if(this.model && this.model.owner && this.model.owner.selectedCell && this.model.owner.selectedCell != null){
		var currColIndex = this.model.owner.selectedCell.colIndex;
		if(this.model.rows && this.model.rows[0] != null){
			var filedName = this.model.rows[0].getFiledNameByColIndex(currColIndex);
			this.notifyChange("currentColID", filedName);	
		}
	}
		
};

/**
 * 激活某个cell的编辑控件
 * 
 * @param cell
 *            当前选中的表格单元格
 * @param ctrl
 *            当前是否按着ctrl键操作,按着ctrl键不激活相应控件
 * @private
 */
GridComp.prototype.setCellActive = function(cell, ctrl) {
	if (this.editable == false)
		return;
	if (ctrl)
		return;
//	if (cell.warningIcon != null)
//		warningIcon.style.display = "none";
	// 记录选中cell为当前cell
	this.selectedCell = cell;
	var rowIndex = this.getCellRowIndex(cell);
	var colIndex = cell.colIndex;
	// 用户自定义editor
	var extendComp = null;
	if (this.extendCellEditor !== null){
		var row = this.model.rows[rowIndex];
	 	extendComp = this.extendCellEditor.call(this, document.body, row, colIndex);
	}
	if (extendComp){
		this.compsMap.put("extend$" + colIndex, extendComp);
		this.basicHeaders[colIndex].isExtendComp = true;
		GridComp.addCompListener(this, extendComp, colIndex);
		extendComp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING, compOffsetTop(cell, document.body) -  compScrollTop(cell, document.body), cell.offsetWidth
								- GridComp.CELL_RIGHT_PADDING - 1, GridComp.ROW_HEIGHT);
		extendComp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
		extendComp.showV();
		extendComp.setFocus();
		this.currActivedCell = cell;
		this.showComp = extendComp;
		this.showComp.extend = true;
		this.showComp.Div_gen.style.zIndex = getZIndex();
	}
	else {
		// 如果comp为null,得到stringtext
		if (cell.editorType == null || cell.editorType == "") {
			var comp = this.compsMap.get(EditorType.STRINGTEXT);
			comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
					compOffsetTop(cell, document.body) - compScrollTop(cell, document.body) + getInteger((cell.offsetHeight - 22) / 2,0), cell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
					cell.offsetHeight);
			// 将真实值设置到编辑控件中
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		} else if (cell.editorType != EditorType.CHECKBOX
				&& cell.editorType != EditorType.COMBOBOX
				&& cell.editorType != EditorType.REFERENCE
				&& cell.editorType != EditorType.TEXTAREA
				&& cell.editorType != EditorType.MONEYTEXT
				&& cell.editorType != EditorType.LANGUAGECOMBOBOX
				&& cell.editorType != EditorType.RADIOGROUP) {
			var comp = this.compsMap.get(cell.editorType);
			var header = cell.parentNode.header;

			if (cell.editorType == EditorType.STRINGTEXT)
				comp.setMaxSize(header.maxLength);
			// 若为数字类型根据header的precision属性设置精度,如果有最大最小值则进行设置
			if (cell.editorType == EditorType.DECIMALTEXT) {
				comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
				comp.setPrecision(header.precision);
				if (header.floatMinValue != null)
					comp.setMinValue(header.floatMinValue);
				else
					comp.setMinValue(null);
				if (header.floatMaxValue != null)
					comp.setMaxValue(header.floatMaxValue);
				else
					comp.setMaxValue(null);
			}
			// 若为整数类型根据header的maxValue和minValue设置最大最小值
			if (cell.editorType == EditorType.INTEGERTEXT) {
				comp.setIntegerMinValue(header.integerMinValue);
				comp.setIntegerMaxValue(header.integerMaxValue);
			}
			if (cell.editorType == EditorType.DATETEXT || cell.editorType == EditorType.DATETIMETEXT) {
			    comp.id = header.keyName;
			}
			comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
					compOffsetTop(cell, document.body) - compScrollTop(cell, document.body)  + getInteger((cell.offsetHeight - 22) / 2,0), cell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
					cell.offsetHeight);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		}else if (cell.editorType == EditorType.MONEYTEXT){
			var comp = this.compsMap.get(cell.editorType + colIndex);
			var header = cell.parentNode.header;
			comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
					compOffsetTop(cell, document.body) - compScrollTop(cell, document.body)  + getInteger((cell.offsetHeight - 22) / 2,0), cell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
					cell.offsetHeight);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.setPrecision(header.precision);
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
			// 处理下拉框类型
		}else if (cell.editorType == EditorType.COMBOBOX) {
			var comp = this.compsMap.get(cell.editorType + colIndex);
			var header = this.basicHeaders[colIndex];
			comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
						   compOffsetTop(cell, document.body) - compScrollTop(cell, document.body)  + getInteger((cell.offsetHeight - 22) / 2,0), 
						   cell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
						   cell.offsetHeight);
			// 将记录的上次的旧值清空
			if (comp.oldValue)
				comp.oldValue = null;
			comp.showV();
			comp.setFocus();
			comp.nowCell = cell;

			var selInd = -1;
			if (header.comboData != null) {
				var keyValues = header.comboData.getValueArray();
				if(header.needNullOption){
					var newvalueArr = new Array;
					newvalueArr.push("");
					for ( var i = 0; i < keyValues.length; i++)
						newvalueArr.push(keyValues[i]);
					keyValues = newvalueArr;
				}
				selInd = keyValues.indexOf(this.model.getCellValueByIndex(rowIndex, colIndex));
			}
			comp.setSelectedItem(selInd);
			comp.setMessage(this.model.getCellValueByIndex(rowIndex, colIndex));
			this.currActivedCell = cell;
			this.showComp = comp;

		}
		// 处理多语输入控件
		else if (cell.editorType == EditorType.LANGUAGECOMBOBOX) {
			var comp = this.compsMap.get(cell.editorType + colIndex);
			var header = this.basicHeaders[colIndex];
			comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
						   compOffsetTop(cell, document.body) - compScrollTop(cell, document.body)  + getInteger((cell.offsetHeight - 22) / 2,0), 
						   cell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
						   cell.offsetHeight);
			// 将记录的上次的旧值清空
			if (comp.oldValue)
				comp.oldValue = null;
				
			//初始化下拉数据
			var gridDs = this.model.dataset;
//			var currentRow = gridDs.getSelectedRow();
			var currentRow = gridDs.getFocusRow();
			comp.setComboDatas4Grid(gridDs,currentRow);
			
			comp.showV();
			comp.setFocus();
			comp.nowCell = cell;
			
			this.showComp = comp;
		}
		// 处理参照
		else if (cell.editorType == EditorType.REFERENCE) {
			var comp = this.compsMap.get(cell.editorType + cell.colIndex);
			comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
					compOffsetTop(cell, document.body) - compScrollTop(cell, document.body)  + getInteger((cell.offsetHeight - 22) / 2,0), cell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
					cell.offsetHeight);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.setMessage(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		}
		// 文本域类型
		else if (cell.editorType == EditorType.TEXTAREA) {
			var comp = this.compsMap.get(cell.editorType);
			var bodyWidth = document.body.offsetWidth;
			var bodyHeight = document.body.offsetHeight;
			if (bodyWidth < 100 || bodyHeight < 200)
				comp.setBounds(compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING,
						compOffsetTop(cell, document.body) - compScrollTop(cell, document.body) - 1, cell.offsetWidth - GridComp.CELL_RIGHT_PADDING
								- 1, cell.offsetHeight);
			else{
				var compLeft = compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING;
				var compTop = compOffsetTop(cell, document.body) - compScrollTop(cell, document.body) - 1;
				if (parseInt(compLeft) + 200 > bodyWidth)
					compLeft = bodyWidth - 200;
				if (parseInt(compTop) + 100 > bodyHeight)
					compTop = bodyHeight - 100;
				comp.setBounds(compLeft, compTop, "200", "100");
			}
			var header = this.basicHeaders[colIndex];
			if(header.maxLength)
				comp.setMaxSize(header.maxLength);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		}
		// 处理复选框(checkbox)类型
		else if (cell.editorType == EditorType.CHECKBOX) {
			/* 单元格中本身就包含checkbox，不需要在创建checkbox控件
			var comp = this.compsMap.get(cell.editorType);
//			comp.setBounds(compOffsetLeft(cell, document.body)
//					+ 10, compOffsetTop(cell,
//					document.body)
//					+ 1, cell.offsetWidth - 10 - 1, cell.offsetHeight);
			comp.setBounds(compOffsetLeft(cell, document.body)
					+ parseInt(cell.offsetWidth / 2) - 10, compOffsetTop(cell,
					document.body)
					+ 1, cell.offsetWidth - 1, cell.offsetHeight);
			var header = this.basicHeaders[cell.colIndex];
			if (header.valuePair != null)
				comp.setValuePair(header.valuePair);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.nowCell = cell;
			this.currActivedCell = cell;
			this.showComp = comp;*/
		}
		// 在弹出窗口中，激活某个cell的编辑控件，弹出窗口会遮住comp
		if (this.showComp){
			this.showComp.currColIndex = colIndex;
			this.showComp.Div_gen.style.zIndex = getZIndex();
			/*编辑控件紧靠单元格上边
			if (this.showComp.Div_text != null){
				var marginTop = (this.showComp.Div_gen.offsetHeight - this.showComp.Div_text.offsetHeight)/2;
				if(marginTop > 0){
					this.showComp.Div_text.style.marginTop = marginTop + "px";
				}
			}
			*/
		}
	}
};

/**
 * 若cell处于部分隐藏状态，则将此cell全部显示出来
 * 
 * @param cell
 *            要全部显示出来的cell
 * @private
 */
GridComp.prototype.letCellVisible = function(cell) {
	if (cell == null)
		return;
	// 处理左右隐藏的情况
	// 该cell之前所有显示的表头的宽度
	var preHeadersWidth = this.getPreHeadersWidth(cell);
	var columDiv = cell.parentNode;
	var rowIndex = this.getCellRowIndex(cell);
	if(this.dataOuterDiv)
	   var iScrollLeft = this.dataOuterDiv.scrollLeft;
	var flag = true;

	if (preHeadersWidth == 0) {
		this.setScrollLeft(0); // 1px为修正cell边框显示重合问题
		flag = false;
	} else if (iScrollLeft > preHeadersWidth) {
		var deltX = iScrollLeft - preHeadersWidth;
		this.setScrollLeft(iScrollLeft - deltX - 1); // 1px为修正cell边框显示重合问题
		flag = false;
	}

	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	if (this.pageSize > 0) // 有分页条，要将其高度减去
		gridHeight -= GridComp.PAGEBAR_HEIGHT;
	if (flag) {
		var realWidth = columDiv.offsetLeft + cell.offsetWidth;
		var currWidth = gridWidth - this.constant.fixedColumDivWidth
				+ iScrollLeft;
		if (realWidth > currWidth) {
			var deltX = realWidth - currWidth;
			if (this.isVScroll()) {
				this.setScrollLeft(iScrollLeft + deltX + 1
						+ GridComp.SCROLLBAE_HEIGHT);
			} else {
				this.setScrollLeft(iScrollLeft + deltX + 1);
			}
		}
	}

	// 处理上下隐藏的情况
	var preRowsHeight = rowIndex * this.rowHeight;
	var iScrollTop = this.dataOuterDiv.scrollTop;
	if (iScrollTop > preRowsHeight) {
		var deltY = iScrollTop - preRowsHeight;
		this.setScrollTop(iScrollTop - deltY);
	}
	var realHeight = (rowIndex + 1) * this.rowHeight;
	if (this.isScroll())
		currHeight = gridHeight - this.constant.headerHeight + iScrollTop
				- GridComp.SCROLLBAE_HEIGHT;
	else
		currHeight = gridHeight - this.constant.headerHeight + iScrollTop;
	if (realHeight > currHeight) {
		var deltY = realHeight - currHeight;
		this.setScrollTop(iScrollTop + deltY);
	}
};

/**
 * 处理按住ctrl并且选行时的逻辑
 * 
 * @ctrl 是否按住ctrl键点击当前行
 * @private
 */
GridComp.prototype.processCtrlSel = function(ctrl, rowIndex) {
	// 如果此时有多行选中,则清除所有选中行,然后选中当前行
	if (this.selectedRowIndice != null && this.selectedRowIndice.length > 1) {
		// this.clearAllUISelRows();
		// 选中当前行
		this.model.setRowSelected(rowIndex);
	} else {
		// 如果点击的cell处在选中行上,并且该行已经选中则不再通知model行选中 gd 2007-12-05
		if (this.selectedRowIndice == null
				|| (this.selectedRowIndice.length > 0 && this.selectedRowIndice[0] != rowIndex))
			this.model.setRowSelected(rowIndex);
		else
			this.rowSelected(rowIndex);
	}
};

/**
 * 设置当前焦点行失去焦点,Grid没有焦点行.
 */
GridComp.prototype.loseFocusIndex = function(){
	var focusIndex = this.getFocusIndex();
	if(typeof(focusIndex) == 'number' && focusIndex >= 0){
		var headers = this.basicHeaders;
		for(var i=0; i<headers.length; i++){
			if(headers[i].dataDiv){
				var focusCell = headers[i].dataDiv.cells[focusIndex];
				if(typeof(focusCell) == 'object'){
					if(typeof(focusCell.className) == 'string' && focusCell.className.indexOf("cell_focus") != -1){
						focusCell.className = focusCell.className.replace(" cell_focus", "");
					}
					focusCell.isFocusRow = false;
				}
			}
		}
		this.focusIndex = -1;
		this.model.setFocusIndex(-1);
	}
};

/**
 * 设置当前聚焦行（点击行，包括选中或未选中）
 * 
 * @private
 */
GridComp.prototype.setFocusIndex = function(rowIndex) {
	//如果行rowIndex < 0,焦点行不改变.
	if(typeof(rowIndex) == 'number' && rowIndex >= 0){
		var oldFocusRowIndex = this.getFocusIndex();
		//设置新焦点行在数据集中的真实索引,真实索引由GridCompModel负责转换.
		this.model.setFocusIndex(rowIndex);
		var headers = this.basicHeaders;
		//设置旧焦点行恢复失去焦点时样式,设置新焦点行获取焦点时样式.
		for(var i=0; i<headers.length; i++){
			if(headers[i].dataDiv){
				var focusCel = null;
				if(typeof(oldFocusRowIndex) == 'number' && oldFocusRowIndex >= 0 && rowIndex != oldFocusRowIndex){//旧焦点行恢复失去焦点时样式
					var focusCell = headers[i].dataDiv.cells[oldFocusRowIndex];
					if(typeof(focusCell) == 'object'){
						if(typeof(focusCell.className) == 'string' && focusCell.className.indexOf("cell_focus") != -1){
							focusCell.className = focusCell.className.replace(" cell_focus", "");
						}
						focusCell.isFocusRow = false;
					}
				}
				focusCell = headers[i].dataDiv.cells[rowIndex];
				if(typeof(focusCell) == 'object'){//新焦点行获取焦点时样式
					if(typeof(focusCell.className) == 'string'){
						if(focusCell.className.indexOf("cell_focus") == -1){
							focusCell.className += " cell_focus";
						}
					}else{
						focusCell.className = " cell_focus";
						if(this.hasBorder)
							$(focusCell).addClass("cellExtendCss");
					}
					focusCell.isFocusRow = true;
				}
			}
		}
		this.focusIndex = rowIndex;
	}
};

/**
 * 获取当前聚焦行（点中行）
 * 
 * @private
 */
GridComp.prototype.getFocusIndex = function() {
	// 返回页面显示焦点行索引,不是rowData数据集中的真实行索引.
	return this.focusIndex;
	//return this.model.getFocusIndex();
};

/**
 * 清除所有选中行外观
 * 
 * @private
 */
GridComp.prototype.clearAllUISelRows = function() {
	var selRowsIndice = this.selectedRowIndice;
	if (selRowsIndice != null && selRowsIndice.length > 0) {
		// 所有选中行
		var selIndice = [];
		for (var i = 0, count = selRowsIndice.length; i < count; i++)
			selIndice.push(this.selectedRowIndice[i]);

		// 清除所有选中行的外观
		for (var i = selIndice.length - 1; i >= 0; i--) {
			//if (this.isMultiSelWithBox == false) {
				var index = selIndice[i];
				for (var j = 0, headerLength = this.basicHeaders.length; j < headerLength; j++) {
					var header = this.basicHeaders[j];
					if (header.isHidden == false) {
						if(header.dataDiv && header.dataDiv.cells)
							var cell = header.dataDiv.cells[index];
						if (cell != null) {
							var isOdd = this.isOdd(index);
							// 校验不通过的的字段颜色不清除掉
							if (cell.isErrorCell) {
								if (header.isFixedHeader)
									cell.className = isOdd
											? "fixed_gridcell_odd cell_error"
											: "fixed_gridcell_even cell_error";
								else
									cell.className = isOdd
											? "gridcell_odd cell_error"
											: "gridcell_even cell_error";
							} else {
								if (header.isFixedHeader)
									cell.className = isOdd
											? "fixed_gridcell_odd"
											: "fixed_gridcell_even";
								else
									cell.className = isOdd
											? "gridcell_odd"
											: "gridcell_even";
							}
							if(this.hasBorder)
								$(cell).addClass("cellExtendCss");
						}
					}
				}

				// 设置此行状态div的背景
				var node = this.lineStateColumDiv.cells[index];
				if (node != null && node.className != "row_state_div row_update_state"
						&& node.className != "row_state_div row_add_state")
					node.className = "row_state_div";

				// 从选择数组中将此选择行删除
				this.selectedRowIndice.splice(i, 1);
				// 没有选中行将选中数组置空
				if (this.selectedRowIndice.length == 0)
					this.selectedRowIndice = null;
			//}
		}
	}
};

/**
 * 重新处理设置为自动扩展表头的宽度
 * 
 * @private
 */
GridComp.processAutoExpandHeadersWidth = function(gridId,outDivId) {
	var grid = window.objects[gridId];
	var autoHeaders = grid.getAutoExpandHeaders();
	// 此Header是自动扩展表头
	if (autoHeaders != null && autoHeaders.length > 0) {
//		if (!grid.isScroll()) {
			var expandTotalWidth = 0;
			// TODO:减1是为了修正在portal中自动调整后仍有横向滚动条的问题
			if (grid.isVScroll())
				expandTotalWidth = grid.outerDiv.offsetWidth
						- grid.getNoAutoExpandHeadersWidth()
						//- GridComp.ROWSTATE_COLUMN_WIDTH
						- GridComp.COLUMN_LEFT_BORDER_WIDTH - 17 - 2;
			else
				expandTotalWidth = grid.outerDiv.offsetWidth
						- grid.getNoAutoExpandHeadersWidth()
						//- GridComp.ROWSTATE_COLUMN_WIDTH
						- GridComp.COLUMN_LEFT_BORDER_WIDTH - 2;

			// 60px为多选列的宽度
			if (grid.isMultiSelWithBox)
				expandTotalWidth = expandTotalWidth
						- GridComp.MULTISEL_COLUMN_WIDTH
						- GridComp.COLUMN_LEFT_BORDER_WIDTH;// - 1;

			// 如果显示数字列,自动扩展宽度要减去数字列
			if (grid.isShowNumCol)
				expandTotalWidth = expandTotalWidth
						- grid.constant.rowNumHeaderDivWidth;
			var oneWidth = 0;
			if(expandTotalWidth < 0){
				oneWidth = 101;					
			}else{
				// 每一份的宽度
				oneWidth = Math.floor(expandTotalWidth / autoHeaders.length)
						- GridComp.COLUMN_LEFT_BORDER_WIDTH;
			}	
			//如果每一份的宽度大于设定的最小宽度则进行处理，最小宽度目前在常量中指定，待扩展,当前是120
			if (oneWidth > GridComp.EXPANDHEADER_MINWIDTH){					
				for (var i = 0, count = autoHeaders.length; i < count; i++) {
					if (i == count - 1) {
						if(expandTotalWidth < 0)
							autoHeaders[i].width = oneWidth;
						else
							autoHeaders[i].width = expandTotalWidth - i* (oneWidth + GridComp.COLUMN_LEFT_BORDER_WIDTH)- GridComp.COLUMN_LEFT_BORDER_WIDTH;
						// 改变dynamicHeaderTableDiv的宽度
						var dynTableDivRealWidth = grid
								.getDynamicTableDivRealWidth(true);
						// + (count + 1);
						if (IS_IE)
							dynTableDivRealWidth = dynTableDivRealWidth + 1;
						grid.dynamicHeaderTableDiv.style.width = dynTableDivRealWidth
								+ "px";
	
						autoHeaders[i].dataTable.style.width = autoHeaders[i].width
								+ "px";
						autoHeaders[i].cell.width = autoHeaders[i].width;
						autoHeaders[i].contentDiv.style.width = (autoHeaders[i].width - 1)
								+ "px";
						autoHeaders[i].dataDiv.style.width = autoHeaders[i].width
								+ "px";
						// grid.dynamicColumDataDiv.style.width =
						// (dynTableDivRealWidth - count - 2) + "px";
						grid.dynamicColumDataDiv.style.width = dynTableDivRealWidth	+ "px";
						if(grid.dynSumRowContentDiv){
							grid.dynSumRowContentDiv.style.width = dynTableDivRealWidth	+ "px";
						}
						if(IS_IE7){
							if(grid.dynamicColumDataDiv.offsetWidth > grid.dataOuterDiv.offsetWidth){
								grid.dynamicColumDataDiv.style.marginBottom = "17px";
							}else{
								grid.dynamicColumDataDiv.style.marginBottom = "0px";
							}
						}
						// 处理合计行
						if (autoHeaders[i].sumCell) {
							if (autoHeaders[i].keyName == grid.basicHeaders[0].keyName)
								autoHeaders[i].sumCell.style.width = autoHeaders[i].width
										+ GridComp.ROWSTATE_COLUMN_WIDTH
										- GridComp.SUMROW_DIV_WIDTH
										- (GridComp.SUMCELL_PADDING * 2) - 1 + "px";
							// autoHeaders[i].sumCell.style.width =
							// autoHeaders[i].width - 3 - 20 + "px";
							else
								autoHeaders[i].sumCell.style.width = autoHeaders[i].width
										- (GridComp.SUMCELL_PADDING * 2) - 1 + "px";
						}
						if (grid.dynSumRowDataDiv)
							grid.dynSumRowDataDiv.style.width = dynTableDivRealWidth + "px";
					} else {
						autoHeaders[i].width = oneWidth;
						autoHeaders[i].cell.width = oneWidth;
						autoHeaders[i].contentDiv.style.width = (oneWidth - 1)
								+ "px";
						autoHeaders[i].dataTable.style.width = oneWidth + "px";
						autoHeaders[i].dataDiv.style.width = oneWidth + "px";
						// 处理合计行
						if (autoHeaders[i].sumCell) {
							if (autoHeaders[i].keyName == grid.basicHeaders[0].keyName)
								autoHeaders[i].sumCell.style.width = autoHeaders[i].width
										+ GridComp.ROWSTATE_COLUMN_WIDTH
										- GridComp.SUMROW_DIV_WIDTH
										- (GridComp.SUMCELL_PADDING * 2) + "px";
							else
								autoHeaders[i].sumCell.style.width = autoHeaders[i].width
										- (GridComp.SUMCELL_PADDING * 2) + "px";
						}
					}
					// guoweic: add 修正当列设置为autoExpand="true"时，该元素宽度的显示问题 start
					// 2009-11-20
					if (autoHeaders[i].isHidden == false) {
						for (var j = 0, rowLength = autoHeaders[i].dataDiv.cells.length; j < rowLength; j++) {
							var tempCell = autoHeaders[i].dataDiv.cells[j];
							if (tempCell) {
								// 修正当有列设置为autoExpand="true"时，该元素宽度的显示问题
								tempCell.style.width = autoHeaders[i].width - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING 
										+ "px"; // 5为左右padding加上border的宽
							}
						}
					}
					// guoweic: add end
					//如果当前列的宽度发生变化并且grid自动计算行高，则需要重新计算行高
					if(grid.autoRowHeight == true){
						for(var j = 0 ; j < grid.model.rows.length; j ++){
							grid.adjustRowHeight(j, autoHeaders[i].dataDiv.cells[j]);	
						}
					}
				}
			}
			$("#"+outDivId).perfectScrollbar('updateKeepLeft',outDivId);
//		}
	}
};

/**
 * 获取非自动扩展表头的宽度
 * 
 * @private
 */
GridComp.prototype.getNoAutoExpandHeadersWidth = function() {
	if (this.basicHeaders == null)
		return -1;
	var width = 0;
	for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
		if (this.basicHeaders[i].isHidden == false
				&& this.basicHeaders[i].isAutoExpand == false)
			width += this.basicHeaders[i].width
					+ GridComp.COLUMN_LEFT_BORDER_WIDTH;
	}
	return width;
};

/**
 * 获取自动扩展宽度的表头
 * 
 * @private
 */
GridComp.prototype.getAutoExpandHeaders = function() {
	if (this.basicHeaders == null)
		return null;
	var autoHeaders = [];
	for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
		if (this.basicHeaders[i].isHidden == false
				&& this.basicHeaders[i].isAutoExpand == true && this.basicHeaders[i].isGroupHeader == false)
			autoHeaders.push(this.basicHeaders[i]);
	}
	return autoHeaders;
};

/**
 * 初始化所有常量
 * 
 * @private
 */
GridComp.prototype.initConstant = function() {
	if (this.constant == null)
		this.constant = new Object();
	if (this.wholeDiv.offsetWidth != 0) {
		this.constant.outerDivHeight = this.wholeDiv.offsetHeight;
		this.constant.outerDivWidth = this.wholeDiv.offsetWidth;
		return true;
	}
	else
		return false;
};

/**
 * 获得基本头信息。此grid支持多表头，只有最下层的表头包含有用信息。
 * 
 * @param headers
 *            最顶层的headers
 * @private
 */
GridComp.prototype.initBasicHeaders = function() {
	// 已经调用model.setDataset初始化了basicHeaders
	var basicHeaders = this.model.getBasicHeaders();
	// 对需要分组的header设上标示
	if (this.groupHeaderIds != "") {
		// 记录分组列的列索引
		this.groupHeaderColIndice = [];
		var j = 0;
		for (var i = 0, count = basicHeaders.length; i < count; i++) {
			if (j == this.groupHeaderIds.length)
				break;
			if (basicHeaders[i].keyName == this.groupHeaderIds[j]) {
				basicHeaders[i].isGroupBy = true;
				this.groupHeaderColIndice.push(i);
				j++;
			}
		}
	}

	if (basicHeaders != null && basicHeaders.length > 0) {
		// 得到model中设置的headers数据
//		var headers = this.model.getHeaders();
		// 保存header的总体高度
		if (this.isShowHeader)
			this.constant.headerHeight = this.getHeaderDepth()
					* (this.headerRowHeight); // +1为边框的高度
		else
			this.constant.headerHeight = 0;
	} else {
		if (this.model.getHeaders() == null)
			throw new Error("grid must be initialized with headers!");
	}
};

/**
 * 根据key获取header
 * 
 * @private
 */
GridComp.prototype.getHeader = function(keyName) {
	var basicHeaders = this.model.getBasicHeaders();
	if (basicHeaders == null)
		return null;
	else {
		for (var i = basicHeaders.length - 1; i >= 0; i--) {
			if (basicHeaders[i].keyName == keyName)
				return basicHeaders[i];
		}
	}
};

GridComp.prototype.removeHeader = function(keyName) {
	var header = this.model.removeHeader(keyName);
	return header;
};

/**
 * 判断初始界面是否会出现横向、纵向滚动条
 * 
 * @private
 */
GridComp.prototype.adjustScroll = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	// 判断初始界面时是否会出现横向滚动条
	this.scroll = false;
	var dataRealWidth = this.getDynamicTableDivRealWidth(true)
			+ this.getDynamicTableDivRealWidth(false);
	if (dataRealWidth > gridWidth)
		this.scroll = true;

	// 判断初始界面时是否会出现纵向滚动条(修正表头宽度)
	this.vScroll = false;
	var dataRealHeight = gridHeight - this.getRowsNum() * this.rowHeight
			- this.constant.headerHeight;
	if (this.pageSize > 0)
		dataRealHeight -= GridComp.PAGEBAR_HEIGHT;
	if (dataRealHeight < 0)
		this.vScroll = true;
};

/**
 * 创建分页操作条
 * 
 * @private
 */
GridComp.prototype.initPaginationBar = function() {
	var oThis = this;
	this.paginationBar = $ce("div");
	this.paginationBar.className = "grid_paginationbar";
	this.paginationBar.style.width = "100%";
	this.outerDiv.appendChild(this.paginationBar);
	
	this.paginationPanel = $ce("DIV");
	this.paginationPanel.className = "paginationPanel";
	this.paginationPanel.style.overflow = "hidden";
	this.paginationBar.appendChild(this.paginationPanel);
	
	this.paginationContent = $ce("div");
	this.paginationContent.id = "grid_paginationcontent";
	this.paginationContent.className = "pageinationbgcenter";
	this.paginationPanel.appendChild(this.paginationContent);
	if(this.constant.outerDivWidth > document.body.screenWidth){
  		this.constant.outerDivWidth = document.body.screenWidth;
	}
	this.paginationBar.style.width = this.constant.outerDivWidth + "px";
	
	this.paginationMessage = $ce("DIV"); 
	this.paginationMessage.className = "paginationMessage";
	
	this.paginationText1 = $ce("DIV");
	this.paginationText1.innerHTML = trans("ml_goto")/*"跳转到"*/;
	this.paginationText1.className = "paginationText";
	this.paginationText1.style.marginRight = "5px";
	this.paginationText2 = $ce("DIV");
//	this.paginationText2.innerHTML = trans("ml_goto_page")/*"页"*/;
	this.paginationText2.className = "paginationText";
	this.paginationText2.style.marginLeft = "5px";
	this.paginationText2.style.marginRight = "10px";
	
	this.sumRowCountSpan = $ce("SPAN"); 
	this.sumRowCountSpan.innerHTML = this.model.dataset.getAllRowCount();
	
	this.paginationText3 = $ce("DIV");
//	this.paginationText3.appendChild(document.createTextNode(trans("ml_all")/*"共"*/));
//	this.paginationText3.appendChild(this.sumRowCountSpan);
//	this.paginationText3.appendChild(document.createTextNode(trans("ml_line")/*"条"*/));
	this.paginationText3.innerHTML = trans("ml_allLine", ["<span>" + this.model.dataset.getAllRowCount() + "</span>"])/*共{0}条*/;
	this.paginationText3.className = "paginationText";
	this.paginationText3.style.marginRight = "20px";
	this.paginationText4 = $ce("DIV");
	this.paginationText4.innerHTML = trans("ml_pageRowCount", ["<span class='perPageRowCount' >" + this.pageSize + "</span>"])/*每页显示{0}条*/;
	this.paginationText4.className = "paginationText";
	this.paginationMessage.appendChild(this.paginationText1);
	if(IS_IPAD){
		this.paginationInput = new IntegerTextComp(this.paginationMessage, 'paginationInput', 0, 4, 53, "relative");	
	}else{
		this.paginationInput = new IntegerTextComp(this.paginationMessage, 'paginationInput', 0, 4, 35, "relative");
	}
	this.paginationInput.Div_gen.className = "paginationText";
	this.paginationGo = $ce("DIV");
	this.paginationGo.style.width = "20px";
	this.paginationGo.style.height = "22px";
	this.paginationGo.style.lineHeight = "22px";
	this.paginationGo.style.marginTop = "4px";
	this.paginationGo.style.cursor = "pointer";
	this.paginationGo.style.textAlign = "center";
	this.paginationGo.style.background = "#e4e4e4";
	this.paginationGo.style[ATTRFLOAT] = "left";
	this.paginationGo.innerHTML=">";
	this.paginationGo.onclick = function(e){
		var pageIndex = parseInt(oThis.paginationInput.newValue, 10) - 1;
		GridComp.pageNavgate(e, pageIndex, oThis.id);
	};  
	this.paginationMessage.appendChild(this.paginationGo);
	
	this.paginationMessage.appendChild(this.paginationText2);
	this.paginationInput.onenter = function(e){
		var pageIndex = parseInt(this.newValue, 10) - 1;
		GridComp.pageNavgate(e, pageIndex, oThis.id);
	};
	this.paginationMessage.appendChild(this.paginationText3);
	this.paginationMessage.appendChild(this.paginationText4);
	this.paginationPanel.appendChild(this.paginationMessage);
};

/**
 * 创建简单分页操作条
 * 
 * @private
 */
GridComp.prototype.initSimplePaginationBar = function() {
	var oThis = this;
	this.paginationBar = $ce("div");
	this.paginationBar.className = "grid_paginationbar";
	this.paginationBar.style.width = "100%";
	this.outerDiv.appendChild(this.paginationBar);
	
	this.paginationPanel = $ce("DIV");
	this.paginationPanel.className = "paginationPanel";
	this.paginationPanel.style.overflow = "hidden";
	this.paginationBar.appendChild(this.paginationPanel);
	
	this.paginationContent = $ce("div");
	this.paginationContent.id = "grid_paginationcontent";
	this.paginationContent.className = "pageinationbgcenter";
	//this.paginationPanel.appendChild(this.paginationContent);
	if(this.constant.outerDivWidth > document.body.screenWidth){
  		this.constant.outerDivWidth = document.body.screenWidth;
	}
	this.paginationBar.style.width = this.constant.outerDivWidth + "px";
	
	this.paginationMessage = $ce("DIV");
	this.paginationMessage.className = "simple_paginationMessage";
	
	this.paginationText1 = $ce("DIV");
	this.paginationText1.innerHTML = trans("ml_pagepre")/*"上一页"*/;
	this.paginationText1.className = "paginationText";
	this.paginationText1.style.marginRight = "12px";
	this.paginationText1.style.cursor = "pointer";
	this.paginationText1.onclick = GridComp.pagePre;
	this.paginationText1.gridId = this.id;
	
	this.sumPageCountSpan = $ce("SPAN");
	
	this.paginationText2 = $ce("DIV");
	this.paginationText2.appendChild(document.createTextNode("/"));
	this.paginationText2.appendChild(this.sumPageCountSpan);
	this.paginationText2.className = "paginationText";
	this.paginationText2.style.marginLeft = "0px";
	this.paginationText2.style.marginRight = "12px";
	
	this.paginationText4 = $ce("DIV");
	this.paginationText4.innerHTML = trans("ml_pagenext") /*"下一页"*/;
	this.paginationText4.className = "paginationText";
	this.paginationText4.style.cursor = "pointer";
	this.paginationText4.onclick = GridComp.pageNext;
	this.paginationText4.gridId = this.id;
	
	this.paginationMessage.appendChild(this.paginationText1);
	if(IS_IPAD){
		this.paginationInput = new IntegerTextComp(this.paginationMessage, 'paginationInput', 0, 4, 53, "relative");	
	}else{
		this.paginationInput = new IntegerTextComp(this.paginationMessage, 'paginationInput', 0, 4, 35, "relative");
	}
	
	this.paginationInput.Div_gen.className = "paginationText";
	this.paginationMessage.appendChild(this.paginationText2);
	//this.paginationMessage.appendChild(this.paginationText3);
	this.paginationMessage.appendChild(this.paginationText4);
	
	this.paginationPanel.appendChild(this.paginationMessage);
	
	this.paginationInput.onenter = function(e){
		var pageIndex = parseInt(this.newValue, 10) - 1;
		GridComp.pageNavgate(e, pageIndex, oThis.id);
	};
	/*var onblur = this.paginationInput.input.onblur;
	this.paginationInput.input.onblur = function(e){
		onblur(e);
		if(!isNaN(oThis.pageIndex)){
			this.value = parseInt(oThis.pageIndex, 10) + 1;
		}
	};
	var onfocus = this.paginationInput.input.onfocus;
	this.paginationInput.input.onfocus = function(e){
		onfocus(e);
		this.value = '';
	}*/
};

/**
 * 处理后台分页
 * 
 * @param pageIndex
 *            当前页码
 * @private
 */
GridComp.prototype.processServerPagination = function(pageIndex) {
	var pageCount = this.model.getPageCount();
	if (pageIndex < 0 || pageIndex > pageCount - 1)
		return;
	this.processPaginationInfo(pageIndex, this.model.dataset.getAllRowCount(),
			pageCount, this.pageSize);
	this.model.dataset.setCurrentPage(null, pageIndex);
	// 切换页签时页面数据高度可能发生变化需要重新计算iframe高度
	adjustContainerFramesHeight();
};

/**
 * 处理分页信息
 * 
 * @private
 */
GridComp.prototype.setPaginationInfo = function() {
	if (this.pageSize > 0) {
		var pageIndex = this.model.dataset.getPageIndex(null);
		// 显示分页信息(后台分页)
		this.processPaginationInfo(pageIndex, this.model.dataset
				.getAllRowCount(), this.model.getPageCount(), this.pageSize);
	}
};

/**
 * 根据传入的参数,控制分页按钮状态,显示分页信息
 * 
 * @param pageIndex
 *            页码
 * @param rowCount
 *            当前页行数,对于前台分页指当前分页的总行数
 * @param pageCount
 *            页数
 * @param pageRowCount
 *            每页行数
 * @param isClient
 *            是否前台分页
 * @private
 */
GridComp.prototype.processPaginationInfo = function(pageIndex, rowCount,
		pageCount, pageRowCount) {
	if(this.sumRowCountSpan){
		this.sumRowCountSpan.innerHTML = rowCount;
		if(this.paginationText3)
			this.paginationText3.innerHTML = trans("ml_allLine", ["<span>" + rowCount + "</span>"])/*共{0}条*/;
	}
	if(this.sumPageCountSpan){
		this.sumPageCountSpan.innerHTML = pageCount;
	}
	this.pageIndex = pageIndex;
	
	if(this.paginationBar){
		if(pageCount <= 1){
			this.paginationBar.style.display = "none";
			return;
		}
	}else{
		return;
	}
	this.paginationBar.style.display = "block";
	
	if(this.isSimplePagination){
		this.paginationPanel.style.width = this.paginationText1.offsetWidth + 12 + this.paginationText2.offsetWidth + 12 + this.paginationText4.offsetWidth + this.paginationInput.Div_gen.offsetWidth + "px";//this.paginationMessage.offsetWidth + "px";
		this.paginationInput.input.value = pageIndex + 1;
	}else{
		this.paginationContent.innerHTML = "";
		var preDiv = $ce("div");
		preDiv.className = "pre";
		preDiv.onmouseover = GridComp.preMouseOver;
		preDiv.onmouseout = GridComp.preMouseOut;
		var preA = $ce("A");
		preA.onclick = GridComp.pagePre;
		preA.href = "#";
		preA.gridId = this.id;
		preA.pageIndex = pageIndex;
		preA.appendChild(preDiv);
		this.paginationContent.appendChild(preA);
		
		var pageFirst = $ce("A");
		pageFirst.onclick = GridComp.pageFirst;
		pageFirst.href = "#";
		pageFirst.gridId = this.id;
		pageFirst.pageIndex = pageIndex;
		var selectedCenterDiv = $ce("div");
		selectedCenterDiv.className = "pagefirst";
		selectedCenterDiv.innerHTML = trans("ml_pagefirst")/*"首页"*/;
		pageFirst.appendChild(selectedCenterDiv);
		this.paginationContent.appendChild(pageFirst);
		
		var smartCutDivFirst = $ce("div");
		smartCutDivFirst.className = "smartcut";
		this.paginationContent.appendChild(smartCutDivFirst);
	
		var classNamePre = "un";
		if(pageCount <= 8){
			for(var i = 0; i < pageCount; i ++){
				var a = $ce("A");
				a.onclick = GridComp.pageNavgate;
				a.href = "#";
				a.pageIndex = i;
				a.gridId = this.id;
				if(i == pageIndex){
					classNamePre = "";
				}else{
					classNamePre = "un";
				}
				if(i + 1 <= 99){
					var selectedDiv = $ce("div");
					selectedDiv.className = classNamePre + "selected";
					selectedDiv.innerHTML = (i + 1);
					a.appendChild(selectedDiv);
				}else{
					var selectedLeftDiv = $ce("div");
					var selectedCenterDiv = $ce("div");
					var selectedRightDiv = $ce("div");
					selectedLeftDiv.className = classNamePre + "selectedleft";
					selectedCenterDiv.className = classNamePre + "selectedcenter";
					selectedRightDiv.className = classNamePre + "selectedright";
					selectedCenterDiv.innerHTML = (i + 1);
					a.appendChild(selectedLeftDiv);
					a.appendChild(selectedCenterDiv);
					a.appendChild(selectedRightDiv);
				}
				this.paginationContent.appendChild(a);
			}
		}else{
			var beginIndex = 0;
			var endIndex = 0;
			if(pageIndex <= 1){
				beginIndex = 0;
			}else{
				beginIndex = pageIndex - 2;
			}
			endIndex = beginIndex + 4;
			if(pageIndex >= pageCount - 6){
				beginIndex = pageCount - 8;
				endIndex = beginIndex + 7;
			}
			if(pageIndex >= pageCount - 2){
				beginIndex = 0;
				endIndex = beginIndex + 4;
			}
			for(var i = 0; i < pageCount; i++){
				if((i >= beginIndex && i <= endIndex) || (i >= pageCount - 2)){
					var a = $ce("A");
					a.onclick = GridComp.pageNavgate;
					a.href = "#";
					a.pageIndex = i;
					a.gridId = this.id;
					if(i == pageIndex){
						classNamePre = "";
					}else{
						classNamePre = "un";
					}
					if(i + 1 <= 99){
						var selectedDiv = $ce("div");
						selectedDiv.className = classNamePre + "selected";
						selectedDiv.innerHTML = (i + 1);
						a.appendChild(selectedDiv);
					}else{
						var selectedLeftDiv = $ce("div");
						var selectedCenterDiv = $ce("div");
						var selectedRightDiv = $ce("div");
						selectedLeftDiv.className = classNamePre + "selectedleft";
						selectedCenterDiv.className = classNamePre + "selectedcenter";
						selectedRightDiv.className = classNamePre + "selectedright";
						selectedCenterDiv.innerHTML = (i + 1);
						a.appendChild(selectedLeftDiv);
						a.appendChild(selectedCenterDiv);
						a.appendChild(selectedRightDiv);
					}
					this.paginationContent.appendChild(a);
				}else if(i == endIndex + 1){
					var a = $ce("A");
					var selectedDiv = $ce("div");
					selectedDiv.className = "unselected";
					selectedDiv.innerHTML = "...";
					a.appendChild(selectedDiv);
					this.paginationContent.appendChild(a);
				}
			}
		}
		
		var smartCutDivLast = $ce("div");
		smartCutDivLast.className = "smartcut";
		this.paginationContent.appendChild(smartCutDivLast);
		
		var pageLast = $ce("A");
		pageLast.onclick = GridComp.pageLast;
		pageLast.href = "#";
		pageLast.gridId = this.id;
		pageLast.pageIndex = pageIndex;
		var selectedCenterDiv = $ce("div");
		selectedCenterDiv.className = "pagelast";
		selectedCenterDiv.innerHTML = trans("ml_pagelast")/*"末页"*/;
		pageLast.appendChild(selectedCenterDiv);
		this.paginationContent.appendChild(pageLast);
		
		var nextDiv = $ce("div");
		nextDiv.className = "next";
		nextDiv.onmouseover = GridComp.nextMouseOver;
		nextDiv.onmouseout = GridComp.nextMouseOut;
		var nextA = $ce("A");
		nextA.onclick = GridComp.pageNext;
		nextA.href = "#";
		nextA.gridId = this.id;
		nextA.pageIndex = pageIndex;
		nextA.appendChild(nextDiv);
		this.paginationContent.appendChild(nextA);
		var wholeWidth = this.paginationContent.offsetWidth + 25 + 20 + this.paginationMessage.offsetWidth;
		this.paginationPanel.style.width = wholeWidth + "px";
		if(this.outerDiv.offsetWidth < wholeWidth){
			this.outerDiv.removeChild(this.paginationBar);
			this.isSimplePagination = true;
			this.initSimplePaginationBar();
			this.processPaginationInfo(pageIndex, rowCount, pageCount, pageRowCount);
		}
		
	}
	return;
}; 

/**
 * 创建表头区整体div
 * 
 * @private
 */
GridComp.prototype.initHeaderDiv = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	this.headerDiv = $ce("div");
	this.headerDiv.className = "headerbar_div";
	if(this.hasBorder)
		$(this.headerDiv).addClass("headerbarDivExtendCss");
	// this.headerDiv.style.border = "solid red 1px";
	// 根据是否显示分页条调整top
	if (this.pageSize > 0 && this.isPagenationTop == true)
		this.headerDiv.style.top = GridComp.PAGEBAR_HEIGHT + "px";
	else
		this.headerDiv.style.top = "0px";
	this.outerDiv.appendChild(this.headerDiv);

	// 表头不显示菜单
	this.headerDiv.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	if (this.vScroll) {
		this.headerDiv.style.width = (gridWidth - GridComp.SCROLLBAE_HEIGHT)
				+ "px";// - 2;
		// 记录headerDiv的原始宽度
		this.headerDiv.defaultWidth = gridWidth - GridComp.SCROLLBAE_HEIGHT;// - 2;
	} else {
		this.headerDiv.style.width = gridWidth + "px";// - 2;
		// 记录headerDiv的原始宽度
		this.headerDiv.defaultWidth = gridWidth;// - 2;
	}
	this.constant.headerDivWidth = this.headerDiv.defaultWidth;
	this.headerDiv.style.height = this.constant.headerHeight + "px";
	if (!this.isShowHeader)
		this.headerDiv.style.visibility = "hidden";
};

/**
 * 创建固定表头区整体div
 * 
 * @private
 */
GridComp.prototype.initFixedHeaderDiv = function() {
	this.fixedHeaderDivWidth = 0;
	// 得到固定表头的宽度
	var headers = this.model.getHeaders();
	for (var i = headers.length - 1; i >= 0; i--) {
		if (headers[i].isFixedHeader)
			this.fixedHeaderDivWidth += this.getHeaderDefaultWidth(headers[i]);
	}
	// 加上固定选择列的宽度
	if (this.isMultiSelWithBox)
		this.fixedHeaderDivWidth += (GridComp.SELECTCOLUM_WIDTH); // 

	this.fixedHeaderDiv = $ce("div");
	this.headerDiv.appendChild(this.fixedHeaderDiv);
	// this.fixedHeaderDiv.className = "headerdiv fixedheaderdiv";
	this.fixedHeaderDiv.style.height = this.constant.headerHeight + "px";
	this.fixedHeaderDiv.style.width = this.fixedHeaderDivWidth + "px";
	this.fixedHeaderDiv.style.left = "0px";
	this.fixedHeaderDiv.style.top = "0px";
	this.fixedHeaderDiv.style.position = "absolute";
	this.fixedHeaderDiv.style.zIndex = getZIndex();
	// 将固定表头值放入常量对象
	this.constant.fixedHeaderDivWidth = this.fixedHeaderDiv.offsetWidth;
};

/**
 * 创建静态表头区行标区表头div
 * 
 * @private
 */
GridComp.prototype.initRowNumHeaderDiv = function() {
	this.rowNumHeaderDiv = $ce("div");
	this.fixedHeaderDiv.appendChild(this.rowNumHeaderDiv);
	this.rowNumHeaderDiv.className = "row_num_header_div";
	this.rowNumHeaderDiv.style.height = (this.constant.headerHeight - 1) + "px";

	var width = this.rowNumHeaderDiv.offsetWidth;
	// 将行号表头区的宽度放入constant常量中,供后续使用
	this.constant.rowNumHeaderDivWidth = width;
	// 如果启用了fixedHeaderDiv则需要改变fixedHeaderDiv的宽度
	this.fixedHeaderDiv.style.width = (width + this.fixedHeaderDivWidth) + "px";
	this.constant.fixedHeaderDivWidth = width + this.fixedHeaderDivWidth;
};

/**
 * 创建表格最左侧功能设置触发区
 * 
 * @private
 */
GridComp.prototype.initLineStateHeaderDiv = function() {
	this.constant.lineStateHeaderDivWidth = 0;
	return;
};

/**
 * 创建固定表头区固定选择列header
 * 
 * @private
 */
GridComp.prototype.initSelectColumHeaderDiv = function() {
	var oThis = this;
	this.selectColumHeaderDiv = $ce("div");
	var div = this.selectColumHeaderDiv;
	this.fixedHeaderDiv.appendChild(div);
	div.id = "fixedSelectColumHeader";
	div.className = "select_headerdiv";
	div.style.overflow = "hidden"; 
	if(this.hasBorder)
		$(div).addClass("selectheaderdivExtendCss");
	div.style.left = (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth)
			+ "px";
	if(this.isShowHeader)
		div.style.height = (this.constant.headerHeight - 1) + "px";
	else
		div.style.height = "0px"; 
//	// 如果多表头,表头高度会超过两倍表头高,这时要计算paddingTop让checkbox聚中显示
//	if (this.constant.headerHeight > GridComp.HEADERROW_HEIGHT)
//		div.style.paddingTop = parseInt((this.constant.headerHeight - 20) / 2)
//				+ "px";
	// 多选模式下的全选checkbox
	if(this.isMultiSelectShow){
		this.selectAllBox = $ce("INPUT");
		this.selectAllBox.type = "checkbox";
		div.appendChild(this.selectAllBox);
		this.selectAllBox.defaultChecked = false;
		this.selectAllBox.checked = false;
		this.selectAllBox.style.display = "block";
		this.selectAllBox.style.marginTop = "10px";
		this.selectAllBox.style.marginLeft= "8px";
		this.selectAllBox.onclick = function(e) {
			e = EventUtil.getEvent();
			// 置空当前选择单元格
			oThis.selectedCell = null;
			oThis.oldCell = null;
			// 隐藏编辑框
			oThis.hiddenComp();
			oThis.expandAllNodes();
			// 取消全选
			if (this.checked == false) {
				var indices = new Array;
				for (var i = 0, count = oThis.model.getRowsCount(); i < count; i++) {
					indices.push(i);
					// oThis.model.setRowUnSelected(i);
				}
				oThis.model.setRowUnSelected(indices);
			}
			// 全选
			else {
				var count = oThis.selectColumDiv.children.length;
				for(var i=count-1; i>=0; i--){
					//触发每一行checkbox事件
					if(oThis.selectColumDiv.children[i] && oThis.selectColumDiv.children[i].children[0].checked == false){
						oThis.selectColumDiv.children[i].children[0].onmousedown(e);
					}
				}
				this.checked = true;
				/*var indices = new Array;
				for (var i = 0, count = oThis.model.getRowsCount(); i < count; i++) {
					indices.push(i);
				}
				oThis.model.addRowSelected(indices);*/
			}
			// 取消焦点行
			oThis.loseFocusIndex();
			
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
	}
};

/**
 * 创建固定表头区header数据区div
 * 
 * @private
 */
GridComp.prototype.initFixedHeaderTableDiv = function() {
	var currLeft = this.constant.rowNumHeaderDivWidth
			+ this.constant.lineStateHeaderDivWidth;
	if (this.isMultiSelWithBox)
		currLeft = currLeft + this.selectColumHeaderDiv.offsetWidth;
	this.fixedHeaderTableDiv = $ce("div");
	this.fixedHeaderDiv.appendChild(this.fixedHeaderTableDiv);
	// this.fixedHeaderTableDiv.className = "fixed_headertable_div";
//	this.fixedHeaderTableDiv.style.position = "absolute";
	this.fixedHeaderTableDiv.style.overflow = "hidden";
	this.fixedHeaderTableDiv.style.top = "0px";
	this.fixedHeaderTableDiv.style.left = currLeft + "px";
	this.fixedHeaderTableDiv.style.height = this.constant.headerHeight + "px";
	if (this.constant.fixedHeaderDivWidth - currLeft > 0)
		this.fixedHeaderTableDiv.style.width = (this.constant.fixedHeaderDivWidth - currLeft) + "px";
	// 将固定表头区header数据区的宽度放入常量对象
	this.constant.fixedHeaderTableDivWidth = this.constant.fixedHeaderDivWidth
			- currLeft;
};

/**
 * 创建动态表头区整体div
 * 
 * @private
 */
GridComp.prototype.initDynamicHeaderDiv = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	var fixedHeaderWidth = this.constant.fixedHeaderDivWidth;
	this.dynamicHeaderDiv = $ce("div");
	this.headerDiv.appendChild(this.dynamicHeaderDiv);
	//this.dynamicHeaderDiv.className = "headerbar_div";
	// this.dynamicHeaderDiv.style.border = "solid red 1px";
	this.dynamicHeaderDiv.style.position = "absolute";
	this.dynamicHeaderDiv.style.left = fixedHeaderWidth + "px";
	this.dynamicHeaderDiv.style.height = this.constant.headerHeight + "px";
	if (this.vScroll) {
		var dynHeaderWidth = gridWidth - fixedHeaderWidth
				- GridComp.SCROLLBAE_HEIGHT;// - 5;
		if (dynHeaderWidth > 0) {
			this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
		} else {
			this.dynamicHeaderDiv.style.width = "0px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
		}
	} else {
		var dynHeaderWidth = gridWidth - fixedHeaderWidth;// - 3;
		if (dynHeaderWidth > 0) {
			this.dynamicHeaderDiv.style.width = (gridWidth - fixedHeaderWidth)
					+ "px";// - 3;
			this.dynamicHeaderDiv.defaultWidth = gridWidth - fixedHeaderWidth;// - 3;
		} else {
			this.dynamicHeaderDiv.style.width = "0px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
		}
	}
};

/**
 * 创建动态表头区header数据区div
 * 
 * @private
 */
GridComp.prototype.initDynamicHeaderTableDiv = function() {
	// 包装tables的包容div
	this.dynamicHeaderTableDiv = $ce("div");
	this.dynamicHeaderDiv.appendChild(this.dynamicHeaderTableDiv);
	// this.dynamicHeaderTableDiv.className = "dynamic_headertable_div";
	// this.dynamicHeaderTableDiv.style.border = "solid blue 1px";
	this.dynamicHeaderTableDiv.style.left = "0px";
//	this.dynamicHeaderTableDiv.style.position = "absolute";
	this.dynamicHeaderTableDiv.style.height = this.constant.headerHeight + "px";
	this.dynamicHeaderTableDiv.style.width = this.getDynamicTableDivRealWidth(true) + "px";
};

/**
 * 根据生成的headers model画固定和动态header内的table
 * 
 * @private
 */
GridComp.prototype.initHeaderTables = function() {
	// 表头的总深度
	this.headerDepth = this.getHeaderDepth();
	// 将动态表头和固定表头放在以下两个数组中
	this.defaultFixedHeaders = new Array();
	this.defaultDynamicHeaders = new Array();
	var headers = this.model.getHeaders();
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].isFixedHeader)
			this.defaultFixedHeaders.push(headers[i]);
		else if (headers[i].isFixedHeader == false)
			this.defaultDynamicHeaders.push(headers[i]);
	}
	// TODO:目前对于多表头先不允许拖动
	for (var i = 0, count = headers.length; i < count; i++)
		this.initHeaderTable(headers[i]);
};

/**
 * @private
 */
GridComp.prototype.getFirstVisibleHeader = function() {
	var headers = this.model.getHeaders();
	for (var i = 0; i < headers.length; i++) {
		if (headers[i].isHidden == false)
			return headers[i];
	}
	return null;
};

/**
 * @private
 */
GridComp.prototype.getLastVisibleHeader = function() {
	var headers = this.model.getHeaders();
	for (var i = headers.length - 1; i >= 0; i--) {
		if (headers[i].isHidden == false)
			return headers[i];
	}
	return null;
};

/**
 * 得到动态表头的第一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getFirstDynamicVisibleHeader = function() {
	if (this.defaultDynamicHeaders == null
			|| this.defaultDynamicHeaders.length == 0)
		return null;
	for (var i = 0, count = this.defaultDynamicHeaders.length; i < count; i++) {
		if (this.defaultDynamicHeaders[i].isHidden == false)
			return this.defaultDynamicHeaders[i];
	}
};

/**
 * 得到动态表头的最后一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getLastDynamicVisibleHeader = function() {
	if (this.defaultDynamicHeaders == null
			|| this.defaultDynamicHeaders.length == 0)
		return null;
	for (var i = this.defaultDynamicHeaders.length - 1; i >= 0; i--) {
		if (this.defaultDynamicHeaders[i].isHidden == false)
			return this.defaultDynamicHeaders[i];
	}
};

/**
 * 得到固定表头的第一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getFirstFixedVisibleHeader = function() {
	if (this.defaultFixedHeaders == null
			|| this.defaultFixedHeaders.length == 0)
		return null;
	for (var i = 0, count = this.defaultFixedHeaders.length; i < count; i++) {
		if (this.defaultFixedHeaders[i].isHidden == false)
			return this.defaultFixedHeaders[i];
	}
};

/**
 * 得到固定表头的最后一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getLastFixedVisibleHeader = function() {
	if (this.defaultFixedHeaders == null
			|| this.defaultFixedHeaders.length == 0)
		return null;
	for (var i = this.defaultFixedHeaders.length - 1; i >= 0; i--) {
		if (this.defaultFixedHeaders[i].isHidden == false)
			return this.defaultFixedHeaders[i];
	}
};

/**
 * 得到指定header的下一个显示的basic header
 * 
 * @private
 */
GridComp.prototype.getNextVisibleBasicHeader = function(header) {
	if (this.basicHeaders == null)
		throw new Error("basicHeaders为null!");
	if (header == null)
		return null;
	for (var i = 0; i < this.basicHeaders.length; i++) {
		if (this.basicHeaders[i] == header && this.basicHeaders[i + 1] != null) {
			for (var j = i + 1; j < this.basicHeaders.length; j++) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j];
			}
		}
	}
	return null;
};

/**
 * 得到指定header的上一个显示的basic header
 * 
 * @private
 */
GridComp.prototype.getLastVisibleBasicHeader = function(header) {
	if (this.basicHeaders == null)
		throw new Error("basicHeaders为null!");
	if (header == null)
		return null;
	for (var i = 0; i < this.basicHeaders.length; i++) {
		if (this.basicHeaders[i] == header) {
			for (var j = i - 1; j >= 0; j--) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j];
			}
		}
	}
	return null;
};

/**
 * 为boolean列创建全选checkbox(提出此方法主要为了代码复用)
 * 
 * @private
 */
GridComp.prototype.createCheckBoxForSelAll = function(header) {
	var oThis = this;
	var selectBox = $ce("INPUT");
	selectBox.type = "checkbox";
	selectBox.style.verticalAlign = "middle";
	selectBox.style.marginTop = "0";
	selectBox.defaultChecked = false;
	selectBox.checked = false;
	//selectBox.style.display = "block";
	// 设置checkbox的禁用状态
	if (this.editable == false || header.columEditable == false)
		selectBox.disabled = true;

	selectBox.onmousedown = function(e) {
		// 置空当前选择单元格
		oThis.selectedCell = null;
		oThis.oldCell = null;
		// 隐藏编辑框
		oThis.hiddenComp();
		var ds = oThis.model.dataset;
		var colIndex = ds.nameToIndex(header.keyName);
		var dsRows = ds.getRows(null);
		if (header.valuePair == null || header.valuePair[1] == null)
			return;

		// 取消全选
		if (this.checked) {
			if (dsRows != null) {
				var rowIndices = new Array();
				var values = new Array();
				for (var i = 0; i < dsRows.length; i++) {
					rowIndices.push(i);
					values.push(header.valuePair[1]);
					// ds.setValueAt(i, colIndex, header.valuePair[1]);
				}
				ds.setValuesAt(rowIndices, colIndex, values);

			}
			this.checked = false;
		}
		// 全选
		else {
			if (dsRows != null) {
				var rowIndices = new Array();
				var values = new Array();
				for (var i = 0; i < dsRows.length; i++) {
					rowIndices.push(i);
					values.push(header.valuePair[0]);
					// ds.setValueAt(i, colIndex, header.valuePair[0]);
				}
				ds.setValuesAt(rowIndices, colIndex, values);
			}
			this.checked = true;
		}
	};
	// 并阻止事件的进一步向上传播
	selectBox.onclick = function(e) {
		e = EventUtil.getEvent();
		// 阻止默认选中事件和事件上传,放在onmousedown中不会起作用,但通知model行选中必须放在onmousedown中!
		stopDefault(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	return selectBox;
};

/**
 * 绘制每个header内的table
 * 
 * @param header
 *            this.headers数组中的header
 * @param isLastHeader
 *            是否最后一个header
 * @private
 */
GridComp.prototype.initHeaderTable = function(header) {
	// 隐藏列不创建
	if (header.isHidden || this.isLocalHiddenColumn(header.keyName))
		return;
	var oThis = this;
	var tempDiv = null;
	// 得到header的总深度
	var totalDepth = header.getDepth();
	// 如果为多表头，且 header总深度小于2，说明 多表头的子全部隐藏了，不创建此多表头
	if (header.children != null && totalDepth < 2)
		return;
	// 得到此header的总宽度作为table的宽度
	var tableWidth = this.getHeaderDefaultWidth(header);
	// 创建table
	header.dataTable = $ce("table");
	header.dataTable.headerOwner = header;
	var headerTable = header.dataTable;

	if (header.isGroupHeader != true) {
		// 处理表头拖动
		header.dataTable.refGrid = this;

		header.dataTable.onmousedown = GRID_INIT;
		if (!IS_IE)
			this.headerDiv.onmouseup = GRID_END;
		header.dataTable.onmouseup = GRID_END;
		// 存储这两个引用直接供GRID_DRAG内部判断鼠标的位置区域时使用
		header.dataTable.refHeader = header;
		if (!IS_IE)
			this.headerDiv.onmousemove = GRID_DRAG;
		header.dataTable.onmousemove = GRID_DRAG;
	}

	// 根据header是固定还是动态,将header添加到不同的div中
	if (header.isFixedHeader == false)
		this.dynamicHeaderTableDiv.appendChild(header.dataTable);
	else
		this.fixedHeaderTableDiv.appendChild(header.dataTable);
	header.dataTable.style.height = this.constant.headerHeight + "px";
	header.dataTable.style.width = tableWidth + "px";
	header.dataTable.cellPadding = "0px";
	header.dataTable.cellSpacing = "0px";
	// 创建tbody
	var oTBody = $ce("tbody");
	header.dataTable.appendChild(oTBody);

	// 单表头情况
	if (header.children == null) {
		headerTable.className = "headerdiv";

		// 设置表头颜色
		if (header.required)
			headerTable.className += " header_required";
		else {
			// 根据header中的textcolor属性设置文字颜色
			if (header.textColor != null && header.textColor != "")
				headerTable.style.color = header.textColor;
		}

		var row = this.addTableRow(oTBody, null);
		var cell = row.insertCell(-1);
		cell.width = header.width;
//		cell.height = this.constant.headerHeight + "px";
		// 保存cell的引用在
		header.cell = cell;

		var selectBox = null;
		// 如果是默认的boolean渲染类型,或者编辑类型为EditorType.CHECKBOX,则创建全选checkbox
		if (header.renderType == BooleanRender
				|| header.editorType == EditorType.CHECKBOX
				|| (header.dataType == DataType.UFBOOLEAN && header.editorType != EditorType.STRINGTEXT)) {
			if (header.isShowCheckBox)		
				selectBox = this.createCheckBoxForSelAll(header);
		}

		// 将header的引用绑定在cell上
		cell.owner = header;
		tempDiv = $ce("div");
		tempDiv.className = "tempDiv";
		if(this.hasBorder)
			$(tempDiv).addClass("tempDivExtendCss");
		if (IS_IE7)
			tempDiv.style.overflow="hidden";
		if(header.width && header.width > 0){
			tempDiv.style.width = header.width - 1 +"px";
		}
		cell.appendChild(tempDiv);
		// 将放置内容的div引用绑定header上
		header.contentDiv = tempDiv;
		// 在header上保存grid的引用
		header.owner = this;

		// var padding = Math.floor((cell.offsetHeight - getTextHeight(
		// header.showName, headerTable.className)) / 2);
		// if (padding < 0)
		// padding = 0;

		header.textNode = $ce("DIV");
		header.textNode.id = this.id + "_" +header.keyName;
		//内容太长时，显示省略号
		header.textNode.style.whiteSpace = "nowrap";
		header.textNode.style.overflow = "hidden";
		header.textNode.style.textOverflow = "ellipsis";
		//提示信息来显示全部内容
		header.textNode.title = header.showName;
		
		// add by renxh 为表头添加编辑态
		var widgetid;
		try{
			widgetid = ((header.owner.widget) ? header.owner.widget.id : "");
		}catch(e){
			widgetid = "";
		}
		
		var params = {
			uiid : "",
			eleid : this.id,
			type : "grid_header",
			widgetid : widgetid,
			subeleid : header.id
		};
		if (window.editMode) {
			new EditableListener(header.textNode, params,
					EditableListener.COMPOMENT_TYPE);
		}
		tempDiv.appendChild(header.textNode);
		if (!header.required){
			//header.required = true;
			//header.textNode.innerHTML = '<span style="color:red;margin-right:3px;">*</span>';
		}
		if (selectBox != null) {
			// 只有header的高度为2倍表头高度以上才设置此时padding
			// if (cell.offsetHeight > GridComp.HEADERROW_HEIGHT * 2)
			// tempDiv.style.paddingTop = (padding - 4) + "px";
			// tempDiv.appendChild(selectBox);
			header.selectBox = selectBox;
			header.textNode.appendChild(selectBox);
		} else {
			// tempDiv.style.paddingTop = padding + "px";
			// tempDiv.appendChild(document.createTextNode(header.showName));
		}
		
		if(typeof(header.renderType) != "undefined" && header.renderType != null && typeof(header.renderType.headerRender) == "function"){
			header.renderType.headerRender.call(this, header.textNode, header.showName);
		}else{
			header.textNode.appendChild(document.createTextNode(header.showName));
		}

		// tempDiv.onmousedown = function(e)
		// {
		// if(!e)
		// e = window.event;
		// stopAll(e);
		// }

		// 显示"排序箭头",并排序,按着ctrl点击表头支持多列排序
		if (this.sortable && header.sortable && this.model.treeLevel == null) {
			tempDiv.onclick = function(e) {
				
				e = EventUtil.getEvent();
				var tag = e.target;
				// 当点击的为显示/隐藏时不进行排序
				if(tag.id == "columnSettingDiv"){
					stopAll(e);
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
				}else{
					// tempdiv相对body的left
					var offsetLeft = compOffsetLeft(this, document.body);
					// 当前鼠标的位置
					var currX = e.clientX + document.body.scrollLeft;
					if (currX > offsetLeft + 15
							&& currX < offsetLeft + this.offsetWidth - 15) {
						// 未按ctrl键
						if (!e.ctrlKey) {
							if (oThis.sortHeaders != null) {
								var headerDiv = null;
								for (var i = 0, count = oThis.sortHeaders.length; i < count; i++) {
									headerDiv = oThis.sortHeaders[i].contentDiv;
									if (headerDiv.sortImg.parentNode)
										headerDiv.sortImg.parentNode
												.removeChild(headerDiv.sortImg);
								}
	
								// 清除多列排序记录数组
								while (oThis.sortHeaders.length != 0) {
									oThis.sortHeaders.shift().contentDiv.sortImg = null;
								}
							}
							// 如果当前排序header不为空,且当前排序header不为现在点击的header,则先将当前排序header的图标隐藏
							if (oThis.sortHeader != null
									&& oThis.sortHeader != header) {
								var lastHeaderDiv = oThis.sortHeader.contentDiv;
								if(lastHeaderDiv.sortImg){
									lastHeaderDiv.sortImg.parentNode
											.removeChild(lastHeaderDiv.sortImg);
									lastHeaderDiv.sortImg = null;
								}
							}
						}
						// 按着ctrl键
						else {
							// 如果有上次单击的排序列,首先清除上次的单击排序列图标
							if (oThis.sortHeader != null
									&& oThis.sortHeader != header) {
								var lastHeaderDiv = oThis.sortHeader.contentDiv;
								lastHeaderDiv.sortImg.parentNode
										.removeChild(lastHeaderDiv.sortImg);
								lastHeaderDiv.sortImg = null;
								oThis.sortHeader = null;
							}
						}
	
						if (this.sortImg == null) {
							this.sortImg = $ce("img");
							this.sortImg.className = "sort_img";
							this.sortImg.src = window.themePath
									+ "/ui/ctrl/grid/images/up_arrow.png";
							tempDiv.appendChild(this.sortImg);
							// 记录当前的排序方向
							header.ascending = -1;
						} else {
							if (header.ascending == -1) {
								this.sortImg.src = window.themePath
										+ "/ui/ctrl/grid/images/down_arrow.png";
								header.ascending = 1;
							} else if (header.ascending == 1) {
								this.sortImg.src = window.themePath
										+ "/ui/ctrl/grid/images/up_arrow.png";
								header.ascending = -1;
							}
						}
	
						// 未按ctrl键
						if (!e.ctrlKey) {
							oThis.model.sortable([header], null, null);
							// 记录当前排序列
							oThis.sortHeader = header;
						}
						// 按着ctrl键
						else {
							// 记录需要排序的多列表头,按点击顺序记录
							if (oThis.sortHeaders == null)
								oThis.sortHeaders = new Array();
							oThis.sortHeaders.push(header);
						}
					}
					stopAll(e);
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
				}
			};

			// ctrl键弹起时排序多列
			tempDiv.onkeyup = function(e) {
				e = EventUtil.getEvent();

				if (e.lfwKey == 17) {
					// if(oThis.sortHeaders != null)
					// oThis.sortHeaders.clear();
				}
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
			};
		}
		headerTable.onmouseover = function(e) {
			if(!e)
				e = window.event;
			if(window.dragStart)
				return;
			if (window.editMode)
				return;
			if (header.contentDiv.columnSettingDiv == null){
				var columnSettingDiv = $ce('DIV');
				columnSettingDiv.id = "columnSettingDiv";
				columnSettingDiv.onmousedown = function(e){
					e = EventUtil.getEvent();
					var ee = {};
					ee.clientX = e.clientX;
					ee.clientY = e.clientY;
					ee.type = e.type;
					setTimeout(function(){oThis.hideenColumnContentMenu();
										oThis.showColumnContentMenu(ee);
										stopEvent(ee);},200);
				};
				columnSettingDiv.onmouseup = function(e){
					if(IS_CHROME){
						setTimeout(function(){destroyDargObj();},200);
					}
				};
				// IE9移至columnSettingDiv不应该显示title
				columnSettingDiv.onmouseover = function(e){
					if(IS_IE9){
						header.contentDiv.title="";
					}
				};
				columnSettingDiv.onmouseout = function(e){
					if(IS_IE9){
						header.contentDiv.title=header.showName;
					}
				};
				columnSettingDiv.className = "columnSetting";
				columnSettingDiv.style.position = "absolute";
				columnSettingDiv.style.marginRight = "5px";
				if(oThis.hasBorder)
					$(columnSettingDiv).addClass("columnSettingExtendCss");
				header.contentDiv.appendChild(columnSettingDiv);
				header.contentDiv.columnSettingDiv = columnSettingDiv; 
			}		 
			header.contentDiv.columnSettingDiv.style.zIndex = getZIndex();
			header.contentDiv.columnSettingDiv.style.display = "block";
//			//初始化列头菜单
			oThis.initColumnContextMenu();
		};

		headerTable.onmouseout = function(e) {
			if(!e)
				e = window.event;
			if(window.dragStart)
				return;
			if (header.contentDiv.columnSettingDiv != null)
				header.contentDiv.columnSettingDiv.style.display = "none";
			// if(e.srcElement.nodeName != 'DIV')
			// return;
			// this.className = this.oldClassName;
		};
		
		
		
	}
	// 多表头情况
	else {
		headerTable.className = "multiheaderdiv";
		/* colspan,rowspan计算公式 */
		// colspan: td跨几列
		// rowspan: td跨几行 总体深度 - header所在层数(从0开始) - 孩子层数
		var tempHeaders = new Array();
		// totalDepth即为行数
		for (var i = 0; i < totalDepth; i++) {
			var row = header.dataTable.insertRow(i);
			// 得到这一行内的所有列(不包括隐藏列)
			tempHeaders = header.getVisibleHeadersByLevel(i);
			for (var j = 0; j < tempHeaders.length; j++) {
				// TODO 判断是否隐藏
				var tempHeader = tempHeaders[j];
				var cell = row.insertCell(-1);
				cell.className = "multiheadercell";
				// 将header的引用绑定在cell上
				cell.owner = tempHeader;
				cell.rowSpan = tempHeader.getRowspan(totalDepth);
				cell.colSpan = tempHeader.getColspan();
				var selectBox = null;
				var tempDiv = $ce("div");
				if (tempHeader.children == null) {
					var headerLevel = tempHeader.getHeaderLevel();
					// 不是多表头中最底层的header
					if (headerLevel != totalDepth - 1)
						cell.height = (this.headerDepth - tempHeader
								.getHeaderLevel())
								* this.headerRowHeight;
					else
						cell.height = this.headerRowHeight - 1;

					// 如果是默认的boolean渲染类型,则创建全选checkbox(注:对于用户自己写的boolean渲染器不会创建全选checkbox)
					if (tempHeader.renderType == BooleanRender
						|| tempHeader.editorType == EditorType.CHECKBOX
						|| (tempHeader.dataType == DataType.UFBOOLEAN && tempHeader.editorType != EditorType.STRINGTEXT)){
						if (tempHeader.isShowCheckBox)	
							selectBox = this.createCheckBoxForSelAll(tempHeader);
					}						
				} else {
					// cell.className = "multiheaderdiv";
					// 第0层header应有高度(header总深-孩子层数)*this.headerRowHeight
					if (tempHeader == header)
						cell.height = (this.headerDepth - header
								.getHeaderChildrenLevel())
								* this.headerRowHeight;
					else
						cell.height = this.headerRowHeight - 1;
				}

				if (j != 0)
					cell.width = tempHeader.width - 1;
				else
					cell.width = tempHeader.width;
				// 将放置内容的div引用绑定header上
				tempHeader.contentDiv = tempDiv;
				// 在header上保存grid的引用
				tempHeader.owner = this;

				tempHeader.textNode = $ce("DIV");
				tempDiv.appendChild(tempHeader.textNode);

				/*
				 * div上不设置width if (tempHeader.children == null)
				 * tempDiv.style.width = tempHeader.width + "px"; else {
				 * //得到此header的真实宽度 // 得到此header下的所有basicHeaders var ownerBasics =
				 * tempHeader.getBasicHeadersBySpecify(); var realWidth = 0; for (
				 * var k = ownerBasics.length - 1; k >= 0; k--) realWidth +=
				 * ownerBasics[k].width; tempDiv.style.width = realWidth + "px"; }
				 */
				tempDiv.title = tempHeader.showName;

				if (selectBox != null) {
					// tempDiv.appendChild(selectBox);
					header.selectBox = selectBox;
					tempHeader.textNode.appendChild(selectBox);
					// tempDiv.appendChild(document.createTextNode(tempHeader.showName));
				} else {
					// tempDiv.appendChild(document.createTextNode(tempHeader.showName));
				}
				if(typeof(tempHeader.renderType) != "undefined" && typeof(tempHeader.renderType.headerRender) == "function"){
					tempHeader.renderType.headerRender.call(this, tempHeader.textNode, tempHeader.showName);
				}else{
					tempHeader.textNode.appendChild(document.createTextNode(tempHeader.showName));
				}
				cell.appendChild(tempDiv);
			}
		}
		tempHeaders = null;
	}

	// 固定列的设置top,right边界
	if (header.isFixedHeader) {
		headerTable.className += " fixedheaderdiv";
	}

	// 列的第一个header不设置left边界(包括fixed和dynamic) TODO 这里需要判断未隐藏的固定列是否>0
	if (header == this.getFirstFixedVisibleHeader()
			|| header == this.getFirstDynamicVisibleHeader()) {
		headerTable.style.borderLeftWidth = "0px";
	}
	// headers最后一个去掉右边界
	if (header == this.getLastFixedVisibleHeader()
			|| header == this.getLastDynamicVisibleHeader()) {
		tempDiv.style.borderRightWidth = "0px";
		headerTable.style.borderRightWidth = "0px";
	}
};

/**
 * 创建信息提示区
 */
GridComp.prototype.initNoRowsDiv = function(){
	this.noRowsDiv = $ce("DIV");
	// 流式布局保持原来的处理，非流式布局由于数据DIV高度是固定的，会将数据DIV顶下去因此需要修改position
	if(this.flowmode){
		this.noRowsDiv.style.position = "relative";
		this.noRowsDiv.style.marginTop = "10px";
	}else{
		this.noRowsDiv.style.position = "absolute";
		this.noRowsDiv.style.width = "100%";
		this.noRowsDiv.style.marginTop = "3px";
	}
	this.noRowsDiv.style.marginBottom = "10px";
	this.noRowsDiv.style.textAlign = "center";
	this.noRowsDiv.innerHTML = trans("ml_grid_norow");
	this.noRowsDiv.style.display = "none";
	this.outerDiv.appendChild(this.noRowsDiv);
};

/**
 * 设置grid提示信息
 */
GridComp.prototype.setGridTipContent = function(html){
	this.noRowsDiv.innerHTML = html;
	this.noRowsDiv.style.display = "";
	this.dynamicColumDataDiv.style.marginBottom = "0px";
	//由于自定滚动条所以设置为hidden
	this.dataOuterDiv.style.overflow = "hidden";
	this.dataOuterDiv.style.display = "block";
};

/**
 * @private
 */
function GRID_INIT(e) {
	if (window.editMode){
		// 设计器里屏蔽表格拖动
		return;
	}
	e = EventUtil.getEvent();
	// 某些情况下会导致移动div没有消失，在最开始先destory
	destroyDargObj();
	var grid = this.refGrid;
	// 首先隐藏掉上一个显示出的控件
	if (grid.showComp != null)
		grid.hiddenComp();

	// 得到触发源(header.contentDiv)
	var src = getTarget(e);
	if (src.tagName != null && (src.tagName.toLowerCase() == "input" || src.tagName.toLowerCase() == "img")) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}

	// 触发源div相对body的left
	var offsetLeft = compOffsetLeft(src, document.body);
	var outerDivScrollLeft = grid.dataOuterDiv.scrollLeft;
	// 当前鼠标的位置
	var currX = e.clientX + document.body.scrollLeft;
	var dragSrc = getDragSrc(src);

	// 获得触发源所在的header
	var curHeader = dragSrc.owner;
	if (curHeader == null) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	// 获得grid的引用
	window.gridOwner = curHeader.owner;

	// 鼠标在以下这两个区域内才能拖拽
	if (currX > offsetLeft + parseInt(dragSrc.width) - 5 && currX < offsetLeft + parseInt(dragSrc.width)) {
		// 保存真正处理拖动的header
		window.dragHeader = curHeader;
		window.src = src;
		window.dragStart = true;
		window.src.style.cursor = 'col-resize';
		if (curHeader.contentDiv.columnSettingDiv != null)
			curHeader.contentDiv.columnSettingDiv.style.display = "none";
		window.dragType = 'changeWidth';	
	}
	//列头调换
	else if (currX > offsetLeft && currX < offsetLeft + parseInt(dragSrc.width) - 5){
		// 保存真正处理拖动的header
		window.dragHeader = curHeader;
		window.src = src;
		window.dragStart = true;
//		if (curHeader.contentDiv.columnSettingDiv != null)
//			curHeader.contentDiv.columnSettingDiv.style.display = "none";
		window.dragType = 'swapColumn';	
		window.dragSrcClone = dragSrc.cloneNode(true);
		window.dragSrcClone.style.position = "absolute";
		window.dragSrcClone.style.height = dragSrc.offsetHeight + "px";
		window.dragSrcClone.style.width = dragSrc.offsetWidth + "px";
		window.dragSrcClone.style.filter = "Alpha(Opacity=30)";
		window.dragSrcClone.style.MozOpacity = 0.3;
		document.body.appendChild(dragSrcClone);
		window.dragSrcClone.style.display="none";
		var flagStyle = window.dragSrcClone.style;
		flagStyle.left = (e.clientX + 3) + "px";
		flagStyle.top = (e.clientY + 3) + "px";
		
	}
	// cell(td)
	window.dragSrc = dragSrc;
	// 开始拖动时的X坐标
	window.dragSrcX = e.clientX + document.body.scrollLeft;
	// 拖动表头的原始宽度
	window.defaultHeaderWidth = parseInt(window.dragSrc.width);
	// 动态数据区的原始宽度
	window.dynamicColumDataDivWidth = window.gridOwner.dynamicColumDataDiv.offsetWidth;// + 2;
	// 动态表头的初始宽度
	window.defaultDynamicHeaderWidth = window.gridOwner.getDynamicTableDivRealWidth(true)+ 2;
	window.defaultDynHeaderTableWidth = window.gridOwner.dynamicHeaderTableDiv.offsetWidth;// + 2;
	// 保存此表头的允许最小宽度
	if (window.src != null &&  window.src.firstChild != null) {
		if (window.src.firstChild.tagName != null && window.src.firstChild.tagName.toLowerCase() == "input")
			window.minWidth = getTextWidth(window.src.childNodes[1].nodeValue,window.src.className) + 25;
		else
			window.minWidth = getTextWidth(window.src.innerHTML,window.src.nodeName) + 10;
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * @private
 */
function GRID_END(e) {
	if (window.dragStart != null && window.dragStart) {
		if (IS_IE) // 仅IE中有此方法
			window.src.releaseCapture();
		var header = window.dragHeader;
		// 改变拖动表头的宽度
		if (window.dragType == 'changeWidth'){
			if (header.isFixedHeader == false) {
				/* 改变动态表头的宽度、改变数据区的宽度、改变相应列的宽度 */
				// 改变的宽度
				var changedWidth = window.headerChangedWidth;
				// 没有drag则changedWidth为null,直接返回不进行下面的处理
				if (changedWidth == null){
					destroyDargObj();			
					return;
				}
				// 当前表头的宽度
				var currWidth = window.defaultHeaderWidth + changedWidth;
				if (currWidth > 0 && currWidth > window.minWidth) {
					/* 改变动态数据区、动态表头宽度 */
					// 动态列headerDiv
					var grid = window.gridOwner;
					// 改变动态表头区宽度
					grid.dynamicHeaderTableDiv.style.width = (window.defaultDynHeaderTableWidth + changedWidth) + "px";
					// 改变拖动表头列宽度
					header.dataDiv.style.width = currWidth + "px";
					if (window.dynamicColumDataDivWidth + changedWidth > 0)
						grid.dynamicColumDataDiv.style.width = (window.dynamicColumDataDivWidth + changedWidth)	+ "px";
					if(grid.dynSumRowContentDiv){
						grid.dynSumRowContentDiv.style.width = (window.dynamicColumDataDivWidth + changedWidth)	+ "px";
					}
					if(IS_IE7){
						if(grid.dynamicColumDataDiv.offsetWidth > grid.dataOuterDiv.offsetWidth){
							grid.dynamicColumDataDiv.style.marginBottom = "17px";
						}else{
							grid.dynamicColumDataDiv.style.marginBottom = "0px";
						}
					}
					// 改变拖动表头div的宽度
					if (header == grid.getLastDynamicVisibleHeader())
						header.contentDiv.style.width = (currWidth - 2) + "px";
					else
						header.contentDiv.style.width = (currWidth - 1) + "px";
					// 改变拖动表头td的宽度
					window.dragSrc.width = currWidth;
					// 改变拖动表头table的宽度
					header.dataTable.style.width = currWidth + "px";
					// 如果该列为合计列,设置合计列的宽度
					if (header.sumCell) {
						grid.dynSumRowDataDiv.style.width = (window.defaultDynHeaderTableWidth + changedWidth) + "px";
						if (header.keyName == grid.basicHeaders[0].keyName)
							header.sumCell.style.width = currWidth + GridComp.ROWSTATE_COLUMN_WIDTH - GridComp.SUMROW_DIV_WIDTH - (GridComp.SUMCELL_PADDING * 2) + "px";
						else
							header.sumCell.style.width = currWidth - (GridComp.SUMCELL_PADDING * 2) + "px";
					}
	
					// 记录拖动表头的width属性
					header.width = currWidth;
					grid.adjustFixedColumDivHeight();
				}
				// 增加此段代码是为了避免表头和数据区div在拖动之后不重合的问题,关键所在! gd 2008-02-21
				var grid = window.gridOwner;
				if (isDivScroll(grid.dataOuterDiv)) {
					if (grid.dataOuterDiv.scrollLeft > 0) {
						grid.setScrollLeft(grid.dataOuterDiv.scrollLeft - 1);
					}
				}
				// 循环设置该列每个cell的宽度，以适应XHTML中的宽度计算问题
				for (var i = 0, n = header.dataDiv.childNodes.length; i < n; i++) {
					// 获取cell元素
					if (header.dataDiv.childNodes[i].className.indexOf("gridcell_") != -1) {
						header.dataDiv.childNodes[i].style.width = (header.dataDiv.offsetWidth - 5) + "px"; // 5为左右padding加上border的宽
						//如果是偏右时，左边距清零，修改文字部分被覆盖
						if(header.dataDiv.childNodes[i].style.textAlign == "right")
							header.dataDiv.childNodes[i].style.paddingLeft =  "0px";
					}
				}
			}
		}
		//交换表头列
		else if (window.dragType == 'swapColumn'){
			if (window.dragTargetHeader != null){
				var grid = window.gridOwner;
				grid.changeColumnOrder(header.keyName, window.dragTargetHeader.keyName);
			}
		}
	}
	destroyDargObj();
};

function destroyDargObj(){
	//释放
	window.dragStart = false;
	if (window.src)
		window.src.style.cursor = 'default';
	window.dragType = null;
	window.dragHeader = null;
	if (window.dragTargetHeader != null){
//		window.dragTargetHeader.contentDiv.style.backgroundColor = "";
		window.dragTargetHeader = null;
	}
	if (window.dragSrcClone){
		try{
			document.body.removeChild(window.dragSrcClone);
		}catch(error){
			//Logger.error(error.name + ":" + error.message);
		}	
		window.dragSrcClone = null;	
	}
};

/**
 * @private
 */
function GRID_DRAG(e) {
	e = EventUtil.getEvent();
	// 得到触发源(header.contentDiv)
	var src = getTarget(e);
	if (src.tagName != null && (src.tagName.toLowerCase() == "input" || src.tagName.toLowerCase() == "img")) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	// 触发源div相对body的left
	var offsetLeft = compOffsetLeft(src, document.body);
	// 当前鼠标的位置
	var currX = e.clientX + document.body.scrollLeft;
	var currY = e.clientY + document.body.scrollTop;
	var dragSrc = getDragSrc(src);
	if (window.dragStart == null || window.dragStart == false) {
		// td
		var flag = 0;
		if (currX > offsetLeft + parseInt(dragSrc.width) - 5 && currX < offsetLeft + parseInt(dragSrc.width)) {
			// 多表头暂时不允许拖动
			if (this.refHeader && this.refHeader.isGroupHeader)
				return;
			flag = 1;
		}
		if (flag == 1 || flag == 2)
			src.style.cursor = 'e-resize';
		else
			src.style.cursor = 'default';
	}
	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if ((e.button == 1 || e.button == 0) && window.dragStart) {
		if (window.dragType == 'changeWidth'){
			window.src.style.cursor = 'col-resize';
			if (IS_IE) // 仅IE中有此方法
				window.src.setCapture();
			window.headerChangedWidth = currX - window.dragSrcX;
		}
		else if (window.dragType == 'swapColumn' && window.dragHeader != null){
			if (IS_IE) // 仅IE中有此方法
				window.src.setCapture();			
			if (window.dragSrcClone){
				var event = EventUtil.getEvent();
				var flagStyle = window.dragSrcClone.style;
				flagStyle.left = (event.clientX + 10) + "px";
				flagStyle.top = (event.clientY + 10) + "px";
				flagStyle.display = "block";
				flagStyle.zIndex = 647;
			}
			if (window.dragTargetHeader != null){
				var targetOffsetLeft = compOffsetLeft(window.dragTargetHeader.dataTable, document.body);
				var targetOffsetTop = compOffsetTop(window.dragTargetHeader.dataTable, document.body);
				if (currX < targetOffsetLeft || currX > targetOffsetLeft + (window.dragTargetHeader.dataTable.offsetWidth / 2)
					||  currY < targetOffsetTop || currY > (targetOffsetTop + window.dragTargetHeader.dataTable.offsetHeight)){
//					window.dragTargetHeader.contentDiv.style.backgroundColor = "";
					window.dragTargetHeader = null;
					if (window.dragSrcClone){
						window.dragSrcClone.style.filter = "Alpha(Opacity=30)";
						window.dragSrcClone.style.MozOpacity = 0.3;
					}
				}
			}
			//计算目标header
			if (window.dragTargetHeader == null){
				var grid = window.gridOwner;
				var headers = grid.defaultDynamicHeaders;
				for (var i = 0; i < headers.length; i++){
					if (headers[i].dataTable){
						var headerLeft = compOffsetLeft(headers[i].dataTable, document.body);
						var headerTop = compOffsetTop(headers[i].dataTable, document.body);
						if (currX > headerLeft && currX < headerLeft + (headers[i].dataTable.offsetWidth / 2)
							&& currY > headerTop && currY < (headerTop + headers[i].dataTable.offsetHeight)){
							if (headers[i].keyName != window.dragHeader.keyName){
								window.dragTargetHeader = headers[i];
//								window.dragTargetHeader.contentDiv.style.backgroundColor = "gray";
								if (window.dragSrcClone){
									window.dragSrcClone.style.filter = "Alpha(Opacity=100)";
									window.dragSrcClone.style.MozOpacity = 1;
								}
							}
							break;
						}else if (currX > headerLeft + (headers[i].dataTable.offsetWidth / 2) 
							&& currX < headerLeft + headers[i].dataTable.offsetWidth && currY > headerTop 
							&& currY < (headerTop + headers[i].dataTable.offsetHeight)){
							// 与当前header下一列互换,如果不存在则与当前列互换.
							var tempHeader = headers[i];
							if(typeof(headers[i+1]) != "undefined"){
								tempHeader = headers[i+1];
							}
							if (tempHeader.keyName != window.dragHeader.keyName){
								window.dragTargetHeader = headers[i+1];
//										window.dragTargetHeader.contentDiv.style.backgroundColor = "gray";
								if (window.dragSrcClone){
									window.dragSrcClone.style.filter = "Alpha(Opacity=100)";
									window.dragSrcClone.style.MozOpacity = 1;
								}
							}
							break;
						}
					}
				}
			}
		}
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 表头拖拽源
 * @param {} src
 * @return {}
 * @private
 */
function getDragSrc(src){
	var dragSrc = null;
	if (src.nodeName == "TD") {
		dragSrc = src;
	} else if (src.nodeName == "TABLE") {
		// TABLE/TBODY/TR/TD
		dragSrc = src.childNodes[0].childNodes[0].childNodes[0]
	} else {
		var tempSrc = src.parentNode;
		while (tempSrc != null && tempSrc.nodeName != "TD") {
			tempSrc = tempSrc.parentNode;
		}
		if (tempSrc == null)
			dragSrc = src;
		else
			dragSrc = tempSrc;
	}
	return dragSrc;
};
/**
 * 动态生成ID
 * @type Number
 */
GridComp.outerDivId = 0;
GridComp.prototype.getId = function (){
	return GridComp.outerDivId++;
};
/**
 * 创建数据区整体div
 * 
 * @private
 */
GridComp.prototype.initDataOuterDiv = function() {
	this.dataOuterDiv = $ce("div");
	this.outerDiv.appendChild(this.dataOuterDiv);
	this.dataOuterDiv.className = "data_outer_div";

	this.scrollState = false;
	this.dataOuterDiv.id = this.outerDivId;
	this.dataOuterDiv.style.zIndex = 101;
	var fixedColumDivWidth = 0;
	if (this.fixedColumDiv)
		fixedColumDivWidth = this.fixedColumDiv.offsetWidth;
	if(this.dataOuterDiv){
		this.dataOuterDiv.style.width = this.constant.outerDivWidth - fixedColumDivWidth + "px";// - 2;
		this.dataOuterDiv.style.left = this.constant.fixedColumDivWidth + "px";
	}
//	if(!this.flowmode){
//		var height = this.outerDiv.offsetHeight - GridComp.HEADERROW_HEIGHT - GridComp.PAGEBAR_HEIGHT;
//		if (height > 0)
//			this.dataOuterDiv.style.height = height + "px";
//		else	
//			this.dataOuterDiv.style.height = "100%";
//	}
	if  (this.canCopy == false){
//		this.dataOuterDiv.onselectstart = function(e){
//			return false;
//		};
//		this.dataOuterDiv.ondragstart = function(e){
//			return false;
//		};
		document.body.onselectstart = function(e){
			return false;
		};
		document.body.ondragstart = function(e){
			return false;
		};
		if(this.dataOuterDiv)
			this.dataOuterDiv.style.MozUserSelect = "none";
	}
	this.dataOuterDiv.style.overflow ="hidden";
};

/**
 * 创建固定列整体div
 * 
 * @private
 */
GridComp.prototype.initFixedColumDiv = function() {
	var h = this.constant.headerHeight;
	this.fixedColumDiv = $ce("div");
	this.fixedColumDiv.id = "fixedColum";
	this.outerDiv.appendChild(this.fixedColumDiv);
	this.fixedColumDiv.className = "fixedcolum_div";
	// 调整高度,paintRows的时候调用，初始化的时候不用调用
	// this.adjustFixedColumDivHeight();
	// 宽度和表头固定区一样
	this.fixedColumDiv.style.width = this.constant.fixedHeaderDivWidth + 1 + "px";
	// this.fixedColumDiv.style.height = "100%";
	// 将固定列的宽度放入常量对象
	this.constant.fixedColumDivWidth = this.constant.fixedHeaderDivWidth;
	//if(!this.flowmode)
	//	this.fixedColumDiv.style.overflow = "hidden";
};

/**
 * 调整fixedColumDiv的高度,当滚动条从有到无或者从无到有时均需要调整此高度
 * 
 * @private
 */
GridComp.prototype.adjustFixedColumDivHeight = function() {
	var oH = this.constant.outerDivHeight, h = this.constant.headerHeight;
	// 调整top值
	if (this.pageSize > 0 && this.isPagenationTop == true)
		this.fixedColumDiv.style.top = (h + GridComp.PAGEBAR_HEIGHT) + "px";
	else
		this.fixedColumDiv.style.top = h + "px";
	
	if(!this.flowmode){
		var height = this.wholeDiv.offsetHeight;
		if(this.descDiv)
			height = height - this.descDiv.offsetHeight;
		if(this.headerBtnDiv)	
			height = height - this.headerBtnDiv.offsetHeight;
		if(height > 0){
			//this.outerDiv.style.height = height + "px";
		}
		if(this.pageSize == -1)
			height = height - this.constant.headerHeight;
		else if(this.needShowNoRowsDiv)
			height = height - this.constant.headerHeight - GridComp.NOROW_DIV_HEIGHT;
		else
			height = height - this.constant.headerHeight - GridComp.PAGEBAR_HEIGHT;
		if (height > 0){
			this.fixedColumDiv.style.height = height + "px";
			this.dataOuterDiv.style.height = height + "px";
		}
		else{
			this.fixedColumDiv.style.height = "100%";
			this.dataOuterDiv.style.height = "100%";
		}
	}
};

/**
 * 画行数字行标div
 * 
 * @private
 */
GridComp.prototype.initRowNumDiv = function() {
	this.rowNumDiv = $ce("div");
	this.fixedColumDiv.appendChild(this.rowNumDiv);
	this.rowNumDiv.id = "rowNumDiv";
	this.rowNumDiv.className = "num_div";
	this.rowNumDiv.style.width = this.constant.rowNumHeaderDivWidth + "px";
	// 保存此div所有子div的引用
	this.rowNumDiv.cells = new Array(this.getRowsNum());
};

/**
 * 创建行状态显示列
 * 
 * @private
 */
GridComp.prototype.initLineStateColumDiv = function() {
	this.lineStateColumDiv = $ce("div");
	var line = this.lineStateColumDiv;
	this.fixedColumDiv.appendChild(line);
	line.id = "lineStateColumDiv";
	line.className = "state_div";
	line.style.top = "0px";
	// line.style.border = "solid blue 1px";

	// 显示合计行,要减小行状态列的高度，使用offsetHeight，待验证
	if (this.isShowSumRow) {
		if(this.fixedColumDiv.offsetHeight>0){
			line.style.height = (this.fixedColumDiv.offsetHeight - GridComp.ROW_HEIGHT) + "px";
			line.defaultHeight = this.fixedColumDiv.offsetHeight - GridComp.ROW_HEIGHT;
		}
	}	

	// 设置left
	if (this.isShowNumCol)
		line.style.left = this.constant.rowNumHeaderDivWidth + "px";
	else
		line.style.left = "0px";
	line.style.width = this.constant.lineStateHeaderDivWidth + "px";
	// 保存此div所有子div的引用
	this.lineStateColumDiv.cells = new Array(this.getRowsNum());
};

/**
 * 画合计行在固定列区域的部分
 * 
 * @private
 */
GridComp.prototype.initSumRowDiv = function() {
	this.sumRowDiv = $ce("div");
	this.sumRowDiv.id = "sumRowDiv";
	this.sumRowDiv.className = "sum_div";
	if(this.hasBorder)
		$(this.sumRowDiv).addClass("sumDivExtendCss");
	this.dynSumRowContentDiv.appendChild(this.sumRowDiv);
	this.sumRowDiv.style.left = "0px";
	/*
	if (this.pageSize > 0 && this.isPagenationTop == false) // 有分页条并且分页条在下方
		this.sumRowDiv.style.top = (this.constant.outerDivHeight
				- GridComp.SCROLLBAE_HEIGHT - GridComp.PAGEBAR_HEIGHT - 12)
				+ "px";
	else
		this.sumRowDiv.style.top = (this.constant.outerDivHeight
				- GridComp.SCROLLBAE_HEIGHT - 4)
				+ "px";*/
	this.sumRowDiv.style.top = "0px";
	this.sumRowDiv.style.height = GridComp.ROW_HEIGHT + "px";
	this.sumRowDiv.style.lineHeight = GridComp.ROW_HEIGHT + "px";
	this.sumRowDiv.style.width = 40 + "px";
	this.sumRowDiv.innerHTML = "<center>" + trans("ml_total") + "</center>";
};
/**
 * 画合计行div,此div内放置合计行的每个cell
 * 
 * @private
 */
GridComp.prototype.initSumRowDataDiv = function() {
	// 创建放置动态数据区"合计"行的div,该div的overflow设置为hidden
	this.dynSumRowContentDiv = $ce("div");
	var cont = this.dynSumRowContentDiv;
	cont.className = "dynsumcontainer_div";
	if(this.hasBorder)
		$(cont).addClass("dynsumcontainerDivExtend");
//	this.outerDiv.appendChild(cont);
	this.dataOuterDiv.appendChild(cont);
	this.initSumRowDiv();
//	if (this.dynamicHeaderDiv.defaultWidth + 4 > 0)
//		cont.style.width = (this.dynamicHeaderDiv.defaultWidth + 4) + "px";
	if (this.dynamicHeaderDiv.defaultWidth -2 > 0)
		cont.style.width = (this.dynamicHeaderDiv.defaultWidth - 2) + "px";
	else
		cont.style.width = "0px";
	cont.style.height = GridComp.ROW_HEIGHT + "px";

//	if (this.pageSize > 0 && this.isPagenationTop == false) // 有分页条并且分页条在下方
//		cont.style.top = (this.constant.outerDivHeight
//				- GridComp.SCROLLBAE_HEIGHT - GridComp.PAGEBAR_HEIGHT - 12)
//				+ "px";
//	else
//		cont.style.top = (this.constant.outerDivHeight
//				- GridComp.SCROLLBAE_HEIGHT - 4)
//				+ "px";
//
//	cont.style.left = this.fixedColumDiv.offsetWidth + 20 + "px";
//	cont.style.defaultLeft = this.fixedColumDiv.offsetWidth + 20 + "px";
//	cont.style.left = this.fixedColumDiv.offsetWidth + "px";
//	cont.style.defaultLeft = this.fixedColumDiv.offsetWidth + "px";

	// 创建动态数据区"合计"行
	this.dynSumRowDataDiv = $ce("div");
	var d = this.dynSumRowDataDiv;
	d.className = "dynsumrow_div";
	if(this.hasBorder)
		$(d).addClass("dynsumrowDivExtendCss");
	d.id = "dynSumRowDataDiv";
	cont.appendChild(d);
	d.style.top = "0px";
	d.style.left = "40px";
	d.defaultLeft = "40px";
	d.style.height = "100%";
	d.style.width = this.dynamicHeaderTableDiv.offsetWidth  - this.sumRowDiv.offsetWidth + "px";
};

/**
 * 画合计行中的每个单元格
 * 
 * @private
 */
GridComp.prototype.initSumRowCells = function() {
	var firstVisibleHeader = this.getFirstVisibleHeader();
	for (var i = 0, count = this.defaultDynamicHeaders.length; i < count; i++) {
		if (this.defaultDynamicHeaders[i].isHidden == false && this.defaultDynamicHeaders[i].isGroupHeader == false) {
			if (firstVisibleHeader != null && firstVisibleHeader.keyName == this.defaultDynamicHeaders[i].keyName)
				this.createSumRowCell(this.defaultDynamicHeaders[i], true);
			else	
				this.createSumRowCell(this.defaultDynamicHeaders[i], false);
			
//			var cell = $ce("div");
//			cell.headKey = this.defaultDynamicHeaders[i].keyName;
//			// 表头保存sumcell的引用
//			this.defaultDynamicHeaders[i].sumCell = cell;
//			cell.className = "dynsumcell_div";
//			this.dynSumRowDataDiv.appendChild(cell);
//			cell.style.height = GridComp.ROW_HEIGHT + "px";
//			cell.style.lineHeight = GridComp.ROW_HEIGHT + "px";
////			if (i == 0)
////				cell.style.width = this.defaultDynamicHeaders[i].width - 5 - 20+ "px";
////			else
//			var extendWidth = 0;
//			if (firstVisibleHeader != null && firstVisibleHeader.keyName == this.defaultDynamicHeaders[i].keyName){
//				extendWidth = this.constant.rowNumHeaderDivWidth + 40;
//			}
//			if (this.defaultDynamicHeaders[i].dataDivWidth != null){
//				cell.style.width = (this.defaultDynamicHeaders[i].dataDivWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth) + "px";
//			} else{
//				cell.style.width = (this.defaultDynamicHeaders[i].dataDiv.offsetWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth) + "px";
//			}
//			if(i == 0){
//				cell.style.width = cell.offsetWidth - this.sumRowDiv.offsetWidth + "px";
//			}
//			cell.style.paddingLeft = GridComp.CELL_LEFT_PADDING + "px";
//			cell.style.paddingRight = "10px";
//			/* 标题中对齐
//			 * 文本类、参照类、枚举类的左对齐
//			 * 数字类的右对齐
//			 * 日期/布尔类的居中 */
//			if(typeof(this.basicHeaders[i].dataType) == "string"){
//				switch(this.basicHeaders[i].dataType){
//					case "UFDateTime":
//					case "UFDate":
//					case "UFTime":
//					case "Date":
//					case "ShortDate":
//					case "Boolean":
//					case "boolean":
//					case "UFBoolean":
//						cell.style.textAlign = "center";
//						break;
//					case "Integer":
//					case "int":
//					case "Double":
//					case "double":
//					case "UFDouble":
//					case "Float":
//					case "float":
//					case "BigDecimal":
//					case "Decimal":
//					case "Long":
//					case "long":
//						cell.style.textAlign = "right";
//						break;
//					default:
//						cell.style.textAlign = "left";
//						break;
//				}
//			}else{
//				// 根据header中textAlign属性设置文字在cell中的位置.
//				cell.style.textAlign = this.basicHeaders[i].textAlign;
//			}
		}
		else if (this.defaultDynamicHeaders[i].isHidden == false && this.defaultDynamicHeaders[i].isGroupHeader == true){
			for (var j = 0; j < this.defaultDynamicHeaders[i].basicHeaders.length; j++) {
				if (this.defaultDynamicHeaders[i].basicHeaders[j].isHidden == false) {
					if (firstVisibleHeader != null && firstVisibleHeader.keyName == this.defaultDynamicHeaders[i].basicHeaders[j].keyName)
						this.createSumRowCell(this.defaultDynamicHeaders[i].basicHeaders[j], true);
					else	
						this.createSumRowCell(this.defaultDynamicHeaders[i].basicHeaders[j], false);
				}		
			}
		}
	}
};

/**
 * private
 */
GridComp.prototype.createSumRowCell = function(header, isFirstHeader){
	var cell = $ce("div");
	cell.headKey = header.keyName;
	// 表头保存sumcell的引用
	header.sumCell = cell;
	cell.className = "dynsumcell_div";
	if(this.hasBorder)
		$(cell).addClass("dynsumcellDivExtendCss");
	this.dynSumRowDataDiv.appendChild(cell);
	cell.style.height = GridComp.ROW_HEIGHT + "px";
	cell.style.lineHeight = GridComp.ROW_HEIGHT + "px";
	var extendWidth = 0;
	if (isFirstHeader){
		extendWidth = this.constant.rowNumHeaderDivWidth + 40;
	}
	if (header.dataDivWidth != null && header.dataDivWidth > 0 && header.dataDivWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth > 0){
		cell.style.width = (header.dataDivWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth) + "px";
	} else if(header.dataDiv && header.dataDiv.offsetWidth > 0 && header.dataDiv.offsetWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth > 0){
		cell.style.width = (header.dataDiv.offsetWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth) + "px";
	} else if(header.width && header.width > 0 && header.width - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth > 0){
		cell.style.width = (header.width - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - extendWidth) + "px";
	}
//	if(i == 0){
//		cell.style.width = cell.offsetWidth - this.sumRowDiv.offsetWidth + "px";
//	}
	cell.style.paddingLeft = GridComp.CELL_LEFT_PADDING + "px";
	cell.style.paddingRight = "10px";
	/* 标题中对齐
	 * 文本类、参照类、枚举类的左对齐
	 * 数字类的右对齐
	 * 日期/布尔类的居中 */
	if(header.textAlign == ""){
		setTextAlign = "left";
	}else{
		setTextAlign = header.textAlign;
	}
	if(typeof(header.dataType) == "string"){
		switch(header.dataType){
			case "UFDateTime":
			case "UFDate":
			case "UFTime":
			case "Date":
			case "ShortDate":
			case "Boolean":
			case "boolean":
			case "UFBoolean":
				cell.style.textAlign = "center";
				break;
			case "Integer":
				if(header.editorType && header.editorType == "ComboBox"){
					//下拉框当做文本类处理
					cell.style.textAlign = "left";
					break;
				}
			case "int":
			case "Double":
			case "double":
			case "UFDouble":
			case "Float":
			case "float":
			case "BigDecimal":
			case "Decimal":
			case "Long":
			case "long":
				cell.style.textAlign = "right";
				break;
			default:
				cell.style.textAlign = setTextAlign;
				break;
		}
	}else{
		// 根据header中textAlign属性设置文字在cell中的位置.
		cell.style.textAlign = setTextAlign;
	}
};

/**
 * 创建固定选择列
 * 
 * @private
 */
GridComp.prototype.initSelectColumDiv = function() {
	this.selectColumDiv = $ce("div");
	this.fixedColumDiv.appendChild(this.selectColumDiv);
	this.selectColumDiv.id = "fixedSelectColum";
	this.selectColumDiv.className = "fixed_select_colum";
	// 将固定表头引用保存在固定选择列的header属性中
	this.selectColumDiv.header = this.selectColumHeaderDiv;
	// this.selectColumDiv.style.position = "absolute";
	// this.selectColumDiv.style.overflow = "hidden";
	this.selectColumDiv.style.left = (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth)
			+ "px";
	this.selectColumDiv.style.width = GridComp.SELECTCOLUM_WIDTH + "px";
	// 当前model中的行数
	var rowNum = this.getRowsNum();
	// 存放所有cells索引的数组
	this.selectColumDiv.cells = new Array(rowNum);
	
	if(!this.flowmode)
		this.selectColumDiv.style.position = "absolute";
};

/**
 * 画动态列数据区div
 * 
 * @private
 */
GridComp.prototype.initDynamicColumDataDiv = function() {
	var iOffsetWidth = this.constant.fixedColumDivWidth;
	var rowsNum = this.getRowsNum();
	if(this.noRowsDiv){
		if(rowsNum <= 0){
			this.needShowNoRowsDiv = true;
			if(this.model.dataset.lazyLoad == true){//懒加载无数据提示晚于loading动画
				setTimeout("GridComp.showNoRowsDiv('" + this.id + "');",1500);
			}else{
				setTimeout("GridComp.showNoRowsDiv('" + this.id + "');",500);
			}
			GridComp.gridResize(this.id);
		}else{
			this.needShowNoRowsDiv = false;
			this.noRowsDiv.style.display = "none";
		}
	}
	this.dynamicColumDataDiv = $ce("div");
	if (this.dataOuterDiv.childNodes[0] != null)
		this.dataOuterDiv.insertBefore(this.dynamicColumDataDiv, this.dataOuterDiv.childNodes[0]);
	else	
		this.dataOuterDiv.appendChild(this.dynamicColumDataDiv);
	this.dynamicColumDataDiv.className = "dynamic_data_div";
	this.dynamicColumDataDiv.id = "dynamicDataDiv";
	// this.dynamicColumDataDiv.style.border = "solid black 1px";

//	this.dynamicColumDataDiv.style.top = this.constant.headerHeight + "px";
	//this.dynamicColumDataDiv.style.left = iOffsetWidth + "px";
	// 保存数据区真正的宽度值,动态表格区真正的宽度值
	var dynamicDataDivRealWidth = this.getDynamicTableDivRealWidth(true);
	this.realWidth = dynamicDataDivRealWidth + iOffsetWidth;
	// this.dynamicColumDataDiv.style.width = dynamicDataDivRealWidth - 2 +
	// "px";
	this.dynamicColumDataDiv.style.width = (dynamicDataDivRealWidth + 2) + "px";//2 左右DIV border边框宽度和
	if(this.dynSumRowContentDiv){
		this.dynSumRowContentDiv.style.width = (dynamicDataDivRealWidth + 2) + "px";
	}
	if(rowsNum > 0){
		//由于自定滚动条所以设置为marginBottom 为0  默认为17px
		this.dynamicColumDataDiv.style.marginBottom = "0px";
		//由于自定滚动条所以设置为hidden
		this.dataOuterDiv.style.overflow = "hidden";
		this.dataOuterDiv.style.display = "block";
	}else{
		this.dynamicColumDataDiv.style.marginBottom = "0px";
		this.dataOuterDiv.style.overflow = "hidden";
		// 自定义俩种态下显示滚动条
		this.dataOuterDiv.style.display = "block";
	}

	if (this.defaultDynamicHeaders != null) {
		var len = this.defaultDynamicHeaders.length;
		var rowNum = this.getRowsNum();
		for (var i = 0; i < len; i++) {
			var tempH = this.defaultDynamicHeaders[i];
			// 单表头情况
			if (tempH.basicHeaders == null && tempH.isHidden == false) {
				tempH.dataDiv = $ce("div");
				var tempDiv = tempH.dataDiv;
				// 将header的引用保存在dataDiv上
				tempDiv.header = tempH;
				// 列的数据存放数组,为了快速检索数据
				tempDiv.cells = new Array(rowsNum);
				this.dynamicColumDataDiv.appendChild(tempDiv);
				tempDiv.style.width = tempH.width + "px";
//				tempDiv.style.height = rowsNum * this.rowHeight + "px";
				tempDiv.style.position = "relative";
				tempDiv.style[ATTRFLOAT] = "left";
				tempDiv.style.overflow = "hidden";
			}
			// 多表头情况
			else if (tempH.basicHeaders != null && tempH.isHidden == false) {
				var tempHeaders = tempH.basicHeaders;
				for (var j = 0; j < tempHeaders.length; j++) {
					if (tempHeaders[j].isHidden == false) {
						tempHeaders[j].dataDiv = $ce("div");
						var tempDiv = tempHeaders[j].dataDiv;
						// 将header的引用保存在dataDiv上
						tempDiv.header = tempHeaders[j];
						// 列的数据存放数组,为了快速检索数据
						tempDiv.cells = new Array(rowsNum);
						this.dynamicColumDataDiv.appendChild(tempDiv);
						tempDiv.style.width = tempHeaders[j].width + "px";
//						tempDiv.style.height = rowsNum * this.rowHeight + "px";
						tempDiv.style.position = "relative";
						tempDiv.style[ATTRFLOAT] = "left";
						tempDiv.style.overflow = "hidden";
					}
				}
			}
		}
	}

	if (isDivVScroll(this.dataOuterDiv)) {
		this.setScrollTop(0);
	}
};

/**
 * 显示空行信息
 * 
 * @modify 2012-05-16 liujmc
 * @description 画动态列数据区div时被调用，为解决onbeforeshow()之前显示空行信息，增加根据进度条进行判断的逻辑
 */
GridComp.showNoRowsDiv = function(gridId) {
	if(window.loadingBar && window.loadingBar.visible){
		setTimeout(function(){
			GridComp.showNoRowsDiv(gridId);
		},500);
	}else{
		var oThis = window.objects[gridId];
		//如果onbeforeshow事件后，数据不为空，则会将needShowNoRowsDiv置为false
		if (oThis.needShowNoRowsDiv != null && oThis.needShowNoRowsDiv == true && oThis.model.rows.length ==0){
			oThis.noRowsDiv.style.display = "";
			if(oThis.dynamicColumDataDiv){
				oThis.dynamicColumDataDiv.style.marginBottom = "0px";
			}
			if(oThis.dataOuterDiv){
				oThis.dataOuterDiv.style.overflow = "hidden";
				oThis.dataOuterDiv.style.display = "block";
			}
			if(oThis.formOuterDiv){
				oThis.formOuterDiv.style.overflow = "hidden";
				oThis.formOuterDiv.style.display = "none";
			}
		}
	}
	//弹出窗口多个panel中存在grid，当展开panel时如果无数据则会出现多滚动条
	adjustContainerFramesHeight();
};

/**
 * 画静态列数据区div
 * 
 * @private
 */
GridComp.prototype.initFixedColumDataDiv = function() {
//	var iOffsetWidth = this.constant.fixedColumDivWidth;
	var rowsNum = this.getRowsNum();
//	if(this.noRowsDiv){
//		if(rowsNum <= 0){
//			this.noRowsDiv.style.display = "";
//		}else{
//			this.noRowsDiv.style.display = "none";
//		}
//	}
//	this.dynamicColumDataDiv = $ce("div");
//	this.dataOuterDiv.appendChild(this.dynamicColumDataDiv);
//	this.dynamicColumDataDiv.className = "dynamic_data_div";
//	this.dynamicColumDataDiv.id = "dynamicDataDiv";
//	
//	var fixedDataDivRealWidth = this.getDynamicTableDivRealWidth(false);
//	this.dynamicColumDataDiv.style.width = (dynamicDataDivRealWidth + 2) + "px";//2 左右DIV border边框宽度和

	if (this.defaultFixedHeaders != null) {
		var len = this.defaultFixedHeaders.length;
		var rowNum = this.getRowsNum();
		for (var i = 0; i < len; i++) {
			var tempH = this.defaultFixedHeaders[i];
			// 单表头情况
			if (tempH.basicHeaders == null && tempH.isHidden == false) {
				tempH.dataDiv = $ce("div");
				var tempDiv = tempH.dataDiv;
				// 将header的引用保存在dataDiv上
				tempDiv.header = tempH;
				// 列的数据存放数组,为了快速检索数据
				tempDiv.cells = new Array(rowsNum);
				this.fixedColumDiv.appendChild(tempDiv);
				tempDiv.style.width = tempH.width + "px";
//				tempDiv.style.height = rowsNum * this.rowHeight + "px";
				tempDiv.style.position = "relative";
				tempDiv.style[ATTRFLOAT] = "left";
				tempDiv.style.overflow = "hidden";
			}
			// 多表头情况
			else if (tempH.basicHeaders != null && tempH.isHidden == false) {
				var tempHeaders = tempH.basicHeaders;
				for (var j = 0; j < tempHeaders.length; j++) {
					if (tempHeaders[j].isHidden == false) {
						tempHeaders[j].dataDiv = $ce("div");
						var tempDiv = tempHeaders[j].dataDiv;
						// 将header的引用保存在dataDiv上
						tempDiv.header = tempHeaders[j];
						// 列的数据存放数组,为了快速检索数据
						tempDiv.cells = new Array(rowsNum);
						this.fixedColumDiv.appendChild(tempDiv);
						tempDiv.style.width = tempHeaders[j].width + "px";
//						tempDiv.style.height = rowsNum * this.rowHeight + "px";
						tempDiv.style.position = "relative";
						tempDiv.style[ATTRFLOAT] = "left";
						tempDiv.style.overflow = "hidden";
					}
				}
			}
		}
	}

	if (!this.flowmode){
		this.fixedColumDiv.style.overflowY =  "hidden";
	}
	if (isDivVScroll(this.dataOuterDiv)) {
		this.setScrollTop(0);
	}
};


/**
 * 返回model中的行数量
 * 
 * @private
 */
GridComp.prototype.getRowsNum = function() {
	if (this.model == null)
		return 0;
	else
		return this.model.getRowsCount();
};

/**
 * 得到动态列中给定header之前的所有显示的动态列header的宽度和
 * 
 * @private
 */
GridComp.prototype.getPreHeadersWidth = function(cell) {
	var totalWidth = 0;
	// 得到此cell的header
	var len = this.basicHeaders.length;
	for (var i = 0; i < len; i++) {
		var header = this.basicHeaders[i];
		if (!header.isFixedHeader && !header.isHidden) {
			if (header != this.basicHeaders[cell.colIndex])
				totalWidth += header.width;
			else
				break;
		}
	}
	return totalWidth;
};

/**
 * 得到动态表头区或者固定表头区真正的宽度
 * 
 * @param isDynamic
 *            true:动态表区 false:静态表区
 * @private
 */
GridComp.prototype.getDynamicTableDivRealWidth = function(isDynamic) {
	var headers = this.model.getHeaders();
	if (headers == null)
		return;

	var realWidth = 0;
	if (isDynamic) {
		// 得到动态表头的宽度
		for (var i = 0; i < headers.length; i++) {
			var header = headers[i];
			if (header.isFixedHeader == false && header.isHidden == false)
				realWidth += this.getHeaderDefaultWidth(header)
						+ GridComp.COLUMN_LEFT_BORDER_WIDTH;
		}
	} else if (isDynamic == false) {
		// 得到固定表头的宽度
		for (var i = 0; i < headers.length; i++) {
			var header = headers[i];
			if (header.isFixedHeader && header.isHidden == false)
				realWidth += this.getHeaderDefaultWidth(header)
						+ GridComp.COLUMN_LEFT_BORDER_WIDTH;
		}
	}
	return realWidth;
};

/**
 * 设置grid的编辑属性,为true则点击cell时会激活相应的编辑控件
 * 
 * @param isEditable
 *            设置grid可否编辑
 * @private
 */
GridComp.prototype.setEditable = function(isEditable) {
	var oldEditable = getBoolean(this.editable, true);
	this.editable = getBoolean(isEditable, true);
	if (this.editable && this.compsInited == false)
		GridComp.initEditCompsForGrid(this.id);

	for (var i = 0; i < this.basicHeaders.length; i++) {
		var header = this.basicHeaders[i];
		//特殊处理BooleanRender的列
		if (header.renderType == BooleanRender) {
			if (header.columEditable == false) continue;
			if (isEditable) {
				if (header.selectBox != null)
					header.selectBox.disabled = false;
				if (header.dataDiv != null && header.dataDiv.cells != null){
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = false;
					}
				}
			} else {
				if (header.selectBox != null) 
					header.selectBox.disabled = true;
				if (header.dataDiv != null && header.dataDiv.cells != null){
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = true;
					}
				}
			}
		}else if (header.renderType == RadioGroupRender){
			if (header.columEditable == false) continue;
			if (header.dataDiv != null && header.dataDiv.cells != null){
				for (var j = 0; j < header.dataDiv.cells.length; j++) {
					if (header.dataDiv.cells[j] && header.dataDiv.cells[j].comp && header.dataDiv.cells[j].comp.setActive){
						header.dataDiv.cells[j].comp.setActive(isEditable);
					}
				}
			}
		}else if (header.renderType && header.renderType.settings && header.renderType.settings["render_editchange"] && header.renderType.settings["render_editchange"] == true){
			header.reRender();
		}
		if(oldEditable != this.editable){
			if(header.columEditable == false && header.dataDiv != null && header.dataDiv.cells != null){
				for (var j = 0; j < header.dataDiv.cells.length; j++) {
					try{
						if(this.editable){
							header.dataDiv.cells[j].style.background = "#e4e4e4";
						}else{
							header.dataDiv.cells[j].style.background = "";
						}
					}catch(e){
					}
				}
			}
		}
		
	}
	this.notifyChange(NotifyType.EDITABLE, this.editable);
};

GridComp.prototype.setShowTree = function(showTree){
	if (this.showTree != showTree){
		this.showTree = showTree;
		this.model.showTree = this.showTree;
		this.model.rows = null;
		this.model.initUIRows();
		this.setModel(this.model);
		this.notifyChange("showTree", this.showTree);
	}
};

/**
 * 设置是否多选
 * 
 * @param {} isMultiSelect
 */
GridComp.prototype.setMultiSelect = function(isMultiSelWithBox){
	if(isMultiSelWithBox == this.isMultiSelWithBox) return;
	this.isMultiSelWithBox = isMultiSelWithBox;
	//由复选到单选时，要把选中行去掉
	if (this.isMultiSelWithBox == false){
		var ds = this.model.dataset;
		ds.setAllRowUnSelected();
		ds.setRowSelected(0);
	}
	if(this.showForm){
		this.paintFormData();
	}else{
		this.paintData();
	}
};

/**
 * 设置此grid是否激活
 * 
 * @param isActive
 *            true表示处于激活,否则表示禁用状态
 */
GridComp.prototype.setActive = function(isActive) {
	this.isGridActive = getBoolean(isActive, true);
	this.notifyChange(NotifyType.ENABLE, isActive);
};

/**
 * 根据后台生成的js脚本设置合计值
 * 
 * @param colIndex:列号
 * @param keyName:列名
 * @param sumValue:合计值
 */
GridComp.prototype.setSumCellValue = function(colIndex,keyName,sumValue) {
	if(this.model){
		this.model.setSumColValueByExecuteJs(colIndex,keyName,sumValue);
	}
};

/**
 * 初始化grid的编辑控件
 * 
 * @param gridId
 *            要初始化控件的grid id
 * @private
 */
GridComp.initEditCompsForGrid = function(gridId) {
	
	var oThis = window.objects[gridId];
	if (oThis == null)
		return;
	if (!oThis.editable)
		return;
	if (oThis.compsInited)
		return;
	// 标示控件已经被初始化过
	oThis.compsInited = true;
	// 标示各个控件是否已经初始化
	var stringInited = false;
	var textAreaInited = false;
	var integerInited = false;
	var decimalInited = false;
	var dateInited = false;
	var dateTimeInited = false;
	var boolInited = false;
	var yearInited = false;
	var monthInited = false;
	var yearmonthInited = false;
	var timeInited = false;
	var emailInited = false;
	var phoneInited = false;
	var linkInited = false;
	var moneyInited = false;
	var precentInited = false;
	var addressInited = false;
	var pwdInited = false;

	var basicHeaders = oThis.basicHeaders;
	if (oThis.compsMap == null)
		oThis.compsMap = new HashMap();

	for (var i = 0; i < basicHeaders.length; i++) {
		var comp = null;
		// textArea控件
		if (basicHeaders[i].editorType == EditorType.TEXTAREA
				&& textAreaInited == false) {
			var maxLen = getInteger(basicHeaders[i].maxLength, "-1");
			comp = new TextAreaComp(document.body, "textArea", 0, 0, "", "",
					"absolute", false, "", "", "","","",{"maxSize":maxLen});
			oThis.compsMap.put(EditorType.TEXTAREA, comp);
			textAreaInited = true;
		} else if (basicHeaders[i].editorType == EditorType.INTEGERTEXT
				&& integerInited == false) {
			// 整型输入框
			comp = new IntegerTextComp(document.body, "integerText", 0, 0,
					"100%", "absolute", "", "", "");
			oThis.compsMap.put(EditorType.INTEGERTEXT, comp);
			integerInited = true;
		} else if (basicHeaders[i].editorType == EditorType.DECIMALTEXT
				&& decimalInited == false) {
			// 浮点数输入框
			// parent, name, left, top, width, position, precision, attrArr
			comp = new FloatTextComp(document.body, "floatText", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.DECIMALTEXT, comp);
			decimalInited = true;
		} else if (basicHeaders[i].editorType == EditorType.CHECKBOX
				&& boolInited == false) {
			/*单元格中本身就包含checkbox，不需要在创建checkbox控件
			// 选择框
			comp = new CheckboxComp(document.body, "checkBox", 0, 0, "100%",
					"", false, "absolute");
			oThis.compsMap.put(EditorType.CHECKBOX, comp);*/
			boolInited = true;
		} else if (basicHeaders[i].editorType == EditorType.DATETEXT
				&& dateInited == false) {
			// 日期类型输入框
			comp = new DateTextComp(document.body, "dateText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			oThis.compsMap.put(EditorType.DATETEXT, comp);
			dateInited = true;
		} else if (basicHeaders[i].editorType == EditorType.DATETIMETEXT
				&& dateTimeInited == false) {
			// 日期时间类型输入框
			comp = new DateTextComp(document.body, "dateText", 0, 0, "100%",
					"absolute", {
						readOnly : false,
						editMin : basicHeaders[i].editMin,
						editSec : basicHeaders[i].editSec
					});
			comp.setShowTimeBar(true);
			oThis.compsMap.put(EditorType.DATETIMETEXT, comp);
			dateTimeInited = true;
		} else if (basicHeaders[i].editorType == EditorType.YEARTEXT
				&& yearInited == false) {
			// 日期类型输入框
			comp = new YearTextComp(document.body, "yearText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			oThis.compsMap.put(EditorType.YEARTEXT, comp);
			yearInited = true;
		} else if (basicHeaders[i].editorType == EditorType.MONTHTEXT
				&& monthInited == false) {
			// 日期类型输入框
			comp = new MonthTextComp(document.body, "monthText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			oThis.compsMap.put(EditorType.MONTHTEXT, comp);
			monthInited = true;
		} else if (basicHeaders[i].editorType == EditorType.YEARMONTHTEXT
				&& yearmonthInited == false) {
			// 日期类型输入框
			comp = new YearMonthTextComp(document.body, "yearmonthText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			oThis.compsMap.put(EditorType.YEARMONTHTEXT, comp);
			yearmonthInited = true;
		} else if (basicHeaders[i].editorType == EditorType.TIMETEXT
				&& timeInited == false) {
			// 日期类型输入框
			comp = new TimeTextComp(document.body, "timeText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			oThis.compsMap.put(EditorType.TIMETEXT, comp);
			timeInited = true;
		} else if (basicHeaders[i].editorType == EditorType.COMBOBOX) {
			// ComboComp(parent, name, left, top, width, position, selectOnly,
			// attrArr, className)
			// 下拉框类型(暂时设置下拉控件为仅选择的)
			comp = new ComboComp(document.body, "COMBOBOX" + i, 0, 0, "100%",
					"absolute", true, {needNullOption:basicHeaders[i].needNullOption},"");
			basicHeaders[i].comboComp = comp;
			// 设置输入控件的高度
			comp.Div_gen.style.height = GridComp.ROW_HEIGHT + "px";
			// 仅显示图片的combo
			if (basicHeaders[i].comboData != null) {
				// var keyValues = basicHeaders[i].comboData.getValueArray();
				// var captionValues = basicHeaders[i].comboData.getNameArray();
				// if (basicHeaders[i].showImgOnly) {
				// for ( var j = 0, count = keyValues.length; j < count; j++)
				// comp.createOption(captionValues[j], keyValues[j], null,
				// false, -1, true);
				// }
				// // 显示文字的combo
				// else {
				// for ( var j = 0, count = keyValues.length; j < count; j++)
				// comp.createOption(captionValues[j], keyValues[j], null,
				// false, -1, false);
				// }
				comp.setComboData(basicHeaders[i].comboData);
			}
			var key = EditorType.COMBOBOX + i;
			oThis.compsMap.put(key, comp);
		}
		//多语控件的处理,构造多语控件,初始化下拉项
		else if(basicHeaders[i].editorType == EditorType.LANGUAGECOMBOBOX){
			var maxLen = getInteger(basicHeaders[i].maxLength, 20);
			var userObj = { "disabled" : false,
							"readOnly" : false,
							"dataDivHeight" : "160",
							"inputAssistant" : null,
							"maxSize":maxLen};
			
			var key = EditorType.LANGUAGECOMBOBOX + i;
			comp = new LanguageComboComp(document.body, key, 0, 0, "100%", "absolute", false, userObj, "", parseInt(oThis.currentLanguageCode)-1,parseInt(oThis.defaultLangCode)-1);
			if(basicHeaders[i].langugeComboDatas!=null && typeof(basicHeaders[i].langugeComboDatas)!="undefined"){
				comp.setComboOptions(basicHeaders[i].langugeComboDatas);
			}
			basicHeaders[i].languageComboComp = comp;
			comp.Div_gen.style.height = GridComp.ROW_HEIGHT + "px";
			oThis.compsMap.put(key, comp);
		} else if (basicHeaders[i].editorType == EditorType.REFERENCE) {
			comp = new ReferenceTextComp(document.body, "Reference" + i, 0, 0,
					"100%", "absolute", {"viewURL":basicHeaders[i].viewURL}, basicHeaders[i].nodeInfo);
			comp.setBindInfo(oThis.model.dataset.id, basicHeaders[i].keyName, basicHeaders[i].pkField);
			comp.refGridId = oThis.id;
			comp.refGridHeaderId = basicHeaders[i].keyName;
			comp.widget = oThis.widget;
			var key = EditorType.REFERENCE + i;
			oThis.compsMap.put(key, comp);
		} else if (basicHeaders[i].editorType == EditorType.EMAILTEXT
				&& emailInited == false) {
			// email类型输入框
			comp = new EmailTextComp(document.body, "EmailText", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.EMAILTEXT, comp);
			emailInited = true;
		} 
		else if (basicHeaders[i].editorType == EditorType.PHONETEXT
				&& phoneInited == false) {
			// 电话类型输入框
			comp = new PhoneTextComp(document.body, "PhoneText", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.PHONETEXT, comp);
			phoneInited = true;
		} else if (basicHeaders[i].editorType == EditorType.LINKTEXT
				&& linkInited == false) {
			// 超链接类型输入框
			comp = new LinkTextComp(document.body, "LinkText", 0, 0, "100%",
					"absolute", {"target":basicHeaders[i].target,"imgPath":basicHeaders[i].imgPath,"linkType":basicHeaders[i].linkType}, "");
			oThis.compsMap.put(EditorType.LINKTEXT, comp);
			linkInited = true;
		} else if (basicHeaders[i].editorType == EditorType.MONEYTEXT
				) {
			// 金额类型输入框
			comp = new MoneyTextComp(document.body, "MoneyText" + i, 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.MONEYTEXT + i, comp);
//			moneyInited = true;
		} else if (basicHeaders[i].editorType == EditorType.PRECENTTEXT
				&& precentInited == false) {
			// 百分比类型输入框
			comp = new PrecentTextComp(document.body, "PrecentText", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.PRECENTTEXT, comp);
			precentInited = true;
		}  else if (basicHeaders[i].editorType == EditorType.PWDTEXT
				&& pwdInited == false) {
			// 密码类型输入框
			comp = new PswTextComp(document.body, "pswText", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.PWDTEXT, comp);
			pwdInited = true;
		} else if (stringInited == false) {
			// 字符串输入框
			comp = new StringTextComp(document.body, "text", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.STRINGTEXT, comp);
			stringInited = true;
		} 
		GridComp.addCompListener(oThis, comp, i);
	}
		// 标示控件已经被初始化过
		// oThis.compsInited = true;
};

/**
 * 对控件增加事件
 * 
 * @param {} grid
 * @param {} comp
 * @param {} colIndex
 */
GridComp.addCompListener = function(grid, comp, colIndex){
	if (comp != null) {
		comp.ingrid = true;
		comp.colIndex = colIndex;
//		var compKeyListener = new KeyListener();
		var compKeyListener = new Listener("onKeyDown");
		compKeyListener.func = function(keyEvent) {
			GridComp.compKeyDownFun(grid, keyEvent.event);
		};
		comp.addListener(compKeyListener);
		comp.hideV();
		//只有IE下支持粘贴操作
		if (IS_IE){
			comp.onPaste = function(e){
				if (typeof(grid.onPaste) == 'function'){
					var activeCell = grid.selectedCell;
					var colIndex = activeCell.colIndex;
					var filedName = grid.model.rows[0].getFiledNameByColIndex(colIndex);
					var rowIndex = activeCell.rowIndex; 
					var data = null;
					if (window.clipboardData)
						data = window.clipboardData.getData("text"); 
					var result = grid.onPaste.call(grid, filedName, rowIndex, data);
					if (result == false)
						return false;
					else
						return true;
				}
				else{
					return true;
				}
			};
		}
		
		// 数据改变事件
//		var textListener = new TextListener();
		var textListener = new Listener("valueChanged");
		textListener.func = function(valueChangeEvent) {
			GridComp.compValueChangeFun(grid, valueChangeEvent);
		};
		comp.addListener(textListener);
		
		// 焦点移出事件
//		var compFocusListener = new FocusListener();
		var compFocusListener = new Listener("onBlur");
		compFocusListener.func = function(focusEvent) {
			GridComp.compBlurFun(grid, focusEvent);
			
			var obj = focusEvent.obj;
			if (comp.componentType == "EDITOR") { // EditorComp子项，为dataset设值
				if (oThis.getFocusIndex() == -1)
					return;
				var index = oThis.dataset.nameToIndex(this.id);
				if (index == -1)
					return;
				// 当前值
				var newValue = this.getValue();
				// 旧值
				var oldValue = oThis.dataset.getValueAt(oThis.rowIndex, index);
				if (oldValue != newValue) { // 值发生变化
					// 更新ds内容
					oThis.dataset.setValueAt(oThis.rowIndex, index, newValue);
				}
			} else { // 非EditorComp子项，鼠标移出且内容为空，进行数据校验
				var selectRows = grid.getSelectedRows();
				if (typeof(selectRows) == 'undefined' || selectRows.length == 0)
					return;
				var selectRowIndexs = grid.getSelectedRowIndice();
				for(var i=0; i<selectRows.length; i++){
					var row = selectRows[i];
					if(typeof(row) == 'undefined'){
						continue;
					}
					var colIndex = comp.currColIndex;
					if (colIndex == -1){
						continue;
					}
					var value = row.getCellValue(colIndex);
					//获取当前字段名称
					var keyName = grid.basicHeaders[colIndex].keyName;
					//获取当前字段在dataset中的索引
					var datasetColIndex = row.rowData.dataset.nameToIndex(keyName);
					var resultStr = checkDatasetCell(row.rowData.dataset, value, datasetColIndex, row);
					var cell = grid.basicHeaders[colIndex].dataDiv.cells[selectRowIndexs[i]];
					cell.errorMsg = resultStr;
					var warningIcon = cell.warningIcon;
					if(typeof(warningIcon) == 'undefined'){
						warningIcon = $ce("DIV");
						warningIcon.className = "cellwarning";
						cell.warningIcon = warningIcon;
						cell.style.position = "relative";
					}
					cell.appendChild(warningIcon);
					if (typeof(resultStr) == "string" && resultStr != "") {
						if(typeof(comp.setError) == 'function'){
							comp.setError(true);
						}
						if(typeof(comp.setErrorMessage) == 'function'){
							//comp.setErrorMessage(resultStr);
						}
						if(typeof(comp.setErrorStyle) == 'function'){
							comp.setErrorStyle();
						}
						if(typeof(comp.setErrorPosition) == 'function'){
							var top = cell.offsetTop;
							if(grid.headerDiv && grid.headerDiv.offsetHeight){
								top += grid.headerDiv.offsetHeight;
							}
							if(grid.outerDiv && grid.outerDiv.offsetTop > 0){
								top += grid.outerDiv.offsetTop;
							}
//							var left = cell.offsetWidth * (colIndex) + 10;
							var left = compOffsetLeft(cell, document.body) + GridComp.CELL_LEFT_PADDING;
							comp.setErrorPosition(grid.wholeDiv, left, top - 31);
						}
						warningIcon.style.display = "block";
					}else{
						if(typeof(comp.setError) == 'function'){
							comp.setError(false);
						}
						if(typeof(comp.setErrorMessage) == 'function'){
							comp.setErrorMessage("");
						}
						if(typeof(comp.setNormalStyle) == 'function'){
							comp.setNormalStyle();
						}
						
						//integer类型设置最大值最小值提示框的position
						if(comp.componentType == "INTEGERTEXT"){
							if(typeof(comp.setErrorPosition) == 'function'){
								var top = cell.offsetTop;
								if(grid.headerDiv && grid.headerDiv.offsetHeight){
									top += grid.headerDiv.offsetHeight;
								}
								if(grid.outerDiv && grid.outerDiv.offsetTop > 0){
									top += grid.outerDiv.offsetTop;
								}
								//var left = compOffsetLeft(cell, document.body) - cell.offsetWidth + GridComp.CELL_LEFT_PADDING + 29;
								var left = compOffsetLeft(cell, document.body)  + GridComp.CELL_LEFT_PADDING;
								comp.setErrorPosition(grid.wholeDiv, left, top - 31);
							}	
						}
						
						warningIcon.style.display = "none";
					}	
				}
			}
		};
		comp.addListener(compFocusListener);
	}
};

/**
 * 失去焦点时事件处理
 * 
 * @private
 */
GridComp.compBlurFun = function(oThis, keyEvent) {
	var currCell = oThis.selectedCell;
	if (currCell) {
		var comp = keyEvent.obj;
		// 激活将要激活的控件,因为控件隐藏触发失去焦点是个异步的过程,必须在上一个控件失去焦点后才能触发下一个控件激活
		if (oThis.nextNeedActiveCell) {
			oThis.setCellSelected(oThis.nextNeedActiveCell);
			oThis.nextNeedActiveCell = null;
		}
	}
};

/**
 * 值改变时事件处理
 */
GridComp.compValueChangeFun = function(oThis, valueChangeEvent) {
	var currCell = oThis.selectedCell;
	if (currCell) {
		var comp = valueChangeEvent.obj;
		// 如果输入控件的旧值和改变后的值不一样才改变model的值
//		var newValue = comp.getValue();
		var newValue = valueChangeEvent.newValue ;
		if ((comp.visible &&  comp.visible == true && comp.oldValue != newValue) || comp.componentType == "COMBOBOX") {
			// 下拉框的valuechange事件有问题
			var colIndex = currCell.colIndex;
			var compColIndex = comp.currColIndex;
			if (compColIndex != null && compColIndex != colIndex) return;
			var rowIndex = oThis.getCellRowIndex(currCell);
			if (comp.componentType == "COMBOBOX" && comp.colIndex != null && comp.colIndex != currCell.colIndex)
				colIndex = comp.colIndex;
			// cell编辑后事件
			oThis.model.setValueAt(rowIndex, colIndex, newValue);
			oThis.onAfterEdit(rowIndex, colIndex, comp.oldValue, newValue);
		}
		//如果是多语控件,将多语输入的内容直接保存到ds中
		if(comp.componentType == EditorType.LANGUAGECOMBOBOX.toUpperCase()){
			var gridDs = oThis.model.dataset;
			var rowIndex = oThis.getCellRowIndex(currCell);
			var fieldId = valueChangeEvent.fieldId;
			var fieldId2 = valueChangeEvent.fieldId2;
			if(fieldId!=null && typeof(fieldId)!="undefined"){
				var index = gridDs.nameToIndex(fieldId);
				gridDs.setValueAt(rowIndex, index,comp.getValue());
				if(valueChangeEvent.fieldId2 != ""){
					var languageDefaultIndex = gridDs.nameToIndex(fieldId2);	
					gridDs.setValueAt(rowIndex, languageDefaultIndex, comp.getValue());	
				}
			}
		}
	}
};

/**
 * onkeydown事件处理
 * 
 * @private
 */
GridComp.compKeyDownFun = function(oThis, e) {
	// 当前激活的cell
	e = EventUtil.getEvent();
	//ctrl + v
//	if ((e.ctrlKey) && (e.key == 86) && IS_IE) {
//		if (typeof(this.onBeforePaste) == 'function'){
////			oThis.onBeforePaste(e);
//			var activeCell = oThis.selectedCell;
//			var colIndex = activeCell.colIndex; 
//			var rowIndex = activeCell.rowIndex; 
//			var data = null;
//			if (window.clipboardData)
//				data = window.clipboardData.getData("text"); 
//			this.onBeforePaste.call(this, colIndex, rowIndex, data);
//		}
//	}	
	// end键 home键分别编辑当前行最后一个、第一个cell   自动化测试需要用到end 恩 home键在输入框内控制光标 
//	if ((e.key == 35 || e.key == 36) && !e.shiftKey){
//		var activeCell = oThis.selectedCell;
//		var nextActiveCell;
//		if (oThis.editable) {
//			if (e.key == 35){
//				var cell = oThis.getEditableCellByDirection(activeCell, 1);
//				while (cell != null && activeCell.rowIndex == cell.rowIndex){
//					nextActiveCell = cell;
//					cell = oThis.getEditableCellByDirection(cell, 1);
//				}
//			}else if (e.key == 36){
//				var cell = oThis.getEditableCellByDirection(activeCell, -1);
//				while (cell != null && activeCell.rowIndex == cell.rowIndex){
//					nextActiveCell = cell;
//					cell = oThis.getEditableCellByDirection(cell, -1);
//				}
//			}
//		}
//		if (oThis.showComp && oThis.showComp.input)
//			oThis.showComp.input.onblur();
//		if (nextActiveCell != null) {
//			oThis.hiddenComp();
//			oThis.setCellSelected(nextActiveCell);
//		}
//		
//	}else
	// "tab"键和"shift+tab"键和回车键处理
	if ((e.lfwKey == 9 && e.shiftKey) || e.lfwKey == 9 || e.lfwKey == 13) {
		if(oThis.showComp.componentType == "REFERENCETEXT" && oThis.showComp.divIsShown == true)
			return;
		var activeCell = oThis.selectedCell;
		var nextActiveCell;
		if (oThis.editable) {
			nextActiveCell = oThis.getEditableCellByDirection(activeCell, 1);
			if (e.lfwKey == 9 && e.shiftKey)
				nextActiveCell = oThis.getEditableCellByDirection(activeCell,
						-1);
		}
		// // guoweic: 不可编辑状态下屏蔽tab和shift tab功能
		// else {
		// nextActiveCell = oThis.getVisibleCellByDirection(activeCell, 1);
		// if (e.keyCode == 9 && e.shiftKey)
		// nextActiveCell = oThis.getVisibleCellByDirection(activeCell, -1);
		// }
		if(!IS_IE){//非IE浏览器情况oldvalue使编辑控件每次都执行valuechange
			oThis.showComp.oldValue = null;
		}
		oThis.showComp.input.onblur();
		//grid上最后一行的最后一个可编辑列按回车事件
		if ((e.lfwKey == 13) && !e.shiftKey && (nextActiveCell == null)){
			oThis.hiddenComp();
			oThis.onLastCellEnter(e);
		}
		if (nextActiveCell != null) {
			if (activeCell.rowIndex == nextActiveCell.rowIndex) {
				oThis.hiddenComp();
				oThis.setCellSelected(nextActiveCell);
			}
			// 回车换行
			else if (activeCell.rowIndex != nextActiveCell.rowIndex){
				oThis.model.setRowSelected(nextActiveCell.rowIndex);
				oThis.hiddenComp();
				oThis.setCellSelected(nextActiveCell);
			}
		}
		stopAll(e);
	}
	//下移
	else if (e.lfwKey == 40){
		if(oThis.showComp.componentType == "REFERENCETEXT" && oThis.showComp.divIsShown == true)
			return;
		var activeCell = oThis.selectedCell;
		var nextActiveCell;
		var rowIndex = oThis.getCellRowIndex(activeCell);
		var colIndex = activeCell.colIndex;
		// 如果当前列可编辑，单元格编辑前调用的方法
		if (oThis.model.dataset.editable == true && oThis.basicHeaders[colIndex].columEditable == true) {
			if (oThis.onBeforeEdit(rowIndex + 1, colIndex) == false) 
				return;
		}		
		oThis.showComp.input.onblur();
//		var rowCount = oThis.basicHeaders[colIndex].dataDiv.childElementCount;
		var rowCount = oThis.basicHeaders[colIndex].dataDiv.children.length;
		//最后一条记录
		if (rowIndex == rowCount - 1){
			// 最后一行时，触发onLastCellEnter事件
			oThis.onLastCellEnter(e);
		}
		else{
			nextActiveCell = oThis.basicHeaders[colIndex].dataDiv.cells[rowIndex + 1];
			if (nextActiveCell){
				oThis.model.setRowSelected(nextActiveCell.rowIndex);
				oThis.hiddenComp();
				oThis.setCellSelected(nextActiveCell);
			}
		}
	}
	//上移
	else if (e.lfwKey == 38){
		if(oThis.showComp.componentType == "REFERENCETEXT" && oThis.showComp.divIsShown == true)
			return;
		var activeCell = oThis.selectedCell;
		var nextActiveCell;
		var rowIndex = oThis.getCellRowIndex(activeCell);
		var colIndex = activeCell.colIndex;
		var rowCount = oThis.basicHeaders[colIndex].dataDiv.cells.length;
		// 如果当前列可编辑，单元格编辑前调用的方法
		if (oThis.model.dataset.editable == true && oThis.basicHeaders[colIndex].columEditable == true) {
			if (oThis.onBeforeEdit(rowIndex - 1, colIndex) == false) 
				return;
		}
		oThis.showComp.input.onblur();
		nextActiveCell = oThis.basicHeaders[colIndex].dataDiv.cells[rowIndex - 1];
		if (nextActiveCell){
			oThis.model.setRowSelected(nextActiveCell.rowIndex);
			oThis.hiddenComp();
			oThis.setCellSelected(nextActiveCell);
		}
	}

};

/**
 * 回车事件处理
 * 
 * @private
 */
GridComp.compEnterFun = function(oThis) {
	var activeCell = oThis.selectedCell;
	// 获取下一个将要激活的cell
	var nextActiveCell = oThis.getEditableCellByDirection(activeCell, 1);
	if (nextActiveCell != null) {
		if (activeCell.rowIndex != nextActiveCell.rowIndex) // 选中新行
			oThis.model.setRowSelected(nextActiveCell.rowIndex);
		oThis.nextNeedActiveCell = nextActiveCell;
		oThis.hiddenComp();
	}
};

GridComp.prototype.getCellRowIndex = function(cell){
	if(cell && cell.parentNode){
		var nodes = cell.parentNode.childNodes;
		for(var i = 0; i < nodes.length; i ++){
			if(nodes[i] == cell)
				return i;
		}
	}
	return 0;
};

/**
 * 隐藏当前显示的控件
 * 
 * @private
 */
GridComp.prototype.hiddenComp = function() {
	if (this.showComp != null) {
			this.showComp.hideV();
			if (typeof ComboComp != "undefined" && this.showComp instanceof ComboComp){
				this.showComp.oldValue = null;
				this.showComp.selectedIndex = -1;
			}
		if  (this.showComp.extend)
			this.showComp.destroySelf();
		this.showComp = null;
		// 将激活控件的记录变量设为null(只有编辑控件真正的隐藏才会设置当前的激活cell为null)
		this.currActivedCell = null;
	}
};

/**
 * 得到最顶层header的默认初始宽度
 * 
 * @param header
 *            最顶层header
 * @private
 */
GridComp.prototype.getHeaderDefaultWidth = function(header) {
	if (header.parent != null)
		return null;

	var headerWidth = 0;
	// 单表头
	if (header.children == null)
		headerWidth = header.width;
	// 多表头
	else {
		var basicHeaders = header.basicHeaders;
		for (var j = 0, count = basicHeaders.length; j < count; j++) {
			if (basicHeaders[j].isHidden == false)
				headerWidth += basicHeaders[j].width;
		}
	}
	return headerWidth;
};

/**
 * 创建表格行
 * 
 * @private
 * @param table
 *            要增加的行的table的引用
 * @param posi
 *            要增加行的位置
 */
GridComp.prototype.addTableRow = function(tbody, posi) {
	if (posi == null || isNaN(posi))
		posi = tbody.rows.length;
	tbody.insertRow(posi);
	return tbody.rows[posi];
};

/**
 * 得到dataOuterDiv是否出了纵向滚动条
 * 
 * @private
 */
GridComp.prototype.isDataDivVScroll = function() {
	return isDivVScroll(this.dataOuterDiv);
};

/**
 * 得到dataOuterDiv是否出了横向滚动条
 * 
 * @private
 */
GridComp.prototype.isDataDivScroll = function() {
	return isDivScroll(this.dataOuterDiv);
};

/**
 * 根据当前grid中的行数判断是否出纵向滚动条
 * 
 * @return} true|false
 * @private
 */
GridComp.prototype.isVScroll = function() {
	if(this.flowmode)
		return false;
	var num = this.getRowsNum();
	if (num > 0) {
		// 是否竖直滚动
		if (num * this.rowHeight > this.constant.outerDivHeight
				- this.constant.headerHeight + 2)
			return true;
		else
			return false;
	} else if (num == 0)
		return false;
};

/**
 * 根据数据区(数字列+静态表头+动态表头)真正的宽度判断当前是否出横向滚动条
 * 
 * @return boolean 是否出横向滚动条
 * @private
 */
GridComp.prototype.isScroll = function() {
	var gridRealWidth = this.getDynamicTableDivRealWidth(true)
			+ this.getDynamicTableDivRealWidth(false)
			+ this.constant.rowNumHeaderDivWidth + 5;
	if (this.isMultiSelWithBox)
		gridRealWidth += GridComp.SELECTCOLUM_WIDTH;
	if (gridRealWidth > this.constant.outerDivWidth)
		return true;
	else
		return false;
};

/**
 * 插入一行 用户调用此方法插入一行数据
 * 
 * @param row
 *            GridCompRow
 * @param index
 *            插入的位置
 */
GridComp.prototype.insertRow = function(row, index) {
	if (this.model == null)
		this.model = new GridCompModel();
	if (row == null || GridCompRow.prototype.isPrototypeOf(row))
		return this.model.insertRow(row, index);
	else
		throw new Error("Row must be the instance of GridCompRow or null!");
};

/**
 * 增加一行 用户调用此方法增加一行数据
 * 
 * @param row
 *            GridCompRow
 */
GridComp.prototype.addRow = function(row) {
	if (this.model == null)
		this.model = new GridCompModel();
	if (row == null || GridCompRow.prototype.isPrototypeOf(row))
		return this.model.addRow(row);
	else
		throw new Error("Row must be the instance of GridCompRow or null!");
};

/**
 * 增加一组行
 * 
 * @param rows
 *            GridCompRow数组
 */
GridComp.prototype.addRows = function(rows) {
	if (rows != null) {
		for (var i = 0; i < rows.length; i++)
			this.addRow(rows[i]);
	}
};

/**
 * Model中插入行时触发此方法.
 * 
 * @param index
 *            插入行的位置
 * @private
 */
GridComp.prototype.fireRowInserted = function(index, level, parentRowIndex) {
	if(this.model.getRows().length > (index + 1)){
  		this.paintRows();
  		return;
 	}
	var gridHeight = this.constant.outerDivHeight;
	// 可见区域高度
	var areaHeight = 0;
	if (this.scroll)
		areaHeight = gridHeight - this.constant.headerHeight
				- GridComp.SCROLLBAE_HEIGHT;
	else
		areaHeight = gridHeight - this.constant.headerHeight;

	if (areaHeight < 0)
		areaHeight = 0;

	// 每一个header.dataDiv的高度增加一个行高
	var basicHeaders = this.basicHeaders;

	var row = this.model.getRow(index);
	if (level != null)
		row.level = level + 1;
	this.setHeadersOffsetWidth();
	initLayoutMonitorState();
	this.addOneRow(row, index,
					this.dataOuterDiv.scrollLeft, 
					this.rowHeight, this.model.getRowsCount(), parentRowIndex);
	this.clearHeadersOffsetWidth();
	if(this.scrollState){
		$("#"+this.outerDivId).perfectScrollbar('updateAllandLeft',this.outerDivId);
	}
	executeLayoutMonitor();
};

/**
 * 创建行状态和行标div
 * 
 * @private
 */
GridComp.prototype.andLineStateAndColNum = function(rowCount, index, rowHeight,
		row) {
	var isOdd = this.isOdd(index);
	// 增加数字行号
	if (this.isShowNumCol) {
		var $n = $ce("div");
		$n.className = isOdd ? "gridcell_odd" : "gridcell_even";
		var style = $n.style;
		$n.id = "numline" + index;
		$n.rowIndex = index;
		style.height = (rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH) + "px";
		style.lineHeight = (rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH) + "px";
		$n.innerHTML = "<center>" + (index + 1) + "</center>";
		if(typeof(this.rowNumDiv.cells[index]) == "undefined"){
			this.rowNumDiv.appendChild($n);
		}else{
			this.rowNumDiv.insertBefore($n, this.rowNumDiv.cells[index]);
		}
	}

	// 创建行状态列内的div
	var $l = $ce("div");
	$l.className = "row_state_div";
	var stl = $l.style;
	//$l.id = "linestate" + (rowCount + index);
	stl.height = rowHeight-GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
	
	if(this.lineStateColumDiv.childNodes.length == index)
		this.lineStateColumDiv.appendChild($l);
	else
		this.lineStateColumDiv.insertBefore($l, this.lineStateColumDiv.childNodes[index]);
//	this.lineStateColumDiv.appendChild($l);

	if (this.isShowNumCol){
		this.rowNumDiv.cells.splice(index, 0, $n);
		var len = this.rowNumDiv.cells.length;
		for(var i=index+1; i<len; i++){
			if(typeof(this.rowNumDiv.cells[i]) != "undefined"){
				this.rowNumDiv.cells[i].rowIndex = i;
				this.rowNumDiv.cells[i].innerHTML = "<center>" + (i + 1) + "</center>";
			}
		}
	}
	this.lineStateColumDiv.cells.splice(index, 0, $l);
	if (row.rowData.state == DatasetRow.STATE_NEW)
		this.lineStateColumDiv.cells[index].className = "row_state_div row_add_state";
	else
		this.lineStateColumDiv.cells[index].className = "row_state_div";
};

/**
 * 设置复选框选中状态
 * 
 * @param checked:是否选中
 * @param rowIndex:行号
 * @private addOneRow()的时候调用
 */
GridComp.prototype.setCheckBoxChecked = function(checked, rowIndex) {
	// 分组复选框
	if (this.groupHeaderIds.length > 0 && this.isMultiSelWithBox == true && this.isGroupWithCheckbox == true) { 
		var groupRowIds = this.model.rows[rowIndex].groupRowIds;
		if (groupRowIds != null) {
			for (var i = groupRowIds.length - 1; i >= 0; i--) {
				var index = this.model.getRowIndexById(groupRowIds[i]);
				if (index != -1) {
					if (checked == true) {
						this.model.addRowSelected(index);
					} else {
						this.model.setRowUnSelected(index);
					}
				}
			}
		}
	} else {
		if (checked == true) {
			//递归勾选所有行
			if (this.isCheckingParent != null && this.isCheckingParent == true)
				return;
			this.expandAndSeclectNodesByRowIndex(rowIndex);
			//this.checkBoxModel = 2  时要勾选父
			if (this.model.treeLevel != null  && this.checkBoxModel == 2){
				this.isCheckingParent = true;
				var pIndex = this.model.getParentRowIndex(rowIndex);
				while (pIndex != null && pIndex != -1){
					this.model.addRowSelected(pIndex);
					pIndex = this.model.getParentRowIndex(pIndex);
				}
				this.isCheckingParent = false;
			}
			//this.expandNodesByRowIndex(rowIndex);
			//this.model.addRowSelected(rowIndex);
			this.rowSelected(rowIndex);
			this.setFocusIndex(rowIndex);
		} else {
			this.unselectNodesByRowIndex(rowIndex);
			//this.model.setRowUnSelected(rowIndex);
			//this.loseFocusIndex();
			this.setFocusIndex(rowIndex);
		}
	}
};

/**
 * 给表格增加一行
 * 
 * @param row(model数据中的一行)
 * @param len
 *            即headers.length
 * @param isInsertInMiddle
 *            向中间插入行
 * @private
 */
GridComp.prototype.addOneRow = function(row, index, scrollLeft, rowHeight, rowCount, parentRowIndex) {
//	initLayoutMonitorState();
	if(this.noRowsDiv){
		this.noRowsDiv.style.display = "none";
		//由于自定滚动条所以设置为marginBottom 为0  默认为17px
		this.dynamicColumDataDiv.style.marginBottom = "0px";
		//由于自定滚动条所以设置为hidden
		this.dataOuterDiv.style.overflow = "hidden";
		this.dataOuterDiv.style.display = "block";
	}
	var isOdd = this.isOdd(index);
	var tempHeaders = [];
	var fixedheaderWidth = this.constant.fixedHeaderDivWidth;
	var scrollTop = 0;
	var oThis = this;
	// 判断是否出竖直滚动条,将headerDiv的宽度缩小17px
	if (this.firstVScroll == false) {
		if (this.isVScroll()) {
			var barWidth = GridComp.SCROLLBAE_HEIGHT;
			var dynHeaderWidth = this.constant.outerDivWidth - fixedheaderWidth
					- barWidth - 1 + scrollLeft;
			if (dynHeaderWidth > 0)
				this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
			this.headerDiv.defaultWidth = this.constant.outerDivWidth
					- barWidth - 1;
			this.firstVScroll = true;
		}
	}
	this.andLineStateAndColNum(rowCount, index, rowHeight, row);

	var checkDiv = null;
	var checkBox = null;
	if (this.isMultiSelWithBox) {
		// 向固定选择列增加选择框
		checkDiv = $ce("div");
		checkDiv.className = isOdd
				? "fixed_selectcolum_checkbox_div_odd"
				: "fixed_selectcolum_checkbox_div_even";
//		checkDiv.rowIndex = index;
		checkDiv.editorType = "CheckBox";
		if(this.hasBorder)
			$(checkDiv).addClass("checkDivExtendCss");
		//checkDiv.style.top = rowHeight * index + "px";
		checkDiv.style.left = "0px";
		checkDiv.style.width = GridComp.SELECTCOLUM_WIDTH + "px";
		checkDiv.style.height = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH
				+ "px";
		checkDiv.style.lineHeight = rowHeight
				- GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
		checkDiv.style.overflow= "hidden";
		checkBox = $ce("INPUT");
		checkBox.type = "checkbox";
		checkBox.style.marginTop = "5px";
		checkBox.style.display= "block";
		checkBox.style.marginLeft= "8px";
		checkDiv.appendChild(checkBox);
		this.selectColumDiv.appendChild(checkDiv);
		if(this.selectColumDiv.childNodes.length == index)
			this.selectColumDiv.appendChild(checkDiv);
		else
			this.selectColumDiv.insertBefore(checkDiv, this.selectColumDiv.childNodes[index]);
		
//		if (this.selectColumDiv.divWidth != null) 
//			this.fixedColumDiv.style.height = this.selectColumDiv.divWidth + "px";
//		else	
//			this.fixedColumDiv.style.height = this.selectColumDiv.offsetHeight + "px";
			
		// 设置选中状态
		checkBox.checked = this.model.isRowSelected(index);
		// 通知model选中该行
		checkBox.onmousedown = function(e) {
			// 置空当前选择单元格
			oThis.selectedCell = null;
			oThis.oldCell = null;
			var rowIndex = oThis.getCellRowIndex(this.parentNode);
			// 设置选中状态
			if(oThis.model.rows[rowIndex].loadImg && oThis.model.rows[rowIndex].loadImg.plus == true){
				this.tempChecked = this.checked;
				oThis.setCheckBoxChecked(!this.checked, rowIndex);
			}else{
				/*if(typeof(this.tempChecked) == "boolean"){//plus从true到false过程中的第一次
					this.tempChecked = null;
				}else{*/
					oThis.setCheckBoxChecked(!this.checked, rowIndex);
				/*}*/
			}
			// 隐藏编辑框
			oThis.hiddenComp();
		};
		// 并阻止事件的进一步向上传播
		checkBox.onclick = function(e) {
			e = EventUtil.getEvent();
			// 阻止默认选中事件和事件上传,放在onmousedown中不会起作用,但通知model行选中必须放在onmousedown中!
			stopDefault(e);
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};

		this.selectColumDiv.cells.splice(index, 0, checkDiv);
//		if (GridComp.tempCount < 5)
//			alert('t2-t1='+ (t2-t1));

	}

	// 动态表头长度
	var dynHeaderLen = this.defaultDynamicHeaders.length;
	var lastHeader = this.getLastDynamicVisibleHeader();
	var firstHeader = this.getFirstDynamicVisibleHeader();
	var rowsCount = this.model.getRowsCount();
//	this.setHeadersOffsetWidth();

	//存储一行的所有cell,行渲染用
	if (this.rowCells == null)
	var rowCells = new Array();
	
	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		header.parentRowIndex = parentRowIndex;
		if (header.isHidden == true)
			continue;
		var cell = $ce("div");
		rowCells.push(cell);
		cell.rowIndex = index;
		if(row.level != null)
			cell.level = row.level;
		if(row.hasChildren && row.hasChildren != null)
			cell.hasChildren = row.hasChildren;
			
		// 记录cell对应ds中的第几列
		cell.colIndex = i;
		cell.editorType = header.editorType;
		cell.className = isOdd ? "gridcell_odd" : "gridcell_even";
		if(this.hasBorder)
			$(cell).addClass("cellExtendCss");
		if(this.editable && header.columEditable == false)
			cell.style.background = "#e4e4e4";

		cell.onmouseover = function() {
			oThis.showTipMessage(this);
			oThis.gridRowMouseOver(this);
		};
		cell.onmouseout = function() {
			oThis.hideTipMessage();
			oThis.gridRowMouseOut(this);
		};
		var cellStyle = cell.style;
		cellStyle.width = "100%";
		if (this.autoRowHeight == false){
			cellStyle.height = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
		}
		else{
			var minHeight = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH;
			cellStyle.minHeight = minHeight + "px";
			if(minHeight > this.rowMinHeight[index] || typeof(this.rowMinHeight[index]) == "undefined"){
				this.rowMinHeight[index] = minHeight;
			}
			this.defaultRowMinHeight[index] = minHeight;
		}	
		// 在div中只有一行的情况下设置行距等于div高度,可以使div中的文字距中,从而不需要设置paddingTop
		cellStyle.lineHeight = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
		cellStyle.paddingLeft = GridComp.CELL_LEFT_PADDING + "px";
		cellStyle.paddingRight = GridComp.CELL_RIGHT_PADDING +  "px";
		/* 标题中对齐
		 * 文本类、参照类、枚举类的左对齐
		 * 数字类的右对齐
		 * 日期/布尔类的居中 */
		if(header.textAlign == ""){
			setTextAlign = "left";
		}else{
			setTextAlign = header.textAlign;
		}
		if(typeof(header.dataType) == "string"){
			switch(header.dataType){
				case "UFDateTime":
				case "UFDate":
				case "UFTime":
				case "Date":
				case "ShortDate":
				case "Boolean":
				case "boolean":
				case "UFBoolean":
					cellStyle.textAlign = "center";
					break;
				case "Integer":
					if(header.editorType && header.editorType == "ComboBox"){
						//下拉框当做文本类处理
						cell.style.textAlign = "left";
						break;
					}
				case "int":
				case "Double":
				case "double":
				case "UFDouble":
				case "Float":
				case "float":
				case "BigDecimal":
				case "Decimal":
				case "Long":
				case "long":
					cellStyle.textAlign = "right";
					break;
				default:
					cellStyle.textAlign = setTextAlign;
					break;
			}
		}else{
			// 根据header中textAlign属性设置文字在cell中的位置.
			cellStyle.textAlign = setTextAlign;
		}
		if(parentRowIndex != null){
			cell.parentCell = header.dataDiv.childNodes[parentRowIndex];
		}
		// 先将cell放入dataDiv,以便于子可以获取各种定位属性
		if(header.dataDiv != null){
			if(header.dataDiv.childNodes.length == index){
				header.dataDiv.appendChild(cell);
			} else{
				header.dataDiv.insertBefore(cell, header.dataDiv.childNodes[index]);
			}
		}
		if (header.dataDivWidth != null && header.dataDivWidth > 0){
			cellStyle.width = (header.dataDivWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING) + "px";
		} else if(header.dataDiv && header.dataDiv.offsetWidth > 0){
			cellStyle.width = (header.dataDiv.offsetWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING) + "px";
		} else if(header.width && header.width > 0){
			cellStyle.width = (header.width - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING) + "px";
		}
		// 如果分组显示,新分组开始的标示
		var newGroupBegin = false;
		// 如果分组显示,新分组结束的标示
		var newGroupEnd = false;
		if (header.isGroupBy == true) {
			// 第一个分组的header
			if (header.keyName == this.groupHeaderIds[0]) {
				if ((index > 0 && row.getCellValue(i) != this.model.rows[index - 1].getCellValue(i)) || index == 0) {
					newGroupBegin = true;
					// 为Model设置临时的新分组第一行
					this.model.newGroupRow = this.model.rows[index];
					// 第一行的相关分组行数组，用于级联选中和反选
					this.model.rows[index].groupRowIds = new Array();
					var rowId = this.model.rows[index].rowData.rowId;
					this.model.rows[index].groupRowIds.push(rowId);
				}
			}
			/*
			 * 后面分组的header开始新组的条件:
			 * 1.第一行肯定要分组 
			 * 2.此cell数据和上一行该列的cell数据不一样
			 * 3.上一个分组header列的该行的cell数据和此列上一行的不一致则也要分组
			 */
			else {
				if (index == 0 || (row.getCellValue(i) != this.model.rows[index - 1].getCellValue(i)) || this.isCurrCellNeedNewGroup(row, index, i))
					newGroupBegin = true;
			}
			// 判断是否为新分组的最后一个cell
			if ((index <= (this.model.rows.length - 2) && row.getCellValue(i) != this.model.rows[index + 1].getCellValue(i))|| index == this.model.rows.length - 1){
				newGroupEnd = true;
			}
		}
		// 动态列
		if (header.isFixedHeader == false) {
			// 单表头
			if (header.children == null) {
				if (dynHeaderLen > 0 && header != firstHeader && header != lastHeader) {
				} else {
					// 动态列最后一列为单表头的情况
					if (dynHeaderLen > 0 && header == lastHeader) {
						if (header.isGroupBy) {
						} else {
							if (header.dataDivWidth != null && header.dataDivWidth > 0)
								cellStyle.width = (header.dataDivWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - 1) + "px"; //最后一列border为1 
							else if(header.dataDiv && header.dataDiv.offsetWidth > 0){
								cellStyle.width = (header.dataDiv.offsetWidth - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - 1) + "px"; //最后一列border为1 
							}
							else if(header.width && header.width > 0){
								cellStyle.width = (header.width - GridComp.CELL_LEFT_PADDING - GridComp.CELL_RIGHT_PADDING - 1) + "px"; //最后一列border为1
							}
							var bottomWidth = getStyleAttribute(cell, "border-bottom-width");
							var bottomStyle = getStyleAttribute(cell, "border-bottom-style");
							var bottomColor = getStyleAttribute(cell, "border-bottom-color");
							if (bottomWidth)
								cell.style.borderRightWidth = bottomWidth;
							if (bottomStyle)
								cell.style.borderRightStyle = bottomStyle;
							if (bottomColor)
								cell.style.borderRightColor = bottomColor;
								
							cell.style.borderRightColor = "#D1DFE4";
							cell.style.borderRightStyle = "solid";
							cell.style.borderRightWidth = "1px";
						}
					}
				}
			} else {
				if (dynHeaderLen > 0) {
					if (header != firstHeader && header != lastHeader) {
						cell.className = isOdd ? "gridcell_odd" : "gridcell_even";
					}
					// 动态列第一列为多表头的情况
					else if (header == firstHeader) {
						cell.className = isOdd ? "gridcell_odd" : "gridcell_even";
						if (j == 0) {
							// 多表头最后一列不显示右边线
							cell.style.borderLeft = "0";
						}
					}
					// 动态列最后一列为多表头的情况
					else if (header == lastHeader) {
						cell.className = isOdd ? "gridcell_odd" : "gridcell_even";
					}
				}
			}
		}
		// 固定列
		else {
			// 单表头
			if (header.children == null) {
				cell.className = isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even";
			} else {
				cell.className = "fixed_colum_grid_cell";
			}
		}
		if(this.hasBorder)
			$(cell).addClass("cellExtendCss");

		// 渲染cell
//		var childNode = null;
		
		// 判断是否为分组显示的列
		if (!header.isGroupBy) {
//			childNode = header.renderType.render.call(this, index, i, row.getCellValue(i), header, cell, parentRowIndex);
			this.renderCell(header.renderType, index, i, row.getCellValue(i), header, cell, parentRowIndex);
		}else if (header.isGroupBy == true) {
			// 新组开始才画该cell
			var realValue = null;
			if (newGroupBegin){
				realValue = row.getCellValue(i);
			}
//			childNode = header.renderType.render.call(this, index, i, realValue, header, cell, parentRowIndex);
			this.renderCell(header.renderType, index, i, realValue, header, cell, parentRowIndex);
			if (checkDiv != null && checkBox != null && this.isGroupWithCheckbox) {
				if (!newGroupBegin) {
					// 第一个分组的header
					if (header.keyName == this.groupHeaderIds[0]) {
						// 去掉除新组第一个cell以外的复选框
						checkDiv.removeChild(checkBox);
						// 向第一个分组行增加相关分组行数据，用于级联选中和反选
						var rowId = this.model.rows[index].rowData.rowId;
						this.model.newGroupRow.groupRowIds.push(rowId);
					}
				}
			}
			// 分组非最后一个cell去掉底边
			if (!newGroupEnd) {
				var bottomColor = getStyleAttribute(cell, "background-color");
				//cell.style.borderBottomColor = "#ffffff";
				//cell.style.backgroundColor = "#ffffff";
			}
			//在点击新增时要清除上一行的borderBottomColor
			else{
				if (index != 0){
					var preCell = this.basicHeaders[i].dataDiv.cells[index - 1];
					if (preCell.style.borderBottomColor == null || preCell.style.borderBottomColor == ""){
						var bottomColor = getStyleAttribute(preCell, "background-color");
						//preCell.style.borderBottomColor = "#ffffff";
						//cell.style.backgroundColor = "#ffffff";
					}
				}
			}
			// 分组非第一个cell,改变前一个cell的背景色和下边框,当前cell的背景色.
			if(!newGroupBegin){
				if (index != 0){
					var preCell = this.basicHeaders[i].dataDiv.cells[index - 1];
					preCell.style.borderBottomColor = "#ffffff";
					preCell.style.backgroundColor = "#ffffff";
				}
				cell.style.backgroundColor = "#ffffff";
			}
		}

		// 根据header中的bgcolor属性设置列背景色
		if (header.columBgColor != null && header.columBgColor != "" && cell.style.backgroundColor != "#ffffff"){
			cellStyle.backgroundColor = header.columBgColor;
		}
		
		if (header.textColor != null && header.textColor != ""){
			cellStyle.color = header.textColor;
		}
		if(header.dataDiv != null && header.dataDiv.cells != null)
			header.dataDiv.cells.splice(index, 0, cell);
		
		//最后调整高度
		if (this.autoRowHeight == true)	{
			this.adjustRowHeight(index, cell);	
		}
	}
	
	//行渲染
	this.rowRender.render.call(this, row, rowCells);
	if (this.isMultiSelWithBox) {
		if(this.model.isRowSelected(index) == true)
			this.rowSelected(index);
	}
	
//	this.clearHeadersOffsetWidth();
//	executeLayoutMonitor();
};

/**
 * 
 * 渲染单元格
 * @param {} rowIndex
 * @param {} colIndex
 * @param {} value
 * @param {} header
 * @param {} cell
 * @param {} parentRowIndex
 * @private
 */
GridComp.prototype.renderCell = function(render,rowIndex, colIndex, value, header, cell, parentRowIndex) {
	if (typeof value == "string"){
		value = value.replace(/\&/g,"&amp;");
		value = value.replace(/\</g,"&lt;");
	}
	
	render.render.call(this, rowIndex, colIndex, value, header, cell, parentRowIndex);
	if (header.required && this.editable == true  && header.columEditable == true){
		var length = cell.children.length;
		if(length > 0){
			//移除cell中存在的必输项SPAN
			for(var count = length - 1; count >= 0; count--){
				if(cell.children[count]){
					if(cell.children[count].tagName.toUpperCase() == 'SPAN' && cell.children[count].className.toLowerCase() == 'requiredstyle'){
						cell.removeChild(cell.children[count]);
						break;
					}
				}
			}
		}	
		if (isNull(value)){
			var requiredStar = $ce("span");
			requiredStar.className = "requiredstyle";
			requiredStar.innerHTML = "*";
			cell.appendChild(requiredStar);
		}
	}
};

GridComp.prototype.setTipSticky = function() {
	this.tipSticky = true;
};
GridComp.prototype.hideTipMessage = function(force) {
	// this.tipSticky = true;
	if (!this.tipSticky || force) {
		if (GridComp.tipDiv)
			GridComp.tipDiv.style.display = "none";
	}
	if (this.tipRt)
		clearTimeout(this.tipRt);
};
GridComp.prototype.showTipMessage = function(cell) {
	if(IS_IPAD)
		return;
	if (this.tipRt != null)
		clearTimeout(this.tipRt);
	var title = cell.tip;
	if (title == null || title == "")
		return;
	if(cell.editorType != null && (cell.editorType == "CheckBox" || cell.editorType == "PwdText"))
		return;
	var left = compOffsetLeft(cell, document.body) + 1;
	var top = compOffsetTop(cell, document.body) - compScrollTop(cell, document.body) + GridComp.ROW_HEIGHT;
	var width = (cell.offsetWidth - 2) + "px";
	/*
	this.tipRt = setTimeout(
		"GridComp.doShowTipMessage(" + left + "," 
								     + top + ",'" 
								     + width + "','" 
								     + encodeURIComponent(title)+ "')", 500);
	//如果用上面的方法，需要正则表达式替换英文的单引号，替换成中文引号
	if(title.indexOf("'")>-1){
		title = title.replace(/\'/g,"‘");
	}
	如果用下面方法调用，就不需要替换英文单引号了
	*/
	this.tipRt = setTimeout(function(){
		GridComp.doShowTipMessage(left,top,width,encodeURIComponent(title));
	},500);			
};

GridComp.doShowTipMessage = function(left, top, width, title) {
	var div = GridComp.tipDiv;
	if (GridComp.tipDiv == null) {
		div = $ce("DIV");
		div.className = "tip_message";
		div.style.zIndex = getZIndex();
		GridComp.tipDiv = div;
		document.body.appendChild(div);
		GridComp.popwindow(div);
	}
	div.style.display = "";
	div.style.left = left + "px";
	div.style.top = (top - 7) + "px";
	div.centerDiv.innerHTML = decodeURIComponent(title);
	var height = getTextHeight(title);
	//div.style.height = height + 36 + "px";
};

GridComp.popwindow = function(parentDiv){
	var leftTopDiv = $ce("DIV");
	leftTopDiv.className = 'left_top_div';
	var centerTopDiv = $ce("DIV");
	centerTopDiv.className = 'center_top_div';
	var rightTopDiv = $ce("DIV");
	rightTopDiv.className = 'right_top_div';
	
	var leftCenterDiv = $ce("DIV");
	leftCenterDiv.className = 'left_center_div';
	var centerDiv = $ce("DIV");
	centerDiv.className = 'center_div';
	var rightCenterDiv = $ce("DIV");
	rightCenterDiv.className = 'right_center_div';
	
	var leftBottomDiv = $ce("DIV");
	leftBottomDiv.className = 'left_bottom_div';
	var centerBottomDiv = $ce("DIV");
	centerBottomDiv.className = 'center_bottom_div';
	var rightBottomDiv = $ce("DIV");
	rightBottomDiv.className = 'right_bottom_div';
	
	parentDiv.appendChild(centerDiv);
	parentDiv.appendChild(leftTopDiv);
	parentDiv.appendChild(centerTopDiv);
	parentDiv.appendChild(rightTopDiv);
	parentDiv.appendChild(leftCenterDiv);
	parentDiv.appendChild(rightCenterDiv);
	parentDiv.appendChild(leftBottomDiv);
	parentDiv.appendChild(centerBottomDiv);
	parentDiv.appendChild(rightBottomDiv);
	
	parentDiv.centerDiv = centerDiv;
};

/**
 * 设置每个Header的OffsetWidth属性，避免增行时反复计算
 * 
 * @private
 */
GridComp.prototype.setHeadersOffsetWidth = function() {
	var headers = this.model.getHeaders();
	for (var i = 0, n = headers.length; i < n; i++) {
		var header = headers[i];
		if (header.dataDiv != null) {
			header.dataDivWidth = header.dataDiv.offsetWidth;
			var basicHeaders = header.basicHeaders;
			if (basicHeaders != null) {
				for (var j = 0, m = basicHeaders.length; j < m; j++) {
					var basicHeader = basicHeaders[j];
					basicHeader.dataDivWidth = basicHeader.dataDiv.offsetWidth;
				}
			}
		}
	}
	if (this.selectColumDiv)
		this.selectColumDiv.divWidth = this.selectColumDiv.offsetWidth; 
};

/**
 * 清空每个Header的OffsetWidth属性
 * 
 * @private
 */
GridComp.prototype.clearHeadersOffsetWidth = function() {
	var headers = this.model.getHeaders();
	for (var i = 0, n = headers.length; i < n; i++) {
		var header = headers[i];
		header.dataDivWidth = null;
		var basicHeaders = header.basicHeaders;
		if (basicHeaders != null) {
			for (var j = 0, m = basicHeaders.length; j < m; j++) {
				basicHeaders[j].dataDivWidth = null;
			}
		}
	}
	if (this.selectColumDiv)
		this.selectColumDiv.divWidth = null; 
};

/**
 * 鼠标移入后改变行样式
 * 
 * @private
 */
GridComp.prototype.gridRowMouseOver = function(cell) {
	//TODO IE9有bug
	if (IS_IE9) return;
	var rowIndex = this.getCellRowIndex(cell);
	if(rowIndex == this.getFocusIndex()){
		//焦点行不改变样式
		return;
	}
	// 改变当前行的外观
	for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
		var header = this.basicHeaders[i];
		if (header.isHidden == false) {
			if(header.dataDiv!= null && header.dataDiv.cells != null)
				var tempCell = header.dataDiv.cells[rowIndex];
			if (tempCell != null) {
				if (!tempCell.isErrorCell) {
					var isOdd = this.isOdd(rowIndex);
					if(!(typeof(tempCell.className) == 'string' && tempCell.className.indexOf("cell_select") != -1)){
						//当前行不是选中行,不是焦点行,改变外观.
						tempCell.oldClassName = tempCell.className;
						tempCell.className = isOdd ? "gridcell_odd gridcell_mouseover" : "gridcell_even gridcell_mouseover";
						if(this.hasBorder)
							$(tempCell).addClass("cellExtendCss");
					}
				}
			}
		}
	}
};

/**
 * 鼠标移出后改变行样式
 * 
 * @private
 */
GridComp.prototype.gridRowMouseOut = function(cell) {
	//TODO IE9有bug
	if (IS_IE9) return;
	var rowIndex = this.getCellRowIndex(cell);
	if(rowIndex == this.getFocusIndex()){
		//焦点行
		for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
			var header = this.basicHeaders[i];
			if (header.isHidden == false) {
				if(header.dataDiv != null && header.dataDiv.cells != null)
					var tempCell = header.dataDiv.cells[rowIndex];
				if (tempCell != null) {
					if (!tempCell.isErrorCell) {
						tempCell.className = tempCell.className.replace(" gridcell_mouseover", "");
					}
				}
			}
		}
		return;
	}
	var selectedRowIndice = this.getSelectedRowIndice();
	if (selectedRowIndice && selectedRowIndice.indexOf(rowIndex) != -1) {
		this.rowSelected(rowIndex);
		return;
	}
	for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
		var header = this.basicHeaders[i];
		if (header.isHidden == false) {
			if(header.dataDiv && header.dataDiv.cells)
				var tempCell = header.dataDiv.cells[rowIndex];
			if (tempCell != null) {
				if (!tempCell.isErrorCell) {
					tempCell.className = tempCell.oldClassName;
				}
			}
		}
	}
};

/**
 * 判断该cell是否应该启动新的分组
 * 
 * @private
 */
GridComp.prototype.isCurrCellNeedNewGroup = function(row, rowIndex,
		curGroupColIndex) {
	// 分组列列号数组
	var indice = this.groupHeaderColIndice;
	if (indice != null && indice.length > 0) {
		// 获取上一个最近的分组
		var startIndex = indice.indexOf(curGroupColIndex) - 1;
		for (var i = startIndex; i >= 0; i--) {
			if ((row.getCellValue(indice[i]) != this.model.rows[rowIndex - 1]
					.getCellValue(indice[i])))
				return true;
		}
		return false;
	} else
		return false;
};

/**
 * 若选中行不在选中区域,滚动行到选中的行
 * 
 * @param index
 *            行位置
 * @private
 */
GridComp.prototype.scrollToSelectedRow = function(index) {
	// 滚动时将当前选中的cell置空,则在index行选中时会清除当前行的选中cell的外观
	this.selectedCell = null;
	// 滚动的时候隐藏当前显示的控件
	this.hiddenComp();

	// 得到显示区域的top,bottom值
	var displayCont = this.calcuDisplayRowNum();
	var displayContTop = displayCont[0] + 2;
	// 因为显示的特殊需要此displayContBottom的值要大于实际的显示区域的底部值,故减去2
	var displayContBottom = displayCont[1] - 2;
	var sRowH = index * this.rowHeight;

	// guoweic: modify start 2009-10-22
	if (sRowH < displayContTop * this.rowHeight)
		this.setScrollTop(parseFloat(this.dataOuterDiv.scrollTop)
				- (displayContTop * this.rowHeight - sRowH));
	else if (sRowH > displayContBottom * this.rowHeight)
		this.setScrollTop(parseFloat(this.dataOuterDiv.scrollTop)
				+ (sRowH - displayContTop * this.rowHeight));
	displayCont = this.calcuDisplayRowNum();
	displayContTop = displayCont[0];
	displayContBottom = displayCont[1];
};

/**
 * 得到key在header中的位置
 * 
 * @return key在headers中的索引
 * @private
 */
GridComp.prototype.nameToIndex = function(key) {
	for (var i = this.basicHeaders.length - 1; i >= 0; i--) {
		if (this.basicHeaders[i].keyName == key)
			return i;
	}
	return -1;
};

/**
 * 得到当前选中行的索引数组
 * 
 * @return 当前选中行的索引数组
 * @private
 */
GridComp.prototype.getSelectedRowIndice = function() {
	if (this.isMultiSelWithBox == false)
		return this.selectedRowIndice;
	else {
		if (this.model.dataset != null)
			return this.model.getSelectedIndices();
	}
};

/**
 * 得到选中的所有行
 * 
 * @return GridCompRow的数组
 * @private
 */
GridComp.prototype.getSelectedRows = function() {
	if (this.isMultiSelWithBox == false) {
		if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0)
			return [this.getRow(this.selectedRowIndice[0])];
	} else
		return this.model.getSelectedRows();
};

/**
 * 改变点击的cell所在一行的所有cell的外观
 * 
 * @param rowIndex
 *            行号
 * @param isAddSel
 *            是否追加显示选中行
 * @private
 */
GridComp.prototype.rowSelected = function(rowIndex, isAddSel) {
	// 此模式下只能有一行的外观为选中行样式
	if (isAddSel == null) {
		// 改变上次选中行外观效果
		if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0) {
			if (!this.isMultiSelWithBox && this.selectedRowIndice[0] != -1) {
				for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
					var header = this.basicHeaders[i];
					if (header.isHidden == false) {
						var selIndex = this.selectedRowIndice[0];
						var isOdd = this.isOdd(selIndex);
						if(header.dataDiv && header.dataDiv.cells)
							var seleCell = header.dataDiv.cells[selIndex];
						// 将上次选中行的背景色全部变为白色,需要校验的field不变色
						if (seleCell != null) {
							if (seleCell.isErrorCell) {
								if (header.isFixedHeader)
									seleCell.className = isOdd ? "fixed_gridcell_odd cell_error" : "fixed_gridcell_even cell_error";
								else
									seleCell.className = isOdd ? "gridcell_odd cell_error" : "gridcell_even cell_error";
							} else {
								if (header.isFixedHeader)
									seleCell.className = isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even";
								else
									seleCell.className = isOdd ? "gridcell_odd" : "gridcell_even";
							}
							if(this.hasBorder)
								$(seleCell).addClass("cellExtendCss");
						}
					}
				}
			}
		}
		else{
			// 没有记录的选中行,说明是第一次点击
			this.selectedRowIndice = new Array();
		}

		// 改变当前行的外观
		this.changeCurrSelectedRowStyle(rowIndex);
		// 设置行状态
		if (this.isMultiSelWithBox) {
			if (this.selectedRowIndice.length > 0) {
				// 恢复上次选中行的状态图标
				var node = this.lineStateColumDiv.cells[this.selectedRowIndice[0]];
				if (node != null
						&& node.className == "row_state_div row_normal_state")
					node.className = "row_state_div";

				var curNode = this.lineStateColumDiv.cells[rowIndex];
				if (curNode != null && curNode.className != "row_state_div row_delete_state")
					curNode.className = "row_state_div row_normal_state";
			}
		} else {
			var node = this.lineStateColumDiv.cells[rowIndex];
			if (node != null
					&& node.className != "row_state_div row_update_state"
					&& node.className != "row_state_div row_add_state"
					&& node.className != "row_state_div row_delete_state")
				node.className = "row_state_div row_normal_state";
		}

		// 改变当前点击的cell的外观
		this.changeSelectedCellStyle(rowIndex);

		if (this.selectedCell != null && this.currActivedCell != null
				&& (this.currActivedCell != this.selectedCell)) {
			if (this.selectedCell.rowIndex == rowIndex)
				this.selectedCell.className = "cell_select";
			else {
				var isOdd = this.isOdd(rowIndex);
				var header = this.basicHeaders[this.selectedCell.colIndex];
				if (header.isFixedHeader)
					this.selectedCell.className = isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even";
				else
					this.selectedCell.className = isOdd ? "gridcell_odd" : "gridcell_even";
			}
			if(this.hasBorder)
				$(this.selectedCell).addClass("cellExtendCss");
		}
		if (!this.isMultiSelWithBox){
			// 保存当前选中行
			this.selectedRowIndice[0] = rowIndex;	
		}else{
			// 保存当前选中行
			var isExist = false;
			for(var i=0; i<this.selectedRowIndice.length; i++){
				if(this.selectedRowIndice[i] == rowIndex){
					isExist = true;
				}
			}
			if(!isExist){
				this.selectedRowIndice.push(rowIndex);		
			}
		}
	}
	// 追加行选中
	else if (isAddSel) {
		if (this.selectedRowIndice == null)
			this.selectedRowIndice = [];
		// 将当前点击行的外观改变
		this.changeCurrSelectedRowStyle(rowIndex);
		// 改变当前点击的cell的外观
		this.changeSelectedCellStyle(rowIndex);
		// 记录当前选中行
		this.selectedRowIndice.push(rowIndex);
	}
	// 调用用户的方法
	if (this.isMultiSelWithBox == false)
		this.onRowSelected(rowIndex);
};

/**
 * @private
 */
GridComp.prototype.changeCurrSelectedRowStyle = function(rowIndex) {
	// if (this.isMultiSelWithBox)
	// return;
	for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
		var header = this.basicHeaders[i];
		if (header.isHidden == false) {
			if(header.dataDiv && header.dataDiv.cells)
				var tempCell = header.dataDiv.cells[rowIndex];
			if (tempCell != null) {
				if (!tempCell.isErrorCell) {
					var isOdd = this.isOdd(rowIndex);
					if (header.isFixedHeader)
						tempCell.className = isOdd ? "fixed_gridcell_odd cell_select" : "fixed_gridcell_even cell_select";
					else
						tempCell.className = isOdd ? "gridcell_odd cell_select" : "gridcell_even cell_select";
					if(this.hasBorder)
						$(tempCell).addClass("cellExtendCss");
				}
			}
		}
	}
};

/**
 * @private
 */
GridComp.prototype.changeSelectedCellStyle = function(rowIndex) {
	if (!this.isMultiSelWithBox && this.selectedCell != null
			&& !this.selectedCell.isErrorCell) {
		var isOdd = this.isOdd(rowIndex);
		var header = this.basicHeaders[this.selectedCell.colIndex];
		if (header == null)
			return;
		if (header.isFixedHeader)
			this.selectedCell.className = isOdd ? "fixed_gridcell_odd cell_focus" : "fixed_gridcell_even cell_focus";
		else
			this.selectedCell.className = isOdd ? "gridcell_odd cell_focus" : "gridcell_even cell_focus";
		if(this.hasBorder)
			$(this.selectedCell).addClass("cellExtendCss");
	}
};

/**
 * 得到给定索引数组的行
 * 
 * @return GridCompRow数组
 * @private
 */
GridComp.prototype.getRows = function(indice) {
	if (indice == null || !(indice instanceof Array))
		return null;

	var rows = new Array();
	for (var i = 0; i < indice.length; i++)
		rows.push(this.model.getRow(indice[i]));
	return rows;
};

/**
 * 得到给定索引数组的行
 * 
 * @return GridCompRow数组
 */
GridComp.prototype.getRow = function(index) {
	if (index == null)
		return null;
	var tempIndex = parseInt(index);
	return this.getRows([tempIndex])[0];
};

GridComp.prototype.getDatasetRow = function(uiRowIndex) {
	var row = this.getRow(uiRowIndex);
	if(row != null)
		return row.rowData;
	return null;
};

/**
 * model中行删除时的回调方法
 * 
 * @private
 */
GridComp.prototype.fireRowDeleted = function(indice) {
	// 如果outerDiv没有显示出来，不进行处理
	if (this.outerDiv.offsetWidth == 0) {
		this.needPaintRows = true;
		return;
	};
	var gridWidth = this.constant.outerDivWidth;
	for (var i = 0, count = indice.length; i < count; i++) {
		// 若显示控件在当前选中行则隐藏当前控件
		if (this.currActivedCell != null
				&& this.getCellRowIndex(this.currActivedCell) == indice[i])
			this.hiddenComp();
		// 删除整个选中行
		this.deleteRows([indice[i]]);
	}
	this.oldCell = null;
};

/**
 * 删除选中行
 * 
 */
GridComp.prototype.deleteSeletedRow = function() {
	var selectedRowIndice = this.getSelectedRowIndice();
	if (selectedRowIndice != null && selectedRowIndice.length > 0) {
		// 删除model中的此行数据
		this.model.deleteRows(selectedRowIndice);
		this.selectedRowIndice = null;
	}
};

/**
 * 删除指定行
 * 
 * @param indice
 *            指定的索引值数组
 * @private
 */
GridComp.prototype.deleteRows = function(indice) {
	// 将所有行删除时需要将滚动条移动到行头位置
	if(this.getRowsNum() == 0){
		this.setScrollLeft(0);
	}
	initLayoutMonitorState();
	if (indice == null || indice.length <= 0)
		return;

	// 要删除的行数
	var deleCount = indice.length;
	// 将数组的值按升序排列
	indice.sort(ascendRule_int);
	var len = this.basicHeaders.length;

	// 如果显示数字行标,则从最后行删除deleCount个numdiv
	if (this.isShowNumCol) {
		for (var i = 0; i < deleCount; i++) {
			if (this.rowNumDiv.childNodes.length - 1 >= 0) {
				var node = this.rowNumDiv.cells[indice[i]];
				if (node != null) {
					// 从界面上删除
					this.rowNumDiv.removeChild(node);
				}
				// 从数组中移除,必须在最后移除,否则数据会错位
				this.rowNumDiv.cells.splice(indice[i], 1);
			}
		}
		var cellLen = this.rowNumDiv.cells.length;
		for(var i=0; i<cellLen; i++){
			if(typeof(this.rowNumDiv.cells[i]) != "undefined"){
				this.rowNumDiv.cells[i].rowIndex = i;
				this.rowNumDiv.cells[i].innerHTML = "<center>" + (i + 1) + "</center>";
			}
			if(typeof(this.rowNumDiv.childNodes[i]) != "undefined"){
				this.rowNumDiv.childNodes[i].rowIndex = i;
				this.rowNumDiv.childNodes[i].innerHTML = "<center>" + (i + 1) + "</center>";
			}
		}
	}

	// 删除行状态列,deleCount个numdiv
	for (var i = 0; i < deleCount; i++) {
		if (this.lineStateColumDiv.childNodes.length - 1 >= 0) {
			var node = this.lineStateColumDiv.cells[indice[i]];
			if (node != null) {
				this.lineStateColumDiv.removeChild(node);
			}
			this.lineStateColumDiv.cells.splice(indice[i], 1);
		}
	}
	if (deleCount == 1) {
		var rowIndex = indice[0];
		var rowCount = this.model.getRowsCount();
		// 选中的是最后一行直接删除
		/*if (rowIndex < 0) {
			// 如果有固定列删除固定列中相应行
			if (this.isMultiSelWithBox) {
				// 如果删除行在显示区域则删除
				this.selectColumDiv
						.removeChild(this.selectColumDiv.cells[rowIndex]);
				this.selectColumDiv.cells.splice(rowIndex, 1);
			}
		}
		// 选中的不是最后一行
		else {*/
			// 如果多选,删除多选列中的checkbox
			if (this.isMultiSelWithBox) {
				this.selectColumDiv
						.removeChild(this.selectColumDiv.cells[rowIndex]);
				this.selectColumDiv.cells.splice(rowIndex, 1);
			}
			// 删除选中行中cells
			for (var i = 0; i < len; i++) {
				if (this.basicHeaders[i].isHidden == false) {
					// 得到列div
					var dataDiv = this.basicHeaders[i].dataDiv;
					dataDiv.removeChild(dataDiv.cells[rowIndex]);
					dataDiv.cells.splice(rowIndex, 1);
					//处理列分组
					if (this.basicHeaders[i].isGroupBy == true){
						this.afterDeleteChangeGroupCellStyle(rowIndex, i);
					}
				}
			}
			// 从该删除行的下一行开始向下扫描,改变每一行的cell的id,并将cell上移一个单位
			var signLen = this.model.getRowsCount();//this.paintedSign.length;
			var cell = null;
			var seleCheck = null;
			for (var i = indice[0]; i < signLen; i++) {
				if (this.isMultiSelWithBox) {
					seleCheck = this.selectColumDiv.cells[i];
					seleCheck.rowIndex = seleCheck.rowIndex - 1;

				}

				for (var j = 0; j < len; j++) {
					if (this.basicHeaders[j].isHidden == false) {
						cell = this.basicHeaders[j].dataDiv.cells[i];
						// 改变行号
						cell.rowIndex = cell.rowIndex - 1;
						if(EditorType.CHECKBOX == cell.editorType && cell.checkBox){
							cell.checkBox.rowIndex = cell.rowIndex;
						}
					}
				}
			}
		/*}*/
		if (this.selectedRowIndice != null) {
			// 调整选中行的索引值
			for (var i = 0, count = this.selectedRowIndice.length; i < count; i++) {
				if (this.selectedRowIndice[i] > rowIndex)
					this.selectedRowIndice[i]--;
			}
		}
	}
	if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0) {
		if (this.selectedRowIndice[0] == rowIndex)
			this.selectedRowIndice = null;
	}
	executeLayoutMonitor();
};

/**
 * 获得Header宽度
 * 
 * @private
 */
GridComp.prototype.getHeaderWidth = function() {
	var headers = this.model.getHeaders();
	if (headers == null)
		return 0;
	var width = 0;
	for (var i = 0; i < headers.length; i++) {
		if (!headers[i].isHidden)
			width += headers[i].width;
	}
	return width;
};

/**
 * 获得Header最大深度
 * 
 * @private
 */
GridComp.prototype.getHeaderDepth = function() {
	var headers = this.model.getHeaders();
	var maxDepth = 1;
	for (var i = 0; i < headers.length; i++) {
		var depth = headers[i].getDepth();
		if (maxDepth <= depth)
			maxDepth = depth;
	}
	return maxDepth;
};

/**
 * model改变后会调用此方法通知grid
 * 
 * @param rowIndex
 *            cell所在行号
 * @param colIndex
 *            cell所在列号
 * @param newValue
 *            新值
 * @param oldValue
 *            旧值
 * @private
 */
GridComp.prototype.cellValueChangedFunc = function(rowIndex, colIndex, newValue, oldValue) {
//	if (this.paintedSign[rowIndex] == 1) {
	// 此列不隐藏
	if (this.basicHeaders[colIndex].isHidden == false) {
		var cell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
		var header = this.basicHeaders[colIndex];
		if (cell.firstChild != null)
			cell.removeChild(cell.firstChild);
//		this.basicHeaders[colIndex].renderType.render.call(this, rowIndex, colIndex, newValue, header, cell);
		this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex, colIndex, newValue, header, cell);	
		if (this.autoRowHeight == true)	
			this.adjustRowHeight(rowIndex, cell);	

		// 如果该cell原先处于选中行要将此cell的背景重新设置为选中行样式
		var isOdd = this.isOdd(rowIndex);
		if (header.isFixedHeader) {
			if (this.selectedRowIndice != null
					&& this.selectedRowIndice.indexOf(rowIndex) != -1)
				cell.className = isOdd ? "fixed_gridcell_odd cell_select" : "fixed_gridcell_even cell_select";
			else
				cell.className = isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even";
		} else {
			if (this.selectedRowIndice != null
					&& this.selectedRowIndice.indexOf(rowIndex) != -1)
				cell.className = isOdd ? "gridcell_odd cell_select" : "gridcell_even cell_select";
			else
				cell.className = isOdd ? "gridcell_odd" : "gridcell_even";
		}
		if(this.hasBorder)
			$(cell).addClass("cellExtendCss");
		cell.isErrorCell = false;
		
		//处理列分组
		if (this.basicHeaders[colIndex].isGroupBy == true){
			this.changeGroupCellStyle(rowIndex, colIndex);
		}		
		if(header.editorType == EditorType.TEXTAREA
			|| header.editorType == EditorType.INTEGERTEXT
			|| header.editorType == EditorType.DECIMALTEXT
			|| header.editorType == EditorType.DATETEXT
			|| header.editorType == EditorType.DATETIMETEXT
			|| header.editorType == EditorType.YEARTEXT
			|| header.editorType == EditorType.MONTHTEXT
			|| header.editorType == EditorType.YEARMONTHTEXT
			|| header.editorType == EditorType.TIMETEXT
			|| header.editorType == EditorType.EMAILTEXT
			|| header.editorType == EditorType.PHONETEXT
			|| header.editorType == EditorType.LINKTEXT
			|| header.editorType == EditorType.PRECENTTEXT
			|| header.editorType == EditorType.PWDTEXT
			|| header.editorType == EditorType.STRINGTEXT){
			if (this.compsMap != null) {
				var comp = null; 
				if (header.isExtendComp) 
					comp = this.compsMap.get("extend$" + colIndex);
				else	
				  	comp = this.compsMap.get(header.editorType);
				if (comp != null && comp.visible) {
					if (this.currActivedCell != null
							&& this.currActivedCell == cell){
						if(comp.setValue){
							comp.setValue(newValue);
						}
					}
				}
			}
		}
		if (header.editorType == EditorType.REFERENCE
			|| header.editorType == EditorType.COMBOBOX
			|| header.editorType == EditorType.MONEYTEXT
			|| header.editorType == EditorType.LANGUAGECOMBOBOX) {
			if (this.compsMap != null) {
				var comp = null;
				if (header.isExtendComp) 
					comp = this.compsMap.get("extend$" + colIndex);
				else	
					comp = this.compsMap.get(header.editorType + colIndex);
				if (comp != null && comp.visible) {
					if (this.currActivedCell != null
							&& this.currActivedCell == cell){
						if(comp.setValue){
							comp.setValue(newValue);
						}
					}
				}
			}
		}
		// 设置行状态(行状态为更新行才设置行状态图标为更新状态,新增行cell数据的改变不变化行状态图标)
		if (this.model != null
				&& this.model.getRow(rowIndex).rowData.state == DatasetRow.STATE_UPD)
			this.lineStateColumDiv.cells[rowIndex].className = "row_state_div row_update_state";
	}
//}
	// cell值改变后通知用户
	this.onCellValueChanged(rowIndex, colIndex, oldValue, newValue);
	
	//处理有关的字段
	for (var i = 0; i < this.basicHeaders.length; i ++){
		if (this.basicHeaders[i].isHidden == false && this.basicHeaders[i].refFieldsArr != null){
			if (this.basicHeaders[i].refFieldsArr.indexOf(this.basicHeaders[colIndex].keyName) > 0){
				var value = this.model.dataset.getValueAt(rowIndex,i);
				this.cellValueChangedFunc(rowIndex, i,value, value);
			}
		}
	}
};

/**
 * 存在分组的情况下，删除单元格后改变单元格的样式
 * @add zuopf 2012.12.28
 * @param {} rowIndex
 * @param {} colIndex
 */
GridComp.prototype.afterDeleteChangeGroupCellStyle = function(rowIndex, colIndex){
	if (this.model.rows.length < rowIndex) return;
	var preRow = null;
	//由于currentRow已经被删除所以拿不到currentRow
//	var currentRow = null;
	var nextRow = null;
	if (rowIndex != 0)
		preRow = this.model.rows[rowIndex - 1];
	if (rowIndex != this.model.rows.length)
		nextRow = this.model.rows[rowIndex];
//	currentRow = this.model.rows[rowIndex];
	var preCell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex - 1];
//	var currentCell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
	var nextCell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
	if(preRow != null && nextRow != null){
		if (preRow.getCellValue(colIndex) == nextRow.getCellValue(colIndex)){
			preCell.style.borderBottomColor = "#ffffff";
			nextCell.style.backgroundColor = "#ffffff";
//			this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, null, this.basicHeaders[colIndex],nextCell, null);
			this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex + 1, colIndex, null, this.basicHeaders[colIndex],nextCell);
			if (this.autoRowHeight == true)	
				this.adjustRowHeight(rowIndex, nextCell);	
			}	
			
		if (preRow.getCellValue(colIndex) != nextRow.getCellValue(colIndex)){
			preCell.style.borderBottomColor = "";
			nextCell.style.backgroundColor = "";
			var value = nextRow.getCellValue(colIndex);
//			this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex],nextCell, null);		
			this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex],nextCell);
			if (this.autoRowHeight == true)	
				this.adjustRowHeight(rowIndex, nextCell);	
			}	
	}
	
	//删除的单元格在第一行,只需修改nextCell背景色,重新渲染value,无论nextCell和next2Cell是否相等borderBottomColor不用改变
	if(preRow == null && nextRow != null){
		nextCell.style.backgroundColor = "";
		var value = nextRow.getCellValue(colIndex);
//		this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex],nextCell, null);		
		this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex],nextCell);
		
		if (this.autoRowHeight == true)	
			this.adjustRowHeight(rowIndex, nextCell);	
	}	
	
	//删除的单元格在最后一行,只需修改preCell的borderBottomColor
	if(preRow != null && nextRow == null){
		preCell.style.borderBottomColor = "";
	}	
	
};

/**
 * 数据改变后，校正分组显示
 * 
 * @param {} rowIndex
 * @param {} colIndex
 */
GridComp.prototype.changeGroupCellStyle = function(rowIndex, colIndex){
	if (this.model.rows.length < rowIndex) return;
	var preRow = null;
	var currentRow = null;
	var nextRow = null;
	if (rowIndex != 0)
		preRow = this.model.rows[rowIndex - 1];
	if (rowIndex != (this.model.rows.length - 1))
		nextRow = this.model.rows[rowIndex + 1];
	currentRow = this.model.rows[rowIndex];
	var currentCell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
	if (preRow != null){
		var preCell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex - 1];
		//与上一行相同
		if (preRow.getCellValue(colIndex) == currentRow.getCellValue(colIndex)){
			var bottomColor = getStyleAttribute(preCell, "background-color");
			preCell.style.borderBottomColor = "#ffffff";
			// 合并单元格背景色#ffffff
			preCell.style.backgroundColor = "#ffffff";
			currentCell.style.backgroundColor = "#ffffff";
			//如果currentCell有子节点，先去掉再渲染
			if (currentCell.firstChild != null)currentCell.removeChild(currentCell.firstChild);
//			this.basicHeaders[colIndex].renderType.render.call(this, rowIndex, colIndex, null, this.basicHeaders[colIndex], currentCell, null);
			this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex, colIndex, null, this.basicHeaders[colIndex], currentCell);
			
			if (this.autoRowHeight == true)	
				this.adjustRowHeight(rowIndex, currentCell);	

		}
		//与上一行不同
		else{
			preCell.style.borderBottomColor = "";
			if(rowIndex - 2 >= 0){
				var pre2Row = this.model.rows[rowIndex - 2];
				if(preRow.getCellValue(colIndex) == pre2Row.getCellValue(colIndex)){
					preCell.style.backgroundColor = "#ffffff";
				}else{
					preCell.style.backgroundColor = "";
				}
			}else{
				preCell.style.backgroundColor = "";
			}
			currentCell.style.backgroundColor = "";
		}
	}
	if (nextRow != null){
		var nextCell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex + 1];
		//如果nextCell有子节点，先去掉再渲染
		if (nextCell.firstChild != null)nextCell.removeChild(nextCell.firstChild);
		//与下一行相同
		if (nextRow.getCellValue(colIndex) == currentRow.getCellValue(colIndex)){
			var bottomColor = getStyleAttribute(currentCell, "background-color");
			currentCell.style.borderBottomColor = "#ffffff";
			currentCell.style.backgroundColor = "#ffffff";
			nextCell.style.backgroundColor = "#ffffff";
//			this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, null, this.basicHeaders[colIndex],nextCell, null);		
			this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex + 1, colIndex, null, this.basicHeaders[colIndex], nextCell);
			if (this.autoRowHeight == true)	
				this.adjustRowHeight(rowIndex + 1, nextCell);	
		}
		//与下一行不同
		else{
			currentCell.style.borderBottomColor = "";
			if(rowIndex - 1 >= 0){
				var preRow = this.model.rows[rowIndex - 1];
				if(preRow.getCellValue(colIndex) == currentRow.getCellValue(colIndex)){
					currentCell.style.backgroundColor = "#ffffff";
				}else{
					currentCell.style.backgroundColor = "";
				}
			}else{
				currentCell.style.backgroundColor = "";
			}
			var next2Row = this.model.rows[rowIndex + 2];
			if(next2Row != null){
				if(nextRow.getCellValue(colIndex) == next2Row.getCellValue(colIndex)){
					nextCell.style.backgroundColor = "#ffffff";
				}else{
					nextCell.style.backgroundColor = "";
				}
			}else{
				nextCell.style.backgroundColor = "";
			}
			var value = nextRow.getCellValue(colIndex);
//			this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex],nextCell, null);
			this.renderCell(this.basicHeaders[colIndex].renderType, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex], nextCell);
			if (this.autoRowHeight == true)	
				this.adjustRowHeight(rowIndex + 1, nextCell);	
		}
	}
};

/**
 * 获取grid当前的显示列
 * 
 * @return 显示列id的数组
 */
GridComp.prototype.getVisibleColumnIds = function() {
	if (!this.model)
		return null;
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return null;

	var visibleColumns = [];
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].isHidden == false)
			visibleColumns.push(headers[i].id);
	}
	return visibleColumns;
};

/**
 * 获取grid当前的隐藏列
 * 
 * @return 隐藏列id的数组
 */
GridComp.prototype.getHiddenColumnIds = function() {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return null;

	var hiddenColumns = [];
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].isHidden == true)
			hiddenColumns.push(headers[i].keyName);
		return hiddenColumns;
	}
	return hiddenColumns;
};

/**
 * 设置要显示的列
 * 
 * @param columnIds
 *            要显示的列的id数组
 */
GridComp.prototype.setShowColumns = function(columnIds) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;

	// 全部隐藏
	if (columnIds == null || columnIds.length == 0) {
		for (var i = 0, count = headers.length; i < count; i++)
			headers[i].isHidden = true;
	}
	// 按指定列隐藏
	else {
		for (var i = 0, count = headers.length; i < count; i++) {
			if (columnIds.indexOf(headers[i].keyName) != -1)
				headers[i].isHidden = false;
			else
				headers[i].isHidden = true;
		}
	}
	this.setModel(this.model);

//	// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
//	// 设置行标志为未画
//	for (var i = 0, count = this.getRowsNum(); i < count; i++)
//		this.paintedSign[i] = 0;
	if(this.showForm){
		this.paintFormData();
	}else{
		this.paintData();
	}
};

/**
 * 根据ID取列
 * 
 * @param columnId
 *            要取的列ID
 */
GridComp.prototype.getBasicHeaderById = function(columnId) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0 || columnId == null)
		return;
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].keyName == columnId)
			return headers[i];
	}
	return;
};

/**
 * 设置要锁定的列
 * 
 * @param columnIds
 *            要锁定的列的id数组
 */
GridComp.prototype.setFixedColumns = function(columnIds) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;

	if (columnIds == null || columnIds.length == 0) {
		for (var i = 0, count = headers.length; i < count; i++)
			headers[i].isFixedHeader = false;
	} else {
		for (var i = 0, count = headers.length; i < count; i++) {
			if (columnIds.indexOf(headers[i].keyName) != -1)
				headers[i].isFixedHeader = true;
			else
				headers[i].isFixedHeader = false;
		}
	}
	this.setModel(this.model);

	// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的绘制了界面显示的区域.必须设置重新画一下
	// 设置行标志为未画
//	for (var i = 0, count = this.getRowsNum(); i < count; i++)
//		this.paintedSign[i] = 0;
	this.paintZone();
};

/**
 * 设置参数指定列的显示隐藏属性
 * 
 * @param{Array} columns 列显示隐藏数组
 */
GridComp.prototype.setColumnVisible = function(columns) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	var hasChanged = false;
	for (var i = 0; i < headers.length; i++) {
		var header = headers[i];
		// columns: [columName:visible;colunmName2:visible;columnName3:visible] 
		//columns.indexOf()返回每项在数组中的下标
		if (columns.indexOf(header.keyName + ":" + String(header.isHidden)) != -1) {
			if(header.isHidden == false){
				// 如果隐藏的是最后一列则设置倒数第二列的isAutoExpand为true，最后一列的isAutoExpand为false
				var visibleColumn = this.getVisibleColumnIds();
				
				if(visibleColumn != null &&  visibleColumn.length > 0 && header.id == visibleColumn[visibleColumn.length - 1] && this.autoExpand == 0){
					header.isAutoExpand = false;
					this.model.getBasicHeaderById(visibleColumn[visibleColumn.length - 2]).isAutoExpand = true;
				}
			}else{
				// 如果显示的隐藏列位于当前显示列的后面，则设置当前显示列的isAutoExpand为false，要显示的列的isAutoExpand为true
				var oldVisibleColumn = this.getVisibleColumnIds();
				header.isHidden = (!header.isHidden);
				var newVisibleColumn = this.getVisibleColumnIds();
				header.isHidden = (!header.isHidden);
				if(oldVisibleColumn != null && oldVisibleColumn.length >0   && newVisibleColumn != null && newVisibleColumn.length > 0   &&  oldVisibleColumn[oldVisibleColumn.length - 1] != newVisibleColumn[newVisibleColumn.length - 1] && this.autoExpand == 0){
					header.isAutoExpand = true;
					this.model.getBasicHeaderById(oldVisibleColumn[oldVisibleColumn.length - 1]).isAutoExpand = false;
					var oldWidth = this.model.getBasicHeaderById(oldVisibleColumn[oldVisibleColumn.length - 1]).oldWidth;
					this.model.getBasicHeaderById(oldVisibleColumn[oldVisibleColumn.length - 1]).width = oldWidth; 
				}
			}
			header.isHidden = (!header.isHidden);

			// 修改group的显示属性(如果group中的所有子都隐藏，group也设为隐藏)
			if (header.isGroupHeader == true) {
				if (header.isHidden == true) {
					var groupHeader = header.topHeader;
					if (groupHeader != null && groupHeader.isHidden == false) {
						var childrenHeaders = groupHeader.allChildrenHeader;
						var allHidden = true;
						for (var j = 0; j < childrenHeaders.length; j++) {
							if (childrenHeaders[j].isHidden == false) {
								allHidden = false;
								break;
							}
						}
						if (allHidden == true) {
							groupHeader.isHidden = true;
						}
					}
				} else {
					var groupHeader = header.topHeader;
					if (groupHeader != null && groupHeader.isHidden == true) {
						groupHeader.isHidden = false;
					}
				}
			}
			
			hasChanged = true;
		}
	}

	if (hasChanged) {
		this.model.rows = null;
		this.model.initUIRows();
		this.setModel(this.model);
		// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
		// 设置行标志为未画
//		for (var i = 0, count = this.getRowsNum(); i < count; i++)
//			this.paintedSign[i] = 0;
//		this.paintZone();  //setModel中本身包含paintZone()所以去掉
	}
};

/**
 * 设置参数指定列的显示隐藏属性
 * 
 * @param{Array} columns 列显示隐藏数组
 */
GridComp.prototype.setColumnVisibleB = function(header, columnContext) {
	//if(header.isHidden) return;
	var hasChanged = false;
	header.isHidden = !columnContext.column_visible;
	// 修改group的显示属性(如果group中的所有子都隐藏，group也设为隐藏)
	if (header.isGroupHeader == true) {
		if (header.isHidden == true) {
			var groupHeader = header.topHeader;
			if (groupHeader != null && groupHeader.isHidden == false) {
				var childrenHeaders = groupHeader.allChildrenHeader;
				var allHidden = true;
				for (var j = 0; j < childrenHeaders.length; j++) {
					if (childrenHeaders[j].isHidden == false) {
						allHidden = false;
						break;
					}
				}
				if (allHidden == true) {
					groupHeader.isHidden = true;
				}
			}
		}
		hasChanged = true;
	} 
	else {
		var groupHeader = header.topHeader;
		if (groupHeader != null && groupHeader.isHidden == true) {
				groupHeader.isHidden = false;
			}
		hasChanged = true;
	}
	
	if (hasChanged) {
		this.model.rows = null;
		this.model.initUIRows();
		this.setModel(this.model);
	}
};

/**
 * 设置指定columnGroup的显示隐藏属性
 * @param {} header 
 * @param {} groupColumnContext 
 */
GridComp.prototype.setGroupColumnVisible = function(header, groupColumnContext) {
	var hasChanged = false;
	header.isHidden = !groupColumnContext.column_visible;
	// 修改group的显示属性(如果group隐藏，则所有子都隐藏)
	if(header.isGroupHeader == true){
		var childrenHeaders = header.allChildrenHeader;
		if (header.isHidden == true) {
			for (var j = 0; j < childrenHeaders.length; j++) {
				if (childrenHeaders[j].isHidden == true) continue;
					childrenHeaders[j].isHidden = true;
					hasChanged = true;
			}
		}
		//如果group显示，则子保持原来的显示或隐藏
		else if(header.isHidden == false){
//			for (var j = 0; j < childrenHeaders.length; j++) {
//				if (childrenHeaders[j].isHidden == false) continue;
//					childrenHeaders[j].isHidden = false;
					hasChanged = true;
//			}
		}
	}
			
	if (hasChanged) {
		this.model.rows = null;
		this.model.initUIRows();
		this.setModel(this.model);
	}
};

/**
 * 设置参数指定列的显示背景颜色属性
 * @param {} columns
 */
GridComp.prototype.setColumnBgcolor = function(columns){
	var headers = this.model.basicHeaders;
	if(headers == null || headers.length == 0)
		return;
	var hasChanged = false;
	for (var i = 0; i < headers.length; i++) {
		var header = headers[i];
		for (var j = 0; j < columns.length; j++) {
			var column = columns[j];
			var attrValue = column.split(":");
			var name = attrValue[0];
			var value = attrValue[1];
			if(header.keyName == name){						
				if(header.isGroupHeader == true){
					var childrenHeaders = groupHeader.allChildrenHeader;
					for (var k = 0; k < childrenHeaders.length; k++) {
						childerHeaders[k].columBgColor = value;	
					}
				}else{
					header.columBgColor = value;
				}
				hasChanged = true;
				break;
			}
		}		
	}
	if (hasChanged){
		if(this.showForm){
			this.paintFormData();
		}else{
			this.paintData();
		}
	}
	
};

/**
 * 设置参数指定列的显示背景颜色属性
 * 
 * @param {} header
 * @param {} context
 * @public
 */
GridComp.prototype.setColumnBGColor = function(header, context){
	var hasChanged = false;
	var name = context.id;
	var value = context.column_bgcolor;							
	if(header.isGroupHeader == true) {
		var childrenHeaders = header.allChildrenHeader;
		for (var k = 0; k < childrenHeaders.length; k++) {
				childerHeaders[k].columBgColor = value;	
			}
		hasChanged = true;
	}else {
		header.columBgColor = value;
		hasChanged = true;
	}
	if (hasChanged){
		if(this.showForm){
			this.paintFormData();
		}else{
			this.paintData();
		}
	}	
};

/**
 * 设置columnTextColor
 * @param {} columns
 */
GridComp.prototype.setColumnTextcolor = function(columns){
	var headers = this.model.basicHeaders;
	if(headers == null || headers.length == 0)
		return;
	var hasChanged = false;
	for (var i = 0; i < headers.length; i++) {
		var header = headers[i];
		for (var j = 0; j < columns.length; j++) {
			var column = columns[j];
			var attrValue = column.split(":");
			var name = attrValue[0];
			var value = attrValue[1];
			if(header.keyName == name){						
				if(header.isGroupHeader == true){
					var childrenHeaders = groupHeader.allChildrenHeader;
					for (var k = 0; k < childrenHeaders.length; k++) {
						childerHeaders[k].textColor = value;	
					}
				}else{
					header.textColor = value;
				}
				hasChanged = true;
				break;
			}
		}		
	}
	if (hasChanged){
		if(this.showForm){
			this.paintFormData();
		}else{
			this.paintData();
		}
	}
	
};

/**
 * 设置columnTextColor
 * @param {} header 
 * @param {} context 
 */
GridComp.prototype.setColumnTextColor = function(header, context){
	var hasChanged = false;
	var value = context.column_textcolor;							
	if(header.isGroupHeader == true) {
		var childrenHeaders = header.allChildrenHeader;
		for (var k = 0; k < childrenHeaders.length; k++) {
			childerHeaders[k].textColor = value;	
		}
		hasChanged = true;
	}else {
		header.textColor = value;
		hasChanged = true;
	}
	if (hasChanged){
		if(this.showForm){
			this.paintFormData();
		}else{
			this.paintData();
		}
	}	
	
};

/**
 * 设置ColumnWidth
 * @param {} header
 * @param {} context
 */
GridComp.prototype.setColumnWidth = function(header, context){
	var hasChanged = false;
	if(context.column_width != null &&  header.width != context.column_width){
		var value = context.column_width;	
		if(value < 35)
			value = 35;
		header.width = value;
		hasChanged = true;
	}
	if (hasChanged){
		this.model.rows = null;
		this.model.initUIRows();
		this.setModel(this.model);
	}		
};

/**
 * 设置参数指定列的editable属性
 * 
 * @param{Array} columns 列editable数组，格式: [columName:editable]
 */
GridComp.prototype.setColumnEditable = function(columns) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	for (var i = 0; i < headers.length; i++) {
		var header = headers[i];
		
		if (columns.indexOf(header.keyName + ":" + String(!header.columEditable)) != -1) {
			header.columEditable = (!header.columEditable);
			if (header.columEditable == true && this.editable == false) {
				// 根据model编辑属性设置grid的编辑属性
				if (this.model.dataset != null && this.model.dataset.editable)
					this.setEditable(true);
			}
		}
		
		//因为grid的setEditable()方法在调用的时候激活了所有的BooleanRender，所以在这里要重新设置
		if (header.renderType == BooleanRender) {
			if (header.columEditable) {
				if (header.selectBox != null)
					header.selectBox.disabled = false;
				if (header.dataDiv != null && header.dataDiv.cells != null){
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = false;
					}
				}
			} else {
				if (header.selectBox != null) 
					header.selectBox.disabled = true;
				if (header.dataDiv != null && header.dataDiv.cells != null){
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = true;
					}
				}
			}
		}		
	}
};

/**
 * 设置参数指定列的editable属性
 * 
 * @param heaer
 * @param context
 */
GridComp.prototype.setColumnEditableB = function(header, context) {
		header.columEditable = context.column_editable;
		if (header.columEditable == true && this.editable == false) {
			// 根据model编辑属性设置grid的编辑属性
			if (this.model.dataset != null && this.model.dataset.editable)
					this.setEditable(true);
		}
		
		//因为grid的setEditable()方法在调用的时候激活了所有的BooleanRender，所以在这里要重新设置
		if (header.renderType == BooleanRender) {
			if (header.columEditable) {
				if (header.selectBox != null)
					header.selectBox.disabled = false;
				if (header.dataDiv != null && header.dataDiv.cells != null){
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = false;
					}
				}
			} else {
				if (header.selectBox != null) 
					header.selectBox.disabled = true;
				if (header.dataDiv != null && header.dataDiv.cells != null){
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = true;
					}
				}
			}
		}
		else if (header.renderType && header.renderType.settings && header.renderType.settings["render_editchange"] && header.renderType.settings["render_editchange"] == true){
			header.reRender();
		}
};

/**
 * 设置列头全选框隐藏/显示
 * 
 * @param keyName
 *            列值名称
 * @param visible
 *            显示隐藏属性 true/false
 */
GridComp.prototype.setHeaderCheckBoxVisible = function(keyName, visible) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	for (var i = 0; i < headers.length; i++) {
		if (headers[i].keyName == keyName) {
			// 判断是否存在全选框
			if (headers[i].selectBox) {
				visible = getBoolean(visible, true);
				if (visible == true)
					headers[i].selectBox.style.display = "";
				else
					headers[i].selectBox.style.display = "none";
			}
			break;
		}
	}
};

/**
 * 根据header中设置的显示状态和当前状态判断是否应该显示
 * 
 * @private
 */
GridComp.prototype.showByState = function(state) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	var hasChanged = false;
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].showState == null)
			continue;
		if (headers[i].showState != state) {
			if (headers[i].isHidden == false) {
				headers[i].isHidden = true;
				hasChanged = true;
			}
		} else {
			if (headers[i].isHidden == true) {
				headers[i].isHidden = false;
				hasChanged = true;
			}
		}
	}
	if (!hasChanged)
		return;
	this.setModel(this.model);
	// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
	// 设置行标志为未画
//	for (var i = 0, count = this.getRowsNum(); i < count; i++)
//		this.paintedSign[i] = 0;
	this.paintZone();
};

/**
 * 设置此Grid的model，重画表头和所有行数据
 * 
 * @param model
 *            grid数据集
 */
GridComp.prototype.setModel = function(model) {
	//是否需要重新初始化model
	var needInitModel = false;

	//读取本地列显示隐藏
	if (!window.editMode){
		var cols = this.getLocalColumnVisible();
		if (cols != null){
			for (var i = 0; i < cols.length; i++){
				var header = model.getHeader(cols[i].keyName);
				if (header == null)
					continue;
				header.isHidden = true;
				if(this.autoExpand == 0){
					header.isAutoExpand = false;
				}
			}
			needInitModel = true;
		}	
		
		//读取本地列顺序设置
		var orderData = this.getLocalColumnOrder();
		if (orderData != null){
			for (var i = 0; i < orderData.length; i++){
				var keyName = orderData[i].keyName;
				var index = orderData[i].index;
				var sourceHeader = model.getHeader(keyName);
				if (sourceHeader != null){
					model.removeHeader(keyName);
					model.addHeader(sourceHeader,index);
				}
			}
			needInitModel = true;
		}
		if (needInitModel){
			model.initBasicHeaders();
			model.initBindRelation();
			model.rows = null;
			model.initUIRows();
		}
	}
	
	// 初始化设置model
	if (this.model == null) {
		this.model = model;
		// 将grid对象绑定在model的owner属性上
		this.model.owner = this;
		// 将model的basicHeaders保存到grid的basicHeaders中
		this.basicHeaders = this.model.basicHeaders;
		this.create();
	} else {
		if (this.model != model){
			this.model.dataset.unbindComponent(this.model);
			delete this.model;
			this.model = model;
			this.model.owner = this;
		}	
		this.basicHeaders = this.model.basicHeaders;
		if(this.showForm){
			this.paintFormData();
		}else{
			this.paintData();
		}
		if(!this.showForm){
			setTimeout("GridComp.processAutoExpandHeadersWidth('" + this.id + "','"+this.outerDivId+"')", 350);
		}
		// 将model的basicHeaders保存到grid的basicHeaders中
		// this.paintZone();
		// this.attachEvents();
	}
	
	var visibleColumns = this.getVisibleColumnIds();
	var autoHeaders = this.getAutoExpandHeaders();
	if (visibleColumns != null && visibleColumns != null && visibleColumns.length > 0 && this.autoExpand == 0 && (autoHeaders == null || autoHeaders.length == 0)){
		model.getBasicHeaderById(visibleColumns[visibleColumns.length - 1]).isAutoExpand = true;
	}
	if (this.pageSize > 0)
		this.setPaginationInfo();

	// 计算所有合计列的值
	if(!this.showForm){
		if (this.isShowSumRow) {
			this.model.setSumColValue(null, null);
		}
	}

	// 根据model编辑属性设置grid的编辑属性
	if (this.model.dataset != null && this.model.dataset.isEditable() == false)
		this.setEditable(false);
	else if (this.model.dataset != null && this.model.dataset.editable
			&& this.model.dataset.editableChanged)
		this.setEditable(true);
		
		this.initColumnContextMenu();
		// 通过后台修改model之后initColumnContextMenu不会重新设置数据列，需要重新设置
		if(this.columnContextMenu.menuColumn){
			this.initColumnItemMenu(this.columnContextMenu.menuColumn);
		}
};

/**
 * 删除参数节点下所有子节点
 * 
 * @private
 */
GridComp.prototype.removeAllChildren = function(p) {
	if (p) {
		while (p.childNodes.length > 0)
			p.removeChild(p.childNodes[0]);
	}
};

/**
 * grid滚动处理函数.设置30毫秒延迟
 * 
 * @private
 */
function handleScrollEvent(e) {
	handleScrollEvent.triggerObj = e.triggerObj;
	if (handleScrollEvent.timer != null)
		clearTimeout(handleScrollEvent.timer);
	handleScrollEvent.timer = setTimeout("doScroll()", 30);
};

/**
 * @private
 */
function doScroll() {
	//handleScrollEvent.triggerObj.paintZone();
};



/**
 * 根据header的数据类型解析data
 * 
 * @private
 */
GridComp.parseData = function(header, data, isComputeSum) {
	if (header.dataType == DataType.BOOLEAN) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new BooleanFormater();
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.CHOOSE) {
		if (header.comboData == null)
			return data;
		var keyValues = header.comboData.getValueArray();
		var captionValues = header.comboData.getNameArray();
		var index = keyValues.indexOf(data);
		if (index != -1)
			return captionValues[index];
		return "";
	} else if (header.dataType == DataType.DATE) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new DateFormater();
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.INTEGER) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new IntegerFormater(header.integerMinValue,
					header.integerMaxValue);
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.Decimal
			|| header.dataType == DataType.FLOAT
			|| header.dataType == DataType.fLOAT
			|| header.dataType == DataType.UFDOUBLE
			|| header.dataType == DataType.dOUBLE) {
		var formater;
		/*add by zuopf 2012.10.15 添加如果是合计行把maxValue、minValue设置为空*/
		if(isComputeSum){
			formater = new DicimalFormater(header.precision, null, null);
		}else{
			if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new DicimalFormater(header.precision,
					header.floatMinValue, header.floatMaxValue);
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		
			}
		}
		// 如果decimal精度变化则重新设置formatter精度
		if (formater.precision != header.precision)
			formater.precision = header.precision;
		return formater.format(data);
	} else {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new Formater();
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	}
};


/**
 * 设置被改变的对象信息
 * @since 6.3
 * @param context 后台发生变化的值，json对象
 * @private
 */
GridComp.prototype.setChangedContext = function(context) {
	if (context.enable != null)
		this.setActive(context.enable);
	if (context.editable != null
			&& (this.editable == null || (this.editable != null && this.editable != context.editable)))
		this.setEditable(context.editable);
	if(context.gridMenuCtx != null){
		var gridMenuCtx = context.gridMenuCtx;
		this.menubarComp.setChangedContext(gridMenuCtx);
	}
	if (context.hideErrorMsg != null)
		this.hideErrorMsg();
	if (context.showTree != null)
		this.setShowTree(context.showTree);
	if (this.model == null)
		return;
	var headers = this.model.basicHeaders;
	if(headers == null || headers.length == 0)
		return;
	if (context.columnContexts) {
		for (var i = 0, n = context.columnContexts.length; i < n; i++) {
			//处理GridColumnGroup
			if(context.columnContexts[i].isColumnGroup == "isColumnGroup"){
				if(context.columnContexts[i].column_visible != null){
					var header = this.model.getHeaderById(context.columnContexts[i].id);
					this.setGroupColumnVisible(header, context.columnContexts[i]);
				}
			}
			for (var j = 0; j < headers.length; j++) {
				if(headers[j].id == context.columnContexts[i].id){	
					var columnContext = context.columnContexts[i];
					var header = headers[j];
					if(columnContext.column_bgcolor != null)
						this.setColumnBGColor(header, columnContext);
					if(columnContext.column_visible != null)
						this.setColumnVisibleB(header, columnContext);
					if(columnContext.column_textcolor != null)
						this.setColumnTextColor(header, columnContext);
					if(columnContext.column_editable != null)
						this.setColumnEditableB(header, columnContext);
					if(columnContext.column_width != null)
						this.setColumnWidth(header, columnContext);
					if(columnContext.text != null)
						header.replaceText(columnContext.text);
					if(columnContext.precision != null)
						header.setPrecision(columnContext.precision);
					if(columnContext.matchValues != null)
						this.showComp.setMatchValues(columnContext.matchValues);
					if(columnContext.beforeOpenParam != null)
						this.showComp.beforeOpenParam(columnContext.beforeOpenParam);
					if(columnContext.gridTipContent != null)
						this.setGridTipContent(columnContext.gridTipContent);
					if(columnContext.showImageBtn != null)
						this.setHeaderBtnVisible(columnContext.showImageBtn);
					/*if(columnContext.viewURL != null)
						this.setViewURL(j,columnContext.viewURL);*/
				}
			}
		}
	}
};

GridComp.nextMouseOver = function(e){
	var event = EventUtil.getEvent(e);
	stopAll(event);
	this.className = 'nextover';
};

GridComp.nextMouseOut = function(e){
	var event = EventUtil.getEvent(e);
	stopAll(event);
	this.className = 'next';
};

GridComp.preMouseOver = function(e){
	var event = EventUtil.getEvent(e);
	stopAll(event);
	this.className = 'preover';
};

GridComp.preMouseOut = function(e){
	var event = EventUtil.getEvent(e);
	stopAll(event);
	this.className = 'pre';
};

GridComp.pageNavgate = function(e, index, gridId) {
	var event = EventUtil.getEvent(e);
	stopAll(event);
	if(this.pageIndex){
		index = this.pageIndex;
	}
	if(this.gridId){
		gridId = this.gridId;
	}
	var grid = window.objects[gridId];;
	if(index == grid.pageIndex)
		return;
	grid.processServerPagination(index);
};

GridComp.pagePre = function(e) {
	var event = EventUtil.getEvent(e);
	stopAll(event);
	var grid = window.objects[this.gridId];
	if(grid.pageIndex == 0)
		return;
	grid.processServerPagination(grid.pageIndex - 1);
};

GridComp.pageFirst = function(e){
	var event = EventUtil.getEvent(e);
	stopAll(event);
	var grid = window.objects[this.gridId];
	if(grid.pageIndex == 0)
		return;
	grid.processServerPagination(0);
};

GridComp.pageNext = function(e) {
	var event = EventUtil.getEvent(e);
	stopAll(event);
	var grid = window.objects[this.gridId];
	if(grid.pageIndex == grid.model.getPageCount() - 1)
		return;
	grid.processServerPagination(grid.pageIndex + 1);
};

GridComp.pageLast = function(e){
	var event = EventUtil.getEvent(e);
	stopAll(event);
	var grid = window.objects[this.gridId];
	if(grid.pageIndex == grid.model.getPageCount() - 1)
		return;
	grid.processServerPagination(grid.model.getPageCount() - 1);
};


GridComp.prototype.initColumnContextMenu = function() {
	var oThis = this;
	if (!this.columnContextMenu) {
		var menuName = this.id + "_menu";
		this.columnContextMenu = new ContextMenuComp(menuName, 0, 0, false);
		
//		// 隐藏
//		var hiddenCaption = "隐藏"; // TODO 多语
//		var menuHidden= this.columnContextMenu.addMenu(menuName + "_hidden", hiddenCaption, null, null, false);
//		// 增加点击事件
//		var hiddenListener = new Listener("onclick");
//		hiddenListener.func = function(e) {
//			oThis.saveColumnVisibleToLocal(item.id, false);
//			oThis.setColumnVisible(item.id+":false");
//		};
//		menuHidden.addListener(hiddenListener);
		
		
		// 表格列
		var columnCaption = trans("ml_show_column"); // TODO 多语
		var menuColumn = this.columnContextMenu.addMenu(menuName + "_column", columnCaption, null, null, false);
		this.columnContextMenu.menuColumn = menuColumn;
		
		
		// 清除设置
		var cleanCaption = trans("ml_clear_set"); // TODO 多语
		var menuClean= this.columnContextMenu.addMenu(menuName
				+ "_clean", cleanCaption, null, null, false);
		// 增加点击事件
		var cleanListener = new Listener("onclick");
		cleanListener.func = function(e) {
			var defData = oThis.getLocalData();
			if (defData != null){
				var cols = defData["colVisible"];
				defData["colVisible"] = null;
				defData["colOrder"] = null;
				saveStorageData();
				if (cols != null){
					var columns = new Array();
					for (var i = 0; i < cols.length; i++){
						columns.push(cols[i].keyName + ":true");
					}
					oThis.setColumnVisible(columns);
				}
			}
		};
		menuClean.addListener(cleanListener);
		
		//关闭菜单
		if(IS_IPAD){
			var hiddenCaption = trans("ml_hidden_menu");//TODO 多语
			var menuHidden = this.columnContextMenu.addMenu(menuName
					+ "_hidden", "关闭菜单", null, null, false);
			// 增加点击事件
			var hiddenListener = new Listener("onclick");
			hiddenListener.func = function(e) {
				this.hideTipMessage(true);
				this.hideenColumnContentMenu();
			};
			menuHidden.addListener(hiddenListener);
		}
		//初始化column项
		this.initColumnItemMenu(this.columnContextMenu.menuColumn);
	}
	
};

/**
 * 初始化表格列项菜单
 * @param {} menuColumn
 * @private  
 */
GridComp.prototype.initColumnItemMenu = function(menuColumn) {
	if (!isNull(menuColumn.childMenu, false)){
		menuColumn.removeChildMenu();
	}
	var headers = this.model.getBasicHeaders();	
	for (var i = 0; i < headers.length; i++){
		var keyName = headers[i].keyName;
		var showName = headers[i].showName;
		if (headers[i].isHidden == false || this.isLocalHiddenColumn(keyName)){
			var visible = !headers[i].isHidden && !this.isLocalHiddenColumn(keyName);
			this.createColumnItemMenu(menuColumn, keyName, showName, showName, visible);
		}
	}
};

/**
 * 是否本地设置为隐藏
 * @param {} keyName
 * @return {Boolean}
 * @private
 */
GridComp.prototype.isLocalHiddenColumn = function(keyName){
	var cols = this.getLocalColumnVisible();
	if (cols == null) return false;
	for (var i = 0; i < cols.length; i++){
		if (keyName == cols[i].keyName)
			return true;
	}
	return false;
};

/**
 * 创建表格列项菜单
 * @param {} id
 * @param {} name
 * @param {} tip
 * @param {} visible
 * @private
 */
GridComp.prototype.createColumnItemMenu = function(parentMneu, id, name, tip, visible){
	var oThis = this;
	visible = getBoolean(visible,true);	
	var item = parentMneu.addMenu(id, name, tip, null, true, visible, null);
	item.Div_gen.onclick = function(e){
	};
	item.checkbox.onclick = function(e){
		e = EventUtil.getEvent(e);
		parentMneu.parentOwner.keepShow = true;
		item.parentOwner.keepShow = true;
		//显示列
		if (this.checked){
			oThis.saveColumnVisibleToLocal(item.id, true);
			oThis.setColumnVisible([item.id+":true"]);
		}
		//隐藏列
		else{
			var showColumns = oThis.getVisibleColumnIds();
			if (showColumns.length <= 1)
				stopAll(e);
			else{
				oThis.saveColumnVisibleToLocal(item.id, false);
				oThis.setColumnVisible([item.id+":false"]);
			}	
		}
//		stopAll(e);
		stopEvent(e);
		
	};
	item.Div_gen.onclick = function(e) {
		item.checkbox.click();
		e = EventUtil.getEvent(e);
//		stopAll(e);
		stopEvent(e);
	};
	
};

/**
 * 显示所有已经显示出来的设置menu
 * 
 * @private
 */
GridComp.prototype.showColumnContentMenu = function(e) {
	//初始化列头菜单
	this.initColumnContextMenu();
	if (this.columnContextMenu && this.columnContextMenu.visible == false)
		this.columnContextMenu.show(e);
};


/**
 * 隐藏所有已经显示出来的设置menu
 * 
 * @private
 */
GridComp.prototype.hideenColumnContentMenu = function() {
	if (this.columnContextMenu && this.columnContextMenu.visible)
		this.columnContextMenu.hide();
	if (this.columnContextMenu.menuColumn.childMenu && this.columnContextMenu.menuColumn.childMenu.hide)
		this.columnContextMenu.menuColumn.childMenu.hide();
};

/**
 * 获取本地个性化存储的设置
 * @private
 */
GridComp.prototype.getLocalData = function() {
	var storageData = getStorageData();
	if (storageData == null) return null;
	var selfDefData = storageData["selfDef"];
	if (selfDefData == null) return null;
	var pageId = getPageId();
	var defData = selfDefData[pageId + "_" + this.id];
	if (defData == null) return null;
	return defData;
};

/**
 * 初始化本地个性化存储的设置 ,不支持html5的localStorage时，返回null
 * @private
 */
GridComp.prototype.initLocalData = function() {
	var storageData = getStorageData();
	if (storageData == null) return null;
	if (storageData["selfDef"] == null)
		storageData["selfDef"] = {};
	var selfDefData = storageData["selfDef"];
	var pageId = getPageId();
	if (selfDefData[pageId + "_" + this.id] == null)
		selfDefData[pageId + "_" + this.id] = {};
	var defData = selfDefData[pageId + "_" + this.id];
	return defData;
};

/**
 * 获取本地列显示隐藏设置
 * @return {}
 * @private
 */
GridComp.prototype.getLocalColumnVisible = function() {
	var defData = this.getLocalData();
	if (defData == null) return null;
	return defData["colVisible"];
};

/**
 * 同步本地列显示隐藏设置到localStorage
 * @return {}
 * @private
 */
GridComp.prototype.saveColumnVisibleToLocal = function(keyName, visible) {
	visible = getBoolean(visible, true);
	var changed = false;
	var defData = this.getLocalData();
	if (defData == null)
		defData = this.initLocalData();
	if (defData == null) return;
	if (defData["colVisible"] == null)
		defData["colVisible"] = new Array();
	var visibleData = defData["colVisible"];
	for (var i = 0; i < visibleData.length; i ++){
		if (visibleData[i].keyName == keyName){
			visibleData.splice(i,1);
			changed = true;
			break;
		} 
	} 
	if (visible == false){
		var obj = {};
		obj.keyName = keyName;
		visibleData.push(obj);
		changed = true;
	}
	if (changed)
		saveStorageData();
	
};

/**
 * 获取本地列顺序设置
 * @return {}
 * @private
 */
GridComp.prototype.getLocalColumnOrder = function() {
	var defData = this.getLocalData();
	if (defData == null) return null;
	return defData["colOrder"];
};

/**
 * 同步本地列顺序设置到localStorage
 * @return {}
 * @private
 */
GridComp.prototype.saveColumnOrderToLocal = function(headers) {
	var changed = false;
	var defData = this.getLocalData();
	if (defData == null)
		defData = this.initLocalData();
	if (defData == null) return;
	defData["colOrder"] = new Array();
	var orderData = defData["colOrder"];
	for (var i = 0; i < headers.length; i ++){
		if (headers[i] == null) continue;
		var columnObj = {};
		columnObj.keyName = headers[i].keyName;
		columnObj.index = i;
		orderData.push(columnObj);
	} 
	saveStorageData();
};

/**
 * 
 * 把源header放到目标header之前
 * @param {} sourceKeyName
 * @param {} targetKeyName
 */
GridComp.prototype.changeColumnOrder = function(sourceKeyName, targetKeyName){
	this.keepScroll = true;
	var sourceHeader = this.model.getHeader(sourceKeyName);
	this.model.removeHeader(sourceKeyName);
	var targetHeader = this.model.getHeader(targetKeyName);
	var index = this.model.headers.indexOf(targetHeader);
	this.model.addHeader(sourceHeader,index);
	this.saveColumnOrderToLocal(this.model.headers);
	//浏览器不支持本地存储
	if (this.getLocalData() == null){
		this.model.initBasicHeaders();
		this.model.initBindRelation();
		this.model.rows = null;
		this.model.initUIRows();
	}
	this.setModel(this.model);
};


/****************************************************************
 * 
 *	paint 界面渲染相关方法
 *
*****************************************************************/

/**
 * paint所有重新设定的model数据
 * 
 * @private
 */
GridComp.prototype.paintData = function() {
	// 清空原来数据显示页
	this.clearDivs();
	if (this.model == null)
		return;

		
	// 初始化构建框架所需的各个常量
	var initSuccess = this.initConstant();

	if (initSuccess == false)
		return;
	// 根据model初始化header
	this.initBasicHeaders();
	// 判断初始化界面时是否会出现横向、纵向滚动条
	this.adjustScroll();
	// 初始化基础框架
	this.initDivs();
	// 根据model初始化数据
	this.initDatas();
	// 真正的画界面
	if(!this.scrollState){
		var self = this;
		$("#"+this.outerDivId).perfectScrollbar("",this.outerDivId);
		$(window).bind("resize",function (){
			$("#"+self.outerDivId).perfectScrollbar('updateAll',this.outerDivId);
		});
//		setTimeout(function (){
//			$("#"+self.outerDivId).perfectScrollbar('updateAll');
//		},1000);
		this.scrollState = true;
	}else{
//		$("#"+this.outerDivId).perfectScrollbar('updateAllandLeft',this.outerDivId);
	}
	this.paintZone();
	// 各区域的事件处理
	this.attachEvents();
	// 当前选中行索引
	this.selectedRowIndice = null;
	this.currActivedCell = null;
	if (this.showComp)
		this.hiddenComp();
// 此处加入自定义滚动条
		

	
};

/**
 * 创建最外层包容div
 * 
 * @private
 */
GridComp.prototype.initOuterDiv = function() {
	if(this.outerDiv){
		return;
	}
	var oThis = this;
	this.outerDiv = $ce("div");
	this.outerDiv.className = this.className;
	if(this.hasBorder)
		$(this.outerDiv).addClass("outDivExtendCss");
	this.outerDiv.style.left = this.left;
	this.outerDiv.style.top = this.top;
	this.outerDiv.style.width = this.width;
	if(!this.isRunMode){
		this.outerDiv.style.overflowX = "hidden";
	}
	if(!this.flowmode){
		this.outerDiv.style.overflowY = "hidden";
		this.outerDiv.style.overflowX = "hidden";
	}	
	this.outerDiv.onscroll = function(e) {
		//修改编辑控件位置
		if(oThis.currActivedCell && oThis.showComp){
			oThis.showComp.setBounds(compOffsetLeft(oThis.currActivedCell, document.body) + GridComp.CELL_LEFT_PADDING,
					compOffsetTop(oThis.currActivedCell, document.body) - compScrollTop(oThis.currActivedCell, document.body) , oThis.currActivedCell.offsetWidth - GridComp.CELL_RIGHT_PADDING - 1,
					oThis.currActivedCell.offsetHeight);
			oThis.showComp.setFocus();
		}
	};
	this.wholeDiv.appendChild(this.outerDiv);
};

/**
 * 画区域
 * 
 * @private
 */
GridComp.prototype.paintZone = function(key, rowIndex, hasParent, level) {
	if (this.model == null){
			// 自定义滚动条，更新相关位置。
		
		if(this.scrollState&&!this.keepScroll){
			$("#"+this.outerDivId).perfectScrollbar('updateAllandLeft',this.outerDivId);
			
		}else{
			$("#"+this.outerDivId).perfectScrollbar('updateKeepLeft',this.outerDivId);
			this.keepScroll  = false;
		}
		return;
	}
		
	if (this.needPaintRows != null && this.needPaintRows == true) {
		this.needPaintRows = false;
		this.paintRows();
	} 
	else {
		if(rowIndex == null)
			rowIndex = 0;
		// 对每行进行处理
		var rows = this.model.getRows(key);
		if (rows == null || rows.length == 0){
			// 自定义滚动条，更新相关位置。
			if(this.scrollState&&!this.keepScroll){
				$("#"+this.outerDivId).perfectScrollbar('updateAllandLeft',this.outerDivId);
				
			}else{
				$("#"+this.outerDivId).perfectScrollbar('updateKeepLeft',this.outerDivId);
				this.keepScroll  = false;
			}
			return;
		}
		var modelLen = rows.length;
		var scrollLeft = this.dataOuterDiv.scrollLeft;
		var rowHeihgt = this.rowHeight;
		var rowCount = this.model.getRowsCount();
		this.setHeadersOffsetWidth();
		initLayoutMonitorState();
		for (var i = 0; i < modelLen; i++) {
			if(hasParent){
				if(level != null){
					rows[i].level = level + 1;
					this.addOneRow(rows[i], rowIndex + i, scrollLeft, rowHeihgt, rowCount, rowIndex - 1);
				}
			}
			else
				this.addOneRow(rows[i], rowIndex + i, scrollLeft, rowHeihgt, rowCount, null);
		}
		
		//画数据时如果有选中行，则将选中行的外观改变（避免重新画界面时，选中行样式丢失）
		if(this.selectedRowIndice && this.selectedRowIndice.length > 0){
			for(var i = 0; i < this.selectedRowIndice.length > 0; i++){
				// 将选中行的外观改变
				this.changeCurrSelectedRowStyle(this.selectedRowIndice[i]);
			}
		}
		
		this.clearHeadersOffsetWidth();
		setTimeout("executeLayoutMonitor()",500);
	}
	// 调整固定列高度
	this.adjustFixedColumDivHeight();
		// 自定义滚动条，更新相关位置。
	if(this.scrollState&&!this.keepScroll){
		$("#"+this.outerDivId).perfectScrollbar('updateAllandLeft',this.outerDivId);
	}else{
		$("#"+this.outerDivId).perfectScrollbar('updateKeepLeft',this.outerDivId);
		this.keepScroll  = false;
	}
	this.constant.outerDivWidth = this.wholeDiv.offsetWidth;
	this.constant.outerDivHeight = this.wholeDiv.offsetHeight;
};

/**
 * 清除grid所有框架div
 * 
 * @private
 */
GridComp.prototype.clearDivs = function() {
	this.removeAllChildren(this.outerDiv);
	if (this.dataOuterDiv != null) {
		this.dataOuterDiv.style.width = "0px";
	}
};

/**
 * 初始化grid中的各个框架结构
 * 
 * @private
 */
GridComp.prototype.initDivs = function() {
	// 创建表头区整体div
	this.initHeaderDiv();
	// 创建固定表头区整体div
	this.initFixedHeaderDiv();
	if (this.isShowNumCol) {
		// 创建行号列表头div
		this.initRowNumHeaderDiv();
	} else
		this.constant.rowNumHeaderDivWidth = 0;

	// 创建表格行状态显示列header
	this.initLineStateHeaderDiv();
	// 创建固定表头区固定选择列
	if (this.isMultiSelWithBox)
		this.initSelectColumHeaderDiv();
	// 创建固定表头区header数据区div
	this.initFixedHeaderTableDiv();
	// 创建动态表头区整体div
	this.initDynamicHeaderDiv();
	// 创建动态表头区header数据区div
	this.initDynamicHeaderTableDiv();
	// 创建提示信息区
	if(this.isRunMode){
		this.initNoRowsDiv();
	}
	// 创建固定列整体div
	this.initFixedColumDiv();
	// 创建数据区整体div
	this.initDataOuterDiv();
	if (this.isShowNumCol) {
		// 画行数字行标列div
		this.initRowNumDiv();
	}
	// 创建固定选择列
	if (this.isMultiSelWithBox)
		this.initSelectColumDiv();
	// 创建行状态显示列
	this.initLineStateColumDiv();

	// 创建浮动在最下面的合计行
	if (this.isShowSumRow) {
		this.initSumRowDataDiv();
	}
	if (this.pageSize > 0){
		if(this.isSimplePagination){
			this.initSimplePaginationBar();
		}else{
			this.initPaginationBar();
		}
	}
};

/**
 * 根据model中的数据初始化界面数据
 * 
 * @private
 */
GridComp.prototype.initDatas = function() {
	// 根据headers model画headers内的table(数据)
	this.initHeaderTables();
	this.initFixedColumDataDiv();
	// 画动态列数据区的数据外层包装div及每列数据div
	this.initDynamicColumDataDiv();
	// 初始化合计行中的每个cell单元
	if (this.isShowSumRow)
		this.initSumRowCells();
};

/**
 * model改变后调用此方法重画所有行数据
 * 
 * @param sort
 *            表示是排序时重画，不需要重新initUIRows
 * @param
 * @private
 */
GridComp.prototype.paintRows = function(sort, startIndex, count) {
	//如果outerDiv没有显示出来，不进行paint,this.outerDiv.offsetWidth == 0 判断不准确，tab页签下在火狐浏览器中未显示的页签中 this.outerDiv.offsetWidth = 0,但是IE不等于0
	/*if (this.outerDiv.offsetWidth == 0) {
		this.needPaintRows = true;
		return;
	};*/
	if(!this.paginationBar){
		this.keepScroll = true;
	}
	if (this.isMultiSelWithBox) {
		if (this.selectColumDiv != null)
			this.selectColumDiv.parentNode.removeChild(this.selectColumDiv);
	}
	// IE下提前设置滚动条，否则remveChild之后滚动条不存在，无法触发this.dataOuterDiv.onscroll
	if (this.isScroll())
		this.setScrollLeft(0);
	if (this.dynamicColumDataDiv != null){
		this.setScrollLeft(0);
		this.dynamicColumDataDiv.parentNode.removeChild(this.dynamicColumDataDiv);
	}
	if (this.fixedColumDiv != null)
		this.fixedColumDiv.innerHTML ="";
			
	// 移除行标列
	if (this.isShowSumRow && this.rowNumDiv != null) {
		if(this.rowNumDiv.parentNode){
			this.rowNumDiv.parentNode.removeChild(this.rowNumDiv);
		}
		this.rowNumDiv.cells = null;
	}
	// 移除行状态列
//	if (this.lineStateColumDiv != null){
//		this.lineStateColumDiv.parentNode.removeChild(this.lineStateColumDiv);
//		this.lineStateColumDiv.cells = null;
//	}
	
	//重新paintRows时，可能原来有滚动条，现在没有了，要重算 this.constant.outerDivWidth
	if(this.wholeDiv.offsetWidth != 0){
		this.constant.outerDivWidth = this.wholeDiv.offsetWidth;
		var fixedColumDivWidth = 0;
		if (this.fixedColumDiv)
			fixedColumDivWidth = this.fixedColumDiv.offsetWidth;
		if(this.dataOuterDiv)
	    	this.dataOuterDiv.style.width = this.constant.outerDivWidth - fixedColumDivWidth + "px";// - 2;
	}
	
	// 将model的rows置为null,需要重新取新数据
	if (!sort) {
		this.model.rows = null;
		if (startIndex != null && count != null)
			this.model.initUIRows(startIndex, count);
		else
			this.model.initUIRows(startIndex);
	}

	var rowCount = this.getRowsNum();

	// 当前选中行索引
	this.selectedRowIndice = null;
	this.currActivedCell = null;
	// 重画行时隐藏掉当前显示的控件
	if (this.showComp)
		this.hiddenComp();

	var gridWidth = this.constant.outerDivWidth;
	var fixedHeaderDivWidth = this.constant.fixedHeaderDivWidth;
	// 调整表头的宽度
	if (this.isVScroll()) {
		this.headerDiv.style.width = (gridWidth - GridComp.SCROLLBAE_HEIGHT)
				+ "px";// - 1;
		this.headerDiv.defaultWidth = gridWidth - GridComp.SCROLLBAE_HEIGHT;// - 1;
		this.headerDiv.style.left = "0px";
		this.fixedHeaderDiv.style.left = "0px";

		var dynHeaderWidth = gridWidth - fixedHeaderDivWidth
				- GridComp.SCROLLBAE_HEIGHT;// - 1;
		if (dynHeaderWidth > 0)
			this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
		this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
	} 
	else {
		this.headerDiv.style.width = gridWidth + "px";// - 2;
		this.headerDiv.defaultWidth = gridWidth;// - 2;
		// this.headerDiv.style.left = 0;
		this.headerDiv.style.left = "0px";
		this.fixedHeaderDiv.style.left = "0px";
		var w = gridWidth - fixedHeaderDivWidth;
		if(w < 0)
			w = 0;
		/*this.dynamicHeaderDiv.style.width = (gridWidth - fixedHeaderDivWidth)
				+ "px";// - 3;
		this.dynamicHeaderDiv.defaultWidth = gridWidth - fixedHeaderDivWidth;*/// - 3;
		this.dynamicHeaderDiv.style.width = w + "px";// - 3;
		this.dynamicHeaderDiv.defaultWidth = w;
	}
	
		
	// 画动态列数据区的数据外层包装div及每列数据div
	this.initDynamicColumDataDiv();
	// 画数字行标区
	if (this.isShowNumCol)
		this.initRowNumDiv();
	// 画行状态区
	this.initLineStateColumDiv();
	// 画固定选择列
	if (this.isMultiSelWithBox)
		this.initSelectColumDiv();
	this.initFixedColumDataDiv();	
	this.paintZone();

	if (this.stForAutoExpand != null)
		clearTimeout(this.stForAutoExpand);
	this.stForAutoExpand = setTimeout(
			"GridComp.processAutoExpandHeadersWidth('" + this.id + "','"+this.outerDivId+"')", 100);
};

/****************************************************************
 * 
 *	Grid 对外开放操作方法
 *
*****************************************************************/

/**
 * 激活编辑框，进入编辑状态
 * 
 */
 GridComp.prototype.setGridInEdit = function(){
 	if (this.editable == false) return;
 	var ds = this.model.dataset;
 	if (ds == null) return;
 	if (ds.editable == false) return;
 	var rowIndex = ds.getSelectedIndex();
 	if (rowIndex == -1) return;

 	var colIndex = -1;
 	var headers = this.model.getHeaders();
	for (var i = 0; i < headers.length; i++) {
		if (headers[i].isHidden == false && headers[i].columEditable == true){
			colIndex = i;
			break;
		}
	}
 	if (colIndex == -1) return;
 	var cell = this.getCell(rowIndex, colIndex);
 	if (cell == null) return;
 	this.hiddenComp();
 	this.setCellSelected(cell);
 };



/****************************************************************
 * 
 *	treeLevel 树表相关方法
 *
*****************************************************************/
function GridTreeLevel(id, recursivePkField, recursivePPkField, labelFields, loadField, leafField) {	
	this.id = id;
	// 此level所挂的contextMenu菜单
	this.contextMenu = null;
	this.recursiveKeyField = recursivePkField;
	this.recursivePKeyField = recursivePPkField;
	this.labelFields = labelFields;
	this.loadField = loadField;
	this.leafField = leafField;
};

GridComp.prototype.hideChildRows = function(rowIndex){
	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		if (header.isHidden == true)
			continue;
		var dataDiv = header.dataDiv;
		var cell = dataDiv.childNodes[rowIndex];
		var parentCell = new Array();		
		for(var j = rowIndex + 1; j < dataDiv.childNodes.length; j ++){
			var cCell = dataDiv.childNodes[j];
			if(cCell.parentCell == cell){
				//递归的先收起子，再收起本行
				this.hideChildRows(j);
				
				//隐藏左侧checkbox
				if (this.selectColumDiv != null)
					this.selectColumDiv.cells[j].style.display = "none";
				
				cCell.style.display = "none";
				parentCell.push(cCell);
			}
			else{
				if(parentCell.length > 0){
					for (var k = 0; k < parentCell.length; k++){
						if(cCell.parentCell == parentCell[k]){
							cCell.style.display = "none";
							parentCell.push(cCell);
							break;
						}
					}
				}
			}
		}
	}
};

GridComp.prototype.showChildRows = function(rowIndex){
	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		if (header.isHidden == true)
			continue;
		var dataDiv = header.dataDiv;
		var cell = dataDiv.childNodes[rowIndex];
		for(var j = rowIndex + 1; j < dataDiv.childNodes.length; j ++){
			var cCell = dataDiv.childNodes[j];
			if (cCell.style.display == "")
				break;
			var gridRow = this.model.getRow(j);
			if (gridRow.loadImg && gridRow.loadImg.plus == false){
					gridRow.loadImg.plus = true;
					gridRow.loadImg.src = DefaultRender.plusImgSrc;
			}
			if(cCell.parentCell == cell){
				//显示左侧checkbox
				if (this.selectColumDiv != null)
					this.selectColumDiv.cells[j].style.display = "";
				
				cCell.style.display = "";
			}
		}
	}
};

GridComp.prototype.loadChild = function(fk, rowIndex, level) {
	this.paintChild(fk, rowIndex, true, level);
	var pRow = this.model.getRow(rowIndex - 1);
	pRow.loadedChild = true;
	
};

GridComp.prototype.paintChild = function(key, rowIndex, hasParent, level) {
	this.model.initUIRows(rowIndex, null, key, level);
	this.paintZone(key, rowIndex, hasParent, level);
};

/**
 * 展开所有树节点
 */
GridComp.expandAllNodes = function(gridId){
	var oThis = window.objects[gridId];
	oThis.expandAllNodes();
};
/**
 * 展开所有树节点
 */
GridComp.prototype.expandAllNodes = function() {
	
	if (this.model.treeLevel == null) 
		return;
	for(var i = 0; i < this.model.rows.length; i ++){
		var gridRow = this.model.rows[i];
		if(gridRow.loadImg){
			if (gridRow.loadImg.plus == true){
				gridRow.loadImg.click();
			}	
		}
	}
};

/**
 * 勾选本行和所有子行的checkbox,先触发展开节点方法,再递归勾选
 * 
 * @param index:父行的行号
 */
GridComp.prototype.expandAndSeclectNodesByRowIndex = function(index) {
	this.model.addRowSelected(index);
	var gridRow = this.model.rows[index];
	if(gridRow==null 
	   || gridRow.loadImg==null 
	   || gridRow.loadImg.plus==null 
	   || typeof(gridRow.loadImg)=="undefined" 
	   || typeof(gridRow.loadImg.plus)=="undefined") {
	   return;
    }
	if(gridRow.loadImg){
		if (gridRow.loadImg.plus == true){
			//触发展开事件
			if(typeof(gridRow.loadImg.onclick) == "function"){
				gridRow.loadImg.byCheckBox = true;
				gridRow.loadImg.onclick();
			}else if(typeof(gridRow.loadImg.click) == "function"){
				gridRow.loadImg.byCheckBox = true;				
				gridRow.loadImg.click();
			}
		}
	}
	if (this.model.treeLevel == null  || this.checkBoxModel == 0) return;
	var childrenRows = [];
	//得到子级节点的索引
	childrenRows = this.getChildrenRowsByRowIndex(index);
	if(childrenRows!=null && childrenRows.length>0){
		for (var i = 0; i < childrenRows.length; i++) {
			var index =  childrenRows[i];
			//递归调用展开子级
			this.expandAndSeclectNodesByRowIndex(index);
		}		
	}
};

GridComp.prototype.expandNodesByRowIndex = function(index) {
	var gridRow = this.model.rows[index];
	if (gridRow.loadImg.plus == true){
		gridRow.loadImg.click();
	}	
	if (this.model.treeLevel == null) return;
	var childrenRows = [];
	childrenRows = this.getChildrenRowsByRowIndex(index);
	if(childrenRows!=null && childrenRows.length>0){
		for (var i = 0; i < childrenRows.length; i++) {
			var index =  childrenRows[i];
			this.expandNodesByRowIndex(index);
		}		
	}
};

/**
 * 勾掉本行和本行的所有子行的checkbox,递归调用
 * 
 * @param index:父行的行号
 */
GridComp.prototype.unselectNodesByRowIndex = function(index) {
	this.model.setRowUnSelected(index);
	if (this.model.treeLevel == null) return;
	var childrenRowIndexs = this.getChildrenRowsByRowIndex(index);
	if(childrenRowIndexs!=null && childrenRowIndexs.length>0){
		for (var k = 0; k < childrenRowIndexs.length; k++) {
			this.unselectNodesByRowIndex(childrenRowIndexs[k]);
		}
	}
};

/**
 * 获取本行的下一级子节点的行号集合
 * 
 * @param rowIndex：父行行号
 */
GridComp.prototype.getChildrenRowsByRowIndex = function(rowIndex) {
	var childrenRows = [];
	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		if (header.isHidden == true){
			continue;
		}else{
			var dataDiv = header.dataDiv;
			var cell = dataDiv.childNodes[rowIndex];
			for(var j = rowIndex + 1; j < dataDiv.childNodes.length; j ++){
				var cCell = dataDiv.childNodes[j];
				if(cCell.parentCell == cell){
					childrenRows.push(j);
				}
			}
			break;
		}
	}
	return childrenRows;
};


/****************************************************************
 * 
 *	autoRowHeight 行高自适应相关方法
 *
*****************************************************************/

/**
 * 调整行高
 */
GridComp.prototype.adjustRowHeight = function(rowIndex, cell){
	if(typeof(this.rowMinHeight[rowIndex]) == "undefined")
		return;
	cell.style.textOverflow = "";
	cell.style.whiteSpace = "normal";
	cell.style.wordWrap = "break-word";
	cell.style.lineHeight = "";	
	cell.style.overflow = "auto";	
	
	cell.style.minHeight = "";
	var height = cell.offsetHeight;
	cell.realHeight = height;
	var defaultHeight = this.defaultRowMinHeight[rowIndex] == null ? GridComp.ROW_HEIGHT : this.defaultRowMinHeight[rowIndex];
	//行高变小
	if (height < this.rowMinHeight[rowIndex]){
		var maxHeight = defaultHeight;
		for (var i = 0; i < this.model.basicHeaders.length; i++) {
			var header = this.model.basicHeaders[i];
			if (header.isHidden == true)
				continue;
			if (header.dataDiv.cells && header.dataDiv.cells[rowIndex]){
//				header.dataDiv.cells[rowIndex].style.minHeight = "";
//				if (header.dataDiv.cells[rowIndex].offsetHeight > maxHeight) 
//					maxHeight = header.dataDiv.cells[rowIndex].offsetHeight;
				if (header.dataDiv.cells[rowIndex].realHeight > maxHeight) 
					maxHeight = header.dataDiv.cells[rowIndex].realHeight;
			}	
		}
		this.setRowMinHeight(rowIndex, maxHeight);
//		cell.style.minHeight = this.rowMinHeight[rowIndex] + "px";
	}
	else if (height == this.rowMinHeight[rowIndex]){
		cell.style.minHeight = this.rowMinHeight[rowIndex] + "px";
	}
	//行高变大
	else
		this.setRowMinHeight(rowIndex, height);
};

/**
 * 设置一行的最小高度
 * @param {} rowIndex
 * @param {} height
 * @private
 */
GridComp.prototype.setRowMinHeight = function(rowIndex, height){
	this.rowMinHeight[rowIndex] = height;
	if (this.lineStateColumDiv){
		this.lineStateColumDiv.cells[rowIndex].style.minHeight = height + "px";
	}	
	if (this.selectColumDiv){
		this.selectColumDiv.childNodes[rowIndex].style.minHeight = height + "px";	
	}
	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		if (header.isHidden == true)
			continue;
		if (header.dataDiv.cells && header.dataDiv.cells[rowIndex])	
			header.dataDiv.cells[rowIndex].style.minHeight = height + "px";
	}		
};




/*******************************************************************************
 * 
 * GridCompHeader 创建表头及表头操作方法
 * 
 ******************************************************************************/
/**
 * 表头类构造函数
 * 
 * @class 表头类，用于构件grid的表头。
 * @constructor 表头类构造函数
 * @param keyName
 *            表头的真实值
 * @param showName
 *            表头的显示值
 * @param width
 *            表头宽度
 * @param dataType
 *            此列的数据类型
 * @param sortable
 *            此列是否可排序
 * @param isHidden
 *            初始化时是否隐藏
 * @param columEditable
 *            此列是否可以编辑
 * @param columBgColor
 *            此列的背景色
 * @param textAlign
 *            此列的文字居中方式
 * @param textColor
 *            此列的文字颜色
 * @param isFixedHeader
 *            此列是否是固定列
 * @param renderType
 *            渲染器类型
 * @param editorType
 *            编辑器类型
 * @param topHeader
 *            isGroupHeader为true时有用,若此header是多表头中的一个header,此参数表示所属多表头的最顶层header(null说明此header是最顶层header,否则必须传入此header的顶层header)
 * @param groupHeader
 *            isGroupHeader为true时有用,如果此header是属于一个特定groupHeader组,则此参数表示此header所属于的groupHeader
 * @param isGroupHeader
 *            true表示此header是多表头中的header
 * @param isSumCol
 *            是否是合计列
 * @param isAutoExpand
 *            自动扩展列,设置为自动扩展列的header会自动占满表格的剩余空间
 */
function GridCompHeader(keyName, showName, width, dataType, sortable, isHidden,
		columEditable, defaultValue, columBgColor, textAlign, textColor,
		isFixedHeader, renderType, editorType, topHeader, groupHeader,
		isGroupHeader, isSumCol, isAutoExpand, isShowCheckBox, sumColRenderFunc,selectOnly,
		linkType, imgPath, target, viewURL, editMin, editSec, needNullOption) {
	width = getInteger(width, GridComp.COlUMWIDTH_DEFAULT);
	if(width < 35){
		width = 35;
	}
	this.width = width;
	this.oldWidth = width;
	this.children = null;
	this.keyName = keyName;
	this.showName = getString(showName, "");
	// 此列是否默认隐藏
	this.isHidden = getBoolean(isHidden, false);
	this.parent = null;
	// 初始是否是固定表头(默认为动态表头)
	this.isFixedHeader = getBoolean(isFixedHeader, false);
	// 此列的数据类型
	this.dataType = getString(dataType, DataType.STRING);
	// 此列是否可排序
	this.sortable = getBoolean(sortable, true);
	// cell中元素的对齐方式
	this.textAlign = getString(textAlign, "left");
	this.columBgColor = getString(columBgColor, "");
	this.textColor = getString(textColor, "#333");
	// 此列的默认值
	this.defaultValue = getString(defaultValue, "");
	this.columEditable = getBoolean(columEditable, true);
	// 值解析器.如果没有设置,则将根据数值类型调用默认解析器.
	this.parser = null;
	// 渲染器对象的引用
	this.renderType = renderType;
	// 此列的编辑类型(即是什么控件)
	this.editorType = getString(editorType, EditorType.STRINGTEXT);
	// 表示该列是否是合计列
	this.isSumCol = getBoolean(isSumCol, false);
	// 合计行的渲染方式
	if(sumColRenderFunc){
		this.sumColRenderFunc = sumColRenderFunc;
	}	
	// 该表头是否自动扩展
	this.isAutoExpand = getBoolean(isAutoExpand, false);
	// 是否分组显示,如果分组显示,相同的一组值将只会显示第一个值
	this.isGroupBy = false;
	// 是否显示表头checkbox
	this.isShowCheckBox = getBoolean(isShowCheckBox, true);

	// 标示此header是否是多表头中的header
	this.isGroupHeader = getBoolean(isGroupHeader, false);
	
	// 控制comboBox是否只能从combodata中取数据
	this.selectOnly = getBoolean(selectOnly,true);
	//LinkTextComp根据输入内容构建URL
	this.linkType = getString(linkType,"");
	//linkTextComp右侧显示图片URL
	this.imgPath = getString(imgPath,"");
	//linkTextComp打开超链接方式
	this.target = getString(target,"");
	//參照的viewURL
	this.viewURL = getString(viewURL,"");
	
	this.editMin = getBoolean(editMin, true);
	this.editSec = getBoolean(editSec, true);
	this.needNullOption = getBoolean(needNullOption, false);
	this.refFieldArr = null;
	
	if (this.isGroupHeader) {
		// 多表头最顶层表头
		this.topHeader = topHeader;
		if (this.topHeader == null || this.topHeader == "")
			this.allChildrenHeader = new Array();
		else
			this.topHeader.allChildrenHeader.push(this);

		if (groupHeader != null && groupHeader != "") {
			// 如果 groupHeader隐藏，子改设置为隐藏
			if (groupHeader.isHidden == true)
				this.isHidden == true;
			groupHeader.addChildHeader(this);
		}

	}
};

GridCompHeader.prototype.precisionNullType = "nullType";
GridCompHeader.prototype.precisionPositiveType = "positiveType";
GridCompHeader.prototype.precisionNegativeType = "negativeType";
/**
 * 修改表头显示文字
 */
GridCompHeader.prototype.replaceText = function(text) {
	if (this.textNode && text != null) {
		this.textNode.innerHTML = "";
		this.textNode.appendChild(document.createTextNode(text));
		this.showName = text;
	}
};

/**
 * 设置显示状态
 */
GridCompHeader.prototype.setShowState = function(state) {
	this.showState = state;
};

/**
 * 设置相关字段
 */
GridCompHeader.prototype.setRefFields = function(refFields) {
	this.refFieldsArr = refFields;
};



/**
 * 增加子header,多表头情况的处理
 */
GridCompHeader.prototype.addChildHeader = function(header) {
	if (this.children == null)
		this.children = new Array();
	this.children.push(header);
	// 保存此header的父header
	header.parent = this;
};

/**
 * 根据传入的最父级header,返回指定层数的所有header
 * 
 * @param level
 *            层数从0开始
 * @return header数组(包括隐藏列) 注:调用此方法的只能是最顶层header
 * @private
 */
GridCompHeader.prototype.getHeadersByLevel = function(level) {
	var headers = this.getAllChildrenHeaderByLevel(level);
	return headers;
};

/**
 * 得到多表头列的每一层的最左边的header(用于改变边界style)
 * 
 * @return headers数组
 * @private
 */
GridCompHeader.prototype.getAllLeftHeaders = function() {
	if (this.children == null)
		return;
	var depth = this.getDepth();
	var headers = new Array();
	var temp = null;
	for (var i = 0; i < depth; i++) {
		temp = this.getVisibleHeadersByLevel(i);
		if (temp != null && temp.length > 0)
			headers.push(temp[0]);
	}
	return headers;
};

/**
 * 得到指定层的可见header,不包括隐藏列
 * 
 * @param level
 *            层数从0开始
 * @return header数组 注:调用此方法的只能是最顶层header
 * @private
 */
GridCompHeader.prototype.getVisibleHeadersByLevel = function(level) {
	// 隐藏列直接返回
	if (this.isHidden)
		return null;
	var headers = this.getAllChildrenHeaderByLevel(level);
	var temp = new Array();
	for (var i = 0; i < headers.length; i++) {
		if (headers[i].isHidden == false)
			temp.push(headers[i]);
	}
	headers = null;
	return temp;
};

/**
 * 得到指定层的所有子header,不考虑子header是隐藏列的情况 注:任何header均可调用此方法
 * 
 * @private
 */
GridCompHeader.prototype.getAllChildrenHeaderByLevel = function(level) {
	var temp = new Array();
	// 顶层header调用情况
	if (this.parent == null && this.children != null) {
		// 取第0级直接返回this
		if (level == 0) {
			temp.push(this);
			return temp;
		}
		// 取其他级
		else {
			if (this.allChildrenHeader != null
					&& this.allChildrenHeader.length > 0) {
				for (var i = 0; i < this.allChildrenHeader.length; i++) {
					if (this.allChildrenHeader[i].getHeaderLevel() == level)
						return this.allChildrenHeader[i].parent.children;
				}
			}
		}
	}
	// basicHeader调用情况
	else if (this.parent != null && this.children == null) {
		temp.push(this);
		return temp;
	} else {
		// 得到此header的所在层数
		var currHeaderLevel = this.getHeaderLevel();
		var header = this;
		// 得到最顶层header
		while (header.parent != null)
			header = header.parent;
		for (var i = 0; i < header.allChildrenHeader.length; i++) {
			if (header.allChildrenHeader[i].getHeaderLevel() == currHeaderLevel
					+ 1 + level)
				return header.allChildrenHeader[i].parent.children;
		}
	}
	// 返回空数组
	return temp;
};

/**
 * 得到header深度
 * 
 * @private
 */
GridCompHeader.prototype.getDepth = function() {
	return 1 + this.getMaxDepth(this);
};

/**
 * 递归得到表头最大深度
 * 
 * @private
 */
GridCompHeader.prototype.getMaxDepth = function() {
	var maxDepth = 0;
	if (this.children != null) {
		var childs = this.children;
		for (var i = 0; i < childs.length; i++) {
			if (!childs[i].isHidden) {
				var depth = 1 + childs[i].getMaxDepth();
				if (depth > maxDepth)
					maxDepth = depth;
			}
		}
	}
	return maxDepth;
};

/**
 * 得到header所在的层值 注:层数从0开始
 * 
 * @private
 */
GridCompHeader.prototype.getHeaderLevel = function() {
	var level = 0;
	if (this.parent != null)
		level = 1 + this.parent.getHeaderLevel();
	return level == 0 ? 0 : level;
};

/**
 * 递归得到给定header的colspan值
 * 
 * @private
 */
GridCompHeader.prototype.getColspan = function() {
	var w = 0;
	if (this.children != null) {
		for (var i = 0; i < this.children.length; i++) {
			if (!this.children[i].isHidden) {
				var ret = this.children[i].getColspan();
				w += ret;
			}
		}
	}
	return w == 0 ? 1 : w;
};

/**
 * 得到header列最底层的全部basicHeaders(顺序为从左到右)
 * 
 * @return basicHeaders数组 注:调用此方法的只能是顶层header
 * @private
 */
GridCompHeader.prototype.getBasicHeaders = function() {
	var basicHeaders = new Array();
	if (this.children == null) {
		basicHeaders.push(this);
	} else {

		// // header的孩子层数
		// var level = this.getHeaderChildrenLevel();
		// // 得到从左到右的按顺序的basicHeaders
		// for ( var i = level; i >= 0; i--) {
		// var headers = this.getHeadersByLevel(i);
		// for ( var j = 0; j < headers.length; j++) {
		// if (headers[j].children == null)
		// basicHeaders.push(headers[j]);
		// }
		// }
		this.getBasicHeader(this, basicHeaders);

	}
	return basicHeaders;
};

/**
 * 递归取header的basicheaders
 * 
 * @param {GridCompHeader}
 *            header
 * @param {array}
 *            basicHeaders
 */
GridCompHeader.prototype.getBasicHeader = function(header, basicHeaders) {
	for (var i = 0; i < header.children.length; i++) {
		var childrenHeader = header.children[i];
		if (childrenHeader.children == null)
			basicHeaders.push(childrenHeader);
		else
			this.getBasicHeader(childrenHeader, basicHeaders);
	}
};

/**
 * 得到任意给定header的所有下挂的basicHeaders
 * 
 * @private
 */
GridCompHeader.prototype.getBasicHeadersBySpecify = function() {
	var headers = new Array();
	if (this.children == null) {
		headers.push(this);
		return headers;
	} else {
		if (this.parent == null)
			return this.getBasicHeaders();
		else {
			// 得到此header的孩子层数
			var childLevel = this.getHeaderChildrenLevel();
			for (var i = 0; i <= childLevel; i++) {
				var currLevelHeaders = this.getAllChildrenHeaderByLevel(i);
				for (var j = 0; j < currLevelHeaders.length; j++) {
					if (currLevelHeaders[j].children == null)
						headers.push(currLevelHeaders[j]);
				}
			}
			return headers;
		}
	}
};

/**
 * 此header列最底层有多少个子header 根据传入的最父级header
 * 
 * @private
 */
GridCompHeader.prototype.getDepthestHeadersNum = function() {
	if (this.parent != null)
		return null;
	// 最顶层header的colspan的值就是最底层的header数
	return this.getColspan();
};

/**
 * 得到header的rowspan值
 * 
 * @totalDepth 此header所在table的总深度
 * @private
 */
GridCompHeader.prototype.getRowspan = function(totalDepth) {
	var childLevel = 0;
	if (this.children != null)
		childLevel = this.getHeaderChildrenLevel();
	var rowspan = totalDepth - this.getHeaderLevel() - childLevel;
	return rowspan;
};

/**
 * 对于多表头列求得给定一个header的孩子层数
 * 
 * @private
 */
GridCompHeader.prototype.getHeaderChildrenLevel = function() {
	return this.getMaxDepth();
};

/**
 * 如果header是符点数，则调用此方法设置最小值
 */
GridCompHeader.prototype.setFloatMinValue = function(minValue) {
	if (!isNaN(parseFloat(minValue))) {
		this.floatMinValue = parseFloat(minValue);
	} else
		this.floatMinValue = null;
};

/**
 * 如果header是浮点数，则调用此方法设置最大值
 */
GridCompHeader.prototype.setFloatMaxValue = function(maxValue) {
	if (!isNaN(parseFloat(maxValue))) {
		this.floatMaxValue = parseFloat(maxValue);
	} else
		this.floatMaxValue = null;
};

/**
 * 如果header类型是inttext，则调用此方法设置最小值
 * 
 * @param minValue
 *            最小值，不能小于javascript允许的整数最小值，小于则采用默认最小值
 */
GridCompHeader.prototype.setIntegerMinValue = function(minValue) {
	if (minValue != null) {
		// 判断minValue是否是数字
		if (isNumber(minValue)) {
			if ((parseInt(minValue) >= -9007199254740992)
					&& (parseInt(minValue) <= 9007199254740992))
				this.integerMinValue = minValue;
			else
				this.integerMinValue = "";
		} else
			this.integerMinValue = -9007199254740992;
	}
};

/**
 * 如果header类型是inttext，则调用此方法设置最大值
 * 
 * @param maxValue
 *            最大值，不能大于javascript允许的整数最大值，大于则采用默认最大值
 */
GridCompHeader.prototype.setIntegerMaxValue = function(maxValue) {
	if (maxValue != null) {
		// 判断maxValue是否是数字
		if (isNumber(maxValue)) {
			if (parseInt(maxValue) >= -9007199254740992
					&& parseInt(maxValue) <= 9007199254740992)
				this.integerMaxValue = maxValue;
			else
				this.integerMaxValue = "";
		} else
			this.integerMaxValue = 9007199254740992;
	}
};



/**
 * 如果header类型是numtext，则调用此方法设置精度
 * 
 * @param precision
 *            int型，小数点后几位
 */
GridCompHeader.prototype.setPrecision = function(precision,fromDs) {
	fromDs = (fromDs == null) ? false : fromDs;
	if (fromDs == true){
		this.precisionFromDs = true;
	}
	//以ds设置的精度为准
	if (this.precisionFromDs != null && this.precisionFromDs == true && fromDs == false)
		return;
	if (this.precision == null || this.precision != precision) {
		this.precision = getString(precision + "","");
		// 如果precision为空则显示和输入都不控制精度
		if(this.precision == "" || this.precision == "null" || this.precision == "undefined"){
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
		this.reRender();
		// 修改精度之后显示值可能发生变化，此时需要对ds进行相应修改
		this.reSetDataset();
	}
};

/**
 * 重新render列中的cell
 * 
 */
GridCompHeader.prototype.reRender = function() {
	if (this.dataDiv && this.dataDiv.cells) {
		var grid = this.owner;
		for (var i = 0; i < this.dataDiv.cells.length; i++) {
			var cell = this.dataDiv.cells[i];
			if (cell == null) continue;
			var value = grid.model.getCellValueByIndex(i,cell.colIndex);
			if (cell.firstChild != null)
				cell.removeChild(cell.firstChild);
			if(this.renderType && this.renderType.render){
//				this.renderType.render.call(grid, i, this.dataDiv.cells[i].colIndex, value, this, cell);
				grid.renderCell(this.renderType, i, this.dataDiv.cells[i].colIndex, value, this, cell);
			}
			if (this.autoRowHeight == true)	
				this.adjustRowHeight(i, cell);	
			
		}
	}
};

/**
 * 重新渲染之后根据变化值设置dataset
 * 
 */
GridCompHeader.prototype.reSetDataset = function() {
	if (this.dataDiv && this.dataDiv.cells) {
		var grid = this.owner;
		var dataset = grid.model.dataset;
		if(dataset == null) return;
		for (var i = 0; i < this.dataDiv.cells.length; i++) {
			var cell = this.dataDiv.cells[i];
			if (cell == null) continue;
			var index = dataset.nameToIndex(this.keyName);
			dataset.setValueAt(i,index,cell.value);
		}
	}
};



/**
 * 如果header类型是stringtext，则调用此方法设置最大长度
 * 
 * @param{int} maxLength 最大字符允许长度
 */
GridCompHeader.prototype.setMaxLength = function(maxLength) {
	this.maxLength = parseInt(maxLength);
};

/**
 * 设置此列是否必须填写，设置为true会改变表头的颜色
 * 
 * @param isRequired
 *            此列是否为必输项
 */
GridCompHeader.prototype.setRequired = function(isRequired) {
	this.required = getBoolean(isRequired, false);
};

/**
 * 参照类型设置nodeinfo
 */
GridCompHeader.prototype.setNodeInfo = function(nodeInfo) {
	this.nodeInfo = nodeInfo;
};

/**
 * 参照对应viewURL
 */
GridCompHeader.prototype.setViewURL = function(viewURL) {
	this.viewURL = viewURL;
};

/**
 * 参照对应pkField
 */
GridCompHeader.prototype.setPkField = function(pkField) {
   this.pkField = pkField;
};
/**
 * 设置checkbox的真实值和显示值
 */
GridCompHeader.prototype.setValuePair = function(valuePair) {
	this.valuePair = valuePair;
};

/**
 * 如果header类型是combox，则调用此方法把showImgOnly的值传入，来标明此列 的combox是什么类型的combox
 * 
 * @param
 */
GridCompHeader.prototype.setShowImgOnly = function(showImgOnly) {
	this.showImgOnly = showImgOnly;
};

/**
 * 如果header类型是combox，则调用此方法设置combox的ComboData
 */
GridCompHeader.prototype.setHeaderComboBoxComboData = function(comboData) {
	this.comboData = comboData;
};

/**
 * 如果header类型是languageCcombox，则调用此方法设置下拉项
 */
GridCompHeader.prototype.setHeaderLanguageComboBoxs = function(langugeComboDatas) {
	this.langugeComboDatas = langugeComboDatas;
};

GridCompHeader.prototype.destroySelf = function() {
	this.comboData = null;
	if(this.langugeComboDatas!=null)this.langugeComboDatas = null;
	this.textNode = null;
	this.renderType = null;
	this.editorType = null;
	this.parent = null;
	this.topHeader = null;
	this.sumColRenderFunc = null;
	this.dataDiv = null;
	
	//this.cell.all[0]=null;
	//this.cell.all[1]=null;	
	this.cell=null;
	if (this.dataTable) {
		this.dataTable.headerOwner = null;
		this.dataTable = null;
	}
	this.owner = null;
};

/**
 * Grid行定义
 * 
 * @class GridComp行定义，这个row是对dataset row的适配，根据已经建立好的
 *        匹配规则，在执行期调用row.getCellValue(0)会根据匹配规则调用dataset相应行的getCell(i)来真正得到数据。
 * @version NC5.5
 * @since NC5.02
 */
GridCompRow = Array;

/**
 * 设置此行实际的数据
 * 
 * @param row
 *            一维数组或者dsRow
 */
GridCompRow.prototype.setRowData = function(row) {
	this.rowData = row;
};

/**
 * 得到此行实际的数据
 * 
 * @return 一维数组或者dsRow
 */
GridCompRow.prototype.getRowData = function() {
	return this.rowData;
};

/**
 * 设定GridCompRow和DsRow列的对应关系
 */
GridCompRow.prototype.setBindRelation = function(relation) {
	this.relation = relation;
};

/**
 * 根据字段名返回value
 * @param {} name
 */
GridCompRow.prototype.getCellValueByFieldName = function(name){
	var dataset = this.rowData.dataset;
	var dsIndex = dataset.nameToIndex(name);
	return this.rowData.getCellValue(dsIndex);
};

/**
 *根据字段名返回row中对应的列index 
 * @param {} name
 */
GridCompRow.prototype.getColIndexByFieldName = function(name){
	var dataset = this.rowData.dataset;
	var dsIndex = dataset.nameToIndex(name);
	for (var i = 0; i< this.relation.length ; i++){
		if (this.relation[i] == dsIndex)
		return i;
	}
	return -1;
};

/**
 *根据列index取出字段名 
 * @param {} name
 */
GridCompRow.prototype.getFiledNameByColIndex = function(colIndex){
	var dsColIndex = this.relation[colIndex];
	if (typeof(dsColIndex) == "undefined" || dsColIndex == -1)
		return null;
	var dataset = this.rowData.dataset;
	if (dataset.metadata[dsColIndex])
		return dataset.metadata[dsColIndex].key;
	else
		return null;
};



/**
 * 根据指定的grid中的index返回dataset中实际列位置的值
 */
GridCompRow.prototype.getCellValue = function(index) {
	// dataset数据(根据帮定关系返回真实数据)
	if (this.relation != null) {
		if (index >= this.rowData.length || index < 0)
			showErrorDialog("Index out of bounds exception!");
		else {
			// 得到dataset中实际的列号
			var relPosi = this.relation[index];
			return this.rowData.getCellValue(relPosi);
		}
	}
};

/**
 * 根据索引设置row中相应列的值
 * 
 * @param index
 *            列号
 */
GridCompRow.prototype.setCellValue = function(index, value) {
	// dataset数据(根据帮定关系返回真实数据)
	if (this.relation != null) {
		if (index >= this.rowData.length || index < 0)
			showErrorDialog("Index out of bounds exception!");
		else {
			// 得到实际的dataSet列号
			var relPosi = this.relation[index];
			this.rowData.setCellValue(relPosi, value);
		}
	}
};


/*******************************************************************************
 * 
 * form展示相关方法
 * 
 ******************************************************************************/
/**
 * paint所有重新设定的model数据（form展示）
 * 
 * @private
 */
GridComp.prototype.paintFormData = function() {  
	// 清空原来数据显示页
	this.clearDivs();
	if (this.model == null)
		return;
	// 初始化构建框架所需的各个常量
	this.initConstant();
	// 初始化基础框架
	this.initFormDivs();
	// 真正的画界面
	this.paintFormZone();
	// 各区域的事件处理
	this.attachFormEvents();
	if (this.showComp)
		this.hiddenComp();
};


/**
 * 初始化grid中的各个框架结构（form展示）
 * 
 * @private
 */
GridComp.prototype.initFormDivs = function() {
	this.constant.fixedHeaderDivWidth = GridComp.SELECTCOLUM_WIDTH; 
	// 创建表格行状态显示列header    
	this.initLineStateHeaderDiv();
	// 创建提示信息区
	if(this.isRunMode){
		this.initNoRowsDiv();
	}
	// 创建固定列整体div
	this.initFixedColumDiv();
	// 创建数据区整体div
	this.initFormOuterDiv();
	if (this.pageSize > 0){
		if(this.isSimplePagination){
			this.initSimplePaginationBar();
		}else{
			this.initPaginationBar();			
		}
	}
};


/**
 * 创建数据区整体div（form展示）
 * 
 * @private
 */
GridComp.prototype.initFormOuterDiv = function() {
	this.formOuterDiv = $ce("div");
	this.outerDiv.appendChild(this.formOuterDiv);
	// this.formOuterDiv.className = "data_outer_div";
	this.formOuterDiv.style.zIndex = 101;
	this.formOuterDiv.style.width = this.constant.outerDivWidth ;

	this.formOuterDiv.style.left = "0px";
	if  (this.canCopy == false){
		document.body.onselectstart = function(e){
			return false;
		};
		document.body.ondragstart = function(e){
			return false;
		};
		this.formOuterDiv.style.MozUserSelect = "none";
	}
};

/**
 * 画区域（form展示）
 * 
 * @private
 */
GridComp.prototype.paintFormZone = function(key, rowIndex, hasParent, level) {
	if (this.model == null)
		return;
	if (this.needPaintRows != null && this.needPaintRows == true) {
		this.needPaintRows = false;
		this.paintFormRows();
	} 
	else {
		if(rowIndex == null)
			rowIndex = 0;
		// 对每行进行处理
		var rows = this.model.getRows(key);
		if (rows == null || rows.length == 0)
			return;
		var modelLen = rows.length;
		var scrollLeft = this.formOuterDiv.scrollLeft;
		var rowHeihgt = this.rowHeight;
		var rowCount = this.model.getRowsCount();
		initLayoutMonitorState();
		this.cellForms = new Array();
		for (var i = 0; i < modelLen; i++) {
			if(hasParent){
				if(level != null){
					rows[i].level = level + 1;
					this.addFormOneRow(rows[i], rowIndex + i, scrollLeft, rowHeihgt, rowCount, rowIndex - 1);
				}
			}
			else
				this.addFormOneRow(rows[i], rowIndex + i, scrollLeft, rowHeihgt, rowCount, null);
		}
		this.clearHeadersOffsetWidth();
		setTimeout("executeLayoutMonitor()",500);
	}
};


/**
 * model改变后调用此方法重画所有行数据（form展示）
 * 
 * @param sort
 *            表示是排序时重画，不需要重新initUIRows
 * @param
 * @private
 */
GridComp.prototype.paintFormRows = function(sort, startIndex, count) {
	this.constant.outerDivWidth = this.wholeDiv.offsetWidth;
	var fixedColumDivWidth = 0;
	this.formOuterDiv.style.width = this.constant.outerDivWidth ;
	// 将model的rows置为null,需要重新取新数据
	if (!sort) {
		this.model.rows = null; 
		if (startIndex != null && count != null)
			this.model.initUIRows(startIndex, count);
		else
			this.model.initUIRows(startIndex);
	}
	var rowCount = this.getRowsNum();
	// 当前选中行索引
	this.selectedRowIndice = null;
	this.currActivedCell = null;
	// 重画行时隐藏掉当前显示的控件
	if (this.showComp)
		this.hiddenComp();
	var gridWidth = this.constant.outerDivWidth;
	// 画数据区外层div及每行数据div
	this.initFormColumDataDiv();
	this.paintFormZone();

};





/**
 * 画数据区外层div(form展示)
 * 
 * @private
 */
GridComp.prototype.initFormColumDataDiv = function() {
	var rowsNum = this.getRowsNum();
	if(this.noRowsDiv){
		if(rowsNum <= 0){
			this.needShowNoRowsDiv = true;
			if(this.model.dataset.lazyLoad == true){// 懒加载无数据提示晚于loading动画
				setTimeout("GridComp.showNoRowsDiv('" + this.id + "');",1500);
			}else{
				setTimeout("GridComp.showNoRowsDiv('" + this.id + "');",500);
			}
		}else{
			this.needShowNoRowsDiv = false;
			this.noRowsDiv.style.display = "none";
		}
	}  
	this.formColumDataDiv = $ce("div");
	if (this.formOuterDiv.childNodes[0] != null){
		this.removeAllChildren(this.formOuterDiv);
	}
	this.formOuterDiv.appendChild(this.formColumDataDiv);
	// this.formColumDataDiv.className = "dynamic_data_div";
	this.formColumDataDiv.id = "formColumDataDiv";
	
	// 保存数据区真正的宽度值,动态表格区真正的宽度值
	var formDataDivRealWidth = this.formOuterDiv.offsetWidth;
	this.realWidth = formDataDivRealWidth ;
	this.formColumDataDiv.style.width = formDataDivRealWidth + "px";// 2  左右DIV border边框宽度和
	if(rowsNum > 0){
		this.formColumDataDiv.style.marginBottom = "17px";
		this.formOuterDiv.style.overflow = "auto";
		this.formOuterDiv.style.display = "block";
	}else{
		this.formColumDataDiv.style.marginBottom = "0px";  
		this.formOuterDiv.style.overflow = "hidden";
		this.formOuterDiv.style.display = "none";
	}
	if (isDivVScroll(this.formOuterDiv)) {
		this.setScrollTop(0);
	}
};


/**
 * 给表格增加一行(Form展示)
 * 
 * @param row(model数据中的一行)
 * @param index
 *            数据行号
 * @param scrollleft
 * 
 * @param rowHeight
 *            行高
 * @param rowCount
 *            数据行数
 * @param parentRowIndex
 * 
 * @private
 */
GridComp.prototype.addFormOneRow = function(row, index, scrollLeft, rowHeight, rowCount, parentRowIndex) {
	if(this.noRowsDiv){
		this.noRowsDiv.style.display = "none";
		this.formColumDataDiv.style.marginBottom = "17px";
		this.formOuterDiv.style.overflow = "auto";
		this.formOuterDiv.style.display = "block";
	}
	var isOdd = this.isOdd(index);
	
	// 一行只有一个cell
	var cell = $ce("div");
	var rowCells = new Array();
	rowCells.push(cell);  
	
	cell.rowIndex = index;
	if(row.hasChildren && row.hasChildren != null)
			cell.hasChildren = row.hasChildren;
	var cellStyle = cell.style;
		cellStyle.width = "100%";
		cell.className = isOdd ? "gridformcell_odd" : "gridformcell_even";
	if(this.hasBorder)
		$(cell).addClass("cellExtendCss");
	this.formColumDataDiv.appendChild(cell);  
	// 一行创建一个form
	var formCompId = 'grid_cell_form' + index;
	var formAttr = {'eleWidth':'250','labelMinWidth':'0','formRender':null,'ellipsis':false};
	var cellForm = new AutoFormComp(cell,formCompId,2,false,22,1,formAttr);
	this.cellForms.push(cellForm);
	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		if (header.isHidden == true)
			continue;
		// 创建form组件
		var eleId = header.keyName;
		var field = header.keyName;
		var eleWidth = header.width;
		var height = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
		var rowSpan = 1;
		var colSpan = 1;
		var type = header.editorType;
		var userObject = {};
		var disabled = !header.columEditable;  
		var readOnly = !header.columEditable;
		// 参照元素需要提前将Dataset传入
		var dataset = null;
		if (type == "Reference"){
			dataset = this.model.dataset.id;
		}
		var labelName = header.showName;
		var labelColor = header.textColor;
		var nextLine = false;
		var required = header.required;
		var tip = null;
		var inputAssistant = null;
		var showTip = null;
		var description = null;
		var isAttachNext = false;
		var className = null;
		
		var cellFormEle = cellForm.createElement(eleId,field,eleWidth,height,rowSpan,colSpan,type,userObject,disabled,readOnly,dataset,labelName,labelColor,nextLine,required,tip,inputAssistant,showTip,description,isAttachNext,className);
	}
	cellForm.setDataset(this.model.dataset,index);
	if (IS_IE) {
		try{
			cellForm.pLayout.paint(true);
		}
		catch(e){
		}
	}
	// 行渲染
	this.rowRender.render.call(this, row, rowCells);
};



/**
 * model改变后会调用此方法通知grid(form展示)
 * 
 * @param rowIndex
 *            cell所在行号
 * @param colIndex
 *            cell所在列号
 * @param newValue
 *            新值
 * @param oldValue
 *            旧值
 * @private
 */
GridComp.prototype.formCellValueChangedFunc = function(rowIndex, colIndex, newValue, oldValue) {
	if (this.model.basicHeaders[colIndex].isHidden == false) {
		var a = this.cellForms[rowIndex];
		var b = a.getElementByIndex(colIndex);
		b.setValue(newValue);
	}
	// cell值改变后通知用户
	this.onCellValueChanged(rowIndex, colIndex, oldValue, newValue);
}; 

 
/**
 * model中行删除时的回调方法
 * 
 * @private
 */
GridComp.prototype.formFireRowDeleted = function(indice) {
	// 如果outerDiv没有显示出来，不进行处理
	if (this.outerDiv.offsetWidth == 0) {
		this.needPaintRows = true;
		return;
	};
	var gridWidth = this.constant.outerDivWidth;
	for (var i = 0, count = indice.length; i < count; i++) {
		// 若显示控件在当前选中行则隐藏当前控件
		if (this.currActivedCell != null
				&& this.getCellRowIndex(this.currActivedCell) == indice[i])
			this.hiddenComp();
		// 删除整个选中行
		this.deleteFormRows([indice[i]]);
	}
	this.oldCell = null;
};


/**
 * 删除指定行(form展示)
 * 
 * @param indice
 *            指定的索引值数组
 * @private
 */
GridComp.prototype.deleteFormRows = function(indice) {
	initLayoutMonitorState();
	if (indice == null || indice.length <= 0)
		return;
	// 要删除的行数
	var deleCount = indice.length;
	// 将数组的值按升序排列
	indice.sort(ascendRule_int);
	var len = this.basicHeaders.length;
	// 删除行状态列,deleCount个numdiv
	for (var i = 0; i < deleCount; i++) {
		var index = indice[i];
		this.formColumDataDiv.removeChild(this.formColumDataDiv.childNodes[index]);
	}
};


/**
 * Model中插入行时触发此方法.
 * 
 * @param index
 *            插入行的位置
 * @private
 */ 
GridComp.prototype.formFireRowInserted = function(index, level, parentRowIndex) {
	var gridHeight = this.constant.outerDivHeight;
	// 可见区域高度
	var areaHeight = 0;
	if (this.scroll)
		areaHeight = gridHeight - this.constant.headerHeight
				- GridComp.SCROLLBAE_HEIGHT;
	else
		areaHeight = gridHeight - this.constant.headerHeight;

	if (areaHeight < 0)
		areaHeight = 0;

	// 每一个header.dataDiv的高度增加一个行高
	var basicHeaders = this.basicHeaders;

	var row = this.model.getRow(index);
	if (level != null)
		row.level = level + 1;
	this.setHeadersOffsetWidth();
	initLayoutMonitorState();
	this.addFormOneRow(row, index,
					this.formOuterDiv.scrollLeft, 
					this.rowHeight, this.model.getRowsCount(), parentRowIndex);
	this.clearHeadersOffsetWidth();
	if(this.scrollState){
		$("#"+this.outerDivId).perfectScrollbar('updateAllandLeft',this.outerDivId);
	}
	executeLayoutMonitor();
};



/**
 * 数据区的事件处理(form展示)
 * 
 * @private
 */
GridComp.prototype.attachFormEvents = function() {
	var oThis = this;
	// 监测整体数据区的点击事件
	this.formOuterDiv.onclick = function(e) {
		e = EventUtil.getEvent();
		// 点击执行方法
		oThis.formClick(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	// 增加统一的resize事件处理必须给对象一个id
	this.outerDiv.id = oThis.id + "_outerdiv";
	// 给outerDiv增加onresize方法
	addResizeEvent(this.outerDiv, function() {
		GridComp.gridResize(oThis.id);
	});
	// 弹出窗口中包含grid，在编辑控件显示时，滚动滚动条（body第一个子元素的滚动条），隐藏编辑控件。
	if(document.body.children[0]){
		if(!document.body.children[0].gridMap){
			document.body.children[0].gridMap = new HashMap();
		}
		document.body.children[0].gridMap.put(this.id, this);
		document.body.children[0].onscroll = function(e){
			// 滚动时隐藏掉当前显示的控件
			var grids = this.gridMap.values();
			for(var i=0; i<grids.length; i++){
				if (grids[i].showComp != null) {
					if (grids[i].autoScroll != true)
						grids[i].hiddenComp();
					else
						grids[i].autoScroll = false;
				}
			}
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
	}
};



/**
 * 数据区点击后执行方法(form展示)
 * 
 * @private
 */
GridComp.prototype.formClick = function(e) {
	document.onclick(e);
	// 隐藏已经显示出来的设置按钮
	this.hideenColumnContentMenu();
	// grid整体禁用直接返回
	if (this.isGridActive == false) return;
	// 获得一行的整体div 
	var cell = this.getRealFormCell(e);
	var rowIndex = this.getCellRowIndex(cell);
	// 只处理点击动态数据区和静态数据区cell的情况,点击数据区的其他地方不处理
	// if (columDiv == null || (columDiv.parentNode != null && columDiv.parentNode.id != "formColumDataDiv")) return;
	// 首先隐藏掉上一个显示出的控件
	if (this.showComp != null) 
		this.hiddenComp();
	this.setFormFocusIndex(rowIndex);
	
	this.processCtrlSel(false, rowIndex);
};


/**
 * 获得form中一行的整体div
 * 
 * @private
 */
GridComp.prototype.getRealFormCell = function(e) {
	var cell = getTarget(e);
	var pNode = cell;
	while (pNode != null) {
		if(pNode.parentNode.id == "formColumDataDiv"){
			cell=pNode;
			break;
		}
		pNode = pNode.parentNode;
	}
	return cell;
};



/**
 * 设置当前聚焦行（form展示）
 * 
 * @private
 */
GridComp.prototype.setFormFocusIndex = function(rowIndex) {
	if(typeof(rowIndex) == "number" && rowIndex >= 0){
		var oldFocusRowIndex = this.getFocusIndex();
		//设置新焦点行在数据集中的真实索引,真实索引由GridCompModel负责转换.
		this.model.setFocusIndex(rowIndex);
		var headers = this.basicHeaders;
		//设置旧焦点行恢复失去焦点时样式,设置新焦点行获取焦点时样式.
		if(typeof(oldFocusRowIndex) == "number" && oldFocusRowIndex >= 0 && rowIndex != oldFocusRowIndex){//旧焦点行恢复失去焦点时样式
			var oldFocusCell = this.formColumDataDiv.childNodes[oldFocusRowIndex];
			if(typeof(oldFocusCell) == "object"){
				if(typeof(oldFocusCell.className) == "string" && oldFocusCell.className.indexOf("cell_focus") != -1){
					oldFocusCell.className = oldFocusCell.className.replace(" cell_focus", "");
				}
				oldFocusCell.isFocusRow = false;
			}
		}
		var focusCell = this.formColumDataDiv.childNodes[rowIndex];
		if(typeof(focusCell) == "object"){//新焦点行获取焦点时样式
			if(typeof(focusCell.className) == "string"){
				if(focusCell.className.indexOf("cell_focus") == -1){
					focusCell.className += " cell_focus";
				}
			}else{
				focusCell.className = " cell_focus";
				if(this.hasBorder)
					$(focusCell).addClass("cellExtendCss");
			}
			focusCell.isFocusRow = true;
		}
		this.focusIndex = rowIndex;
	}
};

/**
 * 设置參照編輯的viewURL
 * 
 * @private
 */
//GridComp.prototype.setViewURL = function(colIndex,viewURL) {
//	if (this.compsMap != null) {
//		var comp = this.compsMap.get(EditorType.REFERENCE + colIndex);
//		if (comp != null) {
//			if(comp.setViewURL){
//				comp.setViewURL(viewURL);
//			}
//		}
//	}
//};

/**
 * 获取真正显示的所在列
 * 
 * @private
 */
GridComp.prototype.getShowColIndex = function(colIndex) {
	var showColIndex = -1;
	for(var i = 0;i < this.basicHeaders.length;i++){
		if(i <= colIndex){
			if(!this.basicHeaders[i].isHidden){
				showColIndex++;
			}
		}
	}
	return showColIndex;
};
