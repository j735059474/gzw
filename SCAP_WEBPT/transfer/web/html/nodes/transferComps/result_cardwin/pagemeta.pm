<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="挂牌转让转让卡片" controllerClazz="nc.scap.transfer.trs.TrsCardWinMajorAgreeViewCtr" id="trsmajoragree_cardwin" sourcePackage="transfer/src/public/" windowType="win">
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
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="transferComps.trs_cardwin" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>