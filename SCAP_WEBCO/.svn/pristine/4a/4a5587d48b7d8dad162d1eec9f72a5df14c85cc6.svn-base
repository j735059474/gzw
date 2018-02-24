<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="工作报告" controllerClazz="nc.scap.pub.workreport.WorkreportAppController" defaultWindowId="workreportComp.workreport_listwin" id="pub_workreport" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="工作报告列表" id="workreportComp.workreport_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="工作报告卡片" id="workreportComp.workreport_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_workreportconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="workreportComp.workreport_cardwin" sourceWindow="" target="workreportComp.workreport_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>