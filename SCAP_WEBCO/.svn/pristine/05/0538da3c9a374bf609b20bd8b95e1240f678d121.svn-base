<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="工作报告列表" componentId="workreportComp" controllerClazz="nc.scap.pub.workreport.WorkreportListWinCtrl" foldPath="/sync/scapco/pub/html/nodes/workreportComp/workreport_listwin/" id="workreport_listwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="simplequery" refId="../uap.lfw.imp.query.pubview_simplequery">
            <Attributes>
                <Attribute>
                    <Key>auto_query</Key>
                    <Value>true</Value>
                    <Desc></Desc>
                </Attribute>
            </Attributes>
        </Widget>
        <Widget canFreeDesign="false" id="modeorg" refId="../uap.lfw.bd.org.pubview_modeConfigOrg">
            <Attributes>
                <Attribute>
                    <Key>auto_load</Key>
                    <Value>false</Value>
                    <Desc></Desc>
                </Attribute>
            </Attributes>
        </Widget>
        <Widget canFreeDesign="true" id="workreport_orgtree" refId="workreport_orgtree">
        </Widget>
    </Widgets>
    <PlugoutDescs>
    </PlugoutDescs>
    <PluginDescs>
        <PluginProxy delegatedViewId="main" id="refreshProxy">
        </PluginProxy>
    </PluginDescs>
    <Connectors>
        <Connector connType="8" id="proxyin_refreshProxy_refresh_plugin" pluginId="refresh_plugin" plugoutId="refreshProxy" source="workreportComp.workreport_listwin" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="simpleQueryConnListView" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="modeOrgConnListView" pluginId="org_plugin" plugoutId="orgout" source="modeorg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="b9f41b58-c515-4021-b52e-42ac3f0558e5" pluginId="orgtree_plugin" plugoutId="orgtreeout" source="workreport_orgtree" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>