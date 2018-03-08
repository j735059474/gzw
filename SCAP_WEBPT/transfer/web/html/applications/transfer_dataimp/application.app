<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="省交易数据导入" controllerClazz="nc.scap.transfer.dataimp.DataimpAppController" defaultWindowId="dataimpComps.dataimp_listwin" id="transfer_dataimp" sourcePackage="transfer/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="省交易数据导入列表" id="dataimpComps.dataimp_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="省交易数据导入卡片" id="dataimpComps.dataimp_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权交易信息" id="transferComps.trs_look_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="transfer_dataimpconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="dataimpComps.dataimp_cardwin" sourceWindow="" target="dataimpComps.dataimp_listwin" targetWindow="">
        </Connector>
         <Connector connType="1" id="trs_look_cardwin" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_gpzr_cardwin" sourceWindow="" target="dataimpComps.dataimp_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>