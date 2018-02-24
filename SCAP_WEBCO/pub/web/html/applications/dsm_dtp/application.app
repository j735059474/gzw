<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="资料类型权限" controllerClazz="nc.scap.dsm.dtp.DtpAppController" defaultWindowId="dtpuiComps.dtp_listwin" id="dsm_dtp" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="资料类型权限列表" id="dtpuiComps.dtp_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="资料类型权限卡片" id="dtpuiComps.dtp_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="dsm_dtpconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="dtpuiComps.dtp_cardwin" sourceWindow="" target="dtpuiComps.dtp_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>