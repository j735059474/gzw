<?xml version="1.0" encoding='UTF-8'?>
<PageMeta i18nName="" caption="产权转让列表"  controllerClazz="nc.scap.transfer.trs.TrsListWinCtrl"  id="trs_listwin" sourcePackage="transfer/src/public/" windowType="win">
     <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget canFreeDesign="true" id="main" refId="main">
        </Widget>
        <Widget canFreeDesign="false" id="simplequery" refId="../uap.lfw.imp.query.pubview_simplequery">
        </Widget>
        <Widget canFreeDesign="false" id="orgtree" refId="../tree.pubview_orgtree">
        </Widget>
    </Widgets>
    <PlugoutDescs>
    </PlugoutDescs>
    <PluginDescs>
        <PluginProxy delegatedViewId="main" id="refreshProxy">
        </PluginProxy>
    </PluginDescs>
    <Connectors>
    	<Connector connType="" id="orgtree_to_main" pluginId="orgtree_plugin" plugoutId="orgtreeout" source="orgtree" sourceWindow="" target="main" targetWindow="">
        </Connector>  
        <Connector connType="8" id="proxyin_refreshProxy_refresh_plugin" pluginId="refresh_plugin" plugoutId="refreshProxy" source="transferComps.trs_listwin" sourceWindow="" target="main" targetWindow="">
        </Connector>
        <Connector connType="2" id="simpleQueryConnListView" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" sourceWindow="" target="main" targetWindow="">
        </Connector>       
    </Connectors>
</PageMeta>