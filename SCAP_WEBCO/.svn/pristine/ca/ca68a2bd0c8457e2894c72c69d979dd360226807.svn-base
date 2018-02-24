<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="统一通知下发卡片" componentId="nc.scap.pub.notice" controllerClazz="nc.scap.pub.notice_manager.Notice_managerCardWinCtrl" foldPath="/sync/scapco/pub/html/nodes/nc/scap/pub/notice/notice_manager_cardwin/" id="notice_manager_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="true" id="attachlist" refId="../com.scap.pub.fileComps.attachlist">
        </Widget>
        <Widget canFreeDesign="true" id="visualOrganization" refId="../com.scap.pub.visualOrganization.visualOrganization">
        </Widget>
        <Widget canFreeDesign="true" id="multiUserSelect" refId="../nc.scap.pub.multiUserSelect.multiUserSelect">
        </Widget>
        <Widget canFreeDesign="true" id="selectUserByVisualOrg" refId="../nc.scap.pub.selectUserByVisualOrg.selectUserByVisualOrg">
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
        <Connector connType="" id="3641433f-2e13-46fa-85ad-4ddc961fd97c" pluginId="loadAttach" plugoutId="attachout" source="main" sourceWindow="" target="attachlist" targetWindow="">
        </Connector>
        <Connector connType="" id="93532572-4a82-4734-9845-1d229dfaf02f" pluginId="SelectCompAfterPlugin" plugoutId="afterOkplugout" source="visualOrganization" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="01bbeb0f-8026-4824-8175-1ec1fad757d7" pluginId="SelectUserAfterPlugin" plugoutId="afterOkplugout" source="multiUserSelect" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="2e6f5c9d-a26e-4005-a076-4c29559fd631" pluginId="SelectUserAfterPlugin" plugoutId="afterOkplugout" source="selectUserByVisualOrg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="nc.scap.pub.notice.notice_manager_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>