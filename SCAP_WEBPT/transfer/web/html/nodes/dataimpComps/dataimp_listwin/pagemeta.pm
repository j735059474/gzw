<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="省交易数据导入列表" componentId="dataimpComps" controllerClazz="nc.scap.transfer.dataimp.DataimpListWinCtrl" foldPath="/sync/SCAP_WEBPT/transfer/html/nodes/dataimpComps/dataimp_listwin/" id="dataimp_listwin" sourcePackage="transfer/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="simplequery" refId="../uap.lfw.imp.query.pubview_simplequery">
        </Widget>
        <Widget canFreeDesign="false" id="modeorg" refId="../uap.lfw.bd.org.pubview_modeConfigOrg">
        </Widget>
        <Widget canFreeDesign="true" id="merge" refId="merge">
        </Widget>
        <Widget canFreeDesign="true" id="selecttrs" refId="selecttrs">
        </Widget>
    </Widgets>
    <PlugoutDescs>
    </PlugoutDescs>
    <PluginDescs>
        <PluginProxy delegatedViewId="main" id="refreshProxy">
        </PluginProxy>
    </PluginDescs>
    <Connectors>
        <Connector connType="2" id="modeOrgConnListView" pluginId="org_plugin" plugoutId="orgout" source="modeorg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="8" id="proxyin_refreshProxy_refresh_plugin" pluginId="refresh_plugin" plugoutId="refreshProxy" source="dataimpComps.dataimp_listwin" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="simpleQueryConnListView" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>