<#ftl output_format="plainText" strip_text="true">
<#assign documentName = documents[0].name>
<#assign workbook = ExcelTool.parse(documents[0])>
<#assign date = .now?iso_utc>

<#compress>
    # ${documentName}
    <@writeSheets workbook/>
</#compress>
${'\n'}

<#--------------------------------------------------------------------------->
<#-- writeSheets                                                           -->
<#--------------------------------------------------------------------------->
<#macro writeSheets workbook>
    <#assign sheets = ExcelTool.getSheets(workbook)>
    <#list sheets as sheet>
        <@writeSheet sheet/>
    </#list>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeSheet                                                            -->
<#--------------------------------------------------------------------------->
<#macro writeSheet sheet>
    <#assign rows = ExcelTool.parseSheet(sheet)>
    ## ${sheet.getSheetName()}
    ${'\n'}
    <@writeRows rows/>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeRows                                                             -->
<#--------------------------------------------------------------------------->
<#macro writeRows rows>
    <#list rows as row>
        <#if row?is_first>
            <@writeHeaderRow row/>
        <#else>
            <@writeDataRow row/>
        </#if>
    </#list>
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeHeaderRow                                                        -->
<#--------------------------------------------------------------------------->
<#macro writeHeaderRow headers>
    | ${headers?join(" | ", "", " |")}
    <#list headers as header>| -------- </#list>|
</#macro>

<#--------------------------------------------------------------------------->
<#-- writeDataRow                                                          -->
<#--------------------------------------------------------------------------->
<#macro writeDataRow columns>
    | ${columns?join(" | ", "", " |")}
</#macro>
