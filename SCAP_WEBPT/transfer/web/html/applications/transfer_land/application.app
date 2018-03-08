<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="土地占有简况" controllerClazz="nc.scap.transfer.land.LandAppCtrl" defaultWindowId="transferComps.land_listwin" id="transfer_land" sourcePackage="transfer/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="土地占有简况列表" id="transferComps.land_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="土地占有简况卡片" id="transferComps.land_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="transfer_landconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.land_cardwin" sourceWindow="" target="transferComps.land_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>