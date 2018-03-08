<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="不同比例增资卡片" controllerClazz="nc.scap.transfer.trs.CapitalIncreaseCardWinCtr" id="capitalIncrease_cardwin" sourcePackage="transfer/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_simpleexetask" refId="../uap.lfw.wfm.simpleapprove.pubview_simpleexetask">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_approveeexetask" refId="../uap.lfw.wfm.approve.pubview_approveeexetask">
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
         <PlugoutProxy delegatedViewId="main" id="capitalIncrease_proxyAfterSavePlugout">
        </PlugoutProxy>
    </PlugoutDescs>
	<Connectors>
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="capitalIncrease_proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="transferComps.capitalIncrease_cardwin" targetWindow="">
        </Connector>
        <Connector connType="2" id="simplepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_simpleexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="approvepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_approveeexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>