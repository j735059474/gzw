function tbcall(pk_lfwfile,fileName,size,fileType,createtime,userpk,username,billitem){
	debugger;
	var proxy = new ServerProxy(null,null,false); //代理类
	proxy.addParam('clc', 'com.scap.pub.attlist.AttachViewCtrl' );//控制类
	proxy.addParam('m_n', 'refshDataByScript');//方法名
	proxy.addParam('billitem', billitem);//方法名
	
	// 设置提交父数据集的提交规则
	var rule = new SubmitRule();
	var wdr_parentWidget = new WidgetRule('attachlist');	
	var dsr_signDataset = new DatasetRule('attach_ds', 'ds_all_line');
	wdr_parentWidget.addDsRule('attach_ds', dsr_signDataset);
	rule.addWidgetRule('attachlist', wdr_parentWidget);
	proxy.setSubmitRule(rule);
	proxy.execute();
	
	return false;
};

function OperateRender() {}
 
OperateRender.render = function(rowIndex, colIndex, value, header, cell, dsIndex) {
		debugger;
		var oThis = this;
		var aEdit = $ce("A");  
		var Image = $ce("IMG");
		Image.src="/portal/a/bianji.gif";
		//aEdit.appendChild(document.createTextNode("查看"));
		aEdit.appendChild(Image);
		aEdit.style.paddingTop="3px";
		aEdit.style.left = "20px";
		aEdit.style.position="absolute";
		aEdit.href = "#";
		aEdit.rowIndex = rowIndex;
		aEdit.onclick = function(e) {
			alert("编辑触发");
		};
		//删除
		var aDel = $ce("A");  
		var delImage = $ce("IMG");
		delImage.src="/portal/a/shanchu.gif";
		//aEdit.appendChild(document.createTextNode("查看"));
		aDel.appendChild(delImage);
		aDel.style.paddingTop="3px";
		aDel.style.left = "40px";
		aDel.style.position="absolute";
		aDel.href = "#";
		aDel.rowIndex = rowIndex;
		aDel.onclick = function(e) {
			//实现方式一 通过调用控制类的控制方法实现
			e = EventUtil.getEvent();
			var menubar = pageUI.getWidget('main').getMenu("menubar");
			//leaderinfo_grid$MenuBar
			var menuitem = menubar.getMenu('del');
			menuitem.onclick();
			/**
			var ds = window.pageUI.getWidget('main').getDataset('eduinfo');
			ds.setRowSelected(this.rowIndex);
			var proxy = new ServerProxy(null, null, false);
//			var submitRule = new SubmitRule();
//			var wdr = new WidgetRule('main');
//			var dsRule = new DatasetRule("eduinfo", 'ds_current_line');
//			wdr.addDsRule("cp_appstype", dsRule);
//			submitRule.addWidgetRule('main', wdr);
			proxy.addParam('clc','com.prod.comp.testApp.winApplist.view.ctrl.MainViewController');
			proxy.addParam('m_n', 'onDelete');
			proxy.addParam('widget_id', 'main');
			
//			proxy.setSubmitRule(submitRule);
			//showDefaultLoadingBar();
			proxy.execute();
			*/
			e = EventUtil.getEvent();
			stopAll(e);
		};
		
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		cell.style.textAlign = "center";
		cell.appendChild(aEdit);
		cell.appendChild(aDel);
};