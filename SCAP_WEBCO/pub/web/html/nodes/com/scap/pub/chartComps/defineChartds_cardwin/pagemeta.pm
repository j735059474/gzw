<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="定义数据源列表Pop" controllerClazz="com.scap.pub.defineChartds.DefineChartdsCardWinCtrl" id="defineChartds_cardwin" sourcePackage="pub/src/public/">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
     <Widgets>
       <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>CARD_WINDOW</Key>
            <Value>true</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
     <PlugoutDescs>
         <PlugoutProxy delegatedViewId="main" id="proxyAfterSavePlugout">
        </PlugoutProxy>
    </PlugoutDescs>
    <Connectors>
	     <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="com.scap.pub.chartComps.defineChartds_cardwin" targetWindow="">
        </Connector>
    </Connectors>
   
    <Events>
    </Events>
</PageMeta>