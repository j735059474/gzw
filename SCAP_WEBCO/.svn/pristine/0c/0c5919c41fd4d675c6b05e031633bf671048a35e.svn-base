<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="短信管理" controllerClazz="nc.scap.pub.sms.comps.SmsManageAppController" defaultWindowId="smsManageComps.smsManage_listwin" id="smsManage" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="短信管理列表" id="smsManageComps.smsManage_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="短信管理卡片" id="smsManageComps.smsManage_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="smsManageconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="smsManageComps.smsManage_cardwin" sourceWindow="" target="smsManageComps.smsManage_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>