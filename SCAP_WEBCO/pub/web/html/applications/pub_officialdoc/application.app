<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="公文管理" controllerClazz="nc.scap.pub.officialdoc.OfficialdocAppController" defaultWindowId="nc.scap.pub.officialdoc.officialdoc_listwin" id="pub_officialdoc" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="公文管理列表" id="nc.scap.pub.officialdoc.officialdoc_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="公文管理卡片" id="nc.scap.pub.officialdoc.officialdoc_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_officialdocconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.officialdoc.officialdoc_cardwin" sourceWindow="" target="nc.scap.pub.officialdoc.officialdoc_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>