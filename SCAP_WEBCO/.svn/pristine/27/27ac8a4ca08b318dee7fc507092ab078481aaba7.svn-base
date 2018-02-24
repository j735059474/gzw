<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="工作报告类型卡片" componentId="nc.scap.pub.notice" controllerClazz="nc.scap.pub.work_type.Work_typeCardWinCtrl" foldPath="/sync/scapco/pub/html/nodes/nc/scap/pub/notice/work_type_cardwin/" id="work_type_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="true" id="timeDefineInfo" refId="timeDefineInfo">
        </Widget>
        <Widget canFreeDesign="true" id="attachlist" refId="../com.scap.pub.fileComps.attachlist">
        </Widget>
        <Widget canFreeDesign="true" id="visualOrganization" refId="../com.scap.pub.visualOrganization.visualOrganization">
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
        <Connector connType="" id="d7aaaa40-119c-4a0c-8608-755b4ddfa16d" pluginId="SelectVisualOrgsAfterPlugin" plugoutId="afterOkplugout" source="visualOrganization" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="74f3fd2f-6926-4c72-ac9c-c2527aff616f" pluginId="loadAttach" plugoutId="attachout" source="main" sourceWindow="" target="attachlist" targetWindow="">
        </Connector>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="nc.scap.pub.notice.work_type_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>