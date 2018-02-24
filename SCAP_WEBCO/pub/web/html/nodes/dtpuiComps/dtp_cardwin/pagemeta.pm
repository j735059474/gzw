<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE PageMeta PUBLIC "-//Yonyou Co., Ltd.//UAP LFW WINDOW 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_window_6_3.dtd">
<PageMeta caption="资料类型权限卡片" componentId="dtpuiComps" controllerClazz="nc.scap.dsm.dtp.ctrl.DtpCardWinCtrl" foldPath="/sync/scapco/dsm/html/nodes/dtpuiComps/dtp_cardwin/" id="dtp_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="true" id="pubview_selectUserbyOrg" refId="../selectUserComps.pubview_selectUserbyOrg">
        </Widget>
        <Widget canFreeDesign="true" id="role" refId="../uap.lfw.bd.user.role">
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
        <Connector connType="" id="4e047f08-aaf4-4e04-8df6-c2ffc316471b" pluginId="selectUserAfterPlugin" plugoutId="afterOkplugout" source="pubview_selectUserbyOrg" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="" id="104dd95a-9e60-48f9-a0be-058c4f35cfde" pluginId="selectRoleAfterplugin" plugoutId="role_plugout" source="role" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="dtpuiComps.dtp_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>