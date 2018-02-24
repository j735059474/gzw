<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="组织维护" controllerClazz="nc.scap.pub.visualOrganization.VisualOrganizationAppController" defaultWindowId="visualOrganization.visualOrganization_listwin" id="pub_visualOrganization" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="组织维护列表" id="visualOrganization.visualOrganization_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="组织维护卡片" id="visualOrganization.visualOrganization_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_visualOrganizationconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="visualOrganization.visualOrganization_cardwin" sourceWindow="" target="visualOrganization.visualOrganization_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>