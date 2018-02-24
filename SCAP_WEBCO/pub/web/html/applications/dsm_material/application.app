<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="资料维护" controllerClazz="nc.scap.dsm.material.MaterialAppController" defaultWindowId="materialuiComps.material_listwin" id="dsm_material" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="资料维护列表" id="materialuiComps.material_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="资料维护卡片" id="materialuiComps.material_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="dsm_materialconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="materialuiComps.material_cardwin" sourceWindow="" target="materialuiComps.material_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>