<?xml version="1.0" encoding='UTF-8'?>
<PageMeta i18nName="" caption="短信管理列表"  controllerClazz="nc.scap.pub.sms.comps.SmsManageListWinCtrl"  id="smsManage_listwin" sourcePackage="pub/src/public/" windowType="win">
     <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="simplequery" refId="../uap.lfw.imp.query.pubview_simplequery">
        </Widget>
    </Widgets>
    <PlugoutDescs>
    </PlugoutDescs>
    <PluginDescs>
        <PluginProxy delegatedViewId="main" id="refreshProxy">
        </PluginProxy>
    </PluginDescs>
    <Connectors>
        <Connector connType="8" id="proxyin_refreshProxy_refresh_plugin" pluginId="refresh_plugin" plugoutId="refreshProxy" source="smsManageComps.smsManage_listwin" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="simpleQueryConnListView" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" sourceWindow="" target="main" targetWindow="">
        </Connector>       
    </Connectors>
</PageMeta>