<?xml version="1.0" encoding='UTF-8'?>
<PageMeta controllerClazz="nc.uap.portal.login.PortalLoginPageListener" id="login" sourcePackage="pserver/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <PageStates>
    </PageStates>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
    </Widgets>
      <Events>
        <Event async="true" jsEventClaszz="nc.uap.lfw.core.event.conf.PageListener" methodName="onAfterPageInit" name="afterPageInit" onserver="false">
            <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
            </SubmitRule>
            <Params>
                <Param>
                    <Name>pageEvent</Name>
                    <Value></Value>
                    <Desc>                        <![CDATA[nc.uap.lfw.core.event.PageEvent]]>
                    </Desc>
                </Param>
            </Params>
           <Action>                        
           <![CDATA[
       			var p_userId=decodeURIComponent(getCookie("p_userId"));
       			var p_userPwd=decodeURIComponent(getCookie("p_userPwd"));
       			var compUserid = pageUI.getWidget("main").getComponent("userid");
       			var comppwd = pageUI.getWidget("main").getComponent("password");
       			if(IS_IPAD)return ;
       			var ulab = document.getElementById('usernamelab');
       			if(ulab!=null && 'undefined'!=typeof(ulab)){
       				ulab.innerHTML = '';
       			}
       			if(p_userId==null || p_userId=='null' || p_userId=='undefined' || p_userId=='developadmin'){
       				compUserid.input.select();
       				compUserid.input.focus();
       			}
       			else{
       				  compUserid.setValue(p_userId);
       				 if(p_userPwd==null ||p_userPwd=='null'){
       				 	}else{
       				 		document.getElementById('passwordlab').innerHTML = '';
       				 	 comppwd.setValue(p_userPwd);
       				 	}
       			   comppwd.input.select();
       			   comppwd.input.focus();
       			}
       			stopAll(pageEvent.event);
			]]>
           </Action>
        </Event>
    </Events>
    <Containers>
    </Containers>
</PageMeta>
