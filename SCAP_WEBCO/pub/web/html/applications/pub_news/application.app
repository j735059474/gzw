<?xml version="1.0" encoding='UTF-8'?>
<!DOCTYPE Application PUBLIC "-//Yonyou Co., Ltd.//UAP LFW APPLICATION 6.3//EN" "http://uap.yonyou.com/dtd/uap_lfw_app_6_3.dtd">
<Application TagName="Application" caption="首页新闻管理" controllerClazz="nc.scap.pub.news.NewsAppController" defaultWindowId="nc.scap.pub.news.uiComps.news_listwin" id="pub_news" sourcePackage="pub/src/public/">
    <Attributes>
        <Attribute>
            <Key>templateType</Key>
            <Value>nc.uap.lfw.template.mastersecondlyflow.MasterSecondlyFlowFactory</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <PageMetas>
        <PageMeta caption="首页新闻管理列表" id="nc.scap.pub.news.uiComps.news_listwin" isCanFreeDesign="true">
        </PageMeta>
        <PageMeta caption="首页新闻管理卡片" id="nc.scap.pub.news.uiComps.news_cardwin" isCanFreeDesign="true">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector connType="1" id="pub_newsconn" pluginId="refreshProxy" plugoutId="proxyAfterSavePlugout" source="nc.scap.pub.news.uiComps.news_cardwin" sourceWindow="" target="nc.scap.pub.news.uiComps.news_listwin" targetWindow="">
        </Connector>
    </Connectors>
</Application>