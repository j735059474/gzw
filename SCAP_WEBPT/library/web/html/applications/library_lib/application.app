<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="产权交易库" controllerClazz="nc.scap.library.lib.LibAppController" defaultWindowId="libraryComps.lib_listwin" id="library_lib" sourcePackage="library/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="产权交易库列表" id="libraryComps.lib_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权交易库卡片" id="libraryComps.lib_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="library_libconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="libraryComps.lib_cardwin" sourceWindow="" target="libraryComps.lib_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>