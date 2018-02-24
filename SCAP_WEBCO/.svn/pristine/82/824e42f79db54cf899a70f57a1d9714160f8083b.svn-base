<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="常用联系人" controllerClazz="nc.scap.pub.contacts.ContactsAppController" defaultWindowId="nc.scap.pub.contacts.contacts_listwin" id="pub_contacts" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="常用联系人列表" id="nc.scap.pub.contacts.contacts_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="常用联系人卡片" id="nc.scap.pub.contacts.contacts_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_contactsconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.contacts.contacts_cardwin" sourceWindow="" target="nc.scap.pub.contacts.contacts_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>