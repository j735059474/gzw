<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="公文管理卡片" componentId="nc.scap.pub.officialdoc" controllerClazz="nc.scap.pub.officialdoc.OfficialdocCardWinCtrl" foldPath="/sync/scapco/pub/html/nodes/nc/scap/pub/officialdoc/officialdoc_cardwin/" id="officialdoc_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="true" id="userChoice" refId="../nc.scap.pub.userChoice.userChoice">
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
        <Connector connType="" id="bbf6b6ce-bc06-4376-9677-964595b745e1" pluginId="userChoice" plugoutId="doOk" source="userChoice" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="nc.scap.pub.officialdoc.officialdoc_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>