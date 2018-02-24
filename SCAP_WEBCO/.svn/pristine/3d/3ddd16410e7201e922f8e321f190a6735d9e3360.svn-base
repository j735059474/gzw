<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="企业事项卡片" controllerClazz="nc.scap.pub.entmatter.EntmatterCardWinCtrl" id="entmatter_cardwin" sourcePackage="pub/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_simpleexetask" refId="../uap.lfw.wfm.simpleapprove.pubview_simpleexetask">
        </Widget>
        <Widget canFreeDesign="false" id="pubview_approveeexetask" refId="../uap.lfw.wfm.approve.pubview_approveeexetask">
        </Widget>
        <Widget canFreeDesign="true" id="attachlist" refId="../com.scap.pub.fileComps.attachlist">
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
        <Connector connType="7" id="proxyout_proxyAfterSavePlugout_afterSavePlugout" pluginId="proxyAfterSavePlugout" plugoutId="afterSavePlugout" source="main" sourceWindow="" target="entmatterComps.entmatter_cardwin" targetWindow="">
        </Connector>
        <Connector connType="2" id="simplepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_simpleexetask" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="approvepubview_exetask_to_main" pluginId="plugin_exetask" plugoutId="plugout_exetask" source="pubview_approveeexetask" sourceWindow="" target="main" targetWindow="">
        </Connector> 
        <Connector connType="" id="main_to_attch" pluginId="loadAttach" plugoutId="plugout_winpopattchlist" source="main" sourceWindow="" target="attachlist" targetWindow="">
        </Connector>
    </Connectors>
</PageMeta>