<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="产权转让信息统计" controllerClazz="nc.scap.transfer.infocount.InfocountAppController" defaultWindowId="infoComps.infocount_listwin" id="transfer_infocount" sourcePackage="transfer/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="产权转让信息统计列表" id="infoComps.infocount_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权转让信息统计卡片" id="infoComps.infocount_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="transfer_infocountconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="infoComps.infocount_cardwin" sourceWindow="" target="infoComps.infocount_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>