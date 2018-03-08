<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="产权转让" controllerClazz="nc.scap.transfer.trs.TrsAppController" defaultWindowId="transferComps.trs_listwin" id="transfer_trs" sourcePackage="transfer/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="产权转让列表" id="transferComps.trs_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权转让卡片" id="transferComps.trs_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="重大资产挂牌转让卡片" id="transferComps.trsmajorlist_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="重大资产挂牌转让卡片" id="transferComps.trsmajoragree_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="重大资产挂牌转让卡片" id="transferComps.trsmajorlease_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="挂牌让卡片" id="transferComps.trs_gpzr_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="协议转让卡片" id="transferComps.trs_xyzr_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="无偿转让卡片" id="transferComps.trs_wczr_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="增资卡片" id="transferComps.capitalIncrease_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="增资卡片" id="transferComps.capitalreduce_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="企业并购" id="transferComps.commerger_cardwin" isCanFreeDesign="true">
        </PageMeta>
        
        <PageMeta caption="交易结果" id="transferComps.result_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="transfer_trsconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="transfer_capitalIncreaseconn" pluginId="refreshProxy" plugoutId="capitalIncrease_proxyAfterSavePlugout" source="transferComps.capitalIncrease_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="trs_gpzr" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_gpzr_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="trs_xyzr" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_xyzr_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="trs_wczr" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_wczr_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="0bc65ae9-c30f-424e-83e3-9279f4143b1a" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trsmajorlist_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="203ac759-457c-4362-a174-3e386ad53fb0" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trsmajoragree_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="070a6ece-51be-4888-a30a-4a4c85ff0c83" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trsmajorlease_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="bd746156-ae02-47c6-ade4-a5b672fbc92c" pluginId="refreshProxy" plugoutId="capitalIncrease_proxyAfterSavePlugout" source="transferComps.capitalreduce_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="835c2544-a368-41a7-af5a-76f304ab4116" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.commerger_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
        
        <Connector connType="1" id="trs_result" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.result_cardwin" sourceWindow="" target="transferComps.trs_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>