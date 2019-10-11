<#ftl output_format="plainText">
<#assign map = YamlTool.parse(documents[0])>
<#--------------------------------------------------------------------------->
<#compress>
<@print map 1/>
</#compress>

<#macro print object level>
    <#if object?is_sequence>
        <#list object as value>
            <@print value level/>
        </#list>
    <#elseif object?is_hash>
        <#list object as key, value>
            ${level} ${key} : <@printValue value level/>
        </#list>
    </#if>
</#macro>

<#macro printValue value level>
        <#if value?is_sequence>
            ${'\n'}<@print value level+1/>
        <#elseif value?is_hash>
            ${'\n'}<@print value level+1/>
        <#elseif value?is_number>
            ${value?c}
        <#else>
            ${value}
        </#if>
</#macro>
