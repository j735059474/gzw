<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="自定义项维护" controllerClazz="com.scap.pub.defdoc.DefdocAppCtrl" defaultWindowId="com.scap.pub.defdoc.defdoc_listwin" id="defdoc" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="自定义项维护列表List" id="com.scap.pub.defdoc.defdoc_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="自定义项维护列表Pop" id="com.scap.pub.defdoc.defdoc_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="defdocconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="com.scap.pub.defdoc.defdoc_cardwin" sourceWindow="" target="com.scap.pub.defdoc.defdoc_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>