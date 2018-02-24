/**  
*     @desc: 设置节点以及子节点选中
*     @author: dingrf
*     @type: private
*     @param: state - 状??true/false
*     @param: treeNode - 目标节点
*     @param: isRoot - 是否为根，为根时，不管哪种复选策略，都全??
*/
TreeNode.prototype._setSubChecked = function(state,isRoot){
	//点根节点时全??
	if (this.treeViewComp.checkBoxModel != TreeViewComp.CHECKBOXMODEL_SELF || isRoot || this.treeViewComp.checkBoxModel == TreeViewComp.CHECKBOXMODEL_SELF && state == false){
		if (this.open == false){
			this.toggle();
		}
		if(this.childrenTreeNodes && this.childrenTreeNodes.length > 0){
			if (this.childrenTreeNodes[this.childrenTreeNodes.length-1] != null && this.childrenTreeNodes[this.childrenTreeNodes.length-1].id.endWith(TreeViewComp.RELOADNODE_suffix)){
//				TreeViewComp.SUBCHECK_MAP.put(this.id, this);
				this._state = state;
				this._isRoot = isRoot;
				this.prapareCheck = true;
//				setTimeout("TreeNode._setSubChecked('"+ this.id + "', "+ state + ", "+ isRoot +")", 1000);
				return;
			}
			else{
				for (var i=0; i<this.childrenTreeNodes.length; i++){
					if(this.childrenTreeNodes[i]){
						this.childrenTreeNodes[i]._setSubChecked(state,isRoot);
					}
				}
			}
		}
	}
//	if (state) this._setCheck(treeNode,1);
//	else this._setCheck(treeNode,0);
	this.setChecked(state);
};


/**
*     @desc: 修正父节点的复选框状??
*     @author: dingrf
*     @type: private
*     @param: treeNode - 目标节点
*/
TreeNode.prototype._correctCheckStates=function(){
	if (this.root) return;
	debugger;
	//只设置自己时,或者设置自己和子节点时,不做修正
	if (this.treeViewComp.checkBoxModel == TreeViewComp.CHECKBOXMODEL_SELF_SUB) return;
		
	var parentNode = this.parentTreeNode;
	if (!parentNode) return;
   
	//记录是否存在状态为完全非选中的子
	var flag1=0; 
	//记录是否存在状态为完全选中的子
	var flag2=0;
	for (var i=0; i<parentNode.childrenTreeNodes.length; i++){
		var node = parentNode.childrenTreeNodes[i];
		if (node.checkstate == null) return;
		if (node.checkstate == 0) flag1=1;
      	else if (node.checkstate==1) flag2=1;
        else { flag1=1; flag2=1; break; }
	}

	if ((flag1)&&(flag2)){
		parentNode.setChecked(true);
		parentNode._setCheck(2);
	}
	else if (flag1){
		parentNode.setChecked(false);
		parentNode._setCheck(0);
	}  
	else{
		parentNode.setChecked(true);
		parentNode._setCheck(1);
	}  

	parentNode._correctCheckStates();
};
