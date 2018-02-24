<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="即时通讯管理" controllerClazz="nc.scap.pub.messageing.MessageingAppCtrl" defaultWindowId="即时通讯.messageing_listwin" id="pub_messageing" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="即时通讯管理列表List" id="即时通讯.messageing_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="即时通讯管理列表Pop" id="即时通讯.messageing_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_messageingconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="即时通讯.messageing_cardwin" sourceWindow="" target="即时通讯.messageing_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>