<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="会议管理" controllerClazz="nc.scap.pub.meeting.MeetingAppController" defaultWindowId="nc.scap.pub.Meeting.meeting_listwin" id="meeting" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="会议管理列表" id="nc.scap.pub.Meeting.meeting_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="会议管理卡片" id="nc.scap.pub.Meeting.meeting_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="meetingconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.Meeting.meeting_cardwin" sourceWindow="" target="nc.scap.pub.Meeting.meeting_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>