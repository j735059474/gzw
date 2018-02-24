<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="资料维护列表" componentId="materialuiComps" controllerClazz="nc.scap.dsm.material.MaterialListWinCtrl" foldPath="/sync/scapco/pub/html/nodes/materialuiComps/material_listwin/" id="material_listwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="simplequery" refId="../uap.lfw.imp.query.pubview_simplequery">
            <Attributes>
                <Attribute>
                    <Key>auto_query</Key>
                    <Value>true</Value>
                    <Desc>
                    </Desc>
                </Attribute>
            </Attributes>
        </Widget>
        <Widget canFreeDesign="false" id="modeorg" refId="../uap.lfw.bd.org.pubview_modeConfigOrg">
        </Widget>
        <Widget canFreeDesign="true" id="DatatypeTree" refId="DatatypeTree">
        </Widget>
        <Widget canFreeDesign="true" id="pubview_selectUserbyOrg" refId="../selectUserComps.pubview_selectUserbyOrg">
        </Widget>
        <Widget canFreeDesign="true" id="pubview_simpleexetask" refId="../uap.lfw.wfm.simpleapprove.pubview_simpleexetask">
        </Widget>
    </Widgets>
    <PlugoutDescs>
    </PlugoutDescs>
    <PluginDescs>
        <PluginProxy delegatedViewId="main" id="refreshProxy">
        </PluginProxy>
    </PluginDescs>
    <Connectors>
        <Connector connType="" id="03ba37e9-ca48-41c6-a4b2-adb682d8a418" pluginId="doSelectTreeNode" plugoutId="selectRowAfterPlugout" source="DatatypeTree" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="8" id="proxyin_refreshProxy_refresh_plugin" pluginId="refresh_plugin" plugoutId="refreshProxy" source="materialuiComps.material_listwin" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="simpleQueryConnListView" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="5f23914e-ee00-4dc9-b1fa-1b50fd3c722c" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_simpleexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="modeOrgConnListView" pluginId="org_plugin" plugoutId="orgout" source="modeorg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="f7cfe538-b947-4894-b526-5b526fbe6e0f" pluginId="addSelectUserAfterPlugin" plugoutId="afterOkplugout" source="pubview_selectUserbyOrg" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>