<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="催报通知" controllerClazz="nc.scap.pub.urge.UrgeAppController" defaultWindowId="nc.scap.pub.urge.urge_notice_listwin" id="pub_urge_notice" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="催报通知列表" id="nc.scap.pub.urge.urge_notice_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="催报通知卡片" id="nc.scap.pub.urge.urge_notice_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_urge_noticeconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.urge.urge_notice_cardwin" sourceWindow="" target="nc.scap.pub.urge.urge_notice_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>