<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="资料类型权限列表" componentId="dtpuiComps" controllerClazz="nc.scap.dsm.dtp.ctrl.DtpListWinCtrl" foldPath="/sync/scapco/dsm/html/nodes/dtpuiComps/dtp_listwin/" id="dtp_listwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="simplequery" refId="../uap.lfw.imp.query.pubview_simplequery">
        </Widget>
        <Widget canFreeDesign="false" id="modeorg" refId="../uap.lfw.bd.org.pubview_modeConfigOrg">
        </Widget>
      
    </Widgets>
    <PlugoutDescs>
    </PlugoutDescs>
    <PluginDescs>
        <PluginProxy delegatedViewId="main" id="refreshProxy">
        </PluginProxy>
    </PluginDescs>
    <Events>
        <Event async="false" controllerClazz="nc.uap.lfw.core.app.LfwAppDefaultController" methodName="onPageClosed" name="onClosed" onserver="true">
            <Action>
            </Action>
        </Event>
    </Events>
    <Connectors>
        <Connector connType="8" id="proxyin_refreshProxy_refresh_plugin" pluginId="refresh_plugin" plugoutId="refreshProxy" source="dtpuiComps.dtp_listwin" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="045afcf5-2b20-430e-ad40-3864c26229cb" pluginId="selectDataType_plugin" plugoutId="selectRowAfterPlugout" source="selectDateTypeTree" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="simpleQueryConnListView" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="modeOrgConnListView" pluginId="org_plugin" plugoutId="orgout" source="modeorg" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>