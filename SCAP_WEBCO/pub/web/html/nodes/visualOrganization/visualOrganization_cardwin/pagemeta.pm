<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="组织维护卡片" componentId="visualOrganization" controllerClazz="nc.scap.pub.visualOrganization.VisualOrganizationCardWinCtrl" foldPath="/sync/scapco/pub/html/nodes/visualOrganization/visualOrganization_cardwin/" id="visualOrganization_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="true" id="pubview_selectUserbyOrg" refId="../selectUserComps.pubview_selectUserbyOrg">
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
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="visualOrganization.visualOrganization_cardwin" targetWindow="">
        </Connector>
        <Connector connType="" id="f34caf98-4014-429c-9628-43bcb0068e8e" pluginId="SelectUserAfterPlugin" plugoutId="afterOkplugout" source="pubview_selectUserbyOrg" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>