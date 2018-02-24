<#if USER_MENU??>
<#assign menuitems =  USER_MENU.items>
<#list menuitems as menuitem>
	<#assign pageUrl = env().web+"/pt/home/view?pageModule="+menuitem.module+"&pageName="+menuitem.id >
	<#assign itemClass = "" >
	<#assign itemTarget = "" >
	<#assign cursor= "" >
	<#if menuitem.visibility ?? && menuitem.visibility==1>
		<#assign pageUrl = "javascript:void(0)" >
		<#assign cursor= "style='cursor:default;'" >
		<#else>
		<#if menuitem.keepstate ><#assign itemTarget = "target='_blank'" ></#if>
	</#if>

	<#if menuitem.visibility ?? && menuitem.visibility==2 && !(menuitem.id == currPage.pagename&&menuitem.module==currPage.module)>
	<#else>
	  	  <li id="${menuitem.module}_${menuitem.id}" <#if menuitem.linkgroup ??>linkgroup="${menuitem.linkgroup}"</#if> <#if menuitem.id == currPage.pagename&&menuitem.module==currPage.module> class="current"  </#if>><a class="mi_l"></a><a href="${pageUrl}" ${itemTarget} ${cursor}><span>${menuitem.title}</span></a><a class="mi_r"></a> <#if menuitem_index<menuitems?size-1><span class="mi_f mi_f_${env().THEME_ID}"></span></#if></li>
	</#if>
</#list>
</#if>