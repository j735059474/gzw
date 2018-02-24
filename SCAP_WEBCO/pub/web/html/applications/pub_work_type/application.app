<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="工作报告类型" controllerClazz="nc.scap.pub.work.WorkAppCtrl" defaultWindowId="nc.scap.pub.notice.work_type_listwin" id="pub_work_type" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="工作报告类型列表" id="nc.scap.pub.notice.work_type_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="工作报告类型卡片" id="nc.scap.pub.notice.work_type_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_work_typeconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.notice.work_type_cardwin" sourceWindow="" target="nc.scap.pub.notice.work_type_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>