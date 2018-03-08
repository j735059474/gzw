<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="省属企业交易导入" controllerClazz="nc.scap.library.transfer.export.ExportAppCtrl" defaultWindowId="dataimpComps.export_listwin" id="transfer_export" sourcePackage="transfer/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="省属企业交易导入列表" id="dataimpComps.export_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="省属企业交易导入卡片" id="dataimpComps.export_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权交易申请列表" id="nc.scap.transfer.applyComps.apply_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权交易申请卡片" id="nc.scap.transfer.applyComps.apply_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="省属企业交易导入列表" id="dataimpComps.export_listwin_compare" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="transfer_exportconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="dataimpComps.export_cardwin" sourceWindow="" target="dataimpComps.export_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="transfer_exportconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="dataimpComps.export_cardwin" sourceWindow="" target="dataimpComps.export_listwin_compare" targetWindow="">
        </Connector>
    </Connectors>
</Application>