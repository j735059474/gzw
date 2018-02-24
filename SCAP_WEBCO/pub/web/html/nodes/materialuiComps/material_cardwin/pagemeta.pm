<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="资料维护卡片" componentId="materialuiComps" controllerClazz="nc.scap.dsm.material.MaterialCardWinCtrl" foldPath="/sync/scapco/dsm/html/nodes/materialuiComps/material_cardwin/" id="material_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_simpleexetask" refId="../uap.lfw.wfm.simpleapprove.pubview_simpleexetask">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_approveeexetask" refId="../uap.lfw.wfm.approve.pubview_approveeexetask">
        </Widget>
        <Widget canFreeDesign="true" id="attachlist" refId="../com.scap.pub.fileComps.attachlist">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>CARD_WINDOW</Key>
            <Value>true</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PlugoutDescs>
        <PlugoutProxy delegatedViewId="main" id="proxyAfterSavePlugout">
        </PlugoutProxy>
    </PlugoutDescs>
    <PluginDescs>
    </PluginDescs>
    <Connectors>
        <Connector connType="2" id="simplepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_simpleexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="materialuiComps.material_cardwin" targetWindow="">
        </Connector>
        <Connector connType="2" id="approvepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_approveeexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>