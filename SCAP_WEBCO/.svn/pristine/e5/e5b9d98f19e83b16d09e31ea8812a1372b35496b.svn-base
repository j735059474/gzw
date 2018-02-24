<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="首页新闻管理卡片" controllerClazz="nc.scap.pub.news.NewsCardWinCtrl" id="news_cardwin" sourcePackage="pub/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_simpleexetask" refId="../uap.lfw.wfm.simpleapprove.pubview_simpleexetask">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_approveeexetask" refId="../uap.lfw.wfm.approve.pubview_approveeexetask">
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
	<Connectors>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="nc.scap.pub.news.uiComps.news_cardwin" targetWindow="">
        </Connector>
        <Connector connType="2" id="simplepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_simpleexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="approvepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_approveeexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>