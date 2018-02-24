<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="企业事项" controllerClazz="nc.scap.pub.entmatter.EntmatterAppController" defaultWindowId="entmatterComps.entmatter_listwin" id="pub_entmatter" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="企业事项列表" id="entmatterComps.entmatter_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="企业事项卡片" id="entmatterComps.entmatter_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_entmatterconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="entmatterComps.entmatter_cardwin" sourceWindow="" target="entmatterComps.entmatter_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>