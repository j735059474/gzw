<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="统一通知下发" controllerClazz="nc.scap.pub.notice.NoticeAppController" defaultWindowId="nc.scap.pub.notice.notice_manager_listwin" id="pub_notice_manager" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="统一通知下发列表" id="nc.scap.pub.notice.notice_manager_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="统一通知下发卡片" id="nc.scap.pub.notice.notice_manager_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_notice_managerconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.notice.notice_manager_cardwin" sourceWindow="" target="nc.scap.pub.notice.notice_manager_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>