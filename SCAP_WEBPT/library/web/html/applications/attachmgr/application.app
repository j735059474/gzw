<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="产权附件规则定义" controllerClazz="nc.scap.library.attachmgr.AttachmgrAppController" defaultWindowId="libraryComps.attachmgr_listwin" id="attachmgr" sourcePackage="library/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondly.MasterSecondlyFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="产权附件规则定义列表" id="libraryComps.attachmgr_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="产权附件规则定义卡片" id="libraryComps.attachmgr_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="attachmgrconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="libraryComps.attachmgr_cardwin" sourceWindow="" target="libraryComps.attachmgr_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>