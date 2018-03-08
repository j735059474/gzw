function OperateRenderPT() {}
OperateRenderPT.render = function(rowIndex, colIndex, value, header, cell, dsIndex) {

	var grid = this;
	var ds = grid.model.dataset;
	var aEdit = $ce("A");  
	var gridRow = grid.model.getRow(rowIndex);
	var realIndex = ds.getRowIndex(gridRow.rowData);
	aEdit.appendChild(document.createTextNode(value));
	aEdit.style.left = "10px";
	aEdit.style.position="absolute";
	aEdit.href = "#";

	aEdit.rowIndex = realIndex;
	
	aEdit.onclick = function(e) {
		this.style.color = '#363636';
		//var gridRow = grid.model.getRow(rowIndex);
		//var realIndex = ds.getRowIndex(gridRow.rowData);
		ds.setRowSelected(this.rowIndex);
		var proxy = new ServerProxy(null, null, false);
		var submitRule = new SubmitRule();
		var wdr = new WidgetRule('main');
		var dsRule = new DatasetRule("scappt_apply_h", 'ds_all_line');
		wdr.addDsRule("scappt_apply_h", dsRule);
		submitRule.addWidgetRule('main', wdr);
		proxy.addParam('clc','nc.scap.transfer.apply.ApplyListWinMainViewCtrl');
		proxy.addParam('m_n', 'onDetail');
		proxy.addParam('widget_id', 'main');
		
		proxy.setSubmitRule(submitRule);
		showDefaultLoadingBar();
		proxy.execute();
		e = EventUtil.getEvent();
		stopAll(e);
	};
	

	cell.appendChild(aEdit);
};
