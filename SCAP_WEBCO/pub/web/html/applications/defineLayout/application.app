<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="定义分析主题" controllerClazz="com.scap.pub.defineLayout.DefineLayoutAppController" defaultWindowId="com.scap.pub.chartComps.defineLayout_listwin" id="defineLayout" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="定义分析主题列表List" id="com.scap.pub.chartComps.defineLayout_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="定义分析主题列表Pop" id="com.scap.pub.chartComps.defineLayout_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="defineLayoutconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="com.scap.pub.chartComps.defineLayout_cardwin" sourceWindow="" target="com.scap.pub.chartComps.defineLayout_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>