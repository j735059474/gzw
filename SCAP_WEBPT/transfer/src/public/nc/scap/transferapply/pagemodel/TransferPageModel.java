package nc.scap.transferapply.pagemodel;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import uap.web.bd.pub.AppUtil;

public class TransferPageModel extends PageModel{

	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}
	
	@Override
	protected void initPageMetaStruct() {
		// TODO �Զ����ɵķ������
		super.initPageMetaStruct();
		initUI();
	}
	
	private void initUI(){
	    String node_type = CpPubMethod.getWebParter(IGlobalConstants.NODE_TYPE);//�ڵ�����жϵ�����ĸ��ڵ�
	    String method_type = CpPubMethod.getWebParter(IGlobalConstants.METHOD_TYPE);//�ڵ�����жϵ��������ڵ���ĸ�����������ʱ��Ҫ����url��
	    if (node_type != null)
	      AppUtil.addAppAttr(IGlobalConstants.NODE_TYPE, node_type);
	    else {
	      node_type = (String)AppUtil.getAppAttr(IGlobalConstants.NODE_TYPE);
	    }
	    
	    if (method_type != null)
	        AppUtil.addAppAttr(IGlobalConstants.METHOD_TYPE, method_type);
	      else {
	    	  method_type = (String)AppUtil.getAppAttr(IGlobalConstants.METHOD_TYPE);
	    }
		LfwView main = getPageMeta().getView("main");
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		String pk_org = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_org();
		if(method_type == null || method_type.equals("")){
			//����
			MenubarComp pubbar = main.getViewMenus().getMenuBar("menubar");
			initBtn(pubbar,IGlobalConstants.BTN_ADD);
		}else if(method_type != null && method_type.equals("look")){
			//�鿴
			MenubarComp pubbar = main.getViewMenus().getMenuBar("menubar");
			initBtn(pubbar,IGlobalConstants.BTN_EDIT);
		}else if(method_type != null && method_type.equals("approve")){
			//����
			MenubarComp pubbar = main.getViewMenus().getMenuBar("menubar");
			initBtn(pubbar,IGlobalConstants.BTN_APPROVE);
		}

	}
	
	private void initBtn(MenubarComp pubbar,String str){
		
		if(str == null)
			return;
		if(str.equals(IGlobalConstants.BTN_ADD)){
			pubbar.getItem("attachfile").setVisible(false);
			pubbar.getItem("print").setVisible(false);
//			pubbar.getItem("approvetrs").setVisible(false);
			
		}else if(str.equals(IGlobalConstants.BTN_EDIT)){
			pubbar.getItem("add").setVisible(false);
			pubbar.getItem("edit").setVisible(false);
			pubbar.getItem("del").setVisible(false);
			pubbar.getItem("attachfile").setVisible(false);
			pubbar.getItem("print").setVisible(false);
			pubbar.getItem("approvetrs").setVisible(false);
			
		}else if(str.equals(IGlobalConstants.BTN_APPROVE)){
			pubbar.getItem("add").setVisible(false);
			pubbar.getItem("edit").setVisible(false);
			pubbar.getItem("del").setVisible(false);
			pubbar.getItem("attachfile").setVisible(false);
			pubbar.getItem("print").setVisible(false);
		}
		
	}
}
