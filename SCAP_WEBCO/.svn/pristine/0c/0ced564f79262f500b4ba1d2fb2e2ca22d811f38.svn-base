<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="常用联系人卡片" componentId="nc.scap.pub.contacts" controllerClazz="nc.scap.pub.contacts.ContactsCardWinCtrl" foldPath="/sync/scapco/pub/html/nodes/nc/scap/pub/contacts/contacts_cardwin/" id="contacts_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="true" id="pubview_selectUserbyOrg" refId="../selectUserComps.pubview_selectUserbyOrg">
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
        <Connector connType="2" id="be384459-6b57-483c-9597-44c4994c7255" pluginId="SelectUserAfterPlugin" plugoutId="afterOkplugout" source="pubview_selectUserbyOrg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="18865992-f0e9-459b-9399-82142aaf3b5e" pluginId="" plugoutId="" source="" sourceWindow="" target="" targetWindow="">
        </Connector>
        <Connector connType="" id="2e6f5c9d-a26e-4005-a076-4c29559fd631" pluginId="SelectUserAfterPlugin" plugoutId="afterOkplugout" source="selectUserByVisualOrg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="nc.scap.pub.contacts.contacts_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>