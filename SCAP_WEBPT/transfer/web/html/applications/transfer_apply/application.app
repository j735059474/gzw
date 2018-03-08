<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="产权交易申请" controllerClazz="nc.scap.transfer.apply.ApplyAppController" defaultWindowId="nc.scap.transfer.applyComps.apply_listwin" id="transfer_apply" sourcePackage="transfer/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="产权交易申请列表" id="nc.scap.transfer.applyComps.apply_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权交易申请卡片" id="nc.scap.transfer.applyComps.apply_cardwin" isCanFreeDesign="true">
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
         <PageMeta caption="产权置换" id="transferComps.trs_czzh_cardwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="土地占有简况卡片" id="transferComps.land_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="transfer_applyconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.transfer.applyComps.apply_cardwin" sourceWindow="" target="nc.scap.transfer.applyComps.apply_listwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="trs_gpzr" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_gpzr_cardwin" sourceWindow="" target="nc.scap.transfer.applyComps.apply_cardwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="trs_xyzr" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_xyzr_cardwin" sourceWindow="" target="nc.scap.transfer.applyComps.apply_cardwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="trs_wczr" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.trs_wczr_cardwin" sourceWindow="" target="nc.scap.transfer.applyComps.apply_cardwin" targetWindow="">
        </Connector>
        <Connector connType="1" id="land" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="transferComps.land_cardwin" sourceWindow="" target="nc.scap.transfer.applyComps.apply_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>